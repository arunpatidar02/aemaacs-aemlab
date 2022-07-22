(function (document, $) {
  "use strict";
   $(document).on("foundation-contentloaded", function() {
     $( ".aemlab-dialog-radio--init").each(function( ) {
      let $radio = $(this);
      let checked = $radio.find("coral-radio[checked]");
      if (checked.length === 0) {
        $radio.find('coral-radio[data-checked]').first().attr('checked', true);
      }
     });
  });
})(document, Granite.$);
