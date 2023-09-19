public class SubTask extends Task {
    int idOfEpic;
    String status;

    public SubTask(String name, String description, int statusId, int idOfEpic) {
        super.name = name;
        super.description = description;
        this.status = setStatus(statusId);
        this.idOfEpic = idOfEpic;
    }

    @Override
    public String setStatus(int statusId) {
        return super.setStatus(statusId);
    }

    @Override
    public String toString() {
        return "SubTask{" + "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idOfEpic='" + idOfEpic + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
