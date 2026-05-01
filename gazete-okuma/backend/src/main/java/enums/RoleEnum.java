package enums;

public enum RoleEnum {
    ADMIN("admin"),
    USER("user");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
