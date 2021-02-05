<#include "header.ftl">

<#if appointments?size == 0>
<h1>Keine Termine in der Datenbank</h1>
<#else>
<h1>Deine Termine</h1>
</br>
<table id="appointments" style="width:100%;">
	<tr>
		<th style="width:15%;">Name</th>
		<th style="width:30%;">Beschreibung</th>
		<th style="width:20%;">Ort</th>
		<th style="width:10%;">Dauer</th>
		<th style="width:15%;">Daten</th>
		<th style="width:15%;">Deadline</th>
	</tr>
	<#list appointments as apt>
	<tr style="background-color:<#if apt.isFinal>lightgreen<#else>lightcoral</#if>">
		<td><#if !apt.isFinal><a href="reply?action=selectAppointment&amp;aid=${apt.aid}" title="Make Booking"></#if>${apt.name}<#if !apt.isFinal></a></#if></td>
		<td>${apt.description}</td>
		<td>${apt.location}</td>
		<td>${apt.duration}</td>
		<td><table id="appointments">
		<#list apt.possibleDates as pd>
			<tr style="border-bottom:none; line-height:20pt"><td>${pd.date}</td></td>
		</#list>
		</table></td>
		<td>${apt.deadline}</td>
	</tr>
	</#list>
</table>
</#if>
<br>
<#include "footer.ftl">