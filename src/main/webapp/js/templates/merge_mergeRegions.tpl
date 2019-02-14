  count = {{count}} <br />
                           <table>
                           <tr>
                                <td>hh.id</td>
                                <td>hh.name</td>
                                <td>vk.id</td>
                                <td>vk.name/td>
                                <td>hh.locationType</td>
                                <td>resolved</td>
                                <td>Action</td>
                                <td>Result</td>
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
                                           <td>
                                                          {{^resolved}}
                                                                <input class="button" type="button"
                                                                 id="merge_mapLocation_{{id}}"
                                                                 value="map"
                                                                 data-url-method="POST"
                                                                 data-url="merge/mapLocation/{{id}}"
                                                                 data-output-id="#merge_out_{{id}}"/>
                                                          {{/resolved}}

                                           </td>
                                            <td id="merge_out_{{id}}">


                                           </td>

                                   </tr>
                              {{/items}}
                           </table>