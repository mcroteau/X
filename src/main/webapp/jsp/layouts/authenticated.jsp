<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.ioc.model.Account" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<!doctype html>
<html>
<head>
	<title>&Delta;&deg; Sigma90 : Music Driven</title>
	
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/Sigma90-logo.png">

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/Request.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/WebForm.js"></script>

    <script type="text/javascript">

        var progressBar = {}

        function getRandomInt(min, max) {
            min = Math.ceil(min);
            max = Math.floor(max);
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }

        var songs = [];

        var playing;

        var track = 0;
        var nextSong = 0;
        var songDisplay = 1;
        var musicPlayer = null;


        var req = new Request("${pageContext.request.contextPath}");
        var web = new WebForm();

        function initializeProgressBar(){

            if(progressBar && progressBar != "" && (Object.entries(progressBar).length != 0 && progressBar.constructor === Object) ){
                progressBar.destroy()
            }

            if(isEmptyObj(progressBar)){

                progressBar = new ProgressBar.Line('#progress-bar-top', {
                    easing: 'easeInOut',
                });

                progressBar.animate(1, function(){
                    progressBar.destroy()
                    progressBar = {}
                });
            }

        }
    </script>

    <link href="https://fonts.googleapis.com/css?family=Roboto:200,300,400,500,600,700&display=swap" rel="stylesheet">
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/progress-bar.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/js.cookies.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/mustache.js"></script>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/lib/pure.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css?v=<%=System.currentTimeMillis()%>">

    <style type="text/css">
        html, body, h1, h2, h3,
        p, span, table, th, td, 
        div, select, input{
            font-family: Arial;
            font-weight:200;
        }

        h1, h2, h3{
            font-weight:700;
        }

        #logo-logo{
            font-weight:500;
        }

        @media screen and (max-width: 590px) {
            #top-outer-container{
                width:100%;
                height:141px;
                text-align:center;
            }
            #top-inner-container{
                width:100%;
            }
            #logo-container{
                width:auto;
                float:left;
                text-align:center;
                position:relative;
            }
            #logo-logo{

            }
            #navigation-container{
                float:left;
                margin-top:16px;
                position:relative;
                clear:both;
            }
            #music-player-container{
                top:0px;
                right:0px;
                float:left;
                position:relative;
            }
            #music-container{
                float:left;
                position:relative;
            }
            #content-container{
                width:100%;
                text-align:center;
            }
            #search-container{
                top:0px;
                left:0px;
                float:left;
                position:relative;
                clear:right;
                margin-top:5px;
            }
            #search-box{
                float:left;
                left:0px;
                top:0px;
                width: 192px;
                position:relative;
            }
            #search-button{
                top:0px;
                left:0px;
                margin-top:0px;
                margin-left:-59px;
                float:left;
                position:relative;
            }
            #feed-href,
            #music-href,
            #messages-href {
                float:left;
                right:0px;
                top:9px;
                width:52px;
                text-align:center;
                display:inline-block;
                position:relative;
            }
            #profile-actions-href{
                float:left;
                position:relative;
            }
            #playlist-popup{
                display:none !important;
            }
            #news-feed{
                text-align:left;
                margin:0px auto;
                width:100%;
            }
            #posts-header{
                text-align:center;
                margin:21px auto 10px auto !important;
            }
            #feed{
                text-align:center;
            }
            .feed-content-container{
                text-align:left;
                margin-left:auto;
                margin-right:auto;
                width:89%;
            }
            #special-something{
                display:none;
            }
            .share-container{
                display:none;
            }
            .post-comment-form-container{
                display:none;
            }
        }

        .page{
            display:none;
        }

        .href{
            color:#1f9bc4;
            font-family:Arial, Sans;
            text-decoration:none;
            font-weight:normal;
            margin-top:11px;
            margin-right:20px;
        }

        textarea{
            font-family: Helvetica, Arial;
        }

        #posts-header{
            margin:21px 0px 10px 0px;
        }
        #special-something-container{
            float:right;
            width:450px !important;
            width: 417px !important;
        }
        #special-something{
            width:inherit !important;
        }

        #profile-picture-actions-container{
            right:0px;
            width:137px;
            margin-top:52px;
            position:absolute;
            background:#fff;
            z-index:13;
            -webkit-box-shadow: 1px 3px 12px 0px rgba(214,214,214,0.7);
            -moz-box-shadow: 1px 3px 12px 0px rgba(214,214,214,0.7);
            box-shadow: 1px 3px 12px 0px rgba(214,214,214,0.7);
        }
        #profile-picture-actions-container a{
            display:block;
            padding:20px 30px;
            text-decoration:none;
        }
        #profile-picture-actions-container a:hover{
            background:#f8f8f8;
        }

        #playlist-popup{
            margin-top:52px;
            right:192px;
            position:absolute;
            background:#E34B86 !important;
            z-index:11113;
            width:520px;
            padding:10px 30px 67px 30px;
            -webkit-border-radius: 13px;
            -moz-border-radius: 13px;
            border-radius: 13px;
            -webkit-box-shadow: 1px 3px 12px 0px rgba(214,214,214,0.7);
            -moz-box-shadow: 1px 3px 12px 0px rgba(214,214,214,0.7);
            box-shadow: 1px 3px 12px 0px rgba(214,214,214,0.7);
            -webkit-animation-name: playlist-animation; /* Safari 4.0 - 8.0 */
            -webkit-animation-duration: 19s; /* Safari 4.0 - 8.0 */
            animation-name: playlist-animation;
            animation-iteration-count: infinite;
            animation-duration: 19s;
        }

        /* Safari 4.0 - 8.0 */
        @-webkit-keyframes playlist-animation {
          0%  {background: #E34B86;}
          51%  {background: #12ADFD;}
          100%  {background: #E34B86;}
        }

        @keyframes playlist-animation {
          0%  {background: #E34B86;}
          51%  {background: #12ADFD;}
          100%  {background: #E34B86;}
        }

        #playlist-container{
            margin:0px !important;
            float:none !important;
            width:inherit !important;
        }

        #playlist-container tr{
            padding:5px 0px;
        }

        #playlist-container tr:hover{
            background:#fbf35a;
            cursor:pointer;
            cursor:hand;
        }

        #playlist-container td{
            color:#fff;
            font-size:13px;
            padding:2px 0px;
        }

        #playlist-popup h3{
            color:#fff;
            margin: 20px 0px 10px 0px !important;
        }

        .track-row.progress{
            background:#fbf35a !important;
        }

        .arrow-up{
            width: 0px;
            height: 0px;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            border-bottom: 5px solid #fff;
        }
    </style>

	<decorator:head />

</head>
<body>

    <div id="mock" style="display:none"></div>

	<div id="progress-bar-top"></div>

	<div id="love" style="display:none">
		<div id="opacity"></div>
		<div id="content">
			<h1>Thank you</h1>
			<img src="${pageContext.request.contextPath}/images/african-children.jpg"/><br/>
			<a href="javascript:" id="close-love" class="button yella">Close</a>
		</div>
	</div>

	<div id="processing-overlay">
	    <img src="${pageContext.request.contextPath}/images/processing.gif" style="height:50px; width:50px;"/>
	</div>



	<div id="layout-container" >

        <div id="content-mobile" style="display:none">
            <h1>&inifi; SigmaX</h1>
            <p>Working on a mobile version</p>
        </div>

		<div id="top-outer-container">

			<div id="top-inner-container">

                <div id="profile-picture-actions-container" style="display:none" data-id="${sessionScope.account.id}">
                    <a href="javascript:" id="profile-href" data-id="${sessionScope.account.id}">Profile</a>
                    <a href="javascript:" id="invites-href" data-id="${sessionScope.account.id}"><span id="invites-total">0</span> Invites</a>
                    <a href="${pageContext.request.contextPath}/signout">Logout</a>
                </div>



                <div id="playlist-popup" style="display:none">
                    <div class="arrow-up right-float" style="margin-right:100px;"></div>
                    <br class="clear"/>
                    <h3>Playlist</h3>
                    <table id="playlist-container"></table>
                </div>

				<div id="logo-container" class="float-left">
					<a href="javascript:" id="logo-logo" style="margin:-1px 0px 0px 0px; padding:0px; line-height:1.0; vertical-align:middle; text-decoration:none;">&infin;</a>
				</div>


				<div id="search-container" class="float-left">
                    <input type="text" class="input-text" id="search-box" style="border:solid 1px #c7ecfd;"/><input type="button" id="search-button" name="search-button" value="o">
				</div>

				<div id="navigation-container" class="float-right">

					<div id="music-player-container">

					    <a href="javascript:" id="no-music-button" style="display:none">+ Add Music</a>

						<div id="music-player">

						    <div id="song-count-container" class="float-left">
                                <span id="current-song"></span>/<span id="song-count"></span>
							</div>

                            <!--<a href="javascript:" id="add-music-button">+</a>-->

                            <a href="javascript:" class="button sky music-player-action play" playing="false" id="music-play-button">&triangleright;</a>
                            
                            <a href="javascript:" class="button sky music-player-action" id="next-song">&raquo;</a>

							<span id="duration">03:40</span>

						</div>



					</div>

					<a href="javascript:" class="navigation-href" id="feed-href">F
						<span id="latest-feed-total" class="notifications-count">0</span>
					</a>

					<a href="javascript:" class="navigation-href" id="music-href">M<span style="font-size:13px;"></span></a>

					<a href="javascript:" class="navigation-href" id="messages-href">m
						<span id="latest-messages-total" class="notifications-count"></span>
                    </a>

					<a href="javascript:" id="profile-actions-href">
					    <img src="${pageContext.request.contextPath}/${sessionScope.imageUri}" id="profile-ref-image" style="z-index:1"/>
					    <span id="friend-invites-count">0</span>
					</a>

				</div>

				<br class="clear"/>

			</div>

		</div>


		<div id="content-container" >

            <decorator:body />

		</div>

	</div>


    <br class="clear"/>

    <div id="site-refs" style="text-align:center;margin-top:50px;">
        <a href="javascript:" class="page-ref" data-ref="about">About</a>
        <a href="javascript:" class="page-ref" data-ref="donate">Donate</a>
        <a href="${pageContext.request.contextPath}/issues/report">Report an Issue</a>
    </div>
    <h1 style="text-align:center;">Sigma90</h1>
	<p style="margin-bottom:560px; text-align: center">&copy;</p>





<script async src="https://www.googletagmanager.com/gtag/js?id=UA-40862316-16"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', '');
</script>

</body>
</html>

