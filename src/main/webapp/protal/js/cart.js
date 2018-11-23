webpackJsonp([12],{0:function(t,e,r){t.exports=r(62)},1:function(t,e,r){"use strict";var c=r(9),n={serverHost:""},s={request:function(t){var e=this;$.ajax({type:t.method||"get",url:t.url||"",dataType:t.type||"json",data:t.data||"",success:function(r){0===r.status?"function"==typeof t.success&&t.success(r.data,r.msg):10===r.status?e.doLogin():1===r.status&&"function"==typeof t.error&&t.error(r.msg)},error:function(e){"function"==typeof t.error&&t.error(e.statusText)}})},getServerUrl:function(t){return n.serverHost+t},getUrlParam:function(t){var e=new RegExp("(^|&)"+t+"=([^&]*)(&|$)"),r=window.location.search.substr(1).match(e);return r?decodeURIComponent(r[2]):null},renderHtml:function(t,e){var r=c.compile(t),n=r.render(e);return n},successTips:function(t){alert(t||"操作成功！")},errorTips:function(t){alert(t||"哪里不对了~")},validate:function(t,e){var t=$.trim(t);return"require"===e?!!t:"phone"===e?/^1\d{10}$/.test(t):"email"===e?/^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/.test(t):void 0},doLogin:function(){window.location.href="./user-login.html?redirect="+encodeURIComponent(window.location.href)},goHome:function(){window.location.href="./index.html"}};t.exports=s},3:function(t,e,r){"use strict";var c=r(2),n={register:function(t,e,r){c.request({url:c.getServerUrl("/user/register"),data:t,method:"POST",success:e,error:r})},login:function(t,e,r){c.request({url:c.getServerUrl("/user/login"),data:t,method:"POST",success:e,error:r})},logout:function(t,e){c.request({url:c.getServerUrl("/user/logout"),method:"POST",success:t,error:e})},checkUsername:function(t,e,r){c.request({url:c.getServerUrl("/user/check_valid"),data:{type:"username",str:t},method:"POST",success:e,error:r})},checkLogin:function(t,e){c.request({url:c.getServerUrl("/user/get_user_info"),method:"POST",success:t,error:e})},getQuesion:function(t,e,r){c.request({url:c.getServerUrl("/user/forget_get_question"),data:{username:t},method:"POST",success:e,error:r})},checkQuestionAnswer:function(t,e,r){c.request({url:c.getServerUrl("/user/forget_check_answer"),data:t,method:"POST",success:e,error:r})},getUserInfo:function(t,e){c.request({url:c.getServerUrl("/user/get_information"),method:"POST",success:t,error:e})},updateUserInfo:function(t,e,r){c.request({url:c.getServerUrl("/user/update_information"),data:t,method:"POST",success:e,error:r})},updatePasswordInfo:function(t,e,r){c.request({url:c.getServerUrl("/user/reset_password"),data:t,method:"POST",success:e,error:r})}};t.exports=n},4:function(t,e,r){"use strict";var c=r(2),n={getCartCount:function(t,e){c.request({url:c.getServerUrl("/cart/get_cart_product_count"),method:"POST",success:t,error:e})},addToCart:function(t,e,r){c.request({url:c.getServerUrl("/cart/add"),data:t,success:e,error:r})},getCartList:function(t,e){c.request({url:c.getServerUrl("/cart/list"),method:"POST",success:t,error:e})},selectProduct:function(t,e,r){c.request({url:c.getServerUrl("/cart/select"),data:{productId:t},success:e,error:r})},unselectProduct:function(t,e,r){c.request({url:c.getServerUrl("/cart/un_select"),data:{productId:t},success:e,error:r})},selectAllProduct:function(t,e){c.request({url:c.getServerUrl("/cart/select_all"),success:t,error:e})},unselectAllProduct:function(t,e){c.request({url:c.getServerUrl("/cart/un_select_all"),success:t,error:e})},updateProduct:function(t,e,r){c.request({url:c.getServerUrl("/cart/update"),data:t,success:e,error:r})},deleteProduct:function(t,e,r){c.request({url:c.getServerUrl("/cart/delete_product"),data:{productIds:t},success:e,error:r})}};t.exports=n},5:function(t,e){},6:function(t,e){},7:function(t,e,r){"use strict";r(5);var c=r(1),n={init:function(){this.onLoad(),this.bindEvent()},onLoad:function(){var t=c.getUrlParam("keyword");t&&$("#search-input").val(t)},bindEvent:function(){var t=this;$("#search-btn").click(function(){t.searchSubmit()}),$("#search-input").keyup(function(e){13===e.keyCode&&t.searchSubmit()})},searchSubmit:function(){var t=$.trim($("#search-input").val());t?window.location.href="./list.html?keyword="+t:c.goHome()}};n.init()},8:function(t,e,r){"use strict";r(6);var c=r(1),n=r(3),s=r(4),o={init:function(){return this.bindEvent(),this.loadUserInfo(),this.loadCartCount(),this},bindEvent:function(){$(".js-login").click(function(){c.doLogin()}),$(".js-register").click(function(){window.location.href="./user-register.html"}),$(".js-logout").click(function(){n.logout(function(t){window.location.reload()},function(t){c.errorTips(t)})})},loadUserInfo:function(){n.checkLogin(function(t){t&&$(".user.not-login").hide().siblings(".user.login").show().find(".username").text(t.username)},function(t){})},loadCartCount:function(){s.getCartCount(function(t){$(".nav .cart-count").text(t||0)},function(t){$(".nav .cart-count").text(0)})}};t.exports=o.init()},23:function(t,e){},44:function(t,e){t.exports='{{#notEmpty}} <div class="cart-header"> <table class="cart-table"> <tr> <th class="cart-cell cell-check"> <label class="cart-label"> {{#allChecked}} <input type="checkbox" class="cart-select-all" checked="checked"/> {{/allChecked}} {{^allChecked}} <input type="checkbox" class="cart-select-all"/> {{/allChecked}} <span>全选</span> </label> </th> <th class="cart-cell cell-info">商品信息</th> <th class="cart-cell cell-price">单价</th> <th class="cart-cell cell-count">数量</th> <th class="cart-cell cell-total">合计</th> <th class="cart-cell cell-opera">操作</th> </tr> </table> </div> <div class="cart-list"> {{#cartProductVoList}} <table class="cart-table" data-product-id="{{productId}}"> <tr> <td class="cart-cell cell-check"> <label class="cart-label"> {{#productChecked}} <input type="checkbox" class="cart-select" checked="checked"/> {{/productChecked}} {{^productChecked}} <input type="checkbox" class="cart-select"/> {{/productChecked}} </label> </td> <td class="cart-cell cell-img"> <a class="link" href="./detail.html?productId={{productId}}"> <img class="p-img" src="{{imageHost}}{{productMainImage}}" alt="{{productName}}"/> </a> </td> <td class="cart-cell cell-info"> <a class="link" href="./detail.html?productId={{productId}}">{{productName}}</a> </td> <td class="cart-cell cell-price">￥{{productPrice}}</td> <td class="cart-cell cell-count"> <span class="count-btn minus">-</span> <input class="count-input" value="{{quantity}}" data-max="{{productStock}}"/> <span class="count-btn plus">+</span> </td> <td class="cart-cell cell-total">￥{{productTotalPrice}}</td> <td class="cart-cell cell-opera"> <span class="link cart-delete">删除</span> </td> </tr> </table> {{/cartProductVoList}} </div> <div class="cart-footer"> <div class="select-con"> <label> {{#allChecked}} <input type="checkbox" class="cart-select-all" checked="checked"/> {{/allChecked}} {{^allChecked}} <input type="checkbox" class="cart-select-all"/> {{/allChecked}} <span>全选</span> </label> </div> <div class="delete-con"> <span class="link delete-selected"> <i class="fa fa-trash-o"></i> <span>删除选中</span> </span> </div> <div class="submit-con"> <span>总价：</span> <span class="submit-total">￥{{cartTotalPrice}}</span> <span class="btn btn-submit">去结算</span> </div> </div> {{/notEmpty}} {{^notEmpty}} <p class="err-tip"> <span>您的购物车空空如也，</span> <a href="./index.html">立即去购物</a> </p> {{/notEmpty}}'},62:function(t,e,r){"use strict";r(23),r(7);var c=r(8),n=r(1),s=r(4),o=r(44),a={data:{},init:function(){this.onLoad(),this.bindEvent()},onLoad:function(){this.loadCart()},bindEvent:function(){var t=this;$(document).on("click",".cart-select",function(){var e=$(this),r=e.parents(".cart-table").data("product-id");e.is(":checked")?s.selectProduct(r,function(e){t.renderCart(e)},function(e){t.showCartError()}):s.unselectProduct(r,function(e){t.renderCart(e)},function(e){t.showCartError()})}),$(document).on("click",".cart-select-all",function(){var e=$(this);e.is(":checked")?s.selectAllProduct(function(e){t.renderCart(e)},function(e){t.showCartError()}):s.unselectAllProduct(function(e){t.renderCart(e)},function(e){t.showCartError()})}),$(document).on("click",".count-btn",function(){var e=$(this),r=e.siblings(".count-input"),c=parseInt(r.val()),o=e.hasClass("plus")?"plus":"minus",a=e.parents(".cart-table").data("product-id"),l=1,u=parseInt(r.data("max")),i=0;if("plus"===o){if(c>=u)return void n.errorTips("该商品数量已达到上限");i=c+1}else if("minus"===o){if(c<=l)return;i=c-1}s.updateProduct({productId:a,count:i},function(e){t.renderCart(e)},function(e){t.showCartError()})}),$(document).on("click",".cart-delete",function(){if(window.confirm("确认要删除该商品？")){var e=$(this).parents(".cart-table").data("product-id");t.deleteCartProduct(e)}}),$(document).on("click",".delete-selected",function(){if(window.confirm("确认要删除选中的商品？")){for(var e=[],r=$(".cart-select:checked"),c=0,s=r.length;c<s;c++)e.push($(r[c]).parents(".cart-table").data("product-id"));e.length?t.deleteCartProduct(e.join(",")):n.errorTips("您还没有选中要删除的商品")}}),$(document).on("click",".btn-submit",function(){t.data.cartInfo&&t.data.cartInfo.cartTotalPrice>0?window.location.href="./order-confirm.html":n.errorTips("请选择商品后再提交")})},loadCart:function(){var t=this;s.getCartList(function(e){t.renderCart(e)},function(e){t.showCartError()})},renderCart:function(t){this.filter(t),this.data.cartInfo=t;var e=n.renderHtml(o,t);$(".page-wrap").html(e),c.loadCartCount()},deleteCartProduct:function(t){var e=this;s.deleteProduct(t,function(t){e.renderCart(t)},function(t){e.showCartError()})},filter:function(t){t.notEmpty=!!t.cartProductVoList.length},showCartError:function(){$(".page-wrap").html('<p class="err-tip">哪里不对了，刷新下试试吧。</p>')}};$(function(){a.init()})}});