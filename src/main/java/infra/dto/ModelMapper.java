//package infra.dto;
//
//import domain.generic.LectureTime;
//import domain.generic.Period;
//import domain.model.*;
//
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class ModelMapper {
//
//    public static RegisteringPeriodDTO regPeriodToDTO(RegisteringPeriod regPeriod){
//        try{
//            long id = getLongField(regPeriod, "id");
//            PeriodDTO periodDTO = periodToDTO(getPeriod(regPeriod, "period"));
//            int year = getYearField(regPeriod, "allowedYear");
//            return RegisteringPeriodDTO.builder()
//                    .id(id)
//                    .period(periodDTO)
//                    .allowedYear(year)
//                    .build();
//
//        }catch(NoSuchFieldException | IllegalAccessException e){
//            e.getStackTrace();
//        }
//        return null;
//    }
//
//    public static AccountDTO accountToDTO(Account account){
//        try{
//            long pk = getLongField(account, "pk");
//            String id = getStringField(account, "id");
//            String password = getStringField(account, "password");
//            long memberID = getLongField(account, "memberID");
//
//            return AccountDTO.builder()
//                        .pk(pk)
//                        .id(id)
//                        .memberID(memberID)
//                        .password(password)
//                        .build();
//
//        }catch(NoSuchFieldException | IllegalAccessException e){
//            e.getStackTrace();
//        }
//        return null;
//    }
//
//    public static RegisteringDTO registeringToDTO(Registering reg){
//        try{
//            long id = getLongField(reg, "id");
//            long lectureID = getLongField(reg, "lectureID");
//            String studentCode = getStringField(reg, "studentCode");
//            String registeringTime = getStringField(reg, "registeringTime");
//
//            return RegisteringDTO.builder()
//                    .id(id)
//                    .lectureID(lectureID)
//                    .studentCode(studentCode)
//                    .registeringTime(registeringTime)
//                    .build();
//
//        }catch(NoSuchFieldException | IllegalAccessException e){
//            e.getStackTrace();
//        }
//        return null;
//    }
//
//    public static LecturePlanner getPlanner(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (LecturePlanner) f1.get(obj);
//    }
//
//    public static LecturePlannerDTO plannerToDTO(LecturePlanner planner){
//        try{
//            Map<String, String> items = getPlannerItems(planner, "items");
//            PeriodDTO writePeriod = periodToDTO(getPeriod(planner,"writePeriod"));
//            return LecturePlannerDTO.builder()
//                    .items(items)
//                    .writePeriod(writePeriod)
//                    .build();
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.getStackTrace();
//        }
//        return null;
//    }
//
//    private static Map<String, String> getPlannerItems(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (Map<String, String>) f1.get(obj);
//    }
//
//    public static Period getPeriod(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (Period) f1.get(obj);
//    }
//
//    public static PeriodDTO periodToDTO(Period period) throws NoSuchFieldException, IllegalAccessException {
//        LocalDateTime beginTime = getLocalDateTimeField(period, "beginTime");
//        LocalDateTime endTime = getLocalDateTimeField(period, "endTime");
//        return PeriodDTO.builder()
//                .beginTime(beginTime)
//                .endTime(endTime)
//                .build();
//    }
//
//    public static AdminDTO adminToDTO(Admin admin) {
//        try {
//            long id = getSuperLongField(admin, "id");
//            String name = getSuperStringField(admin, "name");
//            String department = getSuperStringField(admin, "department");
//            String birthDate = getSuperStringField(admin, "birthDate");
//            String adminCode = getStringField(admin, "adminCode");
//
//            return AdminDTO.builder()
//                    .id(id)
//                    .name(name)
//                    .department(department)
//                    .birthDate(birthDate)
//                    .adminCode(adminCode)
//                    .build();
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.getStackTrace();
//        }
//
//        return null;
//    }
//
//    public static ProfessorDTO professorToDTO(Professor prof) {
//        try {
//            long id = getSuperLongField(prof, "id");
//            String name = getSuperStringField(prof, "name");
//            String department = getSuperStringField(prof, "department");
//            String birthDate = getSuperStringField(prof, "birthDate");
//            String professorCode = getStringField(prof, "professorCode");
//            String telePhone = getStringField(prof, "telePhone");
//            Set<LectureTimeDTO> timeTable = getLectureTimeDTOTable(getTimeTable(prof, "timeTable"));
//
//            return ProfessorDTO.builder()
//                    .id(id)
//                    .name(name)
//                    .department(department)
//                    .birthDate(birthDate)
//                    .professorCode(professorCode)
//                    .telePhone(telePhone)
//                    .timeTable(timeTable)
//                    .build();
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.getStackTrace();
//        }
//
//        return null;
//    }
//
//    public static StudentDTO studentToDTO(Student std) {
//        try {
//            long id = getSuperLongField(std, "id");
//            String name = getSuperStringField(std, "name");
//            String department = getSuperStringField(std, "department");
//            String birthDate = getSuperStringField(std, "birthDate");
//            String studentCode = getStringField(std, "studentCode");
//            int credit = getIntField(std, "credit");
//            int maxCredit = getIntField(std, "maxCredit");
//            int year = getYearField(std, "year");
//            Set<LectureTimeDTO> timeTable = getLectureTimeDTOTable(getTimeTable(std, "timeTable"));
//            Set<RegisteringDTO> myRegisterings = getRegDTOSet(getRegisteringSet(std, "myRegisterings"));
//
//            return StudentDTO.builder()
//                    .id(id)
//                    .name(name)
//                    .department(department)
//                    .birthDate(birthDate)
//                    .credit(credit)
//                    .maxCredit(maxCredit)
//                    .studentCode(studentCode)
//                    .year(year)
//                    .myRegisterings(myRegisterings)
//                    .timeTable(timeTable)
//                    .build();
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.getStackTrace();
//        }
//
//        return null;
//    }
//
////    public static LectureDTO lectureToDTO(Lecture lecture) {
////        try {
////            long id = getLongField(lecture, "id");
////            long courseID = getLongField(lecture, "courseID");
////            String lectureCode = getStringField(lecture,"lectureCode");
////            String lecturerID = getStringField(lecture, "lecturerID");
////            int limit = getIntField(lecture, "limit");
////            Set<LectureTimeDTO> lectureTimes = getLectureTimeDTOTable(getTimeTable(lecture,"lectureTimes"));
////            Set<RegisteringDTO> myRegisterings = getRegDTOSet(getRegisteringSet(lecture,"myRegisterings"));
////            LecturePlannerDTO planner = plannerToDTO(getPlanner(lecture, "planner"));
////
////            return LectureDTO.builder()
////                    .id(id)
////                    .courseID(courseID)
////                    .lectureCode(lectureCode)
////                    .lecturerID(lecturerID)
////                    .limit(limit)
////                    .lectureTimes(lectureTimes)
////                    .registerings(myRegisterings)
////                    .planner(planner)
////                    .build();
////
////        } catch (NoSuchFieldException | IllegalAccessException e) {
////            e.getStackTrace();
////        }
////
////        return null;
////    }
//
//    public static LectureDTO lectureToDTO(Lecture lecture) {
//        try {
//            long id = getLongField(lecture, "id");
//            long courseID = getLongField(lecture, "courseID");
//            String lectureCode = getStringField(lecture,"lectureCode");
//            String lecturerID = getStringField(lecture, "lecturerID");
//            int limit = getIntField(lecture, "limit");
//            Set<LectureTimeDTO> lectureTimes = getLectureTimeDTOTable(getTimeTable(lecture,"lectureTimes"));
//            Set<RegisteringDTO> myRegisterings = getRegDTOSet(getRegisteringSet(lecture,"myRegisterings"));
//            LecturePlannerDTO planner = plannerToDTO(getPlanner(lecture, "planner"));
//
//            return LectureDTO.builder()
//                    .id(id)
//                    .courseID(courseID)
//                    .lectureCode(lectureCode)
//                    .lecturerID(lecturerID)
//                    .limit(limit)
//                    .lectureTimes(lectureTimes)
//                    .registerings(myRegisterings)
//                    .planner(planner)
//                    .build();
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.getStackTrace();
//        }
//
//        return null;
//    }
//
//
//    private static Set<RegisteringDTO> getRegDTOSet(Set<Registering> myRegisterings) throws NoSuchFieldException, IllegalAccessException {
//        Set<RegisteringDTO> regSet = new HashSet<>();
//
//        for (Registering reg : myRegisterings) {
//            long id = getLongField(reg, "id");
//            long lectureID = getLongField(reg, "lectureID");
//            String studentCode = getStringField(reg, "studentCode");
//            String registeringTime = getStringField(reg, "registeringTime");
//
//            regSet.add(
//                    RegisteringDTO.builder()
//                            .id(id)
//                            .lectureID(lectureID)
//                            .studentCode(studentCode)
//                            .registeringTime(registeringTime)
//                            .build()
//            );
//
//        }
//
//        return regSet;
//    }
//
//    private static Set<Registering> getRegisteringSet(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (Set<Registering>) f1.get(obj);
//    }
//
//    private static Set<LectureTimeDTO> getLectureTimeDTOTable(Set<LectureTime> timeTable) throws NoSuchFieldException, IllegalAccessException {
//        Set<LectureTimeDTO> dtoList = new HashSet<>();
//
//        for (LectureTime time : timeTable) {
//            long id = getLongField(time,"id");
//            String room = getStringField(time, "room");
//            LectureTime.DayOfWeek lectureDay = getLectureDay(time, "lectureDay");
//            LectureTime.LecturePeriod startTime = getLecturePeriod(time, "startTime");
//            LectureTime.LecturePeriod endTime = getLecturePeriod(time, "endTime");
//
//            dtoList.add(
//                    LectureTimeDTO.builder()
//                            .id(id)
//                            .room(room)
//                            .lectureDay(lectureDay)
//                            .startTime(startTime)
//                            .endTime(endTime)
//                            .build()
//            );
//        }
//
//        return dtoList;
//    }
//
//
//    private static LectureTime.DayOfWeek getLectureDay(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (LectureTime.DayOfWeek) f1.get(obj);
//    }
//
//    private static LectureTime.LecturePeriod getLecturePeriod(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (LectureTime.LecturePeriod) f1.get(obj);
//    }
//
//    public static CourseDTO courseToDTO(Course course){
//        try{
//            long id = getLongField(course, "id");
//            int credit = getIntField(course, "credit");
//            int targetYear = getIntField(course, "targetYear");
//            String courseCode = getStringField(course, "courseCode");
//            String department = getStringField(course, "department");
//            String courseName = getStringField(course, "courseName");
//
//            return CourseDTO.builder()
//                    .id(id)
//                    .courseCode(courseCode)
//                    .targetYear(targetYear)
//                    .credit(credit)
//                    .department(department)
//                    .courseName(courseName)
//                    .build();
//        }catch(NoSuchFieldException | IllegalAccessException e){
//            e.getStackTrace();
//        }
//
//        return null;
//    }
//
//    private static String getSuperStringField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getSuperclass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (String) f1.get(obj);
//    }
//
//    private static long getSuperLongField(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
//        Field f1 = obj.getClass().getSuperclass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (long) f1.get(obj);
//    }
//
//    private static int getYearField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        if (f1.get(obj) == Student.Year.FRESHMAN) {
//            return 1;
//        } else if (f1.get(obj) == Student.Year.SOPHOMORE) {
//            return 2;
//        } else if (f1.get(obj) == Student.Year.JUNIOR) {
//            return 3;
//        } else {
//            return 4;
//        }
//    }
//
//    private static List<Long> getLongList(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (List<Long>) f1.get(obj);
//    }
//
//    private static Set<LectureTime> getTimeTable(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (Set<LectureTime>) f1.get(obj);
//    }
//
//    private static LocalDateTime getLocalDateTimeField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (LocalDateTime) f1.get(obj);
//    }
//
//    private static String getStringField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (String) f1.get(obj);
//    }
//
//    public static int getIntField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (int) f1.get(obj);
//    }
//
//    public static long getLongField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field f1 = obj.getClass().getDeclaredField(fieldName);
//        f1.setAccessible(true);
//        return (long) f1.get(obj);
//    }
//}
