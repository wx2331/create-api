/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
/*     */ import java.util.ArrayList;
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
/*     */ public class DummyListField
/*     */   extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */   
/*     */   protected DummyListField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList) {
/*  96 */     super(context, prop, !coreList.fullName().equals("java.util.ArrayList"));
/*  97 */     this.coreList = coreList.narrow(this.exposedType.boxify());
/*  98 */     generate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void annotate(JAnnotatable field) {
/* 106 */     super.annotate(field);
/*     */     
/* 108 */     if (this.prop instanceof CReferencePropertyInfo) {
/* 109 */       CReferencePropertyInfo pref = (CReferencePropertyInfo)this.prop;
/* 110 */       if (pref.isDummy()) {
/* 111 */         annotateDummy(field);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void annotateDummy(JAnnotatable field) {
/* 118 */     field.annotate(OverrideAnnotationOf.class);
/*     */   }
/*     */   
/*     */   protected final JClass getCoreListType() {
/* 122 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {}
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 129 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   class Accessor extends AbstractListField.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 134 */       super($target);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 141 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(DummyListField.this.codeModel.ref(ArrayList.class).narrow(DummyListField.this.exposedType.boxify())).arg((JExpression)this.$target
/* 142 */             .invoke(DummyListField.this.$get)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 149 */       JVar $list = block.decl((JType)DummyListField.this.listT, uniqueName + 'l', (JExpression)this.$target.invoke(DummyListField.this.$get));
/* 150 */       block.invoke((JExpression)$list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\DummyListField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */