/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.xml.namespace.QName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomizationContextChecker
/*     */   extends XMLFilterImpl
/*     */ {
/* 118 */   private final Stack<QName> elementNames = new Stack<>();
/*     */ 
/*     */   
/*     */   private final ErrorHandler errorHandler;
/*     */   
/*     */   private Locator locator;
/*     */   
/* 125 */   private static final Set<String> prohibitedSchemaElementNames = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizationContextChecker(ErrorHandler _errorHandler) {
/* 132 */     this.errorHandler = _errorHandler;
/*     */   }
/*     */   
/*     */   static {
/* 136 */     prohibitedSchemaElementNames.add("restriction");
/* 137 */     prohibitedSchemaElementNames.add("extension");
/* 138 */     prohibitedSchemaElementNames.add("simpleContent");
/* 139 */     prohibitedSchemaElementNames.add("complexContent");
/* 140 */     prohibitedSchemaElementNames.add("list");
/* 141 */     prohibitedSchemaElementNames.add("union");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private QName top() {
/* 149 */     return this.elementNames.peek();
/*     */   }
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 153 */     QName newElement = new QName(namespaceURI, localName);
/*     */     
/* 155 */     if (newElement.getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb") && 
/* 156 */       top().getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema"))
/*     */     {
/*     */       
/* 159 */       if (this.elementNames.size() >= 3) {
/*     */ 
/*     */         
/* 162 */         QName schemaElement = this.elementNames.get(this.elementNames.size() - 3);
/* 163 */         if (prohibitedSchemaElementNames.contains(schemaElement.getLocalPart()))
/*     */         {
/* 165 */           this.errorHandler.error(new SAXParseException(
/* 166 */                 Messages.format("CustomizationContextChecker.UnacknolwedgedCustomization", new Object[] { localName }), this.locator));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     this.elementNames.push(newElement);
/*     */     
/* 178 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 184 */     super.endElement(namespaceURI, localName, qName);
/*     */     
/* 186 */     this.elementNames.pop();
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 190 */     super.setDocumentLocator(locator);
/* 191 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\parser\CustomizationContextChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */