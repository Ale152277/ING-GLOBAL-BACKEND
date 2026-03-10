FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY . .

RUN rm -f src/main/resources/application-local.properties

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "echo DB_URL=$DB_URL && echo spring.datasource.url=$DB_URL > /tmp/test.properties && java -Dspring.datasource.url=$DB_URL -Dspring.datasource.username=$DB_USERNAME -Dspring.datasource.password=$DB_PASSWORD -jar target/ecommerce-0.0.1-SNAPSHOT.jar"]