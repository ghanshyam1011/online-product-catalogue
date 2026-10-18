FROM eclipse-temurin:17-jre

RUN useradd --system --create-home appuser
WORKDIR /app

# The executable WAR is built by Maven first (mvn package) and copied in.
COPY target/product-catalogue-0.0.1-SNAPSHOT.war app.war

USER appuser
EXPOSE 8080

# Passwords are not baked into the image. Pass them at run time:
#   -e ADMIN_PASSWORD=... -e USER_PASSWORD=...
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -fs http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.war"]
