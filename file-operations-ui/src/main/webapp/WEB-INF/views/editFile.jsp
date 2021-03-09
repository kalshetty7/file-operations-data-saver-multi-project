<%@ include file="header.jsp"%>


<style>
textarea
{
overflow-y: scroll;
height:700px;
}

</style>

<script>

function save(){
			let fileContent=document.querySelector('#content').value
			let fileName='${fileName}'
			let form = document.createElement('form');
			form.action="save"
			form.method="POST"
			form.hidden = true;
			
			let fileNameInput = document.createElement('input')
			fileNameInput.setAttribute("type", "text");
			fileNameInput.setAttribute("name", "fileName"); 
			fileNameInput.setAttribute("value", fileName); 
			form.appendChild(fileNameInput)
			
				let fileContentInput = document.createElement("textarea");
			fileContentInput.name = "fileContent";
			//fileContentInput.maxLength = "5000";
			fileContentInput.cols = "1000";
			fileContentInput.rows = "1000";
			fileContentInput.value=fileContent
			form.appendChild(fileContentInput)
			
			document.querySelector('body').appendChild(form)
			form.submit();
}

</script>
<body>
<b>
File : ${fileName}
</b>
<br/>
<button style="margin-left: 500px;margin-bottom: 10px" onclick="save()" >Save</button>
<br/>
   <textarea id="content" rows="400" cols="170">${fileContent}</textarea>
</body>

</html>