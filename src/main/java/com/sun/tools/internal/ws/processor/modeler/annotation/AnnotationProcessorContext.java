/*     */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.Operation;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationProcessorContext
/*     */ {
/*  49 */   private Map<Name, SeiContext> seiContextMap = new HashMap<>();
/*  50 */   private int round = 1;
/*     */   private boolean modelCompleted = false;
/*     */   
/*     */   public void addSeiContext(Name seiName, SeiContext seiContext) {
/*  54 */     this.seiContextMap.put(seiName, seiContext);
/*     */   }
/*     */   
/*     */   public SeiContext getSeiContext(Name seiName) {
/*  58 */     SeiContext context = this.seiContextMap.get(seiName);
/*  59 */     if (context == null) {
/*  60 */       context = new SeiContext();
/*  61 */       addSeiContext(seiName, context);
/*     */     } 
/*  63 */     return context;
/*     */   }
/*     */   
/*     */   public SeiContext getSeiContext(TypeElement d) {
/*  67 */     return getSeiContext(d.getQualifiedName());
/*     */   }
/*     */   
/*     */   public Collection<SeiContext> getSeiContexts() {
/*  71 */     return this.seiContextMap.values();
/*     */   }
/*     */   
/*     */   public int getRound() {
/*  75 */     return this.round;
/*     */   }
/*     */   
/*     */   public void incrementRound() {
/*  79 */     this.round++;
/*     */   }
/*     */   
/*     */   public static boolean isEncoded(Model model) {
/*  83 */     if (model == null)
/*  84 */       return false; 
/*  85 */     for (Service service : model.getServices()) {
/*  86 */       for (Port port : service.getPorts()) {
/*  87 */         for (Operation operation : port.getOperations()) {
/*  88 */           if (operation.getUse() != null && operation.getUse().equals(SOAPUse.LITERAL))
/*  89 */             return false; 
/*     */         } 
/*     */       } 
/*     */     } 
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public void setModelCompleted(boolean modelCompleted) {
/*  97 */     this.modelCompleted = modelCompleted;
/*     */   }
/*     */   
/*     */   public boolean isModelCompleted() {
/* 101 */     return this.modelCompleted;
/*     */   }
/*     */   
/*     */   public static class SeiContext
/*     */   {
/* 106 */     private Map<String, WrapperInfo> reqOperationWrapperMap = new HashMap<>();
/* 107 */     private Map<String, WrapperInfo> resOperationWrapperMap = new HashMap<>();
/* 108 */     private Map<Name, FaultInfo> exceptionBeanMap = new HashMap<>();
/*     */     
/*     */     private Name seiImplName;
/*     */     
/*     */     private boolean implementsSei;
/*     */     
/*     */     private String namespaceUri;
/*     */ 
/*     */     
/*     */     public SeiContext() {}
/*     */ 
/*     */     
/*     */     public SeiContext(Name seiName) {}
/*     */     
/*     */     public void setImplementsSei(boolean implementsSei) {
/* 123 */       this.implementsSei = implementsSei;
/*     */     }
/*     */     
/*     */     public boolean getImplementsSei() {
/* 127 */       return this.implementsSei;
/*     */     }
/*     */     
/*     */     public void setNamespaceUri(String namespaceUri) {
/* 131 */       this.namespaceUri = namespaceUri;
/*     */     }
/*     */     
/*     */     public String getNamespaceUri() {
/* 135 */       return this.namespaceUri;
/*     */     }
/*     */     
/*     */     public Name getSeiImplName() {
/* 139 */       return this.seiImplName;
/*     */     }
/*     */     
/*     */     public void setSeiImplName(Name implName) {
/* 143 */       this.seiImplName = implName;
/*     */     }
/*     */     
/*     */     public void setReqWrapperOperation(ExecutableElement method, WrapperInfo wrapperInfo) {
/* 147 */       this.reqOperationWrapperMap.put(methodToString(method), wrapperInfo);
/*     */     }
/*     */     
/*     */     public WrapperInfo getReqOperationWrapper(ExecutableElement method) {
/* 151 */       return this.reqOperationWrapperMap.get(methodToString(method));
/*     */     }
/*     */     
/*     */     public void setResWrapperOperation(ExecutableElement method, WrapperInfo wrapperInfo) {
/* 155 */       this.resOperationWrapperMap.put(methodToString(method), wrapperInfo);
/*     */     }
/*     */     
/*     */     public WrapperInfo getResOperationWrapper(ExecutableElement method) {
/* 159 */       return this.resOperationWrapperMap.get(methodToString(method));
/*     */     }
/*     */     
/*     */     public String methodToString(ExecutableElement method) {
/* 163 */       StringBuilder buf = new StringBuilder(method.getSimpleName());
/* 164 */       for (VariableElement param : method.getParameters())
/* 165 */         buf.append(';').append(param.asType()); 
/* 166 */       return buf.toString();
/*     */     }
/*     */     
/*     */     public void clearExceptionMap() {
/* 170 */       this.exceptionBeanMap.clear();
/*     */     }
/*     */     
/*     */     public void addExceptionBeanEntry(Name exception, FaultInfo faultInfo, ModelBuilder builder) {
/* 174 */       this.exceptionBeanMap.put(exception, faultInfo);
/*     */     }
/*     */     
/*     */     public FaultInfo getExceptionBeanName(Name exception) {
/* 178 */       return this.exceptionBeanMap.get(exception);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\AnnotationProcessorContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */