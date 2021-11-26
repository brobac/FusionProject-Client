package domain.model;

import java.util.Objects;

public class Course {
    private long id;
    private String courseCode;
    private String courseName;
    private String department;
    private int targetYear;
    private int credit;

    public int getYear() {
        return targetYear;
    }

    public static class Builder {
        private long id=-1;
        private String courseCode;
        private String courseName;
        private String department;
        private int targetYear;
        private int credit;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder courseCode(String courseCode) {
            this.courseCode = courseCode;
            return this;
        }

        public Builder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder targetYear(int targetYear) {
            this.targetYear = targetYear;
            return this;
        }

        public Builder credit(int credit) {
            this.credit = credit;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Course(){}

    private Course(Builder builder) {
        id = builder.id;
        courseCode = builder.courseCode;
        courseName = builder.courseName;
        department = builder.department;
        targetYear = builder.targetYear;
        credit = builder.credit;
    }

    public Course(long id, String courseCode, String courseName, String department, int targetYear, int credit) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
        this.targetYear = targetYear;
        this.credit = credit;
    }

    public long getId() {
        return id;
    }
    public int getCredit() {
        return credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }

    //TODO : 테스트용
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", department='" + department + '\'' +
                ", targetYear=" + targetYear +
                ", credit=" + credit +
                '}';
    }
}
