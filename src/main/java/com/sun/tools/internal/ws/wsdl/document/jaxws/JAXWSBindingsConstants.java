/*     */ package com.sun.tools.internal.ws.wsdl.document.jaxws;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface JAXWSBindingsConstants
/*     */ {
/*     */   public static final String NS_JAXWS_BINDINGS = "http://java.sun.com/xml/ns/jaxws";
/*     */   public static final String NS_JAXB_BINDINGS = "http://java.sun.com/xml/ns/jaxb";
/*     */   public static final String NS_XJC_BINDINGS = "http://java.sun.com/xml/ns/jaxb/xjc";
/*  65 */   public static final QName JAXWS_BINDINGS = new QName("http://java.sun.com/xml/ns/jaxws", "bindings");
/*     */ 
/*     */   
/*     */   public static final String WSDL_LOCATION_ATTR = "wsdlLocation";
/*     */   
/*     */   public static final String NODE_ATTR = "node";
/*     */   
/*     */   public static final String VERSION_ATTR = "version";
/*     */   
/*  74 */   public static final QName PACKAGE = new QName("http://java.sun.com/xml/ns/jaxws", "package");
/*     */   public static final String NAME_ATTR = "name";
/*  76 */   public static final QName JAVADOC = new QName("http://java.sun.com/xml/ns/jaxws", "javadoc");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final QName ENABLE_WRAPPER_STYLE = new QName("http://java.sun.com/xml/ns/jaxws", "enableWrapperStyle");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final QName ENABLE_ASYNC_MAPPING = new QName("http://java.sun.com/xml/ns/jaxws", "enableAsyncMapping");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final QName ENABLE_ADDITIONAL_SOAPHEADER_MAPPING = new QName("http://java.sun.com/xml/ns/jaxws", "enableAdditionalSOAPHeaderMapping");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final QName ENABLE_MIME_CONTENT = new QName("http://java.sun.com/xml/ns/jaxws", "enableMIMEContent");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public static final QName PROVIDER = new QName("http://java.sun.com/xml/ns/jaxws", "provider");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final QName CLASS = new QName("http://java.sun.com/xml/ns/jaxws", "class");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static final QName METHOD = new QName("http://java.sun.com/xml/ns/jaxws", "method");
/* 146 */   public static final QName PARAMETER = new QName("http://java.sun.com/xml/ns/jaxws", "parameter");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String PART_ATTR = "part";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ELEMENT_ATTR = "childElementName";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static final QName EXCEPTION = new QName("http://java.sun.com/xml/ns/jaxws", "exception");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public static final QName JAXB_BINDINGS = new QName("http://java.sun.com/xml/ns/jaxb", "bindings");
/*     */   public static final String JAXB_BINDING_VERSION = "2.0";
/* 192 */   public static final QName XSD_APPINFO = new QName("http://www.w3.org/2001/XMLSchema", "appinfo");
/* 193 */   public static final QName XSD_ANNOTATION = new QName("http://www.w3.org/2001/XMLSchema", "annotation");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\jaxws\JAXWSBindingsConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */