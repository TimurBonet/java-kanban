package manager;

import http.HttpTaskManager;
import manager.Managers;
import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import server.KVServer;


import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void taskManagerShouldThrowExceptionWithTwoIntersectionTasks() {
        Task task = new Task("nt1","dt1","NEW","15-15_15.11.2024", 44);
        Task task1 = new Task("nt2","dt2","NEW","15-15_15.11.2025", 45);

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

        assertEquals("exception.IntersectionIntervalException: Пересечение выполнения задач!", e.getMessage());
    }

    @Test
    public void testLoadAndSaveWithThreeTasks() throws IOException, InterruptedException {
        HttpTaskManager secondTaskManager;
        Task task = new Task("nt1","dt1","NEW","15-15_15.11.2024", 44);
        Epic epic = new Epic("ne1","de1");
        SubTask subtask = new SubTask("nst11","dst11","NEW", epic.getId(),"13-13_13.06.2024", 23);
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);

        secondTaskManager = taskManager.load("http://localhost:8078");

        assertEquals(3, secondTaskManager.getAllTasks().size());
        assertEquals(0, secondTaskManager.getInMemoryHistoryManager().getHistory().size());
    }

    @Test
    public void testLoadEmptyData() {
        taskManager.load("http://localhost:8078");

        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getInMemoryHistoryManager().getHistory().size());
    }

    @Test
    public void testRemoveAllTasks() {
        Task task = new Task("nt1","dt1","NEW","15-15_15.11.2024", 44);
        Epic epic = new Epic("ne1","de1");
        SubTask subtask = new SubTask("nst11","dst11","NEW", epic.getId(),"13-13_13.06.2024", 23);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);

        taskManager.clearAll();

        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getInMemoryHistoryManager().getHistory().size());
    }
}