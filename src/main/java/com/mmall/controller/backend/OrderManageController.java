package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {


    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> getOrderListNoUserIdManager(Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return  iOrderService.getOrderListNoUserIdManager(pageNum,pageSize);
    }

    @RequestMapping("/search")
    @ResponseBody
    public ServerResponse<PageInfo> searchOrderManager(Long orderNo,Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10")  Integer pageSize){
        return  iOrderService.searchOrderManager(orderNo,pageNum,pageSize);
    }

    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<OrderVo> getOrderDetailManager(Long orderNo){
        return  iOrderService.getOrderDetailManager(orderNo);
    }

    @RequestMapping("/send_goods")
    @ResponseBody
    public ServerResponse orderSendManager(Long orderNo){
        return iOrderService.orderSendManager(orderNo);
    }
}
