console.log("client multifields....");
(function (document, $, ns) {
    "use strict";
    $(document).on("click", ".cq-dialog-submit", function (e) {
        var attributelist = $("input[name='./fname']").attr("data-attribute1").split(",");

        console.log("attributelist" + attributelist.size);
        var parentlabel = "./" + $("input[name='./fname']").attr("data-parentlabel");


        var $formminmax = $(this).closest("form.foundation-form");

        var field = $formminmax.find("[data-granite-coral-multifield-name='" + parentlabel + "']");
        var totalLinkCount = field.children('coral-multifield-item').length;
        var test = field.children('coral-Form-fieldwrapper').name;


        var jsonObj = [];

        for (var i = 0; i < totalLinkCount; i++) {
            var item = {}
            console.log("pp" + 'itemXX'.replace('XX', i));

            for (var j = 0; j < attributelist.length; j++) {
                var itemname = './bookdetailswithmap/item' + i + '/./' + attributelist[j];
                item[attributelist[j]] = field.find($("[name='" + itemname + "']")[0]).val();
            }
            jsonObj.push(item);
        }
        var obj2 = { "parentlabel": jsonObj };
        snippet.log(obj2.personalization.name);
        $('.fnameclass').val(JSON.stringify(jsonObj));
    });
})(document, Granite.$, Granite.author);