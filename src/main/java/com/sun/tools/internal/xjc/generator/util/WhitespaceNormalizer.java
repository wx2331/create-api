/*    */ package com.sun.tools.internal.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JExpr;
/*    */ import com.sun.codemodel.internal.JExpression;
/*    */ import com.sun.codemodel.internal.JStringLiteral;
/*    */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
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
/*    */ public abstract class WhitespaceNormalizer
/*    */ {
/*    */   public abstract JExpression generate(JCodeModel paramJCodeModel, JExpression paramJExpression);
/*    */   
/*    */   public static WhitespaceNormalizer parse(String method) {
/* 60 */     if (method.equals("preserve")) {
/* 61 */       return PRESERVE;
/*    */     }
/* 63 */     if (method.equals("replace")) {
/* 64 */       return REPLACE;
/*    */     }
/* 66 */     if (method.equals("collapse")) {
/* 67 */       return COLLAPSE;
/*    */     }
/* 69 */     throw new IllegalArgumentException(method);
/*    */   }
/*    */   
/* 72 */   public static final WhitespaceNormalizer PRESERVE = new WhitespaceNormalizer() {
/*    */       public JExpression generate(JCodeModel codeModel, JExpression literal) {
/* 74 */         return literal;
/*    */       }
/*    */     };
/*    */   
/* 78 */   public static final WhitespaceNormalizer REPLACE = new WhitespaceNormalizer()
/*    */     {
/*    */       public JExpression generate(JCodeModel codeModel, JExpression literal) {
/* 81 */         if (literal instanceof JStringLiteral)
/*    */         {
/* 83 */           return JExpr.lit(WhiteSpaceProcessor.replace(((JStringLiteral)literal).str));
/*    */         }
/* 85 */         return (JExpression)codeModel.ref(WhiteSpaceProcessor.class)
/* 86 */           .staticInvoke("replace").arg(literal);
/*    */       }
/*    */     };
/*    */   
/* 90 */   public static final WhitespaceNormalizer COLLAPSE = new WhitespaceNormalizer()
/*    */     {
/*    */       public JExpression generate(JCodeModel codeModel, JExpression literal) {
/* 93 */         if (literal instanceof JStringLiteral)
/*    */         {
/* 95 */           return JExpr.lit(WhiteSpaceProcessor.collapse(((JStringLiteral)literal).str));
/*    */         }
/* 97 */         return (JExpression)codeModel.ref(WhiteSpaceProcessor.class)
/* 98 */           .staticInvoke("collapse").arg(literal);
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generato\\util\WhitespaceNormalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */