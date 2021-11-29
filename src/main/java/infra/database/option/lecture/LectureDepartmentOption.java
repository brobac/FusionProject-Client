package infra.database.option.lecture;

public class LectureDepartmentOption implements LectureOption {
    private String query = "department=";

    public LectureDepartmentOption(){}

    public LectureDepartmentOption(String option) {
        query += ("'" + option + "'");
    }

    @Override
    public String getQuery() {
        return query;
    }
}
