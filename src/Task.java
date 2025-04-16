public class Task {
    int id;
    String name;
    String description;
    Status taskStatus;


    public Task(int id, String name, String description) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.taskStatus = Status.NEW;
    }

    @Override
    public String toString() {
        return String.format("ID задачи - %d, Название - %s, Описание - %s, Статус - %s",id,name,description, taskStatus.toString());
    }

    public int getId() {
        return id;
    }
}
