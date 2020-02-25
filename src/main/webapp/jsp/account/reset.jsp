<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="reset-password-form">

	<c:if test="${not empty message}">
		<div class="span12">
			<div class="alert alert-info">${message}</div>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div>
			<div class="alert alert-danger">${error}</div>
		</div>
	</c:if>


	<h1>Reset Password</h1>

	<form action="${pageContext.request.contextPath}/account/send_reset" method="post" class="pure-form pure-form-stacked">

        <fieldset>

            <br/>
            <label for="username">Username</label>
            <input id="username" type="email" placeholder="Email" name="username">

            <br/>

            <div class="form-group">
                <input type="submit" class="button retro" id="reset-password" value="Reset Password"/>
            </div>
        </fieldset>

	</form>

</div>


