package com.leyou.service;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.SpuDetail;

import java.util.List;

public interface GoodsService {

    public List<Sku> getSKUBySPUId(long spuid);

    public SpuDetail getSPUDetailBySPUId(long spuid);

    public PageResult getSpuByPage(boolean descending, int page, int rowsPerPage, String sortBy);
}
