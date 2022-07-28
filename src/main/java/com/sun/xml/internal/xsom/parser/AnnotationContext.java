/*    */ package com.sun.xml.internal.xsom.parser;
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
/*    */ public final class AnnotationContext
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   private AnnotationContext(String _name) {
/* 41 */     this.name = _name;
/*    */   }
/*    */   public String toString() {
/* 44 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/* 48 */   public static final AnnotationContext SCHEMA = new AnnotationContext("schema");
/*    */   
/* 50 */   public static final AnnotationContext NOTATION = new AnnotationContext("notation");
/*    */   
/* 52 */   public static final AnnotationContext ELEMENT_DECL = new AnnotationContext("element");
/*    */   
/* 54 */   public static final AnnotationContext IDENTITY_CONSTRAINT = new AnnotationContext("identityConstraint");
/*    */   
/* 56 */   public static final AnnotationContext XPATH = new AnnotationContext("xpath");
/*    */   
/* 58 */   public static final AnnotationContext MODELGROUP_DECL = new AnnotationContext("modelGroupDecl");
/*    */   
/* 60 */   public static final AnnotationContext SIMPLETYPE_DECL = new AnnotationContext("simpleTypeDecl");
/*    */   
/* 62 */   public static final AnnotationContext COMPLEXTYPE_DECL = new AnnotationContext("complexTypeDecl");
/*    */   
/* 64 */   public static final AnnotationContext PARTICLE = new AnnotationContext("particle");
/*    */   
/* 66 */   public static final AnnotationContext MODELGROUP = new AnnotationContext("modelGroup");
/*    */   
/* 68 */   public static final AnnotationContext ATTRIBUTE_USE = new AnnotationContext("attributeUse");
/*    */   
/* 70 */   public static final AnnotationContext WILDCARD = new AnnotationContext("wildcard");
/*    */   
/* 72 */   public static final AnnotationContext ATTRIBUTE_GROUP = new AnnotationContext("attributeGroup");
/*    */   
/* 74 */   public static final AnnotationContext ATTRIBUTE_DECL = new AnnotationContext("attributeDecl");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\parser\AnnotationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */