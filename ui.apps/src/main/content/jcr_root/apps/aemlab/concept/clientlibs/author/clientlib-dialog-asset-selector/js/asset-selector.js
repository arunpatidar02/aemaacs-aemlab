
(function (document, $) {
    "use strict";

    const PICKER_FORM = 'form.granite-pickerdialog-content';
    const PATHFIELD_TARGET = '.pathfield__asset--selector';
    const MULTIPLE = 'multiple';
    const CLICK = 'click';
    const SELECTORS = {
        SELECT_BTN: PICKER_FORM + ' button.asset-picker-done',
        CANCEL_BTN: PICKER_FORM + ' button.asset-picker-clear',
        SELECTED_ITEM: PICKER_FORM + ' .foundation-selections-item.is-selected',
        ITEM_ATTR: 'data-foundation-collection-item-id',
        PATHFIELD_SELECTOR: PATHFIELD_TARGET + ' input[aria-controls="asset-selector-launcher-setting"]',
        PATHFIELD_MULTI_SELECTOR: PATHFIELD_TARGET + '[multiple] input[aria-controls="asset-selector-launcher-setting"]',
        MODE_ELEMENT: PICKER_FORM + ' #cq-damadmin-admin-assetselector-collection',
        MODE_ATTR: 'selectionmode',
        CORAL_TAGLIST: 'coral-taglist'
    };

    $(document).on(CLICK, SELECTORS.SELECT_BTN, function () {
        var mode = document.querySelector(SELECTORS.MODE_ELEMENT).getAttribute(SELECTORS.MODE_ATTR);
        if (mode === MULTIPLE) {
            var selectedItems = document.querySelectorAll(SELECTORS.SELECTED_ITEM);
            var tagList = $(SELECTORS.PATHFIELD_MULTI_SELECTOR).closest(PATHFIELD_TARGET).find(SELECTORS.CORAL_TAGLIST);
            for (const item of selectedItems) {
                var value = item.getAttribute(SELECTORS.ITEM_ATTR);
                var tag = new Coral.Tag().set({
                    value: value,
                    label: {
                        innerHTML: value
                    }
                });
                tagList.append(tag);
            }
        }
        else {
            var selectedItem = document.querySelector(SELECTORS.SELECTED_ITEM).getAttribute(SELECTORS.ITEM_ATTR);
            document.querySelector(SELECTORS.PATHFIELD_SELECTOR).value = selectedItem;
        }
        //closing popup by cancel button click
        document.querySelector(SELECTORS.CANCEL_BTN).click();
    });

})(document, Granite.$);


