FROM eclipse-temurin:19

LABEL mentainer="anhtran8301@gmail.com"

WORKDIR /app

COPY target/springboot-blog-restapi-0.0.1-SNAPSHOT.jar /app/springboot-blog-restapi.jar

ENTRYPOINT ["java", "-jar", "springboot-blog-restapi.jar"]