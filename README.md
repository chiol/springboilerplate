# Spring boilerplate

## Requirement
- openjdk(AdoptOpenJdk) 11.0.8
- Gradle 6.5.1

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
        - application-test.yml // 테스트 환경 설정 파일
- test
    - common // 테스트에 필요한 공통적인 기능
    - domain1

\* 폴더는 소문자 파일은 대문자로 시작

\* 환경 설정 https://cheese10yun.github.io/spring-jpa-best-11/
## Web
- WebMvc using servlet

![webMvc](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=http%3A%2F%2Fcfile8.uf.tistory.com%2Fimage%2F99AB22345C03AB4C1D9630)

- flow
    - client <--(DTO)--> controller <--(DTO)--> service <--(DTO)--> repository <--(Entity)--> DB

## Data-JPA
- JPA (Java Persistence API) 
- 자바 진영의 ORM(Object-Relational Mapping) 기술 표준 
- 구현체 
![jpa](https://gmlwjd9405.github.io/images/inflearn-jpa/implementation-of-jpa.png)

단순 반복 작업을 편하게 만들어줌.

- repository
- auditing
  - CreatedDate
  - LastModifiedDate
  - CreatedBy
- dialect
  - 여러 데이터 베이스를 자동으로 지원


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
쿼리문을 자바 코드로 작성할 수 있음.

쿼리문의 컴파일 단계에서 잡을 수 있음. 

참고: https://kimyhcj.tistory.com/356

## Lombok

https://www.projectlombok.org/features/all

## Mapstruct

객체간 매핑 라이브러리

https://www.baeldung.com/java-performance-mapping-frameworks
https://meetup.toast.com/posts/213

## Security

Authentication(인증)
- role-based
    - ADMIN
    - USER

Authorization(인가)
- AOP
```java
// 1. Annotation 생성
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessCheck {

}

// 2. AOP 클래스 생성
@Aspect
@Component
public class AccountAspect {
    
    // 3. Pointcut 설정
    @Pointcut("@annotation(kr.ibct.springboilerplate.account.AccessCheck)")
    private void AccessCheck() {
    }


    // 4. Before, After, Around 등 pointcut에 시점에 해당하는 메소드 작성
    @Before("AccessCheck()")
    public Object doAccessCheck(JoinPoint joinPoint) {
        // ...
    }

}

@RestController
@RequestMapping("/api/v1/users")
public class AccountController {

        // 5. Annotation 추
        @AccessCheck
        @GetMapping("/{id}")
        public ResponseEntity<?> getAccount(@PathVariable Long id, @CurrentUser Account account) {
            return ResponseEntity.ok(accountService.getAccount(id));
        }
}
```

- Expression-Based Access Control
```java
public class AccountSecurity {
    public boolean check(Authentication authentication, Long userId) {
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        AccountAdapter principal = (AccountAdapter) authentication.getPrincipal();
        Account account = principal.getAccount();
        return account.getId().equals(userId);
    }
}

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public AccountSecurity accountSecurity() {
        return new AccountSecurity();
    }

    // 해당 요청에 SpEL을 통해 조건식을 만든다.
    @Override
    protected void configure(HttpSecurity http) {
        http.authorizeRequests()
            .antMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}")
                .access("hasRole('ADMIN') or @accountSecurity.check(authentication, #userId)"); 
    }
}
```

- Method Security
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true //prePostEnabled를 true 설정한다.
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // ...
}

@RestController
@RequestMapping("/api/v1/users")
public class AccountController {


    // 해당 메소드에 SpEL을 통해 조건식을 만든다.
    @PatchMapping("/{id}")
    @PreAuthorize("(hasRole('USER') and #id == #account.id) or hasRole('ADMIN')")
    public ResponseEntity<?> patchAccount(...) {
        ...
    }
}
```

## Test
- 테스트에서 lombok을 사용해야하기 때문에 추가
- 유닛 테스트
- 테스트를 추가할 때 BaseTest 클래스 상속 (기본 세팅을 위해)
```java
@SpringBootTest // 스프링 테스트를 위한 어노테이션
@AutoConfigureMockMvc // Mvc를 모킹으로 만들기 위한 설정
@ActiveProfiles("test") // application.yml 설정 중 test profile을 사용
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트의 순서를 위해 사용
public class BaseTest {
}
```

참고 https://github.com/keesun/study/blob/master/rest-api-with-spring/src/test/java/me/whiteship/demoinfleanrestapi/common/BaseTest.java

## REST docs

테스트를 통해 자동으로 docs 생성

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
