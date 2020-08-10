package com.leyou.search.service;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.vo.SearchRequest;
import com.leyou.search.vo.SearchResult;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    public Goods buildGoods(Spu spu) throws IOException;

    public SearchResult<Goods> searchGoods(SearchRequest searchRequest);
}
