/*     */ package com.sun.tools.internal.xjc.util;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ public abstract class SubtreeCutter
/*     */   extends XMLFilterImpl
/*     */ {
/*  47 */   private int cutDepth = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static final ContentHandler stub = new DefaultHandler();
/*     */ 
/*     */ 
/*     */   
/*     */   private ContentHandler next;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  64 */     this.cutDepth = 0;
/*  65 */     super.startDocument();
/*     */   }
/*     */   
/*     */   public boolean isCutting() {
/*  69 */     return (this.cutDepth > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startCutting() {
/*  79 */     super.setContentHandler(stub);
/*  80 */     this.cutDepth = 1;
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/*  84 */     this.next = handler;
/*     */     
/*  86 */     if (getContentHandler() != stub)
/*  87 */       super.setContentHandler(handler); 
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*  91 */     if (this.cutDepth > 0)
/*  92 */       this.cutDepth++; 
/*  93 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  97 */     super.endElement(namespaceURI, localName, qName);
/*     */     
/*  99 */     if (this.cutDepth != 0) {
/* 100 */       this.cutDepth--;
/* 101 */       if (this.cutDepth == 1) {
/*     */         
/* 103 */         super.setContentHandler(this.next);
/* 104 */         this.cutDepth = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\SubtreeCutter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */