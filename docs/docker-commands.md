# Docker container lifecycle (Task 11)

```
mvn -B package -DskipTests
docker build -t product-catalogue:1.0 .
docker run -d --name catalogue -p 8082:8080 -e ADMIN_PASSWORD=<admin-pass> -e USER_PASSWORD=<user-pass> product-catalogue:1.0
docker ps
docker logs catalogue
docker stop catalogue
docker start catalogue
docker rm -f catalogue
```

App URL: http://localhost:8082/ (8080 is Jenkins, 8081 is Tomcat).
