package infra.dto;

public class LecturePlannerDTO {
    private String goal;
    private String summary;

    public static class Builder {
        private String goal;
        private String summary;

        public Builder goal(String value) {
            goal = value;
            return this;
        }

        public Builder summary(String value) {
            summary = value;
            return this;
        }

        public LecturePlannerDTO build() {
            return new LecturePlannerDTO(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getGoal() {
        return goal;
    }

    public String getSummary() {
        return summary;
    }

    public LecturePlannerDTO() {
        goal = " ";
        summary = " ";
    }

    private LecturePlannerDTO(Builder builder) {
        goal = builder.goal;
        summary = builder.summary;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}