package manager;

import api.HttpTaskServer;
import com.google.gson.Gson;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;


public abstract class HttpTaskServerTest {
    protected InMemoryTaskManager manager = new InMemoryTaskManager();
    protected HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    protected Gson gson = httpTaskServer.getGson();
    protected Epic epic;
    protected Task task1;
    protected Task task2;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected HttpClient httpClient;
    protected HttpResponse.BodyHandler<String> handler;


    @BeforeEach
    public void beforeEach() throws IOException {
        httpTaskServer.startServer();
        epic = new Epic("Эпик", "Описание эпика");
        task1 = new Task("Таск1", "Описание таск1");
        task2 = new Task("Таск2", "Описание таск2", Status.NEW, Duration.ofMinutes(50), LocalDateTime.now());
        subTask1 = new SubTask("Сабтаск1", "Описание сабтаск1", Status.NEW, 1);
        subTask2 = new SubTask("Сабтаск2", "Описание сабтаск2", Status.IN_PROGRESS, 1);
        manager.addEpic(epic); //id=1
        manager.addTask(task1); // id=2
        manager.addTask(task2); //id=3
        manager.addSubTask(subTask1); //id=4
        manager.addSubTask(subTask2); //id=5
        handler = HttpResponse.BodyHandlers.ofString();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    public void afterEach() {
        manager.clearAllTasks();
        manager.clearAllSubTasks();
        manager.clearAllEpics();
        httpTaskServer.stopServer();
    }

    public HttpResponse<String> sendRequest(String uri, String method) throws IOException, InterruptedException {
        URI url = URI.create(uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method.toUpperCase(), HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.send(request, handler);
    }

    public HttpResponse<String> sendRequest(String uri, String method, String body) throws IOException, InterruptedException {
        URI url = URI.create(uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method.toUpperCase(), HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, handler);
    }


}
