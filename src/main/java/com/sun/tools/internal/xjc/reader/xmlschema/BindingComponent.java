/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
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
/*    */ public abstract class BindingComponent
/*    */ {
/*    */   protected BindingComponent() {
/* 37 */     Ring.add(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final ErrorReporter getErrorReporter() {
/* 47 */     return (ErrorReporter)Ring.get(ErrorReporter.class);
/*    */   }
/*    */   protected final ClassSelector getClassSelector() {
/* 50 */     return (ClassSelector)Ring.get(ClassSelector.class);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\BindingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */