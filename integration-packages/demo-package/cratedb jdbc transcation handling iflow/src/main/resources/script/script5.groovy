import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

def Message processData(Message message) {

    def payload   = message.getProperties().get("rawPayload")
    def tableName = message.getProperties().get("tableName")
    def json      = new JsonSlurper().parseText(payload)
    def records   = json.records   // Each record = one WHERE condition set

    def writer = new StringWriter()
    def xml    = new MarkupBuilder(writer)

    xml.root {
        records.each { record ->
            StatementName {
                dbTableName(action: "DELETE") {
                    table("doc.${tableName.toLowerCase()}")
                    keys {     // All columns in DELETE are WHERE conditions
                        record.each { col, val ->
                            "${col.toLowerCase()}"(escapeXml(val?.toString() ?: ""))
                        }
                    }
                }
            }
        }
    }

    message.setBody(writer.toString())
    message.setHeader("Content-Type", "application/xml")
    return message
}

def String escapeXml(String val) {
    return val.replace("&", "&amp;").replace("<", "&lt;")
              .replace(">", "&gt;").replace("\"", "&quot;")
              .replace("'", "&apos;")
}