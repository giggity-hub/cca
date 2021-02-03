<#include "header.ftl">

<h1>reply.ftl</h1>





<form method="POST" action="reply?action=postPossibleDates&amp;aid=${aid}">
	<input type="hidden" name="size" value= ${possibleDates?size} >
	
	<#list 0..possibleDates?size-1 as i>
		<input type="checkbox" name="${possibleDates[i]}" /> ${possibleDates[i]} 
		</br>
	</#list>
	
	
	
	
	
	
	
Alternative Dates </br>
	
<input type="hidden" id="numOfDates" name="numOfDates" value ="1">
<div id=inputDates>
	<input type="date" name="date1"></br>
</div>


<button type="button" onclick="addDate()">+</button>
<button type="button" onclick="removeDate()">-</button>
				
				
</br> </br>				
<button class="btn btn-primary" type="submit" id="addPossibleDates" name="addPossibleDates" value="Submit">Submit!</button>							
</form>


<#include "footer.ftl">