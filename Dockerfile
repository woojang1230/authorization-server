FROM amazoncorretto:11-al2-jdk AS base

FROM base AS builder

ADD . .
RUN ./gradlew clean build

FROM base AS runner

ENV JARFILE="/app.jar" \
    JAVA_OPTS=""

COPY --from=builder ./build/libs/*.jar $JARFILE

RUN mkdir -p /app/security
COPY ./output/*.jks /app/security/

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN yum -y install jq git iputils telnet perl-Digest-SHA

CMD java $JAVA_OPTS -jar $JARFILE
