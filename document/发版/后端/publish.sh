cd /usr/local/leji-background/leji-platform
git pull origin master
mvn clean install -D skipTests
cd /usr/local/leji-background/leji-platform/mall-auth/target
nohup java -jar mall-auth-1.0-SNAPSHOT.jar
exit
cd /usr/local/leji-background/leji-platform/mall-gateway/target
nohup java -jar mall-gateway-1.0-SNAPSHOT.jar
exit
cd /usr/local/leji-background/leji-platform/mall-admin/target
nohup java -jar mall-admin-1.0-SNAPSHOT.jar
exit