<#include "header.ftl">

<h1>invitations.ftl</h1>


<#list invitations as inv>
	<a href="reply?action=selectAppointment&amp;aid=${inv.aid}" title="selectAppointment">${inv.name}</a>
</#list>



<#include "footer.ftl">