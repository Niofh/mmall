
### idea java配置

1. mybatis 插件下载 free Mybatis plugin

2. 去掉xml底色问题 https://blog.csdn.net/qq_31156277/article/details/77802767 




### 慕课网mmall从零开发 V1.0

资料 http://learning.happymmall.com/

1. 创建并且切换分支来开发

    git checkout -b v1.0 origin/master

2. 推送到窗口
   
    git push origin HEAD -u
    
3. mysql5.7以上问题

    * [解决：ONLY_FULL_GROUP_BY](https://blog.csdn.net/qq_34707744/article/details/78031413)
    * [解决：my.ini配置](https://www.jb51.net/article/127627.htm)
    * [mysql5.7详细教程](https://www.cnblogs.com/renjianjun/p/9016286.html)
    
    
### 本章节学到软件
1. 谷歌插件 画图工具  gliffy  https://blog.csdn.net/u010402584/article/details/81207430
2. 谷歌插件 简单版的http请求工具 Restlet Client
3. idea mybatis free  mybatis快速指向工具
4. idea gosnformat josn格式化
5. 支付宝 rsa加密工具
6. 手机连接到客户端 vysor pro
7. 支付宝沙箱环境下载 


### 支付宝当面付对接 案例是二维码支付
1. 当面付文档 https://docs.open.alipay.com/194/103296/
2. 异步回调的文档 https://docs.open.alipay.com/194/103296/ 仅支持扫二维码
3. 支付完异步回调的地址处理，需要验签 https://docs.open.alipay.com/200/106120#s1 
4. 沙箱环境配置测试，需要配置公钥.私钥、支付宝私钥、回调地址 https://openhome.alipay.com/platform/appDaily.htm?tab=info
5. 用natapp内网穿透来测试沙箱环境 https://natapp.cn/#download



### 上线问题
1. 阿里云ESC服务器，域名等配置
2. linux安装环境 nginx 映射 mysql maven git jdk 
3. nginx反向代理，java配置文件改变，自动发布脚本
4. nginx 反向代理如果只是host、端口转换一般session不会丢失，所以后端不用开跨域，
    nginx 开启反向代理接口，把跨域开起来
  ` location / { proxy_pass http://127.0.0.1:8080/; add_header Access-Control-Allow-Origin '*'; }`
    详细访问地址：https://www.cnblogs.com/zang dalei/p/6021352.html

5. nginx访问静态页面，tomcat来启动服务

6. 自动化脚本：给shell脚本单独加入可执行的权限 chmod u+x xxx.sh,之后把文件提交git仓库

   * rwx r->读 w->写 x->执行shell脚本  详情：https://www.cnblogs.com/123-/p/4189072.html
   
7. /usr/local/nginx/sbin  ./nginx -s reload 重启nginx