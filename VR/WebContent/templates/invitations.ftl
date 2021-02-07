<#include "header.ftl">

<h1>invitations.ftl</h1>


<#if appointments?size == 0>
	Keine neuen Invitations
</#if>
 
 <#list appointments as apt>
 	<div class="card mt-4">
		<h5 class="card-header">Deadline: ${apt.deadline}</h5>
  		<div class="card-body">
	  		<h5 class="card-title">
	  			${apt.name}
	  			<#if apt.isFinal><span class="badge bg-success">Success</span></#if>
	  		</h5>
	  		<p class="card-text">${apt.description}</p>
			<a class="btn btn-primary" href="reply?action=selectAppointment&amp;aid=${apt.aid}" title="selectAppointment">Reply</a>
  		</div>
	</div>
 </#list>
 
 

<#include "footer.ftl">