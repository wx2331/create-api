/*    */ package com.sun.tools.internal.ws.processor.model.java;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
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
/*    */ public class JavaSimpleType
/*    */   extends JavaType
/*    */ {
/*    */   public JavaSimpleType() {}
/*    */   
/*    */   public JavaSimpleType(String name, String initString) {
/* 39 */     super(name, true, initString);
/*    */   }
/*    */   
/*    */   public JavaSimpleType(JAXBTypeAndAnnotation jtype) {
/* 43 */     super(jtype);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaSimpleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */