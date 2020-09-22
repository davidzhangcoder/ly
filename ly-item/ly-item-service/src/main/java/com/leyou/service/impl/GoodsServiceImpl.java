package com.leyou.service.impl;

import com.leyou.common.vo.PageResult;
import com.leyou.dao.SkuDao;
import com.leyou.dao.SpuDao;
import com.leyou.dao.SpuDetailDao;
import com.leyou.dao.StockDao;
import com.leyou.domain.*;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service( value = "GoodsServiceImpl" )
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private SpuDetailDao spuDetailDao;

    @Autowired
    private SpuDao spuDao;

    @Autowired
    private StockDao stockDao;

    @Override
    public List<Sku> getSKUBySPUId(long spuid) {
        return skuDao.findSkuBySpuId(spuid);
    }

    @Override
    public SpuDetail getSPUDetailBySPUId(long spuid) {
        return spuDetailDao.findBySpuId(spuid);
    }

    @Override
    public PageResult getSpuByPage(boolean descending, int page, int rowsPerPage, String sortBy) {
        page--;
        page = page < 0 ? 0 : page;//page 为页码，数据库从0页开始
        //可以使用重载的 of(int page, int size, Sort sort) 方法指定排序字段
        //Pageable pageable = PageRequest.of((int)page, (int)rowsPerPage);

        // Order定义了排序规则。参数一是根据倒叙还是正序，参数二是根据那个字段进行排序。
        Sort.Order order = new Sort.Order((descending?Sort.Direction.DESC:Sort.Direction.ASC), sortBy);
        // Sort对象封装了排序的规则。
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page, rowsPerPage , sort );

        Page<Spu> spuPage = spuDao.findAll( pageable );

        PageResult<Brand> pageResult = new PageResult(spuPage.getTotalElements(), (long)spuPage.getTotalPages(), spuPage.getContent());

        return pageResult;
    }

    @Override
    public Spu querySpuById(Long id) {

        return spuDao.findById( id ).orElse( null );

    }

    @Override
    public Spu testPersistSpu(long id, String title) {
        Spu spu = spuDao.getOne(id);
        spu.setTitle( spu.getTitle() + " / 测试" + title);

        spu = spuDao.save( spu );

        //todo: using AOP to send message here?

        return spu;
    }

    @Override
    public List<Sku> getSKUListByIds(List<Long> skuIds) {
        List<Sku> skusWithStockBySkuIDs = skuDao.getSkusWithStockBySkuIDs(skuIds);

        for (Sku sku : skusWithStockBySkuIDs) {
            Stock stock = stockDao.findById(sku.getId()).orElse(null);
            if(stock != null) {
                sku.setStock(stock.getStock());
            }
        }


        return skusWithStockBySkuIDs;
    }

}
