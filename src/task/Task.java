package task;

import manager.TaskManager;

import java.util.Objects;

public class Task {
    static private int idCounter = 1;
    private int id;
    private String name;
    private String description;
    private Status status;

    private static int getNewId() {
        return idCounter++;
    }


    public Task(String name, String description) {
        this.description = description;
        this.id = getNewId();
        this.name = name;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.description = description;
        this.id = getNewId();
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s", id, name, description, status.toString());
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
