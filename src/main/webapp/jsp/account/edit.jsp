<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="edit-account-form">

	<style type="text/css">
		#profile-image img{
			height:250px;
			width:250px;
			border-radius: 23px;
			-moz-border-radius: 23px;
			-webkit-border-radius: 23px;
		}
	</style>

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


	<div class="float-left">

        <form action="${pageContext.request.contextPath}/account/update/${account.id}" class="pure-form pure-form-stacked" modelAttribute="account" method="post" enctype="multipart/form-data">

            <div id="profile-image">
                <img src="${pageContext.request.contextPath}/${account.imageUri}"/>
            </div>

            <span id="">250 x 250</span>

            <input type="file" name="image"/>

            <input type="hidden" name="id" value="${account.id}"/>
            <input type="hidden" name="imageUri" value="${account.imageUri}"/>

            <br class="clear"/>
            <label for="username">Username : ${account.username}</label>


            <br class="clear"/>
            <label for="name">Name</label>
            <input type="text" name="name" id="name" placeholder="" value="${account.name}">

            <br class="clear"/>
            <label for="name">Age</label>
            <input type="text" name="age" id="name" placeholder="" value="${account.age}" style="width:57px">

            <br class="clear"/>
            <label for="name">Location</label>
            <input type="text" name="location" id="name" placeholder="Boston, MA" value="${account.location}">




            <div style="margin-top:34px;">
                <a href="${pageContext.request.contextPath}/">Cancel</a>
                <input type="submit" class="button" id="update" value="Update"/>
            </div>

        </form>
    </div>
</div>
	
		
