package infra.database.option.professor;

public class DepartmentOption implements ProfessorOption {
    private String query="department=";

    public DepartmentOption(){}
    public DepartmentOption(String value){
        query+=("'"+value+"'");
    }

    public String getQuery() {
        return query;
    }
}
