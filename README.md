# SpringBootStudy
회원로그인,로그아웃,이메일중복,회원관리,게시판,댓글


<!-- templates/example.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Example Page</title>
</head>
<body>
    <h1 th:text="${dto.name}">Name Placeholder</h1>
    <p th:text="${dto.email}">Email Placeholder</p>
</body>
</html>
