FROM ubuntu:22.04 AS builder

# 필요한 환경 변수 설정
ENV PRIVATE_KEY_FILE=/app/private_key.pem
ENV PUBLIC_KEY_FILE=/app/public_key.pem

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 필수 패키지 설치
RUN apt-get update && apt-get install -y openssl

# OpenSSL을 사용하여 개인 키와 공개 키를 생성합니다.
RUN openssl genpkey -algorithm RSA -out $PRIVATE_KEY_FILE -pkeyopt rsa_keygen_bits:2048
RUN openssl rsa -pubout -in $PRIVATE_KEY_FILE -out $PUBLIC_KEY_FILE

# 출력 단계
FROM scratch AS export
COPY --from=builder /app/ /
