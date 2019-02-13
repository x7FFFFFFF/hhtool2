  count = {{count}} <br />
                           <table>
                           <tr>
                                <td>hh.id</td>
                                <td>hh.name</td>
                                <td>vk.id</td>
                                <td>vk.name/td>
                                <td>hh.locationType</td>
                                <td>resolved</td>
                           </tr>

                              {{#items}}
                                   <tr>
                                           <td>{{hhLocation.id}}</td>
                                           <td>{{hhLocation.name}}</td>
                                           <td>{{vkLocation.id}}</td>
                                           <td>{{vkLocation.name}}</td>

                                           <td>{{locationType}}</td>
                                           <td>
                                                        {{^resolved}}
                                                                                      <span style="color:red">NOT Resolved!</span>
                                                        {{/resolved}}
                                                        {{#resolved}}
                                                                                            Resolved.
                                                        {{/resolved}}

                                           </td>






                                        <!--input class="button" type="button" id="merge_mapLocation" value="Map selected vk location to hh"
                                                               data-url-method="POST"
                                                               data-url="merge/mapLocation/{{id}}/"
                                                               data-parms-id="input[id^=hh_{{hhLocation.id}}_param_]:checked"
                                                               data-output-id="#hh_{{hhLocation.id}}_out"

                                                   <div id="hh_{{hhLocation.id}}_out">
                                                                                            </div>
                                                    /-->


                                   </tr>
                              {{/items}}
                           </table>