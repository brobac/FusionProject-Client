package option.student;

public class StudentCodeOption implements StudentOption {
    private String query="student_code=";

    public StudentCodeOption(String studentCode){
        query+=studentCode;
    }

    public String getQuery(){
        return query;
    }
}