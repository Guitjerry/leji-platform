cd  /usr/local/leji/platform/leji-platform
git pull origin master
mvn clean install -D skipTests
cd /usr/local/leji/platform/leji-platform/mall-auth/target
java -jar mall-auth-1.0-SNAPSHOT.jar
cd /usr/local/leji/platform/leji-platform/mall-gateway/target
java -jar mall-gateway-1.0-SNAPSHOT.jar
cd /usr/local/leji/platform/leji-platform/mall-admin/target
java -jar mall-admin-1.0-SNAPSHOT.jar