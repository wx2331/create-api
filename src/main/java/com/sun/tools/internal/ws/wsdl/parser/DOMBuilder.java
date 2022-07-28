/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBindingsConstants;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.LocatorTable;
/*     */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
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
/*     */ class DOMBuilder
/*     */   extends SAX2DOMEx
/*     */   implements LexicalHandler
/*     */ {
/*     */   private final LocatorTable locatorTable;
/*     */   private final Set outerMostBindings;
/*     */   private Locator locator;
/*     */   
/*     */   public DOMBuilder(Document dom, LocatorTable ltable, Set outerMostBindings) {
/*  63 */     super(dom);
/*  64 */     this.locatorTable = ltable;
/*  65 */     this.outerMostBindings = outerMostBindings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  76 */     this.locator = locator;
/*  77 */     super.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
/*  82 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  84 */     Element e = getCurrentElement();
/*  85 */     this.locatorTable.storeStartLocation(e, this.locator);
/*     */ 
/*     */     
/*  88 */     if (JAXWSBindingsConstants.JAXWS_BINDINGS.getNamespaceURI().equals(e.getNamespaceURI()) && "bindings"
/*  89 */       .equals(e.getLocalName())) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       Node p = e.getParentNode();
/*  95 */       if (p instanceof Document) {
/*  96 */         this.outerMostBindings.add(e);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) {
/* 102 */     this.locatorTable.storeEndLocation(getCurrentElement(), this.locator);
/* 103 */     super.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {}
/*     */ 
/*     */   
/*     */   public void endDTD() throws SAXException {}
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */   
/*     */   public void startCDATA() throws SAXException {}
/*     */   
/*     */   public void endCDATA() throws SAXException {}
/*     */   
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 121 */     Node parent = this.nodeStack.peek();
/* 122 */     Comment comment = this.document.createComment(new String(ch, start, length));
/* 123 */     parent.appendChild(comment);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\DOMBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */