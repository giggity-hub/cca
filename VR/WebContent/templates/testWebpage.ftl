<#include "header.ftl">

<b>Welcome to our little demonstration on the VR Webapp</b><br><br>


<div class="card">
<div class="card-body">
<form method="POST" action="test?action=addAppointment">
	<fieldset id="browseAvailableOffers">
		dates
		<input type="date" name="dates">
		participants
		<input type="text" name="participants" placeholder="1,2,3" />
		description
		<input type="text" name="description" placeholder="description">
		name
		<input type="text" name="name" placeholder="max mustermann">
		location
		<input type="text" name="location" placeholder="Bundestag">
		duration (in milliseconds)
		<input type="text" name="duration" placeholder="50">
		deadline
		<input type="date" name="description">
		
		
	</fieldset>
	<button type="submit" id="SelectHOWebpage" name="SelectHOWebpage" value="Submit">Submit!</button>
</form>
</div>
</div>



<h1>You're mom gay</h1>

<#include "footer.ftl">