package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    private void save() {

        try {
            if (!Files.exists(file.toPath())) {
                Files.createFile(file.toPath());
            }

            List<String> taskCSVLinesArray = new ArrayList<>();
            for (Task task : taskHashMap.values()) {
                taskCSVLinesArray.add(task.toCSVLine());
            }
            for (Epic epic : epicHashMap.values()) {
                taskCSVLinesArray.add(epic.toCSVLine());
            }
            for (SubTask subTask : subTaskHashMap.values()) {
                taskCSVLinesArray.add(subTask.toCSVLine());
            }
            Files.write(file.toPath(), taskCSVLinesArray);
        } catch (Exception exception) {
            System.out.println("Произошла ошибка записи в файл! " + exception.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager restoredManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String[] taskCSVArray = bufferedReader.readLine().split("\\R");
                for (String taskCSV : taskCSVArray) {
                    String[] taskData = taskCSV.split(",");
                    TaskType taskType = TaskType.valueOf(taskData[1]);
                    switch (taskType) {
                        case TaskType.TASK:
                            Task task = Task.fromCSVLine(taskCSV);
                            restoredManager.putTaskWithID(task);
                            break;
                        case TaskType.SUBTASK:
                            SubTask subTask = SubTask.fromCSVLine(taskCSV);
                            restoredManager.putSubtaskWithID(subTask);
                            break;
                        case TaskType.EPIC:
                            Epic epic = Epic.fromCSVLine(taskCSV);
                            restoredManager.putEpicWithID(epic);
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("Ошибка загрузки файла! " + exception.getMessage());
        }

        return restoredManager;
    }


    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    public void putTaskWithID(Task task) {
        taskHashMap.put(task.getId(), task);
        id = Math.max(task.getId() + 1, id);
    }

    public void putEpicWithID(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
        id = Math.max(epic.getId() + 1, id);
    }

    public void putSubtaskWithID(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        id = Math.max(subTask.getId() + 1, id);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }


    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        super.updateSubTask(updatedSubTask);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

}
