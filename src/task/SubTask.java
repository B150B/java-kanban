package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int partOfEpic;
    private static final int LENGTH_WITHOUT_DURATION = 6;


    public SubTask(String name, String description, Status status, int partOfEpic) {
        super(name, description);
        this.partOfEpic = partOfEpic;
        setStatus(status);
    }

    public SubTask(int id, String name, String description, Status status, int partOfEpic) {
        super(id, name, description, status);
        this.partOfEpic = partOfEpic;
    }

    public SubTask(String name, String description, Status status, int partOfEpic, Duration duration, LocalDateTime localDateTime) {
        super(name, description, status, duration, localDateTime);
        this.partOfEpic = partOfEpic;
    }

    public SubTask(int id, String name, String description, Status status, int partOfEpic, Duration duration, LocalDateTime localDateTime) {
        super(id, name, description, status, duration, localDateTime);
        this.partOfEpic = partOfEpic;
    }


    public int getPartOfEpic() {     //Узнаем частью какого эпика является подзадача
        return partOfEpic;
    }


    @Override
    public String toString() {
        if (hasDuration()) {
            return String.format("%n-ID подзадачи - %d, Часть Эпика - %d Название - %s, Описание - %s, Статус - %s, " +
                            "Длительность - %s, Дата начала - %s, Дата окончания - %s", getId(), partOfEpic, getName(),
                    getDescription(), getStatus().toString(), getDuration(), getStartTime(), getEndTime());
        } else {
            return String.format("%n-ID подзадачи - %d, Часть Эпика - %d Название - %s, Описание - %s, Статус - %s",
                    getId(), partOfEpic, getName(), getDescription(), getStatus().toString());
        }
    }


    public String toCSVLine() {
        if (hasDuration()) {
            return String.format("%d,%s,%s,%s,%s,%d,%d,%s",
                    getId(), TaskType.SUBTASK.toString(), getName(), getStatus().toString(), getDescription(),
                    getPartOfEpic(), getDuration().toMinutes(), getStartTime().toString());
        } else {
            return String.format("%d,%s,%s,%s,%s,%d",
                    getId(), TaskType.SUBTASK.toString(), getName(), getStatus().toString(), getDescription(),
                    getPartOfEpic());
        }
    }

    public static SubTask fromCSVLine(String stringData) {
        String[] dataArray = stringData.split(",");
        int id = Integer.parseInt(dataArray[0]);
        String name = dataArray[2];
        Status status = Status.valueOf(dataArray[3]);
        String description = dataArray[4];
        int partOfEpic = Integer.parseInt(dataArray[5]);

        if (dataArray.length == LENGTH_WITHOUT_DURATION) {
            return new SubTask(id, name, description, status, partOfEpic);
        } else {
            Duration duration = Duration.ofMinutes(Integer.parseInt(dataArray[6]));
            LocalDateTime startTime = LocalDateTime.parse(dataArray[7]);
            return new SubTask(id, name, description, status, partOfEpic, duration, startTime);
        }


    }


}
