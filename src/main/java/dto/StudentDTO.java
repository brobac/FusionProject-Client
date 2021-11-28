package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentDTO extends MemberDTO {
    private int maxCredit = 21;
    private int credit;
    private int year;
    private String studentCode;
    private RegisteringDTO[] myRegisterings;
    private LectureTimeDTO[] timeTable;

    public static class Builder {
        private Long id = -1L;
        private int credit;
        private int maxCredit = 21;
        private int year;
        private String name;
        private String department;
        private String studentCode;
        private String birthDate;
        private RegisteringDTO[] myRegisterings;
        private LectureTimeDTO[] timeTable;

        public Builder id(long value) {
            id = value;
            return this;
        }

        public Builder name(String value) {
            name = value;
            return this;
        }

        public Builder department(String value) {
            department = value;
            return this;
        }

        public Builder birthDate(String value) {
            birthDate = value;
            return this;
        }

        public Builder studentCode(String value) {
            studentCode = value;
            return this;
        }

        public Builder year(int value) {
            year = value;
            return this;
        }

        public Builder credit(int value) {
            credit = value;
            return this;
        }

        public Builder maxCredit(int value) {
            maxCredit = value;
            return this;
        }

        public Builder myRegisterings(Set<RegisteringDTO> value) {
            myRegisterings = value.toArray(new RegisteringDTO[value.size()]);
            return this;
        }

        public Builder timeTable(Set<LectureTimeDTO> value) {
            timeTable = value.toArray(new LectureTimeDTO[value.size()]);
            return this;
        }

        public StudentDTO build() {
            return new StudentDTO(this);
        }
    }//end of builder class

    public static Builder builder() {
        return new Builder();
    }

    private StudentDTO(Builder builder) {
        super(builder.id, builder.name,
                builder.department, builder.birthDate);
        year = builder.year;
        myRegisterings = builder.myRegisterings;
        timeTable = builder.timeTable;
        credit = builder.credit;
        maxCredit = builder.maxCredit;
        studentCode = builder.studentCode;
    }


    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getBirthdate() {
        return birthDate;
    }

    public void setName(String value) {
        name = value;
    }

    public void setDepartment(String value){
        department = value;
    }

    public void setBirthdate(String value){
        birthDate = value;
    }

    //TODO : 테스트용

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", maxCredit=" + maxCredit +
                ", credit=" + credit +
                ", year=" + year +
                ", studentCode='" + studentCode + '\'' +
                ", myRegisterings=" + myRegisterings +
                ", timeTable=" + timeTable +
                '}';
    }
}

