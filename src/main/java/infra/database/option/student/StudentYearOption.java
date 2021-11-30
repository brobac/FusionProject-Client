package infra.database.option.student;

public class StudentYearOption implements StudentOption {
    private String query="year=";

    public StudentYearOption(){}
    public StudentYearOption(String year) {
        query+=year;
    }

    @Override
    public String getQuery() {
        return query;
    }
}
