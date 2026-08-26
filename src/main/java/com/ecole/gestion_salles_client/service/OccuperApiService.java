package com.ecole.gestion_salles_client.service;


import com.ecole.gestion_salles_client.model.Occuper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class OccuperApiService {

    private static final String BASE_URL = "http://localhost:8080/api/occuper";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<Occuper> getAll() throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), new TypeReference<List<Occuper>>() {});
    }

    public void create(String codeProf, String codeSal, LocalDate date) throws Exception {
        String url = BASE_URL + "?codeProf=" + codeProf + "&codeSal=" + codeSal + "&date=" + date;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    public void delete(String codeProf, String codeSal, LocalDate date) throws Exception {
        String url = BASE_URL + "/" + codeProf + "/" + codeSal + "/" + date;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
    }
}
