package com.chartmultiplexaxes.chartmultiplexaxes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<?> getData(){
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }
}
