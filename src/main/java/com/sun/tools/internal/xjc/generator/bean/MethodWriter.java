/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JDocComment;
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.codemodel.internal.JVar;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
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
/*    */ public abstract class MethodWriter
/*    */ {
/*    */   protected final JCodeModel codeModel;
/*    */   
/*    */   protected MethodWriter(ClassOutline context) {
/* 52 */     this.codeModel = context.parent().getCodeModel();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JMethod declareMethod(JType paramJType, String paramString);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final JMethod declareMethod(Class returnType, String methodName) {
/* 65 */     return declareMethod((JType)this.codeModel.ref(returnType), methodName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JDocComment javadoc();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract JVar addParameter(JType paramJType, String paramString);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final JVar addParameter(Class type, String name) {
/* 86 */     return addParameter((JType)this.codeModel.ref(type), name);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\MethodWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */