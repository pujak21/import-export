FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -q package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --no-create-home app

COPY --from=build /app/target/importexport-0.0.1-SNAPSHOT.jar app.jar

USER app

# Render injects PORT at runtime; 8085 is only the local fallback.
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
