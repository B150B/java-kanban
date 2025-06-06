package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> subTasksIds) {
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

    public ArrayList<Integer> getSubtasksIds() {
        return subTasksIds;
    }

    public String subTasksIdtoString() {
        String result = "";
        for (Integer integer : subTasksIds) {
            result += integer + ";";
        }
        return result;
    }

    public static ArrayList<Integer> stringToSubTasksId(String string) {
        ArrayList<Integer> result = new ArrayList<>();
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
        ArrayList<Integer> subTasksId = stringToSubTasksId(dataArray[5]);
        return new Epic(id, name, description, status, subTasksId);
    }


}
