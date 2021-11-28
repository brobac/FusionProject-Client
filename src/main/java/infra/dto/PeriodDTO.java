package infra.dto;

import java.time.LocalDateTime;

public class PeriodDTO {
    private String beginTime;
    private String endTime;

    public static class Builder{
        private String beginTime;
        private String endTime;

        public Builder beginTime(LocalDateTime value){
            beginTime = value.toString();
            return this;
        }

        public Builder endTime(LocalDateTime value){
            endTime = value.toString();
            return this;
        }

        public PeriodDTO build(){
            return new PeriodDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private PeriodDTO(Builder builder){
        beginTime = builder.beginTime;
        endTime = builder.endTime;
    }

    public LocalDateTime getBeginTime() {
        return LocalDateTime.parse(beginTime);
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.parse(endTime);
    }

}
