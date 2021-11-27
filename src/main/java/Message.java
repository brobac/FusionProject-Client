public class Message {
    //공용
    public static final String INPUT = "입력 : ";
    public static final String WRONG_INPUT_NOTICE = "잘못된 입력입니다. 다시 입력해주세요.";
    public static final String NAME_INPUT = "이름 : ";
    public static final String PHONE_NUMBER_INPUT = "전화번호 : ";
    public static final String BIRTHDAY_INPUT = "생년월일 : ";

    //EnrollmentProgram
    public static final String ENROLLMENT_MENU = "";


    //AdminService
    public static final String ADMIN_SERVICE_MENU = "[1] 계정 생성  [2]교과목 관리  [3]개설 교과목 관리  [4]강의계획서 입력기간 설정\n[5] 수강신청 기간 설정  [6]교수 / 학생 조회  [7]개설 교과목 조회  [8] 종료";
    public static final String COURSE_CODE_INPUT = "과목코드 : ";
    public static final String COURSE_NAME_INPUT = "과목명 : ";
    public static final String DEPARTMENT_INPUT = "학과 : ";
    public static final String TARGET_GRADE_INPUT = "대상 학년 : ";
    public static final String CREDIT_INPUT = "학점 : ";


    //AdminService > createAccount
    public static final String CREATE_ACCOUNT_MENU = "[1]학생 계정 생성  [2]교수 계정 생성  [3]나가기";
    public static final String CODE_INPUT = "학번을 입력해주세요 : ";
    public static final String PROFESSOR_CODE_INPUT = "교원번호를 입력해주세요 : ";
    public static final String STUDENT_CODE_INPUT = "학번 : ";
    public static final String STUDENT_YEAR_INPUT = "학년 : ";

    public static final String PROFESSOR_CODE_INPUT = "교원번호 : ";

    //AdminService > courseManage
    public static final String COURSE_MANAGE_MENU = "[1]교과목 생성  [2]교과목 수정  [3]교과목 삭제  [4]나가기";
    public static final String CREATE_COURSE = "생성할 교과목의 정보를 입력해주세요.";
    public static final String UPDATE_COURSE_INPUT = "수정할 교과목 코드를 입력해주세요 : ";
    public static final String DELETE_COURSE_INPUT = "삭제할 교과목 코드를 입력해주세요 : ";

    //AdminService > lectureManage
    public static final String LECTURE_MANAGE_MENU = "[1]교과목 생성  [2]교과목 수정  [3]교과목 삭제  [4]나가기";
    public static final String CREATE_LECTURE = "생성할 교과목의 정보를 입력해주세요.";

    public static final String UPDATE_LECTURE_INPUT = "수정할 개설 교과목 코드를 입력해주세요 : ";
    public static final String DELETE_LECTURE_INPUT = "삭제할 개설 교과목 코드를 입력해주세요 : ";


    //AdminService > plannerInputPeriodSettings
    public static final String PLANNER_INPUT_PERIOD_MENU = "[1]강의계획서 입력 기간 설정  [2]등록된 입력 기간 조회  [3]나가기";
    public static final String BEGIN_PERIOD_INPUT = "시작 날짜 및 시간(yyyy-MM-dd HH:mm) : ";
    public static final String END_PERIOD_INPUT = "종료 날짜 및 시간 : ";

    //AdminService > registeringPeriodSettings
    public static final String REGISTERING_PERIOD_MENU = "[1]수강신청 기간 설정  [2]수강신청 기간 조회  [3]나가기";


    //AdminService > memberLookup
    public static final String MEMBER_LOOKUP_MENU = "[1]학생 조회  [2]교수 조회  [3]나가기";


    //AdminService > lectureLookup
    public static final String LECTURE_LOOKUP_MENU = "[1]전체 조회  [2]조건부 조회  [3]나가기";


    //ProfessorService
    public static final String PROFESSOR_SERVICE_MENU = "[1]개인정보 수정  [2]개설 교과목 조회  [3]강의 계획서 관리  [4]개설 교과목 강의 계획서 조회\n [5]교과목 수강 신청 학생 목록 조회  [6]교과목 시간표 조회  [7]종료";

    //ProfessorService > updatePersonalInformation
    public static final String UPDATE_PERSONAL_INFORMATION_MENU = "[1]이름 변경  [2]전화번호 변경  [3]비밀번호 변경  [4]나기기";
    public static final String CHANGE_NAME_INPUT = "새 이름 : ";
    public static final String CHANGE_PHONE_NUMBER_INPUT = "새 전화번호 : ";
    public static final String CHANGE_PASSWORD_INPUT = "새 비밀번호 : ";

    //ProfessorService > lectureLookup
    public static final String PROFESSOR_LECTURE_LOOKUP_MENU1 = "[1]조건부 조회 [2]나가기";
    public static final String PROFESSOR_LECTURE_LOOKUP_MENU2 = "[1]학년별 조회 [2]담당 교수별 조회 [3]학년별 담당 교수별 조회 [4]나가기";

    //ProfessorService > lecturePlannerSettings
    public static final String LECTURE_PLANNER_SETTINGS_MENU = "[1]강의 계획서 등록 [2]강의 계획서 수정 [3]나가기";
    public static final String LECTURE_PLANNER_INSERT_INPUT = "강의 계획서를 등록할 개설 교과목 번호를 입력해주세요 : ";
    public static final String LECTURE_PLANNER_UPDATE_INPUT = "강의 계획서를 수정할 개설 교과목 번호를 입력해주세요 : ";
    public static final String MISMATCH_LECTURE_CODE = "담당 교과목이 아닙니다.";
    public static final String LECTURE_PLANNER_INPUT = "강의 계획서 내용을 입력해주세요 : ";
    public static final String LECTURE_PLANNER_INPUT_SUCCESS ="강의 계획서 등록을 성공하였습니다.";
    public static final String LECTURE_PLANNER_INPUT_FAIL ="강의 계획서 등록을 실패하였습니다.";
    public static final String LECTURE_PLANNER_UPDATE_SUCCESS ="강의 계획서 수정을 성공하였습니다.";
    public static final String LECTURE_PLANNER_UPDATE_FAIL ="강의 계획서 수정을 실패하였습니다.";

    //ProfessorService > LecturePlannerLookup

    //ProfessorService > studentListLookup
    public static final String STUDENT_LIST_LOOKUP_MENU = "교과목 수강 신청 학생 목록을 조회할 개설 교과목 번호를 입력해주세요 : ";
    public static final String LOOKUP_STUDENT_LIST_FAIL = "교과목 수강 신청 학생 목록 조회를 실패하였습니다.";
    //ProfessorService > timeTableLookup


    //StudentService


    //StudentService > updatePersonalInformation
    public static final String LOOKUP_PERSONAL_INFORMATION_FAIL = "개인정보 조회를 실패하였습니다.";
    public static final String UPDATE_NAME_SUCCESS = "이름 수정을 성공하였습니다.";
    public static final String UPDATE_NAME_FAIL = "이름 수정을 실패하였습니다.";
    public static final String UPDATE_PHONENUM_SUCCESS = "전화번호 수정을 성공하였습니다.";
    public static final String UPDATE_PHONENUM_FAIL = "전화번호 수정을 실패하였습니다.";
    public static final String UPDATE_PASSWORD_SUCCESS = "비밀번호 수정을 성공하였습니다.";
    public static final String UPDATE_PASSWORD_FAIL = "비밀번호 수정을 실패하였습니다.";

    //StudentService > registeringManage
    public static final String REGISTERING_MENU = "[1]수강 신청 [2]수강 취소 [3]수강 신청 현황 조회 [4]나가기";
    public static final String REGISTERING_SUCCESS = "수강신청을 성공하였습니다.";
    public static final String REGISTERING_FAIL = "수강신청을 실패하였습니다.";
    public static final String REGISTERING_CANCEL_SUCCESS = "수강신청 취소를 성공하였습니다.";
    public static final String REGISTERING_CANCEL_FAIL = "수강신청 취소를 실패하였습니다.";
    public static final String LOOKUP_REGISTERING_FAIL = "수강신청 현황 조회를 실패하였습니다.";

    //StudentService > lectureLookup
    public static final String LOOKUP_LECTURE_FAIL = "전체 개설교과목 조회를 실패하였습니다.";

    //StudentService >lecturePlannerLookup
    public static final String LECTURE_PLANNER_LOOKUP_MENU = "[1]강의 계획서 조회 [2]나가기";

    public static final String LOOKUP_LECTURE_PLANNER_FAIL = "개설교과목 강의 계획서 조회를 실패하였습니다.";

    //StudentService > timeTableLookup
    public static final String LOOKUP_TIMETABLE_FAIL = "강의시간표 조회를 실패하였습니다.";

}
