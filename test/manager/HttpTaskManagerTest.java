package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import server.KVServer;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
        task = new Task("nt1", "dt1", "NEW", "15-15_15.11.2024", 44);
        taskManager.createTask(task);
        epic = new Epic("ne1", "de1");
        taskManager.createEpic(epic);
        subTask = new SubTask("nst11", "dst11", "NEW", epic.getId(), "13-13_13.06.2024", 23);
        taskManager.createSubTask(subTask);
        epic.setStatus(subTask.getStatus());
        epic.setStartTime(subTask.getStartTime());
        epic.setDuration(subTask.getDuration());
        epic.setEndTime();
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());
        taskManager.getEpicById(epic.getId());

    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void taskManagerShouldThrowExceptionWithTwoIntersectionTasks() {
        Task task = new Task("nt1", "dt1", "NEW", "15-15_15.11.2024", 44);
        Task task1 = new Task("nt2", "dt2", "NEW", "15-25_15.11.2024", 45);

        task.setDuration(30);
        task1.setDuration(40);

        task.setStartTime(LocalDateTime.now());
        task1.setStartTime(LocalDateTime.now().plusMinutes(10L));
        task.getEndTime();
        task1.getEndTime();

        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            taskManager.createTask(task);
            taskManager.createTask(task1);
        });

        assertEquals("manager.IntersectionIntervalException: Пересечение выполнения задач!", e.getMessage());
    }

    @Test
    public void testLoadAndSaveWithThreeTasks() throws IOException, InterruptedException {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task("nt1", "dt1", "NEW", "15-15_15.11.2024", 44);
        taskManager.createTask(task);
        SubTask subtask = new SubTask("nst11", "dst11", "NEW", epic.getId(), "13-13_13.06.2024", 23);
        taskManager.createSubTask(subtask);
        tasks.add(task);
        tasks.add(subtask);
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subtask.getId());

        HttpTaskManager taskManager1 = taskManager.load();

        assertEquals(tasks.size(), taskManager1.getAllTasks().size());
        assertEquals(2, taskManager1.getInMemoryHistoryManager().getHistory().size());
    }

    @Test
    public void testLoadEmptyData() {
        taskManager = new HttpTaskManager("http://localhost:8078");
        taskManager.clearAll();
        HttpTaskManager httpTaskManager1 = taskManager.load();

        assertEquals(0, httpTaskManager1.getAllTasks().size());
        assertEquals(0, httpTaskManager1.getInMemoryHistoryManager().getHistory().size());
    }

    @Test
    public void testRemoveAllTasks() {

        Task task = new Task("nt1", "dt1", "NEW", "15-15_15.11.2024", 44);
        Epic epic = new Epic("ne1", "de1");
        SubTask subtask = new SubTask("nst11", "dst11", "NEW", epic.getId(), "13-13_13.06.2024", 23);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);

        taskManager.clearAll();

        assertEquals(0, taskManager.getAllTasks().size());
    }
}