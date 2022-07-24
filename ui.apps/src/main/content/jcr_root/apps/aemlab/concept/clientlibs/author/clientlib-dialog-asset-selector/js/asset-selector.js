/* This does not work with fielupload and radio */


(function (document, $) {
    "use strict";

    const PICKER_FORM = 'form.granite-pickerdialog-content';
    const SELECTORS={
        SELECT_BTN:PICKER_FORM + ' button.asset-picker-done',
        CANCEL_BTN:PICKER_FORM + ' button.asset-picker-clear',
        SELECTED_ITEM:PICKER_FORM + ' .foundation-selections-item.is-selected',
        ITEM_ATTR : 'data-foundation-collection-item-id',
        PATH_FIELD_SELECTOR : '.pathfield__asset--selector input[aria-controls="asset-selector-launcher-setting"]'
    };

    $(document).on("click", SELECTORS.SELECT_BTN, function (e) {
        var selectedItem = document.querySelector(SELECTORS.SELECTED_ITEM).getAttribute(SELECTORS.ITEM_ATTR);
        document.querySelector(SELECTORS.PATH_FIELD_SELECTOR).value=selectedItem;
        //close popup
        document.querySelector(SELECTORS.CANCEL_BTN).click();
    });



})(document, Granite.$);


