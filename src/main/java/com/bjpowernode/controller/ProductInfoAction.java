package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {

    public static final int PAGE_SIZE = 5;

    String saveFileName = "";

    @Autowired
    private ProductInfoService productInfoService;
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo info = null;
        ProductInfoVo vo = (ProductInfoVo) request.getSession().getAttribute("vo");
        if (vo != null){
            info = productInfoService.selectCondition(vo,PAGE_SIZE);
            request.getSession().removeAttribute("vo");
        }else {
            info = productInfoService.splitPage(1,PAGE_SIZE);
        }
        request.setAttribute("info",info);
        return "product";
    }

    @ResponseBody
    @RequestMapping("/ajaxsplit")
    public void ajaxsplit(HttpSession session,ProductInfoVo vo){
        PageInfo info = productInfoService.selectCondition(vo,PAGE_SIZE);
        session.setAttribute("info",info);
    }


    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        PageInfo info = productInfoService.selectCondition(vo,PAGE_SIZE);
        session.setAttribute("info",info);
    }

    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        String path = request.getServletContext().getRealPath("/image_big");
        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject();
        object.put("imgurl",saveFileName);
        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg","增加成功！");
        }else {
            request.setAttribute("msg","增加失败！");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, Model model,ProductInfoVo vo,HttpSession session){
        ProductInfo prod = productInfoService.getById(pid);
        model.addAttribute("prod",prod);
        session.setAttribute("vo",vo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        if (!"".equals(saveFileName)) {
            info.setpImage(saveFileName);
        }
        int num = -1;

        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg","修改成功！");
        }else {
            request.setAttribute("msg","修改失败！");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/delete")
    public String delete(int pid,ProductInfoVo vo,HttpServletRequest request){
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg","删除成功！");
            request.setAttribute("dvo",vo);
        }else {
            request.setAttribute("msg","删除失败！");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request){
        ProductInfoVo vo = (ProductInfoVo) request.getAttribute("dvo");
        PageInfo info = null;
        if (null != vo){
            info = productInfoService.selectCondition(vo,PAGE_SIZE);
        }else {
            info = productInfoService.splitPage(1,PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    @RequestMapping("/deletebatch")
    public String deletebatch(String str,ProductInfoVo vo,HttpServletRequest request){
        String[] ids = str.split(",");
        int num = -1;
        try {
            num = productInfoService.deleteBatch(ids);
            if (num > 0){
                request.setAttribute("msg","删除成功！");
                request.setAttribute("dvo",vo);
            }else {
                request.setAttribute("msg","删除失败！");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除！");
        }
        return "forward:/prod/deleteAjaxSplit.action?pageNum=";
    }
}
