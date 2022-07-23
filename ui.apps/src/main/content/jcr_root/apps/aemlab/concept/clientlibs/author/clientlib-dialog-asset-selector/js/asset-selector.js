/* This does not work with fielupload and radio */


(function (document, $) {
    "use strict";

    const SELECTORS={
        CQ_DIALOG_SUBMIT : '.cq-dialog-submit',
        CQ_DIALOG_FORM :'form.cq-dialog.foundation-form',
        MULTIFIELD_JSON_FIELD :'.multifield-save-is-json',
        MULTIFIELD_ITEM : 'coral-multifield-item',
        RICHTEXT_FIELD : 'cq-RichText-editable',
        CORAL_RADIO :'coral-radio',
        RADIO_INPUT: '._coral-Radio-input',
        FIELUPLOAD_FIELD : 'coral-fileupload',
        FIELUPLOAD_THUMBNAIL : '.cq-FileUpload-thumbnail',
        FIELUPLOAD_THUMBNAIL_IMG :'.cq-FileUpload-thumbnail-img',
        FIELUPLOAD_EDIT_BTN : 'button.cq-FileUpload-edit',
        FIELD_NON_EMPTY_NAME : '[name!=""][name]',
        FIELD_NAME_AT : '[name*="@"]',
        INPUT_FILEUPLOAD_FILENAME : 'input[data-cq-fileupload-parameter="filename"]',
        FIELUPLOAD_FIELD_INPUT : 'input._coral-FileUpload-input',
        CQ_FILEUPLOAD : '.cq-FileUpload'
    };

    const CONST = {
        ACTION_ATTR : 'action',
        JSON_EXTENSION : '.json',
        MULTIFIELD_NAME_ATTR : 'data-granite-coral-multifield-name',
        FIELUPLOAD_PARAMETER_ATTR : 'data-cq-fileupload-parameter',
        CORAL_RADIO_ITEM :'_coral-Radio',
        CHECKBOX_INPUT : '_coral-Checkbox-input',
        SWITCH_INPUT : '_coral-ToggleSwitch-input',
        CHECKED : 'checked',
        DISABLED : 'disabled',
        IS_FILLED : 'is-filled',
        TYPE_ATTR : 'type',
        NAME_ATTR : 'name',
        VALUE_ATTR : 'value',
        INPUT : 'input',
        HIDDEN : 'hidden',
        TYPEHINT : '@TypeHint',
        STRING_ARR : 'String[]',
        FIELUPLOAD_THUMBNAIL_IMG_RENDITION : '/_jcr_content/renditions/cq5dam.thumbnail.319.319.png'
    };


    $(document).on("click", SELECTORS.CQ_DIALOG_SUBMIT, function (e) {
        $(SELECTORS.MULTIFIELD_JSON_FIELD).each(function () {
            console.log('clicked');
        });
    });

    

})(document, Granite.$);


