(function ($, $document) {
    "use strict";
    var flag = true;
    $document.on("dialog-ready", function () {

        const DIALOG_ACTION_HEADER_SELECTOR = '.cq-Dialog coral-dialog-header.cq-dialog-header>div.cq-dialog-actions';
        const GLOBAL_SIDE_PANEL_BUTTON_SELECTOR = '.editor-GlobalBar coral-actionbar-primary coral-actionbar-item button.toggle-sidepanel';
        const ACTION_HEADER_BUTTONS_SELECTOR = DIALOG_ACTION_HEADER_SELECTOR + '>button';
        const ACTION_HEADER_HELP_BUTTON_SELECTOR = ACTION_HEADER_BUTTONS_SELECTOR + ':nth-child(1)';
        const CQ_DIALOG_SIDEPANEL_TOGGLE_CLASS = 'cq-dialog-sidepaneltoggle';
        const ACTION_HEADER_SIDEPANEL_BUTTON_SELECTOR = 'button.' + CQ_DIALOG_SIDEPANEL_TOGGLE_CLASS;
        const CQ_DIALOG_HEADER_ACTION_CLASS = 'cq-dialog-header-action';

        var sidePanelToggleBtn = new Coral.Button().set({
            size: 'M',
            variant: 'minimal',
            icon: 'railLeft',
            iconSize: "S",
            type: 'button',
            title: 'Toggle Side Panel'
        });

        sidePanelToggleBtn.classList.add(CQ_DIALOG_SIDEPANEL_TOGGLE_CLASS, CQ_DIALOG_HEADER_ACTION_CLASS);

        if ($(ACTION_HEADER_BUTTONS_SELECTOR).length > 2) {
            return;
        }

        $(ACTION_HEADER_HELP_BUTTON_SELECTOR).after(sidePanelToggleBtn);

        if (flag) {
            $(document).on('click', ACTION_HEADER_SIDEPANEL_BUTTON_SELECTOR, function () {
                $(GLOBAL_SIDE_PANEL_BUTTON_SELECTOR).click();
                flag = false;
            });
        }

    });

})($, $(document));