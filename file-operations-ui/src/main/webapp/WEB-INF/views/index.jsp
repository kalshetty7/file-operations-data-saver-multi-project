<%@ include file="header.jsp"%>

<script>

function backup(){
			let ids = new Array();
			let al = document.querySelectorAll('.found-record');
			al.forEach((i)=>{
				if(i.checked)
					ids.push(i.getAttribute('value'));
			})
			let form = document.querySelector('form');
			form.action="backup"
			form.method="POST"
			form.hidden = true;
			let input = document.createElement('input')
			input.setAttribute("type", "text");
			input.setAttribute("name", "selectedValues"); 
			input.setAttribute("value", ids); 
			form.appendChild(input)
			document.querySelector('body').appendChild(form)
			form.submit();
}

function deleteRecords(){
	let ids = new Array();
	let al = document.querySelectorAll('.found-record');
	al.forEach((i)=>{
		if(i.checked)
			ids.push(i.getAttribute('value'));
	})
	let form = document.querySelector('form');
	form.action="delete"
	form.method="POST"
	form.hidden = true;
	let input = document.createElement('input')
	input.setAttribute("type", "text");
	input.setAttribute("name", "selectedValues"); 
	input.setAttribute("value", ids); 
	form.appendChild(input)
	document.querySelector('body').appendChild(form)
	form.submit();
}

function reset(e){
	e.preventDefault();
	let form = document.querySelector('form');
	form.action="reset"
		form.submit();
}

function search(){
	let form = document.querySelector('form');
	form.action="search"
		form.submit();
}

function restore(e){
	e.preventDefault();
	let form = document.querySelector('form');
	form.action="restore"
		form.submit();
}

function checkAll(targetClass){
	let al = document.querySelectorAll('.'+targetClass);
	al.forEach((i)=>{
		i.click();
	})
}

</script>
<body>
    <form:form action="/"  modelAttribute="searchCriteria">
        Working Directory : <form:input path = "workingDir" size="100"/>
        <br>
        File or Folder Names search : <form:input path = "fileOrFolderNames" />&nbsp;&nbsp;&nbsp;<form:checkboxes items = "${searchTypes}" path = "searchTypes" />
        <br>
        Backup Path : <form:input path = "backupPath" />&nbsp;&nbsp;&nbsp;&nbsp;<button onclick="restore(event)">Restore</button>
        <br>
   </form:form>
   <button onclick="search()">Search</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button onclick="reset(event)">Reset</button>
        <br>
   
   <c:if test="${not empty searchCriteria.foundRecords}">
   <div>
   Found Records : ${searchCriteria.foundRecords.size()}
   </div>
        <table>
        <tr>
        <td>Sr.No.</td><td>Path</td><td>Select All : <input type="checkbox" onclick="checkAll('found-record')" /><br/><input type="button" onclick="backup()" value="Backup" />&nbsp;/&nbsp;<input type="button" onclick="deleteRecords()" value="Delete" /></td>
        </tr>
        <c:forEach items="${searchCriteria.foundRecords}" var="r" varStatus="rcount">
        <tr>
        <td>${rcount.count }</td><td><a href="${pageContext.request.contextPath}/editFile?value=${r}" target="_blank">${r}</a></td><td><input class="found-record" value="${r}" type="checkbox" /></td>
        </tr>
        </c:forEach>
        </table>
        </c:if>

</body>

</html>