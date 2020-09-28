# Spring boilerPlate
## Plugins
- spring
    - web
    - data-jpa
    - validation
    - security
    - actuator
    - devtools
    - rest-docs
- jjwt 
- mapstruct
- querydsl
  
## Directory structure
- main
    - java 
        - common // 각 도메인에서 공통으로 사용할 것
        - config // 해당 어플리케이션의 설정 파일
        - domain1
            - Domain1 (required)
            - Domain1Controller (required)
            - Domain1ControllerAdvice (required)
            - Domain1Service (required)
            - Domain1Repository (required)
            - Domain1Dto (required)
            - Domain1Mapper (required)
            - Domain1Aspect (optionally)
            - exceptions (optionally)
                - Domain~~Exception
                - ...
            - and so on...
    - resources
        - application.yml // 공통 환경 설정 파일
        - application-local.yml // 로컬 환경 설정 파일
        - application-prod.yml // 프로덕션 환경 설정 파일
        - application-test.yml // 테스트 환경 설정 파
- test
    - common // 테스트에 필요한 공통적인 기능
    - domain1

\* 폴더는 소문자 파일은 대문자

\* 환경 설정 https://cheese10yun.github.io/spring-jpa-best-11/
## Web
- WebMvc
![webMvc](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=http%3A%2F%2Fcfile8.uf.tistory.com%2Fimage%2F99AB22345C03AB4C1D9630)

- flow
    - controller <--(DTO)--> service <--(DTO)--> repository <--(Entity)--> DB

## Data-JPA
- JPA (Java Persistence API) 
- 자바 진영의 ORM(Object-Relational Mapping) 기술 표준 
- 구현체 
![jpa](https://gmlwjd9405.github.io/images/inflearn-jpa/implementation-of-jpa.png)

단순 반복 작업을 편하게 만들어줌.

## Querydsl
```groovy
dependencies {
    compile("com.querydsl:querydsl-core")
    compile("com.querydsl:querydsl-jpa")

    annotationProcessor("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa") // querydsl JPAAnnotationProcessor 사용 지정
    annotationProcessor("jakarta.persistence:jakarta.persistence-api") // java.lang.NoClassDefFoundError(javax.annotation.Entity) 발생 대응 
    annotationProcessor("jakarta.annotation:jakarta.annotation-api") // java.lang.NoClassDefFoundError (javax.annotation.Generated) 발생 대응 
}
```
쿼리문의 컴파일 단계에서 잡을 수 있음. 

참고: https://kimyhcj.tistory.com/356

## Lombok

https://www.projectlombok.org/features/all

## Security

Authentication(인증)
- role-based
    - ADMIN
    - USER

Authorization(인가)
- AOP
- Expression-Based Access Control
- Method Security

PasswordEncoder

## Test
- 테스트에서 lombok을 사용해야하기 때문에 추가

- 유닛 테스트
- 테스트를 추가할 때 BaseTest 클래스 상속 (기본 세팅을 위해)
```java
@SpringBootTest // 스프링 테스트를 위한 어노테이션
@AutoConfigureMockMvc // Mvc를 모킹으로 만들기 위한 설정
@ActiveProfiles("test") // application.yml 설정 중 test profile을 사용
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트의 순서를 위해 사용
```

참고 https://github.com/keesun/study/blob/master/rest-api-with-spring/src/test/java/me/whiteship/demoinfleanrestapi/common/BaseTest.java



## REST docs

## Actuator

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
