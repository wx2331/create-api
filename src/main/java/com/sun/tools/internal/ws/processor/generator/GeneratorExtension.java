/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.tools.internal.ws.processor.model.Model;
/*    */ import com.sun.tools.internal.ws.processor.model.Port;
/*    */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GeneratorExtension
/*    */ {
/*    */   public String getBindingValue(String transport, SOAPVersion soapVersion) {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeWebServiceAnnotation(Model model, JCodeModel cm, JDefinedClass cls, Port port) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validateOption(String name) {
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public void writeWebServiceClientAnnotation(WsimportOptions options, JCodeModel cm, JDefinedClass cls) {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\GeneratorExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */