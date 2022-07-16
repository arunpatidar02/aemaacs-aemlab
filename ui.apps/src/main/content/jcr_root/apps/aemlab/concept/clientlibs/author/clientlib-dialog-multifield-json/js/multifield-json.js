
(function (document, $, ns) {
    "use strict";

    $(document).on("click", ".cq-dialog-submit", function (e) {
        $('.multifield-save-is-json').each(function () {
            var $multifield = $(this);
            setMultifield($multifield);
        });
    });

    $(document).on("foundation-contentloaded", function (e) {
        var $form = $('form.cq-dialog.foundation-form');
        var nodePath = $form.attr('action');
        $.get(nodePath + '.json', function (data) {
            populateMultifield(data);
        });
    });

    function populateMultifield(data){
        $('.multifield-save-is-json').each(function () {
            var $multifield = $(this);
            var multifieldname = $multifield.attr('data-granite-coral-multifield-name');
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
                        var multifieldItem = $multifield.find('coral-multifield-item')[index];
                        var $multifieldItem = $(multifieldItem);
                        for (var n in multifieldItemJson) {
                            var inputValue = multifieldItemJson[n];

                            if(!inputValue){
                                continue;
                            }


                            var $input = $multifieldItem.find('[name="./' + n + '"]');
                            $input.val(inputValue);
                            var $rte = $input.hasClass('cq-RichText-editable');
                           // console.log($input);
                    if($rte){
                        $input.html(inputValue);
                    }
                    
                    var $checkbox = $input.hasClass('_coral-Checkbox-input');
                    if($checkbox){
                        $input.parent().attr('checked', true);
                    }
                    
                    
                     var $radio = $input.hasClass('_coral-Radio-input');
                    if($radio){
                        console.log($radio);
                        $input.parent().attr('checked', true);
                    }

                   var $fileBrowser = $input.attr('data-cq-fileupload-parameter');
                    if($fileBrowser){
                        $input.parents('coral-fileupload').addClass('is-filled');
                        $input.siblings('.cq-FileUpload-thumbnail').find('button.cq-FileUpload-edit').removeAttr('disabled')
                        $input.siblings('.cq-FileUpload-thumbnail').find('.cq-FileUpload-thumbnail-img').html('<img class="cq-dd-image" src="'+inputValue+'/_jcr_content/renditions/cq5dam.thumbnail.319.319.png?ch_ck="'+Math.random()+' alt="">');
                    }
                   

                        }
                    }
                }
            }
        }
    }


    function setMultifield($multifield) {
        var multifieldname = $multifield.attr('data-granite-coral-multifield-name');
        $multifield.find('coral-multifield-item').each(function () {
            var $multifieldItem = $(this);
            var inputs = $multifieldItem.find('[name!=""][name]').not('[name*="@"]').not('input[data-cq-fileupload-parameter="filename"]').not('input._coral-FileUpload-input').not('.cq-FileUpload');
            var jsonObj = {};
            inputs.each(function () {
                var $input = $(this);
                var key = $input.attr('name').substring(2);
                var inputValue = $input.val();

                if($input.hasClass('_coral-Checkbox-input')){
                    inputValue = $input.parent().attr('checked') ? inputValue : "";
                }
                jsonObj[key] = inputValue;
                $input.attr('disabled', true);
               
            });
            createHiddenInputField($multifield, multifieldname, JSON.stringify(jsonObj));
        });
        createHiddenInputField($multifield, multifieldname + '@TypeHint', 'String[]');
    }

    function createHiddenInputField(ele, name, value) {
        var inputField = document.createElement('input');
        inputField.setAttribute('type', 'hidden');
        inputField.setAttribute('name', name);
        inputField.setAttribute('value', value);
        ele.append(inputField);
    }


})(document, Granite.$, Granite.author);


