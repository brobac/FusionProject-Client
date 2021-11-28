package option.lecture;

public class LectureCodeOption implements LectureOption {
    private String query="lecture_code=";

    public LectureCodeOption(String code) {
        this.query += "'"+code+"'";
    }

    @Override
    public String getQuery() {
        return query;
    }
}
