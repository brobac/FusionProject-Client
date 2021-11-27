import dto.*;
import network.Protocol;
import network.StudentProtocolService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class StudentService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private InputStream is;
    private OutputStream os;
    private StudentProtocolService ps;

    public StudentService(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        ps = new StudentProtocolService(is, os);
    }

    public void run() throws Exception {
        int menu = 0;
        while (menu != 6) {
            System.out.println(Message.STUDENT_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            switch (menu) {
                case 1:
                    personaInformation();  // 개인정보 관리
                    break;
                case 2:
                    registering();         // 수강신청 관리
                    break;
                case 3:
                    lectureLookup();       // 개설교과목 조회 //TODO 강의계획서 포함 하는거야 마는거야
                    break;
                case 4:
                    lecturePlannerLookup();  // 강의계획서 조회
                    break;
                case 5:
                    timeTableLookup();      // 시간표 조회
                    break;
                case 6:                     // 로그아웃
                    break;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void personaInformation() throws Exception {
        ps.requestReadPersonalInfo();  // 개인정보 조회 요청

        StudentDTO studentDTO;
        Protocol recvPt = ps.response();   // 개인정보 조회 요청에 대한 응답
        if (recvPt != null) {   // 조회 성공
            studentDTO = (StudentDTO) recvPt.getObject();
            System.out.println("id : " + studentDTO.getId());
            System.out.println("name : " + studentDTO.getName());
            System.out.println("department : " + studentDTO.getDepartment());
            System.out.println("birthDate : " + studentDTO.getBirthDate());
            System.out.println("maxCredit : " + studentDTO.getMaxCredit());
            System.out.println("year : " + studentDTO.getYear());
            System.out.println("studentCode : " + studentDTO.getStudentCode());
        }
        else {                                    // 조회 실패
            System.out.println(Message.LOOKUP_PERSONAL_INFORMATION_FAIL);
            return;
        }

        // 변경 기능
        int menu = 0;
        while (true) {
            System.out.println(Message.UPDATE_PERSONAL_INFORMATION_MENU);
            menu = scanner.nextInt(); //TODO int 파싱 오류 처리 필요??

            if (menu == 1) { //TODO 서버에게 이름 변경 요청하기
                System.out.print(Message.CHANGE_NAME_INPUT);
                String name = scanner.nextLine();

                StudentDTO studentDTO1 = StudentDTO.builder().name(name).build();
                ps.requestUpdatePersonalInfo(studentDTO1);   // 이름 변경 요청

                Protocol recv = ps.response();
                if (recv != null)
                    System.out.println(Message.UPDATE_NAME_SUCCESS);
                else
                    System.out.println(Message.UPDATE_NAME_FAIL);

            } else if (menu == 2) { //TODO 서버에게 전화번호 변경 요청하기
                System.out.print(Message.CHANGE_PHONE_NUMBER_INPUT);
                String phoneNumber = scanner.nextLine();
                //TODO 학생 DTO에 전화번호 없음
            
            } else if (menu == 3) {  //TODO 서버에게 비밀번호 변경 요청하기
                System.out.print(Message.CHANGE_PASSWORD_INPUT);
                String newPassword = scanner.nextLine();

                AccountDTO accountDTO = AccountDTO.builder().password(newPassword).build();
                ps.requestUpdateAccount(accountDTO);  // 비밀번호 변경 요청

                Protocol recv = ps.response();
                if (recv != null)
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

    private void registering() throws Exception {
        int menu = 0;
        while (true) {
            System.out.print(Message.REGISTERING_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();

            if (menu == 1) {       //수강 신청
                System.out.print(Message.COURSE_CODE_INPUT);
                String lectureCode = scanner.nextLine();
                LectureDTO lectureDTO = LectureDTO.builder().lectureCode(lectureCode).build();
                ps.requestRegistering(lectureDTO);

                Protocol recv = ps.response();
                if (recv != null)
                    System.out.println(Message.REGISTERING_SUCCESS);
                else
                    System.out.println(Message.REGISTERING_FAIL);
            }
            else if (menu == 2) {    //수강 취소   //TODO 조회 하고 취소??
                System.out.print(Message.COURSE_CODE_INPUT);
                String lectureCode = scanner.nextLine();
                LectureDTO lectureDTO = LectureDTO.builder().lectureCode(lectureCode).build();
                ps.requestDeleteRegistering(lectureDTO);   // 수강신청 취소 요청

                Protocol recv = ps.response();
                if (recv != null)
                    System.out.println(Message.REGISTERING_CANCEL_SUCCESS);
                else
                    System.out.println(Message.REGISTERING_CANCEL_FAIL);

            }
            else if (menu == 3) {    //수강 신청 현황 조회
                ps.requestReadRegistering();
                Protocol recv = ps.response();
                if (recv != null) {
                    LectureDTO[] lectureLookUp = (LectureDTO[]) recv.getObjectArray();
                    for (int i = 0; i < lectureLookUp.length; i++) {
                        System.out.println(lectureLookUp[i].toString());
                    }
                } else {
                    System.out.println(Message.LOOKUP_REGISTERING_FAIL);
                }
            }
            else if (menu == 4)
                break;
            else
                System.out.println(Message.WRONG_INPUT_NOTICE);
        }
    }

    private void lectureLookup() throws Exception{
        ps.requestAllLectureList();  // 개설 교과목 (전체) 목록 요청

        Protocol recv = ps.response();
        if (recv != null) {
            LectureDTO[] lectureLookUp = (LectureDTO[]) recv.getObjectArray();
            for (int i = 0; i < lectureLookUp.length; i++) {
                System.out.println(lectureLookUp[i].toString());
            }
        } else
            System.out.println(Message.LOOKUP_LECTURE_FAIL);
    }

    // TODO 강의계획서 조회랑 시간표 조회는 어떻게 된건지 모르겠어서 일단 놔둠
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


