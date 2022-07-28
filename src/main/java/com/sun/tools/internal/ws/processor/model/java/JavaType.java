/*     */ package com.sun.tools.internal.ws.processor.model.java;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JavaType
/*     */ {
/*     */   private String name;
/*     */   private String realName;
/*     */   private boolean present;
/*     */   private boolean holder;
/*     */   private boolean holderPresent;
/*     */   private String initString;
/*     */   private String holderName;
/*     */   private JAXBTypeAndAnnotation type;
/*     */   
/*     */   public JavaType() {}
/*     */   
/*     */   public JavaType(JAXBTypeAndAnnotation type) {
/*  48 */     this.type = type;
/*  49 */     init(type.getName(), false, null, null);
/*     */   }
/*     */   
/*     */   public JavaType(String name, boolean present, String initString) {
/*  53 */     init(name, present, initString, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType(String name, boolean present, String initString, String holderName) {
/*  59 */     init(name, present, initString, holderName);
/*     */   }
/*     */   
/*     */   public JAXBTypeAndAnnotation getType() {
/*  63 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(String name, boolean present, String initString, String holderName) {
/*  69 */     this.realName = name;
/*  70 */     this.name = name.replace('$', '.');
/*  71 */     this.present = present;
/*  72 */     this.initString = initString;
/*  73 */     this.holderName = holderName;
/*  74 */     this.holder = (holderName != null);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  78 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doSetName(String name) {
/*  84 */     this.realName = name;
/*  85 */     this.name = name.replace('$', '.');
/*     */   }
/*     */   
/*     */   public String getRealName() {
/*  89 */     return this.realName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRealName(String s) {
/*  94 */     this.realName = s;
/*     */   }
/*     */   
/*     */   public String getFormalName() {
/*  98 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setFormalName(String s) {
/* 102 */     this.name = s;
/*     */   }
/*     */   
/*     */   public boolean isPresent() {
/* 106 */     return this.present;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPresent(boolean b) {
/* 111 */     this.present = b;
/*     */   }
/*     */   
/*     */   public boolean isHolder() {
/* 115 */     return this.holder;
/*     */   }
/*     */   
/*     */   public void setHolder(boolean holder) {
/* 119 */     this.holder = holder;
/*     */   }
/*     */   
/*     */   public boolean isHolderPresent() {
/* 123 */     return this.holderPresent;
/*     */   }
/*     */   public void setHolderPresent(boolean holderPresent) {
/* 126 */     this.holderPresent = holderPresent;
/*     */   }
/*     */   
/*     */   public String getInitString() {
/* 130 */     return this.initString;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInitString(String s) {
/* 135 */     this.initString = s;
/*     */   }
/*     */   
/*     */   public String getHolderName() {
/* 139 */     return this.holderName;
/*     */   }
/*     */   
/*     */   public void setHolderName(String holderName) {
/* 143 */     this.holderName = holderName;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */