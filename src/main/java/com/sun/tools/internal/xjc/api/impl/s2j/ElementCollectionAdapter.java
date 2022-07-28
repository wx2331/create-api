/*     */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JForEach;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ final class ElementCollectionAdapter
/*     */   extends ElementAdapter
/*     */ {
/*     */   public ElementCollectionAdapter(FieldOutline core, CElementInfo ei) {
/*  55 */     super(core, ei);
/*     */   }
/*     */   
/*     */   public JType getRawType() {
/*  59 */     return (JType)codeModel().ref(List.class).narrow(itemType().boxify());
/*     */   }
/*     */   
/*     */   private JType itemType() {
/*  63 */     return this.ei.getContentInMemoryType().toType(outline(), Aspect.EXPOSED);
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/*  67 */     return new FieldAccessorImpl(targetObject);
/*     */   }
/*     */   
/*     */   final class FieldAccessorImpl extends ElementAdapter.FieldAccessorImpl {
/*     */     public FieldAccessorImpl(JExpression target) {
/*  72 */       super(target);
/*     */     }
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/*  76 */       JCodeModel cm = ElementCollectionAdapter.this.outline().getCodeModel();
/*  77 */       JClass elementType = ElementCollectionAdapter.this.ei.toType(ElementCollectionAdapter.this.outline(), Aspect.EXPOSED).boxify();
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
/*  88 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(cm.ref(ArrayList.class).narrow(ElementCollectionAdapter.this.itemType().boxify())));
/*  89 */       JVar $col = block.decl(ElementCollectionAdapter.this.core.getRawType(), "col" + hashCode());
/*  90 */       this.acc.toRawValue(block, $col);
/*  91 */       JForEach loop = block.forEach((JType)elementType, "v" + hashCode(), (JExpression)$col);
/*     */       
/*  93 */       JConditional cond = loop.body()._if(loop.var().eq(JExpr._null()));
/*  94 */       cond._then().invoke((JExpression)$var, "add").arg(JExpr._null());
/*  95 */       cond._else().invoke((JExpression)$var, "add").arg((JExpression)loop.var().invoke("getValue"));
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/*  99 */       JCodeModel cm = ElementCollectionAdapter.this.outline().getCodeModel();
/* 100 */       JClass elementType = ElementCollectionAdapter.this.ei.toType(ElementCollectionAdapter.this.outline(), Aspect.EXPOSED).boxify();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       JClass col = cm.ref(ArrayList.class).narrow(elementType);
/* 110 */       JVar $t = block.decl((JType)col, uniqueName + "_col", (JExpression)JExpr._new(col));
/*     */       
/* 112 */       JForEach loop = block.forEach(ElementCollectionAdapter.this.itemType(), uniqueName + "_i", (JExpression)$t);
/* 113 */       loop.body().invoke($var, "add").arg((JExpression)createJAXBElement((JExpression)loop.var()));
/*     */       
/* 115 */       this.acc.fromRawValue(block, uniqueName, (JExpression)$t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\ElementCollectionAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */