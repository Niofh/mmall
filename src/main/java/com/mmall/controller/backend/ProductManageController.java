package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.fastdfs.client.*;
import com.mmall.pojo.Product;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/11/5 11:07
 */
@Controller
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Value("${file_server_addr}")
    private String fileServerAddr;


    private FastDFSClient fastDFSClient = new FastDFSClient();

    /**
     * 获取商品列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.getProductList(pageNum, pageSize);
    }

    /**
     * 查询商品
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public ServerResponse<PageInfo> getSearchProductList(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.getSearchProductList(productName, productId, pageNum, pageSize);
    }

    /**
     * 添加商品
     * @param productVo
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ServerResponse addProduct(ProductVo productVo) {
        if(productVo.getId()==null){
            return iProductService.addProduct(productVo);
        }
        return iProductService.updateProduct(productVo);
    }


    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<ProductVo> getProductById(Integer productId) {
        return iProductService.getProductById(productId);
    }

    /*订单上架或者下架*/
    @RequestMapping("/set_sale_status")
    @ResponseBody
    public ServerResponse set_sale_status(Integer productId,Integer status) {
        return iProductService.updateProductStatus(productId,status);
    }




    /**
     * 只能上传图片
     *
     * @param upload_file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public ServerResponse upload(@RequestParam("upload_file") MultipartFile upload_file, HttpServletRequest httpServletRequest) {

        // 检查文件类型
        if (!FileCheck.checkImage(upload_file.getOriginalFilename())) {
            FileResponseData responseData = new FileResponseData(false);
            responseData.setCode(ErrorCode.FILE_TYPE_ERROR_IMAGE.CODE);
            responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_IMAGE.MESSAGE);
            return ServerResponse.createByError();
        }


        FileResponseData fileResponseData = this.uploadSample(upload_file, httpServletRequest);

        Map map = new HashMap<String, String>();
        if (!fileResponseData.isSuccess()) {
            return ServerResponse.createByError();
        }

        map.put("uri", fileResponseData.getFilePath());
        map.put("url", fileResponseData.getHttpUrl());
        return ServerResponse.createBySuccess(map);
    }

    /**
     * 富文本上传图片
     *
     * @param upload_file
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/richtext_img_upload")
    @ResponseBody
    public Map richtextImg_Upload(@RequestParam("upload_file") MultipartFile upload_file, HttpServletRequest httpServletRequest) {

        Map map = new HashMap<String, String>();

        // 检查文件类型
        if (!FileCheck.checkImage(upload_file.getOriginalFilename())) {
            FileResponseData responseData = new FileResponseData(false);
            responseData.setCode(ErrorCode.FILE_TYPE_ERROR_IMAGE.CODE);
            responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_IMAGE.MESSAGE);
            map.put("success", false);
            map.put("msg", responseData.getMessage());
            return map;
        }


        FileResponseData fileResponseData = this.uploadSample(upload_file, httpServletRequest);

        if (!fileResponseData.isSuccess()) {
            map.put("success", false);
            map.put("msg", fileResponseData.getMessage());
            return map;
        }

        map.put("file_path", fileResponseData.getHttpUrl());
        map.put("success", true);
        return map;
    }


    /**
     * 上传通用方法，只上传到服务器，不保存记录到数据库
     *
     * @param file
     * @param request
     * @return
     */
    private FileResponseData uploadSample(MultipartFile file, HttpServletRequest request) {

        FileResponseData responseData = new FileResponseData();
        try {
            // 上传到服务器
            String filepath = fastDFSClient.uploadFileWithMultipart(file);
            responseData.setFilePath(filepath);
            responseData.setFileName(file.getOriginalFilename());
            responseData.setFileType(FastDFSClient.getFilenameSuffix(file.getOriginalFilename()));
            responseData.setHttpUrl(fileServerAddr + "/" + filepath);
        } catch (FastDFSException e) {
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }

}


