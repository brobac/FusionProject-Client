package domain.model;

import domain.generic.Period;

import java.time.LocalDateTime;
import java.util.*;

//TODO : 프로그램 시작시 카테고리를 줘야함
public class LecturePlanner {
    private final static List<String> categories = new ArrayList<>();
    private Map<String, String> items;
    private static Period writePeriod;
    //TODO : 테스트용
    {
        categories.add("goal");
    }
    public LecturePlanner(){
        items = new HashMap<>();

        //TODO : 기본값을 빈문자열로?
        for(String category : categories){
            items.put(category, "");
        }

        writePeriod = new Period( //TODO : 기본 강의계획서 입력기간?
                LocalDateTime.of(2020,01,01,12,00),
                LocalDateTime.of(2021,12,21,12,00)
        );
    }

    public void writeItem(String itemName, String content) {
        if(!writePeriod.isInPeriod(LocalDateTime.now())){
            throw new IllegalStateException("강의계획서 입력기간이 아닙니다.");
        }

        if(!isValidItemName(itemName)){
            throw new IllegalArgumentException("존재하지않는 항목입니다.");
        }


        items.put(itemName, content);
    }

    public static void addCategory(String newCategory){ categories.add(newCategory); }
    public static List<String> getCategory(){return categories;}
    public static void setWritingPeriod(Period period){
        writePeriod = period;
    }

    private boolean isValidItemName(String itemName){
        for(String category : categories){
            if(category.equals(itemName)){
                return true;
            }
        }
        return false;
    }

    //TODO : 테스트용
    @Override
    public String toString() {
        return "LecturePlanner{" +
                "items=" + items +
                '}';
    }
}
