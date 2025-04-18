package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }


    @Override
    public String toString() {
        return String.format("%nID задачи - %d, Название - %s, Описание - %s, Статус - %s, входящие подзадачи - %s",getId(),getName(),getDescription(), getStatus().toString(), subTasksIds.toString());
    }

    public void addSubtaskToEpic (SubTask subTask) {
        subTasksIds.add(subTask.getId());
    }

    public void deleteSubtaskFromEpic (SubTask subTask) {
        subTasksIds.remove(subTask.getId());
    }

    public void deleteSubtaskFromEpic (int subTaskId) {
        subTasksIds.remove(subTaskId);
    }

    public void clearAllSubTasksFromEpic () {
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds () {
        return subTasksIds;
    }




}
