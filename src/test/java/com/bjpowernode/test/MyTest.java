package com.bjpowernode.test;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-dao.xml","classpath:applicationContext-service.xml"})
public class MyTest {

    @Resource
    ProductInfoMapper productInfoMapper;

    @Test
    public void testSelectCondition(){
        ProductInfoVo productInfoVo = new ProductInfoVo();
        productInfoVo.setpName("4");
        productInfoVo.setTypeid(1);
        productInfoVo.setLprice(1000);
        productInfoVo.setHprice(2000);
        System.out.println(productInfoVo);
        List<ProductInfo> list = productInfoMapper.selectCondition(productInfoVo);
        System.out.println(list.size());
        list.forEach(p -> System.out.println(p));
    }

}
