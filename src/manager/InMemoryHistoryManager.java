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
        if (idAndNodeMap.containsKey(id)) {    //Проверка на null, потому что теперь некоторые методы читают task у переданного в них node
            removeNode(idAndNodeMap.get(id));
        }
        linkLast(task);
        idAndNodeMap.put(id, tail);

    }

    @Override
    public void remove(int id) {
        removeNode(idAndNodeMap.get(id));
        idAndNodeMap.remove(id);
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


    public void removeNode(Node node) {
        Node currentNode = idAndNodeMap.remove(node.task.getId());
        if (currentNode == null) {
            return;
        } else {
            if (currentNode.prev != null) {
                currentNode.prev.next = currentNode.next;
            } else {
                head = currentNode.next;
            }

            if (currentNode.next != null) {
                currentNode.next.prev = currentNode.prev;
            } else {
                tail = currentNode.prev;
            }
        }
    }


}


