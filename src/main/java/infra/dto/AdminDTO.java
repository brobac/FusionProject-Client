package infra.dto;


public class AdminDTO extends MemberDTO {
    private String adminCode;

    public static class Builder{
        private long id=-1;
        private String name;
        private String department;
        private String birthDate;
        private String adminCode;

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

        public Builder adminCode(String value){
            adminCode = value;
            return this;
        }

        public AdminDTO build(){
            return new AdminDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private AdminDTO(Builder builder) {
        super(builder.id, builder.name, builder.department, builder.birthDate);
        adminCode = builder.adminCode;
    }

    public String getAdminCode() {
        return adminCode;
    }

    //TODO : 테스트용

    @Override
    public String toString() {
        return "AdminDTO{" +
                "adminCode='" + adminCode + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }
}
