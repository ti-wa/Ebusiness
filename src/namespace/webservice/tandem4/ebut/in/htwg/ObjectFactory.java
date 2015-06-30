
package namespace.webservice.tandem4.ebut.in.htwg;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the namespace.webservice.tandem4.ebut.in.htwg package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UpdateRequest_QNAME = new QName("http://htwg.in.ebut.tandem4.webservice.namespace", "updateRequest");
    private final static QName _UpdateFault_QNAME = new QName("http://htwg.in.ebut.tandem4.webservice.namespace", "updateFault");
    private final static QName _UpdateResponse_QNAME = new QName("http://htwg.in.ebut.tandem4.webservice.namespace", "updateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: namespace.webservice.tandem4.ebut.in.htwg
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UpdateFaultType }
     * 
     */
    public UpdateFaultType createUpdateFaultType() {
        return new UpdateFaultType();
    }

    /**
     * Create an instance of {@link UpdateRequestType }
     * 
     */
    public UpdateRequestType createUpdateRequestType() {
        return new UpdateRequestType();
    }

    /**
     * Create an instance of {@link UpdateResponseType }
     * 
     */
    public UpdateResponseType createUpdateResponseType() {
        return new UpdateResponseType();
    }

    /**
     * Create an instance of {@link SupplierProductType }
     * 
     */
    public SupplierProductType createSupplierProductType() {
        return new SupplierProductType();
    }

    /**
     * Create an instance of {@link ListOfUnavaiableProductsType }
     * 
     */
    public ListOfUnavaiableProductsType createListOfUnavaiableProductsType() {
        return new ListOfUnavaiableProductsType();
    }

    /**
     * Create an instance of {@link PriceType }
     * 
     */
    public PriceType createPriceType() {
        return new PriceType();
    }

    /**
     * Create an instance of {@link AuthenticationType }
     * 
     */
    public AuthenticationType createAuthenticationType() {
        return new AuthenticationType();
    }

    /**
     * Create an instance of {@link ListOfProductsType }
     * 
     */
    public ListOfProductsType createListOfProductsType() {
        return new ListOfProductsType();
    }

    /**
     * Create an instance of {@link ListOfModifiedProductsType }
     * 
     */
    public ListOfModifiedProductsType createListOfModifiedProductsType() {
        return new ListOfModifiedProductsType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://htwg.in.ebut.tandem4.webservice.namespace", name = "updateRequest")
    public JAXBElement<UpdateRequestType> createUpdateRequest(UpdateRequestType value) {
        return new JAXBElement<UpdateRequestType>(_UpdateRequest_QNAME, UpdateRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://htwg.in.ebut.tandem4.webservice.namespace", name = "updateFault")
    public JAXBElement<UpdateFaultType> createUpdateFault(UpdateFaultType value) {
        return new JAXBElement<UpdateFaultType>(_UpdateFault_QNAME, UpdateFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://htwg.in.ebut.tandem4.webservice.namespace", name = "updateResponse")
    public JAXBElement<UpdateResponseType> createUpdateResponse(UpdateResponseType value) {
        return new JAXBElement<UpdateResponseType>(_UpdateResponse_QNAME, UpdateResponseType.class, null, value);
    }

}
