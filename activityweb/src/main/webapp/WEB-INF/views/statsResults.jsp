<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false"%>
<!DOCTYPE html>
<head>
<title>Home</title>
<script src="resources/scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#activityText").keyup(function() {
			showObjOnEmpty("activityText", "tooltip");
		});

		$("#activityText").focus(function() {
			showObjOnEmpty("activityText", "tooltip");
		});

		$("#activityText").blur(function() {
			hideObj("tooltip");
		});

		$("#recordSubmit").click(function() {
			$("#activityForm").attr("action", "record");
		});
		

		$("#exploreSubmit").click(function() {
			$("#activityForm").attr("action", "explore");
		});
		
		var responseType = '${responseType}';
		if(responseType=="record"){
			$("#recordText").show();
		} else if(responseType=="explore"){
			$("#exploreText").show();
		}

		var maxPopular = $(".popularListCanvas").get(0).title;
		$(".popularListCanvas").each(function() {

			var ctx = this.getContext("2d");
			var normWidth = (this.title * this.width) / maxPopular;
			var grd = ctx.createLinearGradient(0, 0, normWidth, 0);
			grd.addColorStop(0, "#F88017");
			grd.addColorStop(1, "#F88017");

			ctx.fillStyle = grd;
			ctx.fillRect(0, 0, normWidth, 15);

			ctx.fillStyle = "#800517";
			ctx.font = "italic normal bold 10px tahoma";
			ctx.fillText(this.title + " hits", 0, 10);

		});
		
		$(".popularListSentence").each(function(){
			sentence = $(this).attr("title");
			if(sentence.length>20){
				$(this).text(sentence.substring(0,20) + "...");
			} else {
				$(this).text(sentence);
			}
		});

	});

	function showObjOnEmpty(actingObject, reactingObject) {
		if ($("#" + actingObject).val().length == 0) {
			showObj(reactingObject);
		} else {
			hideObj(reactingObject);
		}
	}

	function showObj(objectId) {
		$("#" + objectId).fadeIn(2000);
	}

	function hideObj(objectId) {
		$("#" + objectId).fadeOut(2000);
	}
</script>
</head>
<body style="margin: 0; padding: 0;">
<div style="width:1800px; height:800px; margin:0 auto; text-align:left">
	<form id="activityForm" style="position:relative; top: 100px; left: 5%;">
		<p style="position: absolute; left:5%">
			<img src="<c:url value='/resources/images/Activity.png'/>"
				style="margin-right: 5px"></img>
		</p>

		<p style="position: absolute; top: 60px; left:4%">
			<input type="text" name="activityText" id="activityText"
				value="${activityText}"
				style="outline-color: #A0A0A0; position: absolute; margin: 15px; width: 450px; height: 30px; color: black; background: white; font-size: 15px; font-family: 'Verdana', Geneva, sans-serif;"></input>
		</p>

		<p style="position: absolute; top: 120px; left: 4.5%;">
			<input type="image"
				src="<c:url value='/resources/images/Record.png'/>"
				style="margin-right: 3px;" id="recordSubmit"></input>
		</p>

		<p style="position: absolute; top: 120px; left: 18%;">
			<input type="image"
				src="<c:url value='/resources/images/Explore.png'/>"
				style="margin-right: 3px;" id="exploreSubmit"></input>
		</p>

		<p style="position: absolute; top: 40px; left: 31%;">
			<img src="<c:url value='/resources/images/tooltip.png'/>"
				style="margin-right: 3px; display: none" id="tooltip"></img>
		</p>

		
		<p id="recordText"
		style="position: absolute; top: 180px; left: 5%; width: 480px; font-family: tahoma; font-size: large; display:none;">
		Thats Awesome! <span style="font-size: x-large; color: #810541">${totalHits}</span>
		people currently <span style="font-size: x-large; color: #810541">${activityText}</span>.
		Keep going, do let us know what you upto next!!
		</p>
	
		
		<p id="exploreText"
		style="position: absolute; top: 180px; left: 5%; width: 480px; font-family: tahoma; font-size: large; display:none;">
		Thinking about <span style="font-size: x-large; color: #810541">${activityText}</span>??
		<span style="font-size: x-large; color: #810541">${totalHits}</span> people currently doing it!!
		</p>
	
	</form>




	<div
		style="position: relative; top: 25%; left: 57%; width:480px; height:200px; color: #810541; font-family: tahoma;">
		
		<p style="position:absolute; top:1%; left:40%; font-size:x-large;">
			Current Trending
		</p>

		<p style="position: absolute; top: 22%; font-size: large;">
			Most Popular</p>

		<table
		style="position: absolute; top: 45%; color: black; border-top-style: solid; font-size: 85%;">
			<tr>
				<td style="width: 130px;"></td>
				<td style="width: 130px"></td>
			</tr>

			<c:forEach var="topActivity" items="${popularList}">
				<tr>
					<td style="width: 130px;  white-space:nowrap;" class="popularListSentence" title="${topActivity.sentence}"></td>
					<td style="width: 130px;">
						<canvas width="130" height="15" class="popularListCanvas"
							title="${topActivity.hits}"></canvas>
					</td>
				</tr>
			</c:forEach>
		</table>

			<p style="position: absolute; top: 22%; left:80%; font-size: large">
			Most Odd</p>

		<table
		style="position: absolute; top: 45%; left:80%; color: black; border-top-style: solid; font-size: 85%;">
			<tr>
				<td style="width: 130px;"></td>
				<td style="width: 130px"></td>
			</tr>

			<c:forEach var="topActivity" items="${oddList}">
				<tr>
					<td style="width: 130px; white-space:nowrap;" class="popularListSentence" title="${topActivity.sentence}"></td>
					<td style="width: 130px;">
						<canvas width="130" height="15" class="popularListCanvas"
							title="${topActivity.hits}"></canvas>
					</td>
				</tr>
			</c:forEach>
		</table>

	</div>
	
</div>
</body>
</html>
