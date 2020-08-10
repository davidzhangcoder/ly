package com.leyou.search.repository;

import com.leyou.domain.Brand;
import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long>
{
}
