// RTE StylePicker Plugin code
(function ($, CUI) {
    var GROUP = "styleformat",
        STYLES_FEATURE = "styles",
        DIALOG = "stylePickerDialog",
        PICKER_NAME_IN_POPOVER = "style",
        REQUESTER = "requester",
        TITLE = "Style Picker",
        ICON = "colorPalette",
        PICKER_URL = "/apps/aemlab/oneweb/concept/utils/dialog/rte-plugins/style-picker/popovers/style-picker/_cq_dialog.html";

    if (document.location.pathname.indexOf("/editor.html/") > -1)
        addDialogTemplate();


    var StylePickerDialog = new Class({
        extend: CUI.rte.ui.cui.AbstractDialog,

        toString: "StylePickerDialog",

        initialize: function (config) {
            this.exec = config.execute;
        },

        getDataType: function () {
            return DIALOG;
        }
    });

    var StylePickerPlugin = new Class({
        toString: "StylePickerPlugin",

        extend: CUI.rte.plugins.Plugin,

        pickerUI: null,

        getFeatures: function () {
            return [STYLES_FEATURE];
        },

        initializeUI: function (tbGenerator) {
            var plg = CUI.rte.plugins;

            if (!this.isFeatureEnabled(STYLES_FEATURE)) {
                return;
            }

            this.pickerUI = tbGenerator.createElement(STYLES_FEATURE, this, false, {
                title: TITLE
            });
            tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.pickerUI, 10);

            var groupFeature = GROUP + "#" + STYLES_FEATURE;
            tbGenerator.registerIcon(groupFeature, ICON);
        },

        execute: function (id, value, envOptions) {

            if (!isValidSelection()) {
                return;
            }

            var context = envOptions.editContext,
                dialog,
                ek = this.editorKernel,
                selection = context.win.getSelection(),
                startNode = selection.baseNode,
                styleClasses = $(startNode).attr('class'),
                plugin = this,
                dm = ek.getDialogManager(),
                $container = CUI.rte.UIUtils.getUIContainer($(context.root)),
                propConfig = {
                    'parameters': {
                        'command': this.pluginId + '#' + STYLES_FEATURE
                    }
                };


            if (this.StylePickerDialog) {
                dialog = this.StylePickerDialog;
            } else {
                dialog = new StylePickerDialog();

                dialog.attach(propConfig, $container, this.editorKernel);

                dialog.$dialog.css("-webkit-transform", "scale(0.8)").css("-webkit-transform-origin", "0 0")
                    .css("-moz-transform", "scale(0.8)").css("-moz-transform-origin", "0px 0px");


                if (typeof styleClasses === 'undefined') {
                    styleClasses = "";
                }
                dialog.$dialog.find("iframe").attr("src", getPickerIFrameUrl(styleClasses));

                this.StylePickerDialog = dialog;
            }

            dm.show(dialog);
            registerReceiveDataListener(receiveMessage);

            function isValidSelection() {
                var winSel = window.getSelection();
                return winSel && winSel.rangeCount == 1 && winSel.getRangeAt(0).toString().length > 0;
            }

            function getPickerIFrameUrl(style) {
                var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

                if (!_.isEmpty(style)) {
                    url = url + "&" + PICKER_NAME_IN_POPOVER + "=" + style;
                }
                url = encodeURI(url)
                return url;
            }

            function removeReceiveDataListener(handler) {
                if (window.removeEventListener) {
                    window.removeEventListener("message", handler);
                } else if (window.detachEvent) {
                    window.detachEvent("onmessage", handler);
                }
            }

            function registerReceiveDataListener(handler) {
                if (window.addEventListener) {
                    window.addEventListener("message", handler, false);
                } else if (window.attachEvent) {
                    window.attachEvent("onmessage", handler);
                }
            }

            function receiveMessage(event) {

                if (_.isEmpty(event.data)) {
                    return;
                }
                var message = event.data,
                    action;
                if (!message || message.sender !== GROUP) {
                    return;
                }

                action = message.action;

                if (action === "submit") {
                    if (!_.isEmpty(message.data)) {
                        if (startNode.nodeType == 3) {
                            $(startNode).parent().addClass(message.data.style);
                        } else {
                            $(startNode).addClass(message.data.style);
                        }
                    }
                } else if (action === "remove") {
                    if (startNode.nodeType == 3) {
                        $(startNode).parent().removeClass(message.data.style);
                    } else {
                        $(startNode).removeClass(message.data.style);
                    }
                }

                if (action === "submit" || action === "remove" || action === "cancel") {
                    plugin.StylePickerDialog = null;
                }

                dialog.hide();
                removeReceiveDataListener(receiveMessage);
            }
        },

        //to mark the icon selected/deselected
        updateState: function (selDef) {
            var hasUC = this.editorKernel.queryState(STYLES_FEATURE, selDef);

            if (this.pickerUI !== null) {
                this.pickerUI.setSelected(hasUC);
            }
        }
    });

    CUI.rte.plugins.PluginRegistry.register(GROUP, StylePickerPlugin);

    var StylePickerCmd = new Class({
        toString: "StylePickerCmd",

        extend: CUI.rte.commands.Command,

        isCommand: function (cmdStr) {
            return (cmdStr.toLowerCase() == STYLES_FEATURE);
        },

        getProcessingOptions: function () {
            var cmd = CUI.rte.commands.Command;
            return cmd.PO_SELECTION | cmd.PO_BOOKMARK | cmd.PO_NODELIST;
        }
    });

    CUI.rte.commands.CommandRegistry.register(STYLES_FEATURE, StylePickerCmd);


    function addDialogTemplate() {
        var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

        var html = "<iframe class='stylePickerIframe' width='500px' height='400px' frameBorder='0' src='" + url + "'></iframe>";

        if (_.isUndefined(CUI.rte.Templates)) {
            CUI.rte.Templates = {};
        }

        if (_.isUndefined(CUI.rte.templates)) {
            CUI.rte.templates = {};
        }

        CUI.rte.templates['dlg-' + DIALOG] = CUI.rte.Templates['dlg-' + DIALOG] = Handlebars.compile(html);

    }
}(jQuery, window.CUI, jQuery(document)));


