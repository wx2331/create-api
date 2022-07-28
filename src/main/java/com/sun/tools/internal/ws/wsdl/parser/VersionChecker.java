/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*     */ public class VersionChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*  54 */   private String version = null;
/*     */ 
/*     */   
/*     */   private boolean seenRoot = false;
/*     */ 
/*     */   
/*     */   private boolean seenBindings = false;
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */   
/*     */   private Locator rootTagStart;
/*     */ 
/*     */   
/*     */   public VersionChecker(XMLReader parent) {
/*  70 */     setParent(parent);
/*     */   }
/*     */   
/*     */   public VersionChecker(ContentHandler handler, ErrorHandler eh, EntityResolver er) {
/*  74 */     setContentHandler(handler);
/*  75 */     if (eh != null) setErrorHandler(eh); 
/*  76 */     if (er != null) setEntityResolver(er);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  82 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  84 */     if (!this.seenRoot) {
/*     */       
/*  86 */       this.seenRoot = true;
/*  87 */       this.rootTagStart = new LocatorImpl(this.locator);
/*     */       
/*  89 */       this.version = atts.getValue("http://java.sun.com/xml/ns/jaxws", "version");
/*  90 */       if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxws")) {
/*  91 */         String version2 = atts.getValue("", "version");
/*  92 */         if (this.version != null && version2 != null) {
/*     */ 
/*     */           
/*  95 */           SAXParseException e = new SAXParseException(WsdlMessages.INTERNALIZER_TWO_VERSION_ATTRIBUTES(), this.locator);
/*  96 */           getErrorHandler().error(e);
/*     */         } 
/*     */         
/*  99 */         if (this.version == null) {
/* 100 */           this.version = (version2 != null) ? version2 : "2.0";
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if ("http://java.sun.com/xml/ns/jaxws".equals(namespaceURI)) {
/* 106 */       this.seenBindings = true;
/* 107 */       if (this.version == null) {
/* 108 */         this.version = "2.0";
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 114 */     super.endDocument();
/*     */     
/* 116 */     if (this.seenBindings && this.version == null) {
/*     */       
/* 118 */       SAXParseException e = new SAXParseException(WsdlMessages.INTERNALIZER_VERSION_NOT_PRESENT(), this.rootTagStart);
/* 119 */       getErrorHandler().error(e);
/*     */     } 
/*     */ 
/*     */     
/* 123 */     if (this.version != null && !VERSIONS.contains(this.version)) {
/* 124 */       SAXParseException e = new SAXParseException(WsdlMessages.INTERNALIZER_INCORRECT_VERSION(), this.rootTagStart);
/* 125 */       getErrorHandler().error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 130 */     super.setDocumentLocator(locator);
/* 131 */     this.locator = locator;
/*     */   }
/*     */   
/* 134 */   private static final Set<String> VERSIONS = new HashSet<>(Arrays.asList(new String[] { "2.0", "2.1" }));
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\VersionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */