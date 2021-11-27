import dto.CourseDTO;
import dto.ProfessorDTO;
import dto.StudentDTO;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AdminService implements EnrollmentService {
    public static Scanner scanner = new Scanner(System.in);

    private InputStream is;
    private OutputStream os;


    public AdminService(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() throws IOException, IllegalAccessException {
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

    private void createAccount() throws IllegalAccessException, IOException {
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
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_ACCOUNT);
                sendProtocol.setObject(studentDTO);
                sendProtocol.send(os);

                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("계정생성 성공");
                    } else {
                        System.out.println("계정생성 실패");
                    }
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

                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_ACCOUNT);
                sendProtocol.setObject(professorDTO);
                sendProtocol.send(os);

                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("계정생성 성공");
                    } else {
                        System.out.println("계정생성 실패");
                    }
                }

                //TODO 서버로 memberCode 전송해 계정 생성을 요청한다.
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

                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_COURSE);
                sendProtocol.setObject(courseDTO);
                sendProtocol.send(os);

                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("과목 생성 성공");
                    } else {
                        System.out.println("과목 생성 실패");
                        //잘못된 입력? 이미존재하는 과목?
                    }
                }

            } else if (menu == 2) {
                System.out.print(Message.UPDATE_COURSE_INPUT);
                String courseCode = scanner.nextLine();
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_COURSE);
                CourseDTO courseDTO = CourseDTO.builder()
                        .courseCode(courseCode)
                        .build();
                sendProtocol.setObject(courseDTO);
                sendProtocol.send(os);

                //해당과목 정보조회
                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("조회성공");
                        courseDTO = (CourseDTO) receiveProtocol.getObject();
                    } else {
                        System.out.println("과목 생성 실패");
                        return;
                    }
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
                            sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_COURSE);
                            sendProtocol.setObject(courseDTO);
                            sendProtocol.send(os);
                            receiveProtocol = new Protocol();
                            while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                                receiveProtocol.read(is);
                                int result = receiveProtocol.getType();
                                if (result == Protocol.T2_CODE_SUCCESS) {
                                    System.out.println("업데이트 되었습니다.");
                                } else {
                                    System.out.println("업데이트 실패");
                                    return;
                                }
                            }
                            break;
                        case 7:
                            break;
                        default:
                            System.out.println(Message.WRONG_INPUT_NOTICE);
                            break;
                    }
                }

//findByOption 제외 다 DTO에 담아서 보낸다.


                //TODO 서버에서 현재 정보를 받아와서 print 해주고 새로운 입력을 받는다.

            } else if (menu == 3) {
                System.out.print(Message.DELETE_COURSE_INPUT);
                String courseCode = scanner.nextLine();
                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_DELETE, Protocol.ENTITY_COURSE);
                CourseDTO courseDTO = CourseDTO.builder()
                        .courseCode(courseCode)
                        .build();
                sendProtocol.setObject(courseDTO);
                sendProtocol.send(os);

                Protocol receiveProtocol = new Protocol();
                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
                    receiveProtocol.read(is);
                    int result = receiveProtocol.getType();
                    if (result == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("삭제 성공");
                    } else {
                        System.out.println("삭제 실패");
                        return;
                    }
                }
            } else if (menu == 4) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lectureManage() {
        int menu = 0;
        while (menu != 4) {
            System.out.println(Message.COURSE_MANAGE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();
            scanner.nextLine();// int 파싱 오류 처리 필요
            if (menu == 1) {
                System.out.println(Message.CREATE_LECTURE);
                //TODO 사용자로 부터 개설 교과목 정보를 입력받고 서버에게 개설 교과목 생성을 요청한다.

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

    private void plannerInputPeriodSettings() {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.PLANNER_INPUT_PERIOD_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.print(Message.BEGIN_PERIOD_INPUT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 hh시 mm분");
                LocalDateTime begin = LocalDateTime.parse(scanner.nextLine(), formatter); // format이랑 일치하지 않으면 오류 발생
                System.out.print(Message.END_PERIOD_INPUT);
                LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);  // format이랑 일치하지 않으면 오류 발생
                //TODO 사용자로부터 입력받은 기간을 서버에게 등록 요청한다.

            } else if (menu == 2) {
                //TODO 서버에게 현재 등록되어있는 강의계획서 등록 기간 정보를 받아와서 출력한다.
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void registeringPeriodSettings() {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.REGISTERING_PERIOD_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.print(Message.BEGIN_PERIOD_INPUT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 hh시 mm분");
                LocalDateTime begin = LocalDateTime.parse(scanner.nextLine(), formatter);
                System.out.print(Message.END_PERIOD_INPUT);
                LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);
                System.out.println(Message.TARGET_GRADE_INPUT);
                int grade = scanner.nextInt();
                //TODO 사용자로부터 입력받은 기간을 서버에게 등록 요청한다.

            } else if (menu == 2) {
                //TODO 서버에게 현재 등록되어있는 수강신청 기간 정보를 받아와서 출력한다.

            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void memberLookup() {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.MEMBER_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                //TODO 학생 조회
            } else if (menu == 2) {
                //TODO 교수 조회
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lectureLookup() {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.LECTURE_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                //TODO 전체 조회
            } else if (menu == 2) {
                //TODO 조건부 조회
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
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
