package option.lecture;

public class LectureTargetYearOption implements LectureOption{
    private String query="target_year=";

    public LectureTargetYearOption(int year) {
        this.query += year;
    }

    @Override
    public String getQuery() {
        return query;
    }
}
