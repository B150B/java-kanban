package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static Epic epic;
    static SubTask subtask1;
    static SubTask subtask2;
    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    public static void beforeAll() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Эпик1", "Описание1");
        inMemoryTaskManager.addEpic(epic);
        subtask1 = new SubTask("Сабтаск1", "Описание1", 1, Status.NEW);
        inMemoryTaskManager.addSubTask(subtask1);
    }


    @Test
    void testEqualsIfIdIsEqual() {
        boolean expected = epic.equals(epic);
        boolean result = epic.getId() == epic.getId();
        assertEquals(expected, result);
    }


    @org.junit.jupiter.api.Test
    void addSubtaskToEpic() {
        subtask2 = new SubTask("Сабтаск2", "Описание2", 1, Status.NEW);
        inMemoryTaskManager.addSubTask(subtask2);
        List<Integer> result = epic.getSubtasksIds();
        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        assertArrayEquals(result.toArray(), expected.toArray());
    }

    @org.junit.jupiter.api.Test
    void deleteSubtaskFromEpic() {
        epic.clearAllSubTasksFromEpic();
        List<Integer> result = epic.getSubtasksIds();
        List<Integer> expected = new ArrayList<>();
        assertArrayEquals(result.toArray(), expected.toArray());
    }

    @org.junit.jupiter.api.Test
    void convertToCSVLineAndBack () {
        List subTasksIds = new ArrayList<>();
        subTasksIds.add(9);
        subTasksIds.add(10);
        Epic epic4 = new Epic(5,"Эпик 5","Описание эпика 5",Status.NEW , subTasksIds );
        String epicInString = epic4.toCSVLine();
        Epic epic5 = Epic.fromCSVLine(epicInString);
        String result = epic5.toString();
        String expected = epic4.toString();
        assertEquals(result,expected);
    }


}