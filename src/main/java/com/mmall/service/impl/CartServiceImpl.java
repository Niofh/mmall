package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.CartExample;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.service.ICartService;
import com.mmall.utli.BigDecimalUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Value("${file_server_addr}")
    private String imgHost;

    @Override
    public ServerResponse<Integer> getCartCountByUserId(Integer userId) {

        int userCartTotal = cartMapper.getUserCartTotal(userId);
        return ServerResponse.createBySuccess(userCartTotal);
    }

    @Override
    public ServerResponse<CartVo> getCartList(Integer userId) {

        CartVo cartVo = this.getList(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    @Override
    public ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        List<Cart> cartList = cartMapper.selectByExample(cartExample);


        if (cartList.size() == 0) {
            // 表示当前用户没有添加过这个产品, 把他加入到购物车
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setChecked(Const.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            Cart cart = cartList.get(0);
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }


        return this.getCartList(userId);  // 最后维护正确库存数据返回前台
    }


    @Override
    public ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count) {

        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        List<Cart> cartList = cartMapper.selectByExample(cartExample);

        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("该用户没有这个商品");
        }


        // 更新产品数量
        Cart cart = cartList.get(0);
        cart.setQuantity(count);
        int i = cartMapper.updateByPrimaryKeySelective(cart);
        if (i == 0) {
            return ServerResponse.createByErrorMessage("更新产品失败");
        }

        return this.getCartList(userId);
    }


    @Override
    public ServerResponse<CartVo> delCart(Integer userId, String productIds) {
        List<String> productIdList = Arrays.asList(productIds.split(","));
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        // sql 删除多个
        int i = cartMapper.delCartByuserIdandProIds(userId, productIdList);

        if (i == 0) {
            return ServerResponse.createByErrorMessage("删除失败");
        }

        return this.getCartList(userId);
    }




    @Override
    public ServerResponse<CartVo> selectCheckorunCheck(Integer userId,Integer productId,Integer checked){
        // 如果productId 是null, 代表这个用户购物车商品全部全选或者反选
        int i = cartMapper.updateCheckorUncheck(userId, productId, checked);

        if(i==0){
            return ServerResponse.createByErrorMessage("更新失败");
        }

        return this.getCartList(userId);
    }


    // 这个公用方法用来判断库存是否够，还有计算总价格等
    private CartVo getList(Integer userId) {

        // 动态修改购物车数量 和 总价格 不会影响商品库存

        List<CartProductVo> cartProductVos = new ArrayList<>();
        List<Cart> carts = cartMapper.selectCartsByUserId(userId); // 购物车列表
        BigDecimal totalDecimal = new BigDecimal("0"); // 购物车总价格

        if (CollectionUtils.isNotEmpty(carts)) {

            for (Cart cart : carts) {

                CartProductVo cartProductVo = new CartProductVo();

                cartProductVo.setId(cart.getId());

                cartProductVo.setUserId(cart.getUserId());

                cartProductVo.setProductId(cart.getProductId());
                cartProductVo.setProductChecked(cart.getChecked());

                ProductWithBLOBs product = productMapper.selectByPrimaryKey(cart.getProductId());

                if (product != null) {

                    // 产品赋值
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());


                    // 判断库存
                    Integer totalStock = product.getStock(); // 一个商品总库存

                    Integer cartNumStock = cart.getQuantity(); // 购物车里有多少件一样商品

                    int buyLimitCount = 0;

                    if (totalStock >= cartNumStock) {
                        // 库存充足 购物车的数量就是选中数量
                        buyLimitCount = cartNumStock;
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);

                    } else {
                        // 库存不充足， 产品剩下的库存就是能购买最大数量
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);

                        //购物车中更新有效库存 （能购买的最大数量）
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cart.getId());
                        cartForQuantity.setQuantity(buyLimitCount);

                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);

                    }

                    cartProductVo.setQuantity(buyLimitCount); // 设置可以能购买的数量

                    // 单个商品总价格（价格*数量）
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                }
                cartProductVos.add(cartProductVo);

                // 如果商品是勾选的，就计算总价
                if (cart.getChecked() == Const.Cart.CHECKED) {
                    //  总价格 单个商品总价格（价格*数量）相加
                    totalDecimal = BigDecimalUtil.add(totalDecimal.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());

                }
            }


        }

        CartVo cartVo = new CartVo();
        cartVo.setImageHost(imgHost+"/");
        cartVo.setCartProductVoList(cartProductVos);
        cartVo.setCartTotalPrice(totalDecimal);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));

        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andCheckedEqualTo(Const.Cart.UN_CHECKED);
        return cartMapper.countByExample(cartExample) == 0;

    }


}
