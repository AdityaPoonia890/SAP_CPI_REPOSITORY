import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {

    def payload    = message.getProperties().get("rawPayload")
    def json       = new JsonSlurper().parseText(payload)
    def conditions = json.conditions
    def columns    = json.columns

    def colList = columns.collect { it.toLowerCase() }.join(", ")
    def sql     = "SELECT ${colList} FROM doc.employees"

    if (conditions && !conditions.isEmpty()) {
        def where = conditions.collect { col, val -> "${col.toLowerCase()} = '${val}'" }.join(" AND ")
        sql      += " WHERE ${where}"
    }

    message.setBody(sql)
    return message
}