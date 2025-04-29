package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static Epic epic;
    static SubTask subtask1;
    static SubTask subtask2;
    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    public static void beforeAll() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Эпик1","Описание1");
        inMemoryTaskManager.addEpic(epic);
        subtask1 = new SubTask("Сабтаск1","Описание1",1, Status.NEW);
        inMemoryTaskManager.addSubTask(subtask1);
    }


    @Test
    void testEqualsIfIdIsEqual() {
        boolean expected = epic.equals(epic);
        boolean result = epic.getId() == epic.getId();
        assertEquals(expected,result);
    }




    @org.junit.jupiter.api.Test
    void addSubtaskToEpic() {
        subtask2 = new SubTask("Сабтаск2","Описание2",1, Status.NEW);
        inMemoryTaskManager.addSubTask(subtask2);
        ArrayList<Integer> result = epic.getSubtasksIds();
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        assertArrayEquals(result.toArray(),expected.toArray());
    }

    @org.junit.jupiter.api.Test
    void deleteSubtaskFromEpic() {
        epic.clearAllSubTasksFromEpic();
        ArrayList<Integer> result = epic.getSubtasksIds();
        ArrayList<Integer> expected = new ArrayList<>();
        assertArrayEquals(result.toArray(),expected.toArray());
    }




}