package manager;

import task.Task;

import java.util.Objects;

public class Node {
    Node next;
    Node prev;
    Task task;


    public Node(Task task) {
        this.task = task;
    }

    public Node(Node prev, Node next, Task task) {
        this.next = next;
        this.prev = prev;
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task);
    }


}
