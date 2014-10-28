<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Error</title>
</head>
<body>
	<h1>Error Page</h1>
	<p>Application has encountered an error. Please contact support on...</p>

	Failed URL: ${url} <br/>
	Exception: ${exception.message} <br/>
</body>
</html>
