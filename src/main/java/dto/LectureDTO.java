package dto;

import domain.generic.LectureTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class LectureDTO {
    private long id;
    private long courseID;
    private String lectureCode;
    private String lecturerID;
    private int limit;
    private int plannerID;

    private Set<LectureTimeDTO> lectureTimes;
    private Set<RegisteringDTO> myRegisterings;
    private LecturePlannerDTO planner;

    public static class Builder {
        private long id;
        private long courseID;
        private String lectureCode;
        private String lecturerID;
        private int limit;
        private Set<LectureTimeDTO> lectureTimes;
        private Set<RegisteringDTO> myRegisterings;
        private LecturePlannerDTO planner;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder courseID(long courseID) {
            this.courseID = courseID;
            return this;
        }

        public Builder lectureCode(String lectureCode) {
            this.lectureCode = lectureCode;
            return this;
        }

        public Builder lecturerID(String lecturerID) {
            this.lecturerID = lecturerID;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder lectureTimes(Set<LectureTimeDTO> lectureTimes) {
            this.lectureTimes = lectureTimes;
            return this;
        }

        public Builder registerings(Set<RegisteringDTO> registerings) {
            myRegisterings = registerings;
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
        courseID = builder.courseID;
        lectureCode = builder.lectureCode;
        lecturerID = builder.lecturerID;
        limit = builder.limit;
        lectureTimes = builder.lectureTimes;
        myRegisterings = builder.myRegisterings;
        planner = builder.planner;
    }

    public int getApplicant(){
        return myRegisterings.size();
    }
}
