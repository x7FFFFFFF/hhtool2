
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jQuery, Ajax and Servlet/JSP integration example</title>

<script src="https://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script type="text/javascript">
    var api = '/hhtool2/services/';
    $(document).ready(function() {
            $('#loadHHLocations').click(function(event) {
                    //var name = $('#userName').val();
                    $.get(api +'hh/loadLocations', {

                    }, function(responseText) {
                            $('#ajaxGetUserServletResponse').text(responseText);
                    });
            });
            $('#loadVkRegions').click(function(event) {
                                let countryCode = $('#countryCode').val();
                                $.get(api +'vk/loadRegions', {
                                    id:countryCode
                                }, function(responseText) {
                                        $('#ajaxGetUserServletResponse').text(responseText);
                                });
             });


    });

</script>
</head>
<body>

	<form>
		 <input type="button" id="loadHHLocations" value="Load HH Locations" /><br />
		  Country code: <input type="text" id="countryCode" /> <input type="button" id="loadVkRegions" value="Load VK Regions" />

	</form>
	<br>
	<br>

	<strong>Ajax Response</strong>:
	<div id="ajaxGetUserServletResponse"></div>
</body>
</html>
