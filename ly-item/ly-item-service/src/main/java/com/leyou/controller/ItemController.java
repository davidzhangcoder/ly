package com.leyou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/item")
@RestController
public class ItemController {

    @GetMapping("getItemList")
    public String getItemList() {
        return "getItemList";
    }
}
