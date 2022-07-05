
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
            checkMultifieldInJsonResponse($multifield, multifieldname);
        });
    }

    function checkMultifieldInJsonResponse($multifield, multifieldname){
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                if (multifieldname.replace('./', '') == key) {
                    var multifieldJson = data[key];
                    for (var index = 0; index < multifieldJson.length; index++) {
                        var multifieldItemJson = JSON.parse(multifieldJson[index]);
                        var multifieldItem = $multifield.find('coral-multifield-item')[index];
                        var $multifieldItem = $(multifieldItem);
                        console.log(multifieldItemJson);
                        for (var n in multifieldItemJson) {
                            var $input = $multifieldItem.find('[name="./' + n + '"]');
                            console.log($input);
                            $input.val(multifieldItemJson[n]);
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
            var inputs = $multifieldItem.find('input[name]');
            var jsonObj = {};
            inputs.each(function () {
                var $input = $(this);
                var key = $input.attr('name')?.replace('./', '');
                if (!key.includes('@')) {
                    jsonObj[key] = $input.val();
                    $input.attr('disabled', true);
                }
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
        ele.append(newField);
    }


})(document, Granite.$, Granite.author);


