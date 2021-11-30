package infra.database.option.professor;

public class ProfessorCodeOption implements ProfessorOption {
    private String query="professor_code=";

    public ProfessorCodeOption(){}
    public ProfessorCodeOption(String code){
        query+="'"+code+"'";
    }

    public String getQuery(){
        return query;
    }
}
