### 2.0开发（升级版）
[git拉取远程分支和推送远程分支](https://www.cnblogs.com/phpper/p/7136048.html)

#### 一、Lambok 来优雅的编码

1. 安装Lambok插件，不然ide会报错
2. [Lambok基本讲解](https://blog.csdn.net/motui/article/details/79012846)

#### 二、maven环境隔离

好处：区分好线上环境和生成环境，使得配置文件不用混乱

1. pom.xml build加入

```xml
 <resources>
    <resource>
        <directory>src/main/resources.${deploy.type}</directory>
        <excludes>
            <exclude>*.jsp</exclude>
        </excludes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
    </resource>
</resources>
```

2. 加入环境变量

```xml
 <profiles>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <deploy.type>dev</deploy.type>
            </properties>
        </profile>

        <profile>
            <id>prod</id>
            <properties>
                <deploy.type>prod</deploy.type>
            </properties>
        </profile>
    </profiles>
```

3. idea打包war 可以用到右侧来区分打包

4. 命令打包 

   * mvn clear package -Dmaven.test=true -P${deploy.type}; (deploy.dev是环境变量的dev或者prod)
   
   
### redis

1.linux是否开启外包连接redis https://blog.csdn.net/liuweixiao520/article/details/78216142


### redis+nginx负载均衡+tomcat+cookies+session单点登录
* JsonUtil.java 对json数据转换成String /String转换json
* CookieUtil.java 对cookie封装，和运用了单点登录的domin,path
* RedisPool.java jedis连接redis客户端
* RedisPoolUtli.java
* SessionExpireFilter.java 全局拦截器重置redis用户数据的过期时间


### redis分布式





    