/*    */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.List;
/*    */ import javax.lang.model.type.TypeMirror;
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
/*    */ final class MemberInfo
/*    */   implements Comparable<MemberInfo>
/*    */ {
/*    */   private final TypeMirror paramType;
/*    */   private final String paramName;
/*    */   private final List<Annotation> jaxbAnnotations;
/*    */   
/*    */   public MemberInfo(TypeMirror paramType, String paramName, List<Annotation> jaxbAnnotations) {
/* 43 */     this.paramType = paramType;
/* 44 */     this.paramName = paramName;
/* 45 */     this.jaxbAnnotations = jaxbAnnotations;
/*    */   }
/*    */   
/*    */   public List<Annotation> getJaxbAnnotations() {
/* 49 */     return this.jaxbAnnotations;
/*    */   }
/*    */   
/*    */   public TypeMirror getParamType() {
/* 53 */     return this.paramType;
/*    */   }
/*    */   
/*    */   public String getParamName() {
/* 57 */     return this.paramName;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(MemberInfo member) {
/* 62 */     return this.paramName.compareTo(member.paramName);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 67 */     return super.equals(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     int hash = 5;
/* 73 */     hash = 47 * hash + ((this.paramType != null) ? this.paramType.hashCode() : 0);
/* 74 */     hash = 47 * hash + ((this.paramName != null) ? this.paramName.hashCode() : 0);
/* 75 */     hash = 47 * hash + ((this.jaxbAnnotations != null) ? this.jaxbAnnotations.hashCode() : 0);
/* 76 */     return hash;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\MemberInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */