package controller;

import infra.dto.*;
import network.AdminProtocolService;
import network.Protocol;

import java.io.InputStream;
import java.io.OutputStream;
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
            menu = Integer.parseInt(scanner.nextLine());
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
//                    memberLookup();
                    break;
                case 7:
//                    lectureLookup();
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
            menu = Integer.parseInt(scanner.nextLine());
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
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("과목 생성 성공");
                } else {
                    System.out.println("과목 생성 실패");
                }
            } else if (menu == 2) {
                ps.reqReadAllCourse();
                Protocol receiveProtocol = ps.response();
                CourseDTO[] courseDTOS = (CourseDTO[]) receiveProtocol.getObjectArray();
                for (int i = 0; i < courseDTOS.length; i++) {
                    System.out.printf("[ %d ]", i + 1);
                    System.out.print("  과목명 : " + courseDTOS[i].getCourseName());
                    System.out.print("  과목코드 : " + courseDTOS[i].getCourseCode());
                    System.out.print("  학과 : " + courseDTOS[i].getDepartment());
                    System.out.print("  대상학년 : " + courseDTOS[i].getTargetYear());
                    System.out.println("  학점 : " + courseDTOS[i].getCredit());
                }
                System.out.print(Message.UPDATE_COURSE_INPUT);
                int courseNum = Integer.parseInt(scanner.nextLine());
                CourseDTO courseDTO = courseDTOS[courseNum - 1];
                System.out.println("----------현재 정보----------");
                System.out.println("과목명 : " + courseDTO.getCourseName());
                System.out.println("과목코드 : " + courseDTO.getCourseCode());
                System.out.println("학과 : " + courseDTO.getDepartment());
                System.out.println("대상학년 : " + courseDTO.getTargetYear());
                System.out.println("학점 : " + courseDTO.getCredit());
                int updateMenu = 0;
                while (updateMenu != 6) {
                    System.out.println(Message.UPDATE_COURSE_MENU);
                    System.out.print(Message.INPUT);
                    updateMenu = Integer.parseInt(scanner.nextLine());
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
                            int result = receiveProtocol.getCode();
                            if (result == Protocol.T2_CODE_SUCCESS) {
                                System.out.println("업데이트 되었습니다.");
                                System.out.println("----------새로운 정보----------");
                                System.out.println("과목명 : " + courseDTO.getCourseName());
                                System.out.println("과목코드 : " + courseDTO.getCourseCode());
                                System.out.println("학과 : " + courseDTO.getDepartment());
                                System.out.println("대상학년 : " + courseDTO.getTargetYear());
                                System.out.println("학점 : " + courseDTO.getCredit());
                            } else {
                                System.out.println("업데이트 실패");
                                return;
                            }
                            break;
                        default:
                            System.out.println(Message.WRONG_INPUT_NOTICE);
                            break;
                    }
                }
            } else if (menu == 3) {
                ps.reqReadAllCourse();
                Protocol receiveProtocol = ps.response();
                CourseDTO[] courseDTOS = (CourseDTO[]) receiveProtocol.getObjectArray();
                for (int i = 0; i < courseDTOS.length; i++) {
                    System.out.printf("[ %d ]", i + 1);
                    System.out.print("  과목명 : " + courseDTOS[i].getCourseName());
                    System.out.print("  과목코드 : " + courseDTOS[i].getCourseCode());
                    System.out.print("  학과 : " + courseDTOS[i].getDepartment());
                    System.out.print("  대상학년 : " + courseDTOS[i].getTargetYear());
                    System.out.println("  학점 : " + courseDTOS[i].getCredit());
                }
                System.out.print(Message.DELETE_COURSE_INPUT);
                int courseNum = Integer.parseInt(scanner.nextLine());
                CourseDTO courseDTO = courseDTOS[courseNum - 1];
                ps.reqDeleteCourse(courseDTO);
                receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
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
                ps.reqReadAllCourse();
                Protocol receiveProtocol = ps.response();
                CourseDTO[] courseDTOS = (CourseDTO[]) receiveProtocol.getObjectArray();
                for (int i = 0; i < courseDTOS.length; i++) {
                    System.out.printf("[ %d ]", i + 1);
                    System.out.print("  과목명 : " + courseDTOS[i].getCourseName());
                    System.out.print("  과목코드 : " + courseDTOS[i].getCourseCode());
                    System.out.print("  학과 : " + courseDTOS[i].getDepartment());
                    System.out.print("  대상학년 : " + courseDTOS[i].getTargetYear());
                    System.out.println("  학점 : " + courseDTOS[i].getCredit());
                }
                System.out.println(Message.CREATE_LECTURE_INPUT);
                System.out.print(Message.INPUT);
                int courseNum = Integer.parseInt(scanner.nextLine());
                CourseDTO course = courseDTOS[courseNum - 1];
                System.out.print("LectureCode : ");
                String lectureCode = scanner.nextLine();
                System.out.print("제한인원 : ");
                int limit = Integer.parseInt(scanner.nextLine());
                System.out.print("담당교수코드 : ");
                String lecturerCode = scanner.nextLine();
                Set<LectureTimeDTO> lectureTimeDTOSet = new HashSet<>();
                int option = 0;
                while (option != 2) {
                    System.out.println("----- 강의시간 입력 -----");
                    System.out.print("요일 : ");
                    String day = scanner.nextLine();
                    System.out.print("시작 교시 : ");
                    int startTime = Integer.parseInt(scanner.nextLine());
                    System.out.print("끝 교시 : ");
                    int endTime = Integer.parseInt(scanner.nextLine());
                    System.out.print("강의실 : ");
                    String room = scanner.nextLine();
                    LectureTimeDTO lectureTime = LectureTimeDTO.builder()
                            .lectureDay(day)
                            .startTime(startTime)
                            .endTime(endTime)
                            .room(room)
                            .lectureName(course.getCourseName())
                            .build();
                    lectureTimeDTOSet.add(lectureTime);
                    System.out.println("[1] 추가입력  [2]종료");
                    System.out.print(Message.INPUT);
                    option = Integer.parseInt(scanner.nextLine());
                }
                LectureDTO lectureDTO = LectureDTO.builder()
                        .course(course)
                        .lectureCode(lectureCode)
                        .limit(limit)
                        .lectureTimes(lectureTimeDTOSet)
                        .planner(new LecturePlannerDTO())
                        .professor(ProfessorDTO.builder().professorCode(lecturerCode).build())
                        .build();

                ps.reqCreateLecture(lectureDTO);
                receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("개설 교과목 등록 성공");
                } else {
                    System.out.println("개설 교과목 등록 실패");
                }

            } else if (menu == 2) {
                ps.reqAllLectureList();
                Protocol response = ps.response();
                LectureDTO[] lectureList = (LectureDTO[]) response.getObjectArray();
                printLectureList(lectureList);
                System.out.print(Message.UPDATE_LECTURE_INPUT);
                int lectureNum = Integer.parseInt(scanner.nextLine());
                LectureDTO updateLecture = lectureList[lectureNum - 1];
                System.out.println("-----현재 개설 교과목 정보-----");
                printLecture(updateLecture);
                int updateMenu = 0;
                while (updateMenu != 5) {
                    System.out.println(Message.UPDATE_LECTURE_MENU);
                    updateMenu = Integer.parseInt(scanner.nextLine());
                    if (updateMenu == 1) {
                        System.out.print(Message.COURSE_CODE_INPUT);
                        updateLecture.setLectureCode(scanner.nextLine());
                    } else if (updateMenu == 2) {
                        //교수목록조회 밑 출력
                        System.out.print(Message.PROF_CODE_INPUT);
                        updateLecture.setProfessor(ProfessorDTO.builder().professorCode(scanner.nextLine()).build());
                    } else if (updateMenu == 3) {
                        LectureTimeDTO[] times = updateLecture.getLectureTimes();
                        int option = 0;
                        while (option != 2) {
                            for (int i = 0; i < updateLecture.getLectureTimes().length; i++) {
                                LectureTimeDTO time = times[i];
                                System.out.printf("[ %d ]", i + 1);
                                System.out.println("요일 : " + time.getLectureDay() + "  시작교시 : " + time.getStartTime() + "  끝교시 : " + time.getEndTime() + "  강의실 : " + time.getRoom());
                            }
                            System.out.print("변경할 강의시간 번호를 입력해주세요 : ");
                            int timeNum = Integer.parseInt(scanner.nextLine());
                            System.out.println("----- 강의시간 입력 -----");
                            System.out.print("요일 : ");
                            times[timeNum - 1].setLectureDay(scanner.nextLine());
                            System.out.print("시작 교시 : ");
                            times[timeNum - 1].setStartTime(Integer.parseInt(scanner.nextLine()));
                            System.out.print("끝 교시 : ");
                            times[timeNum - 1].setEndTime(Integer.parseInt(scanner.nextLine()));
                            System.out.print("강의실 : ");
                            times[timeNum - 1].setRoom(scanner.nextLine());
                            System.out.println("[1] 추가변경  [2]완료");
                            System.out.print(Message.INPUT);
                            option = Integer.parseInt(scanner.nextLine());
                        }
                    } else if (updateMenu == 4) {
                        System.out.print("제한인원 : ");
                        updateLecture.setLimit(Integer.parseInt(scanner.nextLine()));
                    } else if (updateMenu == 5) {
                        printLecture(updateLecture);
                        ps.reqUpdateLecture(updateLecture);
                        response = ps.response();
                        int result = response.getCode();
                        if (result == Protocol.T2_CODE_SUCCESS) {
                            System.out.println("개설 교과목 수정에 성공했습니다.");
                            printLecture(updateLecture);
                        } else {
                            System.out.println("개설 교과목 수정에 실패했습니다.");
                        }
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }


            } else if (menu == 3) {
                ps.reqAllLectureList();
                Protocol response = ps.response();
                LectureDTO[] lectureList = (LectureDTO[]) response.getObjectArray();
                printLectureList(lectureList);
                System.out.print(Message.DELETE_LECTURE_INPUT);
                int lectureNum = Integer.parseInt(scanner.nextLine());

                ps.reqDeleteLecture(lectureList[lectureNum - 1]);
                response = ps.response();

                int result = response.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("개설 교과목 삭제를 성공했습니다.");
                } else {
                    System.out.println("개설 교과목 삭제에 실패했습니다.");
                }

            } else if (menu == 4) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }


    private void plannerInputPeriodSettings() throws Exception {
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
                ps.reqCreatePlannerPeriod(periodDTO);
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("강의 계획서 입력기간 등록 성공");
                } else {
                    System.out.println("강의 계획서 입력기간 등록 실패");
                    return;
                }
            } else if (menu == 2) {
                ps.reqReadPlannerPeriod();
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    PeriodDTO periodDTO = (PeriodDTO) receiveProtocol.getObject();
                    System.out.println("시작 : " + periodDTO.getBeginTime() + " ~ ");
                    System.out.println("종료 : " + periodDTO.getEndTime());
                } else {
                    System.out.println("조회 실패");
                    return;
                }
            } else if (menu == 3) {
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }

    }

    private void registeringPeriodSettings() throws Exception {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.REGISTERING_PERIOD_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());
            if (menu == 1) {
                ps.reqReadRegisteringPeriod();
                Protocol receiveProtocol = ps.response();
                RegisteringPeriodDTO[] registeringPeriodDTOs = (RegisteringPeriodDTO[]) receiveProtocol.getObjectArray();
                if (registeringPeriodDTOs != null) {
                    for (int i = 0; i < registeringPeriodDTOs.length; i++) {
                        System.out.printf("[ %d ]", i + 1);
                        System.out.print("학년 : " + registeringPeriodDTOs[i].getAllowedYear());
                        System.out.print("시작일 : " + registeringPeriodDTOs[i].getPeriodDTO().getBeginTime().toString());
                        System.out.print("종료일 : " + registeringPeriodDTOs[i].getPeriodDTO().getEndTime().toString());
                    }
                }
            } else if (menu == 2) {
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

                ps.reqCreateRegisteringPeriod(registeringPeriodDTO);
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("수강신청 기간 등록 성공");
                } else {
                    System.out.println("수강신청 기간 등록 실패");
                    return;
                }
            } else if (menu == 3) {//삭제
            } else if (menu == 4) {//나가기
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }

    }

    private void printLecture(LectureDTO lecture) {
        System.out.println("교과목명 : " + lecture.getCourse().getCourseName());
        System.out.println("  학점 : " + lecture.getCourse().getCredit());
        System.out.println("  과목코드 : " + lecture.getLectureCode());
        System.out.println("  담당교수 : " + lecture.getProfessor().getName());
        System.out.println("  수강학과 : " + lecture.getCourse().getDepartment());
        System.out.print("  강의시간(강의실) : "); // 반복문필요
        for (LectureTimeDTO lectureTime : lecture.getLectureTimes()) {
            System.out.print(lectureTime.getLectureDay() + " " + lectureTime.getStartTime() + "~" + lectureTime.getEndTime() + "  강의실 : " + lectureTime.getRoom()  + "/");
        }
        System.out.println("  제한인원 : " + lecture.getLimit());
        System.out.println("  수강인원 : " + lecture.getApplicant());
    }

    private void printLectureList(LectureDTO[] lectureList) {
        for (int i = 0; i < lectureList.length; i++) {
            LectureDTO curLecture = lectureList[i];
            System.out.printf("[ %d ]", i + 1);
            // 과목명, 대상학년, 과목코드, 제한인원, 신청인원, 학과, 학점, 교수명
            System.out.print("교과목명 : " + curLecture.getCourse().getCourseName());
            System.out.print("  학점 : " + curLecture.getCourse().getCredit());
            System.out.print("  과목코드 : " + curLecture.getLectureCode());
            System.out.print("  담당교수 : " + curLecture.getProfessor().getName());
            System.out.print("  수강학과 : " + curLecture.getCourse().getDepartment());
            System.out.print("  강의시간(강의실) : "); // 반복문필요
            for (LectureTimeDTO lectureTime : curLecture.getLectureTimes()) {
                System.out.print(lectureTime.getLectureDay() + " " + lectureTime.getStartTime() + "~" + lectureTime.getEndTime() + "  강의실 : " + lectureTime.getRoom() + "/");
            }
            System.out.println("  제한인원 : " + curLecture.getLimit());
            System.out.println("  수강인원 : " + curLecture.getApplicant());
        }
    }


