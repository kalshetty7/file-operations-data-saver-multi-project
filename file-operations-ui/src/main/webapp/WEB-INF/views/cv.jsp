<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>${e.resumeName }</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/cv.css" />
<script src="${pageContext.request.contextPath}/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/cv.js"></script>
<style type="text/css">
.table-bordered td, .table-bordered th {
	border-color: black !important;
}
</style>
</head>
<body>
	<div style="margin-top: 25px"
		class="container shadow-lg p-4 mb-4 bg-white">
		<h1 class="text-left">${e.name }</h1>
		<p style="width: 450px" class="font-italic text-left">${e.address }</p>
		<table class=".table-borderless">
			<tr>
				<td><strong>Email : </strong></td>
				<td></td>
				<td class="text-right">${e.email }</td>
			</tr>
			<tr>
				<td><strong>Contact : </strong></td>
				<td></td>
				<td>${e.mobile }</td>
			</tr>
		</table>
	</div>


	<div class="container shadow-lg p-4 mb-4 bg-white">
		<h2 class="text-left">Summary</h2>
		<l:lister content="${e.summary}" relevantExperience="${o.relevantExperience}" ></l:lister>
	</div>

	<c:if test="${not empty e.skills}">
		<div class="container shadow-lg p-4 mb-4 bg-white">
			<h2 class="text-left">Technical Skills</h2>
			<table class="table skills"
				style="padding-top: 12px; padding-left: 15px">
				<c:forEach items="${e.skills}" var="s" varStatus="counter">
					<tr>
						<td>${s.name }</td>
						<td>${s.value }</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>

	<c:if test="${not empty e.companies}">
		<div class="container shadow-lg p-4 mb-4 bg-white">
			<h2 class="text-left">Work Experience</h2>

			<table class="table" style="margin-top: 2%">
				<c:forEach items="${e.companies}" var="c" varStatus="cin">
					<tr>
						<td colspan="2">
							<h5>${c.name }</h5>
							<div class="text-left"><strong>${c.address}</strong></div>
						</td>
						<td class="text-right"><strong>${c.startDateString} - ${c.endDateString}€“</strong></td>
					</tr>
					<tr>
						<td>Job Profile :</td>
						<td></td>
						<td>${c.jobProfile }</td>
					</tr>
					<%--start : project details --%>
					<c:if test="${not empty c.projects}">
					<c:forEach items="${c.projects}" var="p" varStatus="pin">
					<tr>
						<td>Domain :</td>
						<td></td>
						<td>${p.domain }</td>
					</tr>
					<tr>
						<td>Project Title :</td>
						<td></td>
						<td>${p.name }</td>
					</tr>
					<tr>
						<td>Client :</td>
						<td></td>
						<td>${p.clientDetails }</td>
						<td></td>
					</tr>
					<tr>
						<td>Team Size :</td>
						<td></td>
						<td>${p.teamSize }</td>
					</tr>
					<tr>
						<td>Duration :</td>
						<td></td>
						<td>${p.startDateString } - ${p.endDateString }</td>
					</tr>
					<tr>
						<td>S/W & Tools :</td>
						<td></td>
						<td>${p.tools }</td>
					</tr>
					<c:if test="${not empty p.rolesAndResponsibilities}">
					<tr>
						<td>Roles & Responsibilities :</td>
						<td></td>
						<td><l:lister content="${p.rolesAndResponsibilities }"></l:lister></td>
					</tr>
					</c:if>
					<tr>
						<td>Project Summary :</td>
						<td></td>
						<td><l:lister content="${p.summary }"></l:lister></td>
					</tr>
					</c:forEach>
					</c:if>
					<%--end : project details --%>
				</c:forEach>
			</table>

			<hr />
		</div>
	</c:if>

	<!--education-->
	<div class="container shadow-lg p-4 mb-4 bg-white text-uppercase">
		<h2>education</h2>
		<h4>state board</h4>
		<table class="table">
			<thead>
				<tr>
					<th>sr. no.</th>
					<th>exam</th>
					<th>year</th>
					<th>institute</th>
					<th>board/university</th>
					<th>percentage</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>1</td>
					<td>ssc</td>
					<td>2004</td>
					<td>icl</td>
					<td>mumbai</td>
					<td>71.2</td>
				</tr>
				<tr>
					<td>2</td>
					<td>hsc</td>
					<td>2006</td>
					<td>icl</td>
					<td>mumbai</td>
					<td>68.17</td>
				</tr>
			</tbody>
		</table>

		<h4>BE(IT)</h4>
		<div class="text-capitalize">Mumbai University, A.C.patil
			college of engg, 2006-2010</div>
		<table class="table">
			<thead>
				<tr>

					<th>year</th>
					<th>percentage</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>first</td>

					<td>51.077</td>
				</tr>
				<tr>
					<td>second</td>

					<td>48.8</td>
				</tr>
				<tr>
					<td>third</td>

					<td>50.22</td>
				</tr>
				<tr>
					<td>fourth(final)</td>

					<td>57.2</td>
				</tr>
			</tbody>
		</table>
		</fieldset>
		</fieldset>
	</div>




	<div class="container shadow-lg p-4 mb-4 bg-white">
		<h2>Hobbies & Other Interests</h2>
		<l:lister content="${e.hobbies }"></l:lister>
	</div>


	<div class="container shadow-lg p-4 mb-4 bg-white">
		<h2>Personal Details</h2>
		<table class="table text-capitalize">
			<tr>
				<td>date of birth :</td>
				<td><fmt:formatDate pattern = "dd MMM yyyy" value = "${e.dob}" /></td>
			</tr>
			<tr>
				<td>languages known :</td>
				<td>${e.languagesKnown }</td>
			</tr>
		</table>
	</div>

</body>
</html>