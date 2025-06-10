package manager;


import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {


    @Test
    void savingAndRestoringFromFileOneTask() throws IOException {
        Path path = Files.createTempFile("temp", ".csv");
        String taskData = "1,TASK,Таск1,NEW,Описание таск1";
        Files.writeString(path, taskData);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.toFile());
        String result = fileBackedTaskManager.getTask(1).toCSVLine();
        assertEquals(taskData, result);
    }


    @Test
    void savingAndRestoringFromFile3Tasks() throws IOException {
        Path path = Files.createTempFile("temp", ".csv");
        String taskData1 = "1,TASK,Таск1,NEW,Описание таск1";
        String taskData2 = "2,TASK,Таск2,NEW,Описание таск2";
        String taskData3 = "3,TASK,Таск3,NEW,Описание таск3";
        List<String> taskDataArray = new ArrayList<>();
        taskDataArray.add(taskData1);
        taskDataArray.add(taskData2);
        taskDataArray.add(taskData3);
        Files.write(path, taskDataArray);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path.toFile());
        String stringFromRestoredManager1 = fileBackedTaskManager.getTask(1).toCSVLine();
        String stringFromRestoredManager2 = fileBackedTaskManager.getTask(2).toCSVLine();
        String stringFromRestoredManager3 = fileBackedTaskManager.getTask(3).toCSVLine();
        List<String> result = new ArrayList<>();
        result.add(stringFromRestoredManager1);
        result.add(stringFromRestoredManager2);
        result.add(stringFromRestoredManager3);
        assertArrayEquals(taskDataArray.toArray(), result.toArray());
    }

    @Test
    void creatingFilesWhenAddingTask() throws IOException {
        Path path = Paths.get("src", "temp.csv");
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(path.toFile());
        Task task1 = new Task("Таск1", "Описание таск1");
        fileBackedTaskManager1.addTask(task1);
        assertTrue(Files.exists(path));
        Files.delete(path);
    }


}