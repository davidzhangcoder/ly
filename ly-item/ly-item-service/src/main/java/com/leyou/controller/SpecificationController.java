package com.leyou.controller;

import com.leyou.domain.Brand;
import com.leyou.domain.SpecGroup;
import com.leyou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/spec")
@RestController
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @RequestMapping( method = { RequestMethod.POST } )
    public ResponseEntity<List<SpecGroup>> persistSpecGroup(@RequestBody List<SpecGroup> specifications) {
        return null;
//        return ResponseEntity.ok( specificationService.persistSpecGroup( specGroupList ) );
    }

    @RequestMapping( value = "getSpecGroup" , method = { RequestMethod.GET } )
    public ResponseEntity<List<SpecGroup>> getSpecGroup(@RequestParam  long cid) {
        return ResponseEntity.ok( specificationService.getSpecGroup(cid) );
    }

}


















