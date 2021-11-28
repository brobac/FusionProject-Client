package option.lecture;

public class LectureDepartmentOption implements LectureOption {
    private String query = "department=";

    public LectureDepartmentOption(String option) {
        query += ("'" + option + "'");
    }

    @Override
    public String getQuery() {
        return query;
    }
}
