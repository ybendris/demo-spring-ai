# Étape 1 : Builder avec une image qui a Maven
FROM ghcr.io/graalvm/graalvm-community:24.0.2 AS builder

# Install necessary tools
RUN microdnf install wget
RUN microdnf install xz

# Install maven for build the spring boot application
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz
RUN tar xvf apache-maven-3.9.11-bin.tar.gz

# Set up the environment variables needed to run the Maven command.
ENV M2_HOME=/app/apache-maven-3.9.11
ENV M2=$M2_HOME/bin
ENV PATH=$M2:$PATH

# Set the working directory
COPY pom.xml /appli/
COPY src /appli/src
WORKDIR /appli
RUN mvn -Pnative -Dmaven.test.skip=true native:compile

# Étape 2 : exécution en tant qu'application autonome
FROM ubuntu:latest

ENV HOST 0.0.0.0

WORKDIR /app
COPY --from=builder /appli/target/demo-spring-ai ./app_exec
RUN chmod +777 /app/app_exec

EXPOSE 8080

ENTRYPOINT ["/app/app_exec"]
