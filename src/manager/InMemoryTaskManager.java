package manager;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private int uniqueId = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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
    }                           //теперь только в этом менеджере

    @Override
    public void createTask(Task newTask) {                              // Создать новую обычную задачу
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
    public void updateTask(Task newTask) {                  //  Теперь при обновлении задачи она заносится полностью
        int uniqueId = newTask.getId();                     //  что, вроде как, соответствует ТЗ
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        taskMap.put(uniqueId, newTask);
    }

    @Override
    public void createSubTask(SubTask newSubTask) {                     // Создать новую субзадачу
        int epicId = newSubTask.getEpicId();                             // извлекаем имя эпика для которого она создана

        if (!epicMap.containsKey(epicId)) {
            System.out.println(epicId + " - Эпик-задача с таким Id отсутствует");
        }
        newSubTask.setId(uniqueId);
        subTaskMap.put(uniqueId, newSubTask);                     // добавляем субзадачу в мапу субзадач
        epicMap.get(epicId).getSubTaskForEpic().add(newSubTask);  // добавляем субзадачу в список субзадач эпика
        epicMap.get(epicId).setStatus(getStatus(epicMap.get(epicId).getSubTaskForEpic())); // обновление статуса
        assigningId();                                            // меняем айди
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {                     // обновить субзадачу
        int uniqueId = newSubTask.getId();
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        subTaskMap.put(uniqueId, newSubTask);
        int epicId = newSubTask.getEpicId();
        for (SubTask sub : epicMap.get(epicId).getSubTaskForEpic()) { //Упростили вложение т.к. подаются только
            if (sub.getId() == uniqueId) {                            // элементы subTaskMap
                int index = epicMap.get(epicId).getSubTaskForEpic().indexOf(sub);
                epicMap.get(epicId).getSubTaskForEpic().set(index, newSubTask);  // помещаю в сабтаскЛист эпика с epicId
                epicMap.get(epicId).setStatus(getStatus(epicMap.get(epicId).getSubTaskForEpic())); // обновление статуса
            }
        }
    }

    @Override
    public void createEpic(Epic newEpic) {                              // Создать новый эпик-задачу
        newEpic.setId(uniqueId);
        epicMap.put(uniqueId, newEpic);
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
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        epicMap.put(uniqueId, newEpic);
    }

    @Override
    public String getStatus(List<SubTask> subTaskForEpic) {
        ArrayList<String> subTaskStatuses = new ArrayList<>();
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

// Блок получения задачи по ID

    @Override
    public Epic getEpicById(int uniqueId) {                           //Получить эпик-задачу по ID
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует Эпик - задача с таким ID.");
            return null;
        }
        Epic newEpic = epicMap.get(uniqueId);
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        epicMap.put(uniqueId, newEpic);
        Epic localEpic = epicMap.get(uniqueId);                //Локальные переменные, как я понял, вот так должны быть.
        inMemoryHistoryManager.add(localEpic);
        return localEpic;
    }

    @Override
    public Task getTaskById(int uniqueId) {                               // Получить обычную задачу по ID
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует стандартная задача с таким ID.");
        }
        Task loclTask = taskMap.get(uniqueId);                 //Локальный Task
        inMemoryHistoryManager.add(loclTask);
        return loclTask;
    }

    @Override
    public SubTask getSubTaskById(int uniqueId) {                            // Получить субзадачу по ID
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует субзадача с таким ID.");
        }
        SubTask localSubTask = subTaskMap.get(uniqueId);        // Локальная SubTask
        inMemoryHistoryManager.add(localSubTask);
        return localSubTask;
    }

// Блок вызова списка задач

    @Override
    public List<Task> getTaskList(HashMap<Integer, Task> task) {
        return new ArrayList<>(task.values());
    }

    @Override
    public List<SubTask> getSubTaskList(HashMap<Integer, SubTask> subTask) {
        return new ArrayList<>(subTask.values());
    }

    @Override
    public List<Epic> getEpicList(HashMap<Integer, Epic> epic) {
        return new ArrayList<>(epic.values());
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

// Блок удаления всех задач

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
        subTaskMap.clear();                // предполагается что subTask могут существовать только как часть Tasks.Epic задачи.
        epic.clear();
    }

    // Блок удаления по ID

    @Override
    public void clearTaskById(int uniqueId) {
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Нет обычной задачи с таким Id.");
        } else {
            taskMap.remove(uniqueId);
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
            }
            epicMap.remove(uniqueId);
        }
    }

    // Блок истории
    @Override
    public List<Task> getHistory() {
        return InMemoryHistoryManager.historyList;
    }

}
