package manager;

public enum TasksTypes {
    TASK("TASK"),
    SUBTASK("SUBTASK"),
    EPIC("EPIC"),
    ;

    private String type;

    TasksTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
