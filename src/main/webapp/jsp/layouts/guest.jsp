<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<html>
<head>
    <title>SigmaX : Power to the people</title>

    <link href="https://fonts.googleapis.com/css?family=Roboto:200,300,400,500,600,700&display=swap" rel="stylesheet">

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/sigma-logo.png">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lib/pure.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css?v=<%=System.currentTimeMillis()%>">
</head>
<body>

    <style type="text/css">
      @media screen and (max-width: 590px) {
            #guest-content-container{
                width:100%;
                margin:0px auto 0px auto;
            }
            #guest-content-right{
                float:none;
                margin:30px !important;
            }
            #Sigma90-logo-container{
                display:none !important;
            }
            #content-mobile{
                display:block !important;
            }
        }

        #guest-content-right{
            float:left;
            margin-left:100px;
            margin-top:47px;
            width:300px;
        }

        #guest-content-container{
            margin:0px auto 171px auto;
            width:760px; 
            border:solid 0px #ddd;
        }

        #Sigma90-logo-container{
            float:left;
            margin-top:0px;
            position:relative;
            width:339px;
        }

        #Sigma90-logo-deg{
            margin-top:-97px;
            margin-left:-20px;
            position:absolute;
        }

        #Sigma90-logo-deg{
            -webkit-animation-name: logo-guest; /* Safari 4.0 - 8.0 */
            -webkit-animation-duration: 22s; /* Safari 4.0 - 8.0 */
            animation-name: logo-guest;
            animation-iteration-count: infinite;
            animation-duration: 22s;
        }


        #logo-logo{
            color: #12ADFD;
            font-family:'Roboto', Arial;
            font-weight:500;
            -webkit-animation-name: logo-guest-d; /* Safari 4.0 - 8.0 */
            -webkit-animation-duration: 22s; /* Safari 4.0 - 8.0 */
            animation-name: logo-guest-d;
            animation-iteration-count: infinite;
            animation-duration: 22s;
        }
        /* Safari 4.0 - 8.0 */
        @-webkit-keyframes logo-guest {
          0%  {color: #12ADFD;}
          53%  {color: #2b2b34;}
          100% {color: #12ADFD;}
        }

        @keyframes logo-guest {
          0%  {color: #12ADFD;}
          31%  {color: #2b2b34;}
          64% { color: #E6382D; }
          100% {color: #12ADFD;}
        }

        #Sigma90-logo-container h1{
            font-size:72px;
            font-family:'Roboto', Arial;
            font-weight:700;
            font-weight:bold;
            margin-left:8px;
        }
        #Sigma90-logo-container p{
            font-family:'Roboto', Arial;
        }


    </style>


    <div id="guest-content-container">

        <br class="clear"/>

        <div id="Sigma90-logo-container" style="text-align:center">
            <a href="${pageContext.request.contextPath}/home" id="logo-logo" style="font-size:321px;text-decoration:none;line-height:0.9em;">&infin;</a>
            <h1 style="font-weight:200">Sigma<span style="font-family:'Roboto'; font-weight:600">X</span></h1>
            <p>Bring it to the people.</p>
            <br class="clear"/>
        </div>

        <div id="guest-content-right">

            <div style="text-align:left;margin-bottom:29px;margin-top:20px;">
                <shiro:authenticated>
                    Welcome back <strong>${sessionScope.account.nameUsername}</strong>!
                    &nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/signout">Signout<a>
                    <br/>
                    <br/>
                    <a href="${pageContext.request.contextPath}/">Home<a>&nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/account/edit/${sessionScope.account.id}">Edit Profile<a>
                </shiro:authenticated>

                <shiro:guest>
                    <a href="${pageContext.request.contextPath}/signin">Signin</a>
                        or
                    <a href="${pageContext.request.contextPath}/signup">Signup</a> today!
                </shiro:guest>
            </div>


            <div id="content-mobile" style="display:none">
                <a href="${pageContext.request.contextPath}/home" id="logo-logo" style="font-size:71px;font-weight:500;text-decoration:none;">&Delta;&deg;
                </a>
            </div>

            <decorator:body />
        </div>

        <br class="clear"/>
    </div>


    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-40862316-16"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', '');
    </script>

</body>
</html>