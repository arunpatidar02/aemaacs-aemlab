(function(channel) {
    "use strict";

    channel.on("cq-editor-loaded", function(event) {
        var panelUtils = window.CQ?.CoreComponents?.panelcontainer?.v1?.registry;
        if (panelUtils) {
            panelUtils.register({
                name: "cmp-ce",
                selector: ".cmp-ce",
                wrapperSelector: '[data-panelcontainer="ce"]',
                itemSelector: "[data-cmp-hook-ce='itempanel']",
                itemActiveSelector: ".cmp-ce__itempanel--active"
            });
        }
    });

})(jQuery(document));
