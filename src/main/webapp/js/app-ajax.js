(function (global, factory) {
    global.Binding = {};
    factory(global, global.$, global.Mustache);
}(this, function (global, $, tempateEng) {
    let dataUrl = 'data-url';
    let dataUrlMethod = 'data-url-method';
    let dataParmsId = 'data-parms-id';
    let dataTeplateId = 'data-teplate-id';
    let dataTeplateUrl = 'data-teplate-url';
    let dataOutputId = 'data-output-id';
    let document = global.document;
    let apiBase = '';
    let controlsId = '';
    let outputId = '';
    let defaultTemplateId = '';
    let defaultMethod = 'GET';
    let teplateBaseUrl = '';
    let cache = {};

    let loadParams = function (button, context) {
        let res = {};
        if (button.hasAttribute(dataParmsId)) {
            let value = button.attributes[dataParmsId].value;
            let params = $(value + '[type!=button]', context);
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


    function getMethod(el) {
        let val = getAttr(el, dataUrlMethod);
        return (val) ? val : defaultMethod;
    }

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
                let method = getMethod(this);
                let outputId = getAttr(this, dataOutputId);
                let onResponce = function (data) {
                    let consumerTemplate = function (template) {
                        let text;
                        if (!template) {
                            text = JSON.stringify(data, null, 4);
                        } else {
                            text = tempateEng.to_html(template, data);
                        }
                        if (outputId) {
                            $(outputId)[0].innerHTML = text;
                        } else {
                            loadTab(url, text);
                        }
                    };
                    getTemplate(context, consumerTemplate);
                };
                $.ajax(
                    {
                        method: method,
                        url: url,
                        data: param
                    }).done(onResponce);

            };
            setInterval(function () {
                let button = $('input[' + dataUrl + '][type=button]', main).each(function (i, el) {
                        let attr = getAttr(el, 'id');
                        if (!cache[attr]) {
                            cache[attr] = true;
                            $(el).click(onClick);
                        }
                    }
                );


            }, 1000);

        };
        $(document).ready(init);
    }

}))
;

