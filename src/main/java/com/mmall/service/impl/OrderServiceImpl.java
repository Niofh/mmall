package com.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.*;
import com.mmall.fastdfs.client.FastDFSClient;
import com.mmall.fastdfs.client.FastDFSException;
import com.mmall.pojo.*;
import com.mmall.service.IOrderService;
import com.mmall.utli.BigDecimalUtil;
import com.mmall.vo.OrderItemVo;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Value("${file_server_addr}")
    private String imageHost;

    @Value("${alipay.callback.url}")
    private String aliPayCallbackUrl;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Override
    public ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId) {

        List<Cart> cartList = cartMapper.selectCheckCartProduct(userId);

        ServerResponse serverResponse = this.getOrderItem(userId, cartList);

        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        OrderProductVo orderProductVo = new OrderProductVo();
        BigDecimal total = new BigDecimal("0");

        for (OrderItem orderItem : orderItemList) {
            total = BigDecimalUtil.add(orderItem.getTotalPrice().doubleValue(), total.doubleValue());
            OrderItemVo orderItemVo = this.assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }

        orderProductVo.setProductTotalPrice(total);
        orderProductVo.setImageHost(imageHost + "/");
        orderProductVo.setOrderItemVoList(orderItemVoList);

        return ServerResponse.createBySuccess(orderProductVo);
    }


    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        List<Cart> cartList = cartMapper.selectCheckCartProduct(userId);

        ServerResponse serverResponse = this.getOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        Order order = this.assembleOrder(userId, shippingId, orderItemList);
        if (order == null) {
            return ServerResponse.createByErrorMessage("生成订单失败");
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo()); // 订单里的商品插入订单编号
            orderItem.setUpdateTime(null);
        }
        // 批量插入订单描述插入数据库
        orderItemMapper.insertOrderItemList(orderItemList);


        // 减少库存
        for (OrderItem orderItem : orderItemList) {
            ProductWithBLOBs product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            Integer stock = product.getStock();
            Integer quantity = orderItem.getQuantity();
            product.setStock(stock - quantity);
            product.setUpdateTime(null);
            // 更新一下库存
            productMapper.updateByPrimaryKeySelective(product);
        }

        // 清空用户购物车已勾选商品
        cartMapper.deleteByUserIdAndChecked(userId);

        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);
    }


    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Order> orderList = orderMapper.selectByExample(orderExample); // 获取所有订单

        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, userId);

        PageInfo pageInfo = new PageInfo(orderVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemListByOrderNo(orderNo, userId);
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse cancelOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createByErrorMessage("该订单已付款，不能取消订单");
        }
        Order order1 = new Order();
        order1.setId(order.getId());
        order1.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
