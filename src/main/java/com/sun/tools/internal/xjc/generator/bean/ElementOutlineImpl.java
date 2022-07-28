/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.JCast;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ElementOutline;
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
/*     */ final class ElementOutlineImpl
/*     */   extends ElementOutline
/*     */ {
/*     */   private final BeanGenerator parent;
/*     */   
/*     */   public BeanGenerator parent() {
/*  54 */     return this.parent;
/*     */   }
/*     */   
/*     */   ElementOutlineImpl(BeanGenerator parent, CElementInfo ei) {
/*  58 */     super(ei, parent
/*  59 */         .getClassFactory().createClass(parent
/*  60 */           .getContainer(ei.parent, Aspect.EXPOSED), ei.shortName(), ei.getLocator()));
/*  61 */     this.parent = parent;
/*  62 */     parent.elements.put(ei, this);
/*     */     
/*  64 */     JCodeModel cm = parent.getCodeModel();
/*     */     
/*  66 */     this.implClass._extends(cm
/*  67 */         .ref(JAXBElement.class).narrow(this.target
/*  68 */           .getContentInMemoryType().toType(parent, Aspect.EXPOSED).boxify()));
/*     */     
/*  70 */     if (ei.hasClass()) {
/*  71 */       JType implType = ei.getContentInMemoryType().toType(parent, Aspect.IMPLEMENTATION);
/*  72 */       JCast jCast = JExpr.cast((JType)cm.ref(Class.class), implType.boxify().dotclass());
/*  73 */       JClass scope = null;
/*  74 */       if (ei.getScope() != null)
/*  75 */         scope = (parent.getClazz(ei.getScope())).implRef; 
/*  76 */       JExpression scopeClass = (scope == null) ? JExpr._null() : scope.dotclass();
/*  77 */       JFieldVar valField = this.implClass.field(26, QName.class, "NAME", (JExpression)createQName(cm, ei.getElementName()));
/*     */ 
/*     */       
/*  80 */       JMethod cons = this.implClass.constructor(1);
/*  81 */       cons.body().invoke("super")
/*  82 */         .arg((JExpression)valField)
/*  83 */         .arg((JExpression)jCast)
/*  84 */         .arg(scopeClass)
/*  85 */         .arg((JExpression)cons.param(implType, "value"));
/*     */ 
/*     */       
/*  88 */       JMethod noArgCons = this.implClass.constructor(1);
/*  89 */       noArgCons.body().invoke("super")
/*  90 */         .arg((JExpression)valField)
/*  91 */         .arg((JExpression)jCast)
/*  92 */         .arg(scopeClass)
/*  93 */         .arg(JExpr._null());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JInvocation createQName(JCodeModel codeModel, QName name) {
/* 102 */     return JExpr._new(codeModel.ref(QName.class)).arg(name.getNamespaceURI()).arg(name.getLocalPart());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\ElementOutlineImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */