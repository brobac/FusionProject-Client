package infra.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.InvalidParameterException;

@Getter
@Setter
@ToString
public class LectureTimeDTO {
    private long id;
    private String lectureDay;
    private int startTime;
    private int endTime;
    private String room;
    private String lectureName;

    public static class Builder{
        private long id;
        private String lectureDay;
        private int startTime;
        private int endTime;
        private String room;
        private String lectureName;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder lectureName(String value){
            lectureName = value;
            return this;
        }

        public Builder room(String value){
            room = value;
            return this;
        }

        public Builder lectureDay(String value){
            switch(value){
                case "MON":
                    lectureDay = "MON";
                break;
                case "TUE":
                    lectureDay = "TUE";
                break;
                case "WED":
                    lectureDay = "WED";
                break;
                case "THU":
                    lectureDay = "THU";
                break;
                case "FRI":
                    lectureDay = "FRI";
                break;
                default:
                    throw new InvalidParameterException("잘못된 요일 입력입니다.");
            }
            return this;
        }

        public Builder startTime(int value){
            switch (value){
                case 1:
                    startTime = 1;
                break;
                case 2:
                    startTime = 2;
                break;
                case 3:
                    startTime = 3;
                break;
                case 4:
                    startTime = 4;
                break;
                case 5:
                    startTime = 5;
                break;
                case 6:
                    startTime = 6;
                break;
                case 7:
                    startTime = 7;
                break;
                case 8:
                    startTime = 8;
                break;
                case 9:
                    startTime = 9;
                break;
                default:
                    throw new InvalidParameterException("잘못된 시작 교시 입력입니다.");
            }
            return this;
        }

        public Builder endTime(int value){
            switch (value){
                case 1:
                    startTime = 1;
                    break;
                case 2:
                    startTime = 2;
                    break;
                case 3:
                    startTime = 3;
                    break;
                case 4:
                    startTime = 4;
                    break;
                case 5:
                    startTime = 5;
                    break;
                case 6:
                    startTime = 6;
                    break;
                case 7:
                    startTime = 7;
                    break;
                case 8:
                    startTime = 8;
                    break;
                case 9:
                    startTime = 9;
                    break;
                default:
                    throw new InvalidParameterException("잘못된 시작 교시 입력입니다.");
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
        lectureName = builder.lectureName;
    }
}
