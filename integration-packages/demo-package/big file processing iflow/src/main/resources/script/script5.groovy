import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(String)
    def header = message.getProperty("header")

    def newBody = header + ",status\n" + body

    message.setBody(newBody)
    return message
}
