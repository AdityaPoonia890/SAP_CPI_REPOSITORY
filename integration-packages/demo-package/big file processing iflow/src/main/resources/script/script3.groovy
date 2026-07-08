import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(String)

    // Normalize line endings
    body = body.replaceAll("\\r", "")

    def lines = body.split("\\n")

    // Store header
    def header = lines[0].trim()
    message.setProperty("header", header)

    // Remove header (strictly by index)
    def dataLines = []
    for (int i = 1; i < lines.length; i++) {
        if (lines[i].trim()) {
            dataLines.add(lines[i].trim())
        }
    }

    def newBody = dataLines.join("\n")

    message.setBody(newBody)
    return message
}
