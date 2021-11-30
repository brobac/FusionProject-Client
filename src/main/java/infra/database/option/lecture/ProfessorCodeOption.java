package infra.database.option.lecture;

public class ProfessorCodeOption implements LectureOption {
    private String query = "l.professor_code=";

    public ProfessorCodeOption(){}
    public ProfessorCodeOption(String option) {
        query += "'"+option+"'";
    }

    public String getQuery() {
        return query;
    }
}
