package infra.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    protected long id;
    protected String name;
    protected String department;
    protected String birthDate;

    public MemberDTO(){}

    protected MemberDTO(Long id, String name, String dep, String birthDate){
        this.id = id;
        this.name = name;
        this.department = dep;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
