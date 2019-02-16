 count = {{count}} <br />
                <table>
                <tr>
                     <td>vendorId</td>
                     <td>parentVendorId</td>
                     <td>title</td>
                     <td>region</td>
                     <td>area</td>
                </tr>

                   {{#items}}
                        <tr>
                            <td>{{vendorId}}</td>
                            <td>{{parentVendorId}}</td>
                            <td>{{name}}</td>
                            <td>{{region}}</td>
                            <td>{{area}}</td>
                        </tr>
                   {{/items}}
                </table>