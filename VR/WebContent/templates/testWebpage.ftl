<#include "header.ftl">



<b>Welcome to our little demonstration on the VR Webapp</b><br><br>


<div class="card">
<div class="card-body">
<form method="POST" action="test?action=addAppointment">
	<fieldset id="browseAvailableOffers">
		<table style="width:100%">
		<tr>
			<td>Name</td>
			<td><input type="text" name="name"></td>
		</tr>
		<tr>
			<td>Participants</td>
			<td><input type="text" name="participants"/></td>
		</tr>
		<tr>
			<td>Dates</td>
			<td>
				<input type="hidden" id="numOfDates" name="numOfDates" value ="1">
				<div id=inputDates>
					<input type="date" name="date1"></br>
				</div>
				<button type="button" onclick="addDate()">+</button>
				<button type="button" onclick="removeDate()">-</button>
			</td>

		</tr>
		<tr>
			<td>Description</td>
			<td><input type="text" name="description"></td>
		</tr>
		<tr>
			<td>Location</td>
			<td><input type="text" name="location"></td>
		</tr>
		<tr>
			<td>Duration (in milliseconds)</td>
			<td><input type="text" name="duration"></td>
		</tr>
		<tr>
			<td>Deadline</td>
			<td><input type="date" name="description"></td>
		</tr>
		</table>
		
	</fieldset>
	<button type="submit" id="SelectHOWebpage" name="SelectHOWebpage" value="Submit">Submit!</button>
</form>
</div>
</div>



<h1>You're mom gay</h1>

<#include "footer.ftl">