//    private void memberLookup() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
//        int menu = 0;
//        while (menu != 3) {
//            System.out.println(Message.MEMBER_LOOKUP_MENU);
//            System.out.print(Message.INPUT);
//            menu = scanner.nextInt();// int 파싱 오류 처리 필요
//            scanner.nextLine();
//            if (menu == 1) {
//                //TODO 학생 조회
//                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_STUD_LIST);
//                sendProtocol.send(os);
//                Protocol receiveProtocol = new Protocol();
//                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
//                    receiveProtocol.read(is);
//                    int result = receiveProtocol.getType();
//                    if (result == Protocol.T2_CODE_SUCCESS) {
//                        StudentDTO[] studentList = (StudentDTO[]) receiveProtocol.getObjectArray();
//                        for (int i = 0; i < studentList.length; i++) {
//                            System.out.printf("[ %d ] ", i + 1);
//                            System.out.print("학과 : " + studentList[i].getDepartment());
//                            System.out.print("학년 : " + studentList[i].getYear());
//                            System.out.print("이름 : " + studentList[i].getName());
//                            System.out.print("학번 : " + studentList[i].getStudentCode());
//                            System.out.println("생년월일 : " + studentList[i].getBirthDate());
//                        }
//                    } else {
//                        System.out.println("조회 실패");
//                        return;
//                    }
//                }
//            } else if (menu == 2) {
//                //TODO 교수 조회
//                Protocol sendProtocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PROF_LIST);
//                sendProtocol.send(os);
//                Protocol receiveProtocol = new Protocol();
//                while (receiveProtocol.getType() == Protocol.UNDEFINED) {
//                    receiveProtocol.read(is);
//                    int result = receiveProtocol.getType();
//                    if (result == Protocol.T2_CODE_SUCCESS) {
//                        ProfessorDTO[] professorList = (ProfessorDTO[]) receiveProtocol.getObjectArray();
//                        for (int i = 0; i < professorList.length; i++) {
//                            System.out.printf("[ %d ] ", i + 1);
//                            System.out.print("학과 : " + professorList[i].getDepartment());
//                            System.out.print("이름 : " + professorList[i].getName());
//                            System.out.print("학번 : " + professorList[i].getProfessorCode());
//                            System.out.print("전화번호 : " + professorList[i].getTelePhone());
//                            System.out.println("생년월일 : " + professorList[i].getBirthDate());
//                        }
//                    } else {
//                        System.out.println("조회 실패");
//                        return;
//                    }
//                }
//            } else if (menu == 3) {
//            } else {
//                System.out.println(Message.WRONG_INPUT_NOTICE);
//            }
//        }
//    }
//
//    private void lectureLookup() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
//    }
//}

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
}
