/*     */ package com.sun.tools.internal.ws.processor.model.java;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*     */ import com.sun.tools.internal.ws.resources.ModelMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class JavaMethod
/*     */ {
/*     */   private final ErrorReceiver errorReceiver;
/*     */   private final String name;
/*  44 */   private final List<JavaParameter> parameters = new ArrayList<>();
/*  45 */   private final List<String> exceptions = new ArrayList<>();
/*     */   private final WsimportOptions options;
/*     */   private JavaType returnType;
/*     */   
/*     */   public JavaMethod(String name, WsimportOptions options, ErrorReceiver receiver) {
/*  50 */     this.name = name;
/*  51 */     this.returnType = null;
/*  52 */     this.errorReceiver = receiver;
/*  53 */     this.options = options;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public JavaType getReturnType() {
/*  61 */     return this.returnType;
/*     */   }
/*     */   
/*     */   public void setReturnType(JavaType returnType) {
/*  65 */     this.returnType = returnType;
/*     */   }
/*     */   
/*     */   private boolean hasParameter(String paramName) {
/*  69 */     for (JavaParameter parameter : this.parameters) {
/*  70 */       if (paramName.equals(parameter.getName())) {
/*  71 */         return true;
/*     */       }
/*     */     } 
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   private Parameter getParameter(String paramName) {
/*  78 */     for (JavaParameter parameter : this.parameters) {
/*  79 */       if (paramName.equals(parameter.getName())) {
/*  80 */         return parameter.getParameter();
/*     */       }
/*     */     } 
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParameter(JavaParameter param) {
/*  88 */     if (hasParameter(param.getName())) {
/*  89 */       if (this.options.isExtensionMode()) {
/*  90 */         param.setName(getUniqueName(param.getName()));
/*     */       } else {
/*  92 */         Parameter duplicParam = getParameter(param.getName());
/*  93 */         if (param.getParameter().isEmbedded()) {
/*  94 */           this.errorReceiver.error(param.getParameter().getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE_WRAPPER(param.getName(), param.getParameter().getEntityName()));
/*  95 */           this.errorReceiver.error(duplicParam.getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE_WRAPPER(param.getName(), duplicParam.getEntityName()));
/*     */         } else {
/*  97 */           this.errorReceiver.error(param.getParameter().getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE(param.getName(), param.getParameter().getEntityName()));
/*  98 */           this.errorReceiver.error(duplicParam.getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE(param.getName(), duplicParam.getEntityName()));
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     }
/* 103 */     this.parameters.add(param);
/*     */   }
/*     */   
/*     */   public List<JavaParameter> getParametersList() {
/* 107 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addException(String exception) {
/* 112 */     if (!this.exceptions.contains(exception)) {
/* 113 */       this.exceptions.add(exception);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getExceptions() {
/* 119 */     return this.exceptions.iterator();
/*     */   }
/*     */   
/*     */   private String getUniqueName(String param) {
/* 123 */     int parmNum = 0;
/* 124 */     while (hasParameter(param)) {
/* 125 */       param = param + Integer.toString(parmNum++);
/*     */     }
/* 127 */     return param;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */