 count = {{count}} <br />
                <table>
                <tr>
                     <td>id</td>
                     <td>title</td>
                     <td>Action</td>
                </tr>

                   {{#items}}
                        <tr>
                            <td>{{id}}</td>
                            <td>{{title}}</td>
                            <td>
                                     <input class="button" type="button"
                                     id="load_Cities_{{id}}"
                                     value="Load cities"
                                     data-teplate-url="vkCities.tpl"
                                     data-url="vk/loadCities/{{id}}"
                                     />

                            </td>
                        </tr>
                   {{/items}}
                </table>