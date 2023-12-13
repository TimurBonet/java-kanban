package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected int uniqueId = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public HashMap<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }


    void assigningId() {
        uniqueId++;
    }

    @Override
    public void createTask(Task newTask) {
        newTask.setId(uniqueId);
        taskMap.put(uniqueId, newTask);
        assigningId();
    }

    @Override
    public String setStatus(String status) {
        String result = "";
        switch (status) {
            case "NEW":
                result = TaskStatus.NEW.getTitle();
                break;
            case "IN_PROGRESS":
                result = TaskStatus.IN_PROGRESS.getTitle();
                break;
            case "DONE":
                result = TaskStatus.DONE.getTitle();
                break;
            default:
                System.out.println("Ошибка. Выберите одно из трёх состояний: " +
                        "1 - \"NEW\", 2 - \"IN_PROGRESS\", 3- \"DONE\"");
        }
        return result;
    }

    @Override
    public void updateTask(Task newTask) {
        int uniqueId = newTask.getId();
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        taskMap.put(uniqueId, newTask);
    }

    @Override
    public void createSubTask(SubTask newSubTask) {
        int epicId = newSubTask.getEpicId();

        if (!epicMap.containsKey(epicId)) {
            System.out.println(epicId + " - Эпик-задача с таким Id отсутствует");
        }
        newSubTask.setId(uniqueId);
        subTaskMap.put(uniqueId, newSubTask);
        if (epicMap.get(epicId) != null) {
            epicMap.get(epicId).getSubTaskForEpic().add(newSubTask);
            epicMap.get(epicId).getStartTime();
            epicMap.get(epicId).getDuration();
            epicMap.get(epicId).setStatus(getStatus(epicMap.get(epicId).getSubTaskForEpic()));
        }
        assigningId();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        int uniqueId = newSubTask.getId();
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        subTaskMap.put(uniqueId, newSubTask);
        int epicId = newSubTask.getEpicId();
        for (SubTask sub : epicMap.get(epicId).getSubTaskForEpic()) {
            if (sub.getId() == uniqueId) {
                int index = epicMap.get(epicId).getSubTaskForEpic().indexOf(sub);
                epicMap.get(epicId).getSubTaskForEpic().set(index, newSubTask);
                epicMap.get(epicId).getDuration();
                epicMap.get(epicId).setStatus(getStatus(epicMap.get(epicId).getSubTaskForEpic()));
            }
        }
    }

    @Override
    public void createEpic(Epic newEpic) {
        newEpic.setId(uniqueId);
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        newEpic.getStartTime();
        newEpic.getDuration();
        epicMap.put(uniqueId, newEpic);
        /*epicMap.get(uniqueId).getStartTime();
        epicMap.get(uniqueId).getDuration();*/
        assigningId();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        int uniqueId = newEpic.getId();
        for (Integer key : subTaskMap.keySet()) {
            if (uniqueId == subTaskMap.get(key).getEpicId()) {
                newEpic.getSubTaskForEpic().add(subTaskMap.get(key));
            }

        }
        newEpic.setId(uniqueId);
        newEpic.getStartTime();
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        epicMap.put(uniqueId, newEpic);
    }

    @Override
    public String getStatus(List<SubTask> subTaskForEpic) {
        ArrayList<String> subTaskStatuses = new ArrayList<>();
        if (!subTaskForEpic.isEmpty()) {
            int id = subTaskForEpic.get(0).getEpicId();
            epicMap.get(id).getStartTime();
        }

        for (SubTask s : subTaskForEpic) {
            subTaskStatuses.add(s.getStatus());
        }
        if (subTaskStatuses.isEmpty()) {
            return "NEW";
        }
        if ((subTaskStatuses.contains("NEW"))
                && (!subTaskStatuses.contains("IN_PROGRESS"))
                && (!subTaskStatuses.contains("DONE"))) {
            return "NEW";
        } else if ((subTaskStatuses.contains("DONE"))
                && (!subTaskStatuses.contains("NEW"))
                && (!subTaskStatuses.contains("IN_PROGRESS"))) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }

    @Override
    public Epic getEpicById(int uniqueId) {
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует Эпик - задача с таким ID.");
            return null;
        }
        Epic newEpic = epicMap.get(uniqueId);
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        epicMap.put(uniqueId, newEpic);
        Epic localEpic = epicMap.get(uniqueId);
        inMemoryHistoryManager.add(localEpic);
        return localEpic;
    }

    @Override
    public Task getTaskById(int uniqueId) {
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует стандартная задача с таким ID.");
        }
        Task loclTask = taskMap.get(uniqueId);
        inMemoryHistoryManager.add(loclTask);
        return loclTask;
    }

    @Override
    public SubTask getSubTaskById(int uniqueId) {
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует субзадача с таким ID.");
        }
        SubTask localSubTask = subTaskMap.get(uniqueId);
        inMemoryHistoryManager.add(localSubTask);
        return localSubTask;
    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTaskList());
        allTasks.addAll(getSubTaskList());
        allTasks.addAll(getEpicList());
        return allTasks;
    }

    @Override
    public List<SubTask> getEpicSubtasks(int uniqueId) {
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Нет эпической задачи с таким Id.");
        }
        //Epic thisEpic = epicMap.get(uniqueId);
        if (epicMap.get(uniqueId).getSubTaskForEpic().isEmpty()) {
            System.out.println("Текущая эпик-задача не имеет субзадач");
        }
        return epicMap.get(uniqueId).getSubTaskForEpic();
    }


    @Override
    public void clearAllTasks(HashMap<Integer, Task> task) {
        task.clear();
    }

    @Override
    public void clearAllSubTasks(HashMap<Integer, SubTask> subTask) {
        subTask.clear();
    }

    @Override
    public void clearAllEpic(HashMap<Integer, Epic> epic) {
        subTaskMap.clear();
        epic.clear();
    }

    @Override
    public void clearAll() {
        taskMap.clear();
        subTaskMap.clear();
        epicMap.clear();

        inMemoryHistoryManager.getHistory().clear();
    }

    @Override
    public void clearTaskById(int uniqueId) {
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Нет обычной задачи с таким Id.");
        } else {
            taskMap.remove(uniqueId);
            inMemoryHistoryManager.remove(uniqueId);
        }
    }

    @Override
    public void clearSubTaskById(int uniqueId) {
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Нет субзадачи с таким Id.");
        } else {
            for (Integer key : epicMap.keySet()) {
                epicMap.get(key).getSubTaskForEpic().remove(subTaskMap.get(uniqueId));
            }
            subTaskMap.remove(uniqueId);
            inMemoryHistoryManager.remove(uniqueId);
        }
    }

    @Override
    public void clearEpicById(int uniqueId) {
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Нет эпик-задачи с таким Id.");
        } else {
            ArrayList<Integer> idOfSubTasks = new ArrayList<>();
            for (Integer key : subTaskMap.keySet()) {
                if (subTaskMap.get(key).getEpicId() == uniqueId) {
                    idOfSubTasks.add(key);
                }
            }
            for (int i : idOfSubTasks) {
                subTaskMap.remove(i);
                inMemoryHistoryManager.remove(i);
            }
            epicMap.remove(uniqueId);
            inMemoryHistoryManager.remove(uniqueId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

}
