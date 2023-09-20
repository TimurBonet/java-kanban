import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, SubTask> subTaskForEpic = new HashMap<>();

    protected ArrayList<String> subTaskStatuses = new ArrayList<>();
    protected String status;

    public Epic(String name, String description) {
        super.name = name;
        super.description = description;
    }

    public HashMap<Integer, SubTask> getSubTaskForEpic() {
        return subTaskForEpic;
    }

    @Override
    public String toString() {
        String subTask;
        if (subTaskForEpic.isEmpty()) {
            subTask = "отсутствуют";
        } else {
            subTask = subTaskForEpic.toString();
        }
        return "Epic{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ",  subtasks=' " + subTaskForEpic + '\'' +
                '}';
    }


}
