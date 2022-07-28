/*     */ package com.sun.tools.internal.xjc.util;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForkContentHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   private final ContentHandler lhs;
/*     */   private final ContentHandler rhs;
/*     */   
/*     */   public ForkContentHandler(ContentHandler first, ContentHandler second) {
/*  52 */     this.lhs = first;
/*  53 */     this.rhs = second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler create(ContentHandler[] handlers) {
/*  61 */     if (handlers.length == 0) {
/*  62 */       throw new IllegalArgumentException();
/*     */     }
/*  64 */     ContentHandler result = handlers[0];
/*  65 */     for (int i = 1; i < handlers.length; i++)
/*  66 */       result = new ForkContentHandler(result, handlers[i]); 
/*  67 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/*  74 */     this.lhs.setDocumentLocator(locator);
/*  75 */     this.rhs.setDocumentLocator(locator);
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  79 */     this.lhs.startDocument();
/*  80 */     this.rhs.startDocument();
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException {
/*  84 */     this.lhs.endDocument();
/*  85 */     this.rhs.endDocument();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/*  89 */     this.lhs.startPrefixMapping(prefix, uri);
/*  90 */     this.rhs.startPrefixMapping(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/*  94 */     this.lhs.endPrefixMapping(prefix);
/*  95 */     this.rhs.endPrefixMapping(prefix);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/*  99 */     this.lhs.startElement(uri, localName, qName, attributes);
/* 100 */     this.rhs.startElement(uri, localName, qName, attributes);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 104 */     this.lhs.endElement(uri, localName, qName);
/* 105 */     this.rhs.endElement(uri, localName, qName);
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 109 */     this.lhs.characters(ch, start, length);
/* 110 */     this.rhs.characters(ch, start, length);
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 114 */     this.lhs.ignorableWhitespace(ch, start, length);
/* 115 */     this.rhs.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 119 */     this.lhs.processingInstruction(target, data);
/* 120 */     this.rhs.processingInstruction(target, data);
/*     */   }
/*     */   
/*     */   public void skippedEntity(String name) throws SAXException {
/* 124 */     this.lhs.skippedEntity(name);
/* 125 */     this.rhs.skippedEntity(name);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\ForkContentHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */