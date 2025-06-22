package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Epic extends Task {

    private List<Integer> subTasksIds;
    private LocalDateTime endTime;
    private static final int LENGTH_WITHOUT_DURATION = 6;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, List<Integer> subTasksIds) {
        super(id, name, description, status);
        this.subTasksIds = subTasksIds;
    }

    public Epic(String name, String description, Status status, List<Integer> subTasksIds, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.subTasksIds = subTasksIds;
    }

    public Epic(int id, String name, String description, Status status, List<Integer> subTasksIds, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.subTasksIds = subTasksIds;
    }

    public Epic(int id, String name, String description, Status status, List<Integer> subTasksIds, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, status, duration, startTime);
        this.subTasksIds = subTasksIds;
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        if (hasDuration()) {
            return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s, входящие подзадачи - %s, " +
                            "Длительность - %s, Дата начала - %s, Дата окончания - %s ",
                    getId(), getName(), getDescription(), getStatus().toString(), subTasksIds.toString(), getDuration(),
                    getStartTime(), getEndTime());
        } else {
            return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s, входящие подзадачи - %s, ",
                    getId(), getName(), getDescription(), getStatus().toString(), subTasksIds.toString());
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubtaskToEpic(SubTask subTask) {
        subTasksIds.add(subTask.getId());
    }

    public void deleteSubtaskFromEpic(SubTask subTask) {
        subTasksIds.remove(subTask.getId());
    }

    public void deleteSubtaskFromEpic(int subTaskId) {
        subTasksIds.remove(Integer.valueOf(subTaskId));
    }

    public void clearAllSubTasksFromEpic() {
        subTasksIds = new ArrayList<>();
    }

    public List<Integer> getSubtasksIds() {
        return subTasksIds;
    }

    public String subTasksIdtoString() {
        return subTasksIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }

    public static List<Integer> stringToSubTasksId(String string) {
        return Arrays.stream(string.split(";"))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Override
    public String toCSVLine() {
        if (hasDuration()) {
            return String.format("%d,%s,%s,%s,%s,%s,%d,%s,%s", getId(), TaskType.EPIC.toString(), getName(),
                    getStatus().toString(), getDescription(), subTasksIdtoString(), getDuration().toMinutes(),
                    getStartTime().toString(), getEndTime().toString());
        } else {
            return String.format("%d,%s,%s,%s,%s,%s", getId(), TaskType.EPIC.toString(), getName(),
                    getStatus().toString(), getDescription(), subTasksIdtoString());
        }
    }


    public static Epic fromCSVLine(String stringData) {
        String[] dataArray = stringData.split(",");
        int id = Integer.parseInt(dataArray[0]);
        String name = dataArray[2];
        Status status = Status.valueOf(dataArray[3]);
        String description = dataArray[4];
        List<Integer> subTasksId = stringToSubTasksId(dataArray[5]);

        if (dataArray.length == LENGTH_WITHOUT_DURATION) {
            return new Epic(id, name, description, status, subTasksId);
        } else {
            Duration duration = Duration.ofMinutes(Integer.parseInt(dataArray[6]));
            LocalDateTime startTime = LocalDateTime.parse(dataArray[7]);
            LocalDateTime endTime = LocalDateTime.parse(dataArray[7]);
            return new Epic(id, name, description, status, subTasksId, duration, startTime, endTime);
        }


    }


}
