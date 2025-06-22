package task;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task1;
    private Task task2;
    private TaskManager inMemoryTaskManager;


    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
        task1 = new Task("Таск1", "Описание1");
        task2 = new Task("Таск2", "Описание2");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
    }

    @Test
    void testEqualsIfIdIsEqual() {
        boolean expected = task1.equals(task1);
        boolean result = task1.getId() == task1.getId();
        assertEquals(expected, result);
    }

    @Test
    void convertToCSVLineAndBack() {
        Task task4 = new Task(5, "Таск 4", "Описание таска 4", Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        String TaskInCSVLine = task4.toCSVLine();
        Task task = Task.fromCSVLine(TaskInCSVLine);
        String result = task.toString();
        String expected = task4.toString();
        assertEquals(result, expected);
    }


}