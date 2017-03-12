<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传测试</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/UploadServlet"
		method="post" enctype="multipart/form-data">
		user name<input type="text" name="username" /> <br /> <input
			type="file" name="f1" /><br /> <input type="file" name="f2" /><br />
		<input type="submit" value="save" />
	</form>
</body>
</html>