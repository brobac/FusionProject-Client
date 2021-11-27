package domain.model;

public class Admin extends Member {
    private String adminCode;

    public static class Builder{
        private long id=-1L;
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

        public Admin build(){
            return new Admin(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private Admin(Builder builder){
        super(builder.id, builder.name, builder.department, builder.birthDate);
        adminCode=builder.adminCode;
    }

    @Override
    public boolean isAdminPermission() {
        return true;
    }

    @Override
    public boolean isProfessorPermission() {
        return false;
    }

    @Override
    public boolean isStudentPermission() {
        return false;
    }

}