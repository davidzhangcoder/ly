package com.leyou.search.service;

import com.leyou.domain.Spu;
import com.leyou.search.pojo.Goods;

import java.io.IOException;

public interface SearchService {

    public Goods buildGoods(Spu spu) throws IOException;

}
