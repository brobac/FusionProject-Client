package controller;

import infra.database.option.lecture.LectureDepartmentOption;
import infra.database.option.lecture.LectureNameOption;
import infra.database.option.lecture.LectureOption;
import infra.database.option.lecture.YearOption;
import infra.dto.*;
import network.ProfProtocolService;
import network.Protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProfessorService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private InputStream is;
    private OutputStream os;
    private AccountDTO account;
    private ProfProtocolService ps;

    public ProfessorService(AccountDTO account, InputStream is, OutputStream os) {
        this.account = account;
        this.is = is;
        this.os = os;
        ps = new ProfProtocolService(is, os);
    }


    public void run() throws Exception {
        int menu = 0;
        while (true) {
            System.out.println(Message.PROFESSOR_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());
            switch (menu) {
                case 1:         // 개인정보 수정
                    personalInformation();
                    break;
                case 2:         // 개설 교과목 메뉴
                    lectureMenu();
                    break;
                case 3:         // 교과목 시간표 조회
                    timeTableLookup();
                    break;
                case 4:
                    logout();     // 로그아웃
                    return;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    break;

            }
        }
    }

    // 로그아웃 요청
    private void logout() throws Exception {
        ps.requestLogout();
        Protocol receiveProtocol = ps.response();
    }

    // 개인정보 수정
    private void personalInformation() throws Exception {
        // 변경 기능
        int menu = 0;
        while (true) {
            // 클라이언트 측에서 가지고 있는 accountDTO의 memberID로 ProfessorDTO생성
            ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
            // professorDTO를 패킷에 담아 서버에게 개인정보 조회 요청
            ps.requestReadPersonalInfo(professorDTO);  

            Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
            if (receiveProtocol != null) {   // 조회 성공
                professorDTO = (ProfessorDTO) receiveProtocol.getObject();
                System.out.println("**********개인정보**********");
                System.out.println("이름 : " + professorDTO.getName());
                System.out.println("학과 : " + professorDTO.getDepartment());
                System.out.println("생년월일 : " + professorDTO.getBirthDate());
                System.out.println("전화번호 : " + professorDTO.getTelePhone());
                System.out.println("교원번호 : " + professorDTO.getProfessorCode());
                System.out.println("***************************");
            } else {                                    // 조회 실패
                System.out.println(Message.LOOKUP_PERSONAL_INFORMATION_FAIL);
                return;
            }

            // 개인정보 수정 메뉴
            System.out.println(Message.UPDATE_PROFESSOR_INFORMATION_MENU);
            menu = Integer.parseInt(scanner.nextLine());

            if (menu == 1) {    //이름 변경
                System.out.print(Message.CHANGE_NAME_INPUT);
                String name = scanner.nextLine();
                professorDTO.setName(name);
                ps.requestUpdatePersonalInfo(professorDTO);   // 개인정보 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null) {
                    System.out.println(Message.UPDATE_NAME_SUCCESS);
                } else {
                    System.out.println(Message.UPDATE_NAME_FAIL);
                }
                
            } else if (menu == 2) {     // 전화번호 변경
                System.out.print(Message.CHANGE_PHONE_NUMBER_INPUT);
                String newPhoneNum = scanner.nextLine();
                professorDTO.setTelePhone(newPhoneNum);

                ps.requestUpdatePersonalInfo(professorDTO);   // 개인정보 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null) {
                    System.out.println(Message.UPDATE_PHONENUM_SUCCESS);
                } else { // 변경 실패 시
                    System.out.println(Message.UPDATE_PHONENUM_FAIL);
                }

            } else if (menu == 3) {     // 비밀번호 변경
                System.out.print(Message.CHANGE_PASSWORD_INPUT);
                String newPassword = scanner.nextLine();

                AccountDTO newAccount = new AccountDTO(account);
                newAccount.setPassword(newPassword);

                ps.requestUpdateAccount(newAccount);  // 비밀번호 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null)
                    System.out.println(Message.UPDATE_PASSWORD_SUCCESS);
                else // 변경 실패 시
                    System.out.println(Message.UPDATE_PASSWORD_FAIL);

            } else if (menu == 4) {   // 나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);  //잘못된 입력 알림
            }
        }
    }

    //  개설교과목 메뉴
    private void lectureMenu() throws Exception {
        int menu = 0;
        while (true) {
            System.out.println(Message.PROFESSOR_SERVICE_LECTURE_MENU);
            menu = Integer.parseInt(scanner.nextLine());

            if (menu == 1) {            //개설 교과목 조회
                lectureLookup();
            } else if (menu == 2) {     //담당 교과목 조회
                lookupMyLecture();
            } else if (menu == 3) {      //나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
            }
        }
    }

    // 개설 교과목 조회
    private void lectureLookup() throws Exception {
        int menu = 0;
        
        while (menu != 3) {
            System.out.println(Message.LECTURE_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());

            // 전체 조회
            if (menu == 1) {
                ps.requestAllLectureList();  // 개설 교과목 (전체) 목록 요청
                Protocol receiveProtocol = ps.response();   // 서버에게 응답 받음
                LectureDTO[] lectureList = null;
                
                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                    printLectureList(lectureList);  // 개설교과목 목록 출력
                }else{
                    // 개설교과목이 존재하지 않는 경우
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

                    // 강의계획서 조회
                    if (innerMenu == 1) {
                        System.out.print(Message.PLANNER_LOOKUP_INPUT);
                        int lectureNum = Integer.parseInt(scanner.nextLine()); // 조회할 교과목 번호 입력
                        // 해당 개설교과목의 강의계획서 조회
                        LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                        System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                        System.out.println("강의 목표 : " + planner.getGoal());
                        System.out.println("강의 개요 : " + planner.getSummary());
                    } else if (innerMenu == 2) {
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
                    }
                }

                // 조건부 조회
            } else if (menu == 2) {
                List<LectureOption> optionList = new ArrayList<>();
                System.out.println("-----조건설정-----");
                int optionMenu = 0;
                while (optionMenu != 4) {
                    System.out.println("[1]대상학년  [2]학과  [3]과목명  [4]조회하기");
                    optionMenu = Integer.parseInt(scanner.nextLine());

                    if (optionMenu == 1) {      // 대상학년 옵션 설정
                        System.out.print(Message.TARGET_GRADE_INPUT);
                        int year = Integer.parseInt(scanner.nextLine());
                        optionList.add(new YearOption(year));
                    } else if (optionMenu == 2) {       // 학과 옵션 설정
                        System.out.print(Message.DEPARTMENT_INPUT);
                        String department = scanner.nextLine();
                        optionList.add(new LectureDepartmentOption(department));
                    } else if (optionMenu == 3) {       // 과목명 설정
                        System.out.print(Message.COURSE_NAME_INPUT);
                        String lectureName = scanner.nextLine();
                        optionList.add(new LectureNameOption(lectureName));
                    } else if (optionMenu == 4) {       // 설정된 옵션으로 조회
                        ps.requestLectureListByOption(
                                optionList.toArray(new LectureOption[optionList.size()])
                        );
                        Protocol receiveProtocol = ps.response();
                        LectureDTO[] lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                        // 개설교과목 출력
                        printLectureList(lectureList);

                        int innerMenu = 0;
                        while (innerMenu != 2) {
                            System.out.println(Message.LECTURE_LOOKUP_INNER_MENU);
                            System.out.print(Message.INPUT);
                            innerMenu = Integer.parseInt(scanner.nextLine());

                            if (innerMenu == 1) {       // 강의계획서 조회
                                System.out.print(Message.PLANNER_LOOKUP_INPUT);
                                int lectureNum = Integer.parseInt(scanner.nextLine());  // 개설교과목 번호 입력
                                // 선택한 개설교과목의 강의계획서 출력
                                LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                                System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                                System.out.println("강의 목표 : " + planner.getGoal());
                                System.out.println("강의 개요 : " + planner.getSummary());
                            } else if (innerMenu == 2) {
                            } else {
                                System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
                            }
                        }
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
                    }
                }
                
                // 나가기
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
            }
        }
    }

    // 개설교과목 목록 출력
    private void printLectureList(LectureDTO[] lectureList) {
        for (int i = 0; i < lectureList.length; i++) {
            LectureDTO curLecture = lectureList[i];
            System.out.printf("[ %d ]", i + 1);
            // 과목명, 대상학년, 과목코드, 제한인원, 신청인원, 학과, 학점, 교수명
            System.out.print("교과목명 : " + curLecture.getCourse().getCourseName());
            System.out.print("학점 : " + curLecture.getCourse().getCredit());
            System.out.print("과목코드 : " + curLecture.getLectureCode());
            System.out.print("담당교수 : " + curLecture.getProfessor().getName());
            System.out.print("수강학과 : " + curLecture.getCourse().getDepartment());
            System.out.print("강의시간(강의실) : "); // 반복문필요
            System.out.print("제한인원 : " + curLecture.getLimit());
            System.out.println("수강인원 : " + curLecture.getApplicant());
        }
    }

    // 담당 교과목 조회
    private void lookupMyLecture() throws Exception {
        int menu = 0;
        while (true) {
            // accountDTO가 가지고 있는 memberID로 ProfessorDTO 생성
            ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
            ps.requestReadPersonalInfo(professorDTO);  // 개인정보 조회 요청
            Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
            // 서버에게 받은 ProfessorDTO
            ProfessorDTO profDTO = (ProfessorDTO) receiveProtocol.getObject();
            // 서버에게 담당교과목 조회 요청
            ps.requestMyLecture(profDTO);
            receiveProtocol = ps.response();
            LectureDTO[] lectureDTOS = (LectureDTO[]) receiveProtocol.getObjectArray();
            // 담당 교과목 목록 출력
            printLectureList(lectureDTOS);

            System.out.println(Message.PROFESSOR_MY_LECTURE_MENU);
            menu = Integer.parseInt(scanner.nextLine());
            
            if (menu == 1) {            // 교과목 수강생 조회
                lookupRegisteringStd(lectureDTOS);
            } else if (menu == 2) {     // 강의계획서 조회
                System.out.print(Message.PLANNER_LOOKUP_INPUT); 
                int lectureNum = Integer.parseInt(scanner.nextLine());

                if (lectureNum - 1 >= lectureDTOS.length) {
                    System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
                    continue;
                }

                LecturePlannerDTO planner = lectureDTOS[lectureNum - 1].getPlanner();
                System.out.println("교과목명 : " + lectureDTOS[lectureNum - 1].getCourse().getCourseName());
                System.out.println("강의 목표 : " + planner.getGoal());
                System.out.println("강의 개요 : " + planner.getSummary());

            } else if (menu == 3) { // 강의계획서 입력
                updateLecturePlanner(lectureDTOS);
                
            } else if (menu == 4) { // 나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
            }
        }
    }

    // 강의계획서 입력
    private void updateLecturePlanner(LectureDTO[] lectureDTOS) throws Exception {
        System.out.println(Message.LECTURE_PLANNER_UPDATE_INPUT);
        int lectureNum = Integer.parseInt(scanner.nextLine());  // 강의게획서를 수정할 교과목 번호 입력

        if (lectureNum - 1 >= lectureDTOS.length) {
            System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
            return;
        }

        LectureDTO selectedLecture = lectureDTOS[lectureNum - 1];

        while (true) {
            System.out.println(Message.LECTURE_PLANNER_CATEGORIES);
            System.out.print(Message.LECTURE_PLANNER_CATEGORIES_INPUT);
            int select = Integer.parseInt(scanner.nextLine());

            if (select == 1) {                  // 강의목표 입력
                System.out.println(Message.LECTURE_PLANNER_GOAL_INPUT);
                String goal = scanner.nextLine();
                selectedLecture.getPlanner().setGoal(goal);
            } else if (select == 2) {            // 강의 개요 입력
                System.out.println(Message.LECTURE_PLANNER_SUMMARY_INPUT);
                String summary = scanner.nextLine();
                selectedLecture.getPlanner().setSummary(summary);
                } else if (select == 3) {       // 완료
                    break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
            }
        }
        // 개설교과목(강의계획서를 담은) 수정 요청
        ps.requestUpdateLecture(selectedLecture);
        Protocol receiveProtocol = ps.response();   //성공/실패 응답
    }

    // 교과목 수강생 조회
    private void lookupRegisteringStd(LectureDTO[] lectureDTOS) throws Exception {
        System.out.println(Message.PLANNER_LOOKUP_INPUT);
        int lectureNum = Integer.parseInt(scanner.nextLine());  // 조회할 교과목 선택

        if (lectureNum - 1 >= lectureDTOS.length) {
            System.out.println(Message.WRONG_INPUT_NOTICE);   //잘못된 입력 알림
            return;
        }

        // 교과목 수강생 조회 요청
        ps.requestReadRegisteringStd(lectureDTOS[lectureNum - 1]);
        Protocol receiveProtocol = ps.response();
        StudentDTO[] studentDTOS = (StudentDTO[]) receiveProtocol.getObjectArray();

        for (StudentDTO std : studentDTOS) {
            System.out.print("학과 : " + std.getDepartment());
            System.out.print("학년 : " + std.getYear());
            System.out.print("이름 : " + std.getName());
            System.out.print("학번 : " + std.getStudentCode());
            System.out.print("생년월일 : " + std.getBirthDate());
            System.out.print("최대수강 가능학점 : " + std.getMaxCredit());
            System.out.print("현재 수강 학점 : " + std.getCredit());
        }
    }

    // 시간표 조회
    private void timeTableLookup() throws Exception {
        final int NUM_OF_PERIOD = 9;
        final int NUM_OF_DAY = 5;
        final int MON = 0;
        final int TUE = 1;
        final int WED = 2;
        final int THU = 3;
        final int FRI = 4;

        ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(professorDTO);  // 개인정보 조회 요청

        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
        professorDTO = (ProfessorDTO) receiveProtocol.getObject();

        LectureTimeDTO[] lectureTimeDTOS = professorDTO.getTimeTable();
        LectureTimeDTO[][] timeTable = new LectureTimeDTO[NUM_OF_PERIOD][NUM_OF_DAY];
        String[] days = {"MON", "TUE", "WED", "THU", "FRI"};

        //시간표 채우기
        if (lectureTimeDTOS != null) {
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
                    timeTable[i - 1][day] = dto;
                }
            }
        }

        //시간표 헤더 채우기
        for (int i = 0; i < NUM_OF_DAY; i++) {
            if (i == 0) {
                System.out.printf("%10s%10s", "", " |");
            }
            System.out.printf("%10s%10s", days[i], " |");
        }


        //시간표 출력
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
                            "%" + (10 - timeTable[i][j].getLectureName().length() / 2) + "s%"
                                    + (10 - timeTable[i][j].getLectureName().length() / 2) + "s |",
                            timeTable[i][j].getLectureName(), timeTable[i][j].getRoom());
                }

            }
            System.out.println();
            System.out.println("-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|");
        }

    }
}












