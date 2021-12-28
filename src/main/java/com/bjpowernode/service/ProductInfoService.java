package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    List<ProductInfo> getAll();

    PageInfo splitPage(int pageNum,int pageSize);

    int save(ProductInfo info);

    ProductInfo getById(int pid);

    int update(ProductInfo info);

    int delete(int pid);

    int deleteBatch(String[] ids);

    PageInfo selectCondition(ProductInfoVo vo,int pageSize);
}
