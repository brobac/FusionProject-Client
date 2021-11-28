package infra.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class LectureDTO {
    private long id;
    private CourseDTO course;
    private ProfessorDTO professor;
    private String lectureCode;
    private int limit;
    private int plannerID;

    private LectureTimeDTO[] lectureTimes;
    private RegisteringDTO[] myRegisterings;
    private LecturePlannerDTO planner;

    public static class Builder {
        private long id;
        private CourseDTO course;
        private ProfessorDTO professor;
        private String lectureCode;
        private int limit;
        private LectureTimeDTO[] lectureTimes;
        private RegisteringDTO[] myRegisterings;
        private LecturePlannerDTO planner;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder course(CourseDTO course) {
            this.course = course;
            return this;
        }

        public Builder professor(ProfessorDTO value) {
            professor = value;
            return this;
        }

        public Builder lectureCode(String value){
            lectureCode = value;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder lectureTimes(Set<LectureTimeDTO> value) {
            this.lectureTimes = value.toArray(new LectureTimeDTO[value.size()]);
            return this;
        }

        public Builder registerings(Set<RegisteringDTO> value) {
            myRegisterings = value.toArray(new RegisteringDTO[value.size()]);
            return this;
        }

        public Builder planner(LecturePlannerDTO planner) {
            this.planner = planner;
            return this;
        }

        public LectureDTO build(){
            return new LectureDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LectureDTO(Builder builder){
        id = builder.id;
        course = builder.course;
        professor = builder.professor;
        limit = builder.limit;
        lectureCode = builder.lectureCode;
        lectureTimes = builder.lectureTimes;
        myRegisterings = builder.myRegisterings;
        planner = builder.planner;
    }

    public long getCourseID(){
        return course.getId();
    }

    public String getProfessorCode(){
        return professor.getProfessorCode();
    }
    public int getApplicant(){
        return myRegisterings.length;
    }
}