package API;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private final static int PORT = 8080;
    private HttpServer httpServer;
    private final InMemoryTaskManager manager;


    public HttpTaskServer(InMemoryTaskManager inMemoryTaskManager) {
        this.manager = inMemoryTaskManager;
    }

    public Gson getGson() {
        return new BaseHttpHandler().getGson();
    }


    public static void main(String[] args) throws IOException {

    }

    public void startServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/subtasks", new SubTaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.start();
        System.out.println("Сервер запущен");
    }

    public void stopServer() {
        if (httpServer != null) {
            httpServer.stop(0);
            System.out.println("Сервер остановлен");
        }
    }


}
