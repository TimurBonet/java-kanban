
package manager;

import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import http.KVTaskClient;
import task.Epic;
import task.SubTask;
import task.Task;

import javax.imageio.IIOException;
import java.util.ArrayList;


public class HttpTaskManager extends FileBackedTasksManager {
    private final String url;
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(String url) {
        this.url = url;
        client = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    public HttpTaskManager load() {
        HttpTaskManager httpTaskManager;

        httpTaskManager = new HttpTaskManager(url);

        JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
        JsonElement jsonHistory = JsonParser.parseString(client.load("history"));

        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();

            for (JsonElement task : jsonTasksArray) {
                Task thisTask = gson.fromJson(task, Task.class);

                switch (thisTask.getType()) {
                    case "TASK":
                        httpTaskManager.taskMap.put(thisTask.getId(), thisTask);
                        break;
                    case "SUBTASK":
                        httpTaskManager.subTaskMap.put(thisTask.getId(), (SubTask) thisTask);
                        break;
                    case "EPIC":
                        httpTaskManager.epicMap.put(thisTask.getId(), (Epic) thisTask);
                        break;
                }
            }
            generateHistoryFromJson(httpTaskManager, jsonHistory);
            setPrioritizedTasks();
        }
        return httpTaskManager;
    }

    private void generateHistoryFromJson(HttpTaskManager httpTaskManager, JsonElement jsonHistory) {
        String[] historyTasks = jsonHistory.getAsString().split(",");

        if (jsonHistory.isJsonNull()) {
            for (String historyTask : historyTasks) {
                Task task = httpTaskManager.getTaskById((Integer.parseInt(historyTask)));

                if (task.getType().equals("EPIC")) {
                    Epic ep = (Epic) task;
                    ep.setStatus(ep.getStatus());
                    ep.setEndTime();
                }
            }
        }
    }

    @Override
    public void save() {
        String tasksToJson = gson.toJson(new ArrayList<>(taskMap.values()));
        client.put("tasks", tasksToJson);
        String subTasksToJson = gson.toJson(new ArrayList<>(subTaskMap.values()));
        client.put("subtasks", subTasksToJson);
        String epicsToJson = gson.toJson(new ArrayList<>(epicMap.values()));
        client.put("epics", epicsToJson);

        String historyToJson = gson.toJson(inMemoryHistoryManager.getHistory().stream().map(Task::getId));
        client.put("history", historyToJson);
    }
}

