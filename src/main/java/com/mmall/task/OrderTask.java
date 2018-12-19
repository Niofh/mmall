package com.mmall.task;

import com.mmall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 订单的定时任务
 */
@Component
@Slf4j
public class OrderTask {

    @Value("task.order.close.time")
    private String closeTime;


    @Autowired
    private IOrderService iOrderService;

    /**
     * 定时关闭订单逻辑 （第一版本逻辑）缺点 tomcat集群时候全部tomcat都调用了定时任务，浪费性能
     * 1. 根据时间（1小时）查询未支付的订单，把状态改已关闭
     * 2. 查询订单上所有的商品上数量，逐一把它的数量更新到库存当中
     * 3. 因为设计订单可能比较多，用到悲观锁中的行锁来保证数据的一致性
     */

    @Scheduled(cron = "0 0/1 * * * ?") // 每一分钟执行一次
    public void closeOrder(){
        log.info("Scheduled========================");
        iOrderService.closeOrder(Integer.valueOf(closeTime));
    }


}
