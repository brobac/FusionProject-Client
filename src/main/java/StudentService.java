import dto.*;
import network.Protocol;
import network.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class StudentService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private static InputStream is;
    private static OutputStream os;

    public StudentService(InputStream is, OutputStream os) {
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
        while (menu != 8) {
            System.out.println(Message.PROFESSOR_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            switch (menu) {
                case 1:
                    updatePersonalInformation();
                    break;
                case 2:
                    registering();
                    break;
                case 3:
                    lectureLookup();
                    break;
                case 4:
                    lecturePlannerLookup();
                    break;
                case 5:
                    timeTableLookup();
                    break;
                case 6:
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
        StudentDTO studentDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_ACCOUNT);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    studentDTO = (StudentDTO) recvPt.getObject();
                    System.out.println("id : " + studentDTO.getId());
                    System.out.println("name : " + studentDTO.getName());
                    System.out.println("department : " + studentDTO.getDepartment());
                    System.out.println("birthDate : " + studentDTO.getBirthDate());
                    System.out.println("maxCredit : " + studentDTO.getMaxCredit());
                    System.out.println("year : " + studentDTO.getYear());
                    System.out.println("studentCode : " + studentDTO.getStudentCode());
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

                StudentDTO studentDTO1 = StudentDTO.builder().name(name).build();
                sendPt.setObject(studentDTO1);
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
//                학생 전화번호 없음
//                sendPt.setCode(Protocol.T1_CODE_UPDATE);
//                sendPt.setEntity(Protocol.ENTITY_ACCOUNT);
//                sendPt.send(os);
//
//                recvPt = read();
//                if (recvPt != null) {
//                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
//                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
//                            System.out.println(Message.UPDATE_PHONENUM_SUCCESS);
//                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
//                            System.out.println(Message.UPDATE_PHONENUM_FAIL);
//                    }
//                }
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

    private void registering() throws IOException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        int menu = 0;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);

        while (menu != 4) {
            System.out.print(Message.REGISTERING_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();

            if (menu == 1) { //수강 신청
                System.out.print(Message.COURSE_CODE_INPUT);
                String lectureCode = scanner.nextLine();
                LectureDTO lectureDTO = LectureDTO.builder().lectureCode(lectureCode).build();
                sendPt.setCode(Protocol.T1_CODE_CREATE);
                sendPt.setEntity(Protocol.ENTITY_REGISTRATION);
                sendPt.setObject(lectureDTO);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println(Message.REGISTERING_SUCCESS);
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.REGISTERING_FAIL);
                    }
                }
            } else if (menu == 2) { //수강 취소
                System.out.print(Message.COURSE_CODE_INPUT);
                String lectureCode = scanner.nextLine();
                LectureDTO lectureDTO = LectureDTO.builder().lectureCode(lectureCode).build();
                sendPt.setCode(Protocol.T1_CODE_DELETE);
                sendPt.setEntity(Protocol.ENTITY_REGISTRATION);
                sendPt.setObject(lectureDTO);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println(Message.REGISTERING_CANCEL_SUCCESS);
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.REGISTERING_CANCEL_FAIL);
                    }
                } else if (menu == 3) { //수강 신청 현황 조회
                    sendPt.setCode(Protocol.T1_CODE_READ);
                    sendPt.setEntity(Protocol.ENTITY_REGISTRATION);
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
                                System.out.println(Message.LOOKUP_REGISTERING_FAIL);
                        }
                    }
                } else if (menu == 4) {

                } else {
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                }
            }
        }
    }

    private void lectureLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //개설교과목 전체 목록 출력
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
    }

    private void lecturePlannerLookup() throws Exception {
        int menu = 0;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);

        while (menu != 2) {
            System.out.print(Message.LECTURE_PLANNER_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();

            if (menu == 1) { //선택 교과목 강의 계획서 조회
                System.out.print(Message.COURSE_CODE_INPUT);
                String lectureCode = scanner.nextLine();
                LectureDTO lectureDTO = LectureDTO.builder().lectureCode(lectureCode).build();
                sendPt.setObject(lectureDTO);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setEntity(Protocol.ENTITY_LECTURE);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            lectureDTO = (LectureDTO) recvPt.getObject();
                            System.out.println(lectureDTO.getPlanner());
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println(Message.LOOKUP_LECTURE_PLANNER_FAIL);
                    }
                }
            } else if (menu == 2) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void timeTableLookup() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //시간표 출력
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


