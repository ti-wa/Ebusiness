package namespace.webservice.tandem4.ebut.in.htwg;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;


	@ServiceMode(value = javax.xml.ws.Service.Mode.PAYLOAD)
	@WebServiceProvider(serviceName = "UpdateCatalogWebService", portName = "updateCatalogPort", targetNamespace = "http://localhost:8084/EBUT_Wholesaler/updatecatalog", wsdlLocation = "WEB-INF/wsdl/UpdateCatalogImpl/updateCatalogue.wsdl")
	public class UpdateCatalogImpl implements javax.xml.ws.Provider<javax.xml.transform.Source>, UpdateCatalogPortType {
	
	    public javax.xml.transform.Source invoke(javax.xml.transform.Source source) {
	        //TODO implement this method
	        throw new UnsupportedOperationException("Not implemented yet.");
	    }
	
	    @Override
	    public UpdateResponseType updateCatalog(UpdateRequestType updateRequest) {
	        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	    }
	
	}

