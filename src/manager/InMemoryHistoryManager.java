package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    LinkedList<Node<Task>> list = new LinkedList<>(); // Смущает пункт про CustomLinkedList, может я его неправильно понял,
    private Node<Task> first;                         // и имелось ввиду именно методы добавить, а не пытаться создать реализацию с нуля
    private Node<Task> last;


    @Override
    public void add(Task task) {
        int id = task.getId();
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            linkLast(task);
            historyMap.remove(id);
            historyMap.put(id, list.getLast());
        } else {
            linkLast(task);
            historyMap.put(id, list.getLast());
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks(list);
    }

    @Override
    public void remove(int id) {
        if (historyMap.get(id) != null) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    public static class Node<T> {  // Тут действительно у меня возникал вопрос почему в ТЗ предлагается создать отдельный класс под него
        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public void linkLast(Task data) {
        final Node<Task> oldLast = last;
        final Node<Task> node = new Node<>(oldLast, data, null);
        last = node;
        if (oldLast != null) {
            oldLast.next = last;
            list.add(last);
        } else {
            first = node;
            list.add(first);
        }
    }

    public ArrayList<Task> getTasks(LinkedList<Node<Task>> l) {
        ArrayList<Task> array = new ArrayList<>(l.size());
        for (int i = 0; i < l.size(); i++) {
            array.add(i, l.get(i).data);
        }
        return array;
    }

    public void removeNode(Node<Task> node) {
        list.remove(node);
    }
}

