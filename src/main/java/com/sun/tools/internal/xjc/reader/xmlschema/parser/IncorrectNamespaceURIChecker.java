/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public class IncorrectNamespaceURIChecker
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   private ErrorHandler errorHandler;
/*     */   private Locator locator;
/*     */   private boolean isJAXBPrefixUsed;
/*     */   private boolean isCustomizationUsed;
/*     */   
/*     */   public IncorrectNamespaceURIChecker(ErrorHandler handler) {
/*  74 */     this.locator = null;
/*     */ 
/*     */     
/*  77 */     this.isJAXBPrefixUsed = false;
/*     */     
/*  79 */     this.isCustomizationUsed = false;
/*     */     this.errorHandler = handler;
/*     */   }
/*     */   public void endDocument() throws SAXException {
/*  83 */     if (this.isJAXBPrefixUsed && !this.isCustomizationUsed) {
/*     */       
/*  85 */       SAXParseException e = new SAXParseException(Messages.format("IncorrectNamespaceURIChecker.WarnIncorrectURI", new Object[] { "http://java.sun.com/xml/ns/jaxb" }), this.locator);
/*     */       
/*  87 */       this.errorHandler.warning(e);
/*     */     } 
/*     */     
/*  90 */     super.endDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/*  95 */     if ("http://www.w3.org/XML/1998/namespace".equals(uri))
/*  96 */       return;  if (prefix.equals("jaxb"))
/*  97 */       this.isJAXBPrefixUsed = true; 
/*  98 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/*  99 */       this.isCustomizationUsed = true;
/*     */     }
/* 101 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 106 */     if ("xml".equals(prefix))
/* 107 */       return;  super.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 113 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb")) {
/* 121 */       this.isCustomizationUsed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 126 */     super.setDocumentLocator(locator);
/* 127 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\parser\IncorrectNamespaceURIChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */