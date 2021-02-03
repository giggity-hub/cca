<#include "header.ftl">

<h1>Deine Termine</h1>
</br>

<table id="appointments">
	<tr>
		<th>#</th>
		<th>Name</th>
		<th>Beschreibung</th>
		<th>Ort</th>
		<th>Dauer</th>
		<th>Daten</th>
		<th>Deadline</th>
	</tr>
	<#list appointments as apt>
	<tr>
		<td><a href="reply?action=selectApointment&amp;aid=${apt.aid}" title="Make Booking">${apt.name}</a></td>
		<td>${apt.description}</td>
		<td>${apt.location}</td>
		<td>${apt.duration}</td>
		<table id="appointments">
		<#list appointments.possibleDates as pd>
			<tr><td>${pd.date}</td></td>
		</#list>
		</table>
		<td>${apt.deadline}</td>
	</tr>
	</#list>
</table>
<#include "footer.ftl">