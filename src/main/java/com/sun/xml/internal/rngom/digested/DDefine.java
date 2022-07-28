/*    */ package com.sun.xml.internal.rngom.digested;
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
/*    */ 
/*    */ public class DDefine
/*    */ {
/*    */   private final String name;
/*    */   private DPattern pattern;
/*    */   private Boolean nullable;
/*    */   DAnnotation annotation;
/*    */   
/*    */   public DDefine(String name) {
/* 59 */     this.name = name;
/*    */   }
/*    */   
/*    */   public DPattern getPattern() {
/* 63 */     return this.pattern;
/*    */   }
/*    */   
/*    */   public DAnnotation getAnnotation() {
/* 67 */     if (this.annotation == null)
/* 68 */       return DAnnotation.EMPTY; 
/* 69 */     return this.annotation;
/*    */   }
/*    */   
/*    */   public void setPattern(DPattern pattern) {
/* 73 */     this.pattern = pattern;
/* 74 */     this.nullable = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 81 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isNullable() {
/* 85 */     if (this.nullable == null)
/* 86 */       this.nullable = this.pattern.isNullable() ? Boolean.TRUE : Boolean.FALSE; 
/* 87 */     return this.nullable.booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DDefine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */