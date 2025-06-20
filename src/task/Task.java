package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String name, String description) {
        this.description = description;
        this.name = name;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.description = description;
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }


    public Task(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }


    public void setId(int id) {
        this.id = id;
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

    public Duration getDuration() {
        if (hasDuration()) {
            return duration;
        } else {
            return Duration.ofMinutes(0);
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        if (hasDuration()) {
            return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s, " +
                            "Длительность - %s, Дата начала - %s, Дата окончания - %s",
                    id, name, description, status.toString(), getDuration(), getStartTime(), getEndTime());
        } else {
            return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s ",
                    id, name, description, status.toString());
        }
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

    public boolean hasDuration() {
        return (duration != null);
    }

    public boolean hasOverlap(Task task) {
        if (!this.hasDuration() || !task.hasDuration()) {
            return false;
        } else {
            return ((this.getStartTime().isBefore(task.getEndTime())) && (this.getEndTime().isAfter(task.getStartTime())));
        }
    }


    public String toCSVLine() {
        if (hasDuration()) {
            return String.format("%d,%s,%s,%s,%s,%d,%s",
                    id, TaskType.TASK.toString(), name, getStatus().toString(),
                    description, duration.toMinutes(), startTime.toString());
        } else {
            return String.format("%d,%s,%s,%s,%s",
                    id, TaskType.TASK.toString(), name, getStatus().toString(),
                    description);
        }
    }


    public static Task fromCSVLine(String stringData) {
        String[] dataArray = stringData.split(",");
        int id = Integer.parseInt(dataArray[0]);
        String name = dataArray[2];
        Status status = Status.valueOf(dataArray[3]);
        String description = dataArray[4];

        if (dataArray.length == 5) {
            return new Task(id, name, description, status);
        } else {
            Duration duration = Duration.ofMinutes(Integer.parseInt(dataArray[5]));
            LocalDateTime startTime = LocalDateTime.parse(dataArray[6]);
            return new Task(id, name, description, status, duration, startTime);
        }
    }

}
