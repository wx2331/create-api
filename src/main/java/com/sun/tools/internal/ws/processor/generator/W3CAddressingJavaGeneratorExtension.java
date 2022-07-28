/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.codemodel.internal.JAnnotationArrayMember;
/*    */ import com.sun.codemodel.internal.JAnnotationUse;
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.ws.api.TJavaGeneratorExtension;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Fault;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*    */ import java.util.Map;
/*    */ import javax.xml.ws.Action;
/*    */ import javax.xml.ws.FaultAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class W3CAddressingJavaGeneratorExtension
/*    */   extends TJavaGeneratorExtension
/*    */ {
/*    */   public void writeMethodAnnotations(TWSDLOperation two, JMethod jMethod) {
/* 50 */     JAnnotationUse actionAnn = null;
/*    */     
/* 52 */     if (!(two instanceof Operation)) {
/*    */       return;
/*    */     }
/* 55 */     Operation o = (Operation)two;
/*    */ 
/*    */     
/* 58 */     if (o.getInput().getAction() != null && !o.getInput().getAction().equals("")) {
/*    */       
/* 60 */       actionAnn = jMethod.annotate(Action.class);
/* 61 */       actionAnn.param("input", o.getInput().getAction());
/*    */     } 
/*    */ 
/*    */     
/* 65 */     if (o.getOutput() != null && o.getOutput().getAction() != null && !o.getOutput().getAction().equals("")) {
/*    */       
/* 67 */       if (actionAnn == null) {
/* 68 */         actionAnn = jMethod.annotate(Action.class);
/*    */       }
/* 70 */       actionAnn.param("output", o.getOutput().getAction());
/*    */     } 
/*    */ 
/*    */     
/* 74 */     if (o.getFaults() != null && o.getFaults().size() > 0) {
/* 75 */       Map<String, JClass> map = o.getFaults();
/* 76 */       JAnnotationArrayMember jam = null;
/*    */       
/* 78 */       for (Fault f : o.faults()) {
/* 79 */         if (f.getAction() == null) {
/*    */           continue;
/*    */         }
/* 82 */         if (f.getAction().equals("")) {
/*    */           continue;
/*    */         }
/* 85 */         if (actionAnn == null) {
/* 86 */           actionAnn = jMethod.annotate(Action.class);
/*    */         }
/* 88 */         if (jam == null) {
/* 89 */           jam = actionAnn.paramArray("fault");
/*    */         }
/* 91 */         JAnnotationUse faAnn = jam.annotate(FaultAction.class);
/* 92 */         faAnn.param("className", (JType)map.get(f.getName()));
/* 93 */         faAnn.param("value", f.getAction());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\W3CAddressingJavaGeneratorExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */