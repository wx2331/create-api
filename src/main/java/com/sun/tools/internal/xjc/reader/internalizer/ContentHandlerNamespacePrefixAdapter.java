/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.AttributesImpl;
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
/*     */ final class ContentHandlerNamespacePrefixAdapter
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   private boolean namespacePrefixes = false;
/*  50 */   private String[] nsBinding = new String[8];
/*     */   
/*     */   private int len;
/*     */   
/*     */   private final AttributesImpl atts;
/*     */   
/*     */   private static final String PREFIX_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
/*     */   
/*     */   private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  62 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*  63 */       return this.namespacePrefixes; 
/*  64 */     return super.getFeature(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  69 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
/*  70 */       this.namespacePrefixes = value;
/*     */       return;
/*     */     } 
/*  73 */     if (name.equals("http://xml.org/sax/features/namespaces") && value)
/*     */       return; 
/*  75 */     super.setFeature(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/*  81 */     if ("http://www.w3.org/XML/1998/namespace".equals(uri))
/*  82 */       return;  if (this.len == this.nsBinding.length) {
/*     */       
/*  84 */       String[] buf = new String[this.nsBinding.length * 2];
/*  85 */       System.arraycopy(this.nsBinding, 0, buf, 0, this.nsBinding.length);
/*  86 */       this.nsBinding = buf;
/*     */     } 
/*  88 */     this.nsBinding[this.len++] = prefix;
/*  89 */     this.nsBinding[this.len++] = uri;
/*  90 */     super.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*  95 */     if (this.namespacePrefixes) {
/*  96 */       this.atts.setAttributes(atts);
/*     */       
/*  98 */       for (int i = 0; i < this.len; i += 2) {
/*  99 */         String prefix = this.nsBinding[i];
/* 100 */         if (prefix.length() == 0) {
/* 101 */           this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", "xmlns", "xmlns", "CDATA", this.nsBinding[i + 1]);
/*     */         } else {
/* 103 */           this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", prefix, "xmlns:" + prefix, "CDATA", this.nsBinding[i + 1]);
/*     */         } 
/* 105 */       }  atts = this.atts;
/*     */     } 
/* 107 */     this.len = 0;
/* 108 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */   
/* 111 */   public ContentHandlerNamespacePrefixAdapter() { this.atts = new AttributesImpl(); } public ContentHandlerNamespacePrefixAdapter(XMLReader parent) { this.atts = new AttributesImpl();
/*     */     setParent(parent); }
/*     */ 
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\ContentHandlerNamespacePrefixAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */