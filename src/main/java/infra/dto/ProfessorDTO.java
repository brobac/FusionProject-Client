package infra.dto;

import java.util.*;

public class ProfessorDTO extends MemberDTO {
    private String professorCode;
    private String telePhone;
    private LectureTimeDTO[] timeTable;

    public static class Builder{
        private long id=-1L;
        private String name;
        private String department;
        private String birthDate;
        private String professorCode;
        private String telePhone;
        private LectureTimeDTO[] timeTable;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder name(String value){
            name = value;
            return this;
        }

        public Builder department(String value){
            department = value;
            return this;
        }

        public Builder birthDate(String value){
            birthDate = value;
            return this;
        }

        public Builder professorCode(String value){
            professorCode = value;
            return this;
        }

        public Builder telePhone(String value){
            telePhone = value;
            return this;
        }

        public Builder timeTable(Set<LectureTimeDTO> value){
            timeTable = value.toArray(new LectureTimeDTO[value.size()]);
            return this;
        }

        public ProfessorDTO build(){
            return new ProfessorDTO(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

    public ProfessorDTO(){}
    private ProfessorDTO(Builder builder){
        super(builder.id, builder.name,
                builder.department, builder.birthDate);
        timeTable = builder.timeTable;
        professorCode = builder.professorCode;
        telePhone = builder.telePhone;
    }

    public String getProfessorCode() {
        return professorCode;
    }

    public String getTelePhone() {
        return telePhone;
    }

    //TODO : 테스트용


    @Override
    public String toString() {
        return "ProfessorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", professorCode='" + professorCode + '\'' +
                ", telePhone='" + telePhone + '\'' +
                ", timeTable=" + Arrays.toString(timeTable) +
                '}';
    }
}

