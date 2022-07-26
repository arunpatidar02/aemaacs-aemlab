(function ($, $document) {
    "use strict";
    var flag = true;
    $document.on("dialog-ready", function () {

        console.log('ready');
        tinymce.init({ selector:'.tinymce-editor' });

    });

})($, $(document));