/*     */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.namespace.QName;
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
/*     */ abstract class ElementAdapter
/*     */   implements FieldOutline
/*     */ {
/*     */   protected final FieldOutline core;
/*     */   protected final CElementInfo ei;
/*     */   
/*     */   public ElementAdapter(FieldOutline core, CElementInfo ei) {
/*  74 */     this.core = core;
/*  75 */     this.ei = ei;
/*     */   }
/*     */   
/*     */   public ClassOutline parent() {
/*  79 */     return this.core.parent();
/*     */   }
/*     */   
/*     */   public CPropertyInfo getPropertyInfo() {
/*  83 */     return this.core.getPropertyInfo();
/*     */   }
/*     */   
/*     */   protected final Outline outline() {
/*  87 */     return this.core.parent().parent();
/*     */   }
/*     */   
/*     */   protected final JCodeModel codeModel() {
/*  91 */     return outline().getCodeModel();
/*     */   }
/*     */   
/*     */   protected abstract class FieldAccessorImpl implements FieldAccessor {
/*     */     final FieldAccessor acc;
/*     */     
/*     */     public FieldAccessorImpl(JExpression target) {
/*  98 */       this.acc = ElementAdapter.this.core.create(target);
/*     */     }
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 102 */       this.acc.unsetValues(body);
/*     */     }
/*     */     
/*     */     public JExpression hasSetValue() {
/* 106 */       return this.acc.hasSetValue();
/*     */     }
/*     */     
/*     */     public FieldOutline owner() {
/* 110 */       return ElementAdapter.this;
/*     */     }
/*     */     
/*     */     public CPropertyInfo getPropertyInfo() {
/* 114 */       return ElementAdapter.this.core.getPropertyInfo();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final JInvocation createJAXBElement(JExpression $var) {
/* 121 */       JCodeModel cm = ElementAdapter.this.codeModel();
/*     */       
/* 123 */       return JExpr._new(cm.ref(JAXBElement.class))
/* 124 */         .arg((JExpression)JExpr._new(cm.ref(QName.class))
/* 125 */           .arg(ElementAdapter.this.ei.getElementName().getNamespaceURI())
/* 126 */           .arg(ElementAdapter.this.ei.getElementName().getLocalPart()))
/* 127 */         .arg(ElementAdapter.this.getRawType().boxify().erasure().dotclass())
/* 128 */         .arg($var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\ElementAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */