/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LSInputSAXWrapper
/*     */   implements LSInput
/*     */ {
/*     */   private InputSource core;
/*     */   
/*     */   public LSInputSAXWrapper(InputSource inputSource) {
/*  43 */     assert inputSource != null;
/*  44 */     this.core = inputSource;
/*     */   }
/*     */   
/*     */   public Reader getCharacterStream() {
/*  48 */     return this.core.getCharacterStream();
/*     */   }
/*     */   
/*     */   public void setCharacterStream(Reader characterStream) {
/*  52 */     this.core.setCharacterStream(characterStream);
/*     */   }
/*     */   
/*     */   public InputStream getByteStream() {
/*  56 */     return this.core.getByteStream();
/*     */   }
/*     */   
/*     */   public void setByteStream(InputStream byteStream) {
/*  60 */     this.core.setByteStream(byteStream);
/*     */   }
/*     */   
/*     */   public String getStringData() {
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStringData(String stringData) {}
/*     */ 
/*     */   
/*     */   public String getSystemId() {
/*  72 */     return this.core.getSystemId();
/*     */   }
/*     */   
/*     */   public void setSystemId(String systemId) {
/*  76 */     this.core.setSystemId(systemId);
/*     */   }
/*     */   
/*     */   public String getPublicId() {
/*  80 */     return this.core.getPublicId();
/*     */   }
/*     */   
/*     */   public void setPublicId(String publicId) {
/*  84 */     this.core.setPublicId(publicId);
/*     */   }
/*     */   
/*     */   public String getBaseURI() {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaseURI(String baseURI) {}
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/*  96 */     return this.core.getEncoding();
/*     */   }
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 100 */     this.core.setEncoding(encoding);
/*     */   }
/*     */   
/*     */   public boolean getCertifiedText() {
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   public void setCertifiedText(boolean certifiedText) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\parser\LSInputSAXWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */