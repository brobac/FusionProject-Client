//package controller;
//
//import infra.dto.*;
//import network.ProfProtocolService;
//import network.Protocol;

//import infra.database.option.lecture.LectureOption;
//import infra.database.option.lecture.ProfessorCodeOption;

//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Scanner;
//
//public class ProfessorService implements EnrollmentService {
//    public static Scanner scanner = new Scanner(System.in);
//
//    private InputStream is;
//    private OutputStream os;
//    private AccountDTO account;
//    private ProfProtocolService ps;
//
//    public ProfessorService(AccountDTO account, InputStream is, OutputStream os) {
//        this.account = account;
//        this.is = is;
//        this.os = os;
//        ps = new ProfProtocolService(is, os);
//    }
//
//
//    public void run() throws Exception {
//        int menu = 0;
//        while (menu != 7) {
//            System.out.println(Message.PROFESSOR_SERVICE_MENU);
//            System.out.print(Message.INPUT);
//            menu = scanner.nextInt();
//            switch (menu) {
//                case 1:
//                    personalInformation();
//                    break;
//                case 2:
//                    lectureLookup();
//                    break;
//                case 3:
//
//                    break;
//                case 4:
//                    timeTableLookup();
//                    break;
//                case 5:
//                    break;
//                default:
//                    System.out.println(Message.WRONG_INPUT_NOTICE);
//                    break;
//
//            }
//        }
//    }
//
//    private void personalInformation() throws Exception {
//        ProfessorDTO professorDTO = ProfessorDTO.builder().id(account.getMemberID()).build();
//        ps.requestReadPersonalInfo(professorDTO);  // 개인정보 조회 요청
//
//        Protocol receiveProtocol = ps.response();   // 개인정보 조회 요청에 대한 응답
//        if (receiveProtocol != null) {   // 조회 성공
//            professorDTO = (ProfessorDTO) receiveProtocol.getObject();
//            System.out.println("이름 : " + professorDTO.getName());
//            System.out.println("학과 : " + professorDTO.getDepartment());
//            System.out.println("생년월일 : " + professorDTO.getBirthDate());
//            System.out.println("전화번호 : " + professorDTO.getTelePhone());
//            System.out.println("교원번호 : " + professorDTO.getProfessorCode());
//        } else {                                    // 조회 실패
//            System.out.println(Message.LOOKUP_PERSONAL_INFORMATION_FAIL);
//            return;
//        }
//
//        // 변경 기능
//        int menu = 0;
//        while (true) {
//            System.out.println(Message.UPDATE_PERSONAL_INFORMATION_MENU);
//            menu = Integer.parseInt(scanner.nextLine());
//
//            if (menu == 1) { //이름 변경
//                System.out.print(Message.CHANGE_NAME_INPUT);
//                String name = scanner.nextLine();
//                professorDTO.setName(name);
//                ps.requestUpdatePersonalInfo(professorDTO);   // 이름 변경 요청
//
//                receiveProtocol = ps.response();
//                if (receiveProtocol != null) {
//                    System.out.println(Message.UPDATE_NAME_SUCCESS);
//                } else {
//                    System.out.println(Message.UPDATE_NAME_FAIL);
//                }
//            } else if (menu == 2) {  //비밀번호 변경
//                System.out.print(Message.CHANGE_PASSWORD_INPUT);
//                String newPassword = scanner.nextLine();
//
//                AccountDTO newAccount = new AccountDTO(account);
//                newAccount.setPassword(newPassword);
//
//                ps.requestUpdateAccount(newAccount);  // 비밀번호 변경 요청
//
//                receiveProtocol = ps.response();
//                if (receiveProtocol != null)
//                    System.out.println(Message.UPDATE_PASSWORD_SUCCESS);
//                else
//                    System.out.println(Message.UPDATE_PASSWORD_FAIL);
//
//            } else if (menu == 3) {   // 나가기
//                break;
//            } else {
//                System.out.println(Message.WRONG_INPUT_NOTICE);
//            }
//        }
//    }
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
//    private void timeTableLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
//        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
//        sendPt.setCode(Protocol.T1_CODE_READ);
//        sendPt.setEntity(Protocol.ENTITY_PROF_TIMETABLE);
//        sendPt.send(os);
//
//        LectureTimeDTO[] lectureTimeDTO = new LectureTimeDTO[30];
//        Protocol recvPt = read();
//        if (recvPt != null) {
//            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
//                    lectureTimeDTO = (LectureTimeDTO[]) recvPt.getObjectArray();
//                } else
//                    System.out.println(Message.LOOKUP_TIMETABLE_FAIL);
//            }
//        }
//        int k = 0;
//        String[] day = {"MON", "TUE", "WED", "THU", "FRI"};
//        for (int i = 0; i < 8; i++) {
//            if (i != 0)
//                System.out.print(i + " |");
//            else
//                System.out.print("\\ |");
//            for (int j = 0; j < 5; j++) {
//                if (i == 0) {
//                    System.out.printf("%10s%10s", day[j], " |");
//                } else {
//                    if (lectureTimeDTO[k].getLectureDay() == day[j] && (lectureTimeDTO[k].getStartTime() == i || lectureTimeDTO[k].getEndTime() == i)) {
//                        System.out.printf("%10s%8s", /*lectureTimeDTO[k++].getCourseName(),*/ " ");
//                    } else {
//                        System.out.printf("%10s%10s", "", "");
//                    }
//                }
//            }
//            System.out.println();
//            System.out.println("--|-------------------|-------------------|-------------------|-------------------|-------------------|");
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
