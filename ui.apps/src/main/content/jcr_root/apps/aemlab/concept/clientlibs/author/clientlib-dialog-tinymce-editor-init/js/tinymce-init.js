(function ($, $document) {
    "use strict";
    var flag = true;
    $document.on("dialog-ready", function () {

        var useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;

        tinymce.init({
            selector: '.tinymce-editor',
            plugins: 'preview searchreplace autosave save directionality visualblocks visualchars image link media template codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help charmap quickbars emoticons',
            mobile: {
                plugins: 'preview searchreplace autosave save directionality visualblocks visualchars image link media template codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help charmap quickbar emoticons'
            },
            menubar: 'file edit view insert format tools table help',
            toolbar: 'undo redo | bold italic underline strikethrough link',
            autosave_ask_before_unload: true,
            autosave_interval: '30s',
            autosave_prefix: '{path}{query}-{id}-',
            autosave_restore_when_empty: false,
            autosave_retention: '2m',
            image_advtab: true,
            link_list: [
                { title: 'AEMLAB', value: 'https://aemlab.blogspot.com/2019/06/index.html' },
                { title: 'GitHub', value: 'https://github.com/arunpatidar02/aemaacs-aemlab' }
            ],
            image_class_list: [
                { title: 'Landscape', value: 'aemlab-image__landscape' },
                { title: 'Portrait', value: 'aemlab-image__portrait' }
            ],
            importcss_append: true,
            height: 600,
            image_caption: true,
            quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
            noneditable_noneditable_class: 'mceNonEditable',
            toolbar_mode: 'sliding',
            convert_urls: false,
            content_style: '.aemlab__somectyle{ color: gray; }',
            contextmenu: 'link image imagetools table',
            skin: useDarkMode ? 'oxide-dark' : 'oxide',
            content_css: useDarkMode ? 'dark' : 'default'
        });

    });

})($, $(document));