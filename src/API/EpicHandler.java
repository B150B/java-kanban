package API;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import task.Epic;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager manager;


    public EpicHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (method) {
                case "GET":
                    getHandler(exchange, path);
                    break;
                case "POST":
                    postHandler(exchange, path);
                    break;
                case "DELETE":
                    deleteHandler(exchange, path);
                    break;
            }

        } catch (Exception exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
        }
    }


    private void getHandler(HttpExchange exchange, String path) throws IOException {
        if (requestHasId(path)) {
            try {
                sendJson(exchange, 200, manager.getEpic(getIdFromRequest(path)));
            } catch (Exception exception) {
                sendNotFound(exchange);
            }
        } else if (path.endsWith("subtasks")) {
            try {
                sendJson(exchange, 200, manager.getSubTasksFromEpic(getIdFromRequest(path)));
            } catch (Exception exception) {
                sendNotFound(exchange);
            }
        } else {
            sendJson(exchange, 200, manager.getAllEpic());
        }
    }

    private void postHandler(HttpExchange exchange, String path) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        manager.addEpic(epic);
        sendText(exchange, "Эпик успешно добавлена", 201);

    }

    private void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int id = getIdFromRequest(path);
        try {
            manager.deleteEpic(id);
            sendText(exchange, "Эпик успешно удален", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

}
