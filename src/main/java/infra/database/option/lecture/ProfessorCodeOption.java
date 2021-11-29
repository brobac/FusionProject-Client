package infra.database.option.lecture;

public class ProfessorCodeOption implements LectureOption {
    private String query = "professor_code=";

    public ProfessorCodeOption(String option) {
        query += "'"+option+"'";
    }

    public String getQuery() {
        return query;
    }
}
