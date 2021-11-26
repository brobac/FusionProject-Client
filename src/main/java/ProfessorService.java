import dto.*;
import network.Protocol;
import infra.database.option.lecture.LectureOption;
import infra.database.option.lecture.ProfessorCodeOption;
import infra.database.option.lecture.LectureTargetYearOption;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProfessorService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private static InputStream is;
    private static OutputStream os;

    public ProfessorService(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    private static Protocol read() throws IOException {
        byte[] header = new byte[Protocol.LEN_HEADER];
        Protocol pt = new Protocol();
        int totalReceived = 0;
        int readSize;

        is.read(header, 0, Protocol.LEN_HEADER);
        pt.setHeader(header);

        byte[] buf = new byte[pt.getBodyLength()];
        while (totalReceived < pt.getBodyLength()) {
            readSize = is.read(buf, totalReceived, pt.getBodyLength() - totalReceived);
            totalReceived += readSize;
        }
        pt.setBody(buf);
        return pt;
    }

    public void run() throws Exception {
        int menu = 0;
        while (menu != 7) {
            System.out.println(Message.PROFESSOR_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            switch (menu) {
                case 1:
                    updatePersonalInformation();
                    break;
                case 2:
                    lectureLookup();
                    break;
                case 3:
                    lecturePlannerSettings();
                    break;
                case 4:
                    lecturePlannerLookup();
                    break;
                case 5:
                    studentListLookup();
                    break;
                case 6:
                    timeTableLookup();
                    break;
                case 7:
                    break;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    break;

            }
        }
    }

    private void updatePersonalInformation() throws Exception {
        //TODO 개인정보 조회하여 출력
        int menu = 0;
        ProfessorDTO professorDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_ACCOUNT);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    professorDTO = (ProfessorDTO) recvPt.getObject();
                    System.out.println("id : " + professorDTO.getId());
                    System.out.println("name : " + professorDTO.getName());
                    System.out.println("department : " + professorDTO.getDepartment());
                    System.out.println("birthDate : " + professorDTO.getBirthDate());
                    System.out.println("professorCode : " + professorDTO.getProfessorCode());
                    System.out.println("telePhone : " + professorDTO.getTelePhone());
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println(Message.LOOKUP_PERSONAL_INFORMATION_FAIL);
            }
        }
        while (menu != 4) {
            System.out.println(Message.UPDATE_PERSONAL_INFORMATION_MENU);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요

            if (menu == 1) { //TODO 서버에게 이름 변경 요청하기
                System.out.print(Message.CHANGE_NAME_INPUT);
                String name = scanner.nextLine();

                ProfessorDTO professorDTO1 = ProfessorDTO.builder().name(name).build();
                sendPt.setObject(professorDTO1);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setEntity(Protocol.ENTITY_ACCOUNT);
                sendPt.send(os);

                recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println(Message.UPDATE_NAME_SUCCESS);
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.UPDATE_NAME_FAIL);
                    }
                }
            } else if (menu == 2) { //TODO 서버에게 전화번호 변경 요청하기
                System.out.print(Message.CHANGE_PHONE_NUMBER_INPUT);
                String phoneNumber = scanner.nextLine();
                ProfessorDTO professorDTO1 = ProfessorDTO.builder().telePhone(phoneNumber).build();
                sendPt.setObject(professorDTO1);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setEntity(Protocol.ENTITY_ACCOUNT);
                sendPt.send(os);

                recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println(Message.UPDATE_PHONENUM_SUCCESS);
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.UPDATE_PHONENUM_FAIL);
                    }
                }
            } else if (menu == 3) {  //TODO 서버에게 비밀번호 변경 요청하기
                System.out.print(Message.CHANGE_PASSWORD_INPUT);
                String newPassword = scanner.nextLine();

                AccountDTO accountDTO = AccountDTO.builder().password(newPassword).build();
                sendPt.setObject(accountDTO);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setEntity(Protocol.ENTITY_ACCOUNT);
                sendPt.send(os);

                recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println(Message.UPDATE_PASSWORD_SUCCESS);
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.UPDATE_PASSWORD_FAIL);
                    }
                }
            } else if (menu == 4) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lectureLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        int menu = 0;
        // TODO 전체 목록 출력
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_LECTURE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    LectureDTO[] lectureLookUp = (LectureDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < lectureLookUp.length; i++) {
                        System.out.println(lectureLookUp[i].toString());
                    }
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
            }
        }

        while (menu != 2) {
            System.out.println(Message.PROFESSOR_LECTURE_LOOKUP_MENU1);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            if (menu == 1) {
                //TODO 조건입력받고 서버에 전송 후 결과 받아서 프린트
                //TODO *서버 프로그램에 있는 Option파일 필요(파일가져와야함) 한 번 조회할 때 조건 여려 개 적용 가능해야함
                //방법2
                while (menu != 4) {
                    System.out.println(Message.PROFESSOR_LECTURE_LOOKUP_MENU2);
                    System.out.print(Message.INPUT);
                    menu = scanner.nextInt();
                    if (menu == 1) {
                        System.out.println(Message.TARGET_GRADE_INPUT);
                        int targetGrade = scanner.nextInt();

                        LectureOption lectureOption;
                        LectureTargetYearOption lectureTargetYearOption = new LectureTargetYearOption(targetGrade);
                        lectureOption = lectureTargetYearOption;

                        sendPt.setObject(lectureOption);
                        sendPt.setCode(Protocol.T1_CODE_READ);
                        sendPt.setEntity(Protocol.ENTITY_LECTURE);
                        sendPt.send(os);

                        recvPt = read();
                        if (recvPt != null) {
                            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                                    LectureDTO[] lectureLookUp = (LectureDTO[]) recvPt.getObjectArray();
                                    for (int i = 0; i < lectureLookUp.length; i++) {
                                        System.out.println(lectureLookUp[i].toString());
                                    }
                                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
                            }
                        }
                        //학년별 조회
                    } else if (menu == 2) {
                        System.out.println(Message.PROFESSOR_CODE_INPUT);
                        String professorId = scanner.nextLine();

                        LectureOption lectureOption;
                        ProfessorCodeOption professorCodeOption = new ProfessorCodeOption(professorId);
                        lectureOption = professorCodeOption;

                        sendPt.setObject(lectureOption);
                        sendPt.setCode(Protocol.T1_CODE_READ);
                        sendPt.setEntity(Protocol.ENTITY_LECTURE);
                        sendPt.send(os);

                        recvPt = read();
                        if (recvPt != null) {
                            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                                    LectureDTO[] lectureLookUp = (LectureDTO[]) recvPt.getObjectArray();
                                    for (int i = 0; i < lectureLookUp.length; i++) {
                                        System.out.println(lectureLookUp[i].toString());
                                    }
                                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
                            }
                        }
                        //담당 교수별 조회
                    } else if (menu == 3) {
                        System.out.println(Message.TARGET_GRADE_INPUT);
                        int targetGrade = scanner.nextInt();
                        System.out.println(Message.PROFESSOR_CODE_INPUT);
                        String professorId = scanner.nextLine();

                        LectureOption[] lectureOption = new LectureOption[2];
                        ProfessorCodeOption professorCodeOption = new ProfessorCodeOption(professorId);
                        LectureTargetYearOption lectureTargetYearOption = new LectureTargetYearOption(targetGrade);
                        lectureOption[0] = professorCodeOption;
                        lectureOption[1] = lectureTargetYearOption;

                        sendPt.setObjectArray(lectureOption);
                        sendPt.setCode(Protocol.T1_CODE_READ);
                        sendPt.setEntity(Protocol.ENTITY_LECTURE);
                        sendPt.send(os);

                        recvPt = read();
                        if (recvPt != null) {
                            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                                    LectureDTO[] lectureLookUp = (LectureDTO[]) recvPt.getObjectArray();
                                    for (int i = 0; i < lectureLookUp.length; i++) {
                                        System.out.println(lectureLookUp[i].toString());
                                    }
                                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
                            }
                        }
                    } else if (menu == 4) {
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }
            } else if (menu == 2) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lecturePlannerSettings() throws Exception {
        int menu = 0;
        LectureDTO[] lectureDTO = new LectureDTO[0];

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_LECTURE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    lectureDTO = (LectureDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < lectureDTO.length; i++) {
                        System.out.println((i + 1) + " : ");
                        System.out.println(lectureDTO[i].toString());
                    }
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
            }
        }

        while (menu != 3) {
            System.out.print(Message.LECTURE_PLANNER_SETTINGS_MENU);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요

            if (menu == 1) {
                System.out.print(Message.LECTURE_PLANNER_INSERT_INPUT);
                int lectureNum = scanner.nextInt();

                System.out.print(Message.LECTURE_PLANNER_INPUT);
                String key = scanner.nextLine();
                String value = scanner.nextLine();

                Map<String, String> items = new HashMap<>();
                items.put(key, value);
                LecturePlannerDTO lecturePlannerDTO = LecturePlannerDTO.builder().items(items).build();
                lectureDTO[lectureNum - 1].setPlanner(lecturePlannerDTO);

                sendPt.setCode(Protocol.T1_CODE_CREATE);
                sendPt.setEntity(Protocol.ENTITY_PLANNER);
                sendPt.setObject(lectureDTO);
                sendPt.send(os);

                recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            System.out.println(Message.LECTURE_PLANNER_INPUT_SUCCESS);
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.LECTURE_PLANNER_INPUT_FAIL);
                    }
                }

            } else if (menu == 2) {
                System.out.print(Message.LECTURE_PLANNER_UPDATE_INPUT);
                int lectureNum = scanner.nextInt();

                System.out.print(Message.LECTURE_PLANNER_INPUT);
                String key = scanner.nextLine();
                String value = scanner.nextLine();

                Map<String, String> items = new HashMap<>();
                items.put(key, value);
                LecturePlannerDTO lecturePlannerDTO = LecturePlannerDTO.builder().items(items).build();
                lectureDTO[lectureNum - 1].setPlanner(lecturePlannerDTO);

                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setEntity(Protocol.ENTITY_PLANNER);
                sendPt.setObject(lectureDTO);
                sendPt.send(os);

                recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            System.out.println(Message.LECTURE_PLANNER_UPDATE_SUCCESS);
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.LECTURE_PLANNER_UPDATE_FAIL);
                    }
                }
            } else if (menu == 3) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lecturePlannerLookup() throws Exception {

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_LECTURE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    LectureDTO[] lectureDTO = (LectureDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < lectureDTO.length; i++)
                        System.out.println(lectureDTO[i].getPlanner());

                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println(Message.LOOKUP_LECTURE_PLANNER_FAIL);
            }
        }
    }

    private void studentListLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        LectureDTO[] lectureDTO = new LectureDTO[0];

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_LECTURE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    lectureDTO = (LectureDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < lectureDTO.length; i++) {
                        System.out.println((i + 1) + " : ");
                        System.out.println(lectureDTO[i].toString());
                    }
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println(Message.LOOKUP_LECTURE_FAIL);
            }
        }

        System.out.print(Message.STUDENT_LIST_LOOKUP_MENU);
        int lectureNum = scanner.nextInt();

        sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_LECTURE_STUD_LIST);
        sendPt.setObject(lectureDTO[lectureNum - 1]);
        sendPt.send(os);

        recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    StudentDTO[] studentDTOS = (StudentDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < studentDTOS.length; i++) {
                        System.out.println(i + " : ");
                        System.out.print("id : " + studentDTOS[i].getId() + ", ");
                        System.out.print("name : " + studentDTOS[i].getName() + ", ");
                        System.out.print("department : " + studentDTOS[i].getDepartment() + ", ");
                        System.out.print("maxCredit : " + studentDTOS[i].getMaxCredit() + ", ");
                        System.out.print("year : " + studentDTOS[i].getYear() + ", ");
                        System.out.println("studentCode : " + studentDTOS[i].getStudentCode());
                    }

                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println(Message.LOOKUP_STUDENT_LIST_FAIL);
            }
        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
            System.out.println(Message.MISMATCH_LECTURE_CODE);
    }

    private void timeTableLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_PROF_TIMETABLE);
        sendPt.send(os);

        LectureTimeDTO[] lectureTimeDTO = new LectureTimeDTO[30];
        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    lectureTimeDTO = (LectureTimeDTO[]) recvPt.getObjectArray();
                } else
                    System.out.println(Message.LOOKUP_TIMETABLE_FAIL);
            }
        }
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
                        System.out.printf("%10s%8s", /*lectureTimeDTO[k++].getCourseName(),*/ " ");
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












