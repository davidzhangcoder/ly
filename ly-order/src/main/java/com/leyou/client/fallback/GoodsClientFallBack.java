package com.leyou.client.fallback;

import com.leyou.client.GoodsClient;
import com.leyou.common.dto.CartDto;
import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsClientFallBack /*implements GoodsClient*/ {
//    @Override
    public List<Sku> getSKUBySPUId(long spuid) {
        return null;
    }

//    @Override
    public List<Sku> getSKUListByIds(List<Long> skuIds) {
        return null;
    }

//    @Override
    public SpuDetail getSPUDetailBySPUId(long spuid) {
        return null;
    }

//    @Override
    public PageResult<Spu> getSpuByPage(boolean descending, int page, int rowsPerPage, String sortBy) {
        return null;
    }

//    @Override
    public Spu querySpuById(Long id) {
        return null;
    }

//    @Override
    public void decreaseStock(List<CartDto> cartDtos) {

    }

//    @Override
    public long getStockBySkuId(long skuID) {
        return 0;
    }

//    @Override
    public void testFallBack(long id) {

    }
}
