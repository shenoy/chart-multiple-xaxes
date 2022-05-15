package com.chartmultiplexaxes.chartmultiplexaxes.controller;
import com.chartmultiplexaxes.chartmultiplexaxes.service.ChartDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {
    @Autowired
    ChartDataService service;

    @GetMapping({"","/"})
    public String index(Model model){
        model.addAttribute("msg", "This is thymeleaf model attribute");
        model.addAttribute("data", service.getSampleData() );
        return "index";
    }

}
