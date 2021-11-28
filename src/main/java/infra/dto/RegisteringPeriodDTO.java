package infra.dto;

public class RegisteringPeriodDTO {
    private long id;
    private PeriodDTO period;
    private int allowedYear;

    public static class Builder{
        private long id;
        private PeriodDTO period;
        private int allowedYear;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder period(PeriodDTO value){
            period = value;
            return this;
        }

        public Builder allowedYear(int value){
            allowedYear = value;
            return this;
        }

        public RegisteringPeriodDTO build(){
            return new RegisteringPeriodDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private RegisteringPeriodDTO(Builder builder){
        period = builder.period;
        allowedYear = builder.allowedYear;
        id = builder.id;
    }

    public long getID() {
        return id;
    }

    public PeriodDTO getPeriodDTO() {
        return period;
    }

    public int getAllowedYear() {
        return allowedYear;
    }
}
