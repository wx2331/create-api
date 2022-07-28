/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JExpression;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.xml.internal.xsom.XmlString;
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
/*    */ public abstract class CDefaultValue
/*    */ {
/*    */   public abstract JExpression compute(Outline paramOutline);
/*    */   
/*    */   public static CDefaultValue create(final TypeUse typeUse, final XmlString defaultValue) {
/* 49 */     return new CDefaultValue() {
/*    */         public JExpression compute(Outline outline) {
/* 51 */           return typeUse.createConstant(outline, defaultValue);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CDefaultValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */