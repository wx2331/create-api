/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.ProcessorException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GeneratorException
/*    */   extends ProcessorException
/*    */ {
/*    */   public GeneratorException(String key, Object... args) {
/* 37 */     super(key, args);
/*    */   }
/*    */   
/*    */   public GeneratorException(Throwable throwable) {
/* 41 */     super(throwable);
/*    */   }
/*    */   
/*    */   public String getDefaultResourceBundleName() {
/* 45 */     return "com.sun.tools.internal.ws.resources.generator";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\GeneratorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */