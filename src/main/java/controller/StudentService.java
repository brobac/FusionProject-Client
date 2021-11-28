package controller;

import infra.dto.*;
import network.Protocol;
import network.StudentProtocolService;
import option.lecture.LectureDepartmentOption;
import option.lecture.LectureNameOption;
import option.lecture.LectureOption;
import option.lecture.LectureTargetYearOption;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class StudentService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private InputStream is;
    private OutputStream os;
    private StudentProtocolService ps;
    private AccountDTO account;

    public StudentService(AccountDTO account, InputStream is, OutputStream os) {
        this.account = account;
        this.is = is;
        this.os = os;
        ps = new StudentProtocolService(is, os);
    }

    public void run() throws Exception {
        int menu = 0;
        while (menu != 5) {
            System.out.println(Message.STUDENT_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            switch (menu) {
                case 1:
                    personalInformation();  // 개인정보 관리
                    break;
                case 2:
                    registering();         // 수강신청
                    break;
                case 3:
                    lectureLookup();       // 개설교과목 조회
                    break;
                case 4:
                    timeTableLookup();      // 시간표 조회
                    break;
                case 5:                     // 로그아웃
                    logout();
                    break;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void logout() throws Exception {
        ps.requestLogout();
        Protocol receiveProtocol = ps.response();
        System.out.println((String) receiveProtocol.getObject());
    }

    private void personalInformation() throws Exception {
        StudentDTO studentDTO = StudentDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(studentDTO);  // 개인정보 조회 요청

        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
        if (receiveProtocol != null) {   // 조회 성공
            studentDTO = (StudentDTO) receiveProtocol.getObject();
            System.out.println("name : " + studentDTO.getName());
            System.out.println("department : " + studentDTO.getDepartment());
            System.out.println("birthDate : " + studentDTO.getBirthDate());
            System.out.println("maxCredit : " + studentDTO.getMaxCredit());
            System.out.println("year : " + studentDTO.getYear());
            System.out.println("studentCode : " + studentDTO.getStudentCode());
        } else {                                    // 조회 실패
            System.out.println(Message.LOOKUP_PERSONAL_INFORMATION_FAIL);
            return;
        }

        // 변경 기능
        int menu = 0;
        while (true) {
            System.out.println(Message.UPDATE_PERSONAL_INFORMATION_MENU);
            menu = Integer.parseInt(scanner.nextLine());

            if (menu == 1) { //이름 변경
                System.out.print(Message.CHANGE_NAME_INPUT);
                String name = scanner.nextLine();
                studentDTO.setName(name);
                ps.requestUpdatePersonalInfo(studentDTO);   // 이름 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null) {
                    System.out.println(Message.UPDATE_NAME_SUCCESS);
                } else {
                    System.out.println(Message.UPDATE_NAME_FAIL);
                }
            } else if (menu == 2) {  //비밀번호 변경
                System.out.print(Message.CHANGE_PASSWORD_INPUT);
                String newPassword = scanner.nextLine();

                AccountDTO newAccount = new AccountDTO(account);
                newAccount.setPassword(newPassword);

                ps.requestUpdateAccount(newAccount);  // 비밀번호 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null)
                    System.out.println(Message.UPDATE_PASSWORD_SUCCESS);
                else
                    System.out.println(Message.UPDATE_PASSWORD_FAIL);

            } else if (menu == 3) {   // 나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void registering() throws Exception {
        boolean isRegisteringPeriod = false;
        StudentDTO studentDTO = StudentDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(studentDTO);  // 개인정보 조회 요청

        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
        if (receiveProtocol != null) {   // 조회 성공
            studentDTO = (StudentDTO) receiveProtocol.getObject();
        }

        //수강신청기간 확인
        ps.requestRegisteringReriod();
        receiveProtocol = ps.response();
        RegisteringPeriodDTO[] registeringPeriodDTOS = (RegisteringPeriodDTO[]) receiveProtocol.getObjectArray();
        for (RegisteringPeriodDTO registeringPeriodDTO : registeringPeriodDTOS) {
            if (registeringPeriodDTO.getAllowedYear() == studentDTO.getYear()) {
                isRegisteringPeriod = true;
            }
        }

        //수강신청기간 아닐경우 진행 불가
        if (!isRegisteringPeriod) {
            System.out.println(Message.NOT_REGISTERING_PERIOD);
            return;
        }

        int menu = 0;
        while (true) {
            System.out.print(Message.REGISTERING_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();

            if (menu == 1) {       //수강 신청
                ps.requestAllLectureList();  // 개설 교과목 (전체) 목록 요청
                receiveProtocol = ps.response();

                LectureDTO[] lectureList;
                lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                printLectureList(lectureList);

                System.out.print(Message.REGISTERING_INPUT);
                int lectureNum = Integer.parseInt(scanner.nextLine());
                RegisteringDTO registeringDTO = RegisteringDTO.builder()
                        .lectureID(lectureList[lectureNum - 1].getId())
                        .studentCode(studentDTO.getStudentCode())
                        .build();
                ps.requestRegistering(registeringDTO);

                receiveProtocol = ps.response();
                // 응답으로 메세지받아서 출력해줘야함
                receiveProtocol.getObject();

            } else if (menu == 2) {    //수강 취소   //TODO 조회 하고 취소??
                System.out.println("수강신청현황");
                ps.requestReadRegistering(studentDTO);
                receiveProtocol = ps.response();
                LectureDTO[] registeringList = (LectureDTO[]) receiveProtocol.getObjectArray();
                printLectureList(registeringList);
                System.out.print(Message.REGISTERING_CANCEL_INPUT);
                int cancelNum = Integer.parseInt(scanner.nextLine());
                for (RegisteringDTO registeringDTO : studentDTO.getMyRegisterings()) {
                    if (registeringDTO.getLectureID() == registeringList[cancelNum - 1].getId()) {
                        ps.requestDeleteRegistering(registeringDTO);
                    }
                }
            } else if (menu == 3)
                break;
            else
                System.out.println(Message.WRONG_INPUT_NOTICE);
        }
    }

    private void printLectureList(LectureDTO[] lectureList) {
        for (int i = 0; i < lectureList.length; i++) {
            System.out.printf("[ %d ]", i + 1);
            // 과목명, 대상학년, 과목코드, 제한인원, 신청인원, 학과, 학점, 교수명
            System.out.print("교과목명 : ");
            System.out.print("학점 : ");
            System.out.print("과목코드 : ");
            System.out.print("담당교수 : ");
            System.out.print("수강학과 : ");
            System.out.print("강의시간(강의실) : "); // 반복문필요
            System.out.print("제한인원 : ");
            System.out.print("수강인원 : ");
        }
    }

    private void lectureLookup() throws Exception {
        ps.requestAllLectureList();  // 개설 교과목 (전체) 목록 요청

        Protocol receiveProtocol = ps.response();
        if (receiveProtocol != null) {
            LectureDTO[] lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
            printLectureList(lectureList);
            System.out.println(Message.LECTURE_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            int menu = 0;
            menu = Integer.parseInt(scanner.nextLine());
            if (menu == 1) {
                LectureOption[] options = new LectureOption[3];
                System.out.println("-----조건설정-----");
                System.out.println("[1]대상학년  [2]학과  [3]과목명  [4]조회하기");
                int optionMenu = 0;
                while (optionMenu != 4) {
                    if (optionMenu == 1) {
                        System.out.print(Message.TARGET_GRADE_INPUT);
                        int year = Integer.parseInt(scanner.nextLine());
                        options[0] = new LectureTargetYearOption(year);
                    } else if (optionMenu == 2) {
                        System.out.print(Message.DEPARTMENT_INPUT);
                        String department = scanner.nextLine();
                        options[1] = new LectureDepartmentOption(department);
                    } else if (optionMenu == 3) {
                        System.out.print(Message.COURSE_NAME_INPUT);
                        String lectureName = scanner.nextLine();
                        options[2] = new LectureNameOption(lectureName);
                    } else if (optionMenu == 4) {
                        ps.requestLectureListByOption(options);
                        receiveProtocol = ps.response();
                        lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                        printLectureList(lectureList);
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }

            } else if (menu == 2) {  //TODO lecturedDTO 수정하고나서 더 만들어야함
                System.out.print(Message.PLANNER_LOOKUP_INPUT);
                int lectureNum = Integer.parseInt(scanner.nextLine());
                LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                System.out.println("강의 목표 : " + planner.getGoal());
                System.out.println("강의 개요 : " + planner.getSummary());
            } else if (menu == 3) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        } else
            System.out.println(Message.LOOKUP_LECTURE_FAIL);
    }

    // TODO 강의계획서 조회랑 시간표 조회는 어떻게 된건지 모르겠어서 일단 놔둠
//    private void lecturePlannerLookup() throws Exception {
//        int menu = 0;
//        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
//
//        while (menu != 2) {
//            System.out.print(Message.LECTURE_PLANNER_LOOKUP_MENU);
//            System.out.print(Message.INPUT);
//            menu = scanner.nextInt();
//
//            if (menu == 1) { //선택 교과목 강의 계획서 조회
//                System.out.print(Message.COURSE_CODE_INPUT);
//                String lectureCode = scanner.nextLine();
//                LectureDTO lectureDTO = LectureDTO.builder().lectureCode(lectureCode).build();
//                sendPt.setObject(lectureDTO);
//                sendPt.setCode(Protocol.T1_CODE_READ);
//                sendPt.setEntity(Protocol.ENTITY_LECTURE);
//                sendPt.send(os);
//
//                Protocol recvPt = read();
//                if (recvPt != null) {
//                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                            lectureDTO = (LectureDTO) recvPt.getObject();
//                            System.out.println(lectureDTO.getPlanner());
//                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                            System.out.println(Message.LOOKUP_LECTURE_PLANNER_FAIL);
//                    }
//                }
//            } else if (menu == 2) {
//
//            } else {
//                System.out.println(Message.WRONG_INPUT_NOTICE);
//            }
//        }
//    }

    private void timeTableLookup() throws Exception {
        //시간표 출력
        StudentDTO studentDTO = StudentDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(studentDTO);

        Protocol receiveProtocol = ps.response();
        studentDTO = (StudentDTO) receiveProtocol.getObject();

        LectureTimeDTO[] lectureTimeDTO = studentDTO.getTimeTable();

        int k = 0;
        String[] day = {"MON", "TUE", "WED", "THU", "FRI"};
        for (int i = 0; i < 8; i++) {
            if (i != 0)
                System.out.print(i + " |");
            else
                System.out.print("\\ |");
            for (int j = 0; j < 5; j++) {
                if (i == 0) {
                    System.out.printf("%10s%10s", day[j], " |");
                } else {
                    if (lectureTimeDTO[k].getLectureDay() == day[j] && (lectureTimeDTO[k].getStartTime() == i || lectureTimeDTO[k].getEndTime() == i)) {
                        System.out.printf("%10s%8s", lectureTimeDTO[k++].getLectureName(), " ");
                    } else {
                        System.out.printf("%10s%10s", "", "");
                    }
                }
            }
            System.out.println();
            System.out.println("--|-------------------|-------------------|-------------------|-------------------|-------------------|");
        }
    }
}


