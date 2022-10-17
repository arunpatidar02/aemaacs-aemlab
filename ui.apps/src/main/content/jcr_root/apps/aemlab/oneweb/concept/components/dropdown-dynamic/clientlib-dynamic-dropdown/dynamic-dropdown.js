(function ($, $document) {
    "use strict";

    const contentTypeSelector = ".dropdownfield_contenttype";
    const contentSubTypeSelector = ".dropdownfield_contentsubtype";
    const contentSubTypeHiddenSelector = ".dropdownfield_contentsubtype--hidden";
    const contentSubTypeDataSourceUri =
        "/apps/aemlab/oneweb/concept/utils/dialog/contentsubytpe.json";


    $document.on("foundation-contentloaded", function (e) {
        setSubTypeDropdown(true);
    });

    $document.on("change", contentTypeSelector, function (e) {
        setSubTypeDropdown(false);
    });

    function setSubTypeDropdown(preSelect) {
        const contentType = document.querySelector(contentTypeSelector);
        const contentSubType = document.querySelector(contentSubTypeSelector);

        if (contentType && contentSubType) {
            var url =contentSubTypeDataSourceUri +"?type=" +contentType.value;
            $.get(url, function (data) {
                updateSubTypeDropdownField(preSelect, data);
            });
        }
    }

    function updateSubTypeDropdownField(preSelect, data) {
        const contentSubTypeDropdown = document.querySelector(contentSubTypeSelector);
        const contentSubTypeDropdownValue = document.querySelector(contentSubTypeHiddenSelector).value;
       
        //  Remove existing items from dropdown
        contentSubTypeDropdown.items.clear();

        for (var i in data) {
            contentSubTypeDropdown.items.add({
                value: data[i],
                content: { innerHTML: data[i] },
                selected:
                    preSelect && contentSubTypeDropdownValue === data[i] ? true : false,
            });
        }
    }
})($, $(document));
