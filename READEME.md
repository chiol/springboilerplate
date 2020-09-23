
## jpa

## web

## spring

## test

## rest docs

## actuator

애플리케이션 상태를 종합적으로 모니터링할 수 있는 플러그인

### 의존성 주입
```groovy
dependencies{
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
}
```
### 설정 전체 노출
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 크롬에서 확인
chrome json viewer 크롬 확장 플러그인 설치

http://localhost:8081/actuator

### git 정보 확인

```groovy
plugins {
    id "com.gorylenko.gradle-git-properties" version "2.2.2" 
}
```
```yaml
management:
  info:
    git:
      enabled: true
      mode: full // simple
```
http://localhost:8081/actuator/info

### health Database
```yaml
management:
  endpoint:
    health:
      enabled: true
      show-details: always
```

http://localhost:8081/actuator/health


### intellij Endpoint

![image1](https://github.com/cheese10yun/blog-sample/raw/master/actuator/images/endpoint-mapping.png)

![image2](https://github.com/cheese10yun/blog-sample/raw/master/actuator/images/endpoints-heath.png)

참고 https://www.popit.kr/spring-actuator-%EA%B8%B0%EC%B4%88-%EC%84%A4%EC%A0%95-intellij-%ED%99%9C%EC%9A%A9%ED%95%98%EA%B8%B0/
