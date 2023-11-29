package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager>{
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    @Test
    void createTask() {
            final int taskId = task.getId();

            final Task savedTask = taskManager.getTaskById(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task, savedTask, "Задачи не совпадают.");

            final List<Task> tasks = taskManager.getTaskList();

            assertNotNull(tasks, "Задачи на возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void setStatus() {
        task.setStatus("IN_PROGRESS");
        Assertions.assertEquals("IN_PROGRESS",task.getStatus());
    }

    @Test
    void updateTask() {
        final int id = task.getId();
        Task newTask = new Task("n2","d2","IN_PROGRESS","08-33_09.05.2024", 56);
        newTask.setId(id);
        taskManager.updateTask(newTask);
        final List<Task> tasks = taskManager.getTaskList();
        Assertions.assertEquals(newTask,tasks.get(0), "Задачи не совпадают.");
        assertNotNull(newTask, "Задача не найдена.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

    }

    @Test
    void createSubTask() {
        final int id = subTask.getId();

        final SubTask savedSubTask = taskManager.getSubTaskById(id);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubTaskList();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateSubTask() {
        final int id = subTask.getId();

        SubTask subTask1 = new SubTask("n2","d2","IN_PROGRESS",subTask.getEpicId(),"17-18_19.12.2023", 21);

        subTask1.setId(id);
        taskManager.updateSubTask(subTask1);

        final List<SubTask> subTasks = taskManager.getSubTaskList();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        Assertions.assertEquals(subTask1, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");

    }

    @Test
    void createEpic() {
        final int id = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(id);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> savedEpics = taskManager.getEpicList();

        assertNotNull(savedEpics, "Задачи на возвращаются.");
        assertEquals(1, savedEpics.size(), "Неверное количество задач.");
        assertEquals(epic, savedEpics.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        final int id = epic.getId();
        Epic newEpic = new Epic("n2","d2");
        newEpic.setId(id);

        taskManager.updateEpic(newEpic);
        final List<Epic> epics = taskManager.getEpicList();

        Assertions.assertEquals(newEpic,epics.get(0), "Задачи не совпадают.");

        assertNotNull(newEpic, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");

    }

    @Test
    void getStatus() {
        final List<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask("n21","d21","NEW",0,"17-15_13.12.2023", 21);
        SubTask subTask2 = new SubTask("n22","d22","NEW",0,"17-12_11.12.2023", 23);
        SubTask subTask3 = new SubTask("n23","d23","IN_PROGRESS",0,"17-21_13.12.2023", 24);
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        subTasks.add(subTask3);
        String status;
        ArrayList<String> subTaskStatuses = new ArrayList<>();
        for (SubTask s : subTasks) {
            subTaskStatuses.add(s.getStatus());
        }
        if (subTaskStatuses.isEmpty()) {
            status = "NEW";
        }
        if ((subTaskStatuses.contains("NEW"))
                && (!subTaskStatuses.contains("IN_PROGRESS"))
                && (!subTaskStatuses.contains("DONE"))) {
            status = "NEW";
        } else if ((subTaskStatuses.contains("DONE"))
                && (!subTaskStatuses.contains("NEW"))
                && (!subTaskStatuses.contains("IN_PROGRESS"))) {
            status = "DONE";
        } else {
            status = "IN_PROGRESS";
        }
        Assertions.assertEquals("IN_PROGRESS",status);
    }

    @Test
    void getEpicById() {
        HashMap<Integer,Epic> epicMap = new HashMap<>();
        epicMap.put(0,epic);
        final int id = 0;
        if (!epicMap.containsKey(id)) {
            System.out.println("Отсутствует Эпик - задача с таким ID.");
        }
        Epic newEpic = epicMap.get(id);

        Assertions.assertEquals(epic, newEpic);
    }

    @Test
    void getTaskById() {
        HashMap<Integer,Task> taskMap = new HashMap<>();
        final int id = 0;
        taskMap.put(0,task);
        if (!taskMap.containsKey(id)) {
            System.out.println("Отсутствует стандартная задача с таким ID.");
        }
        Task loclTask = taskMap.get(id);

        Assertions.assertEquals(task,loclTask);
    }

    @Test
    void getSubTaskById() {
        HashMap<Integer,SubTask> subTaskMap = new HashMap<>();
        final int id = 0;
        subTaskMap.put(0,subTask);
        if (!subTaskMap.containsKey(id)) {
            System.out.println("Отсутствует субзадача с таким ID.");
        }
        SubTask localSubTask = subTaskMap.get(id);

        Assertions.assertEquals(subTask, localSubTask);
    }

    @Test
    void getTaskList() {
        HashMap<Integer,Task> taskMap = new HashMap<>();
        taskMap.put(0,task);

        final List<Task> savedTasks = new ArrayList<>(taskMap.values());

        assertNotNull(savedTasks, "Задачи на возвращаются.");
        assertEquals(1, savedTasks.size(), "Неверное количество задач.");
        assertEquals(task, savedTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getSubTaskList() {
        HashMap<Integer,SubTask> subTaskMap = new HashMap<>();
        subTaskMap.put(0,subTask);

        final List<SubTask> savedSubTasks =  new ArrayList<>(subTaskMap.values());

        assertNotNull(savedSubTasks, "Задачи на возвращаются.");
        assertEquals(1, savedSubTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, savedSubTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getEpicList() {
        HashMap<Integer,Epic> epicMap = new HashMap<>();
        epicMap.put(0,epic);

        final List<Task> savedEpics =  new ArrayList<>(epicMap.values());

        assertNotNull(savedEpics, "Задачи на возвращаются.");
        assertEquals(1, savedEpics.size(), "Неверное количество задач.");
        assertEquals(epic, savedEpics.get(0), "Задачи не совпадают.");
    }

    @Test
    void getEpicSubtasks() {
        final List<SubTask> subTasks = new ArrayList<>();
        subTasks.add(0,subTask);
        HashMap<Integer,Epic> epicMap = new HashMap<>();
        epicMap.put(0,epic);
        List<SubTask> epicSubTasks = epicMap.get(0).getSubTaskForEpic();

        assertNotNull(epicSubTasks, "Задачи на возвращаются.");
        assertEquals(1, epicSubTasks.size(), "Неверное количество задач.");
        assertArrayEquals(subTasks.toArray(), epicSubTasks.toArray(), "Задачи не совпадают.");

    }

    @Test
    void clearAllTasks() {
        HashMap<Integer,Task>  taskMap = new HashMap<>();
        taskMap.put(task.getId(),task);
        taskManager.clearAllTasks(taskMap);
        Assertions.assertEquals(0,taskMap.size(), "HashMap Tasks не пуста");
    }

    @Test
    void clearAllSubTasks() {
        HashMap<Integer,SubTask>  subTaskMap = new HashMap<>();
        subTaskMap.put(subTask.getId(),subTask);
        taskManager.clearAllSubTasks(subTaskMap);
        Assertions.assertEquals(0,subTaskMap.size(), "HashMap SubTasks не пуста");
    }

    @Test
    void clearAllEpic() {
        HashMap<Integer,Epic>  epicMap = new HashMap<>();
        epicMap.put(epic.getId(),epic);
        taskManager.clearAllEpic(epicMap);
        Assertions.assertEquals(0,epicMap.size(), "HashMap Epics не пуста");
    }

    @Test
    void clearTaskById() {
        HashMap<Integer,Task>  taskMap = new HashMap<>();
        taskMap.put(task.getId(),task);
        final int id = task.getId();
        taskMap.remove(id);
        Assertions.assertEquals(0,taskMap.size(), "Некорректный размер ожидаемой/полученной HashMap");

    }

    @Test
    void clearSubTaskById() {
        HashMap<Integer,SubTask>  subTaskMap = new HashMap<>();
        subTaskMap.put(subTask.getId(),subTask);
        final int id = subTask.getId();
        subTaskMap.remove(id);
        Assertions.assertEquals(0,subTaskMap.size(), "Некорректный размер ожидаемой/полученной HashMap");
    }

    @Test
    void clearEpicById() {
        HashMap<Integer,Epic>  epicMap = new HashMap<>();
        epicMap.put(epic.getId(),epic);
        final int id = epic.getId();
        epicMap.remove(id);
        Assertions.assertEquals(0,epicMap.size(), "Некорректный размер ожидаемой/полученной HashMap");
    }

    @Test
    void getHistory() {
        final List<Task> history = new ArrayList<>();
        Task newTask = new Task("n2","d2","IN_PROGRESS","08-33_09.05.2024", 56);
        history.add(newTask);
        List<Task> expected = List.of(newTask);

        assertEquals(1, history.size(), "Неверное количество задач.");
        assertArrayEquals(expected.toArray(), history.toArray(), "Задачи не совпадают.");
    }
}