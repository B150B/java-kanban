package API;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;


import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager manager;


    public HistoryHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                sendJson(exchange, 200, manager.getHistory());
            }
        } catch (Exception exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
        }
    }
}
