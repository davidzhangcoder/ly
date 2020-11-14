package com.leyou.service;

import com.leyou.common.dto.CartDto;
import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;

import java.util.List;

public interface GoodsService {

    public List<Sku> getSKUBySPUId(long spuid);

    public SpuDetail getSPUDetailBySPUId(long spuid);

    public PageResult getSpuByPage(boolean descending, int page, int rowsPerPage, String sortBy);

    public Spu querySpuById(Long id);

    public Spu testPersistSpu( long id , String title);

    public List<Sku> getSKUListByIds(List<Long> skuIds);

    public void decreaseStock(List<CartDto> cartDtos);

    public void testFallBack(long id);

    public long getStockBySkuId(long skuID);

    public List<Sku> getOnSaleProduct(List<Long> skuIds);
}
