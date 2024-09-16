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
logMessagePrefix = "[GROOVY]"

/* Configured Properties */
String basedir = properties.get("baseDir")
String logLevel = properties.get("logLevel")
String runMode = properties.get("env")
String dataFilePath = properties.get("configData")

/* Execution Context */
def successList = []
def failedList = []

currentLogLevel = logLevel != null ? logLevel.toInteger() : 1

/* Log Run Mode */
def startMessage = "Execution mode: ${dryRun ? 'dryRun' : 'Replacement'}, LOG LEVEL: ${messageLevel(currentLogLevel)}\n"
log.info(startMessage)

/* Load JSON Data */
def jsonSlurper = new JsonSlurper()
def jsonData = jsonSlurper.parse(new File(dataFilePath))
def configData = jsonData['runmodes-configs'][runMode]?.get(0)

if (!configData) {
    logging("Error: No configuration found for run mode: ${runMode}", 3)
    System.exit(1)
}

final String targetDir = basedir + (basedir.contains(module) ? "" : module) + "/target/classes/conf/aemlab"
logging("Target Folder for replacement: ${targetDir}")

// Check if the target directory exists
def dir = new File(targetDir)
if (!dir.exists() || !dir.isDirectory()) {
    logging("Directory does not exist: ${targetDir}", 3)
    return
}

logging("Traversing files")
dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.xml/) { File xmlFile ->
    def originalContent = xmlFile.text
    def updatedContent = originalContent
    def missingPlaceholders = []
    logging(xmlFile.path, 1)

    /* Find and Replace Placeholders */
    Matcher matcher = Pattern.compile(placeholderPattern).matcher(originalContent)
    while (matcher.find()) {
        def placeholder = matcher.group()
        def placeholderKey = placeholder.replaceAll(/[\$\{\}]/, '')
        logging("$placeholderKey: ${configData[placeholderKey]}", 1)

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
        logging("Updated placeholders in file: ${xmlFile.path}", 1)
        successList << xmlFile.path
    }

    /* Log Missing Placeholders */
    if (missingPlaceholders) {
        logging("Missing placeholders in ${xmlFile.path}: ${missingPlaceholders.join(', ')}", 3)
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
    def border = "#".multiply(90)
    def colorReset = "\u001B[0m"
    def colorGreen = "\u001B[32m"
    
    log.info("\n${border}")
    log.info("${colorGreen}#".padRight(20) + "\t${message}\t" + "#".padLeft(20) + colorReset)
    log.info("${border}\n")
}

// Logs results with title and sorted list
def logResults(title, list) {
    logging(title)
    list.sort().each { log.info("\t\t\t${it}") }
}

// Logging with specified indentation and log level
def logging(message, level = 2) {
    def colorReset = "\u001B[0m"
    def colorBlue = "\u001B[34m"    // Blue for INFO
    def colorYellow = "\u001B[33m"  // Yellow for WARN
    def colorCyan = "\u001B[36m"    // Cyan for DEBUG
    
    def logColor = ""
    
    switch (level) {
        case 1: // DEBUG level
            logColor = colorCyan
            break
        case 2: // INFO level
            logColor = colorBlue
            break
        case 3: // WARN level
            logColor = colorYellow
            break
        default: // Default INFO level
            logColor = colorBlue
            break
    }
    if (level >= currentLogLevel) {
        log.info("${logColor}${logMessagePrefix}[${messageLevel(level)}]${colorReset} ${message}")
    }
}

// Returns the log level label based on the level number
def messageLevel(level) {
    switch (level) {
        case 0: return "TRACE"
        case 1: return "DEBUG"
        case 3: return "WARN"
        default: return "INFO" // Default to INFO for unrecognized levels
    }
}
