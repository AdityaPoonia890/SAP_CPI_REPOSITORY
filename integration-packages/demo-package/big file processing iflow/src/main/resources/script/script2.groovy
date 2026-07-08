import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper


def Message processData(Message message) {
    
    // Get body as InputStream (safe for CPI)
    def bodyStream = message.getBody(java.io.InputStream)
    
    // Parse XML directly from stream
    def xml = new XmlSlurper().parse(bodyStream)
    
    // Validation check
    if (!xml.CustomerID || xml.CustomerID.text().trim().isEmpty()) {
        throw new RuntimeException("CustomerID is missing or empty")
    }
    
    return message
}
