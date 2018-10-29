### 使用方式

1. `resources/generatorConfig.xml` 定义自己的数据库表(table属性)
2. 修改`mysql.properties`数据库属性


### 注意事项

1. 重新生成逆向工程要删除代码，不然会追加，不会重新生成。
2. 测试mybatis 添加、删除、修改 需要 `sqlSession.commit();` ,不然不会添加到数据库
3. maven 工程要添加

    ```xml
         <build>
               <!--把xml文件放入target  classes 目录-->
               <resources>
                   <resource>
                       <directory>src/main/resources</directory>
                       <includes>
                           <include>**/*.xml</include>
                           <include>**/*.properties</include>
                       </includes>
                   </resource>
                   <resource>
                       <directory>src/main/java</directory>
                       <includes>
                           <include>**/*.xml</include>
                       </includes>
                   </resource>
       
               </resources>
       
           </build>
    ```