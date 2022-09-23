
    (function() {
        "use strict";
        var containerUtils = window.CQ?.CoreComponents?.container?.utils;
        if (!containerUtils) {
            // eslint-disable-next-line no-console
            console.warn("CE: container utilities at window.CQ.CoreComponents.container.utils are not available. This can lead to missing features. Ensure the core.wcm.components.commons.site.container client library is included on the page.");
        }
        var NS = "cmp";
        var IS = "ce";
    
        var selectors = {
            self: "[data-" + NS + '-is="' + IS + '"]',
            active: {
                item: "cmp-ce__item--active",
                itempanel: "cmp-ce__itempanel--active"
            }
        };
    
        function CE(config) {
            var that = this;
            if (config && config.element) {
                init(config);
            }
    
            
            function init(config) {
                that._config = config;
                // prevents multiple initialization
                config.element.removeAttribute("data-" + NS + "-is");
    
                cacheElements(config.element);
                that._active = getActiveIndex(that._elements[IS]);
                if (that._elements.itempanel) {
                    refreshActive();
                    bindEvents();
                    scrollToDeepLinkIdInItems();
                }
    
                if (window.Granite?.author?.MessageChannel) {
                    CQ.CoreComponents.MESSAGE_CHANNEL = CQ.CoreComponents.MESSAGE_CHANNEL || new window.Granite.author.MessageChannel("cqauthor", window);
                    CQ.CoreComponents.MESSAGE_CHANNEL.subscribeRequestMessage("cmp.panelcontainer", function(message) {
                        if (message.data && message.data.type === "cmp-ce" && message.data.id === that._elements.self.dataset["cmpPanelcontainerId"]) {
                            if (message.data.operation === "navigate") {
                                navigate(message.data.index);
                            }
                        }
                    });
                }
            }
    
            /**
             * Displays the panel containing the element that corresponds to the deep link in the URI fragment
             * and scrolls the browser to this element.
             */
            function scrollToDeepLinkIdInItems() {
                if (containerUtils) {
                    var deepLinkItemIdx = containerUtils.getDeepLinkItemIdx(that, IS, "itempanel");
                    if (deepLinkItemIdx && deepLinkItemIdx !== -1) {
                        var deepLinkItem = that._elements[IS][deepLinkItemIdx];
                        if (deepLinkItem && that._elements[IS][that._active].id !== deepLinkItem.id) {
                            navigateAndFocusTab(deepLinkItemIdx, true);
                        }
                        var hashId = window.location.hash.substring(1);
                        if (hashId) {
                            var hashItem = document.querySelector("[id='" + hashId + "']");
                            if (hashItem) {
                                hashItem.scrollIntoView();
                            }
                        }
                    }
                }
            }
    
           
            function getActiveIndex(ceItems) {
                if (ceItems) {
                    for (var i = 0; i < ceItems.length; i++) {
                        if (ceItems[i].classList.contains(selectors.active.item)) {
                            return i;
                        }
                    }
                }
                return 0;
            }
    
            
            function cacheElements(wrapper) {
                that._elements = {};
                that._elements.self = wrapper;
                var hooks = that._elements.self.querySelectorAll("[data-" + NS + "-hook-" + IS + "]");
                
                
                for (var i = 0; i < hooks.length; i++) {
                    var hook = hooks[i];
                   
                    if (hook.closest("." + NS + "-" + IS) === that._elements.self) { // only process own tab elements
                        var capitalized = IS;
                        capitalized = capitalized.charAt(0).toUpperCase() + capitalized.slice(1);
                        var key = hook.dataset[NS + "Hook" + capitalized];
                        if (that._elements[key]) {
                            if (!Array.isArray(that._elements[key])) {
                                var tmp = that._elements[key];
                                that._elements[key] = [tmp];
                            }
                            that._elements[key].push(hook);
                        } else {
                            that._elements[key] = hook;
                        }
                    }
                }
            }
    
            
            function bindEvents() {
                window.addEventListener("hashchange", scrollToDeepLinkIdInItems, false);
                var ceItems = that._elements[IS];
                if (ceItems) {
                    for (var i = 0; i < ceItems.length; i++) {
                        (function(index) {
                            ceItems[i].addEventListener("click", function(event) {
                                navigateAndFocusTab(index);
                            });
                        })(i);
                    }
                }
            }
    
            
    
            
            function refreshActive() {
                var itempanels = that._elements["itempanel"];
                var ceItems = that._elements[IS];
                if (itempanels) {
                    if (Array.isArray(itempanels)) {
                        for (var i = 0; i < itempanels.length; i++) {
                            if (i === parseInt(that._active)) {
                                itempanels[i].classList.add(selectors.active.itempanel);
                                ceItems[i].classList.add(selectors.active.item);
                            } else {
                                itempanels[i].classList.remove(selectors.active.itempanel);
                                ceItems[i].classList.remove(selectors.active.item);
                            }
                        }
                    } else {
                        // only one tab
                        itempanels.classList.add(selectors.active.itempanel);
                        ceItems.classList.add(selectors.active.item);
                    }
                }
            }
    
            /**
             * Focuses the element and prevents scrolling the element into view
             *
             * @param {HTMLElement} element Element to focus
             */
            function focusWithoutScroll(element) {
                var x = window.scrollX || window.pageXOffset;
                var y = window.scrollY || window.pageYOffset;
                element.focus();
                window.scrollTo(x, y);
            }
    
            /**
             * Navigates to the tab at the provided index
             *
             * @private
             * @param {Number} index The index of the tab to navigate to
             */
            function navigate(index) {
                that._active = index;
                refreshActive();
            }
    
            /**
             * Navigates to the item at the provided index and ensures the active tab gains focus
             *
             * @private
             * @param {Number} index The index of the item to navigate to
             * @param {Boolean} keepHash true to keep the hash in the URL, false to update it
             */
            function navigateAndFocusTab(index, keepHash) {
                var exActive = that._active;
                if (!keepHash && containerUtils) {
                    containerUtils.updateUrlHash(that, IS, index);
                }
                navigate(index);
                focusWithoutScroll(that._elements[IS][index]);
            }
        }
    
        
        function readData(element) {
            var data = element.dataset;
           
            var options = [];
            var capitalized = IS;
            capitalized = capitalized.charAt(0).toUpperCase() + capitalized.slice(1);
            var reserved = ["is", "hook" + capitalized];
           
            for (var key in data) {
                if (Object.prototype.hasOwnProperty.call(data, key)) {
                   
                    var value = data[key];
    
                    if (key.indexOf(NS) === 0) {
                        key = key.slice(NS.length);
                        key = key.charAt(0).toLowerCase() + key.substring(1);
                        if (reserved.indexOf(key) === -1) {
                            options[key] = value;
                        }
                    }
                }
            }
    
            return options;
        }
    
        
        function onDocumentReady() {
            var elements = document.querySelectorAll(selectors.self);

            for (var i = 0; i < elements.length; i++) {     
                new CE({ element: elements[i], options: readData(elements[i]) });
            }
    
            var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
            var body = document.querySelector("body");
            var observer = new MutationObserver(function(mutations) {
                mutations.forEach(function(mutation) {
                    // needed for IE
                    var nodesArray = [].slice.call(mutation.addedNodes);
                    if (nodesArray.length > 0) {
                        nodesArray.forEach(function(addedNode) {
                            if (addedNode.querySelectorAll) {
                                var elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                                elementsArray.forEach(function(element) {
                                    new CE({ element: element, options: readData(element) });
                                });
                            }
                        });
                    }
                });
            });
    
            observer.observe(body, {
                subtree: true,
                childList: true,
                characterData: true
            });
        }
    
        if (document.readyState !== "loading") {
            onDocumentReady();
        } else {
            document.addEventListener("DOMContentLoaded", onDocumentReady);
        }
    
        if (containerUtils) {
            window.addEventListener("load", containerUtils.scrollToAnchor, false);
        }
    
    }());
    