package com.leyou.controller;

import com.leyou.seata.TestSeataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/item")
@RestController
public class ItemController {

    @Autowired
    private TestSeataService testSeataService;

    @GetMapping("getItemList")
    public String getItemList() {
        return "getItemList";
    }

    @GetMapping("testSeata")
    public void testSeata() {
        testSeataService.updateAccount1();
    }
}
