package domain.model;

import java.util.Objects;

public class Registering {
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

        public Registering build(){
            return new Registering(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public Registering(Builder builder){
        id = builder.id;
        lectureID = builder.lectureID;
        studentCode = builder.studentCode;
        registeringTime = builder.registeringTime;
    }

    public long getID() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registering that = (Registering) o;
        return id == that.id && lectureID == that.lectureID && studentCode == that.studentCode && Objects.equals(registeringTime, that.registeringTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lectureID, studentCode, registeringTime);
    }

    //TODO : 테스트용
    @Override
    public String toString() {
        return "Registering{" +
                "id=" + id +
                ", lectureID=" + lectureID +
                ", studentCode='" + studentCode + '\'' +
                ", registeringTime='" + registeringTime + '\'' +
                '}';
    }
}
