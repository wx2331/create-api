/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.tools.internal.ws.api.TJavaGeneratorExtension;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JavaGeneratorExtensionFacade
/*    */   extends TJavaGeneratorExtension
/*    */ {
/*    */   private final TJavaGeneratorExtension[] extensions;
/*    */   
/*    */   JavaGeneratorExtensionFacade(TJavaGeneratorExtension... extensions) {
/* 39 */     assert extensions != null;
/* 40 */     this.extensions = extensions;
/*    */   }
/*    */   
/*    */   public void writeMethodAnnotations(TWSDLOperation wsdlOperation, JMethod jMethod) {
/* 44 */     for (TJavaGeneratorExtension e : this.extensions)
/* 45 */       e.writeMethodAnnotations(wsdlOperation, jMethod); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\JavaGeneratorExtensionFacade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */