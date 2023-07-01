mvn clean package -DskipTests
docker build -t 10.229.54.39:8297/saga-service:1.0-dev-201908 .
docker push 10.229.54.39:8297/saga-service:1.0-dev-201908
docker -H 10.229.54.39:9697 stack deploy --resolve-image always --compose-file compose-service.yml --with-registry-auth services
