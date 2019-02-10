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
                                       {{^vkLocations.length}}
                                           <p>No Variants</p>
                                       {{/vkLocations.length}}
                                       {{#hhLocation.resolved}}
                                            Resolved
                                       {{/hhLocation.resolved}}
                                        {{^hhLocation.resolved}}
                                           NOT Resolved
                                        {{/hhLocation.resolved}}

                                       {{#vkLocations}}
                                                <input id="hh_{{hhLocation.id}}_param_{{id}}" name="id" type="radio" value="{{id}}">{{name}}</input>
                                       {{/vkLocations}}

                                       <br />


                                        <input class="button" type="button" id="merge_mapLocation" value="Map selected vk location to hh"
                                                               data-url-method="POST"
                                                               data-url="merge/mapLocation/{{id}}/"
                                                               data-parms-id="input[id^=hh_{{hhLocation.id}}_param_]:checked"
                                                               data-output-id="#hh_{{hhLocation.id}}_out"
                                                    />

                                         <div id="hh_{{hhLocation.id}}_out">
                                         </div>




                                       </td>

                                   </tr>
                              {{/items}}
                           </table>