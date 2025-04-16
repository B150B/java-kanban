import java.util.Objects;

public class Task {
    int id;
    String name;
    String description;
    Status status;


    public Task(String name, String description) {
        this.description = description;
        this.id = TaskManager.getNewId();
        this.name = name;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.description = description;
        this.id = TaskManager.getNewId();
        this.name = name;
        this.status = status;
    }


    @Override
    public String toString() {
        return String.format("ID задачи - %d, Название - %s, Описание - %s, Статус - %s", id, name, description, status.toString());
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        Task otherTask = (Task) obj;
        return Objects.equals(id, otherTask.id);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash *= id;
        return hash;
    }
}
