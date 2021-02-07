<#include "header.ftl">

<#if appointments?size == 0>
<h1>Keine Termine in der Datenbank</h1>
<#else>
<h1>Deine Termine</h1>
</br>
<table id="appointments" class="table">
	<thead>
		<tr>
		<th >Name</th>
		<th >Beschreibung</th>
		<th >Ort</th>
		<th >Dauer</th>
		<th >Daten</th>
		<th>Deadline</th>
	</tr>
	</thead>
	<#list appointments as apt>
	<tr class="">
		<th>
		${apt.name}
		<#if apt.isFinal>
			<span class="badge bg-success">Final</span>
		<#else>
			<span class="badge bg-secondary">Not Final</span>
		</#if>
		</th>
		<td>${apt.description}</td>
		<td>${apt.location}</td>
		<td>${apt.duration}</td>
		<td>
		<ul class="list-group list-group-flush">
			<#list apt.possibleDates as pd>
				<li class="list-group-item">${pd.date}</li>
			</#list>
		</ul>
		<#if !apt.isFinal>
			<div class="d-grid gap-2">
			<a href="reply?action=selectAppointment&amp;aid=${apt.aid}" title="Make Booking" class="btn btn-primary btn-sm">edit dates</a>
			</div>
		</#if>
		</td>
		<td>${apt.deadline}</td>
	</tr>
	</#list>
</table>
</#if>
<br>
<#include "footer.ftl">

