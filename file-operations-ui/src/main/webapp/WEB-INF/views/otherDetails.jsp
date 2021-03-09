<%@ include file="header.jsp"%>
<script>
let e = JSON.parse(JSON.stringify(${o.e}));
console.log(e)
</script>
<body>
	<table class="table" style="width: 700px;margin-left: 330px;">
		<tr>
			<td colspan="2">${o.e.name }</td>
		</tr>
		<tr>
			<td>DOB</td>
			<td>${o.dob }</td>
		</tr>
		<tr>
			<td>Age</td>
			<td>${o.age }</td>
		</tr>
		<tr>
			<td>Job Switches</td>
			<td>${o.switches }</td>
		</tr>
	</table>
	
	<c:if test="${not empty o.jobDetails}">
				<table class="table">
				<tr><td>Sr.No.</td><td>Name</td><td>Joining Date </td><td>Last Date</td><td>Duration</td></tr>
				<c:forEach items="${o.jobDetails}" var="j" varStatus="counter">
				<tr><td>${counter.count}</td><td>${j.name }</td><td>${j.startDateString }</td><td>${j.endDateString }</td><td>${j.duration}</td></tr>
				</c:forEach>
				<tr><td colspan="4">Relevant Experience</td><td>${o.relevantExperience}</td></tr>
				<tr><td colspan="2" rowspan="2">Total Experience</td><td>Start Of Career</td><td>${o.e.startDateString}</td><td rowspan="2">${o.totalExperience}</td></tr>
				<tr><td>End Of Career</td><td>${o.e.endDateString}</td></tr>
				</table>
				</c:if>
</body>
</html>