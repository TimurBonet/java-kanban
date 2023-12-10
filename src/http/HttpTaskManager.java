
package http;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.*;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTasksManager {
    private final String url;
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(String url){
        this.url = url;
        client = new KVTaskClient(url);
        gson = Managers.getGson();

    }

    public HttpTaskManager load (String url) {
        HttpTaskManager httpTaskManager;

            httpTaskManager = new HttpTaskManager(url);

            JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
            JsonElement jsonHistory = JsonParser.parseString(client.load("history"));

            if(!jsonTasks.isJsonNull()){
                JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
                String[] historyTasks = jsonHistory.getAsString().split(",");

                for(JsonElement task : jsonTasksArray){
                    Task thisTask = gson.fromJson(task,Task.class);

                    if (thisTask.getType().equals("TASK")){
                        httpTaskManager.taskMap.put(thisTask.getId(), thisTask);
                    } else if (thisTask.getType().equals("SUBTASK")) {
                        httpTaskManager.subTaskMap.put(thisTask.getId(),(SubTask) thisTask);
                    } else if (thisTask.getType().equals("EPIC")) {
                        httpTaskManager.epicMap.put(thisTask.getId(), (Epic) thisTask);
                    }
                }
                if (jsonHistory.isJsonNull()){
                    for(int i = 0; i< historyTasks.length; i++){
                        Task task = httpTaskManager.getTaskById((Integer.parseInt(historyTasks[i])));

                        if(task.getType().equals("EPIC")){
                            Epic ep = (Epic) task;
                            ep.setStatus(ep.getStatus());
                            ep.setEndTime();
                        }
                    }
                }
            } else {
                return  httpTaskManager;
            }
            return httpTaskManager;
    }

    @Override
    public void save() {
        String tasksToJson = gson.toJson(new ArrayList<>(taskMap.values()));
        client.put("tasks",tasksToJson);
        String subTasksToJson = gson.toJson(new ArrayList<>(subTaskMap.values()));
        client.put("subtasks",subTasksToJson);
        String epicsToJson = gson.toJson(new ArrayList<>(epicMap.values()));
        client.put("epics",epicsToJson);

        String historyToJson = gson.toJson(inMemoryHistoryManager.getHistory().stream().map(Task::getId));
        client.put("history", historyToJson);
    }

    @Override
    public void createTask(Task newTask) {
        super.createTask(newTask);
        save();

        try {
            checkIntersection();
        } catch (IntersectionIntervalException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSubTask(SubTask newSubTask) {
        super.createSubTask(newSubTask);
        save();

        try {
            checkIntersection();
        } catch (IntersectionIntervalException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        save();
    }

    @Override
    public void clearTaskById(int uniqueId) {
        super.clearTaskById(uniqueId);
        save();
    }

    @Override
    public void clearSubTaskById(int uniqueId) {
        super.clearSubTaskById(uniqueId);
        save();
    }

    @Override
    public void clearEpicById(int uniqueId) {
        super.clearEpicById(uniqueId);
        save();
    }

    @Override
    public HistoryManager getInMemoryHistoryManager() {
        save();
        return super.getInMemoryHistoryManager();
    }
}

