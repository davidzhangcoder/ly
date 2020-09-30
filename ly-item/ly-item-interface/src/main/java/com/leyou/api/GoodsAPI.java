package com.leyou.api;

import com.leyou.common.dto.CartDto;
import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/goods")
public interface GoodsAPI {

    @GetMapping("getSKUBySPUId")
    public List<Sku> getSKUBySPUId(@RequestParam( name = "spuid" , required = true ) long spuid);

    @GetMapping("getSKUListByIds")
    public List<Sku> getSKUListByIds(@RequestParam( name = "ids" , required = true ) List<Long> skuIds);

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

    @PostMapping("stock/decrease")
    public void decreaseStock(@RequestBody List<CartDto> cartDtos);

    @GetMapping(value="getStockBySkuId/{skuID}")
    public long getStockBySkuId(@PathVariable(value="skuID") long skuID);

    @GetMapping(value="testFallBack/{id}")
    public void testFallBack(@PathVariable(value="id") long id);

}
