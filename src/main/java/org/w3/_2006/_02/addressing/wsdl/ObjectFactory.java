
package org.w3._2006._02.addressing.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.w3._2006._02.addressing.wsdl package. 
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

    private final static QName _ServiceName_QNAME = new QName("http://www.w3.org/2006/02/addressing/wsdl", "ServiceName");
    private final static QName _InterfaceName_QNAME = new QName("http://www.w3.org/2006/02/addressing/wsdl", "InterfaceName");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.w3._2006._02.addressing.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceNameType }
     * 
     */
    public ServiceNameType createServiceNameType() {
        return new ServiceNameType();
    }

    /**
     * Create an instance of {@link AttributedQNameType }
     * 
     */
    public AttributedQNameType createAttributedQNameType() {
        return new AttributedQNameType();
    }

    /**
     * Create an instance of {@link UsingAddressing }
     * 
     */
    public UsingAddressing createUsingAddressing() {
        return new UsingAddressing();
    }

    /**
     * Create an instance of {@link Anonymous }
     * 
     */
    public Anonymous createAnonymous() {
        return new Anonymous();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2006/02/addressing/wsdl", name = "ServiceName")
    public JAXBElement<ServiceNameType> createServiceName(ServiceNameType value) {
        return new JAXBElement<ServiceNameType>(_ServiceName_QNAME, ServiceNameType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedQNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2006/02/addressing/wsdl", name = "InterfaceName")
    public JAXBElement<AttributedQNameType> createInterfaceName(AttributedQNameType value) {
        return new JAXBElement<AttributedQNameType>(_InterfaceName_QNAME, AttributedQNameType.class, null, value);
    }

}
