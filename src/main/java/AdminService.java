import dto.AccountDTO;

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

    public void run() {
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

    private void createAccount() {
        int menu = 0;
        String memberCode;
        while (menu != 3) {
            System.out.println(Message.CREATE_ACCOUNT_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();  // int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.print(Message.STUDENT_CODE_INPUT);
                memberCode = scanner.nextLine();
                AccountDTO accountDTO
                //TODO 서버로 memberCode 전송해 계정 생성을 요청한다.
            } else if (menu == 2) {
                System.out.print(Message.PROFESSOR_CODE_INPUT);
                memberCode = scanner.nextLine();
                //TODO 서버로 memberCode 전송해 계정 생성을 요청한다.
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void courseManage() {
        int menu = 0;
        String courseCode;
        while (menu != 4) {
            System.out.println(Message.COURSE_MANAGE_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();// int 파싱 오류 처리 필요
            scanner.nextLine();
            if (menu == 1) {
                System.out.println(Message.CREATE_COURSE);
                //TODO 사용자로 부터 과목 정보를 입력받고 서버에게 교과목 생성을 요청한다.

            } else if (menu == 2) {
                System.out.print(Message.UPDATE_COURSE_INPUT);
                courseCode = scanner.nextLine();
                //TODO 서버에서 현재 정보를 받아와서 print 해주고 새로운 입력을 받는다.

            } else if (menu == 3) {
                System.out.print(Message.DELETE_COURSE_INPUT);
                courseCode = scanner.nextLine();
                //TODO 사용자로부터 입력받은 과목코드에 해당하는 과목을 서버에게 삭제 요청한다.

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
