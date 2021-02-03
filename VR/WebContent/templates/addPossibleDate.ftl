<#include "header.ftl">

<h1>reply.ftl</h1>





<form method="POST" action="reply?action=postPossibleDates&amp;aid=${aid}">
	<input type="hidden" name="size" value= ${possibleDates?size} >
	
	<#list 0..possibleDates?size-1 as i>
		<input type="checkbox" name="${possibleDates[i]}" /> ${possibleDates[i]} 
		</br>
	</#list>
	
	<button class="btn btn-primary" type="submit" id="addPossibleDates" name="addPossibleDates" value="Submit">Submit!</button>
</form>


<#include "footer.ftl">