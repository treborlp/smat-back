package com.inaigem.smatbackend.data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    @GetMapping("data")
    public List<Data> getListData(){
        return List.of(new Data("Meteorologicos", "url1"), new Data("Glaciologicos", "url2"));
    }
}
