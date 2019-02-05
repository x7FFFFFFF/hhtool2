$(document).ready(function() {
        $('#loadHHLocations').click(function(event) {
                //var name = $('#userName').val();
                $.get('/services/hh/loadLocations', {

                }, function(responseText) {
                        $('#ajaxGetUserServletResponse').text(responseText);
                });
        });
});
