package domain.model;

import domain.generic.LectureTime;

import java.util.*;

public class Student extends Member{
    private Year year;
    private int maxCredit;
    private int credit;
    private String studentCode;
    private final Set<Registering> myRegisterings;
    private final Set<LectureTime> timeTable;
    public enum Year {
        FRESHMAN(1), SOPHOMORE(2), JUNIOR(3), SENIOR(4);
        private final int year;
        Year(int year){
            this.year = year;
        }
    }

    public static class Builder{
        private long id=-1;
        private int credit;
        private int maxCredit=21;
        private Year year;
        private String name;
        private String department;
        private String studentCode;
        private String birthDate;
        private Set<Registering> myRegisterings = new HashSet<>();
        private Set<LectureTime> timeTable = new HashSet<>();

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

        public Builder studentCode(String value){
            studentCode = value;
            return this;
        }

        public Builder year(int value){
            if(value==1){
                year = Year.FRESHMAN;
            }else if(value==2){
                year = Year.SOPHOMORE;
            }else if(value==3){
                year = Year.JUNIOR;
            }else if(value==4){
                year = Year.SENIOR;
            }else{
                throw new IllegalArgumentException("1~4학년만 존재합니다.");
            }
            return this;
        }

        public Builder credit(int value){
            credit = value;
            return this;
        }

        public Builder maxCredit(int value){
            maxCredit = value;
            return this;
        }

        public Builder myRegisterings(Set<Registering> value){
            myRegisterings = value;
            return this;
        }

        public Builder timeTable(Set<LectureTime> value){
            timeTable = new HashSet(value);
            return this;
        }

        public Student build(){
            return new Student(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

    private Student(Builder builder){
        super(builder.id, builder.name, builder.department, builder.birthDate);
        year = builder.year;
        myRegisterings = builder.myRegisterings;
        timeTable = builder.timeTable;
        credit = builder.credit;
        maxCredit = builder.maxCredit;
        studentCode = builder.studentCode;
    }

    public Set<Registering> getMyRegisterings(){
        return new HashSet<>(myRegisterings);
    }
    public Year getYear(){return year;}

    public void register(Registering registering,
                            Set<LectureTime> lectureTimes, int lectureCredit){
        credit += lectureCredit;
        myRegisterings.add(registering);
        timeTable.addAll(lectureTimes);
    }

    public void cancel(Registering registering,
                       Set<LectureTime> lectureTimes, int lectureCredit){
        credit -= lectureCredit;
        myRegisterings.remove(registering);
        timeTable.removeAll(lectureTimes);
    }

    public boolean isDuplicatedTime(Set<LectureTime> lectureTimes){
        for(LectureTime myTime : timeTable){
            for(LectureTime lectureTime : lectureTimes){
                if(myTime.isOverlappedTime(lectureTime)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidCredit(int lectureCredit) {
        if(credit+lectureCredit>=maxCredit){
            return false;
        }

        return true;
    }

    public boolean hasLecture(long lectureID) {
        for(Registering registering : myRegisterings){
            if(registering.getLectureID()==lectureID){
                return true;
            }
        }
        return false;
    }

    public String getStudentCode() {
        return studentCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setName(String value){
        name = value;
    }

    @Override
    public boolean isAdminPermission() {
        return false;
    }

    @Override
    public boolean isProfessorPermission() {
        return false;
    }

    @Override
    public boolean isStudentPermission() {
        return true;
    }


    //TODO : 테스트용
    @Override
    public String toString() {
        return "Student{" +
                "year=" + year +
                ", maxCredit=" + maxCredit +
                ", credit=" + credit +
                ", studentCode='" + studentCode + '\'' +
                ", myRegisterings=" + myRegisterings +
                ", timeTable=" + timeTable +
                '}';
    }
}
