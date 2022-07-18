(function ($, $document) {
    "use strict";
    var flag = true;
    $document.on("dialog-ready", function () {

        const DIALOG_ACTION_HEADER_SELECTOR = '.cq-Dialog coral-dialog-header.cq-dialog-header>div.cq-dialog-actions';
        const GLOBAL_SIDE_PANEL_BUTTON_SELECTOR = '.editor-GlobalBar coral-actionbar-primary coral-actionbar-item button.toggle-sidepanel';
        const ACTION_HEADER_BUTTONS_SELECTOR = DIALOG_ACTION_HEADER_SELECTOR + '>button';
        const ACTION_HEADER_HELP_BUTTON_SELECTOR = ACTION_HEADER_BUTTONS_SELECTOR + ':nth-child(1)';
        const ACTION_HEADER_SIDEPANEL_BUTTON_SELECTOR = 'button.cq-dialog-sidepaneltoggle';

        var sidePanelToggleBtnHTML = '<button is="coral-button" icon="railLeft" variant="minimal" class="cq-dialog-header-action cq-dialog-sidepaneltoggle _coral-Button _coral-Button--secondary _coral-Button--quiet" type="button" title="Toggle Side Panel" size="M">';
        sidePanelToggleBtnHTML += '<coral-icon size="S" class="_coral-Icon--sizeS _coral-Icon" role="img" icon="railLeft">';
        sidePanelToggleBtnHTML += '<svg focusable="false" aria-hidden="true" class="_coral-Icon--svg _coral-Icon"><use xlink:href="#spectrum-icon-18-RailLeft"></use></svg>';
        sidePanelToggleBtnHTML += '</coral-icon><coral-button-label class="_coral-Button-label"></coral-button-label>';
        sidePanelToggleBtnHTML += '</button>';

        if ($(ACTION_HEADER_BUTTONS_SELECTOR).length > 2) {
            return;
        }

        $(ACTION_HEADER_HELP_BUTTON_SELECTOR).after(sidePanelToggleBtnHTML);

        if (flag) {
            $(document).on('click', ACTION_HEADER_SIDEPANEL_BUTTON_SELECTOR, function () {
                $(GLOBAL_SIDE_PANEL_BUTTON_SELECTOR).click();
                flag = false;
            });
        }

    });

})($, $(document));