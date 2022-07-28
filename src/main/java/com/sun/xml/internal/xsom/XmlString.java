/*     */ package com.sun.xml.internal.xsom;
/*     */ 
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XmlString
/*     */ {
/*     */   public final String value;
/*     */   public final ValidationContext context;
/*     */   
/*     */   public XmlString(String value, ValidationContext context) {
/*  57 */     this.value = value;
/*  58 */     this.context = context;
/*  59 */     if (context == null) {
/*  60 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlString(String value) {
/*  67 */     this(value, NULL_CONTEXT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String resolvePrefix(String prefix) {
/*  91 */     return this.context.resolveNamespacePrefix(prefix);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  95 */     return this.value;
/*     */   }
/*     */   
/*  98 */   private static final ValidationContext NULL_CONTEXT = new ValidationContext() {
/*     */       public String resolveNamespacePrefix(String s) {
/* 100 */         if (s.length() == 0) return ""; 
/* 101 */         if (s.equals("xml")) return "http://www.w3.org/XML/1998/namespace"; 
/* 102 */         return null;
/*     */       }
/*     */       
/*     */       public String getBaseUri() {
/* 106 */         return null;
/*     */       }
/*     */       
/*     */       public boolean isUnparsedEntity(String s) {
/* 110 */         return false;
/*     */       }
/*     */       
/*     */       public boolean isNotation(String s) {
/* 114 */         return false;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XmlString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */