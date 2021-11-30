package infra.database.option.student;

public class DepartmentOption implements StudentOption{
    private String query="department=";

    public DepartmentOption(){}
    public DepartmentOption(String value){
        query+=("'"+value+"'");
    }

    public String getQuery(){
        return query;
    }
}
