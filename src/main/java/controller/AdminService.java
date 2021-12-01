package controller;

import infra.database.option.lecture.LectureDepartmentOption;
import infra.database.option.lecture.LectureNameOption;
import infra.database.option.lecture.LectureOption;
import infra.database.option.lecture.YearOption;
import infra.database.option.professor.NameOption;
import infra.database.option.professor.ProfessorOption;
import infra.database.option.student.DepartmentOption;
import infra.database.option.student.StudentOption;
import infra.database.option.student.StudentYearOption;
import infra.dto.*;
import network.AdminProtocolService;
import network.Protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                    memberLookup();
                    break;
                case 7:
                    lectureLookup();
                    break;
                case 8:
                    logout();
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
                CourseDTO[] courseDTOS = null;

                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    courseDTOS = (CourseDTO[]) receiveProtocol.getObjectArray();
                    for (int i = 0; i < courseDTOS.length; i++) {
                        System.out.printf("[ %d ]", i + 1);
                        System.out.print("  과목명 : " + courseDTOS[i].getCourseName());
                        System.out.print("  과목코드 : " + courseDTOS[i].getCourseCode());
                        System.out.print("  학과 : " + courseDTOS[i].getDepartment());
                        System.out.print("  대상학년 : " + courseDTOS[i].getTargetYear());
                        System.out.println("  학점 : " + courseDTOS[i].getCredit());
                    }
                }else {
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.println(failMsg);
                    break;
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
                CourseDTO[] courseDTOS = null;

                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    courseDTOS = (CourseDTO[]) receiveProtocol.getObjectArray();
                    for (int i = 0; i < courseDTOS.length; i++) {
                        System.out.printf("[ %d ]", i + 1);
                        System.out.print("  과목명 : " + courseDTOS[i].getCourseName());
                        System.out.print("  과목코드 : " + courseDTOS[i].getCourseCode());
                        System.out.print("  학과 : " + courseDTOS[i].getDepartment());
                        System.out.print("  대상학년 : " + courseDTOS[i].getTargetYear());
                        System.out.println("  학점 : " + courseDTOS[i].getCredit());
                    }
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.println(failMsg);
                    break;
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
                CourseDTO[] courseDTOS = null;
                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    courseDTOS = (CourseDTO[]) receiveProtocol.getObjectArray();
                    for (int i = 0; i < courseDTOS.length; i++) {
                        System.out.printf("[ %d ]", i + 1);
                        System.out.print("  과목명 : " + courseDTOS[i].getCourseName());
                        System.out.print("  과목코드 : " + courseDTOS[i].getCourseCode());
                        System.out.print("  학과 : " + courseDTOS[i].getDepartment());
                        System.out.print("  대상학년 : " + courseDTOS[i].getTargetYear());
                        System.out.println("  학점 : " + courseDTOS[i].getCredit());
                    }
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[교과목] : ");
                    System.out.println(failMsg);
                    break;
                }

                System.out.println(Message.CREATE_LECTURE_INPUT);
                System.out.print(Message.INPUT);
                int courseNum = Integer.parseInt(scanner.nextLine());
                CourseDTO course = courseDTOS[courseNum - 1];
                System.out.print("LectureCode : ");
                String lectureCode = scanner.nextLine();
                System.out.print("제한인원 : ");
                int limit = Integer.parseInt(scanner.nextLine());

                ps.reqReadAllProfessor();
                Protocol response = ps.response();
                ProfessorDTO[] professorDTOS = null;
                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    professorDTOS = (ProfessorDTO[]) response.getObjectArray();
                    for (int i = 0; i < professorDTOS.length; i++) {
                        ProfessorDTO professor = professorDTOS[i];
                        System.out.printf("[ %d ]", i + 1);
                        System.out.print("  학과 : " + professor.getDepartment());
                        System.out.print("  이름 : " + professor.getName());
                        System.out.print("  교수코드 : " + professor.getProfessorCode());
                        System.out.println("  전화번호 : " + professor.getTelePhone());
                    }
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[교수] : ");
                    System.out.println(failMsg);
                    break;
                }

                System.out.print(Message.PROF_CODE_INPUT);
                int profNum = Integer.parseInt(scanner.nextLine());
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
                        .professor(professorDTOS[profNum - 1])
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
                LectureDTO[] lectureList = null;
                if(response.getCode()==Protocol.T2_CODE_SUCCESS){
                    lectureList = (LectureDTO[]) response.getObjectArray();
                    printLectureList(lectureList);
                }else{
                    MessageDTO failMsg = (MessageDTO) response.getObject();
                    System.out.print("[개설교과목] : ");
                    System.out.println(failMsg);
                    break;
                }


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
                        ps.reqReadAllProfessor();
                        response = ps.response();
                        ProfessorDTO[] professorDTOS = (ProfessorDTO[]) response.getObjectArray();
                        for (int i = 0; i < professorDTOS.length; i++) {
                            ProfessorDTO professor = professorDTOS[i];
                            System.out.printf("[ %d ]", i + 1);
                            System.out.print("  학과 : " + professor.getDepartment());
                            System.out.print("  이름 : " + professor.getName());
                            System.out.print("  교수코드 : " + professor.getProfessorCode());
                            System.out.println("  전화번호 : " + professor.getTelePhone());
                        }
                        System.out.print(Message.PROF_CODE_INPUT);
                        int profNum = Integer.parseInt(scanner.nextLine());
                        updateLecture.setProfessor(professorDTOS[profNum - 1]);
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
                LectureDTO[] lectureList = null;
                if(response.getCode()==Protocol.T2_CODE_SUCCESS){
                    lectureList = (LectureDTO[]) response.getObjectArray();
                    printLectureList(lectureList);
                }else{
                    MessageDTO failMsg = (MessageDTO) response.getObject();
                    System.out.print("[개설교과목] : ");
                    System.out.println(failMsg);
                    break;
                }

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
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[강의계획서 입력기간] : ");
                    System.out.println(failMsg);
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
                readRegisteringPeriods();
            } else if (menu == 2) {
                System.out.print(Message.BEGIN_PERIOD_INPUT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime begin = LocalDateTime.parse(scanner.nextLine(), formatter);
                System.out.print(Message.END_PERIOD_INPUT);
                LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);
                System.out.println(Message.TARGET_GRADE_INPUT);
                int grade = Integer.parseInt(scanner.nextLine());
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
                RegisteringPeriodDTO[] regPeriods = readRegisteringPeriods();
                if(regPeriods==null){
                    break;
                }

                System.out.print(Message.BEGIN_PERIOD_INPUT);
                int regPeriodNum = Integer.parseInt(scanner.nextLine());

                if (regPeriodNum - 1 > regPeriods.length) {
                    System.out.println(Message.WRONG_INPUT_NOTICE);
                    break;
                }

                ps.reqDeleteRegPeriod(regPeriods[regPeriodNum - 1]);
                Protocol receiveProtocol = ps.response();
                int result = receiveProtocol.getCode();
                if (result == Protocol.T2_CODE_SUCCESS) {
                    System.out.println("수강신청 기간 삭제 성공");
                } else {
                    System.out.println("수강신청 기간 삭제 실패");
                    return;
                }
            } else if (menu == 4) {//나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private RegisteringPeriodDTO[] readRegisteringPeriods() throws Exception {
        ps.reqReadRegisteringPeriod();
        Protocol receiveProtocol = ps.response();
        RegisteringPeriodDTO[] registeringPeriodDTOs = null;
        if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
            registeringPeriodDTOs = (RegisteringPeriodDTO[]) receiveProtocol.getObjectArray();
            if (registeringPeriodDTOs != null) {
                for (int i = 0; i < registeringPeriodDTOs.length; i++) {
                    System.out.printf("[ %d ]", i + 1);
                    System.out.print("학년 : " + registeringPeriodDTOs[i].getAllowedYear());
                    System.out.print("시작일 : " + registeringPeriodDTOs[i].getPeriodDTO().getBeginTime().toString());
                    System.out.print("종료일 : " + registeringPeriodDTOs[i].getPeriodDTO().getEndTime().toString());
                    System.out.println();
                }
            }
        }else{
            MessageDTO failMsg = (MessageDTO)receiveProtocol.getObject();
            System.out.print("[수강신청기간] : ");
            System.out.println(failMsg);
            return null;
        }
        return registeringPeriodDTOs;
    }

    private void printLecture(LectureDTO lecture) {
        System.out.println("교과목명 : " + lecture.getCourse().getCourseName());
        System.out.println("  학점 : " + lecture.getCourse().getCredit());
        System.out.println("  과목코드 : " + lecture.getLectureCode());
        System.out.println("  담당교수 : " + lecture.getProfessor().getName());
        System.out.println("  수강학과 : " + lecture.getCourse().getDepartment());
        System.out.print("  강의시간(강의실) : "); // 반복문필요
        for (LectureTimeDTO lectureTime : lecture.getLectureTimes()) {
            System.out.print(lectureTime.getLectureDay() + " " + lectureTime.getStartTime() + "~" + lectureTime.getEndTime() + "  강의실 : " + lectureTime.getRoom() + "/");
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


    private void memberLookup() throws Exception {
        int menu = 0;
        while (true) {
            System.out.println(Message.MEMBER_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());// int 파싱 오류 처리 필요

            if (menu == 1) { //학생조회
                lookupStudent();
            } else if (menu == 2) { //교수조회
                lookupProfessor();
            } else if (menu == 3) {
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lookupProfessor() throws Exception {
        int menu = 0;
        while (true) {
            System.out.println(Message.MEMBER_LOOKUP_MENU_CATEGORIES);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());// int 파싱 오류 처리 필요

            if (menu == 1) { //전체조회
                ps.reqReadAllProfessor();
                Protocol receiveProtocol = ps.response();
                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    ProfessorDTO[] profArr = (ProfessorDTO[]) receiveProtocol.getObjectArray();
                    printProfessorList(profArr);
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[교수] : ");
                    System.out.println(failMsg);
                    break;
                }
            } else if (menu == 2) { //조건조회
                lookupProfessorByOption();
            } else if (menu == 3) {//나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lookupProfessorByOption() throws Exception {
        List<ProfessorOption> optionList = new ArrayList();
        int menu = 0;
        while (true) {
            System.out.println(Message.PROFESSOR_LOOKUP_OPTION_CATEGORIES);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());// int 파싱 오류 처리 필요

            if (menu == 1) { // 학과조건
                System.out.print(Message.INPUT);
                String dep = scanner.nextLine();
                optionList.add(new infra.database.option.professor.DepartmentOption(dep));
            } else if (menu == 2) { //이름조건
                System.out.print(Message.INPUT);
                String name = scanner.nextLine();
                optionList.add(new NameOption(name));
            } else if (menu == 3) {//조회하기
                ps.reqReadProfessorByOption(
                        optionList.toArray(new ProfessorOption[optionList.size()])
                );
                Protocol receiveProtocol = ps.response();
                ProfessorDTO[] profArr = (ProfessorDTO[]) receiveProtocol.getObjectArray();
                printProfessorList(profArr);
                break;
            } else if (menu == 4) { //나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lookupStudent() throws Exception {
        int menu = 0;
        while (true) {
            System.out.println(Message.MEMBER_LOOKUP_MENU_CATEGORIES);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());// int 파싱 오류 처리 필요

            if (menu == 1) { //전체조회
                ps.reqReadAllStudent();
                Protocol receiveProtocol = ps.response();
                if(receiveProtocol.getCode()== Protocol.T2_CODE_SUCCESS){
                    StudentDTO[] stdArr = (StudentDTO[]) receiveProtocol.getObjectArray();
                    printStudentList(stdArr);
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[학생] : ");
                    System.out.println(failMsg);
                    break;
                }
            } else if (menu == 2) { //조건조회
                lookupStudentByOption();
            } else if (menu == 3) {//나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void lookupStudentByOption() throws Exception {
        List<StudentOption> optionList = new ArrayList();
        int menu = 0;
        while (true) {
            System.out.println(Message.STUDENT_LOOKUP_OPTION_CATEGORIES);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());// int 파싱 오류 처리 필요

            if (menu == 1) { // 학과조건
                System.out.print(Message.INPUT);
                String dep = scanner.nextLine();
                optionList.add(new DepartmentOption(dep));
            } else if (menu == 2) { //학년조건
                System.out.print(Message.INPUT);
                String grade = scanner.nextLine();
                optionList.add(new StudentYearOption(grade));
            } else if (menu == 3) {//조회하기
                ps.reqReadStudentByOption(
                        optionList.toArray(new StudentOption[optionList.size()])
                );
                Protocol receiveProtocol = ps.response();
                StudentDTO[] stdArr = (StudentDTO[]) receiveProtocol.getObjectArray();
                printStudentList(stdArr);
                break;
            } else if (menu == 4) { //나가기
                break;
            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void printProfessorList(ProfessorDTO[] profArr) {
        System.out.printf("%1s %14s %8s %5s %4s %2s \n", "번호", "이름", "생년월일", "학과", "교번", "전화번호");
        int num = 0;
        for (ProfessorDTO prof : profArr) {
            num++;
            System.out.printf(
                    "%3s %17s %10s %10s %5s %4s\n",
                    num, prof.getName(), prof.getBirthDate(), prof.getDepartment(),
                    prof.getProfessorCode(), prof.getTelePhone()
            );
        }
    }

    private void printStudentList(StudentDTO[] stdArr) {
        System.out.printf("%1s %14s %8s %9s %4s %1s %1s\n", "번호", "이름", "생년월일", "학과", "학번", "학년", "수강학점");
        int num = 0;
        for (StudentDTO std : stdArr) {
            num++;
            System.out.printf(
                    "%3s %17s %10s %10s %5s %2s %3s\n",
                    num, std.getName(), std.getBirthdate(), std.getDepartment(),
                    std.getStudentCode(), std.getYear(), std.getCredit()
            );
        }
    }

    private void lectureLookup() throws Exception {
        int menu = 0;
        while (menu != 3) {
            System.out.println(Message.LECTURE_LOOKUP_MENU);
            System.out.print(Message.INPUT);
            menu = Integer.parseInt(scanner.nextLine());
            if (menu == 1) {
                ps.reqAllLectureList();  // 개설 교과목 (전체) 목록 요청
                Protocol receiveProtocol = ps.response();
                LectureDTO[] lectureList = null;
                if(receiveProtocol.getCode()==Protocol.T2_CODE_SUCCESS){
                    lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                    printLectureList(lectureList);
                }else{
                    MessageDTO failMsg = (MessageDTO) receiveProtocol.getObject();
                    System.out.print("[개설교과목] : ");
                    System.out.println(failMsg);
                    break;
                }

                int innerMenu = 0;
                while (innerMenu != 2) {
                    System.out.println(Message.LECTURE_LOOKUP_INNER_MENU);
                    System.out.print(Message.INPUT);
                    innerMenu = Integer.parseInt(scanner.nextLine());
                    if (innerMenu == 1) {
                        System.out.print(Message.PLANNER_LOOKUP_INPUT);
                        int lectureNum = Integer.parseInt(scanner.nextLine());
                        LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                        System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                        System.out.println("강의 목표 : " + planner.getGoal());
                        System.out.println("강의 개요 : " + planner.getSummary());
                    } else if (innerMenu == 2) {
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }

            } else if (menu == 2) {
                List<LectureOption> optionList = new ArrayList<>();
                System.out.println("-----조건설정-----");
                int optionMenu = 0;
                while (optionMenu != 4) {
                    System.out.println("[1]대상학년  [2]학과  [3]과목명  [4]조회하기");
                    optionMenu = Integer.parseInt(scanner.nextLine());
                    if (optionMenu == 1) {
                        System.out.print(Message.TARGET_GRADE_INPUT);
                        int year = Integer.parseInt(scanner.nextLine());
                        optionList.add(new YearOption(year));
                    } else if (optionMenu == 2) {
                        System.out.print(Message.DEPARTMENT_INPUT);
                        String department = scanner.nextLine();
                        optionList.add(new LectureDepartmentOption(department));
                    } else if (optionMenu == 3) {
                        System.out.print(Message.COURSE_NAME_INPUT);
                        String lectureName = scanner.nextLine();
                        optionList.add(new LectureNameOption(lectureName));
                    } else if (optionMenu == 4) {
                        ps.requestLectureListByOption(
                                optionList.toArray(new LectureOption[optionList.size()])
                        );
                        Protocol receiveProtocol = ps.response();
                        LectureDTO[] lectureList = (LectureDTO[]) receiveProtocol.getObjectArray();
                        printLectureList(lectureList);

                        int innerMenu = 0;
                        while (innerMenu != 2) {
                            System.out.println(Message.LECTURE_LOOKUP_INNER_MENU);
                            System.out.print(Message.INPUT);
                            innerMenu = Integer.parseInt(scanner.nextLine());
                            if (innerMenu == 1) {
                                System.out.print(Message.PLANNER_LOOKUP_INPUT);
                                int lectureNum = Integer.parseInt(scanner.nextLine());
                                LecturePlannerDTO planner = lectureList[lectureNum - 1].getPlanner();
                                System.out.println("교과목명 : " + lectureList[lectureNum - 1].getCourse().getCourseName());
                                System.out.println("강의 목표 : " + planner.getGoal());
                                System.out.println("강의 개요 : " + planner.getSummary());
                            } else if (innerMenu == 2) {
                            } else {
                                System.out.println(Message.WRONG_INPUT_NOTICE);
                            }
                        }
                    } else {
                        System.out.println(Message.WRONG_INPUT_NOTICE);
                    }
                }

            } else if (menu == 3) {

            } else {
                System.out.println(Message.WRONG_INPUT_NOTICE);
            }
        }
    }

    private void logout() throws Exception {
        ps.requestLogout();
        Protocol receiveProtocol = ps.response();
    }


}
