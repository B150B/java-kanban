package api;


import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import task.Epic;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseCrudHandler<Epic> {

    public EpicHandler(InMemoryTaskManager manager) {
        super(manager);
    }


    @Override
    protected void getHandler(HttpExchange exchange, String path) throws IOException {
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

    @Override
    protected void postHandler(HttpExchange exchange, String path) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        manager.addEpic(epic);
        sendText(exchange, "Эпик успешно добавлена", 201);

    }

    @Override
    protected void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int id = getIdFromRequest(path);
        try {
            manager.deleteEpic(id);
            sendText(exchange, "Эпик успешно удален", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }

}
