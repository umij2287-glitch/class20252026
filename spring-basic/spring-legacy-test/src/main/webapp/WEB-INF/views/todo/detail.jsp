<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>TODO 상세</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 800px;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/spring-legacy-test/todo/list">TODO 관리</a>
    </div>
</nav>

<div class="container mt-5">
    <h2 class="mb-4">TODO 상세</h2>

    <div class="card">
        <div class="card-body">
            <h5 class="card-title mb-3">${ todo.title }</h5>

            <p class="text-muted"><fmt:formatDate value="${ todo.writeDate }"/></p>

<c:set var="enter" value="
" />
            <p class="mt-3">
                ${ fn:replace (todo.content, enter, "<br/>") }
            </p>
   			<c:choose>
         		<c:when test="${ todo.completed }">
          			<span class="badge bg-success text-dark">완료</span>
             	</c:when>
             	<c:otherwise>
             		<span class="badge bg-warning text-dark">진행중</span>
             	</c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-4 text-end">
        <a href="/spring-legacy-test/todo/list" class="btn btn-outline-secondary">목록</a>
        <a href="/spring-legacy-test/todo/edit/${ todo.idx }" class="btn btn-outline-primary">수정</a>
        <button id="delete-post" type="button" class="btn btn-outline-danger">삭제</button>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$( () => {
    $("#delete-post").on("click", (e) => {
		e.preventDefault();
        const confirmation = confirm("삭제하시겠습니?");
        if(!confirmation){
            return;
        }
        location.href = "/spring-legacy-test/todo/delete/${ todo.idx }";
    });
});
</script>
</body>
</html>
    