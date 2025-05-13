package com.environmental.service;

import com.environmental.model.Measurement;
import com.environmental.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    @Value("${esp32.url}")
    private String esp32Url;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public List<Measurement> findAllMeasurements(Sort.Direction direction){
        return repository.findAll(Sort.by(direction, "timestamp"));
    }

    public List<Measurement> findAllForCharts(){
        return repository.findAll(Sort.by(Sort.Direction.ASC, "timestamp"));
    }

    public void saveMeasurement(Measurement measurement){
        repository.save(measurement);
    }

    public void triggerMeasurement(){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(esp32Url))
                    .GET()
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
