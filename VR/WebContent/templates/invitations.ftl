<#include "header.ftl">

<h1>invitations.ftl</h1>



 
<#list invitations as inv>

	<div class="card mt-4">
		<h5 class="card-header">Deadline: ${inv.deadline}</h5>
  		<div class="card-body">
	  		<h5 class="card-title">${inv.name}</h5>
	  		<p class="card-text">${inv.description}</p>
			<a class="btn btn-primary" href="reply?action=selectAppointment&amp;aid=${inv.aid}" title="selectAppointment">Reply</a>
  		</div>




	</div>


	
</#list>




<#include "footer.ftl">