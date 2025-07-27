FROM ghcr.io/graalvm/graalvm-community:21 AS builder

ARG APP_NAME

RUN microdnf install wget
RUN microdnf install xz

RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz
RUN tar xvf apache-maven-3.9.11-bin.tar.gz

ENV M2_HOME=/app/apache-maven-3.9.11
ENV M2=$M2_HOME/bin
ENV PATH=$M2:$PATH

COPY pom.xml /appli/
COPY src /appli/src
WORKDIR /appli
RUN mvn -Pnative -Dmaven.test.skip=true native:compile

FROM alpine:latest AS runtime
ARG APP_NAME

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /appli/target/${APP_NAME} ./app-native

EXPOSE 8080

ENTRYPOINT ["./app-native"]
