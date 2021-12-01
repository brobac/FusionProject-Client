package controller;

import infra.database.option.lecture.LectureDepartmentOption;
import infra.database.option.lecture.LectureNameOption;
import infra.database.option.lecture.LectureOption;
import infra.database.option.lecture.YearOption;
import infra.dto.*;
import network.Protocol;
import network.StudentProtocolService;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            menu = Integer.parseInt(scanner.nextLine());
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
                    return;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void logout() throws Exception {
        ps.requestLogout();
        Protocol receiveProtocol = ps.response();
        System.out.println(Message.LOGOUT_NOTICE);
    }

    private void personalInformation() throws Exception {

        // 변경 기능
        int menu = 0;
        while (true) {
            StudentDTO studentDTO = StudentDTO.builder().id(account.getMemberID()).build();
            ps.requestReadPersonalInfo(studentDTO);  // 개인정보 조회 요청

            Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
            if (receiveProtocol != null) {   // 조회 성공
                System.out.println("**********개인정보**********");
                studentDTO = (StudentDTO) receiveProtocol.getObject();
                System.out.println("이름 : " + studentDTO.getName());
                System.out.println("학과 : " + studentDTO.getDepartment());
                System.out.println("생년월일 : " + studentDTO.getBirthDate());
                System.out.println("최대수강 가능학점 : " + studentDTO.getMaxCredit());
                System.out.println("현재 수강 학점 : " + studentDTO.getCredit());
                System.out.println("학년 : " + studentDTO.getYear());
                System.out.println("학번 : " + studentDTO.getStudentCode());
                System.out.println("***************************");
            } else {                                    // 조회 실패
                System.out.println(Message.LOOKUP_PERSONAL_INFORMATION_FAIL);
                return;
            }
            System.out.println(Message.UPDATE_STUDENT_INFORMATION_MENU);
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
        RegisteringPeriodDTO[] registeringPeriodDTOs = null;
        if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
            RegisteringPeriodDTO[] registeringPeriodDTOS = (RegisteringPeriodDTO[]) receiveProtocol.getObjectArray();
            for (RegisteringPeriodDTO registeringPeriodDTO : registeringPeriodDTOS) {
                if (registeringPeriodDTO.getAllowedYear() == studentDTO.getYear() && registeringPeriodDTO.getPeriodDTO().getBeginTime().isBefore(LocalDateTime.now()) && registeringPeriodDTO.getPeriodDTO().getEndTime().isAfter(LocalDateTime.now())) {
                    isRegisteringPeriod = true;
                }
            }
        }else{
            System.out.println(Message.NOT_REGISTERING_PERIOD);
            return;
        }

        //수강신청기간 아닐경우 진행 불가
        if (!isRegisteringPeriod) {
            System.out.println(Message.NOT_REGISTERING_PERIOD);
            return;
        }

        int menu = 0;
        while (true) {
            System.out.println(Message.REGISTERING_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());

            if (menu == 1) {       //수강 신청
                ps.requestAllLectureList();  // 개설 교과목 (전체) 목록 요청
                receiveProtocol = ps.response();

                LectureDTO[] lectureList;
                lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                printLectureList(lectureList);

                System.out.print(Message.REGISTERING_INPUT);
                int lectureNum = Integer.parseInt(scanner.nextLine());
                if (lectureNum < 0 || lectureList.length < lectureNum) {
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    return;
                }
                RegisteringDTO registeringDTO = RegisteringDTO.builder()
                        .lectureID(lectureList[lectureNum - 1].getId())
                        .studentCode(studentDTO.getStudentCode())
                        .build();
                ps.requestRegistering(registeringDTO);

                receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println(Message.REGISTERING_SUCCESS);
                } else {
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.println(Message.REGISTERING_FAIL);
                    System.out.println(failMsg);
                }

            } else if (menu == 2) {    //수강 취소   //TODO 조회 하고 취소??
                studentDTO = StudentDTO.builder().id(account.getMemberID()).build();
                ps.requestReadPersonalInfo(studentDTO);  // 개인정보 조회 요청
                receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
                if (receiveProtocol != null) {   // 조회 성공
                    studentDTO = (StudentDTO) receiveProtocol.getObject();
                }

                System.out.println("수강신청현황");
                ps.requestReadRegistering(studentDTO);
                receiveProtocol = ps.response();
                LectureDTO[] registeringList = (LectureDTO[]) receiveProtocol.getObjectArray();
                if (registeringList != null) {
                    printLectureList(registeringList);
                    System.out.print(Message.REGISTERING_CANCEL_INPUT);
                    int cancelNum = Integer.parseInt(scanner.nextLine());
                    for (RegisteringDTO registeringDTO : studentDTO.getMyRegisterings()) {
                        if (registeringDTO.getLectureID() == registeringList[cancelNum - 1].getId()) {
                            ps.requestDeleteRegistering(registeringDTO);
                        }
                    }
                } else {
                    System.out.println(Message.REGISTERING_EMPTY);
                }

                ps.response();
            } else if (menu == 3)
                break;
            else
                System.out.println(Message.WRONG_INPUT_NOTICE);
        }
    }

    private void printLectureList(LectureDTO[] lectureList) {
        for (int i = 0; i < lectureList.length; i++) {
            LectureDTO curLecture = lectureList[i];
            System.out.printf("[ %d ]", i + 1);
            // 과목명, 대상학년, 과목코드, 제한인원, 신청인원, 학과, 학점, 교수명
            System.out.print("교과목명 : " + curLecture.getCourse().getCourseName());
            System.out.print("  학점 : " + curLecture.getCourse().getCredit());
            System.out.print("  과목코드 : " + curLecture.getLectureCode());
            System.out.print("  담당교수 : " + curLecture.getProfessor().getName());
            System.out.print("  수강학과 : " + curLecture.getCourse().getDepartment());
            System.out.print("  강의시간(강의실) : "); // 반복문필요
            System.out.print("  제한인원 : " + curLecture.getLimit());
            System.out.println("  수강인원 : " + curLecture.getApplicant());
        }
    }

    private void lectureLookup() throws Exception {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.LECTURE_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());
            if (menu == 1) {
                ps.requestAllLectureList();  // 개설 교과목 (전체) 목록 요청
                Protocol receiveProtocol = ps.response();
                LectureDTO[] lectureList = null;
                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                    printLectureList(lectureList);
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[개설교과목] : ");
                    System.out.println(failMsg);
                    break;
                }

                int innerMenu = 0;
                while (innerMenu != 2) {
                    System.out.println(Message.LECTURE_LOOKUP_INNER_MENU);
                    System.out.print(Message.INPUT);
                    innerMenu = Integer.parseInt(scanner.nextLine());
                    if (innerMenu == 1) {
                        System.out.print(Message.PLANNER_LOOKUP_INPUT);
                        int lectureNum = Integer.parseInt(scanner.nextLine());
                        LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                        System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                        System.out.println("강의 목표 : " + planner.getGoal());
                        System.out.println("강의 개요 : " + planner.getSummary());
                    } else if (innerMenu == 2) {
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }

            } else if (menu == 2) {
                List<LectureOption> optionList = new ArrayList<>();
                System.out.println("-----조건설정-----");
                int optionMenu = 0;
                while (optionMenu != 4) {
                    System.out.println("[1]대상학년  [2]학과  [3]과목명  [4]조회하기");
                    optionMenu = Integer.parseInt(scanner.nextLine());
                    if (optionMenu == 1) {
                        System.out.print(Message.TARGET_GRADE_INPUT);
                        int year = Integer.parseInt(scanner.nextLine());
                        optionList.add(new YearOption(year));
                    } else if (optionMenu == 2) {
                        System.out.print(Message.DEPARTMENT_INPUT);
                        String department = scanner.nextLine();
                        optionList.add(new LectureDepartmentOption(department));
                    } else if (optionMenu == 3) {
                        System.out.print(Message.COURSE_NAME_INPUT);
                        String lectureName = scanner.nextLine();
                        optionList.add(new LectureNameOption(lectureName));
                    } else if (optionMenu == 4) {
                        ps.requestLectureListByOption(
                                optionList.toArray(new LectureOption[optionList.size()])
                        );
                        Protocol receiveProtocol = ps.response();
                        LectureDTO[] lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                        printLectureList(lectureList);

                        int innerMenu = 0;
                        while (innerMenu != 2) {
                            System.out.println(Message.LECTURE_LOOKUP_INNER_MENU);
                            System.out.print(Message.INPUT);
                            innerMenu = Integer.parseInt(scanner.nextLine());
                            if (innerMenu == 1) {
                                System.out.print(Message.PLANNER_LOOKUP_INPUT);
                                int lectureNum = Integer.parseInt(scanner.nextLine());
                                LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                                System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                                System.out.println("강의 목표 : " + planner.getGoal());
                                System.out.println("강의 개요 : " + planner.getSummary());
                            } else if (innerMenu == 2) {
                            } else {
                                System.out.println(Message.WRONG_INPUT_NOTICE);
                            }
                        }
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }

            } else if (menu == 3) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void timeTableLookup() throws Exception {
        final int NUM_OF_PERIOD = 9;
        final int NUM_OF_DAY = 5;
        final int MON = 0;
        final int TUE = 1;
        final int WED = 2;
        final int THU = 3;
        final int FRI = 4;

        //시간표 출력
        StudentDTO studentDTO = StudentDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(studentDTO);

        Protocol receiveProtocol = ps.response();
        studentDTO = (StudentDTO) receiveProtocol.getObject();

        LectureTimeDTO[] lectureTimeDTOS = studentDTO.getTimeTable();
        String[][] timeTable = new String[NUM_OF_PERIOD][NUM_OF_DAY];
        String[] days = {"MON", "TUE", "WED", "THU", "FRI"};


        if(lectureTimeDTOS==null){
            System.out.println("등록된 강의가 없습니다.");
            return;
        }

        for (LectureTimeDTO dto : lectureTimeDTOS) {
            int day = -1;
            switch (dto.getLectureDay()) {
                case "MON":
                    day = MON;
                    break;
                case "TUE":
                    day = TUE;
                    break;
                case "WED":
                    day = WED;
                    break;
                case "THU":
                    day = THU;
                    break;
                case "FRI":
                    day = FRI;
                    break;
            }

            for (int i = dto.getStartTime(); i <= dto.getEndTime(); i++) {
                timeTable[i - 1][day] = dto.getLectureName();
            }
        }

        for (int i = 0; i < NUM_OF_DAY; i++) {
            if (i == 0) {
                System.out.printf("%10s%10s", "", " |");
            }
            System.out.printf("%10s%10s", days[i], " |");
        }

        System.out.println();
        System.out.println("-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|");

        for (int i = 0; i < NUM_OF_PERIOD; i++) {
            for (int j = 0; j < NUM_OF_DAY; j++) {
                if (j == 0) {
                    System.out.printf("%8s%10s", (i + 1) + "교시", " |");
                }

                if (timeTable[i][j] == null) {
                    System.out.printf("%10s%10s", "", " |");
                } else {
                    System.out.printf(
                            "%" + (10 - timeTable[i][j].length() / 2) + "s%"
                                    + (10 - timeTable[i][j].length() / 2) + "s",
                            timeTable[i][j], " |");
                }

            }
            System.out.println();
            System.out.println("-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|");
        }
    }
}