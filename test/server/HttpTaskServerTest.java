package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {    // Часть тестов наладил, с оставшимися, нужен совет

    private final Gson gson = Managers.getGson();

    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private HttpClient httpClient;
    private Task task;
    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpClient = HttpClient.newHttpClient();
        task = new Task("nt1", "dt1", "NEW", "15-15_15.11.2024", 44);

        httpTaskServer.start();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        taskManager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        System.out.println(json);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void removeTask() throws IOException, InterruptedException {
        taskManager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        taskManager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldRemoveTaskByIdOnTheDeleteRequest() throws IOException, InterruptedException {
        taskManager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getSubtaskForEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "descr");
        SubTask subtask = new SubTask("nst11", "dst11", "NEW", epic.getId(), "13-13_13.06.2024", 23);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        int epicId = epic.getId();

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        Task task1 = new Task("nt2", "dt2", "NEW", "15-15_15.11.2025", 45);
        List<Task> historyList = new ArrayList<>();
        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task1.getId());
        historyList.add(task);
        historyList.add(task1);

        String jsonHistory = gson.toJson(historyList);

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(jsonHistory, response.body());
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        TreeSet<Task> treeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        Task task1 = new Task("nt2", "dt2", "NEW", "15-15_15.11.2025", 45);
        taskManager.createTask(task);
        taskManager.createTask(task1);
        treeSet.add(task);
        treeSet.add(task1);

        String jsonPrioritizedTasks = gson.toJson(treeSet);

        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(jsonPrioritizedTasks, response.body());
    }
}