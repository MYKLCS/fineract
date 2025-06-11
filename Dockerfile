# 1️⃣ Use Java 21 kitchen
FROM eclipse-temurin:21-jdk

# 2️⃣ Go to app folder
WORKDIR /app

# 3️⃣ Bring all ingredients → copy your Fineract source code into the kitchen
COPY . .

# 4️⃣ Check that Gradle is ready
RUN ./gradlew --version

# 5️⃣ Bake the app → build the JAR file
RUN ./gradlew clean bootJar -x test

# 6️⃣ Tell people where to find the app (port 8443)
EXPOSE 8443

# 7️⃣ Serve the cake → run the JAR file
CMD ["java", "-jar", "fineract-provider/build/libs/fineract-provider.jar"]
