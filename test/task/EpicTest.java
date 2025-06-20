package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static Epic epic;
    static SubTask subtask1;
    static SubTask subtask2;
    static InMemoryTaskManager inMemoryTaskManager;
    private InMemoryTaskManager statusTestingTaskManager;
    Epic epicForStatusTest;

    @BeforeAll
    public static void beforeAll() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Эпик1", "Описание1");
        inMemoryTaskManager.addEpic(epic);
        subtask1 = new SubTask("Сабтаск1", "Описание1", Status.NEW, 1, Duration.ofMinutes(40), LocalDateTime.now().plus(Duration.ofMinutes(100)));
        inMemoryTaskManager.addSubTask(subtask1);
    }

    @BeforeEach
    public void beforeEach() {
        statusTestingTaskManager = new InMemoryTaskManager();
        epicForStatusTest = new Epic("Эпик для теста статусов", "Эпик для теста статусов");
    }


    @Test
    void testEqualsIfIdIsEqual() {
        boolean expected = epic.equals(epic);
        boolean result = epic.getId() == epic.getId();
        assertEquals(expected, result);
    }


    @Test
    void addSubtaskToEpic() {
        subtask2 = new SubTask("Сабтаск2", "Описание2", Status.NEW, 1, Duration.ofMinutes(40), LocalDateTime.now());
        inMemoryTaskManager.addSubTask(subtask2);
        List<Integer> result = epic.getSubtasksIds();
        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        assertArrayEquals(result.toArray(), expected.toArray());
    }

    @Test
    void deleteSubtaskFromEpic() {
        epic.clearAllSubTasksFromEpic();
        List<Integer> result = epic.getSubtasksIds();
        List<Integer> expected = new ArrayList<>();
        assertArrayEquals(result.toArray(), expected.toArray());
    }

    @Test
    void convertToCSVLineAndBack() {
        List subTasksIds = new ArrayList<>();
        subTasksIds.add(6);
        LocalDateTime testLocalDateTime = LocalDateTime.of(2025, Month.MARCH,21,21,21);
        Epic epic4 = new Epic(5, "Эпик 5", "Описание эпика 5", Status.NEW, subTasksIds, Duration.ofMinutes(40), testLocalDateTime, testLocalDateTime);
        String epicInString = epic4.toCSVLine();
        Epic epic5 = Epic.fromCSVLine(epicInString);
        String result = epic5.toString();
        String expected = epic4.toString();
        assertEquals(result, expected);
    }

    @Test
    void statusMustBeNewWhenAllSubTasksAreNew() {
        statusTestingTaskManager.addEpic(epicForStatusTest);
        SubTask subTaskForStatusTest1 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.NEW, 1);
        SubTask subTaskForStatusTest2 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.NEW, 1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest2);
        assertEquals(statusTestingTaskManager.getEpic(1).getStatus(), Status.NEW);

    }

    @Test
    void statusMustBeDoneWhenAllSubTasksAreDone() {
        statusTestingTaskManager.addEpic(epicForStatusTest);
        SubTask subTaskForStatusTest1 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.DONE, 1);
        SubTask subTaskForStatusTest2 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.DONE, 1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest2);
        assertEquals(statusTestingTaskManager.getEpic(1).getStatus(), Status.DONE);
    }

    @Test
    void statusMustBeInProgressWhenAllSubTasksAreNewAndDone() {
        statusTestingTaskManager.addEpic(epicForStatusTest);
        SubTask subTaskForStatusTest1 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.NEW, 1);
        SubTask subTaskForStatusTest2 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.DONE, 1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest2);
        assertEquals(statusTestingTaskManager.getEpic(1).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void statusMustBeInProgressWhenAllSubTasksAreInProgress() {
        statusTestingTaskManager.addEpic(epicForStatusTest);
        SubTask subTaskForStatusTest1 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.IN_PROGRESS, 1);
        SubTask subTaskForStatusTest2 = new SubTask("Сабтаск для теста статусов", "Сабтаск для теста статусов", Status.IN_PROGRESS, 1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest1);
        statusTestingTaskManager.addSubTask(subTaskForStatusTest2);
        assertEquals(statusTestingTaskManager.getEpic(1).getStatus(), Status.IN_PROGRESS);
    }


}