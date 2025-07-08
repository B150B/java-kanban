package manager.api;


import com.google.gson.reflect.TypeToken;
import manager.HttpTaskServerTest;
import org.junit.jupiter.api.*;
import task.Epic;
import task.Status;
import task.SubTask;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicListTypeToken extends TypeToken<List<Epic>> {

}

@DisplayName("Тест EpicApi")
public class EpicApiTest extends HttpTaskServerTest {

    @Nested
    @DisplayName("GET-запросы")
    class GetTests {
        @Test
        public void testGetEpic() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/epics/1", "GET");
            Epic result = gson.fromJson(response.body(), Epic.class);
            assertEquals(manager.getEpic(1), result);
            assertEquals(200, response.statusCode());
        }


        @Test
        public void testGetAllEpics() throws IOException, InterruptedException {
            Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
            manager.addEpic(epic2);
            HttpResponse<String> response = sendRequest("http://localhost:8080/epics", "GET");
            List<Epic> result = gson.fromJson(response.body(), new EpicListTypeToken().getType());
            List<Epic> expected = manager.getAllEpic();
            assertArrayEquals(result.toArray(), expected.toArray());
            assertEquals(200, response.statusCode());
        }


        @Test
        public void testGetNonExistentEpic() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/epics/222", "GET");
            assertEquals(404, response.statusCode());
        }


        @Test
        public void testGetSubtaskIdsFromEpic() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/epics/1/subtasks", "GET");
            List<SubTask> result = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
            List<SubTask> expected = List.of(subTask1, subTask2);
            assertArrayEquals(result.toArray(), expected.toArray());
            assertEquals(200, response.statusCode());
        }

        @Test
        public void testGetSubtaskIdsFromNonExistentEpic() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/epics/222/subtasks", "GET");
            assertEquals(404, response.statusCode());
        }
    }


    @Test
    @DisplayName("Post-запрос")
    public void testPostCreateEpic() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        manager.addEpic(epic2);
        SubTask subTask3 = new SubTask("СабТаск3", "Описание сабтаска3", Status.NEW, 6);
        manager.addSubTask(subTask3);
        String epic2Json = gson.toJson(epic2);
        HttpResponse<String> response = sendRequest("http://localhost:8080/epics", "POST", epic2Json);
        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = manager.getAllEpic();
        assertEquals(3, epicsFromManager.size());
        assertEquals(epic2.getName(), epicsFromManager.get(1).getName());
    }


    @Test
    @DisplayName("Delete-запрос")
    public void testDeleteEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest("http://localhost:8080/epics/1", "DELETE");
        assertEquals(200, response.statusCode());
        List<Epic> result = manager.getAllEpic();
        assertEquals(result.size(), 0);
    }

}