// RTE StylePicker Plugin Popover code
(function ($, $document) {
    var SENDER = "styleformat",
        REQUESTER = "requester",
        STYLE = "style",
        ADD_STYLE_BTN = "#ADD_STYLE",
        REMOVE_STYLE_BTN = "#REMOVE_STYLE",
        DIALOG_CLASS = "style-Picker-Dialog";

    if (queryParameters()[REQUESTER] !== SENDER) {
        return;
    }

    $(function () {
        _.defer(setupPopoverIframe);
    });

    function queryParameters() {
        var result = {},
            param,
            params = document.location.search.split(/\?|\&/);

        params.forEach(function (it) {
            if (_.isEmpty(it)) {
                return;
            }

            param = it.split("=");
            result[param[0]] = param[1];
        });

        return result;
    }

    function setupPopoverIframe() {
        var queryParams = queryParameters(),
            $dialog = $("coral-dialog.cq-Dialog");
        $dialog.addClass(DIALOG_CLASS);

        if (_.isEmpty($dialog)) {
            return;
        }

        $dialog[0].open = true;

        var $addColor = $dialog.find(ADD_STYLE_BTN),
            $removeColor = $dialog.find(REMOVE_STYLE_BTN),
            $stylePicker = $document.find('.coral3-Select[name="./style"]');


        if (!_.isEmpty(queryParameters()[STYLE])) {
            var param = decodeURIComponent(queryParams[STYLE]).split(" ");
            var items = $stylePicker.find('coral-select-item');
            var selected = "";
            var found = false;
            items.each(function (index) {

                for (var i = 0; i < param.length; i++) {
                    if (param[i] == $(this).val()) {
                        $(this).attr('selected', 'selected')
                        found = true;
                    }
                }
                if (found) {
                    return;
                }

            });
        }

        adjustFooter($dialog);
        $addColor.click(function () {
            sendDataMessage("submit");
        });
        $removeColor.click(function () {
            sendDataMessage("remove");
        });
    }

    function adjustFooter($dialog) {
        var $footer = $dialog.find("._coral-Dialog-footer");

        $footer.find(".cq-dialog-submit").remove();
        $footer.find(".cq-dialog-cancel").click(function (event) {
            event.preventDefault();
            $dialog.remove();
            sendDataMessage("cancel");
        });
    }

    function sendDataMessage(actionType) {
        var message = {
            sender: SENDER,
            action: actionType,
            data: {}
        },
            $dialog, style;
        if (actionType != "cancel") {
            $dialog = $("." + DIALOG_CLASS);
            style = $dialog.find("[name='./" + STYLE + "']").val();
            message.data[STYLE] = style;
        }

        parent.postMessage(message), "*";
    }
})(jQuery, jQuery(document));