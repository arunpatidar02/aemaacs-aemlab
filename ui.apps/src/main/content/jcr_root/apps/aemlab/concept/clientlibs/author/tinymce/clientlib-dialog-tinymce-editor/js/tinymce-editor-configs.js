(function ($) {
    "use strict";

    const useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const convertUrls = false;

    /*
        TinyMCE Editor initialization
    */
    $.fn.initTinyMCE = (selector, tinymceConfig) => {
        switch (tinymceConfig) {
            case "tinymce-editor-1":
                tinyMCEConfig1(selector);
                break;
            case "tinymce-editor-2":
                tinyMCEConfig2(selector);
                break;
            default:
                console.warn(`Tinymce Config ${tinymceConfig} is not initialize for ${selector}`);
        }
    }

    /*
        TinyMCE Editor Config for tinymce-editor-1
    */
    function tinyMCEConfig1(selector) {
        tinymce.init({
            selector: selector,
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
            height: 500,
            image_caption: true,
            quickbars_selection_toolbar: 'bold italic |  blockquote | quicklink | quicktable',
            noneditable_noneditable_class: 'mceNonEditable',
            toolbar_mode: 'sliding',
            convert_urls: convertUrls,
            content_style: '.aemlab__somectyle{ color: gray; }',
            contextmenu: 'link image imagetools table',
            skin: useDarkMode ? 'oxide-dark' : 'oxide',
            content_css: useDarkMode ? 'dark' : 'default'
        });
    }

    /*
        TinyMCE Editor Config for tinymce-editor-2
    */

    function tinyMCEConfig2(selector) {
        tinymce.init({
            selector: selector,
            height: 300,
            plugins: 'table wordcount',
            menubar: 'format tools table help',
            formats: {
                alignleft: { selector: 'p,h1,h2,h3,h4,h5,h6,td,th,div,ul,ol,li,table,img,audio,video', classes: 'left' },
                aligncenter: { selector: 'p,h1,h2,h3,h4,h5,h6,td,th,div,ul,ol,li,table,img,audio,video', classes: 'center' },
                alignright: { selector: 'p,h1,h2,h3,h4,h5,h6,td,th,div,ul,ol,li,table,img,audio,video', classes: 'right' },
                alignfull: { selector: 'p,h1,h2,h3,h4,h5,h6,td,th,div,ul,ol,li,table,img,audio,video', classes: 'full' },
                bold: { inline: 'span', classes: 'bold' },
                italic: { inline: 'span', classes: 'italic' },
                underline: { inline: 'span', classes: 'underline', exact: true },
                strikethrough: { inline: 'del' },
                customformat: { inline: 'span', styles: { color: '#00ff00', fontSize: '20px' }, attributes: { title: 'My custom format' }, classes: 'example1' }
            },
            style_formats: [
                { title: 'Custom format', format: 'customformat' },
                { title: 'Align left', format: 'alignleft' },
                { title: 'Align center', format: 'aligncenter' },
                { title: 'Align right', format: 'alignright' },
                { title: 'Align full', format: 'alignfull' },
                { title: 'Bold text', inline: 'strong' },
                { title: 'Red text', inline: 'span', styles: { color: '#ff0000' } },
                { title: 'Red header', block: 'h1', styles: { color: '#ff0000' } },
                { title: 'Badge', inline: 'span', styles: { display: 'inline-block', border: '1px solid #2276d2', 'border-radius': '5px', padding: '2px 5px', margin: '0 2px', color: '#2276d2' } },
                { title: 'Table row 1', selector: 'tr', classes: 'tablerow1' },
                { title: 'Image formats' },
                { title: 'Image Left', selector: 'img', styles: { 'float': 'left', 'margin': '0 10px 0 10px' } },
                { title: 'Image Right', selector: 'img', styles: { 'float': 'right', 'margin': '0 0 10px 10px' } },
            ]
        });

    }

})($);