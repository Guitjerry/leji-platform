cd /usr/local/leji-background/leji-platform-mall-background
git pull origin master
mvn clean install -D skipTests
mv /usr/local/leji-background/leji-platform-mall-background/mall-auth/target/mall-auth-1.0-SNAPSHOT.jar /usr/local/Programs
mv /usr/local/leji-background/leji-platform-mall-background/mall-gateway/target/mall-gateway-1.0-SNAPSHOT.jar /usr/local/Programs
mv /usr/local/leji-background/leji-platform-mall-background/mall-portal/target/mall-portal-1.0-SNAPSHOT.jar /usr/local/Programs
mv /usr/local/leji-background/leji-platform-mall-background/mall-admin/target/mall-admin-1.0-SNAPSHOT.jar /usr/local/Programs
java -jar mall-auth-1.0-SNAPSHOT.jar &
java -jar mall-gateway-1.0-SNAPSHOT.jar &
java -jar mall-portal-1.0-SNAPSHOT.jar &
java -jar mall-admin-1.0-SNAPSHOT.jar &