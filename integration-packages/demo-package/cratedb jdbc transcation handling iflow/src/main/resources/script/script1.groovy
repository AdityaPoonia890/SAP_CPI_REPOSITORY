import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {

    def body = message.getBody(String)
    def json = new JsonSlurper().parseText(body)

    // Store table name as property — used by all route scripts
    message.setProperty("tableName", json.table?.toString()?.toUpperCase())

    // Store the raw body as a property so routes can re-read it
    // (each route script will re-parse what it needs)
    message.setProperty("rawPayload", body)

    // Log the HTTP method for traceability
    def method = message.getHeaders().get("CamelHttpMethod")
    message.setProperty("httpMethod", method)

    return message
}