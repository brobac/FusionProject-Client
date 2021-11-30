package controller;

import infra.database.option.lecture.*;
import infra.dto.*;
import network.ProfProtocolService;
import network.Protocol;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
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
        while (menu != 7) {
            System.out.println(Message.PROFESSOR_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());
            switch (menu) {
                case 1:
                    personalInformation();
                    break;
                case 2:
                    lectureMenu();
                    break;
                case 3:
                    timeTableLookup();
                    break;
                case 4:
                    logout();
                    return;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    break;

            }
        }
    }

    private void logout() throws Exception {
        ps.requestLogout();
        Protocol receiveProtocol = ps.response();
    }

    private void personalInformation() throws Exception {
        ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(professorDTO);  // 개인정보 조회 요청

        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
        if (receiveProtocol != null) {   // 조회 성공
            professorDTO = (ProfessorDTO) receiveProtocol.getObject();
            System.out.println("이름 : " + professorDTO.getName());
            System.out.println("학과 : " + professorDTO.getDepartment());
            System.out.println("생년월일 : " + professorDTO.getBirthDate());
            System.out.println("전화번호 : " + professorDTO.getTelePhone());
            System.out.println("교원번호 : " + professorDTO.getProfessorCode());
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
                professorDTO.setName(name);
                ps.requestUpdatePersonalInfo(professorDTO);   // 이름 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null) {
                    System.out.println(Message.UPDATE_NAME_SUCCESS);
                } else {
                    System.out.println(Message.UPDATE_NAME_FAIL);
                }
            } else if(menu == 2){
                System.out.print(Message.CHANGE_PHONE_NUMBER_INPUT);
                String newPhoneNum = scanner.nextLine();
                professorDTO.setTelePhone(newPhoneNum);

                ps.requestUpdatePersonalInfo(professorDTO);   // 이름 변경 요청

                receiveProtocol = ps.response();
                if (receiveProtocol != null) {
                    System.out.println(Message.UPDATE_NAME_SUCCESS);
                } else {
                    System.out.println(Message.UPDATE_NAME_FAIL);
                }
            } else if (menu == 3) {  //비밀번호 변경
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

            } else if (menu == 4) {   // 나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lectureMenu() throws Exception {
        int menu=0;
        while(true){
            System.out.println(Message.PROFESSOR_SERVICE_LECTURE_MENU);
            menu = Integer.parseInt(scanner.nextLine());

            if(menu == 1){ //개설 교과목 조회
                lectureLookup();
            }else if(menu == 2){ //담당 교과목 조회
                lookupMyLecture();
            }else if(menu == 3){ //나가기
                break;
            }else{
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
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

            } else if (menu == 2) {
                LectureOption[] options = new LectureOption[3];
                System.out.println("-----조건설정-----");
                int optionMenu = 0;
                while (optionMenu != 4) {
                    System.out.println("[1]대상학년  [2]학과  [3]과목명  [4]조회하기");
                    optionMenu = Integer.parseInt(scanner.nextLine());
                    if (optionMenu == 1) {
                        System.out.print(Message.TARGET_GRADE_INPUT);
                        int year = Integer.parseInt(scanner.nextLine());
                        options[0] = new YearOption(year);
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

    private void lookupMyLecture() throws Exception {
        int menu = 0;
        while(true){
            ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
            ps.requestReadPersonalInfo(professorDTO);  // 개인정보 조회 요청
            Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
            ProfessorDTO profDTO = (ProfessorDTO) receiveProtocol.getObject();

            ps.requestMyLecture(profDTO);
            receiveProtocol = ps.response();
            LectureDTO[] lectureDTOS = (LectureDTO[]) receiveProtocol.getObjectArray();
            printLectureList(lectureDTOS);

            System.out.println(Message.PROFESSOR_MY_LECTURE_MENU);
            menu = Integer.parseInt(scanner.nextLine());
            if(menu == 1){ // 교과목 수강생 조회
                lookupRegisteringStd(lectureDTOS);
            }else if(menu == 2){ // 강의계획서 조회
                System.out.print(Message.PLANNER_LOOKUP_INPUT);
                int lectureNum = Integer.parseInt(scanner.nextLine());

                if(lectureNum-1>=lectureDTOS.length){
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    continue;
                }

                LecturePlannerDTO planner = lectureDTOS[lectureNum - 1].getPlanner();
                System.out.println("교과목명 : " + lectureDTOS[lectureNum - 1].getCourse().getCourseName());
                System.out.println("강의 목표 : " + planner.getGoal());
                System.out.println("강의 개요 : " + planner.getSummary());
            }else if(menu == 3){ // 강의계획서 입력
                updateLecturePlanner(lectureDTOS);
            }else if(menu == 4){ // 나가기
                break;
            }else{
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void updateLecturePlanner(LectureDTO[] lectureDTOS) throws Exception {
        System.out.println(Message.LECTURE_PLANNER_UPDATE_INPUT);
        int lectureNum = Integer.parseInt(scanner.nextLine());

        if(lectureNum-1>=lectureDTOS.length){
            System.out.println(Message.WRONG_INPUT_NOTICE);
            return;
        }

        LectureDTO selectedLecture = lectureDTOS[lectureNum-1];

        while(true){
            System.out.println(Message.LECTURE_PLANNER_CATEGORIES);
            System.out.print(Message.LECTURE_PLANNER_CATEGORIES_INPUT);
            int select = Integer.parseInt(scanner.nextLine());

            if(select == 1){
                System.out.println(Message.LECTURE_PLANNER_GOAL_INPUT);
                String goal = scanner.nextLine();
                selectedLecture.getPlanner().setGoal(goal);
            }else if(select == 2){
                System.out.println(Message.LECTURE_PLANNER_SUMMARY_INPUT);
                String summary = scanner.nextLine();
                selectedLecture.getPlanner().setSummary(summary);
            }else if(select == 3){
                break;
            }else{
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }

        ps.requestUpdateLectrue(selectedLecture);
        Protocol receiveProtocol = ps.response();   //성공/실패 응답
    }

    private void lookupRegisteringStd(LectureDTO[] lectureDTOS) throws Exception {
        System.out.println(Message.PLANNER_LOOKUP_INPUT);
        int lectureNum = Integer.parseInt(scanner.nextLine());
        
        if(lectureNum-1>=lectureDTOS.length){
            System.out.println(Message.WRONG_INPUT_NOTICE);
            return;
        }

        ps.requestReadRegisteringStd(lectureDTOS[lectureNum-1]);
        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
        StudentDTO[] studentDTOS = (StudentDTO[]) receiveProtocol.getObjectArray();

        //TODO : 출력 이쁘게
        for(StudentDTO std : studentDTOS){
            System.out.println("std = " + std);
        }
    }

    //
//    private void LectureManage() {
//        // 담당교과목 리스트 조회 후 출력
//
//        // 1.강의계획서 입력 2.강의계획서 수정 3. 수강신청 인원 조회 4. 나가기
//        int manageMenu = 0;
//        int lectureNum;
//        while (manageMenu != 4) {
//            System.out.println(Message.PROFESSOR_LECTURE_MANAGE_MENU);
//            manageMenu = Integer.parseInt(scanner.nextLine());
//            if (manageMenu == 1) {
//                System.out.print(Message.LECTURE_PLANNER_INSERT_INPUT);
//                lectureNum = Integer.parseInt(scanner.nextLine());
//            } else if (manageMenu == 2) {
//                System.out.print(Message.LECTURE_PLANNER_UPDATE_INPUT);
//                lectureNum = Integer.parseInt(scanner.nextLine());
//            } else if (manageMenu == 3) {
//                System.out.print(Message.LECTURE_REGISTER_LIST_INPUT);
//                lectureNum = Integer.parseInt(scanner.nextLine());
//            } else if (manageMenu == 4) {
//            } else {
//                System.out.println(Message.WRONG_INPUT_NOTICE);
//            }
//        }
//    }
//
//
//    private void lecturePlannerSettings() throws Exception {
//        int menu = 0;
//        LectureDTO[] lectureDTO = new LectureDTO[0];
//
//        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
//        sendPt.setCode(Protocol.T1_CODE_READ);
//        sendPt.setEntity(Protocol.ENTITY_LECTURE);
//        sendPt.send(os);
//
//        Protocol recvPt = read();
//        if (recvPt != null) {
//            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                    lectureDTO = (LectureDTO[]) recvPt.getObjectArray();
//                    for (int i = 0; i < lectureDTO.length; i++) {
//                        System.out.println((i + 1) + " : ");
//                        System.out.println(lectureDTO[i].toString());
//                    }
//                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
//            }
//        }
//
//        while (menu != 3) {
//            System.out.print(Message.LECTURE_PLANNER_SETTINGS_MENU);
//            menu = scanner.nextInt();// int 파싱 오류 처리 필요
//
//            if (menu == 1) {
//                System.out.print(Message.LECTURE_PLANNER_INSERT_INPUT);
//                int lectureNum = scanner.nextInt();
//
//                System.out.print(Message.LECTURE_PLANNER_INPUT);
//                String key = scanner.nextLine();
//                String value = scanner.nextLine();
//
//                Map<String, String> items = new HashMap<>();
//                items.put(key, value);
//                LecturePlannerDTO lecturePlannerDTO = LecturePlannerDTO.builder().items(items).build();
//                lectureDTO[lectureNum - 1].setPlanner(lecturePlannerDTO);
//
//                sendPt.setCode(Protocol.T1_CODE_CREATE);
//                sendPt.setEntity(Protocol.ENTITY_PLANNER);
//                sendPt.setObject(lectureDTO);
//                sendPt.send(os);
//
//                recvPt = read();
//                if (recvPt != null) {
//                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                            System.out.println(Message.LECTURE_PLANNER_INPUT_SUCCESS);
//                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                            System.out.println(Message.LECTURE_PLANNER_INPUT_FAIL);
//                    }
//                }
//
//            } else if (menu == 2) {
//                System.out.print(Message.LECTURE_PLANNER_UPDATE_INPUT);
//                int lectureNum = scanner.nextInt();
//
//                System.out.print(Message.LECTURE_PLANNER_INPUT);
//                String key = scanner.nextLine();
//                String value = scanner.nextLine();
//
//                Map<String, String> items = new HashMap<>();
//                items.put(key, value);
//                LecturePlannerDTO lecturePlannerDTO = LecturePlannerDTO.builder().items(items).build();
//                lectureDTO[lectureNum - 1].setPlanner(lecturePlannerDTO);
//
//                sendPt.setCode(Protocol.T1_CODE_UPDATE);
//                sendPt.setEntity(Protocol.ENTITY_PLANNER);
//                sendPt.setObject(lectureDTO);
//                sendPt.send(os);
//
//                recvPt = read();
//                if (recvPt != null) {
//                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                            System.out.println(Message.LECTURE_PLANNER_UPDATE_SUCCESS);
//                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                            System.out.println(Message.LECTURE_PLANNER_UPDATE_FAIL);
//                    }
//                }
//            } else if (menu == 3) {
//
//            } else {
//                System.out.println(Message.WRONG_INPUT_NOTICE);
//            }
//        }
//    }
//
//    private void lecturePlannerLookup() throws Exception {
//
//        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
//        sendPt.setCode(Protocol.T1_CODE_READ);
//        sendPt.setEntity(Protocol.ENTITY_LECTURE);
//        sendPt.send(os);
//
//        Protocol recvPt = read();
//        if (recvPt != null) {
//            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                    LectureDTO[] lectureDTO = (LectureDTO[]) recvPt.getObjectArray();
//                    for (int i = 0; i < lectureDTO.length; i++)
//                        System.out.println(lectureDTO[i].getPlanner());
//
//                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                    System.out.println(Message.LOOKUP_LECTURE_PLANNER_FAIL);
//            }
//        }
//    }
//
//    private void studentListLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
//        LectureDTO[] lectureDTO = new LectureDTO[0];
//
//        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
//        sendPt.setCode(Protocol.T1_CODE_READ);
//        sendPt.setEntity(Protocol.ENTITY_LECTURE);
//        sendPt.send(os);
//
//        Protocol recvPt = read();
//        if (recvPt != null) {
//            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                    lectureDTO = (LectureDTO[]) recvPt.getObjectArray();
//                    for (int i = 0; i < lectureDTO.length; i++) {
//                        System.out.println((i + 1) + " : ");
//                        System.out.println(lectureDTO[i].toString());
//                    }
//                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
//            }
//        }
//
//        System.out.print(Message.STUDENT_LIST_LOOKUP_MENU);
//        int lectureNum = scanner.nextInt();
//
//        sendPt = new Protocol(Protocol.TYPE_REQUEST);
//        sendPt.setCode(Protocol.T1_CODE_READ);
//        sendPt.setEntity(Protocol.ENTITY_LECTURE_STUD_LIST);
//        sendPt.setObject(lectureDTO[lectureNum - 1]);
//        sendPt.send(os);
//
//        recvPt = read();
//        if (recvPt != null) {
//            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                    StudentDTO[] studentDTOS = (StudentDTO[]) recvPt.getObjectArray();
//                    for (int i = 0; i < studentDTOS.length; i++) {
//                        System.out.println(i + " : ");
//                        System.out.print("id : " + studentDTOS[i].getId() + ", ");
//                        System.out.print("name : " + studentDTOS[i].getName() + ", ");
//                        System.out.print("department : " + studentDTOS[i].getDepartment() + ", ");
//                        System.out.print("maxCredit : " + studentDTOS[i].getMaxCredit() + ", ");
//                        System.out.print("year : " + studentDTOS[i].getYear() + ", ");
//                        System.out.println("studentCode : " + studentDTOS[i].getStudentCode());
//                    }
//
//                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                    System.out.println(Message.LOOKUP_STUDENT_LIST_FAIL);
//            }
//        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//            System.out.println(Message.MISMATCH_LECTURE_CODE);
//    }
//
    private void timeTableLookup() throws Exception {
        final int NUM_OF_PERIOD = 9;
        final int NUM_OF_DAY = 5;
        final int MON = 0;
        final int TUE = 1;
        final int WED = 2;
        final int THU = 3;
        final int FRI = 4;

        //시간표 출력
        ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
        ps.requestReadPersonalInfo(professorDTO);  // 개인정보 조회 요청

        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
        professorDTO = (ProfessorDTO) receiveProtocol.getObject();

        LectureTimeDTO[] lectureTimeDTOS = professorDTO.getTimeTable();
        String[][] timeTable = new String[NUM_OF_PERIOD][NUM_OF_DAY];
        String[] days = {"MON", "TUE", "WED", "THU", "FRI"};

        for(LectureTimeDTO dto : lectureTimeDTOS){
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

            for(int i=dto.getStartTime(); i<=dto.getEndTime(); i++){
                timeTable[i-1][day] = dto.getLectureName();
            }
        }

        for(int i=0; i<NUM_OF_DAY; i++){
            if(i==0){
                System.out.printf("%10s%10s", "", " |");
            }
            System.out.printf("%10s%10s", days[i], " |");
        }

        System.out.println();
        System.out.println("-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|");

        for(int i=0; i<NUM_OF_PERIOD; i++){
            for(int j=0; j<NUM_OF_DAY; j++){
                if(j==0){
                    System.out.printf("%8s%10s", (i+1)+"교시", " |");
                }

                if(timeTable[i][j]==null){
                    System.out.printf("%10s%10s", "", " |");
                }else{
                    System.out.printf(
                            "%"+(10-timeTable[i][j].length()/2)+"s%"
                                    +(10-timeTable[i][j].length()/2)+"s",
                            timeTable[i][j], " |");
                }

            }
            System.out.println();
            System.out.println("-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|");
        }

    }
}












