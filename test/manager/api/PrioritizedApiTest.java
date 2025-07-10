package manager.api;

import manager.HttpTaskServerTest;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedApiTest extends HttpTaskServerTest {


    @Test
    public void testPrioritized() throws IOException, InterruptedException {
        Task task6 = new Task("Таск с ид 6", "Описание таска с ид 6", Status.NEW
                , Duration.ofMinutes(100), LocalDateTime.now().plus(Duration.ofMinutes(150)));
        manager.addTask(task6);
        HttpResponse<String> response = sendRequest("http://localhost:8080/prioritized", "GET");
        List<Task> result = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        List<Task> expected = List.of(task2, task6);
        assertEquals(200, response.statusCode());
        assertArrayEquals(result.toArray(), expected.toArray());
    }


}
