(function (window, Granite, $) {
  const CONST = {
    SELECTOR: '[data-foundation-validation=tag-namespace-validator]',
    ATTR_NAME: 'name',
    ERROR_MESSAGE: 'Valid Namespaces are : {0}',
    ATTR_NAMESAPCE: 'validnamespaces',
    COLON: ':',
    COMMA: ',',
  };

  $(window).adaptTo('foundation-registry').register('foundation.validation.validator', {

    selector: CONST.SELECTOR,
    validate(el) {
      const validNamesapces = el.dataset.validnamespaces?.split(CONST.COMMA);
      const eleNameProp = el.getAttribute(CONST.ATTR_NAME);
      let inValid = false;

      const tagInputs = el.parentElement.querySelectorAll(`input[name="${eleNameProp}"]`);
      tagInputs.forEach((input) => {
        const { value } = input;
        if (!validNamesapces.includes(value?.split(CONST.COLON)[0])) {
          inValid = true;
        }
      });

      if (inValid) {
        return Granite.I18n.get(CONST.ERROR_MESSAGE.replace('{0}', validNamesapces));
      }
      return '';
    },
  });
}(window, Granite, Granite.$));
