package task;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, String status, int epicId) {
        super.name = name;
        super.description = description;
        super.status = status;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicId ='" + epicId + '\'' +
                ", status='" + status + '\'' +
                '}' + "\n";
    }
}
