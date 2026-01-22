<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
	.menu-toggle {
      display: none;
      font-size: 1.8rem;
      background: none;
      border: none;
      color: white;
      cursor: pointer;
    }
    header {
      background-color: #2c3e50;
      color: white;
      padding: 1rem 2rem;
      box-shadow: 0 2px 10px rgba(0,0,0,0.3);
      position: sticky;
      top: 0;
      z-index: 100;
    }
    .nav-container {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    nav ul {
      list-style: none;
      display: flex;
      gap: 2.5rem;
    }

    nav a {
      color: #ecf0f1;
      text-decoration: none;
      font-size: 1.1rem;
      font-weight: 500;
      padding: 0.6rem 1rem;
      border-radius: 6px;
      transition: all 0.25s ease;
    }

    nav a:hover,
    nav a.active {
      background-color: #3498db;
      color: white;
    }
</style>
<meta charset="UTF-8">
<title>Spring Legacy Testing Home</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/modules/header.jsp"></jsp:include>
</body>
</html>