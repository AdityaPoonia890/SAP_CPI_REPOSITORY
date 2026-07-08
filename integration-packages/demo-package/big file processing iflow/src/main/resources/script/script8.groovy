import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body        = message.getBody(String)
    def delimiter   = ","
    def newHeader   = "Status"      // your new column name
    def constValue  = "ACTIVE"      // your constant value

    def lines  = body.readLines()
    def result = new StringBuilder()

    lines.eachWithIndex { line, index ->
        if (line.trim().isEmpty()) return

        def cols = line.split(delimiter, -1)

        if (index == 0) {
            // Header row — always present in every chunk due to splitter
            result.append(cols.toList().join(delimiter))
                  .append(delimiter + newHeader + "\n")
        } else {
            // Data row — add constant value
            result.append(cols.toList().join(delimiter))
                  .append(delimiter + constValue + "\n")
        }
    }

    // Set the split index as a header for use in filename
    def splitIndex = message.getProperties().get("CamelSplitIndex")
    message.setHeader("SplitIndex", splitIndex?.toString()?.padLeft(4, '0') ?: "0000")

    message.setBody(result.toString().trim())
    return message
}