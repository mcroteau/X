<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<style type="text/css">
p{
    line-height:1.4em;
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

<p>Monitor your progress for Tony Horton's workout P90(x).</p>

<p>Built with love &hearts;</p>

<a href="${pageContext.request.contextPath}/signup" class="button retro large">Sign Up !</a>

<br/>
<br/>
<br/>

<p>Want to contribute or get involved... SigmaX is Open Source. We are looking for developers.
<a href="http://www.github.com/mcroteau/Sigma90" target="_blank">Go here learn more &raquo;</a></p>

</script>