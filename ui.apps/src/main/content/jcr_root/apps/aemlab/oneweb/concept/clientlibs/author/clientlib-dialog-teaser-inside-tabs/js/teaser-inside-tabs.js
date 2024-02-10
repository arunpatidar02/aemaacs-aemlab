(function (document, $) {
    "use strict";
  
    const PARENT_TYPE = 'wknd/components/tabs';
    const TYPE_PROPERTY = 'sling:resourceType';
    const FORM_SELECTOR = '.cq-Dialog form.cq-dialog';
  
    $(document).on("foundation-contentloaded", function () {
      console.log('loaded');
  
      const dialogForm = document.querySelector(FORM_SELECTOR);
      const contentPath = dialogForm.action;
      const parentPath = getContainerPath(contentPath);
      console.log(parentPath);
  
      fetchParentResourceType(parentPath)
        .then(type => {
          if (type === PARENT_TYPE) {
            console.log('Tabs component');
            // write logic to hide few fields
          }
        })
        .catch(error => {
          console.error('Fetch error:', error);
        });
    });
  
    function getContainerPath(contentPath) {
      const url = new URL(contentPath);
      const pathSegments = url.pathname.split('/');
      const newPathSegments = pathSegments.slice(0, -2);
      return newPathSegments.join('/');
    }
  
    function fetchParentResourceType(parentPath) {
      return fetch(`${parentPath}.json`)
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => data?.[TYPE_PROPERTY])
        .catch(error => {
          throw new Error('Fetch error:', error);
        });
    }
  
  })(document, Granite.$);
  