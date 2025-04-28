package manager;


import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private ArrayList<Task> history = new ArrayList<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public int getNewId() {
        return id++;
    }


    @Override
    public void addTask(Task task) {
        int newId = getNewId();
        task.setId(newId);
        taskHashMap.put(newId, task);
    }


    @Override
    public void addEpic(Epic epic) {
        int newId = getNewId();
        epic.setId(newId);
        epicHashMap.put(newId, epic);
    }


    @Override
    public void addSubTask(SubTask subTask) {
        int newId = getNewId();
        subTask.setId(newId);
        subTaskHashMap.put(newId, subTask);
        Epic parentalEpic = epicHashMap.get(subTask.getPartOfEpic());
        parentalEpic.addSubtaskToEpic(subTask);
        setEpicStatus(parentalEpic);
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }


    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }


    @Override
    public ArrayList<SubTask> getSubTasksFromEpic(int id) {
        Epic epic = epicHashMap.get(id);
        ArrayList<SubTask> result = new ArrayList<>();
        for (Integer subTaskId : epic.getSubtasksIds()) {
            result.add(subTaskHashMap.get(subTaskId));
        }
        return result;
    }


    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }


    @Override
    public void clearAllTasks() {
        taskHashMap.clear();
    }


    @Override
    public void clearAllSubTasks() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.clearAllSubTasksFromEpic();
            setEpicStatus(epic);
        }
    }


    @Override
    public void clearAllEpics() {
        epicHashMap.clear();
        subTaskHashMap.clear();
    }


    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);
        addToHistory(task);
        return task;
    }


    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        addToHistory(subTask);
        return subTask;
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        addToHistory(epic);
        return epic;
    }


    @Override
    public void updateTask(Task updatedTask) {
        taskHashMap.put(updatedTask.getId(), updatedTask);
    }


    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        subTaskHashMap.put(updatedSubTask.getId(), updatedSubTask);
        setEpicStatus(epicHashMap.get(updatedSubTask.getPartOfEpic()));
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        epicHashMap.put(updatedEpic.getId(), updatedEpic);
    }


    @Override
    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }


    @Override
    public void deleteSubTask(int id) {
        Epic parentalEpic = epicHashMap.get(subTaskHashMap.get(id).getPartOfEpic());
        subTaskHashMap.remove(id);
        parentalEpic.deleteSubtaskFromEpic(id);
        setEpicStatus(parentalEpic);
    }


    @Override
    public void deleteEpic(int id) {
        for (int subTasksId : epicHashMap.get(id).getSubtasksIds()) {
            subTaskHashMap.remove(subTasksId);
        }
        epicHashMap.remove(id);
    }


    @Override
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


    public ArrayList<Task> getHistory() {
        return history;
    }

    private void addToHistory(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(9);
            history.add(task);
        }
    }
}
