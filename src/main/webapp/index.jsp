
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
         .ui-icon-circle-close {
             cursor:pointer;
         }
           #ajaxGetUserServletResponse { margin-top: 1em; }
           #ajaxGetUserServletResponse li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }

    </style>
    <script id="vk_loadRegions_template" type="text/mustache">
                count = {{count}} <br />
                <table>
                <tr>
                     <td>id</td>
                     <td>title</td>
                </tr>

                   {{#items}}
                        <tr>
                            <td>{{id}}</td>
                            <td>{{title}}</td>

                        </tr>
                   {{/items}}
                </table>

     </script>
      <script id="merge_mergeCountries_template" type="text/mustache">
                     count = {{count}} <br />
                     <table>
                     <tr>
                          <td>hhLocation.id</td>
                          <td>hhLocation.name</td>
                          <td>hhLocation.resolved</td>
                          <td>hhLocation.locationType</td>
                          <td>vklocations</td>
                     </tr>

                        {{#items}}
                             <tr>
                                 <td>{{hhLocation.id}}</td>
                                 <td>{{hhLocation.name}}</td>
                                 <td>{{hhLocation.resolved}}</td>
                                 <td>{{hhLocation.locationType}}</td>
                                 <td>
                                        {{#vkLocations}}
                                                {{id}}  {{name}} <br />

                                        {{/vkLocations}}

                                 </td>

                             </tr>
                        {{/items}}
                     </table>

      </script>
      <script id="merge_mergeRegions_template" type="text/mustache">
                           count = {{count}} <br />
                           <table>
                           <tr>
                                <td>hhLocation.id</td>
                                <td>hhLocation.name</td>
                                <td>hhLocation.resolved</td>
                                <td>hhLocation.locationType</td>
                                <td>vklocations</td>
                           </tr>

                              {{#items}}
                                   <tr>
                                       <td>{{hhLocation.id}}</td>
                                       <td>{{hhLocation.name}}</td>
                                       <td>{{hhLocation.resolved}}</td>
                                       <td>{{hhLocation.locationType}}</td>
                                       <td>
                                              {{#vkLocations}}
                                                      {{id}}  {{name}} <br />

                                              {{/vkLocations}}

                                       </td>

                                   </tr>
                              {{/items}}
                           </table>



    </script>

    <script id="hh_loadLocations_template" type="text/mustache">
                           count = {{count}} <br />



    </script>
    <script type="text/javascript">
        Binding.apiBase('/hhtool2/services/').bind('#controls', '#output');
    </script>
</head>
<body>

<form id="controls">
    <table>
        <tr>
            <td>&nbsp;</td>
            <td><input class="button" type="button" id="hh_loadLocations" value="Load HH Locations"
                       data-output-id="output"
                       data-url="hh/loadLocations" data-parms-id="id^=hh_"
                       data-teplate-id="#hh_loadLocations_template"/></td>
        </tr>
        <tr>
            <td> Country code: <input class="input" type="text" id="vk_loadRegions_id" value="RU"/></td>
            <td><input class="button" type="button" id="vk_loadRegions" value="Load VK Regions"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input class="button" type="button" id="merge_mergeCountries" value="Merge Countries"/></td>
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
<div id="output">
    <ul></ul>


    </div>


</body>

</html>
