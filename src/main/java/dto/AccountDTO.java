package dto;

public class AccountDTO {
    private long pk;
    private String id;
    private String password;
    private long memberID;
    private String position;


    public static class Builder {
        private long pk;
        private String id;
        private String password;
        private long memberID;
        private String position;


        public Builder pk(long value) {
            pk = value;
            return this;
        }

        public Builder memberID(long value) {
            memberID = value;
            return this;
        }

        public Builder id(String value) {
            id = value;
            return this;
        }

        public Builder password(String value) {
            password = value;
            return this;
        }

        public Builder position(String value) {
            position = value;
            return this;
        }

        public AccountDTO build() {
            return new AccountDTO(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private AccountDTO(Builder builder) {
        pk = builder.pk;
        id = builder.id;
        password = builder.password;
        memberID = builder.memberID;
        position = builder.position;
    }

    public long getPk() {
        return pk;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public long getMemberID() {
        return memberID;
    }

    public String getPosition() {
        return position;
    }
}
