package task;

import static manager.TasksTypes.EPIC;
import static manager.TasksTypes.SUBTASK;

public class SubTask extends Task {
    //protected int epicId;

    public SubTask(String name, String description, String status, int epicId) {
        super.name = name;
        super.description = description;
        super.status = status;
        super.epicId = epicId;
        super.type = SUBTASK.getType();
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
