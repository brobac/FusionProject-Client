import java.util.Scanner;

public class ProfessorService {
    public static Scanner scanner = new Scanner(System.in);

    public static void run() {
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

    private static void lectureLookup() {
        int menu = 0;
        menu = scanner.nextInt();

    }

    private static void lecturePlannerSettings() {
        int menu = 0;
        //TODO
        System.out.print(Message.LECTURE_PLANNER_SETTINGS_MENU);
        menu = scanner.nextInt();// int 파싱 오류 처리 필요
        while (menu != 3) {
            if (menu == 1) {
                System.out.print(Message.LECTURE_PLANNER_INSERT_INPUT);
                String lectureCode = scanner.nextLine();
                //TODO 담당과목인지 체크 후 맞을 경우만 진행
                if (true /*담당과목인지 체크해서 불린값 리턴하자 */) {

                } else {
                    System.out.println(Message.MISMATCH_LECTURE_CODE);
                }
            } else if (menu == 2) {
                System.out.print(Message.LECTURE_PLANNER_UPDATE_INPUT);
                String lectureCode = scanner.nextLine();
                //TODO 담당과목인지 체크 후 맞을 경우만 진행
                if (true /*담당과목인지 체크해서 불린값 리턴하자 */) {

                } else {
                    System.out.println(Message.MISMATCH_LECTURE_CODE);
                }
            } else if (menu == 3) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
        menu = scanner.nextInt();
    }

    private static void lecturePlannerLookup() {
        int menu = 0;
        menu = scanner.nextInt();
    }

    private static void studentListLookup() {
    }

    private static void timeTableLookup() {
    }


}












