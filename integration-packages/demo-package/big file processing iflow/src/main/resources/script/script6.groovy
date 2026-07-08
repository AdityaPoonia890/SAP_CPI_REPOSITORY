import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(String)

    // Normalize ALL line endings to \n
    body = body.replaceAll("\\r\\n", "\n")
    body = body.replaceAll("\\r", "\n")

    // Remove extra blank lines
    body = body.replaceAll("\\n+", "\n").trim()

    def lines = body.split("\\n")

    // Store header
    def header = lines[0].trim()
    message.setProperty("header", header)

    // Take only data rows
    def dataLines = []
    for (int i = 1; i < lines.length; i++) {
        dataLines.add(lines[i].trim())
    }

    def newBody = dataLines.join("\n")

    message.setBody(newBody)
    return message
}
