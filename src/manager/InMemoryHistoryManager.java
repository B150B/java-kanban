package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private Map<Integer, Node> idAndNodeMap = new HashMap<>();


    @Override
    public void add(Task task) {
        int id = task.getId();
        removeNode(idAndNodeMap.remove(id));
        linkLast(task);
        idAndNodeMap.put(id, tail);
    }

    @Override
    public void remove(int id) {
        removeNode(idAndNodeMap.remove(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }


    public void linkLast(Task task) {
        if (head == null) {
            head = new Node(task);
            tail = head;
        } else {
            Node newNode = new Node(tail, null, task);
            tail.next = newNode;
            tail = newNode;
        }
    }

    public ArrayList<Task> getTasks() {
        Node currentNode = head;
        ArrayList<Task> result = new ArrayList<>();
        while (currentNode != null) {
            result.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return result;
    }


    private void removeNode(Node node) {
        if (node == null) {
            return;
        } else {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
        }
    }


}


