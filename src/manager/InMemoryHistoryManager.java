package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        removeNode(historyMap.get(id));
        linkLast(task);
        historyMap.put(id, last);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks(historyMap);
    }

    @Override
    public void remove(int id) {
        if (historyMap.get(id) != null) {
            removeNode(historyMap.get(id));
        }
    }

    public void linkLast(Task data) {
        final Node<Task> oldLast = last;
        final Node<Task> node = new Node<>(oldLast, data, null);
        last = node;
        if (oldLast != null) {
            oldLast.next = last;
        } else {
            first = node;
        }
    }

    public List<Task> getTasks(Map<Integer, Node<Task>> historyMap) {   // Если здесь можно как-то улучшить код, буду признателен за подсказку
        List<Task> arrayTasks = new ArrayList<>(historyMap.values().size());
        List<Node<Task>> nodes = new ArrayList<>(historyMap.values());

        Node<Task> current = null;
        int previousSize = -1;

        while(arrayTasks.size() != nodes.size() && arrayTasks.size() != previousSize) {
            previousSize = arrayTasks.size();
            for (Node<Task> n : nodes) {
                if (n.prev == current) {
                    arrayTasks.add(n.data);
                    current = n;
                    break;
                }
            }
        }
        return arrayTasks;
    }

    public void removeNode(Node<Task> node) {   //  кое-что почерпнул и адаптировал из вэбинара
        if (node == null) return;
        Node<Task> remove = historyMap.remove(node.data.getId());
        if (remove == null) {
            return;
        }
        if (remove.prev == null && remove.next == null) {
            first = null;
            last = null;
        } else if (remove.prev == null) {
            first = remove.next;
            remove.next.prev = null;
        } else if (remove.next == null) {
            last = remove.prev;
            remove.prev.next = null;
        } else {
            remove.prev.next = remove.next;
            remove.next.prev = remove.prev;
        }
    }

    public static class Node<T> {
        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}

