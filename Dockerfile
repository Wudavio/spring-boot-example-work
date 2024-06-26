# 使用官方的 OpenJDK 影像
FROM openjdk:17-jdk-slim AS build

# 安装 Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# 設定工作目錄
WORKDIR /app

# 複製 pom.xml
COPY pom.xml /app/

# 使用offline模式下載依賴
RUN mvn dependency:go-offline -B

# 複製全部檔案
COPY . /app

# 編譯
RUN mvn clean package -Dmaven.test.skip

# 使用官方的 OpenJDK
FROM openjdk:17-jdk-slim

# 複製編譯好的jar檔案
COPY --from=build /app/target/work_example-0.0.1-SNAPSHOT.jar /app/app.jar

# 複製 .env 檔案
COPY ./.env /app/.env

# 設定工作目錄
WORKDIR /app

# 啟動
ENTRYPOINT ["sh", "-c", "export $(grep -v '^#' .env | xargs) && java -jar app.jar"]

