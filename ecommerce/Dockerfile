FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY . .

RUN rm -f src/main/resources/application-local.properties

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "java \
  -Dspring.datasource.url=${DB_URL} \
  -Dspring.datasource.username=${DB_USERNAME} \
  -Dspring.datasource.password=${DB_PASSWORD} \
  -Dspring.mail.username=${MAIL_USERNAME} \
  -Dspring.mail.password=${MAIL_PASSWORD} \
  -Djwt.secret=${JWT_SECRET} \
  -Dapp.mail.remitente=${MAIL_REMITENTE} \
  -Dapp.mail.destinatario=${MAIL_DESTINATARIO} \
  -Dapp.frontend.url=${FRONTEND_URL} \
  -jar target/ecommerce-0.0.1-SNAPSHOT.jar"]