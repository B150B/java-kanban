package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public Gson getGson() {
        return gson;
    }

    protected void sendText(HttpExchange httpExchange, String text, int responseCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);

        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(responseCode, response.length);

        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response);
        }
    }

    protected void sendJson(HttpExchange httpExchange, int code, Object object) throws IOException {
        String json = gson.toJson(object);
        byte[] response = json.getBytes(StandardCharsets.UTF_8);

        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(code, response.length);

        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response);
        }
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        byte[] response = "Ошибка, указанный объект не найден".getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(404, response.length);
        httpExchange.getResponseBody().write(response);
        httpExchange.close();
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        byte[] response = "Ошибка, задача пересекается с другой".getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(406, response.length);
        httpExchange.getResponseBody().write(response);
        httpExchange.close();
    }

    protected boolean requestHasId(String path) {
        return Character.isDigit(path.charAt(path.length() - 1)) || Character.isDigit(path.charAt(path.length() - 2));
    }

    protected int getIdFromRequest(String path) {
        return Integer.parseInt(path.replaceAll("\\D+", ""));
    }


    private static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            if (duration == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(duration.toString());
            }
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.parse(jsonReader.nextString());
        }
    }

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(localDateTime.toString());
            }

        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString());
        }


    }

}


