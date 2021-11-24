package dto;

import domain.generic.LectureTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LectureTimeDTO {
    private long id;
    private String lectureDay;
    private int startTime;
    private int endTime;
    private String room;

    public static class Builder{
        private long id;
        private String lectureDay;
        private int startTime;
        private int endTime;
        private String room;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder room(String value){
            room = value;
            return this;
        }

        public Builder lectureDay(LectureTime.DayOfWeek value){
            switch(value){
                case MON:
                    lectureDay = "MON";
                break;
                case TUE:
                    lectureDay = "TUE";
                break;
                case WED:
                    lectureDay = "WED";
                break;
                case THU:
                    lectureDay = "THU";
                break;
                case FRI:
                    lectureDay = "FRI";
                break;
            }
            return this;
        }

        public Builder startTime(LectureTime.LecturePeriod value){
            switch (value){
                case FIRST:
                    startTime = 1;
                break;
                case SECOND:
                    startTime = 2;
                break;
                case THIRD:
                    startTime = 3;
                break;
                case FOURTH:
                    startTime = 4;
                break;
                case FIFTH:
                    startTime = 5;
                break;
                case SIXTH:
                    startTime = 6;
                break;
                case SEVENTH:
                    startTime = 7;
                break;
                case EIGHTH:
                    startTime = 8;
                break;
                case NINTH:
                    startTime = 9;
                break;
            }
            return this;
        }

        public Builder endTime(LectureTime.LecturePeriod value){
            switch (value){
                case FIRST:
                    endTime = 1;
                    break;
                case SECOND:
                    endTime = 2;
                    break;
                case THIRD:
                    endTime = 3;
                    break;
                case FOURTH:
                    endTime = 4;
                    break;
                case FIFTH:
                    endTime = 5;
                    break;
                case SIXTH:
                    endTime = 6;
                    break;
                case SEVENTH:
                    endTime = 7;
                    break;
                case EIGHTH:
                    endTime = 8;
                    break;
                case NINTH:
                    endTime = 9;
                    break;
            }
            return this;
        }


        public LectureTimeDTO build(){
            return new LectureTimeDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public LectureTimeDTO(){}
    private LectureTimeDTO(Builder builder){
        id = builder.id;
        lectureDay = builder.lectureDay;
        startTime = builder.startTime;
        endTime = builder.endTime;
        room = builder.room;
    }
}
