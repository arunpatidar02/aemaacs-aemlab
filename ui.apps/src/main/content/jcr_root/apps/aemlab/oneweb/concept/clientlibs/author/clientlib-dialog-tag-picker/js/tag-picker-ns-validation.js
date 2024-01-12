(function (window, Granite, $) {
  const CONST = {
    SELECTOR: '[data-foundation-validation=tag-namespace-validator]',
    ATTR_NAME: 'name',
    ERROR_MESSAGE: 'Valid Namespaces are : {0}',
    ATTR_NAMESPACE: 'validnamespaces',
    COLON: ':',
    COMMA: ',',
  };

  const validateTagNamespace = (el) => {
    const validNamespaces = el.dataset[CONST.ATTR_NAMESPACE]?.split(CONST.COMMA) || [];
    const eleNameProp = el.getAttribute(CONST.ATTR_NAME);
    const tagInputs = el.parentElement.querySelectorAll(`input[name="${eleNameProp}"]`);

    return Array.from(tagInputs).some((input) => {
      const { value } = input;
      return !validNamespaces.includes(value?.split(CONST.COLON)[0]);
    });
  };

  $(window).adaptTo('foundation-registry').register('foundation.validation.validator', {
    selector: CONST.SELECTOR,
    validate(el) {
      return validateTagNamespace(el)
        ? Granite.I18n.get(CONST.ERROR_MESSAGE.replace('{0}', el.dataset[CONST.ATTR_NAMESPACE]))
        : '';
    },
  });
}(window, Granite, Granite.$));
