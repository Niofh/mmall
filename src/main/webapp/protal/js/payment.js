webpackJsonp([10],{0:function(e,t,r){e.exports=r(71)},1:function(e,t,r){"use strict";var n=r(4),o={serverHost:""},s={request:function(e){var t=this;$.ajax({type:e.method||"get",url:e.url||"",dataType:e.type||"json",data:e.data||"",success:function(r){0===r.status?"function"==typeof e.success&&e.success(r.data,r.msg):10===r.status?t.doLogin():1===r.status&&"function"==typeof e.error&&e.error(r.msg)},error:function(t){"function"==typeof e.error&&e.error(t.statusText)}})},getServerUrl:function(e){return o.serverHost+e},getUrlParam:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)"),r=window.location.search.substr(1).match(t);return r?decodeURIComponent(r[2]):null},renderHtml:function(e,t){var r=n.compile(e),o=r.render(t);return o},successTips:function(e){alert(e||"操作成功！")},errorTips:function(e){alert(e||"哪里不对了~")},validate:function(e,t){var e=$.trim(e);return"require"===t?!!e:"phone"===t?/^1\d{10}$/.test(e):"email"===t?/^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/.test(e):void 0},doLogin:function(){window.location.href="./user-login.html?redirect="+encodeURIComponent(window.location.href)},goHome:function(){window.location.href="./index.html"}};e.exports=s},2:function(e,t,r){"use strict";var n=r(4),o={serverHost:""},s={request:function(e){var t=this;$.ajax({type:e.method||"get",url:e.url||"",dataType:e.type||"json",data:e.data||"",success:function(r){0===r.status?"function"==typeof e.success&&e.success(r.data,r.msg):10===r.status?t.doLogin():1===r.status&&"function"==typeof e.error&&e.error(r.msg)},error:function(t){"function"==typeof e.error&&e.error(t.statusText)}})},getServerUrl:function(e){return o.serverHost+e},getUrlParam:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)"),r=window.location.search.substr(1).match(t);return r?decodeURIComponent(r[2]):null},renderHtml:function(e,t){var r=n.compile(e),o=r.render(t);return o},successTips:function(e){alert(e||"操作成功")},errorTips:function(e){alert(e||"哪里不对了～～")},validate:function(e,t){var e=$.trim(e);return"require"===t?!!e:"phone"===t?/^1\d{10}$/.test(e):"email"===t?/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(e):void 0},doLogin:function(){window.location.href="./user-login.html?redirect="+encodeURIComponent(window.location.href)},goHome:function(){window.location.href="./index.html"}};e.exports=s},3:function(e,t,r){"use strict";var n=r(2),o={register:function(e,t,r){n.request({url:n.getServerUrl("/user/register"),data:e,method:"POST",success:t,error:r})},login:function(e,t,r){n.request({url:n.getServerUrl("/user/login"),data:e,method:"POST",success:t,error:r})},logout:function(e,t){n.request({url:n.getServerUrl("/user/logout"),method:"POST",success:e,error:t})},checkUsername:function(e,t,r){n.request({url:n.getServerUrl("/user/check_valid"),data:{type:"username",str:e},method:"POST",success:t,error:r})},checkLogin:function(e,t){n.request({url:n.getServerUrl("/user/get_user_info"),method:"POST",success:e,error:t})},getQuesion:function(e,t,r){n.request({url:n.getServerUrl("/user/forget_get_question"),data:{username:e},method:"POST",success:t,error:r})},checkQuestionAnswer:function(e,t,r){n.request({url:n.getServerUrl("/user/forget_check_answer"),data:e,method:"POST",success:t,error:r})},getUserInfo:function(e,t){n.request({url:n.getServerUrl("/user/get_information"),method:"POST",success:e,error:t})},updateUserInfo:function(e,t,r){n.request({url:n.getServerUrl("/user/update_information"),data:e,method:"POST",success:t,error:r})},updatePasswordInfo:function(e,t,r){n.request({url:n.getServerUrl("/user/reset_password"),data:e,method:"POST",success:t,error:r})}};e.exports=o},5:function(e,t,r){"use strict";var n=r(2),o={getCartCount:function(e,t){n.request({url:n.getServerUrl("/cart/get_cart_product_count"),method:"POST",success:e,error:t})},addToCart:function(e,t,r){n.request({url:n.getServerUrl("/cart/add"),data:e,success:t,error:r})},getCartList:function(e,t){n.request({url:n.getServerUrl("/cart/list"),method:"POST",success:e,error:t})},selectProduct:function(e,t,r){n.request({url:n.getServerUrl("/cart/select"),data:{productId:e},success:t,error:r})},unselectProduct:function(e,t,r){n.request({url:n.getServerUrl("/cart/un_select"),data:{productId:e},success:t,error:r})},selectAllProduct:function(e,t){n.request({url:n.getServerUrl("/cart/select_all"),success:e,error:t})},unselectAllProduct:function(e,t){n.request({url:n.getServerUrl("/cart/un_select_all"),success:e,error:t})},updateProduct:function(e,t,r){n.request({url:n.getServerUrl("/cart/update"),data:e,success:t,error:r})},deleteProduct:function(e,t,r){n.request({url:n.getServerUrl("/cart/delete_product"),data:{productIds:e},success:t,error:r})}};e.exports=o},6:function(e,t){},7:function(e,t){},8:function(e,t,r){"use strict";r(6);var n=r(1),o={init:function(){this.onLoad(),this.bindEvent()},onLoad:function(){var e=n.getUrlParam("keyword");e&&$("#search-input").val(e)},bindEvent:function(){var e=this;$("#search-btn").click(function(){e.searchSubmit()}),$("#search-input").keyup(function(t){13===t.keyCode&&e.searchSubmit()})},searchSubmit:function(){var e=$.trim($("#search-input").val());e?window.location.href="./list.html?keyword="+e:n.goHome()}};o.init()},9:function(e,t,r){"use strict";r(7);var n=r(1),o=r(3),s=r(5),u={init:function(){return this.bindEvent(),this.loadUserInfo(),this.loadCartCount(),this},bindEvent:function(){$(".js-login").click(function(){n.doLogin()}),$(".js-register").click(function(){window.location.href="./user-register.html"}),$(".js-logout").click(function(){o.logout(function(e){window.location.reload()},function(e){n.errorTips(e)})})},loadUserInfo:function(){o.checkLogin(function(e){$(".user.not-login").hide().siblings(".user.login").show().find(".username").text(e.username)},function(e){})},loadCartCount:function(){s.getCartCount(function(e){$(".nav .cart-count").text(e||0)},function(e){$(".nav .cart-count").text(0)})}};e.exports=u.init()},32:function(e,t){},53:function(e,t){e.exports='<p class="payment-tips">订单提交成功，请尽快支付!订单号:{{orderNo}}</p> <p class="payment-tips enhance">请使用支付宝app扫描如下二维码进行支付:</p> <div class="img-con"> <img class="qr-code" src="{{qrUrl}}" alt="支付二维码"> </div>'},71:function(e,t,r){"use strict";r(32),r(9),r(8);var n=r(2),o=r(80),s=r(53),u={data:{orderNo:n.getUrlParam("orderNo")},init:function(){this.onLoad()},onLoad:function(){this.loadPaymentInfo()},loadPaymentInfo:function(){var e=this,t=$(".page-wrap");t.html('<div class="loading"></div>'),o.getPaymentInfo(this.data.orderNo,function(r){var o=n.renderHtml(s,r);t.html(o),e.listenOrderStatus()},function(e){t.html('<p class="err-tip">'+e+"</p>")})},listenOrderStatus:function(){var e=this;this.paymentTimer=window.setInterval(function(){o.getPaymentStatus(e.data.orderNo,function(t){t===!0&&(window.location.href="./result.html?type=payment&orderNo="+e.data.orderNo)},function(e){})},5e3)}};$(function(){u.init()})},80:function(e,t,r){"use strict";var n=r(2),o={getPaymentInfo:function(e,t,r){n.request({url:n.getServerUrl("/order/pay"),data:{orderNo:e},success:t,error:r})},getPaymentStatus:function(e,t,r){n.request({url:n.getServerUrl("/order/query_order_pay_status"),data:{orderNo:e},success:t,error:r})}};e.exports=o}});