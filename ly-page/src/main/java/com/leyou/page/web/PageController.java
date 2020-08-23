package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping( value = "{id}.html" )
    public String toItemPage(Model model , @PathVariable("id") long id ) {
        Map<String, Object> modelMap = pageService.loadModel(id);
        model.addAllAttributes( modelMap );
        return "item";
    }

}
