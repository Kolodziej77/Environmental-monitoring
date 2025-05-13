package com.environmental.controller;

import com.environmental.model.Measurement;
import com.environmental.service.MeasurementService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @GetMapping("/index")
    public String index(Model model){
        List<Measurement> measurements = service.findAllMeasurements(Sort.Direction.DESC);
        model.addAttribute("measurements", measurements);
        return "index";
    }

    @GetMapping("/charts")
    public String charts(){
        return "charts";
    }

    @GetMapping("/api/chart-data")
    @ResponseBody
    public List<Measurement> getChartData(){
        return service.findAllMeasurements(Sort.Direction.ASC);
    }

    @PostMapping("/measure")
    public String trigger(){
        service.triggerMeasurement();
        return "redirect:/index";
    }

    @PostMapping("api/data")
    public ResponseEntity<Void> receiveData(@RequestBody Measurement measurement){
        service.saveMeasurement(measurement);
        return ResponseEntity.ok().build();
    }
}
