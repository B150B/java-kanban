package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    static SubTask subTask1;
    static SubTask subTask2;
    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    public static void beforeAll() {
        inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание Эпика");
        inMemoryTaskManager.addEpic(epic);
        subTask1 = new SubTask("Сабтаск1", "Описание1", 1, Status.NEW);
        subTask2 = new SubTask("Сабтаск2", "Описание2", 1, Status.NEW);


    }

    @Test
    void testEqualsIfIdIsEqual() {
        boolean expected = subTask1.equals(subTask1);
        boolean result = subTask1.getId() == subTask1.getId();
        assertEquals(expected, result);
    }


    @Test
    void convertToCSVLineAndBack() {
        SubTask subTask4 = new SubTask(5, "Сабтаск 4", "Описание сабтаска 4", Status.NEW, 4);
        String subTaskInCSVLine = subTask4.toCSVLine();
        SubTask subTask5 = SubTask.fromCSVLine(subTaskInCSVLine);
        String result = subTask5.toString();
        String expected = subTask4.toString();
        assertEquals(result, expected);
    }
}