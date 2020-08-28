# aemaacs-aemlab

This is the code for the demo, forum contribution . The project has been designed for  **AEM as a Cloud Service**.

There is also a corresponding tutorial where you can learn how to implement a website using the latest standards and technologies in Adobe Experience Manager as a Cloud Service (AEMaaCS):

1.  [Project Setup](https://github.com/adobe/aem-project-archetype)
3.  [Component Basics](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/component-basics.html)
4.  [Pages and Templates](https://docs.adobe.com/content/help/en/experience-manager-65/developing/platform/templates/page-templates-editable.html)
5.  [Client-Side Libraries](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/client-side-libraries.html#organization)
6.  [Style a Component]([https://docs.adobe.com/content/help/en/experience-manager-learn/sites/page-authoring/style-system-feature-video-use.html](https://docs.adobe.com/content/help/en/experience-manager-learn/sites/page-authoring/style-system-feature-video-use.html))
7.  [Custom Component](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/custom-component.html)
8.  [Unit Testing](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/unit-testing.html)

## Modules

The main parts of the project are:

-   **core**: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
-   **ui.apps**: contains the /apps (and /etc) parts of the project, ie JS & CSS clientlibs, components, templates, runmode specific configs
-   **ui.content**: contains mutable content (not /apps) that is integral to the running of the site. This include template types, templates, policies, users and base-line organization page and asset structures.
-   **dispatcher.cloud**: contains dispatcher configurations for AEM as a Cloud Service
-   **repository-structure**: Empty package that defines the structure of the Adobe Experience Manager repository the Code packages in this project deploy into.
-   **all**: An empty module that embeds the above sub-modules and any vendor dependencies into a single deployable package.

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

```
mvn clean install

```

If you have a running AEM instance you can build and package the whole project using the  `all`  module with:

```
mvn clean install -PautoInstallSinglePackage

```

Depending on your maven configuration, you may find it helpful to force the resolution of the Adobe public repo with

```
mvn clean install -PautoInstallSinglePackage -Padobe-public

```

Or to deploy it to a publish instance, run

```
mvn clean install -PautoInstallSinglePackagePublish

```

Or alternatively

```
mvn clean install -PautoInstallSinglePackage -Daem.port=4503

```

Or to deploy only  `ui.apps`  to the author, run

```
cd ui.apps
mvn clean install-PautoInstallPackage

```

Or to deploy only the bundle to the author, run

```
cd core
mvn clean install -PautoInstallBundle

```


## [](https://github.com/adobe/aem-guides-wknd#testing)Testing

There are three levels of testing contained in the project:

-   unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:
    
    ```
    mvn clean test
    
    ```

## Reference Content

This contains core components(v2.10.0) and basic styling to demonstrate how to use CSS and AEM style system to setup a basic site.


    
