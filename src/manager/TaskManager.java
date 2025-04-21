package manager;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();

    private int getNewId() {
        return id++;
    }


    public void addTask(Task task) {
        int newId = getNewId();
        task.setId(newId);
        taskHashMap.put(newId, task);
    }


    public void addEpic(Epic epic) {
        int newId = getNewId();
        epic.setId(newId);
        epicHashMap.put(newId, epic);
    }


    public void addSubTask(SubTask subTask) {
        int newId = getNewId();
        subTask.setId(newId);
        subTaskHashMap.put(newId, subTask);
        Epic parentalEpic = epicHashMap.get(subTask.getPartOfEpic());
        parentalEpic.addSubtaskToEpic(subTask);
        setEpicStatus(parentalEpic);
    }


    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }


    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }


    public ArrayList<SubTask> getSubTasksFromEpic(int id) {
        Epic epic = epicHashMap.get(id);
        ArrayList<SubTask> result = new ArrayList<>();
        for (Integer subTaskId : epic.getSubtasksIds()) {
            result.add(subTaskHashMap.get(subTaskId));
        }
        return result;
    }


    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }


    public void clearAllTasks() {
        taskHashMap.clear();
    }


    public void clearAllSubTasks() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.clearAllSubTasksFromEpic();
            setEpicStatus(epic);
        }
    }


    public void clearAllEpics() {
        epicHashMap.clear();
        subTaskHashMap.clear();
    }


    public Task getTask(int id) {
        return taskHashMap.get(id);
    }


    public SubTask getSubTask(int id) {
        return subTaskHashMap.get(id);
    }


    public Epic getEpic(int id) {
        return epicHashMap.get(id);
    }


    public void updateTask(Task updatedTask) {
        taskHashMap.put(updatedTask.getId(), updatedTask);
    }


    public void updateSubTask(SubTask updatedSubTask) {
        subTaskHashMap.put(updatedSubTask.getId(), updatedSubTask);
        setEpicStatus(epicHashMap.get(updatedSubTask.getPartOfEpic()));
    }

    public void updateEpic(Epic updatedEpic) {
        epicHashMap.put(updatedEpic.getId(), updatedEpic);
    }


    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }


    public void deleteSubTask(int id) {
        Epic parentalEpic = epicHashMap.get(subTaskHashMap.get(id).getPartOfEpic());
        subTaskHashMap.remove(id);
        parentalEpic.deleteSubtaskFromEpic(id);
        setEpicStatus(parentalEpic);
    }


    public void deleteEpic(int id) {
        for (int subTasksId : epicHashMap.get(id).getSubtasksIds()) {
            subTaskHashMap.remove(subTasksId);
        }
        epicHashMap.remove(id);
    }


    private void setEpicStatus(Epic epic) { //Проверяем статус эпика по подзадачам внутри него
        boolean isNew = true;
        boolean isDone = true;
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTaskId : epic.getSubtasksIds()) {
            subTasks.add(subTaskHashMap.get(subTaskId));
        }
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                isNew = false;
                isDone = false;
            } else if (subTask.getStatus().equals(Status.NEW)) {
                isDone = false;
            } else {
                isNew = false;
            }
        }
        if (isDone) {
            epic.setStatus(Status.DONE);
        } else if (isNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


}
