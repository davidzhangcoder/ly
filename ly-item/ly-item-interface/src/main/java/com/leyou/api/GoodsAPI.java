package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/goods")
public interface GoodsAPI {

    @GetMapping("getSKUBySPUId")
    public List<Sku> getSKUBySPUId(@RequestParam( name = "spuid" , required = true ) long spuid);


    @GetMapping("getSPUDetailBySPUId")
    public SpuDetail getSPUDetailBySPUId(@RequestParam( name = "spuid" , required = true ) long spuid ) ;

    @GetMapping("getSpuByPage")
    public PageResult<Spu> getSpuByPage(
            @RequestParam( name = "descending" , required = true ) boolean descending,
            @RequestParam( name = "page" , required = true ) int page,
            @RequestParam( name = "rowsPerPage" , required = true ) int rowsPerPage,
            @RequestParam( name = "sortBy" , required = true ) String sortBy
    );

    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable(value="id") Long id);

}
