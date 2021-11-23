import java.util.Scanner;

public class StudentService {
    public static Scanner scanner = new Scanner(System.in);

    public static void run() {
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

    private static void updatePersonalInformation() {
        int menu = 0;
        //TODO 개인정보 조회하여 출력
        System.out.println(Message.UPDATE_PERSONAL_INFORMATION_MENU);
        menu = scanner.nextInt();// int 파싱 오류 처리 필요
        while (menu != 4) {
            if (menu == 1) {
                System.out.print(Message.CHANGE_NAME_INPUT);
                String name = scanner.nextLine();
                //TODO 서버에게 이름 변경 요청하기
            } else if (menu == 2) {
                System.out.print(Message.CHANGE_PHONE_NUMBER_INPUT);
                String phoneNumber = scanner.nextLine();
                //TODO 서버에게 전화번호 변경 요청하기
            } else if (menu == 3) {
                System.out.print(Message.CHANGE_PASSWORD_INPUT);
                String password = scanner.nextLine();
                //TODO 서버에게 비밀번호 변경 요청하기
            } else if (menu == 4) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private static void registering() {
        int menu = 0;
        while (menu != 4) {
            System.out.print(Message.REGISTERING_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();

            if (menu == 1) {
                System.out.print(Message.COURSE_CODE_INPUT);
                string lectureCode = scanner.nextLine();
                //수강 신청
            } else if (menu == 2) {
                System.out.print(Message.COURSE_CODE_INPUT);
                string lectureCode = scanner.nextLine();
                //수강 취소
            } else if (menu == 3) {
                //수강 신청 현황 조회
            } else if (menu == 4) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private static void lectureLookup() {
        //개설교과목 전체 목록 출력
    }

    private static void lecturePlannerLookup() {
        int menu = 0;
        while (menu != 2) {
            System.out.print(Message.LECTURE_PLANNER_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = scanner.nextInt();

            if (menu == 1) {
                System.out.print(Message.COURSE_CODE_INPUT);
                String lectureCode = scanner.nextLine();
                //선택 교과목 강의 계획서 조회
            } else if (menu == 2) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private static void timeTableLookup() {
        //시간표 출력
    }

}
