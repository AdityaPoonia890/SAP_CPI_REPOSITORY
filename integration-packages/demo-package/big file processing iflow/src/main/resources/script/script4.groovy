import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(String)

    body = body + ",PROCESSED\n"

    message.setBody(body)
    return message
}
