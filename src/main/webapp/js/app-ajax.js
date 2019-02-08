    var api = '/hhtool2/services/';
    var respTab = '#ajaxGetUserServletResponse';

    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
    debugger;

    function loadTab(id, method, url, text) {
       let tab = $(id);
       let ul = $('#ajaxGetUserServletResponse>ul');
       let tabHeaders = ul.children();
       let tabName = method + '_tab_' + (tabHeaders.length+1);
       ul.append('<li><a href="#' + tabName +'">' + url + '</a><span class="ui-icon ui-icon-close" role="presentation">Remove Tab</span>');//<span class="ui-icon ui-icon-circle-close ui-closable-tab"></span>
       tab.append('<div id="' + tabName + '">' + text + '</div>')
       tab.tabs( "refresh" );
    }

    function bind(method){
            $('#' + method).click(function(event) {
                                    let obj = {};
                                    let template = $('#' + method + '_template');
                                    $('input[id^="' + method + '"][type=text]').each(function(i, el){
                                          obj[el.id.replace(method + '_', '')] = el.value
                                    })

                                    let url = api + method.replace('_', '/');
                                    $.get(url, obj, function(data) {
                                            if (!template.length) {
                                               // $('#ajaxGetUserServletResponse').text(JSON.stringify(data, null, 4));
                                                loadTab(respTab, method, url, JSON.stringify(data, null, 4));
                                            } else {
                                                loadTab(respTab, method, url, Mustache.to_html( template[0].innerHTML, data ));
                                               /*let tab = $('#ajaxGetUserServletResponse');
                                               let tabHeaders = tab.find('ul li');
                                               let tabName = method + '_tab_' + tabHeaders.length;
                                               tab.find('ul').append('<li><a href="#' + tabName +'">' + url + '</a></li>');
                                               let text =  Mustache.render( template[0].innerHTML, data );
                                               tab.append('<div id="' + tabName + '">' + text + '</div>')
                                               tab.tabs( "refresh" );*/
                                                 //$('#ajaxGetUserServletResponse').html();
                                            }
                                    });
                 });
    }


    $(document).ready(function() {
               $(respTab).tabs().on( "click", "span.ui-icon-close", function() {
                                          var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
                                          $( "#" + panelId ).remove();
                                          tabs.tabs( "refresh" );
                                        });


                bind('hh_loadLocations');
                bind('vk_loadRegions');
                bind('merge_mergeCountries');
                bind('merge_mergeRegions');
                bind('vk_searchLocation');



    });
