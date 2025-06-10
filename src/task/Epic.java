package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, List<Integer> subTasksIds) {
        super(id, name, description, status);
        this.subTasksIds = subTasksIds;
    }


    @Override
    public String toString() {
        return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s, входящие подзадачи - %s", getId(), getName(), getDescription(), getStatus().toString(), subTasksIds.toString());
    }

    public void addSubtaskToEpic(SubTask subTask) {
        subTasksIds.add(subTask.getId());
    }

    public void deleteSubtaskFromEpic(SubTask subTask) {
        subTasksIds.remove(subTask.getId());
    }

    public void deleteSubtaskFromEpic(int subTaskId) {
        subTasksIds.remove(subTaskId);
    }

    public void clearAllSubTasksFromEpic() {
        subTasksIds = new ArrayList<>();
    }

    public List<Integer> getSubtasksIds() {
        return subTasksIds;
    }

    public String subTasksIdtoString() {
        StringBuilder result = new StringBuilder();
        for (Integer integer : subTasksIds) {
            result.append(integer);
            result.append(";");
        }
        return result.toString();
    }

    public static List<Integer> stringToSubTasksId(String string) {
        List<Integer> result = new ArrayList<>();
        String[] stringArray = string.split(";");
        for (String intString : stringArray) {
            if (intString.isBlank()) {
                break;
            } else {
                result.add(Integer.parseInt(intString));
            }
        }
        return result;
    }

    @Override
    public String toCSVLine() {
        return String.format("%d,%s,%s,%s,%s,%s", getId(), TaskType.EPIC.toString(), getName(), getStatus().toString(), getDescription(), subTasksIdtoString());
    }


    public static Epic fromCSVLine(String stringData) {
        String[] dataArray = stringData.split(",");
        int id = Integer.parseInt(dataArray[0]);
        String name = dataArray[2];
        Status status = Status.valueOf(dataArray[3]);
        String description = dataArray[4];
        List<Integer> subTasksId = stringToSubTasksId(dataArray[5]);
        return new Epic(id, name, description, status, subTasksId);
    }


}
