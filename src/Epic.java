import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    public HashMap<Integer, SubTask> subTaskForEpic = new HashMap<>();
    ArrayList<String> subTaskStatus = new ArrayList<>();
    String status;

    public Epic(String name, String description) {
        super.name = name;
        super.description = description;
        this.status = getStatus(subTaskStatus);
    }

    public String getStatus(ArrayList<String> subTaskStatus) {
        if (subTaskStatus.isEmpty()) {
            return "NEW";
        }
        if ((subTaskStatus.contains("NEW"))
                && (!subTaskStatus.contains("IN_PROGRESS"))
                && (!subTaskStatus.contains("DONE"))) {
            return "NEW";
        } else if ((subTaskStatus.contains("DONE"))
                && (!subTaskStatus.contains("NEW"))
                && (!subTaskStatus.contains("IN_PROGRESS"))) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }

    @Override
    public String toString() {
        String subTask;
        if (subTaskForEpic.isEmpty()) {
            subTask = "отсутствуют";
        } else {
            subTask = subTaskForEpic.toString();
        }
        return "Epic{" + "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ",  subtasks=' " + subTaskForEpic + '\'' +
                '}';
    }


}
