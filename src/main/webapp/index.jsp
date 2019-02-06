
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>jQuery, Ajax and Servlet/JSP integration example</title>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.0.1/mustache.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/hhtool2/js/app-ajax.js"></script>

    <style>
        input {
            width:300px;
            }
         .template {
            display:none;
         }

    </style>
</head>
<body>

	<form>
	     <table>
	        <tr>
	            <td>&nbsp;</td>
		        <td><input class="button" type="button" id="hh_loadLocations" value="Load HH Locations" /></td>
		    </tr>
		    <tr>
		        <td> Country code: <input class="input" type="text" id="vk_loadRegions_id" value="RU" /> </td>
		        <td> <input class="button" type="button" id="vk_loadRegions" value="Load VK Regions" />
		            <div class="template" id="vk_loadRegions_template">
                        <ul>
                          <li>name: {{name}}</li>
                          <li>complete: {{complete}}</li>
                          <li>status: {{status}}</li>
                        </ul>

                	</div>
		        </td>
		    </tr>
		    <tr>
		         <td>&nbsp;</td>
                 <td><input class="button" type="button" id="merge_mergeCountries" value="Merge Countries" /></td>
		    </tr>

		     <tr>
                  <td> Country code: <input class="input" type="text" id="merge_mergeRegions_id" value="113" /> </td>
            	  <td> <input class="button" type="button" id="merge_mergeRegions" value="Merge child regions" /></td>
             </tr>

             <tr>
                   <td> <input class="input" type="text" id="vk_searchLocation_id" value="" /> </td>
                   <td> <input class="button" type="button" id="vk_searchLocation" value="Search VK location" /></td>
             </tr>
		  </table>

	</form>
	<br>
	<br>

	<strong>Ajax Response</strong>:
	<div id="ajaxGetUserServletResponse"></div>


</body>
</html>
