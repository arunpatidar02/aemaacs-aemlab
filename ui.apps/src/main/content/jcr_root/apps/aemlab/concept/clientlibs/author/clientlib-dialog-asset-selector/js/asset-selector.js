
(function (document, $) {
    "use strict";

    const PICKER_FORM = 'form.granite-pickerdialog-content';
    const SELECTORS={
        SELECT_BTN:PICKER_FORM + ' button.asset-picker-done',
        CANCEL_BTN:PICKER_FORM + ' button.asset-picker-clear',
        SELECTED_ITEM:PICKER_FORM + ' .foundation-selections-item.is-selected',
        ITEM_ATTR : 'data-foundation-collection-item-id',
        PATH_FIELD_SELECTOR : '.pathfield__asset--selector input[aria-controls="asset-selector-launcher-setting"]',
        PATH_FIELD_MULTI_SELECTOR : '.pathfield__asset--selector[multiple] input[aria-controls="asset-selector-launcher-setting"]',
        MODE_ELEMENT:PICKER_FORM+' #cq-damadmin-admin-assetselector-collection',
        MODE_ATTR:'selectionmode'
    };

    $(document).on("click", SELECTORS.SELECT_BTN, function (e) {
        var mode = document.querySelector(SELECTORS.MODE_ELEMENT).getAttribute(SELECTORS.MODE_ATTR);
        if(mode === "multiple" ){
            var selectedItems = document.querySelectorAll(SELECTORS.SELECTED_ITEM);
            var tagList = $(SELECTORS.PATH_FIELD_MULTI_SELECTOR).closest('.pathfield__asset--selector').find('coral-taglist');
            for (const item of selectedItems) {
                var value = item.getAttribute(SELECTORS.ITEM_ATTR)
                var tag = new Coral.Tag().set({
                    value: value,
                    label: {
                      innerHTML: value
                    }
                  });
                  tagList.append(tag);
            }
        }
        else{
            var selectedItem = document.querySelector(SELECTORS.SELECTED_ITEM).getAttribute(SELECTORS.ITEM_ATTR);
            document.querySelector(SELECTORS.PATH_FIELD_SELECTOR).value=selectedItem;
        }
         //close popup
        document.querySelector(SELECTORS.CANCEL_BTN).click();
    });

})(document, Granite.$);


