package domain.generic;

import java.util.Objects;

public class LectureTime {
    public enum DayOfWeek {
        MON, TUE, WED, THU, FRI
    }
    public enum LecturePeriod {
        FIRST, SECOND, THIRD, FOURTH,
        FIFTH, SIXTH, SEVENTH, EIGHTH, NINTH
    }

    //TODO : 테스트용
    @Override
    public String toString() {
        return "LectureTime{" +
                "lectureDay=" + lectureDay +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", room='" + room + '\'' +
                '}';
    }

    private  long id;
    private DayOfWeek lectureDay;
    private LecturePeriod startTime;
    private LecturePeriod endTime;
    private String room;

    public static class Builder{
        private long id;
        private DayOfWeek lectureDay;
        private LecturePeriod startTime;
        private LecturePeriod endTime;
        private String room;

        public Builder lectureDay(String value){
            switch(value){
                case "MON":
                    lectureDay = DayOfWeek.MON;
                    break;
                case "TUE":
                    lectureDay = DayOfWeek.TUE;
                    break;
                case "WED":
                    lectureDay = DayOfWeek.WED;
                    break;
                case "THU":
                    lectureDay = DayOfWeek.THU;
                    break;
                case "FRI":
                    lectureDay = DayOfWeek.FRI;
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 요일입니다");
            }
            return this;
        }

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder room(String value){
            room = value;
            return this;
        }

        public Builder startTime(int value){
            switch (value){
                case 1:
                    startTime = LecturePeriod.FIRST;
                    break;
                case 2:
                    startTime = LecturePeriod.SECOND;
                    break;
                case 3:
                    startTime = LecturePeriod.THIRD;
                    break;
                case 4:
                    startTime = LecturePeriod.FOURTH;
                    break;
                case 5:
                    startTime = LecturePeriod.FIFTH;
                    break;
                case 6:
                    startTime = LecturePeriod.SIXTH;
                    break;
                case 7:
                    startTime = LecturePeriod.SEVENTH;
                    break;
                case 8:
                    startTime = LecturePeriod.EIGHTH;
                    break;
                case 9:
                    startTime = LecturePeriod.NINTH;
                    break;
            }
            return this;
        }

        public Builder endTime(int value){
            switch (value){
                case 1:
                    endTime = LecturePeriod.FIRST;
                    break;
                case 2:
                    endTime = LecturePeriod.SECOND;
                    break;
                case 3:
                    endTime = LecturePeriod.THIRD;
                    break;
                case 4:
                    endTime = LecturePeriod.FOURTH;
                    break;
                case 5:
                    endTime = LecturePeriod.FIFTH;
                    break;
                case 6:
                    endTime = LecturePeriod.SIXTH;
                    break;
                case 7:
                    endTime = LecturePeriod.SEVENTH;
                    break;
                case 8:
                    endTime = LecturePeriod.EIGHTH;
                    break;
                case 9:
                    endTime = LecturePeriod.NINTH;
                    break;
            }
            return this;
        }

        public LectureTime build(){
            return new LectureTime(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LectureTime(Builder builder){
        id = builder.id;
        lectureDay = builder.lectureDay;
        startTime = builder.startTime;
        endTime = builder.endTime;
        room = builder.room;
    }

    public LectureTime(long id, DayOfWeek dayOfWeek, LecturePeriod startTime, LecturePeriod endTime, String room){
        this.id = id;
        isValidTime(startTime, endTime);
        lectureDay = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    private void isValidTime(LecturePeriod startTime, LecturePeriod endTime){
        if(endTime.compareTo(startTime) < 0){
            throw new IllegalArgumentException("시간설정 잘못됨");
        }
    }

    public boolean isOverlappedTime(LectureTime time){
        return lectureDay==time.lectureDay &&
                startTime.compareTo(time.endTime)<=0 &&
                endTime.compareTo(time.startTime)>=0;
    }

    public boolean isSameRoom(LectureTime time){
        return room.equals(time.room);
    }

    public LectureTime setRoom(String room) {
        return new LectureTime(
                this.id,
                this.lectureDay,
                this.startTime,
                this.endTime,
                room
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureTime that = (LectureTime) o;
        return lectureDay == that.lectureDay && startTime == that.startTime
                && endTime == that.endTime && Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectureDay, startTime, endTime, room);
    }
}
