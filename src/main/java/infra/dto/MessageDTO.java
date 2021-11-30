package infra.dto;

public class MessageDTO {
    private String message=" ";

    public MessageDTO(){}
    public MessageDTO(String value){
        message = value;
    }

    @Override
    public String toString() {
        return message;
    }
}
