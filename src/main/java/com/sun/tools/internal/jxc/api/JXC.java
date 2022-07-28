/*    */ package com.sun.tools.internal.jxc.api;
/*    */ 
/*    */ import com.sun.tools.internal.jxc.api.impl.j2s.JavaCompilerImpl;
/*    */ import com.sun.tools.internal.xjc.api.JavaCompiler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JXC
/*    */ {
/*    */   public static JavaCompiler createJavaCompiler() {
/* 43 */     return (JavaCompiler)new JavaCompilerImpl();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\api\JXC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */