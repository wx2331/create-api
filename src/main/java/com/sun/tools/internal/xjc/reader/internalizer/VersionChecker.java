/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
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
/*  60 */   private String version = null;
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
/*  76 */     setParent(parent);
/*     */   }
/*     */   
/*     */   public VersionChecker(ContentHandler handler, ErrorHandler eh, EntityResolver er) {
/*  80 */     setContentHandler(handler);
/*  81 */     if (eh != null) setErrorHandler(eh); 
/*  82 */     if (er != null) setEntityResolver(er);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  88 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  90 */     if (!this.seenRoot) {
/*     */       
/*  92 */       this.seenRoot = true;
/*  93 */       this.rootTagStart = new LocatorImpl(this.locator);
/*     */       
/*  95 */       this.version = atts.getValue("http://java.sun.com/xml/ns/jaxb", "version");
/*  96 */       if (namespaceURI.equals("http://java.sun.com/xml/ns/jaxb")) {
/*  97 */         String version2 = atts.getValue("", "version");
/*  98 */         if (this.version != null && version2 != null) {
/*     */ 
/*     */           
/* 101 */           SAXParseException e = new SAXParseException(Messages.format("Internalizer.TwoVersionAttributes", new Object[0]), this.locator);
/* 102 */           getErrorHandler().error(e);
/*     */         } 
/* 104 */         if (this.version == null) {
/* 105 */           this.version = version2;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 110 */     if ("http://java.sun.com/xml/ns/jaxb".equals(namespaceURI))
/* 111 */       this.seenBindings = true; 
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/* 115 */     super.endDocument();
/*     */     
/* 117 */     if (this.seenBindings && this.version == null) {
/*     */ 
/*     */       
/* 120 */       SAXParseException e = new SAXParseException(Messages.format("Internalizer.VersionNotPresent", new Object[0]), this.rootTagStart);
/* 121 */       getErrorHandler().error(e);
/*     */     } 
/*     */ 
/*     */     
/* 125 */     if (this.version != null && !VERSIONS.contains(this.version)) {
/*     */       
/* 127 */       SAXParseException e = new SAXParseException(Messages.format("Internalizer.IncorrectVersion", new Object[0]), this.rootTagStart);
/* 128 */       getErrorHandler().error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 133 */     super.setDocumentLocator(locator);
/* 134 */     this.locator = locator;
/*     */   }
/*     */   
/* 137 */   private static final Set<String> VERSIONS = new HashSet<>(Arrays.asList(new String[] { "1.0", "2.0", "2.1" }));
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\VersionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */