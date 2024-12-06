# SpringBootStudy
회원로그인,로그아웃,이메일중복,회원관리,게시판,댓글

## 학습내용 정리
Spring Boot Project 생성 및 설정
1. 프로젝트 생성
Spring Initializer를 통해 프로젝트를 생성하고 .zip 파일 다운로드 후 압축 해제.
IntelliJ 설정:
설정 경로: 파일 > 설정 > 빌드, 실행, 배포 > 빌드 도구 > Gradle.
빌드 및 테스트 실행 옵션 변경: IntelliJ IDEA로 설정.
프로젝트 이름: 영어로 작성하며, 대괄호([])는 포함하지 않음.
프로젝트 경로: C:\와 같은 영어 경로 및 안전하지 않은 폴더 대신 일반 폴더로 설정.
2. Gradle 설정
Gradle Wrapper 수정: gradle-wrapper.properties 파일을 열어 Spring Boot와 호환되는 Gradle 버전(예: 8.1)으로 설정.
Java 17 및 Spring Boot 3.2.11 기준.
3. 데이터베이스 및 JPA 설정
의존성: MySQL과 JPA를 포함.
application.yml 설정:
yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database_name
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQL8Dialect
Member 프로젝트 구조
패키지 경로: src > main > java > com.codingrecipe.member
MemberApplication

@SpringBootApplication 어노테이션 사용.
Controller

클래스: HomeController, MemberController.
어노테이션: @Controller, @RequiredArgsConstructor, @GetMapping, @PostMapping.
DTO 사용:
@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @ToString(Lombok).
model 객체를 통해 DTO 값을 클라이언트로 전달.
HttpSession 활용:
서비스에서 전달된 DTO 데이터를 세션에 저장.
session.getAttribute("name")으로 세션 정보 가져오기 가능.
Service

클래스: MemberService.
어노테이션: @Service, @RequiredArgsConstructor.
Repository 주입: private final MemberRepository memberRepository.
Repository

JpaRepository 확장: public interface MemberRepository extends JpaRepository<MemberEntity, Long>.
추상 메서드 예시:
java
Optional<MemberEntity> findByMemberEmail(String memberEmail);
SQL 문: select * from member_table where member_email = ?.
Templates

경로: src > main > resources > templates > *.html.
Thymeleaf 사용:
DTO로 전달받은 값들을 HTML 태그에 할당.
button, a, form 태그 및 JavaScript 사용자 정의 함수로 컨트롤러 요청 가능.
Ajax 활용

사용법:
컨트롤러에서 @ResponseBody로 응답 처리.
JavaScript에서 사용자 정의 함수($.ajax)로 서버 값 요청.
응답 처리:
success와 error로 응답 값을 할당 후 브라우저에 출력.
Board 프로젝트 구조
패키지 경로: src > main > java > com.codingrecipe.board
Config

클래스: WebConfig.
어노테이션: @Configuration.
Resource 설정:
java
public class WebConfig implements WebMvcConfigurer {
    private final String resourcePath = "/upload/**";
    private final String savePath = "file:///C:/upload/";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath).addResourceLocations(savePath);
    }
}
Controller

클래스: BoardController, CommentController, HomeController.
페이징 처리:
java
@PageableDefault(page = 1) Pageable pageable, Model model
Entity 관계

CommentController:
다대일 관계: 부모 테이블 접근 가능.
자식 테이블 데이터 반환:
java
commentEntity.getBoardEntity().getId();
Repository

BoardRepository:
@Modifying, @Query를 통해 직접 쿼리 작성 가능.
추가 정보
Java: 17.
Spring Boot: 3.2.11.
Database: MySQL 8.x.
사용 기술: JPA, Thymeleaf, Ajax.
빌드 도구: Gradle.

