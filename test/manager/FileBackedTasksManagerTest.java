package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManagerTest<T extends TaskManager> extends TaskManagerTest<FileBackedTasksManager> {
    File file;
    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        file = new File("task.csv");
        taskManager = new FileBackedTasksManager(file);
        task = new Task("nt1", "dt1", "NEW", "15-15_15.11.2024", 44);
        taskManager.createTask(task);
        epic = new Epic("ne1", "de1");
        taskManager.createEpic(epic);
        subTask = new SubTask("nst11", "dst11", "NEW", epic.getId(), "13-13_13.06.2024", 23);
        taskManager.createSubTask(subTask);
    }

    @Test
    void save() {
        deleteFile();
        HashMap<Integer, Task> taskMap = new HashMap<>();
        taskMap.put(task.getId(), task);
        HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
        subTaskMap.put(subTask.getId(), subTask);
        HashMap<Integer, Epic> epicMap = new HashMap<>();
        epicMap.put(epic.getId(), epic);
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            String header = "id,type,name,status,description,epic,startTime,duration";
            br.write(header);
            br.newLine();

            for (Integer i : taskMap.keySet()) {
                final Task task = taskMap.get(i);
                br.write(toString(task));
                br.newLine();
            }

            for (Integer i : subTaskMap.keySet()) {
                final Task task = subTaskMap.get(i);
                br.write(toString(task));
                br.newLine();
            }

            for (Integer i : epicMap.keySet()) {
                final Task task = epicMap.get(i);
                br.write(toString(task));
                br.newLine();
            }

            br.newLine();
            br.write(historyToString(this.inMemoryHistoryManager));
            br.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу сохранить файл" + file.getName());

        }
        Assertions.assertTrue(file.exists());
    }

    public String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getEpicId() + "," + task.getStartTime().format(formatter) + "," + task.getDuration();
    }

    String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        List<Task> hist = manager.getHistory();
        if (hist.isEmpty()) {
            return "";
        }
        for (Task t : hist) {
            history.append(t.getId()).append(",");
        }
        return history.toString();
    }

    @Override
    @Test
    void createTask() {
        deleteFile();
        super.createTask();
        Assertions.assertTrue(file.exists());
    }

    @Override
    @Test
    void createSubTask() {
        deleteFile();
        super.createSubTask();
        Assertions.assertTrue(file.exists());
    }

    @Override
    @Test
    void createEpic() {
        deleteFile();
        super.createEpic();
        Assertions.assertTrue(file.exists());
    }

    @Override
    @Test
    void setStatus() {
        super.setStatus();
    }

    @Override
    @Test
    void updateTask() {
        deleteFile();
        super.updateTask();
        Assertions.assertTrue(file.exists());
    }

    @Override
    @Test
    void updateSubTask() {
        deleteFile();
        super.updateSubTask();
        Assertions.assertTrue(file.exists());
    }

    @Override
    @Test
    void updateEpic() {
        deleteFile();
        super.updateEpic();
        Assertions.assertTrue(file.exists());
    }

    @Override
    @Test
    void getStatus() {
        super.getStatus();
    }

    @Override
    @Test
    void getEpicById() {
        super.getEpicById();
    }

    @Override
    @Test
    void getTaskById() {
        super.getTaskById();
    }

    @Override
    @Test
    void getSubTaskById() {
        super.getSubTaskById();
    }

    @Override
    @Test
    void getTaskList() {
        super.getTaskList();
    }

    @Override
    @Test
    void getSubTaskList() {
        super.getSubTaskList();
    }

    @Override
    @Test
    void getEpicList() {
        super.getEpicList();
    }

    @Override
    @Test
    void getEpicSubtasks() {
        super.getEpicSubtasks();
    }

    @Override
    @Test
    void clearAllTasks() {
        super.clearAllTasks();
    }

    @Override
    @Test
    void clearAllSubTasks() {
        super.clearAllSubTasks();
    }

    @Override
    @Test
    void clearAllEpic() {
        super.clearAllEpic();
    }

    @Override
    @Test
    void clearTaskById() {
        super.clearTaskById();
    }

    @Override
    @Test
    void clearSubTaskById() {
        super.clearSubTaskById();
    }

    @Override
    @Test
    void clearEpicById() {
        super.clearEpicById();
    }

    @Override
    @Test
    void getHistory() {
        super.getHistory();
    }

    public void deleteFile() {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
