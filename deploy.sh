
echo "===========进入git项目happymmall目录============="
cd /usr/local/mmall


echo "==========git切换分之到v1.0==============="
git checkout v1.0

echo "==================git fetch======================"
git fetch

echo "==================git pull======================"
git pull


echo "===========编译并跳过单元测试===================="
mvn clean package -Dmaven.test.skip=true


echo "============删除旧的ROOT.war==================="
rm /usr/local/java/apache-tomcat-8.5.34/webapps/ROOT.war


echo "======拷贝编译出来的war包到tomcat下-ROOT.war======="
cp /usr/local/web/mmall/target/mmall.war  /usr/local/java/apache-tomcat-8.5.34/webapps/ROOT.war


echo "============删除tomcat下旧的ROOT文件夹============="
rm -rf /usr/local/java/apache-tomcat-8.5.34/webapps/ROOT



echo "====================关闭tomcat====================="
/usr/local/java/apache-tomcat-8.5.34/bin/shutdown.sh


echo "================sleep 10s========================="
for i in {1..10}
do
	echo $i"s"
	sleep 1s
done


echo "====================启动tomcat====================="
/usr/local/java/apache-tomcat-8.5.34/bin/startup.sh


cd /usr/local/java/apache-tomcat-8.5.34/logs

echo "====================查看tomcat日志====================="
tail -f catalina.out
