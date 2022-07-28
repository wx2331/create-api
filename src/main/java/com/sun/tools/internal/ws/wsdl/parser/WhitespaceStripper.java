/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ class WhitespaceStripper
/*     */   extends XMLFilterImpl
/*     */ {
/*  45 */   private int state = 0;
/*     */   
/*  47 */   private char[] buf = new char[1024];
/*  48 */   private int bufLen = 0;
/*     */   
/*     */   private static final int AFTER_START_ELEMENT = 1;
/*     */   private static final int AFTER_END_ELEMENT = 2;
/*     */   
/*     */   public WhitespaceStripper(XMLReader reader) {
/*  54 */     setParent(reader);
/*     */   }
/*     */   
/*     */   public WhitespaceStripper(ContentHandler handler, ErrorHandler eh, EntityResolver er) {
/*  58 */     setContentHandler(handler);
/*  59 */     if (eh != null) setErrorHandler(eh); 
/*  60 */     if (er != null) setEntityResolver(er); 
/*     */   } public void characters(char[] ch, int start, int length) throws SAXException {
/*     */     int len;
/*     */     int i;
/*  64 */     switch (this.state) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/*  70 */         if (this.bufLen + length > this.buf.length) {
/*     */           
/*  72 */           char[] newBuf = new char[Math.max(this.bufLen + length, this.buf.length * 2)];
/*  73 */           System.arraycopy(this.buf, 0, newBuf, 0, this.bufLen);
/*  74 */           this.buf = newBuf;
/*     */         } 
/*  76 */         System.arraycopy(ch, start, this.buf, this.bufLen, length);
/*  77 */         this.bufLen += length;
/*     */         break;
/*     */       
/*     */       case 2:
/*  81 */         len = start + length;
/*  82 */         for (i = start; i < len; i++) {
/*  83 */           if (!WhiteSpaceProcessor.isWhiteSpace(ch[i])) {
/*  84 */             super.characters(ch, start, length);
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*  93 */     processPendingText();
/*  94 */     super.startElement(uri, localName, qName, atts);
/*  95 */     this.state = 1;
/*  96 */     this.bufLen = 0;
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 100 */     processPendingText();
/* 101 */     super.endElement(uri, localName, qName);
/* 102 */     this.state = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPendingText() throws SAXException {
/* 110 */     if (this.state == 1)
/* 111 */       for (int i = this.bufLen - 1; i >= 0; i--) {
/* 112 */         if (!WhiteSpaceProcessor.isWhiteSpace(this.buf[i])) {
/* 113 */           super.characters(this.buf, 0, this.bufLen);
/*     */           return;
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\WhitespaceStripper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */