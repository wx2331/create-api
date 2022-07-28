/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class DOMForestScanner
/*     */ {
/*     */   private final DOMForest forest;
/*     */   
/*     */   public DOMForestScanner(DOMForest _forest) {
/*  60 */     this.forest = _forest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(Element e, ContentHandler contentHandler) throws SAXException {
/*  68 */     DOMScanner scanner = new DOMScanner();
/*     */ 
/*     */     
/*  71 */     LocationResolver resolver = new LocationResolver(scanner);
/*  72 */     resolver.setContentHandler(contentHandler);
/*     */ 
/*     */     
/*  75 */     scanner.setContentHandler(resolver);
/*  76 */     scanner.scan(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(Document d, ContentHandler contentHandler) throws SAXException {
/*  84 */     scan(d.getDocumentElement(), contentHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class LocationResolver
/*     */     extends XMLFilterImpl
/*     */     implements Locator
/*     */   {
/*     */     private final DOMScanner parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean inStart;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     LocationResolver(DOMScanner _parent) {
/* 112 */       this.inStart = false;
/*     */       this.parent = _parent;
/*     */     }
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {
/* 117 */       super.setDocumentLocator(this);
/*     */     }
/*     */     
/*     */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 121 */       this.inStart = false;
/* 122 */       super.endElement(namespaceURI, localName, qName);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 127 */       this.inStart = true;
/* 128 */       super.startElement(namespaceURI, localName, qName, atts);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Locator findLocator() {
/* 135 */       Node n = this.parent.getCurrentLocation();
/* 136 */       if (n instanceof Element) {
/* 137 */         Element e = (Element)n;
/* 138 */         if (this.inStart) {
/* 139 */           return DOMForestScanner.this.forest.locatorTable.getStartLocation(e);
/*     */         }
/* 141 */         return DOMForestScanner.this.forest.locatorTable.getEndLocation(e);
/*     */       } 
/* 143 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 152 */       Locator l = findLocator();
/* 153 */       if (l != null) return l.getColumnNumber(); 
/* 154 */       return -1;
/*     */     }
/*     */     
/*     */     public int getLineNumber() {
/* 158 */       Locator l = findLocator();
/* 159 */       if (l != null) return l.getLineNumber(); 
/* 160 */       return -1;
/*     */     }
/*     */     
/*     */     public String getPublicId() {
/* 164 */       Locator l = findLocator();
/* 165 */       if (l != null) return l.getPublicId(); 
/* 166 */       return null;
/*     */     }
/*     */     
/*     */     public String getSystemId() {
/* 170 */       Locator l = findLocator();
/* 171 */       if (l != null) return l.getSystemId(); 
/* 172 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\DOMForestScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */