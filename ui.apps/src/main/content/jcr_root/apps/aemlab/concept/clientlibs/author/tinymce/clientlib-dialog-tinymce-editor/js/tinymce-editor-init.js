(function ($, $document) {
    "use strict";
    $document.on("dialog-ready", function () {
        const TEXTAREA_SELECTOR = 'textarea.tinymce-editor';
        const TINYMCE_CONFIG_ATTR = 'data-tinymce-config';
        var textareas = document.querySelectorAll(TEXTAREA_SELECTOR);
        for (const ta of textareas) {
            var selector = '#' + ta.id;
            var config = ta.getAttribute(TINYMCE_CONFIG_ATTR);
            if (selector && config) {
                $.fn.initTinyMCE(selector, config);
            }
        }
        $.fn.initTinyMCE('abcd', 'config')
    });

})($, $(document));