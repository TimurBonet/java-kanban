public class Task {
    protected String name;
    protected String description;
    protected String status;
    protected int id;
    protected int statusId;

    public Task() {
    }

    public Task(String name, String description, int statusId) {
        this.name = name;
        this.description = description;
        this.statusId = statusId;
    }

    @Override
    public String toString() {
        return "Task{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
