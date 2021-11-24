package dto;

import domain.generic.Period;

import java.util.Map;
import java.util.Set;

public class LecturePlannerDTO {
    private Map<String, String> items;
    private static PeriodDTO writePeriod;

    public static class Builder{
        private Map<String, String> items;
        private static PeriodDTO writePeriod;

        public Builder items(Map<String, String> value){
            items = value;
            return this;
        }

        public Builder writePeriod(PeriodDTO value){
            writePeriod = value;
            return this;
        }

        public LecturePlannerDTO build(){
            return new LecturePlannerDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public Map<String, String> getItems(){
        return items;
    }

    private LecturePlannerDTO(Builder builder){
        items = builder.items;
        writePeriod = builder.writePeriod;
    }
}