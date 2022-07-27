/* This does not work with fielupload and radio */


(function (document, $) {
    "use strict";


    const MULTIFIELD_ADD_BUTTON = '.tinymce-enabled button[coral-multifield-add]';
    const TARGET_DATASOURCE_ATTR = 'data-target-tinymce-textarea'
    const TARGET_SELECTORS_SOURCE = '.tinymce-enabled['+TARGET_DATASOURCE_ATTR+']';
    const TARGET_TEXTAREAS = '.tinymce-editor';
    const TINYMCE_CONFIG_ATTR = 'data-tinymce-config';


    $(document).on("click", MULTIFIELD_ADD_BUTTON, function (e) {
        var $multifieldItem = $(this).closest(TARGET_SELECTORS_SOURCE);
        if($multifieldItem){
           var targetTextareaClasses = $multifieldItem.attr(TARGET_DATASOURCE_ATTR);
           targetTextareaClasses.split(',').map(function(ele){
            console.log(ele);
            var target = $multifieldItem.find('.'+ele+TARGET_TEXTAREAS);
            var tinymceConfig = target.attr(TINYMCE_CONFIG_ATTR);
            console.log(target, tinymceConfig);
            tinymce.init({selector:target})
           })
        }
        
    });

    


})(document, Granite.$);


