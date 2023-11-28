package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {


    void createTask(Task newTask);

    String setStatus(String status);

    void updateTask(Task newTask);

    void createSubTask(SubTask newSubTask);

    void updateSubTask(SubTask newSubTask);

    void createEpic(Epic newEpic);

    void updateEpic(Epic newEpic);

    String getStatus(List<SubTask> subTaskForEpic);

    // Блок получения задачи по ID

    Epic getEpicById(int uniqueId);

    Task getTaskById(int uniqueId);

    SubTask getSubTaskById(int uniqueId);

    List<Task> getTaskList();

    List<SubTask> getSubTaskList();

    List<Epic> getEpicList();

    List<SubTask> getEpicSubtasks(int uniqueId);

    void clearAllTasks(HashMap<Integer, Task> task);

    void clearAllSubTasks(HashMap<Integer, SubTask> subTask);

    void clearAllEpic(HashMap<Integer, Epic> epic);


    void clearTaskById(int uniqueId);

    void clearSubTaskById(int uniqueId);

    void clearEpicById(int uniqueId);

    List<Task> getHistory();

}
