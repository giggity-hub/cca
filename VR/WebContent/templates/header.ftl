<!DOCTYPE HTML>
<html lang='de' dir='ltr'>
<head>
	<meta charset="utf-8" />
	<title>Colaborative Calendar - ${pagetitle}</title>
	<!-- <link type="text/css" href="css/style.css" rel="stylesheet" media="screen" /> -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" />
  	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
  	<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  	
  	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
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
<body>
<div id="wrapper">
	<div id="logo">Collaborative Calendar<br>Software Engineering Example</div>
    <ul id="navigation">
    	<li><a href="index" title="Index">View Homesite</a></li>
	<#if navtype == "guest">
    	<li><a href="guestgui?page=defaultwebpage" title="Search Offers">Search Offers</a></li>	
	<#elseif navtype == "staffmember">
		<li><a href="staffmembergui?page=insertoffer" title="Insert Offer">Insert Offer</a></li>
	<#else>
    	<li><a href="staffmembergui" title="Staffmember">Staffmember</a></li>
		<li><a href="guestgui" title="guest">Guest</a></li>
	</#if>
    </ul>
	<div id="content">