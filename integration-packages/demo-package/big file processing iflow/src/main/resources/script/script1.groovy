import com.sap.gateway.ip.core.customdev.util.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def Message processData(Message message) {

    try {
        def props   = message.getProperties()
        def headers = message.getHeaders()

        // Safely get exception from all possible locations
        def exception = props.get("CamelExceptionCaught")   ?:
                        props.get("camelExceptionCaught")   ?:
                        headers.get("CamelExceptionCaught") ?: null

        // Safe defaults
        def errorMessage = "No error message captured"
        def errorCause   = "No root cause captured"
        def errorType    = "UnknownException"

        if (exception != null) {
            errorMessage = exception.getMessage()               ?: "Empty message"
            errorCause   = exception.getCause()?.getMessage()  ?: "No root cause"
            errorType    = exception.getClass()?.getSimpleName() ?: "UnknownType"
        }

        // Safe timestamp using Date (allowed in CPI sandbox)
        def timestamp = "N/A"
        try {
            timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter
                            .ofPattern("yyyy-MM-dd HH:mm:ss"))
        } catch (Exception te) {
            timestamp = new Date().toString()
        }

        // Safe message ID — using Date().getTime() instead of System.currentTimeMillis()
        def messageId = headers.get("SAP_MessageProcessingLogID") ?:
                        headers.get("messageId")                  ?:
                        "ID-" + new Date().getTime()

        // Set all properties
        message.setProperty("ERROR_MESSAGE",   errorMessage)
        message.setProperty("ERROR_CAUSE",     errorCause)
        message.setProperty("ERROR_TYPE",      errorType)
        message.setProperty("ERROR_TIMESTAMP", timestamp)
        message.setProperty("ERROR_MSG_ID",    messageId)

    } catch (Exception e) {
        // Failsafe fallback — no System class used anywhere
        message.setProperty("ERROR_MESSAGE",   e.getMessage() ?: "Extractor failed")
        message.setProperty("ERROR_CAUSE",     "ErrorExtractor.groovy threw an exception")
        message.setProperty("ERROR_TYPE",      "ScriptException")
        message.setProperty("ERROR_TIMESTAMP", new Date().toString())
        message.setProperty("ERROR_MSG_ID",    "ID-" + new Date().getTime())
    }

    return message
}