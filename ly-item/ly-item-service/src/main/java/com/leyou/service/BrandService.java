package com.leyou.service;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Brand;

import java.util.List;

public interface BrandService {

    public PageResult<Brand> searchBrand(String key, boolean descending, int page, int rowsPerPage, String sortBy);

    public Brand persistBrand(Brand brand);

    public void deleteBrand(long brandId);

    public Brand getBrandById(long brandId);

    public List<Brand> getBrandsByIds(List<Long> ids);
}
