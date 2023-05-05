FROM openjdk:17-jdk-slim

# 作業ディレクトリを指定
WORKDIR /app

# ソースコードをコピー
COPY src /app/src

# Gradle Wrapperをコピー
COPY gradlew /app
COPY gradle /app/gradle

# ビルド環境を準備
RUN chmod +x ./gradlew
RUN ./gradlew --version

# Install necessary dependencies
RUN apt-get update && \
    apt-get install -y curl bash

# Bundle Swagger
RUN curl -sL https://install-node.now.sh/lts | bash -s -- --yes && \
    npm install -g @apidevtools/swagger-cli

# ビルドして実行可能なJARファイルを生成
COPY build.gradle.kts /app
COPY settings.gradle.kts /app
RUN ./gradlew clean build

# ビルドされたJARファイルを実行用ディレクトリにコピー
RUN mkdir -p /app/production && cp /app/build/libs/*SNAPSHOT.jar /app/production/app.jar

# ポート番号の設定
EXPOSE 8080

# 起動コマンド
CMD ["java", "-jar", "/app/production/app.jar"]
