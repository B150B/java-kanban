package manager;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();





    public void addTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }


    public void addSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
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
        ArrayList<Epic> result = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            result.add(epic);
        }
        return result;
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


    public void updateTask(Task oldTask, Task updatedTask) {
        oldTask = taskHashMap.get(oldTask.getId());
        oldTask.setName(updatedTask.getName());
        oldTask.setDescription(updatedTask.getDescription());
        oldTask.setStatus(updatedTask.getStatus());
    }


    public void updateSubTask(SubTask oldSubTask, Task updatedSubTask) {
        oldSubTask = subTaskHashMap.get(oldSubTask.getId());
        oldSubTask.setName(updatedSubTask.getName());
        oldSubTask.setDescription(updatedSubTask.getDescription());
        oldSubTask.setStatus(updatedSubTask.getStatus());
        setEpicStatus(epicHashMap.get(oldSubTask.getPartOfEpic()));
    }


    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }


    public void deleteSubTask(int id) {
        Epic ParentalEpic = epicHashMap.get(subTaskHashMap.get(id).getPartOfEpic());
        subTaskHashMap.remove(id);
        ParentalEpic.deleteSubtaskFromEpic(id);
        setEpicStatus(ParentalEpic);
    }


    public void deleteEpic(int id) {
        for (int subTasksId : epicHashMap.get(id).getSubtasksIds()) {
            subTaskHashMap.remove(subTasksId);
        }
        epicHashMap.remove(id);
    }


    public void setEpicStatus(Epic epic) { //Проверяем статус эпика по подзадачам внутри него
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
