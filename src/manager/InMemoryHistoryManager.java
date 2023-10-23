package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public static Map<Integer, Task> historyMap = new HashMap<>();

    CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        int id = task.getId();
        if (historyMap.containsKey(id)) {
            customLinkedList.removeNode(task);
            customLinkedList.linkLast(task);
        } else {
            customLinkedList.linkLast(task);
            historyMap.put(id, customLinkedList.list.getLast());
        }
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks(customLinkedList.list);
    }

    @Override
    public void remove(int id) {
        if (historyMap.get(id) != null) {
            customLinkedList.removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }
}

class CustomLinkedList<T> extends LinkedList { 
    LinkedList<T> list = new LinkedList<>();

    public void linkLast(T data) {
        list.addLast(data);
    }

    public ArrayList<T> getTasks(LinkedList<T> l) {
        return new ArrayList<>(l);
    }

    public void removeNode(T node) {
        list.remove(node);
    }
}
