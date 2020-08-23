package com.leyou.controller;

import com.leyou.domain.Brand;
import com.leyou.domain.SpecGroup;
import com.leyou.domain.SpecParam;
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

    /**
     *  Persistent Specification Group
     * @param specifications
     * @return
     */
    @RequestMapping( method = { RequestMethod.POST } )
    public ResponseEntity<List<SpecGroup>> persistSpecGroup(@RequestBody List<SpecGroup> specifications) {
        return null;
//        return ResponseEntity.ok( specificationService.persistSpecGroup( specGroupList ) );
    }

    /**
     * get List of Specification Group by the third level Category ID
     * @param cid
     * @return
     */
    @RequestMapping( value = "getSpecGroup" , method = { RequestMethod.GET } )
    public ResponseEntity<List<SpecGroup>> getSpecGroup(@RequestParam  long cid) {
        return ResponseEntity.ok( specificationService.getSpecGroup(cid) );
    }

    @GetMapping("getSpecParamByList")
    public ResponseEntity<List<SpecParam>> getSpecParamByList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching
    ) {
        return ResponseEntity.ok(specificationService.queryParamByList(gid,cid,searching));
    }


}


















