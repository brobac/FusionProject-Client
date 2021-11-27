package domain.model;

import domain.generic.Period;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class RegisteringPeriod {
    private long id;
    private Period period;
    private Student.Year allowedYear;

    public static class Builder{
        private long id;
        private Period period;
        private Student.Year allowedYear;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder period(Period value){
            period = value;
            return this;
        }

        public Builder allowedYear(int year){
            switch (year){
                case 1:
                    allowedYear = Student.Year.FRESHMAN;
                    break;
                case 2:
                    allowedYear = Student.Year.SOPHOMORE;
                    break;
                case 3:
                    allowedYear = Student.Year.JUNIOR;
                    break;
                case 4:
                    allowedYear = Student.Year.SENIOR;
                    break;
                default:
                    throw new IllegalArgumentException("1~4학년까지만 존재합니다.");
            }
            return this;
        }

        public RegisteringPeriod build(){
            return new RegisteringPeriod(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private RegisteringPeriod(Builder builder){
        id = builder.id;
        period = builder.period;
        allowedYear = builder.allowedYear;
    }

    public boolean isSatisfiedBy(Student std, Course course){
        LocalDateTime nowTime = LocalDateTime.now();

        if(!isAllowedYear(std.getYear(), course.getYear())){
            return false;
        }

        if(!period.isInPeriod(nowTime)){
            return false;
        }

        return true;
    }

    private boolean isAllowedYear(Student.Year year, int courseYear){
        if(allowedYear==year){
            switch (year){
                case FRESHMAN:
                    return courseYear==1;
                case SOPHOMORE:
                    return courseYear==2;
                case JUNIOR:
                    return courseYear==3;
                case SENIOR:
                    return courseYear==4;
            }
        }

        return false;
    }

    public long getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteringPeriod that = (RegisteringPeriod) o;
        return Objects.equals(period, that.period) && allowedYear == that.allowedYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, allowedYear);
    }
}
