package dto;


import domain.model.Course;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDTO {
    private long id;
    private int targetYear;
    private int credit;
    private String courseCode;
    private String department;
    private String courseName;

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

        public CourseDTO build() {
            return new CourseDTO(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public CourseDTO(){}

    private CourseDTO(Builder builder) {
        id = builder.id;
        courseCode = builder.courseCode;
        courseName = builder.courseName;
        department = builder.department;
        targetYear = builder.targetYear;
        credit = builder.credit;
    }

}
