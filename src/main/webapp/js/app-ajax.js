    var api = '/hhtool2/services/';
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
    debugger;
    function bind(method){
            $('#' + method).click(function(event) {
                                    let obj = {};
                                    let template = $('#' + method + '_template');
                                    $('input[id^="' + method + '"][type=text]').each(function(i, el){
                                          obj[el.id.replace(method + '_', '')] = el.value
                                    })



                                    let countryCode = $('#vk_loadRegions_countryCode').val();
                                    $.get(api + method.replace('_', '/'), obj, function(data) {
                                            if (!template.length) {
                                                $('#ajaxGetUserServletResponse').text(JSON.stringify(data, null, 4));
                                            } else {
                                                 $('#ajaxGetUserServletResponse').html( Mustache.render( template[0].innerHTML, data ));
                                            }
                                    });
                 });
    }


    $(document).ready(function() {
                bind('hh_loadLocations');
                bind('vk_loadRegions');
                bind('merge_mergeCountries');
                bind('merge_mergeRegions');
                bind('vk_searchLocation');
    });
