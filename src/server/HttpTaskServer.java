
package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;


    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();
    }


    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Вы остановили сервер на порту: " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }


    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            switch (requestMethod) {
                case "GET": {
                    getHandle(httpExchange, query, path);
                    break;
                }
                case "POST": {
                    postHandle(httpExchange, query, path);
                    break;
                }
                case "DELETE": {
                    deleteHandle(httpExchange, query, path);
                    break;
                }
                default: {
                    writeResponse(httpExchange, "Неверный метод запроса - " + requestMethod, 405);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void getHandle(HttpExchange httpExchange, String query, String path) throws IOException {
        if ((Pattern.matches("^/tasks/task/$", path))
                || (Pattern.matches("^/tasks/subtask/$", path))
                || (Pattern.matches("^/tasks/epic/$", path))) {
            String response = gson.toJson(taskManager.getAllTasks());
            sendText(httpExchange, response);
            //break;
            return;
        }
        if (query != null) {
            if (Pattern.matches("^/tasks/subtask/epic/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    String response = gson.toJson(taskManager.getEpicSubtasks(id));
                    sendText(httpExchange, response);
                    //break;
                    return;
                }
            }
            if (Pattern.matches("^/tasks/task/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    String response = gson.toJson(taskManager.getTaskById(id));
                    sendText(httpExchange, response);
                    //break;
                    return;
                }
            }
        } else {
            if (Pattern.matches("^/tasks/$", path)) {
                FileBackedTasksManager fileBackedTasksManager = (FileBackedTasksManager) taskManager;
                String response = gson.toJson(fileBackedTasksManager.getPrioritizedTasks());
                sendText(httpExchange, response);
                //break;
                return;
            }
            if (Pattern.matches("^/tasks/history$", path)) {
                FileBackedTasksManager fileBackedTasksManager = (FileBackedTasksManager) taskManager;
                String response = gson.toJson(fileBackedTasksManager.getInMemoryHistoryManager().getHistory());
                sendText(httpExchange, response);
                //break;
                return;
            }

            if (Pattern.matches("^/tasks/epic/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    String response = gson.toJson(taskManager.getEpicById(id));
                    sendText(httpExchange, response);
                    //break;
                    return;
                }
            }

            if (Pattern.matches("^/tasks/subtask/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    String response = gson.toJson(taskManager.getSubTaskById(id));
                    sendText(httpExchange, response);
                    //break;
                    return;
                }
            }
        }
        httpExchange.sendResponseHeaders(405, 0);
    }

    private void postHandle(HttpExchange httpExchange, String query, String path) throws IOException {
        String body = readText(httpExchange);

        if (bodyCheck(httpExchange, body)) {
            //break;
            return;
        }

        if (Pattern.matches("^/tasks/task/$", path)) {
            Task task = gson.fromJson(body, Task.class);

            if (taskManager.getAllTasks().contains(task)) {
                taskManager.updateTask(task);
            } else {
                taskManager.createTask(task);
            }
            httpExchange.sendResponseHeaders(200, 0);
            //break;
            return;
        }

        if (Pattern.matches("^/tasks/subtask/$", path)) {
            SubTask task = gson.fromJson(body, SubTask.class);

            if (taskManager.getAllTasks().contains(task)) {
                taskManager.updateSubTask(task);
            } else {
                taskManager.createSubTask(task);
            }
            httpExchange.sendResponseHeaders(200, 0);
            //break;
            return;
        }

        if (Pattern.matches("^/tasks/epic/$", path)) {
            Epic task = gson.fromJson(body, Epic.class);

            if (taskManager.getAllTasks().contains(task)) {
                taskManager.updateEpic(task);
            } else {
                taskManager.createEpic(task);
            }
            httpExchange.sendResponseHeaders(200, 0);
            //break;
            return;
        }

        httpExchange.sendResponseHeaders(405, 0);
        //break;
        return;
    }

    private void deleteHandle(HttpExchange httpExchange, String query, String path) throws IOException {
        if (query == null) {
            if ((Pattern.matches("^/tasks/task/$", path))
                    || (Pattern.matches("^/tasks/subtask/$", path))
                    || (Pattern.matches("^/tasks/epic/$", path))) {
                taskManager.clearAll();
                System.out.println("Задачи удалены");
                httpExchange.sendResponseHeaders(200, 0);
                //break;
                return;
            }
        } else {
            if (Pattern.matches("^/tasks/task/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    taskManager.clearTaskById(id);
                    System.out.println("Задача id - " + id + " удалена.");
                } else {
                    System.out.println("Неверный id");
                }
                httpExchange.sendResponseHeaders(200, 0);
                //break;
                return;
            }

            if (Pattern.matches("^/tasks/subtask/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    taskManager.clearSubTaskById(id);
                    System.out.println("Субзадача id - " + id + " удалена.");
                } else {
                    System.out.println("Неверный id");
                }
                httpExchange.sendResponseHeaders(200, 0);
                //break;
                return;
            }

            if (Pattern.matches("^/tasks/epic/$", path)) {
                int id = parsePathId(query);

                if (id != -1) {
                    taskManager.clearEpicById(id);
                    System.out.println("Эпик задача id - " + id + " удалена.");
                } else {
                    System.out.println("Неверный id");
                }
                httpExchange.sendResponseHeaders(200, 0);
                //break;
                return;
            }
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private void writeResponse(HttpExchange httpExchange, String response, int code) throws IOException {
        if (!response.isEmpty()) {
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(code, response.length());
            httpExchange.getResponseBody().write(response.getBytes(UTF_8));
        } else {
            httpExchange.sendResponseHeaders(code, 0);
        }
    }

    private boolean bodyCheck(HttpExchange httpExchange, String body) throws IOException {
        if (body.isEmpty()) {
            writeResponse(httpExchange, "Error! Empty body", 400);
            return true;
        }
        return false;
    }
}

