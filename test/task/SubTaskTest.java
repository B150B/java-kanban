package task;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    private SubTask subTask1;
    private SubTask subTask2;
    private TaskManager inMemoryTaskManager;



    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описание Эпика");
        inMemoryTaskManager.addEpic(epic);
        subTask1 = new SubTask("Сабтаск1", "Описание1", Status.NEW, 1, Duration.ofMinutes(40), LocalDateTime.now());
        subTask2 = new SubTask("Сабтаск2", "Описание2", Status.NEW, 1, Duration.ofMinutes(40), LocalDateTime.now());
    }

    @Test
    void testEqualsIfIdIsEqual() {
        boolean expected = subTask1.equals(subTask1);
        boolean result = subTask1.getId() == subTask1.getId();
        assertEquals(expected, result);
    }


    @Test
    void convertToCSVLineAndBack() {
        SubTask subTask4 = new SubTask(5, "Сабтаск 4", "Описание сабтаска 4", Status.NEW, 4, Duration.ofMinutes(60), LocalDateTime.now());
        String subTaskInCSVLine = subTask4.toCSVLine();
        SubTask subTask5 = SubTask.fromCSVLine(subTaskInCSVLine);
        String result = subTask5.toString();
        String expected = subTask4.toString();
        assertEquals(result, expected);
    }
}