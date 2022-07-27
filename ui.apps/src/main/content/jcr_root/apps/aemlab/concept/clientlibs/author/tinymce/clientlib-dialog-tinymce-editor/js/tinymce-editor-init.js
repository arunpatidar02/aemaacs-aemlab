(function ($, $document) {
    "use strict";

    const TINYMCE_CONFIG_ATTR = 'data-tinymce-config';
    const TEXTAREA_SELECTOR = 'textarea[' + TINYMCE_CONFIG_ATTR + ']';
    const MULTIFIELD_SELECTOR = '.tinymce-enabled';
    const MULTIFIELD_ADD_BUTTON = MULTIFIELD_SELECTOR + ' button[coral-multifield-add]';
  

    // initialize tinyMCE on dialog load
    $document.on("dialog-ready", function () {
        var textareas = document.querySelectorAll(TEXTAREA_SELECTOR);
        initTinyMCE(textareas);
    });

    // initialize tinyMCE on multifield add
    $(document).on("click", MULTIFIELD_ADD_BUTTON, function (e) {
        var $multifieldItem = $(this).closest(MULTIFIELD_SELECTOR);
        if ($multifieldItem) {
            var textareas = $multifieldItem.find(TEXTAREA_SELECTOR);
            initTinyMCE(textareas);
        }
    });

    function initTinyMCE(textareas) {
        for (const ta of textareas) {
            var selector = '#' + ta.id;
            var config = ta.getAttribute(TINYMCE_CONFIG_ATTR);
            if (selector && config) {
                $.fn.initTinyMCE(selector, config);
            }
        }
    }

})($, $(document));