package infra.database.option.lecture;

public class YearOption implements LectureOption{
    private String query="target_year=";

    public YearOption(int year) {
        this.query += year;
    }

    @Override
    public String getQuery() {
        return query;
    }
}
