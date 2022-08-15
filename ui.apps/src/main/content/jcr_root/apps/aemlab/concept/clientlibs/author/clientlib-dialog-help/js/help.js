(function ($) {
    "use strict";

    const CONST = {
        SUCCESS: "success",
        REGEX : "^(?:[a-z+]+:)?//",
        SHOWDOWN_URL:"https://cdnjs.cloudflare.com/ajax/libs/showdown/2.1.0/showdown.min.js",
        README_SCRIPT: "readme-script"
     };

    const SELECTOR = {
        HELP_LINK: ".cq-dialog-header-action.cq-dialog-help._coral-Button",
        DATA_HREF:"data-href",
        DATA_HREF_INIT:"data-href-init",
        SHOWDOWN_SCRIPT: "#" + CONST.README_SCRIPT
    };

    $(document).on("dialog-ready", function () {
        console.log('loading..')



        let helpEle = document.querySelector(SELECTOR.HELP_LINK);
        let href= helpEle?.getAttribute(SELECTOR.DATA_HREF);
        let hrefInit = helpEle?.getAttribute(SELECTOR.DATA_HREF_INIT);

        console.log(helpEle, href,hrefInit);

        if(!href || hrefInit){
            return;
        }
    
        let r = new RegExp(CONST.REGEX, 'i');
        let isAbsPath = r.test(href)

        if(isAbsPath){
            return;
        }

        // Get Readme file content
        $.get(href, function(data, status){
            if(status !== CONST.SUCCESS){
                return
            }

            let scriptEle = document.querySelector(SELECTOR.SHOWDOWN_SCRIPT);
            if(!scriptEle){
                loadScript(convertMarkup);
            }

            function convertMarkup() {
                console.log('loaded', window.showdown)
                const converter = new showdown.Converter();
                const html = converter.makeHtml(data);
                console.log(html);
            }
            

            // convert MD to HTML
            //console.log(data);
        })

        

        function loadScript(callback) {
            var script = document.createElement('script');
            script.type = 'text/javascript';
            script.src = CONST.SHOWDOWN_URL;
            script.id = CONST.README_SCRIPT;
            helpEle.appendChild(script);
            script.onreadystatechange = callback;
            script.onload = callback;
        
            script.onerror = function(err) {
              console.error(err);
            };

           
          }

        
        helpEle?.setAttribute(SELECTOR.DATA_HREF_INIT, true);


        
    });

   
})($);
