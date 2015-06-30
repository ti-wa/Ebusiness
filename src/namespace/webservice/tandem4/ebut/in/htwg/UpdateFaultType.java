
package namespace.webservice.tandem4.ebut.in.htwg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateFaultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateFaultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="authenticationFault" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="processingFault" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateFaultType", propOrder = {
    "authenticationFault",
    "processingFault"
})
public class UpdateFaultType {

    protected String authenticationFault;
    protected String processingFault;

    /**
     * Gets the value of the authenticationFault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationFault() {
        return authenticationFault;
    }

    /**
     * Sets the value of the authenticationFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationFault(String value) {
        this.authenticationFault = value;
    }

    /**
     * Gets the value of the processingFault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessingFault() {
        return processingFault;
    }

    /**
     * Sets the value of the processingFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessingFault(String value) {
        this.processingFault = value;
    }

}
