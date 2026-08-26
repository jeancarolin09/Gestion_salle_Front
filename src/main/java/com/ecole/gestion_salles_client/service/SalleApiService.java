package com.ecole.gestion_salles_client.service;

import com.ecole.gestion_salles_client.model.Salle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SalleApiService {

    private static final String BASE_URL = "http://localhost:8080/api/salles";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Salle> getAll() throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), new TypeReference<List<Salle>>() {});
    }

    public Salle getByCode(String code) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL + "/" + code)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() == 404) {
            return null;
        }
        return mapper.readValue(res.body(), Salle.class);
    }

    public void create(Salle s) throws Exception {
        String json = mapper.writeValueAsString(s);
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    public void update(String code, Salle s) throws Exception {
        String json = mapper.writeValueAsString(s);
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL + "/" + code))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    public void delete(String code) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL + "/" + code))
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }
}
