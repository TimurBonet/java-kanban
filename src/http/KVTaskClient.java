package http;

import manager.ManagerSaveException;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;  // "http://localhost:" + port + "/";
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
    }

    private String register(String url){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200){
                throw new ManagerSaveException("Невозможно зарегистрировать, код ошибки: " + response.statusCode());
            }
            return response.body();
        } catch ( InterruptedException | IOException e){
            e.printStackTrace();
            throw  new ManagerSaveException("Ошибка");
        }
    }

    public String load (String key){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200){
                throw new ManagerSaveException("Невозможно зарегистрировать, код ошибки: " + response.statusCode());
            }
            return response.body();
        } catch ( InterruptedException | IOException e){
            e.printStackTrace();
            throw  new ManagerSaveException("Ошибка");
        }
    }
    public void put (String key, String value){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<Void> response = client.send(request,HttpResponse.BodyHandlers.discarding());
            if(response.statusCode() != 200){
                throw new ManagerSaveException("Невозможно зарегистрировать, код ошибки: " + response.statusCode());
            }
        } catch ( InterruptedException | IOException e){
            e.printStackTrace();
            throw  new ManagerSaveException("Ошибка");
        }
    }
}
