/*
* @Author: Rosen
* @Date:   2017-05-19 21:52:46
* @Last Modified by:   Rosen
* @Last Modified time: 2017-05-19 23:01:25
*/

'use strict';
require('./index.css');
require('page/common/nav-simple/index.js');
var _mm = require('util/mm.js');
var _util = require('util/util.js');

$(function(){
    var type        = _mm.getUrlParam('type') || 'default',
        $element    = $('.' + type + '-success');
    // 显示对应的提示元素
    $element.show();

    $("#look-order").attr("href",'./order-detail.html?orderNo='+_util.getUrlParam("orderNo"))
})