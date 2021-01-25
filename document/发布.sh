cd /usr/local/leji-background/leji-platform-mall-background
git pull origin master
mvn clean install -D skipTests
#发布gateway
cd /usr/local/leji-background/leji-platform-mall-background/mall-gateway/target
java -jar mall-gateway-1.0-SNAPSHOT.jar
#发布auth
cd /usr/local/leji-background/leji-platform-mall-background/mall-auth/target
java -jar mall-gateway-1.0-SNAPSHOT.jar
#发布admin
cd /usr/local/leji-background/leji-platform-mall-background/mall-admin/target
java -jar mall-admin-1.0-SNAPSHOT.jar
