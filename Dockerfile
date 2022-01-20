FROM public.ecr.aws/ddocker/library/maven:latest as build
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn clean package

FROM FROM public.ecr.aws/docker/library/maven:eclipse-temurin
COPY --from=build target/*.jar ./
ENTRYPOINT java -jar ./book_worms-1.0-SNAPSHOT.jar