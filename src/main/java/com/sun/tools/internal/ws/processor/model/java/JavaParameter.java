/*    */ package com.sun.tools.internal.ws.processor.model.java;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaParameter
/*    */ {
/*    */   private String name;
/*    */   private JavaType type;
/*    */   private Parameter parameter;
/*    */   private boolean holder;
/*    */   private String holderName;
/*    */   
/*    */   public JavaParameter() {}
/*    */   
/*    */   public JavaParameter(String name, JavaType type, Parameter parameter) {
/* 39 */     this(name, type, parameter, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaParameter(String name, JavaType type, Parameter parameter, boolean holder) {
/* 45 */     this.name = name;
/* 46 */     this.type = type;
/* 47 */     this.parameter = parameter;
/* 48 */     this.holder = holder;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String s) {
/* 56 */     this.name = s;
/*    */   }
/*    */   
/*    */   public JavaType getType() {
/* 60 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(JavaType t) {
/* 64 */     this.type = t;
/*    */   }
/*    */   
/*    */   public Parameter getParameter() {
/* 68 */     return this.parameter;
/*    */   }
/*    */   
/*    */   public void setParameter(Parameter p) {
/* 72 */     this.parameter = p;
/*    */   }
/*    */   
/*    */   public boolean isHolder() {
/* 76 */     return this.holder;
/*    */   }
/*    */   
/*    */   public void setHolder(boolean b) {
/* 80 */     this.holder = b;
/*    */   }
/*    */   
/*    */   public String getHolderName() {
/* 84 */     return this.holderName;
/*    */   }
/*    */   
/*    */   public void setHolderName(String holderName) {
/* 88 */     this.holderName = holderName;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */