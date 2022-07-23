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
            var $multifield = $(this);
            setMultifield($multifield);
        });
    });

    

    $(document).on("foundation-contentloaded", function (e) {
        var $form = $(SELECTORS.CQ_DIALOG_FORM);
        var nodePath = $form.attr(CONST.ACTION_ATTR);
        $.get(nodePath + CONST.JSON_EXTENSION, function (data) {
            populateMultifield(data);
        });
    });

    function populateMultifield(data){
        $(SELECTORS.MULTIFIELD_JSON_FIELD).each(function () {
            var $multifield = $(this);
            var multifieldname = $multifield.attr(CONST.MULTIFIELD_NAME_ATTR);
            checkMultifieldInJsonResponse($multifield, multifieldname,data);
        });
    }

    function checkMultifieldInJsonResponse($multifield, multifieldname, data){
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                if (multifieldname.substring(2) == key) {
                    var multifieldJson = data[key];
                    for (var index = 0; index < multifieldJson.length; index++) {
                        var multifieldItemJson = JSON.parse(multifieldJson[index]);
                        var multifieldItem = $multifield.find(SELECTORS.MULTIFIELD_ITEM)[index];
                        var $multifieldItem = $(multifieldItem);
                        for (var n in multifieldItemJson) {
                            var inputValue = multifieldItemJson[n];

                            if(!inputValue){
                                continue;
                            }


                            var $input = $multifieldItem.find('[name="./' + n + '"]');
                            $input.val(inputValue);
                            var $rte = $input.hasClass(SELECTORS.RICHTEXT_FIELD);
                    if($rte){
                        $input.html(inputValue);
                    }
                    
                    var $checkbox = $input.hasClass(CONST.CHECKBOX_INPUT);
                    if($checkbox){
                        $input.parent().attr(CONST.CHECKED, true);
                    }

                    var $switch = $input.hasClass(CONST.SWITCH_INPUT);
                    if($switch){
                        $input.attr(CONST.CHECKED, true);
                    }
                    
                    
                     var $radio = $input.hasClass(CONST.CORAL_RADIO_ITEM);
                    if($radio){
                        $multifieldItem.find(SELECTORS.CORAL_RADIO_ITEM+'[name="./' + n + '"][value="' + inputValue + '"]').attr(CONST.CHECKED, true);
                    }

                   var $fileBrowser = $input.attr(CONST.FIELUPLOAD_PARAMETER_ATTR);
                    if($fileBrowser){
                        $input.parents(SELECTORS.FIELUPLOAD_FIELD).addClass(CONST.IS_FILLED);
                        $input.siblings(SELECTORS.FIELUPLOAD_THUMBNAIL).find(SELECTORS.FIELUPLOAD_EDIT_BTN).removeAttr(CONST.DISABLED)
                        $input.siblings(SELECTORS.FIELUPLOAD_THUMBNAIL).find(SELECTORS.FIELUPLOAD_THUMBNAIL_IMG).html('<img class="cq-dd-image" src="'+inputValue+CONST.FIELUPLOAD_THUMBNAIL_IMG_RENDITION+'?ch_ck="'+Math.random()+' alt="">');
                    }
                   

                        }
                    }
                }
            }
        }
    }


    function setMultifield($multifield) {
        var multifieldname = $multifield.attr(CONST.MULTIFIELD_NAME_ATTR);
        $multifield.find(SELECTORS.MULTIFIELD_ITEM).each(function () {
            var $multifieldItem = $(this);
            var inputs = $multifieldItem.find(SELECTORS.FIELD_NON_EMPTY_NAME)
            .not(SELECTORS.FIELD_NAME_AT)
            .not(SELECTORS.RADIO_INPUT)
            .not(SELECTORS.INPUT_FILEUPLOAD_FILENAME)
            .not(SELECTORS.FIELUPLOAD_FIELD_INPUT)
            .not(SELECTORS.CQ_FILEUPLOAD);
            
            var jsonObj = {};
            inputs.each(function () {
                var $input = $(this);
                var key = $input.attr(CONST.NAME_ATTR).substring(2);
                var inputValue = $input.val();
                if($input.hasClass(SELECTORS.RICHTEXT_FIELD)){
                    if($input.text().length !=0){
                        inputValue=$input.html()
                    }
                }
                else if($input.hasClass(CONST.CORAL_RADIO_ITEM)){
                    inputValue = '';
                    $input.parent().find(SELECTORS.CORAL_RADIO).each(function () {
                        if($(this).attr(CONST.CHECKED)){
                            inputValue = $(this).val();
                           return false;
                        }
                    });
                }
                else if($input.hasClass(CONST.CHECKBOX_INPUT)){
                    inputValue = $input.parent().attr(CONST.CHECKED) ? inputValue : '';
                }else if($input.hasClass(CONST.SWITCH_INPUT)){
                    inputValue = $input.parent().attr(CONST.CHECKED) ? inputValue : '';
                }
                jsonObj[key] = inputValue;
                $input.attr(CONST.DISABLED, true);
               
            });
            createHiddenInputField($multifield, multifieldname, JSON.stringify(jsonObj));
        });
        createHiddenInputField($multifield, multifieldname + CONST.TYPEHINT, CONST.STRING_ARR);
    }

    function createHiddenInputField(ele, name, value) {
        var inputField = document.createElement(CONST.INPUT);
        inputField.setAttribute(CONST.TYPE_ATTR, CONST.HIDDEN);
        inputField.setAttribute(CONST.NAME_ATTR, name);
        inputField.setAttribute(CONST.VALUE_ATTR, value);
        ele.append(inputField);
    }


})(document, Granite.$);


