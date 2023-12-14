package manager;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.KVServer;
import task.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public final class Managers<T extends Task> {

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

   /* public static TaskManager getDefault(){
        return new FileBackedTasksManager(new File("tasks.csv"));
    }*/

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static KVServer getDefaultKVServer() throws IOException {
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
