/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaSimpleType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.concurrent.Future;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.AsyncHandler;
/*     */ import javax.xml.ws.Response;
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
/*     */ public class AsyncOperation
/*     */   extends Operation
/*     */ {
/*     */   private Operation operation;
/*     */   private boolean _async;
/*     */   private AsyncOperationType _asyncOpType;
/*     */   private AbstractType _responseBean;
/*     */   
/*     */   public AsyncOperation(Entity entity) {
/*  49 */     super(entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncOperation(Operation operation, Entity entity) {
/*  57 */     super(operation, entity);
/*  58 */     this.operation = operation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncOperation(QName name, Entity entity) {
/*  65 */     super(name, entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAsync() {
/*  73 */     return this._async;
/*     */   }
/*     */   
/*     */   public void setAsyncType(AsyncOperationType type) {
/*  77 */     this._asyncOpType = type;
/*  78 */     this._async = true;
/*     */   }
/*     */   
/*     */   public AsyncOperationType getAsyncType() {
/*  82 */     return this._asyncOpType;
/*     */   }
/*     */   
/*     */   public void setResponseBean(AbstractType type) {
/*  86 */     this._responseBean = type;
/*     */   }
/*     */   
/*     */   public AbstractType getResponseBeanType() {
/*  90 */     return this._responseBean;
/*     */   }
/*     */   
/*     */   public JavaType getResponseBeanJavaType() {
/*  94 */     JCodeModel cm = this._responseBean.getJavaType().getType().getType().owner();
/*  95 */     if (this._asyncOpType.equals(AsyncOperationType.CALLBACK)) {
/*  96 */       JClass future = cm.ref(Future.class).narrow(cm.ref(Object.class).wildcard());
/*  97 */       return (JavaType)new JavaSimpleType(new JAXBTypeAndAnnotation((JType)future));
/*  98 */     }  if (this._asyncOpType.equals(AsyncOperationType.POLLING)) {
/*  99 */       JClass polling = cm.ref(Response.class).narrow(this._responseBean.getJavaType().getType().getType().boxify());
/* 100 */       return (JavaType)new JavaSimpleType(new JAXBTypeAndAnnotation((JType)polling));
/*     */     } 
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public JavaType getCallBackType() {
/* 106 */     if (this._asyncOpType.equals(AsyncOperationType.CALLBACK)) {
/* 107 */       JCodeModel cm = this._responseBean.getJavaType().getType().getType().owner();
/* 108 */       JClass cb = cm.ref(AsyncHandler.class).narrow(this._responseBean.getJavaType().getType().getType().boxify());
/* 109 */       return (JavaType)new JavaSimpleType(new JAXBTypeAndAnnotation((JType)cb));
/*     */     } 
/*     */     
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   public Operation getNormalOperation() {
/* 116 */     return this.operation;
/*     */   }
/*     */   
/*     */   public void setNormalOperation(Operation operation) {
/* 120 */     this.operation = operation;
/*     */   }
/*     */   
/*     */   public String getJavaMethodName() {
/* 124 */     return super.getJavaMethodName() + "Async";
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\AsyncOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */