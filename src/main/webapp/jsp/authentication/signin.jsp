<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Signin</title>
	</head>
	<body>
		
<style type="text/css">
</style>
	
		<c:if test="${not empty message}">
			<div class="notify notify-info">${message}</div>
		</c:if>

		<c:if test="${not empty error}">
			<div class="notify notify-alert">${error}</div>
		</c:if>

		<style type="text/css">
		    .form-group{
		        margin-bottom:20px;
		    }
		</style>

		<div>
			<form action="${pageContext.request.contextPath}/authenticate" class="pure-form pure-form-stacked" modelAttribute="signon" method="post" >

                <div class="form-group">
				  	<label for="username">Email</label>
				  	<input type="text" name="username" class="form-control" id="username" placeholder="">
				</div>
				<div class="form-group">
				  	<label for="password">Password</label>
				  	<input type="password" name="password" class="form-control" id="password" placeholder="***">
				</div>


                <div style="text-align:center; margin-top:30px;">
				    <a href="${pageContext.request.contextPath}/signup">Signup</a>&nbsp;&nbsp;
			        <input type="hidden" name="targetUri" value="${targetUri}" />
                    <input type="submit" class="button beauty-light" value="Signin">
                </div>
                <br/>

                <br/>
                <br/>
				<a href="${pageContext.request.contextPath}/account/reset">Forgot Password</a>&nbsp;&nbsp;

			</form>
		</div>
		
	</div>
			
	</body>
</html>

