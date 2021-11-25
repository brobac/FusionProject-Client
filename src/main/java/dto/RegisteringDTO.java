package dto;

public class RegisteringDTO {
    private final long id;
    private final long lectureID;
    private final String studentCode;
    private final String registeringTime;

    public static class Builder{
        private long id;
        private long lectureID;
        private String studentCode;
        private String registeringTime;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder lectureID(long value){
            lectureID = value;
            return this;
        }

        public Builder studentCode(String value){
            studentCode = value;
            return this;
        }

        public Builder registeringTime(String value){
            registeringTime = value;
            return this;
        }

        public RegisteringDTO build(){
            return new RegisteringDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public RegisteringDTO(Builder builder){
        id = builder.id;
        lectureID = builder.lectureID;
        studentCode = builder.studentCode;
        registeringTime = builder.registeringTime;
    }

    public long getId() {
        return id;
    }

    public long getLectureID() {
        return lectureID;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getRegisteringTime() {
        return registeringTime;
    }
}