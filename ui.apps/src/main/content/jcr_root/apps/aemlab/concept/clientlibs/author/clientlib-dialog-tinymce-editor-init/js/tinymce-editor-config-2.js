(function ($, $document) {
    "use strict";
    $document.on("dialog-ready", function () {

        var useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;
        const TEXTAREA_SELECTOR = '.tinymce-editor-2';

        tinymce.init({
            selector: TEXTAREA_SELECTOR,
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


    });

})($, $(document));