# SpringBootStudy
회원로그인,로그아웃,이메일중복,회원관리,게시판,댓글

💻 Spring Boot Project Setup Guide

![Spring Boot](https://img.shields.io/badge/SpringBoot-3.2.11-green?style=for-the-badge&logo=spring)  
![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)  
![MySQL](https://img.shields.io/badge/MySQL-8.x-orange?style=for-the-badge&logo=mysql)


📂 프로젝트 생성 및 설정
1️⃣ Spring Initializer로 프로젝트 생성
Spring Initializer에서 프로젝트 설정:
Spring Boot Version: 3.2.11
Dependencies: MySQL, Spring Data JPA, Spring Web, Thymeleaf
.zip 파일 다운로드 후 압축 해제.
IntelliJ IDEA에서 프로젝트 열기:
경로 설정:
영어로 된 경로 사용 (예: C:\projects).
안전한 폴더 대신 일반 폴더 선택.
빌드 도구 변경:
파일 > 설정 > 빌드, 실행, 배포 > 빌드 도구 > Gradle
빌드 및 테스트 실행을 IntelliJ IDEA로 설정.
2️⃣ Gradle 설정
**gradle-wrapper.properties**에서 Gradle 버전을 Java 17 및 Spring Boot 3.2.11과 호환되는 8.1로 수정:

gradle
# gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.1-bin.zip
3️⃣ 데이터베이스 및 JPA 설정
application.yml 파일에 MySQL 및 JPA 관련 설정 추가:

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
  thymeleaf:
    cache: false
🛠️ 프로젝트 구조
📁 Member 모듈
1️⃣ 프로젝트 구조
css
src
└── main
    ├── java
    │   └── com.codingrecipe.member
    │       ├── MemberApplication.java
    │       ├── controller
    │       │   ├── HomeController.java
    │       │   └── MemberController.java
    │       ├── service
    │       │   └── MemberService.java
    │       └── repository
    │           └── MemberRepository.java
    └── resources
        ├── templates
        │   ├── home.html
        │   └── member.html
        └── application.yml
2️⃣ 주요 파일 설명
MemberApplication.java:
Spring Boot 애플리케이션 실행 클래스.

Controller:

HomeController: 홈 화면 처리.
MemberController: 회원 관련 요청 처리.
java
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberEntity member = memberService.login(memberDTO);
        if (member != null) {
            session.setAttribute("loginName", member.getName());
        }
        return "redirect:/";
    }
}
Service:

MemberService: 비즈니스 로직 처리 및 데이터 저장소와의 상호작용.
java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberEntity login(MemberDTO memberDTO) {
        Optional<MemberEntity> optionalMember =
            memberRepository.findByMemberEmail(memberDTO.getEmail());
        return optionalMember.orElse(null);
    }
}
Repository:

MemberRepository: JPA 인터페이스 사용, 사용자 정의 쿼리 작성 가능.
java
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
📁 Board 모듈
1️⃣ 프로젝트 구조
src
└── main
    └── java
        └── com.codingrecipe.board
            ├── BoardController.java
            ├── CommentController.java
            ├── BoardRepository.java
            ├── CommentRepository.java
            ├── config
            │   └── WebConfig.java
            └── service
                └── BoardService.java
2️⃣ 주요 파일 설명
WebConfig.java: 정적 리소스 관리 설정.

java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("file:///C:/resources/");
    }
}
BoardController.java: 게시판 관련 요청 처리.

java
@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/boards")
    public String listBoards(@PageableDefault Pageable pageable, Model model) {
        model.addAttribute("boards", boardService.getBoards(pageable));
        return "board/list";
    }
}
CommentController.java: 댓글 처리 및 부모 테이블과의 연관 관계 관리.

🖥️ 클라이언트 및 Thymeleaf
HTML 파일:

home.html: Thymeleaf 문법으로 데이터 출력.
html
<div>
    <h1 th:text="'Welcome, ' + ${session.loginName}">Welcome, User</h1>
</div>
AJAX 사용:

javascript
$.ajax({
    url: "/api/member",
    type: "POST",
    data: { email: "test@example.com" },
    success: function (response) {
        console.log(response);
    },
    error: function (error) {
        console.error(error);
    }
});
📚 주요 참고 사항
Spring Boot 버전: 3.2.11
Java 버전: 17
MySQL 버전: 8.x
Thymeleaf: 서버-클라이언트 간 데이터 렌더링에 사용.
Gradle Wrapper 버전: 8.1