//        order1.setCloseTime(new Date());
        int i = orderMapper.updateByPrimaryKeySelective(order1);
        if (i > 0) {
            return ServerResponse.createBySuccess("订单取消成功");
        }

        return ServerResponse.createByError("订单取消失败");
    }

    @Override
    public ServerResponse pay(Integer userId, Long orderNo, String path) {

        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);

        if (order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }

        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemListByOrderNo(orderNo, userId);

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderNo.toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "mmall商店当面付扫码消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("购买商品").append(orderItemList.size())
                .append("件共").append(order.getPayment()).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        for (OrderItem orderItem : orderItemList) {


            BigDecimal price = BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), 100);

            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(String.valueOf(orderItem.getId()), orderItem.getProductName(), price.longValue(), orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(aliPayCallbackUrl)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File file = new File(path);
                if (!file.exists()) {
                    file.setWritable(true);
                    file.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String filePath = String.format(path + "/qr-%s.png", response.getOutTradeNo());

                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                // 上传本地图片到fastdfs服务器
                FastDFSClient fastDFSClient = new FastDFSClient();
                String qrPath = null;
                try {
                     qrPath = fastDFSClient.uploadFileWithFilepath(filePath,null); // 二维码地址
                } catch (FastDFSException e) {
                    e.printStackTrace();
                    return ServerResponse.createByError("上传二维码图片失败");
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("orderNo",orderNo);
                hashMap.put("qrUrl",imageHost+"/"+qrPath);
                return ServerResponse.createBySuccess(hashMap);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }


    @Override
    public ServerResponse aliCallback(Map<String, String> params) {

        Long out_trade_no = Long.valueOf(params.get("out_trade_no")); // 订单编号
        String total_amount = params.get("total_amount"); // 交易金额


        String tradeNo = params.get("trade_no");  //支付宝交易凭证号
        String tradeStatus = params.get("trade_status"); // 交易状态


        Order order = orderMapper.selectByOrderNo(out_trade_no);
        if(order==null){
            return ServerResponse.createByErrorMessage("非快乐慕商城的订单,回调忽略");
        }
        BigDecimal payment = order.getPayment();

        if(!(total_amount.equals(payment.toString()))){
            return ServerResponse.createByErrorMessage("这次订单的交易总金额不一致");
        }

        if(order.getStatus()>=Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createByErrorMessage("支付宝重复调用");
        }

        if(tradeStatus.equals(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS)){
            // 支付成功
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date gmt_payment = null;
            try {
                gmt_payment = sdf.parse(params.get("gmt_payment"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Order order1 = new Order();

            order1.setPaymentTime(gmt_payment); // 支付时间
            order1.setStatus(Const.OrderStatusEnum.PAID.getCode());
            order1.setId(order.getId());
            // 更新订单状态

            orderMapper.updateByPrimaryKeySelective(order1);

        }


        // 支付管理表
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insertSelective(payInfo);

        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse<Boolean> queryOrderPayStatus(Integer id, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, id);
        if(order==null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createByError(false);
    }


    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    public ServerResponse<PageInfo> getOrderListNoUserIdManager(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderExample orderExample = new OrderExample();
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        PageInfo pageInfo = new PageInfo<>(orderList);

        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);
        pageInfo.setList(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchOrderManager(Long orderNo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);
        PageInfo<OrderVo> pageInfo = new PageInfo<>(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetailManager(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {
            return ServerResponse.createByErrorMessage("没有找到订单");
        }

        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemListByOrderNo(orderNo, null);

        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse orderSendManager(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {
            return ServerResponse.createByErrorMessage("没有找到订单");
        }

        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
        updateOrder.setSendTime(new Date());

        int i = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (i > 0) {
            return  ServerResponse.createBySuccess("发货成功");
        }

        return ServerResponse.createByError("发货失败");
    }

    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {

            List<OrderItem> orderItemList = new ArrayList<>();

            if (userId == null) {
                //todo 管理员查询的时候 不需要传userId
                orderItemList = orderItemMapper.selectOrderItemListByOrderNo(order.getOrderNo(), null);
            } else {
                //todo 防止用户越权查询
                orderItemList = orderItemMapper.selectOrderItemListByOrderNo(order.getOrderNo(), userId);
            }
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo); // 将数据进行过滤返回出去
        }
        return orderVoList;
    }

    /**
     * 组装返回数据
     *
     * @param order
     * @return
     */
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {

        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShipping(assembleShipping(order.getShippingId()));
        }

        orderVo.setPaymentTime(order.getPaymentTime());
        orderVo.setSendTime(order.getSendTime());
        orderVo.setEndTime(order.getEndTime());
        orderVo.setCreateTime(order.getCreateTime());
        orderVo.setCloseTime(order.getCloseTime());

        orderVo.setImageHost(imageHost + "/");

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private Shipping assembleShipping(Integer shippingId) {

        return shippingMapper.selectByPrimaryKey(shippingId);

    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(orderItem.getCreateTime());
        return orderItemVo;
    }


    /**
     * 生成订单
     *
     * @param userId
     * @param shippingId
     * @param orderItemList
     * @return
     */
    private Order assembleOrder(Integer userId, Integer shippingId, List<OrderItem> orderItemList) {
        Order order = new Order();

        order.setOrderNo(this.createOrderNo());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPayment(this.getOrderItemTotalPayment(orderItemList));
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPostage(0);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());

        //发货时间等等
        //付款时间等等
        // 订单插入一条数据
        int i = orderMapper.insertSelective(order);
        if (i > 0) {
            return order;
        }
        return null;
    }


    /**
     * 获取生成订单之前商品总预付价格
     */
    private BigDecimal getOrderItemTotalPayment(List<OrderItem> orderItemList) {
        BigDecimal totalPayment = new BigDecimal("0"); //todo 小心单引号和双引号，作用是不一样的
        for (OrderItem orderItem : orderItemList) {
            totalPayment = BigDecimalUtil.add(totalPayment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }

        return totalPayment;
    }


    /**
     * 创建订单编号(这是没有高并发的时候编号)
     *
     * @return
     */
    private Long createOrderNo() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis + new Random().nextInt(100);
    }


    /**
     * 获取封装好的订单明细， 已经判断了是否有库存、是否购物车存在商品、商品是否在售
     *
     * @param userId
     * @return
     */
    private ServerResponse getOrderItem(Integer userId, List<Cart> cartList) {


        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车是空的！");
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            // 判断商品状态
            ProductWithBLOBs product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }

            if (product.getStock() < cart.getQuantity()) {
                return ServerResponse.createByErrorMessage("商品" + product.getName() + "库存不足");
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));

            orderItemList.add(orderItem);
        }

        return ServerResponse.createBySuccess(orderItemList);

    }
}
