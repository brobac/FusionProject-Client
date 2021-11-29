package infra.database.option.lecture;

public class LectureNameOption implements LectureOption {
    private String query = "course_name=";

    public LectureNameOption(){}
    public LectureNameOption(String option) {
        query += ("'" + option + "'");
    }

    @Override
    public String getQuery() {
        return query;
    }
}
