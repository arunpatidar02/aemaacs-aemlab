(function (window, Granite, $) {
  const CONST = {
    SELECTOR: '[data-foundation-validation=tag-namespace-validator]',
    ATTR_NAME: 'name',
    ATTR_MULTIPLE: 'multiple',
    ERROR_MESSAGE: 'Max allowed items is {0}',
  };
  $(window).adaptTo('foundation-registry').register('foundation.validation.validator', {

    selector: CONST.SELECTOR,
    validate(el) {
        console.log('Hi');
      let field;
      let value;
      field = $(el);
      console.log(field);

      const valid = false;

      if (!valid) {
        return Granite.I18n.get(CONST.ERROR_MESSAGE);
      }
      return '';
    },
  });
}(window, Granite, Granite.$));
