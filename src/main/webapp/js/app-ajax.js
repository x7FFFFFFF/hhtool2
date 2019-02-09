(function (global, factory) {
    global.Binding = {};
    factory(global, global.$, global.Mustache);
}(this, function (global, $, tempateEng) {
    let dataUrl = 'data-url';
    let dataParmsId = 'data-parms-id';
    let dataTeplateId = 'data-teplate-id';
    let dataTeplateUrl = 'data-teplate-url';
    let dataOutputId = 'data-output-id';
    let document = global.document;
    let apiBase = '';
    let controlsId = '';
    let outputId = '';
    let defaultTemplateId = '';
    let teplateBaseUrl = '';

    let loadParams = function (button, context) {
        let res = {};
        if (button.hasAttribute(dataParmsId)) {
            let value = button.attributes[dataParmsId].value;
            let params = $('input[' + value + '][type!=button]', context);
            for (let param of params) {
                res[param.name] = param.value;
            }
        }
        return res;
    };


    function getUrl(button) {
        let attr = getAttr(button, dataUrl);
        if (!attr) {
            throw "Missed attribute " + dataUrl;
        }
        return apiBase + attr;
    }

    function getAttr(el, name) {
        if (el.hasAttribute(name)) {
            return el.attributes[name].value;
        }
        return null;
    }

//$(templateId)[0].innerHTML;
    function getTemplate(el, consumer) {
        let id = getAttr(el, dataTeplateId);
        if (!id) {
            let url = getAttr(el, dataTeplateUrl);
            if (url) {
                $.get(teplateBaseUrl + url, function (data) {
                    consumer(data);
                });
                return;
            } else {
                if (defaultTemplateId) {
                    consumer($(defaultTemplateId)[0].innerHTML);
                    return;
                }
            }

        } else {
            consumer($(id)[0].innerHTML);
            return;
        }
        return consumer(null);
    }

    global.Binding.apiBase = function (str) {
        apiBase = str;
        return global.Binding;
    };
    global.Binding.controlsId = function (id) {
        controlsId = id;
        return global.Binding;
    };
    global.Binding.outputId = function (id) {
        outputId = id;
        return global.Binding;
    };
    global.Binding.defaultTemplateId = function (id) {
        defaultTemplateId = id;
        return global.Binding;
    };
    global.Binding.teplateBaseUrl = function (url) {
        teplateBaseUrl = url;
        return global.Binding;
    };


    global.Binding.bind = function () {
        let init = function () {
            let main = $(controlsId);
            let tab = $(outputId);

            let onTabClose = function () {
                var panelId = $(this).closest("li").remove().attr("aria-controls");
                $("#" + panelId).remove();
                tab.tabs("refresh");
            };
            tab.tabs().on("click", "span.ui-icon-close", onTabClose);


            let loadTab = function (url, data) {
                let ul = $(outputId + '>ul');
                let tabHeaders = ul.children();
                let tabName = url.replace(/\//g, '_') + '_tab_' + (tabHeaders.length + 1);
                ul.append('<li><a href="#' + tabName + '">' + url + '</a><span class="ui-icon ui-icon-close" role="presentation">Remove Tab</span>');//<span class="ui-icon ui-icon-circle-close ui-closable-tab"></span>
                tab.append('<div id="' + tabName + '">' + data + '</div>');
                tab.tabs("refresh");
            };

            let onClick = function (e) {
                let param = loadParams(this, main);
                let url = getUrl(this);
                let context = this;
                let onResponce = function (data) {
                    let consumerTemplate = function (template) {
                        if (!template) {
                            loadTab(url, JSON.stringify(data, null, 4));
                        } else {
                            loadTab(url, tempateEng.to_html(template, data));
                        }
                    };
                    getTemplate(context, consumerTemplate);
                };
                $.get(url, param, onResponce);

            };
            $('input[' + dataUrl + '][type=button]', main).click(onClick);
        };
        $(document).ready(init);
    }

}))
;

