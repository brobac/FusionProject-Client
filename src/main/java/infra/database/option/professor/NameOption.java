package infra.database.option.professor;

public class NameOption implements ProfessorOption {
    private String query="name=";

    public NameOption(){}
    public NameOption(String value){
        query+=("'"+value+"'");
    }

    public String getQuery() {
        return query;
    }
}
