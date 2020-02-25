<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
    #signup-button-old{
    	-webkit-animation-name: Sigma90; /* Safari 4.0 - 8.0 */
        -webkit-animation-duration: 23s; /* Safari 4.0 - 8.0 */
        animation-name: Sigma90;
        animation-iteration-count: infinite;
        animation-duration: 23s;
    }
    .pure-form-stacked input[type="text"],
    .pure-form-stacked input[type="email"],
    .pure-form-stacked input[type="password"]{
        margin:0.35em 0;
    }
</style>

<div id="registration-form">

    <c:if test="${not empty message}">
        <div class="notify alert-info">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="notify alert-danger">${error}</div>
    </c:if>



    <form action="${pageContext.request.contextPath}/register" modelAttribute="account" method="post" enctype="multipart/form-data" class="pure-form pure-form-stacked">
        <fieldset>
            <legend style="font-weight:bold;font-size:23px;">Signup</legend>

            <!--<label for="name">Name</label>-->
            <input id="name" type="text" placeholder="Name" name="name">

            <!--<br/>
            <label for="username">Email</label>-->

            <input id="username" type="email" placeholder="Email Address" name="username">

            <!--<br/>
            <label for="password">Password</label>-->
            <input id="password" type="password" placeholder="Password &#9679;&#9679;&#9679;" name="password">


            <div style="margin-top:30px;">
                <input type="submit" class="button retro" id="signup-button" value="Register"/>
            </div>

        </fieldset>
    </form>


</div>
