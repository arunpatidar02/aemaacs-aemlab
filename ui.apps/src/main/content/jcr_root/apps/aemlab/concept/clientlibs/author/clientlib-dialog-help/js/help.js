(function ($) {
    "use strict";

    const CONST = {
        SUCCESS: "success",
        REGEX: "^(?:[a-z+]+:)?//",
        SHOWDOWN_URL: "https://cdnjs.cloudflare.com/ajax/libs/showdown/2.1.0/showdown.min.js",
        README_SCRIPT: "readme-script",
        README_MODEL_ID: "readme-model-content",
        README_MODEL_TITLE: "Component Readme"
    };

    const SELECTOR = {
        HELP_LINK: ".cq-dialog-header-action.cq-dialog-help._coral-Button",
        DATA_HREF: "data-href",
        DATA_HREF_INIT: "data-href-init",
        SHOWDOWN_SCRIPT: "#" + CONST.README_SCRIPT,
        README_MODEL: "#" + CONST.README_MODEL_ID
    };

    $(document).on("dialog-ready", function () {
        $(SELECTOR.README_MODEL).remove();
        let helpEle = document.querySelector(SELECTOR.HELP_LINK);
        let href = helpEle?.getAttribute(SELECTOR.DATA_HREF);
        let hrefInit = helpEle?.getAttribute(SELECTOR.DATA_HREF_INIT);


        if (!href || hrefInit) {
            return;
        }

        let r = new RegExp(CONST.REGEX, 'i');
        let isAbsPath = r.test(href)

        if (isAbsPath) {
            return;
        }

        helpEle.addEventListener("click", function (event) {
            event.stopPropagation();
        });


        // Get Readme file content and create popup
        $.get(href, function (data, status) {
            if (status !== CONST.SUCCESS) {
                return
            }

            let scriptEle = document.querySelector(SELECTOR.SHOWDOWN_SCRIPT);
            if (!scriptEle) {
                loadScript(convertMarkup);
            }

            function convertMarkup() {
                const converter = new showdown.Converter();
                const html = converter.makeHtml(data);
                createPopUp(html);
            }
        })

        function createPopUp(html) {
            var popover = new Coral.Popover().set({
                placement: "right",
                variant: "help",
                id: CONST.README_MODEL_ID,
                header: {
                    innerHTML: CONST.README_MODEL_TITLE
                },
                content: {
                    innerHTML: html
                },
                target: SELECTOR.HELP_LINK,
                closable: "on"
            });

            document.body.append(popover);
        }



        function loadScript(callback) {
            var script = document.createElement('script');
            script.type = 'text/javascript';
            script.src = CONST.SHOWDOWN_URL;
            script.id = CONST.README_SCRIPT;
            helpEle.appendChild(script);
            script.onreadystatechange = callback;
            script.onload = callback;

            script.onerror = function (err) {
                console.error(err);
            };
        }

        helpEle?.setAttribute(SELECTOR.DATA_HREF_INIT, true);
    });


})($);
