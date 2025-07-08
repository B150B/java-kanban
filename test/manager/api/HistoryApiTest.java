package manager.api;


import manager.HttpTaskServerTest;
import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryApiTest extends HttpTaskServerTest {


    @Test
    public void testHistory() throws IOException, InterruptedException {
        HttpResponse<String> response1 = sendRequest("http://localhost:8080/tasks/2", "GET");
        HttpResponse<String> response2 = sendRequest("http://localhost:8080/tasks/3", "GET");
        HttpResponse<String> response3 = sendRequest("http://localhost:8080/history", "GET");

        List<Task> resultHistory = gson.fromJson(response3.body(), new TaskListTypeToken().getType());
        List<Task> expected = List.of(task1, task2);
        assertEquals(200, response1.statusCode());
        assertArrayEquals(resultHistory.toArray(), expected.toArray());
    }


}
