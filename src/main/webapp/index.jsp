<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>jQuery, Ajax and Servlet/JSP integration example</title>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <script src="https://code.jquery.com/jquery-1.12.4.js" type="text/javascript"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/mustache.js"
            type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/app-ajax.js"></script>

    <script id="default_template" type="text/mustache">
                           count = {{count}} <br />
                            {{#items}}
                                        {{id}} &nbsp;{{name}}  &nbsp;{{title}}<br />
                             {{/items}}


    </script>
    <script type="text/javascript">
         let binding = Binding;
         binding.apiBase('${pageContext.request.contextPath}/services/');
         binding.teplateBaseUrl('${pageContext.request.contextPath}/js/templates/');
         binding.controlsId('body');
         binding.outputId('#output');
         binding.defaultTemplateId('#default_template');
         binding.bind();
    </script>
</head>
<body>

<form id="controls">
    <table>
        <tr>
            <td>&nbsp;</td>
            <td><input class="button" type="button" id="hh_loadLocations" value="Load HH Locations"
                       data-url="hh/loadLocations"/></td>
        </tr>
        <tr>
            <td><label for="vk_loadRegions_id">Country code:</label><input class="input" name="id" type="text"
                                                                           id="vk_loadRegions_id" value="RU"/></td>
            <td><input class="button" type="button" id="vk_loadRegions" value="Load VK Regions"
                       data-url="vk/loadRegions"
                       data-parms-id="input[id^=vk_loadRegions]"
                       data-teplate-url="vk_loadRegions.tpl"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input class="button" type="button" id="merge_mergeCountries" value="Merge Countries"
                       data-url="merge/mergeCountries"
                       data-teplate-url="merge_mergeRegions.tpl"/></td>
        </tr>

        <tr>
            <td><label for="merge_mergeRegions_id"> Country code:</label><input class="input" name="id" type="text"
                                                                                id="merge_mergeRegions_id" value="113"/>
            </td>
            <td><input class="button" type="button" id="merge_mergeRegions" value="Merge child regions"
                       data-url="merge/mergeRegions"
                       data-parms-id="input[id^=merge_mergeRegions]"
                       data-teplate-url="merge_mergeRegions.tpl"
            /></td>
        </tr>

        <tr>
            <td><label for="vk_searchLocation_id">Паттерн:</label><input class="input" name="id" type="text"
                                                                         id="vk_searchLocation_id" value=""/></td>
            <td><input class="button" type="button" id="vk_searchLocation" value="Search VK location"
                       data-url="vk/searchLocation" data-parms-id="input[id^=vk_searchLocation]"/></td>
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
