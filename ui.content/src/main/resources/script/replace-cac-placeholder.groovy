/* groovylint-disable CompileStatic, VariableTypeRequired */
import groovy.json.JsonSlurper
import groovy.io.FileType
import java.nio.file.Files
import java.util.regex.Matcher
import java.util.regex.Pattern

/* START */
decorativeLog("Groovy Execution STARTED")

/* CONSTANTS */
final Boolean dryRun = false
final String module = "/ui.content"
def placeholderPattern = /\$\{ph_caconfig_[^}]+\}/


/* Configured Properties */
String basedir = properties.get("baseDir")
String logLevel = properties.get("logLevel")
String runMode = properties.get("env")
String dataFilePath = properties.get("configData")

final String targetDir = basedir + (basedir.contains(module) ? "" : module) + "/target/classes/conf/aemlab"


/* Execution Context */
def successList = []
def failedList = []

/* Log Run Mode */
def startMessage = "Execution mode : ${dryRun ? 'dryRun' : 'Replacement'}"
log.info(startMessage)

/* Load JSON Data */
def jsonSlurper = new JsonSlurper()
def jsonData = jsonSlurper.parse(new File(dataFilePath))
def configData = jsonData['runmodes-configs'][runMode]?.get(0)

if (!configData) {
    log.warn("Error: No configuration found for run mode: ${runMode}")
    System.exit(1)
}

log.info("Target Folder for replacement: ${targetDir}")

// Check if the target directory exists
def dir = new File(targetDir)
if (!dir.exists() || !dir.isDirectory()) {
    log.warn("Directory does not exist: ${targetDir}")
    return
}

log.info("Traversing files ")
dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.xml/) { File xmlFile ->
    def originalContent = xmlFile.text
    def updatedContent = originalContent
    def missingPlaceholders = []
	log.debug(xmlFile.path)

	/* Find and Replace Placeholders */
    Matcher matcher = Pattern.compile(/\$\{ph_caconfig_[^}]+\}/).matcher(originalContent)
    while (matcher.find()) {
        def placeholder = matcher.group()
        def placeholderKey = placeholder.replaceAll(/[\$\{\}]/, '')
		log.debug("$placeholderKey : ${configData[placeholderKey]}")

        if (configData.containsKey(placeholderKey)) {
            updatedContent = updatedContent.replace(placeholder, configData[placeholderKey])
        } else {
            missingPlaceholders << placeholder
        }
    }

    /* Write Updated Content if Changed */
    if (originalContent != updatedContent) {
        if (!dryRun) {
            Files.write(xmlFile.toPath(), updatedContent.bytes)
        }
        log.debug("Updated placeholders in file: ${xmlFile.path}")
        successList << xmlFile.path
    }

    /* Log Missing Placeholders */
    if (missingPlaceholders) {
        log.debug("Missing placeholders in ${xmlFile.path}: ${missingPlaceholders.join(', ')}")
        failedList << xmlFile.path
    }
}


/* Log Results */
logResults("Successfully replaced placeholders:", successList)
logResults("Failed to replace placeholders:", failedList)


decorativeLog("Groovy Execution END")

/************************************************************************************
 ************************************ Generic functions *****************************
 ************************************************************************************/


/* Decorative Logging */
def decorativeLog(message) {
    log.info("\n##########################################################################################")
    log.info("####################\t${message}\t####################")
    log.info("##########################################################################################\n")
}

/* Log Results */
def logResults(title, list) {
    log.info("\n${title}")
    list.sort().each { log.info("\t${it}") }
}

