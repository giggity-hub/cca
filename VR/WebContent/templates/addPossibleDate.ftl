<#include "header.ftl">

<h1>Zusagen oder Absagen</h1>

<form method="POST" action="reply?action=postPossibleDates&amp;aid=${aid}">
	<input type="hidden" name="size" value= ${possibleDates?size} >
	
	<h5>Possible Dates</h5>
	<div class="form-group">
	<#list 0..possibleDates?size-1 as i>
	
		<div class="form-check">
          <input class="form-check-input" type="checkbox" name="pddate${i}" value="${possibleDates[i].date}" <#if isMarked[i]>checked</#if>>
          <label class="form-check-label" >
            ${possibleDates[i].date}
          </label>
        </div>
	</#list>
	
	</div>
	
	<h5>Alternative Dates</h5>
	<div class="form-group">
		<input type="hidden" id="numOfDates" name="numOfDates" value ="1">
		<div id=inputDates>
			<input type="date" name="date1"></br>
		</div>
		<button class="btn btn-secondary btn-sm my-2" type="button" onclick="addDate()">+</button>
		<button class="btn btn-secondary btn-sm my-2" type="button" onclick="removeDate()">-</button>
	</div>

		
<button class="btn btn-primary" type="submit" id="addPossibleDates" name="addPossibleDates" value="Submit">Submit!</button>							
</form>


<#include "footer.ftl">