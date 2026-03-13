# 使用Maven镜像构建应用
FROM maven:3.9-eclipse-temurin-17 AS build

# 设置工作目录
WORKDIR /app

# 复制pom.xml和源代码
COPY pom.xml .
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 使用Java运行时镜像
FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=build /app/target/blog-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 5000

# 启动应用
CMD ["java", "-jar", "app.jar"]