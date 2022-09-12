# aemaacs-aemlab

This is the code for the demo, forum contribution . The project has been designed for **AEM as a Cloud Service**.

There is also a corresponding tutorial where you can learn how to implement a website using the latest standards and technologies in Adobe Experience Manager as a Cloud Service (AEMaaCS):

1. [Project Setup](https://github.com/adobe/aem-project-archetype)
2. [Component Basics](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/component-basics.html)
3. [Pages and Templates](https://docs.adobe.com/content/help/en/experience-manager-65/developing/platform/templates/page-templates-editable.html)
4. [Client-Side Libraries](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/client-side-libraries.html#organization)
5. [Style a Component](https://docs.adobe.com/content/help/en/experience-manager-learn/sites/page-authoring/style-system-feature-video-use.html)
6. [Custom Component](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/custom-component.html)
7. [Unit Testing](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/unit-testing.html)

## Modules

The main parts of the project are:

- **core**: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
- **ui.apps**: contains the /apps (and /etc) parts of the project, ie JS & CSS clientlibs, components, templates, runmode specific configs
- **ui.content**: contains mutable content (not /apps) that is integral to the running of the site. This include template types, templates, policies, users and base-line organization page and asset structures.
- **ui.acl**: contains the acl specific configs yaml files
- **dispatcher.cloud**: contains dispatcher configurations for AEM as a Cloud Service
- **repository-structure**: Empty package that defines the structure of the Adobe Experience Manager repository the Code packages in this project deploy into.
- **all**: An empty module that embeds the above sub-modules and any vendor dependencies into a single deployable package.

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

```
mvn clean install
```

If you have a running AEM instance you can build and package the whole project using the `all` module with:

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
Or to deploy only `ui.apps` to the author, run
```
cd ui.apps
mvn clean install-PautoInstallPackage
```
Or to deploy only the bundle to the author, run
```
cd core
mvn clean install -PautoInstallBundle 
```

## Testing

There are three levels of testing contained in the project:

unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:

```
mvn clean test
```

## Dispatcher Setup

To Setup dispatcher in local you need to follow the detail document at https://docs.adobe.com/content/help/en/experience-manager-learn/cloudservice/local-development-environment-set-up/dispatcher-tools.html


### Download Docker Desktop
- Download docker desktop from https://www.docker.com/get-started
- Install Docker Desktop with Hyper -V feature enable(Hyper-V feature is required for windows only, you need to restart Windows after installing docker).

> **Caution**: When you enabled the Hyper-V feature in windows it will disabled the Wifi and you may no longer to use wifi with Hyper-V feature enable. To use wifi again, you need to disabled Hyper-V feature from windows.

- Start the docker.


### Download the Dispatcher Tools (as part of the AEM SDK)

The AEM as a Cloud Service SDK, or AEM SDK, contains the Dispatcher Tools used to run Apache HTTP Web server with the Dispatcher module locally for development, as well as the compatible QuickStart Jar. If the AEM as a Cloud Service SDK has already been downloaded to [setup the local AEM runtime](https://docs.adobe.com/content/help/en/experience-manager-learn/cloud-service/local-development-environment-set-up/aem-runtime.html) , it does not need to be re-downloaded. Otherwise you can download it from https://experience.adobe.com/#/downloads

### Extract the Dispatcher Tools from the AEM SDK zip

The version of Dispatcher Tools is different from that of the AEM SDK. Ensure the version of Dispatcher Tools is provided via the AEM SDK version matching the AEM as a Cloud Service version.
1. Unzip the downloaded aem-sdk-xxx.zip file
2. Unpack the Dispatcher Tools into ~/aem-sdk/dispatcher

- Windows: Unzip aem-sdk-dispatcher-tools-x.x.x-windows.zip into C:\Users\My User\aem-sdk\dispatcher (creating missing folders as needed)

- macOS / Linux: Execute the accompanying shell script aem-sdk-dispatcher-tools-x.x.x-unix.sh to unpack the Dispatcher Tools

- chmod a+x aem-sdk-dispatcher-tools-x.x.x-unix.sh && ./aem-sdk-dispatcher-tools-x.x.x-unix.sh

### Run Dispatcher locally

To run the Dispatcher locally, the Dispatcher configuration files to be used to configure it, must be validated using the Dispatcher Tools's validator CLI tool.

Usage:
- **Windows:**  `bin\validator full -d out src`
- **macOS / Linux:**  `./bin/validator full -d ./out ./src`
- relaxed(beta) -> `rm -rf ./out && ./bin/validator full -relaxed ./src`

Once validated, the transpiled configurations are used run Dispatcher locally in the Docker container.

Usage:
- **Windows:**  `bin\docker_run <deployment-folder> <aem-publish-host>:<aem-publish-port> <dispatcher-port>`
- **macOS / Linux:**  `./bin/docker_run.sh <deployment-folder> <aem-publish-host>:<aem-publish-port> <dispatcher-port>`

> Example :
Windows: `bin\docker_run out host.docker.internal:4503 8080`
macOS / Linux: `./bin/docker_run.sh ./out host.docker.internal:4503 8080`

> The AEM as a Cloud Service SDK's Publish Service, running locally on port 4503 will be available through Dispatcher at http://localhost:8080 .

> To check caching or server logs use Docker CLI or check the folder inside dispatcher-sdk folder

To run Dispatcher Tools against an Experience Manager project's Dispatcher configuration, simply generate the deployment-folder using the project's dispatcher/src folder.

- Windows:
```
$ del -/Q out && bin\validator full -d out /User Directory/aemaacs-aemlab/dispatcher.cloud/src
```
```
$ bin\docker_run out host.docker.internal:4503 8080
```
  
- macOS / Linux:
```
$ rm -rf ./out && ./bin/validator full -d ./out ~/aemaacs-aemlab/dispatcher.cloud/src
```
```
$ ./bin/docker_run.sh ./out host.docker.internal:4503 8080
```
  

## Features

1. ACS Common
2. AC Tool
3. WCM.io CA editor
4. SDI example
5. Rewriter Pipeline
6. Multi tenant project structure
7. Best practices followed
8. Sample code

### TO DO
- Junit test cases
- More custom component examples

## Reference Content

This contains pages created using core components(v2.10.0) and basic styling to demonstrate how to use CSS and AEM style system to setup a basic site using core components.

Sample pages are at [https://github.com/arunpatidar02/aemaacs-aemlab/tree/master/ui.content/src/main/content/jcr_root/content/aemlab/oneweb/reference-content](https://github.com/arunpatidar02/aemaacs-aemlab/tree/master/ui.content/src/main/content/jcr_root/content/aemlab/oneweb/reference-content)

**Example**

[https://github.com/arunpatidar02/aemaacs-aemlab/tree/master/ui.content/src/main/content/jcr_root/content/aemlab/oneweb/reference-content/embed](https://github.com/arunpatidar02/aemaacs-aemlab/tree/master/ui.content/src/main/content/jcr_root/content/aemlab/oneweb/reference-content/embed)

![embed core component reference content](https://github.com/arunpatidar02/aemaacs-aemlab/blob/master/embed.png)
