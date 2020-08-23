package com.leyou.api;

import com.leyou.domain.SpecGroup;
import com.leyou.domain.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/spec")
public interface SpecificationAPI {

    @GetMapping("getSpecParamByList")
    public List<SpecParam> getSpecParamByList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching
    );

    @RequestMapping( value = "getSpecGroup" , method = { RequestMethod.GET } )
    public List<SpecGroup> getSpecGroup(@RequestParam  long cid);

}
