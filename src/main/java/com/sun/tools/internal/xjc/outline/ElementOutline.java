/*    */ package com.sun.tools.internal.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
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
/*    */ public abstract class ElementOutline
/*    */ {
/*    */   public final CElementInfo target;
/*    */   public final JDefinedClass implClass;
/*    */   
/*    */   public abstract Outline parent();
/*    */   
/*    */   public PackageOutline _package() {
/* 53 */     return parent().getPackageContext(this.implClass._package());
/*    */   }
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
/*    */   protected ElementOutline(CElementInfo target, JDefinedClass implClass) {
/* 69 */     this.target = target;
/* 70 */     this.implClass = implClass;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\ElementOutline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */