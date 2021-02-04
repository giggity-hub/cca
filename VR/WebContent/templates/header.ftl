<!DOCTYPE HTML>
<html lang='de' dir='ltr'>
<head>
	<meta charset="utf-8" />
	<title>Colaborative Calendar - ${pagetitle}</title>
  	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
  	<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  	
  	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
	<link type="text/css" href="css/style.css" rel="stylesheet" />
	
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  	<script>
  		$(function() {
    		$( "#datepicker2" ).datepicker(
    		{
    			minDate:'today',
    			
    		});
 
  			$("#datepicker1").datepicker({
  				minDate:'today',
    			onSelect: function (dateValue, inst) {
        			$("#datepicker2").datepicker("option", "minDate", dateValue)
    			}
			});
		});
		
		var pdcount = 1;
		function addDate(){
            pdcount += 1;
            var numpd = document.getElementById("numOfDates");
            numpd.value = pdcount;
			var container = document.getElementById("inputDates");
			var br = document.createElement("br");
			var input = document.createElement("input");
			input.type = "date";
            input.name = "date" + pdcount;
            container.appendChild(input);
            container.appendChild(br);
		}
		
		function removeDate(){
			if(pdcount>1){
				var container = document.getElementById("inputDates");
				container.removeChild(container.lastChild);
				container.removeChild(container.lastChild);
				pdcount-=1;
				 var numpd = document.getElementById("numOfDates");
            	numpd.value = pdcount;
			}
		}
  	</script>
</head>
<body class="d-flex flex-column">
<div id="page-content"> 
 
 <nav class="navbar navbar-expand navbar-dark bg-dark px-3">
  <div class="collapse navbar-collapse" id="navbarNav">
  <#if navtype == "signedIn">
    <ul class="navbar-nav">
      <li class="nav-item active">
        <a class="nav-link" href="index">
        	<i class="fa fa-home fa-lg"></i>
        </a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="calendar">Calendar</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="add">Add</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="reply?page=invitations">Reply</a>
      </li>
      <li class="nav-item">
          <a class="nav-link" href="index?action=logOut">Ausloggen</a>
      </li>
    </ul>
  <#else>
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link">Bitte gib mir eine User id:</a>
      </li>
      <li class="nav-item">
        <form method="POST" action="index?action=signin">
          <input type="text" name="userid">
          <button type="submit" id="signIn" name="singnIn" value="Submit">Ok</button>
        </form>
      </li>
    </ul
    </#if>>
  </div>
</nav>
 
 
 
 
 
<div id="content" class="container">
	
	
	
	
	
	
	
	