import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body        = message.getBody(String)
    def delimiter   = ","          // change if needed
    def newHeader   = "Status"     // your new column name
    def constValue  = "ACTIVE"     // your constant value
    def lines       = body.readLines()
    def result      = new StringBuilder()

    lines.eachWithIndex { line, index ->
        if (line.trim().isEmpty()) return
        def cols = line.split(delimiter, -1)
        if (index == 0) {
            // Header row — append new column header
            result.append(cols.toList().join(delimiter))
                  .append(delimiter + newHeader + "\n")
        } else {
            // Data row — append constant value
            result.append(cols.toList().join(delimiter))
                  .append(delimiter + constValue + "\n")
        }
    }
    message.setBody(result.toString().trim())
    return message
}