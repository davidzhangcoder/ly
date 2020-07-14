package com.leyou.service;

import com.leyou.domain.Brand;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandService {

    public List<Brand> searchBrand(String key, boolean descending, int page, int rowsPerPage, String sortBy);

}
