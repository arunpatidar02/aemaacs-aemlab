(function($, $document) {
    "use strict";


    $(document).on("dialog-ready", function() {
        var breadcrumbItem = '<div class="breadcrumb-rte"><p class="placeholder">Selected item breadcrumb will be shown here, and allow to select parents by clicking on breadcrumb</p></div>';
        setTimeout(function() {
            $('.coral-RichText[data-showbreadcrumb="true"]').each(function() {
                $(this).parent().find(".rte-sourceEditor").after(breadcrumbItem);
            });


            $('.coral-RichText[data-showbreadcrumb="true"]').click(function(e) {
                var $target = $(e.target);
                if (!$target.hasClass("coral-RichText")) {
                    var xpath = getXPath(e.target);
                    $(this).parent().find('.breadcrumb-rte').html(createBreadCrumb(xpath, false));
                }
            });


        }, 1000);
    });


    $(document).on("click", ".item-xpath", function(e) {
        var xpath = $(this).data("xpath");
        setCursor(getElementByXpath(xpath));
    });

    function createBreadCrumb(xpath, sep) {
        var separator = '<div aria-hidden="true" class="item-sep"> >> </div>';
        var tagName = xpath.substr(xpath.lastIndexOf("/") + 1)
        var breadcrumbStr = '<div role="button" class="item-xpath" data-xpath="' + xpath.replace(/\|/g, "/") + '">' + tagName + '</div>'
        if (sep) {
            breadcrumbStr = breadcrumbStr + separator;
        }
        var subPath = xpath.substr(0, xpath.lastIndexOf("/"));
        if (subPath.indexOf("/") > -1)
            breadcrumbStr = createBreadCrumb(subPath, true) + breadcrumbStr;
        return breadcrumbStr;
    }


    function setCursor(element) {
        var el = element;
        var range = document.createRange();
        var sel = window.getSelection();
        range.setStart(el, 0);
        range.setEnd(el, el.childNodes.length);
        //  range.collapse(true);
        sel.removeAllRanges();
        sel.addRange(range);
    }


    function getXPath(element) {
        var xpath = '';
        var separator = '/';
        for (; element && element.nodeType == 1; element = element.parentNode) {
            if ($(element).hasClass("coral-RichText")) {
                separator = '|';
            }
            var id = $(element.parentNode).children(element.tagName).index(element) + 1;
            id > 1 ? (id = '[' + id + ']') : (id = '');
            xpath = separator + element.tagName.toLowerCase() + id + xpath;

        }
        return xpath;
    }


    function getElementByXpath(path) {
        return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
    }


})($, $(document));