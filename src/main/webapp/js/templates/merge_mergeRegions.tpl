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