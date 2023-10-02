package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    static final int LIST_SIZE = 10;
    public static List<Task> historyList = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task){
        if (historyList.size() < LIST_SIZE) {
            historyList.add(task);
        } else {
            historyList.remove(0);
            historyList.add(task);
        }
    }

}
