package domain.model;

import domain.generic.LectureTime;
import domain.generic.Period;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Lecture {
    private long id;
    private long courseID;
    private String lectureCode;
    private String lecturerID;
    private int limit;
    private Set<LectureTime> lectureTimes;
    private Set<Registering> myRegisterings;
    private LecturePlanner planner;

    public static class Builder {
        private long id;
        private long courseID;
        private String lectureCode;
        private String lecturerID;
        private int limit;
        private Set<LectureTime> lectureTimes = new HashSet<>();
        private Set<Registering> myRegisterings = new HashSet<>();
        private LecturePlanner planner = new LecturePlanner();


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

        public Builder lectureTimes(Set<LectureTime> lectureTimes) {
            this.lectureTimes = lectureTimes;
            return this;
        }

        public Builder registerings(Set<Registering> myRegisterings) {
            this.myRegisterings = myRegisterings;
            return this;
        }

        public Builder planner(LecturePlanner planner) {
            this.planner = planner;
            return this;
        }

        public Lecture build() {
            return new Lecture(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Lecture(Builder builder) {
        id = builder.id;
        courseID = builder.courseID;
        lectureCode = builder.lectureCode;
        lecturerID = builder.lecturerID;
        limit = builder.limit;
        lectureTimes = builder.lectureTimes;
        myRegisterings = builder.myRegisterings;
        planner = builder.planner;
    }

    //TODO : set을 직접 받는 것 별로 좋지 않은듯
    public Lecture() {
    }

    public Lecture(long lectureID, String professorID, int limitPersonNum, long courseID, String lectureCode,
                   Set<LectureTime> lectureTimes, Set<Registering> registerings) { //TODO : 강의시간 더나은 방법으로
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
        this.lectureCode = lectureCode;
        myRegisterings = registerings;
    }

    //TODO : LectureManageService 때문에 만들어 놓은 임시 생성자. 나중에 지우자.
    public Lecture(long lectureID, String professorID, int limitPersonNum, long courseID,
                   Set<LectureTime> lectureTimes) { //TODO : 강의시간 더나은 방법으로
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        myRegisterings = new HashSet<>(); //TODO
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
    }

    public long getID() {
        return id;
    }

    public long getCourseID() {
        return courseID;
    }

    public Set<LectureTime> getLectureTimes() {
        return lectureTimes;
    }

    public void setLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("최소 수강인원은 0명 부터입니다.");
        }
        this.limit = limit;
    }

    public void register(Registering registering) {
        myRegisterings.add(registering);
    }

    public boolean validLimitNum() {
        if (myRegisterings.size() >= limit) {
            return false;
        }
        return true;
    }

    public void cancel(Registering registering) {
        myRegisterings.remove(registering);
    }

    public void writePlanner(String itemName, String content, String writerID) {
        if (writerID.equals(lecturerID)) {
            throw new IllegalStateException("담당 교과목이 아닙니다.");
        }

        planner.writeItem(itemName, content);
    }

    public void setPlannerWritingPeriod(Period period) {
        planner.setWritingPeriod(period);
    }

    public boolean isEqualCourse(Lecture lecture) {
        return lecture.courseID == courseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //TODO : 테스트용
    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", courseID=" + courseID +
                ", lectureCode='" + lectureCode + '\'' +
                ", lecturerID='" + lecturerID + '\'' +
                ", limit=" + limit +
                ", lectureTimes=" + lectureTimes +
                ", myRegisterings=" + myRegisterings +
                ", planner=" + planner +
                '}';
    }
}
