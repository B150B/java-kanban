package API;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;

import task.SubTask;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager manager;


    public SubTaskHandler(InMemoryTaskManager manager) {
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
                sendJson(exchange, 200, manager.getSubTask(getIdFromRequest(path)));
            } catch (Exception exception) {
                sendNotFound(exchange);
            }
        } else {
            sendJson(exchange, 200, manager.getAllSubTasks());
        }
    }

    private void postHandler(HttpExchange exchange, String path) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        if (requestHasId(path)) {
            manager.updateSubTask(subTask);
            sendText(exchange, "Подзадача успешно обновлена", 201);
        } else {
            if (manager.addSubTask(subTask)) {
                sendText(exchange, "Подзадача успешно добавлена", 201);
            } else {
                sendHasInteractions(exchange);
            }
        }
    }

    private void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int id = getIdFromRequest(path);
        try {
            manager.deleteSubTask(id);
            sendText(exchange, "Задача успешно удалена", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }


}
