package manager;


import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected int id = 1;
    protected Map<Integer, Task> taskHashMap = new HashMap<>();
    protected Map<Integer, Epic> epicHashMap = new HashMap<>();
    protected Map<Integer, SubTask> subTaskHashMap = new HashMap<>();
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));


    protected int getNewId() {
        return id++;
    }


    @Override
    public void addTask(Task task) {
        if (hasOverlapWithAnyTask(task)) {
            System.out.println("При добавлении задачи произошло пересечение! Задача не добавлена");
            return;
        } else {
            int newId = getNewId();
            task.setId(newId);
            taskHashMap.put(newId, task);
            if (task.hasDuration()) {
                sortedTasks.add(task);
            }
        }
    }


    @Override
    public void addEpic(Epic epic) {
        int newId = getNewId();
        epic.setId(newId);
        epicHashMap.put(newId, epic);
    }


    @Override
    public void addSubTask(SubTask subTask) {
        if (hasOverlapWithAnyTask(subTask)) {
            System.out.println("При добавлении задачи произошло пересечение! Задача не добавлена");
            return;
        } else {
            int newId = getNewId();
            subTask.setId(newId);
            subTaskHashMap.put(newId, subTask);
            Epic parentalEpic = epicHashMap.get(subTask.getPartOfEpic());
            parentalEpic.addSubtaskToEpic(subTask);
            setEpicStatus(parentalEpic);
            setEpicTimeAndDuration(parentalEpic);
            if (subTask.hasDuration()) {
                sortedTasks.add(subTask);
            }
        }
    }


    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }


    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }


    @Override
    public List<SubTask> getSubTasksFromEpic(int id) {
        Epic epic = epicHashMap.get(id);
        List<SubTask> result = new ArrayList<>();
        epic.getSubtasksIds().stream()
                .forEach(subtaskId -> result.add(subTaskHashMap.get(subtaskId)));
        return result;
    }


    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }


    @Override
    public void clearAllTasks() {
        taskHashMap.keySet().stream()
                .forEach(id -> inMemoryHistoryManager.remove(id));
        taskHashMap.clear();
    }


    @Override
    public void clearAllSubTasks() {
        subTaskHashMap.keySet().stream()
                .forEach(id -> inMemoryHistoryManager.remove(id));
        subTaskHashMap.clear();
        epicHashMap.values().stream()
                .forEach(epic -> {
            epic.clearAllSubTasksFromEpic();
            setEpicStatus(epic);
        });
    }


    @Override
    public void clearAllEpics() {
        epicHashMap.keySet().stream()
                .forEach(id -> inMemoryHistoryManager.remove(id));
        subTaskHashMap.keySet().stream()
                .forEach(id -> inMemoryHistoryManager.remove(id));
        epicHashMap.clear();
        subTaskHashMap.clear();
    }


    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);
        inMemoryHistoryManager.add(task);
        return task;
    }


    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        inMemoryHistoryManager.add(subTask);
        return subTask;
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        inMemoryHistoryManager.add(epic);
        return epic;
    }


    @Override
    public void updateTask(Task updatedTask) {
        if (hasOverlapWithAnyTask(updatedTask)) {
            System.out.println("При изменении задачи произошло пересечение! Задача не изменена");
            return;
        } else {
            taskHashMap.put(updatedTask.getId(), updatedTask);
        }
    }


    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        if (hasOverlapWithAnyTask(updatedSubTask)) {
            System.out.println("При изменении задачи произошло пересечение! Задача не изменена");
            return;
        } else {
            subTaskHashMap.put(updatedSubTask.getId(), updatedSubTask);
            Epic parentalEpic = epicHashMap.get(updatedSubTask.getPartOfEpic());
            setEpicStatus(parentalEpic);
            setEpicTimeAndDuration(parentalEpic);
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        epicHashMap.put(updatedEpic.getId(), updatedEpic);
    }


    @Override
    public void deleteTask(int id) {
        if (taskHashMap.get(id).hasDuration()) {
            sortedTasks.remove(taskHashMap.get(id));
        }
        inMemoryHistoryManager.remove(id);
        taskHashMap.remove(id);
    }


    @Override
    public void deleteSubTask(int id) {
        if (subTaskHashMap.get(id).hasDuration()) {
            sortedTasks.remove(subTaskHashMap.get(id));
        }
        inMemoryHistoryManager.remove(id);
        Epic parentalEpic = epicHashMap.get(subTaskHashMap.get(id).getPartOfEpic());
        subTaskHashMap.remove(id);
        parentalEpic.deleteSubtaskFromEpic(id);
        setEpicStatus(parentalEpic);
        setEpicTimeAndDuration(parentalEpic);
    }


    @Override
    public void deleteEpic(int id) {
        inMemoryHistoryManager.remove(id);
        epicHashMap.get(id).getSubtasksIds().stream()
                .forEach(subTasksId -> {
            subTaskHashMap.remove(subTasksId);
            inMemoryHistoryManager.remove(subTasksId);
        });
        epicHashMap.remove(id);
    }


    private boolean hasOverlapWithAnyTask(Task task) {
        return getPrioritizedTasks().stream()
                .anyMatch(otherTask -> task.hasOverlap(otherTask));
    }


    private void setEpicTimeAndDuration(Epic epic) {

        List<SubTask> epicsFilteredSubtasks = getSubTasksFromEpic(epic.getId()).stream()
                .filter(subTask -> subTask.hasDuration())
                .toList();


        if (epicsFilteredSubtasks.size() > 0) {
            Long epicDurationInMinutes = epicsFilteredSubtasks.stream()
                    .mapToLong(subTask -> subTask.getDuration().toMinutes())
                    .sum();
            epic.setDuration(Duration.ofMinutes(epicDurationInMinutes));
            SubTask startSubtask = epicsFilteredSubtasks.stream()
                    .min(Comparator.comparing(SubTask::getStartTime))
                    .get();
            SubTask endSubtask = epicsFilteredSubtasks.stream()
                    .max(Comparator.comparing(SubTask::getStartTime))
                    .get();
            epic.setStartTime(startSubtask.getStartTime());
            epic.setEndTime(endSubtask.getEndTime());
        }
    }


    private void setEpicStatus(Epic epic) { //Проверяем статус эпика по подзадачам внутри него
        boolean isNew = true;
        boolean isDone = true;
        ArrayList<SubTask> subTasks = new ArrayList<>();
        epic.getSubtasksIds().stream()
                .forEach(subTaskId -> subTasks.add(subTaskHashMap.get(subTaskId)));

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


    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }


    public List<Task> getHistory() {
        return (ArrayList<Task>) inMemoryHistoryManager.getHistory();
    }


}
