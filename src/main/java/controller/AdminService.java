package controller;

import infra.dto.*;
import network.AdminProtocolService;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AdminService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private InputStream is;
    private OutputStream os;
    private AccountDTO account;
    private AdminProtocolService ps;

    public AdminService(AccountDTO account, InputStream is, OutputStream os) {
        this.account = account;
        this.is = is;
        this.os = os;
        ps = new AdminProtocolService(is, os);
    }

    public void run() throws Exception {
        int menu = 0;
        while (menu != 8) {
            System.out.println(Message.ADMIN_SERVICE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            switch (menu) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    courseManage();
                    break;
                case 3:
                    lectureManage();
                    break;
                case 4:
                    plannerInputPeriodSettings();
                    break;
                case 5:
                    registeringPeriodSettings();
                    break;
                case 6:
                    memberLookup();
                    break;
                case 7:
                    lectureLookup();
                    break;
                case 8:
                    break;
                default:
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    break;
            }
        }
    }

    private void createAccount() throws Exception {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.CREATE_ACCOUNT_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();  // int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.print(Message.STUDENT_CODE_INPUT);
                String studentCode = scanner.nextLine();

                System.out.print(Message.NAME_INPUT);
                String name = scanner.nextLine();

                System.out.print(Message.STUDENT_YEAR_INPUT);
                int year = Integer.parseInt(scanner.nextLine());

                System.out.print(Message.DEPARTMENT_INPUT);
                String department = scanner.nextLine();

                System.out.print(Message.BIRTHDAY_INPUT);
                String birthday = scanner.nextLine();

                //관리자가 입력한 학생정보로 studentDTO 생성
                StudentDTO studentDTO = StudentDTO.builder()
                        .studentCode(studentCode)
                        .name(name)
                        .year(year)
                        .department(department)
                        .birthDate(birthday)
                        .build();

                //계정생성이 필요한 학생의 정보를 서버에게 전달
                ps.reqCreateStudAccount(studentDTO);
                Protocol receiveProtocol = ps.response();
                if (receiveProtocol.getCode() == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("계정생성 성공");
                } else {
                    System.out.println("계정생성 실패");
                }
            } else if (menu == 2) {
                System.out.print(Message.PROFESSOR_CODE_INPUT);
                String professorCode = scanner.nextLine();

                System.out.print(Message.NAME_INPUT);
                String name = scanner.nextLine();

                System.out.print(Message.DEPARTMENT_INPUT);
                String department = scanner.nextLine();

                System.out.print(Message.BIRTHDAY_INPUT);
                String birthday = scanner.nextLine();

                System.out.print(Message.PHONE_NUMBER_INPUT);
                String phoneNumber = scanner.nextLine();

                ProfessorDTO professorDTO = ProfessorDTO.builder()
                        .professorCode(professorCode)
                        .name(name)
                        .department(department)
                        .birthDate(birthday)
                        .telePhone(phoneNumber)
                        .build();

                ps.reqCreateProfAccount(professorDTO);
                Protocol receiveProtocol = ps.response();
                if (receiveProtocol.getCode() == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("계정생성 성공");
                } else {
                    System.out.println("계정생성 실패");
                }
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }

    }

    private void courseManage() throws Exception {
        int menu = 0;
        while (menu != 4) {
            System.out.println(Message.COURSE_MANAGE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.println(Message.CREATE_COURSE);
                System.out.println(Message.COURSE_CODE_INPUT);
                String courseCode = scanner.nextLine();
                System.out.println(Message.COURSE_NAME_INPUT);
                String courseName = scanner.nextLine();
                System.out.println(Message.DEPARTMENT_INPUT);
                String department = scanner.nextLine();
                System.out.println(Message.TARGET_GRADE_INPUT);
                int targetGrade = Integer.parseInt(scanner.nextLine());
                System.out.println(Message.CREDIT_INPUT);
                int credit = Integer.parseInt(scanner.nextLine());

                CourseDTO courseDTO = CourseDTO.builder()
                        .courseCode(courseCode)
                        .courseName(courseName)
                        .department(department)
                        .targetYear(targetGrade)
                        .credit(credit)
                        .build();

                ps.reqCreateCourse(courseDTO);
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getType();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("과목 생성 성공");
                } else {
                    System.out.println("과목 생성 실패");
                }
            } else if (menu == 2) {
                System.out.print(Message.UPDATE_COURSE_INPUT);
                String courseCode = scanner.nextLine();
                CourseDTO courseDTO = CourseDTO.builder()
                        .courseCode(courseCode)
                        .build();
                ps.reqReadCourse(courseDTO);
                //해당과목 정보조회
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getType();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("조회성공");
                    courseDTO = (CourseDTO) receiveProtocol.getObject();
                } else {
                    System.out.println("과목 생성 실패");
                    return;
                }

                System.out.println("현재 정보");
                System.out.println("과목코드 : " + courseDTO.getCourseCode());
                System.out.println("과목명 : " + courseDTO.getCourseName());
                System.out.println("학과 : " + courseDTO.getDepartment());
                System.out.println("대상학년 : " + courseDTO.getTargetYear());
                System.out.println("학점 : " + courseDTO.getCredit());
                int updateMenu = 0;
                while (updateMenu != 7) {
                    System.out.println("[1] 과목코드 변경 [2] 과목명 변경 [3]학과 변경 [4]대상학년 변경 [5]학점 변경 [6]제출하기");
                    System.out.print(Message.INPUT);
                    switch (updateMenu) {
                        case 1:
                            System.out.print(Message.COURSE_CODE_INPUT);
                            courseDTO.setCourseCode(scanner.nextLine());
                            break;
                        case 2:
                            System.out.print(Message.COURSE_NAME_INPUT);
                            courseDTO.setCourseName(scanner.nextLine());
                            break;
                        case 3:
                            System.out.print(Message.DEPARTMENT_INPUT);
                            courseDTO.setDepartment(scanner.nextLine());
                            break;
                        case 4:
                            System.out.print(Message.TARGET_GRADE_INPUT);
                            courseDTO.setTargetYear(Integer.parseInt(scanner.nextLine()));
                            break;
                        case 5:
                            System.out.print(Message.CREDIT_INPUT);
                            courseDTO.setCredit(Integer.parseInt(scanner.nextLine()));
                            break;
                        case 6:
                            ps.reqUpdateCourse(courseDTO);
                            receiveProtocol = ps.response();
                            result = receiveProtocol.getType();
                            if (result == Protocol.T2_CODE_SUCCESS) {
                                System.out.println("업데이트 되었습니다.");
                            } else {
                                System.out.println("업데이트 실패");
                                return;
                            }
                            break;
                        case 7:
                            break;
                        default:
                            System.out.println(Message.WRONG_INPUT_NOTICE);
                            break;
                    }
                }
            } else if (menu == 3) {
                System.out.print(Message.DELETE_COURSE_INPUT);
                String courseCode = scanner.nextLine();
                CourseDTO courseDTO = CourseDTO.builder()
                        .courseCode(courseCode)
                        .build();
                ps.reqDeleteCourse(courseDTO);
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getType();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("삭제 성공");
                } else {
                    System.out.println("삭제 실패");
                    return;
                }
            } else if (menu == 4) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }

    }

    private void lectureManage() throws Exception {
        int menu = 0;
        while (menu != 4) {
            System.out.println(Message.COURSE_MANAGE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            scanner.nextLine();// int 파싱 오류 처리 필요
            if (menu == 1) {
                System.out.println(Message.CREATE_LECTURE);
                //TODO 사용자로 부터 개설 교과목 정보를 입력받고 서버에게 개설 교과목 생성을 요청한다.
                System.out.print("courseID : ");
                long courseId = Long.parseLong(scanner.nextLine());
                System.out.print("LectureCode : ");
                String lectureCode = scanner.nextLine();
                System.out.print("제한인원 : ");
                int limit = Integer.parseInt(scanner.nextLine());
                System.out.print("lecturerCode : ");
                String lecturerCode = scanner.nextLine();
                Set<LectureTimeDTO> lectureTimeDTOSet = new HashSet<>();
                int option = 0;
                while (option != 2) {
                    System.out.println("----- 강의시간 입력 -----");
                    System.out.print("요일 : ");
                    String day = scanner.nextLine();
                    System.out.println("시작 교시 : ");
                    int startTime = Integer.parseInt(scanner.nextLine());
                    System.out.println("끝 교시 : ");
                    int endTime = Integer.parseInt(scanner.nextLine());
                    System.out.println("강의실 : ");
                    String room = scanner.nextLine();
                    LectureTimeDTO lectureTime = LectureTimeDTO.builder()
                            .lectureDay(day)
                            .startTime(startTime)
                            .endTime(endTime)
                            .build();
                    lectureTimeDTOSet.add(lectureTime);
                    System.out.println("[1] 추가입력  [2]종료");
                    System.out.print(Message.INPUT);
                    option = Integer.parseInt(scanner.nextLine());
                }
                LectureDTO lectureDTO = LectureDTO.builder()
                        .course(CourseDTO.builder().id(courseId).build())
                        .lectureCode(lectureCode)
                        .limit(limit)
                        .lectureTimes(lectureTimeDTOSet)
                        .build();

                ps.reqCreateLecture(lectureDTO);
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("개설 교과목 등록 성공");
                } else {
                    System.out.println("개설 교과목 등록 실패");
                }

            } else if (menu == 2) {
                //TODO 서버에서 현재 정보를 받아와서 print 해주고 새로운 입력을 받는다.
                System.out.print(Message.UPDATE_LECTURE_INPUT);

            } else if (menu == 3) {
                //TODO 사용자로부터 입력받은 과목코드에 해당하는 개설 교과목을 서버에게 삭제 요청한다.
                System.out.print(Message.DELETE_LECTURE_INPUT);

            } else if (menu == 4) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void plannerInputPeriodSettings() throws IllegalAccessException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.PLANNER_INPUT_PERIOD_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.print(Message.BEGIN_PERIOD_INPUT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime begin = LocalDateTime.parse(scanner.nextLine(), formatter); // format이랑 일치하지 않으면 오류 발생
                System.out.print(Message.END_PERIOD_INPUT);
                LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);  // format이랑 일치하지 않으면 오류 발생
                PeriodDTO periodDTO = PeriodDTO.builder()
                        .beginTime(begin)
                        .endTime(end)
                        .build();
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_PLANNER_PERIOD);
                sendProtocol.setObject(periodDTO);
                sendProtocol.send(os);
                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("강의 계획서 입력기간 등록 성공");
                    } else {
                        System.out.println("강의 계획서 입력기간 등록 실패");
                        return;
                    }
                }


                //TODO 사용자로부터 입력받은 기간을 서버에게 등록 요청한다.

            } else if (menu == 2) {
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PLANNER_PERIOD);
                sendProtocol.send(os);
                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        PeriodDTO[] periodDTOS = (PeriodDTO[]) receiveProtocol.getObjectArray();
                        for (PeriodDTO infra.dto:
                        periodDTOS){
                            System.out.print("시작 : " + infra.dto.getBeginTime() + " ~ ");
                            System.out.print("종료 : " + infra.dto.getEndTime());
                        }
                    } else {
                        System.out.println("조회 실패");
                        return;
                    }
                }
                //TODO 서버에게 현재 등록되어있는 강의계획서 등록 기간 정보를 받아와서 출력한다.
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void registeringPeriodSettings() throws IllegalAccessException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.REGISTERING_PERIOD_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.print(Message.BEGIN_PERIOD_INPUT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime begin = LocalDateTime.parse(scanner.nextLine(), formatter);
                System.out.print(Message.END_PERIOD_INPUT);
                LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);
                System.out.println(Message.TARGET_GRADE_INPUT);
                int grade = scanner.nextInt();
                PeriodDTO periodDTO = PeriodDTO.builder()
                        .beginTime(begin)
                        .endTime(end)
                        .build();
                RegisteringPeriodDTO registeringPeriodDTO = RegisteringPeriodDTO.builder()
                        .period(periodDTO)
                        .allowedYear(grade)
                        .build();
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_REGIS_PERIOD);
                sendProtocol.setObject(registeringPeriodDTO);
                sendProtocol.send(os);

                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("수강신청 기간 등록 성공");
                    } else {
                        System.out.println("수강신청 기간 등록 성공");
                        return;
                    }
                }
            } else if (menu == 2) {
                //TODO 서버에게 현재 등록되어있는 수강신청 기간 정보를 받아와서 출력한다.
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_REGIS_PERIOD);
                sendProtocol.send(os);
                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        RegisteringPeriodDTO[] registeringPeriodDTOS = (RegisteringPeriodDTO[]) receiveProtocol.getObjectArray();
                        for (RegisteringPeriodDTO infra.dto:
                        registeringPeriodDTOS){
                            System.out.print("대상 학년 : " + infra.dto.getAllowedYear() + " ");
                            System.out.print("시작 : " + infra.dto.getPeriodDTO().getBeginTime() + " ~ ");
                            System.out.print("종료 : " + infra.dto.getPeriodDTO().getEndTime());
                        }
                    } else {
                        System.out.println("조회 실패");
                        return;
                    }
                }
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void memberLookup() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.MEMBER_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                //TODO 학생 조회
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_STUD_LIST);
                sendProtocol.send(os);
                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        StudentDTO[] studentList = (StudentDTO[]) receiveProtocol.getObjectArray();
                        for (int i = 0; i < studentList.length; i++) {
                            System.out.printf("[ %d ] ", i + 1);
                            System.out.print("학과 : " + studentList[i].getDepartment());
                            System.out.print("학년 : " + studentList[i].getYear());
                            System.out.print("이름 : " + studentList[i].getName());
                            System.out.print("학번 : " + studentList[i].getStudentCode());
                            System.out.println("생년월일 : " + studentList[i].getBirthDate());
                        }
                    } else {
                        System.out.println("조회 실패");
                        return;
                    }
                }
            } else if (menu == 2) {
                //TODO 교수 조회
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PROF_LIST);
                sendProtocol.send(os);
                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        ProfessorDTO[] professorList = (ProfessorDTO[]) receiveProtocol.getObjectArray();
                        for (int i = 0; i < professorList.length; i++) {
                            System.out.printf("[ %d ] ", i + 1);
                            System.out.print("학과 : " + professorList[i].getDepartment());
                            System.out.print("이름 : " + professorList[i].getName());
                            System.out.print("학번 : " + professorList[i].getProfessorCode());
                            System.out.print("전화번호 : " + professorList[i].getTelePhone());
                            System.out.println("생년월일 : " + professorList[i].getBirthDate());
                        }
                    } else {
                        System.out.println("조회 실패");
                        return;
                    }
                }
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lectureLookup() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
    }
}

//    public enum Menu {
//        CREATE_ACCOUNT(1),
//        COURSE_MANAGE(2),
//        LECTURE_MANAGE(3),
//        PLANNER_INPUT_PERIOD(4),
//        REGISTERING_PERIOD(5),
//        MEMBER_LOOKUP(6),
//        LECTURE_LOOKUP(7);
//        private final int value;
//        Menu(int value){
//            this.value = value;
//        }
//    }
