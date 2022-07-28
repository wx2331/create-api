/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
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
/*     */ 
/*     */ public class DOMForestScanner
/*     */ {
/*     */   private final DOMForest forest;
/*     */   
/*     */   public DOMForestScanner(DOMForest _forest) {
/*  61 */     this.forest = _forest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(Element e, ContentHandler contentHandler) throws SAXException {
/*  69 */     DOMScanner scanner = new DOMScanner();
/*     */ 
/*     */     
/*  72 */     LocationResolver resolver = new LocationResolver(scanner);
/*  73 */     resolver.setContentHandler(contentHandler);
/*     */ 
/*     */     
/*  76 */     scanner.setContentHandler(resolver);
/*  77 */     scanner.scan(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(Document d, ContentHandler contentHandler) throws SAXException {
/*  85 */     scan(d.getDocumentElement(), contentHandler);
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
/* 113 */       this.inStart = false;
/*     */       this.parent = _parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {
/* 119 */       super.setDocumentLocator(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 124 */       this.inStart = false;
/* 125 */       super.endElement(namespaceURI, localName, qName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 131 */       this.inStart = true;
/* 132 */       super.startElement(namespaceURI, localName, qName, atts);
/*     */     }
/*     */     
/*     */     private Locator findLocator() {
/* 136 */       Node n = this.parent.getCurrentLocation();
/* 137 */       if (n instanceof Element) {
/* 138 */         Element e = (Element)n;
/* 139 */         if (this.inStart) {
/* 140 */           return DOMForestScanner.this.forest.locatorTable.getStartLocation(e);
/*     */         }
/* 142 */         return DOMForestScanner.this.forest.locatorTable.getEndLocation(e);
/*     */       } 
/* 144 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 153 */       Locator l = findLocator();
/* 154 */       if (l != null) return l.getColumnNumber(); 
/* 155 */       return -1;
/*     */     }
/*     */     
/*     */     public int getLineNumber() {
/* 159 */       Locator l = findLocator();
/* 160 */       if (l != null) return l.getLineNumber(); 
/* 161 */       return -1;
/*     */     }
/*     */     
/*     */     public String getPublicId() {
/* 165 */       Locator l = findLocator();
/* 166 */       if (l != null) return l.getPublicId(); 
/* 167 */       return null;
/*     */     }
/*     */     
/*     */     public String getSystemId() {
/* 171 */       Locator l = findLocator();
/* 172 */       if (l != null) return l.getSystemId(); 
/* 173 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\DOMForestScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */