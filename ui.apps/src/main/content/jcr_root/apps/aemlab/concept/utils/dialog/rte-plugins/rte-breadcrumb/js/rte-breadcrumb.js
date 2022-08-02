(function ($) {
    "use strict";

    const CONST = {
        SLASH: "/",
        DOT: ".",
        PIPE: "|",
        EMPTY: "",
        BREADCRUMB_SEPARAOR: "›",
        REGEX: /\|/g,
        XPATH_DATA_ATTR: "xpath",
        BREADCRUMB_INITIAL_CONTENT:
            '<div class="breadcrumb-rte"><p class="placeholder">Selected item and its breadcrumb will be shown here, and allow to select any element by clicking on breadcrumb</p></div>',
    };

    const SELECTOR = {
        RTE_ELEMENT: "coral-RichText",
        RTE_SOURCEEDITOR: ".rte-sourceEditor",
        BREADCRUMB_ITEM: ".item-xpath",
        TARGET_RTE: '.coral-RichText[data-showbreadcrumb="true"]',
        BREADCRUMB: ".breadcrumb-rte",
    };

    $(document).on("dialog-ready", function () {
        setTimeout(function () {
            $(SELECTOR.BREADCRUMB).remove();
            $(SELECTOR.TARGET_RTE).each(function () {
                var breadcrumbNotExists =
                    $(this).find(SELECTOR.BREADCRUMB).text().length == 0 ? true : false;
                if (breadcrumbNotExists) {
                    $(this)
                        .parent()
                        .find(SELECTOR.RTE_SOURCEEDITOR)
                        .after(CONST.BREADCRUMB_INITIAL_CONTENT);
                }
            });

            $(SELECTOR.TARGET_RTE).click(function (e) {
                var $target = $(e.target);
                if (!$target.hasClass(SELECTOR.RTE_ELEMENT)) {
                    var xpath = getXPath(e.target);
                    $(this)
                        .parent()
                        .find(SELECTOR.BREADCRUMB)
                        .html(createBreadCrumb(xpath, false));
                }
            });
        }, 1000);
    });

    $(document).on("click", SELECTOR.BREADCRUMB_ITEM, function (e) {
        var xpath = $(this).data(CONST.XPATH_DATA_ATTR);
        setCursor(getElementByXpath(xpath));
    });

    function createBreadCrumb(xpath, sep) {
        var separator =
            '<div aria-hidden="true" class="item-sep">' +
            CONST.BREADCRUMB_SEPARAOR +
            "</div>";
        var tagName = getTagName(xpath);
        var breadcrumbStr =
            '<div role="button" class="item-xpath" data-xpath="' +
            replacePipeToSlash(xpath) +
            '">' +
            tagName +
            "</div>";
        if (sep) {
            breadcrumbStr = breadcrumbStr + separator;
        }
        var subPath = xpath.substr(0, xpath.lastIndexOf(CONST.SLASH));
        if (subPath.indexOf(CONST.SLASH) > -1)
            breadcrumbStr = createBreadCrumb(subPath, true) + breadcrumbStr;
        return breadcrumbStr;
    }

    function setCursor(element) {
        var el = element;
        var range = document.createRange();
        var sel = window.getSelection();
        range.setStart(el, 0);
        range.setEnd(el, el.childNodes.length);
        sel.removeAllRanges();
        sel.addRange(range);
    }

    function getXPath(element) {
        var xpath = CONST.EMPTY;
        var separator = CONST.SLASH;
        for (; element && element.nodeType == 1; element = element.parentNode) {
            if ($(element).hasClass(SELECTOR.RTE_ELEMENT)) {
                separator = CONST.PIPE;
            }
            var id =
                $(element.parentNode).children(element.tagName).index(element) + 1;
            id = id > 1 ? "[" + id + "]" : CONST.EMPTY;
            xpath = separator + element.tagName.toLowerCase() + id + xpath;
        }
        return xpath;
    }

    function getTagName(xpath) {
        var tagName = xpath.substr(xpath.lastIndexOf(CONST.SLASH) + 1);
        var doc = getElementByXpath(replacePipeToSlash(xpath));
        var classList = Array.from(doc.classList).join(CONST.DOT);
        return tagName + (classList ? CONST.DOT + classList : CONST.EMPTY);
    }

    function replacePipeToSlash(xpath) {
        return xpath.replace(CONST.REGEX, CONST.SLASH);
    }

    function getElementByXpath(path) {
        return document.evaluate(
            path,
            document,
            null,
            XPathResult.FIRST_ORDERED_NODE_TYPE,
            null
        ).singleNodeValue;
    }
})($);
