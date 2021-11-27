package domain.model;

import java.time.LocalDate;

public abstract class Member implements Permission{
    protected long id;
    protected String name;
    protected String department;
    protected String birthDate;

    protected Member(long id, String name, String dep, String birthDate){
        this.id = id;
        this.name = name;
        this.department = dep;
        this.birthDate = birthDate;
    }

    public long getID(){
        return id;
    }
}
