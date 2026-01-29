# Kotlin Spring API Gateway

Spring Cloud Gateway 기반의 API Gateway 예제입니다. MSA 환경에서 API 토큰 인증을 수행하고 경로에 따라 각 서비스로 라우팅합니다.

## 주요 기능
- API 토큰 인증 (Global Filter)
- 서비스별 라우팅 (Path 기반)
- 환경 변수로 서비스 URL 분리

## 구성
- `/api/users/**` -> USER_SERVICE_URL (기본값: http://localhost:8081)
- `/api/orders/**` -> ORDER_SERVICE_URL (기본값: http://localhost:8082)
- `/api/products/**` -> PRODUCT_SERVICE_URL (기본값: http://localhost:8083)

## 실행 방법
```bash
./gradlew bootRun
```

## 토큰 인증
요청 헤더에 `X-API-TOKEN` 값을 포함해야 합니다. 예제 토큰은 `application.yml`의 `gateway.security.allowed-tokens`에서 설정합니다.

```bash
curl -H "X-API-TOKEN: local-dev-token" http://localhost:8080/api/users/health
```

## 환경 변수
```bash
export USER_SERVICE_URL=http://localhost:8081
export ORDER_SERVICE_URL=http://localhost:8082
export PRODUCT_SERVICE_URL=http://localhost:8083
```
