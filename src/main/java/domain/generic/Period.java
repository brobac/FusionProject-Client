package domain.generic;

import java.time.LocalDateTime;
import java.util.Objects;

//TODO : 강의 시간과 합칠수있나?
public class Period {
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    public Period(LocalDateTime beginTime, LocalDateTime endTime){
        setBeginTime(beginTime);
        setEndTime(endTime);
    }

    public boolean isInPeriod(LocalDateTime time) {
        return beginTime.isBefore(time) && endTime.isAfter(time);
    }

    private void setBeginTime(LocalDateTime time){
        if(!isValidBeginTime(time)){
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        beginTime = time;
    }

    private void setEndTime(LocalDateTime time){
        if(!isValidEndTime(time)){
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        endTime = time;
    }

    private boolean isValidBeginTime(LocalDateTime time){
        return endTime==null || !time.isAfter(endTime);
    }

    private boolean isValidEndTime(LocalDateTime time){
        return beginTime==null || !time.isBefore(beginTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(beginTime, period.beginTime) && Objects.equals(endTime, period.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginTime, endTime);
    }
}
