/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
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
/*     */ public class UnboxedField
/*     */   extends AbstractFieldWithVar
/*     */ {
/*     */   private final JPrimitiveType ptype;
/*     */
/*     */   protected UnboxedField(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  57 */     super(outline, prop);
/*     */
/*  59 */     assert this.implType == this.exposedType;
/*     */
/*  61 */     this.ptype = (JPrimitiveType)this.implType;
/*  62 */     assert this.ptype != null;
/*     */
/*  64 */     createField();
/*     */
/*     */
/*     */
/*     */
/*     */
/*  70 */     MethodWriter writer = outline.createMethodWriter();
/*  71 */     NameConverter nc = outline.parent().getModel().getNameConverter();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  79 */     JMethod $get = writer.declareMethod((JType)this.ptype, getGetterMethod());
/*  80 */     String javadoc = prop.javadoc;
/*  81 */     if (javadoc.length() == 0)
/*  82 */       javadoc = Messages.DEFAULT_GETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) });
/*  83 */     writer.javadoc().append(javadoc);
/*     */
/*  85 */     $get.body()._return((JExpression)ref());
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  92 */     JMethod $set = writer.declareMethod((JType)this.codeModel.VOID, "set" + prop.getName(true));
/*  93 */     JVar $value = writer.addParameter((JType)this.ptype, "value");
/*  94 */     JBlock body = $set.body();
/*  95 */     body.assign((JAssignmentTarget)JExpr._this().ref((JVar)ref()), (JExpression)$value);
/*     */
/*  97 */     writer.javadoc().append(Messages.DEFAULT_SETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }));
/*     */   }
/*     */
/*     */
/*     */   protected JType getType(Aspect aspect) {
/* 102 */     return (JType)super.getType(aspect).boxify().getPrimitiveType();
/*     */   }
/*     */
/*     */   protected JType getFieldType() {
/* 106 */     return (JType)this.ptype;
/*     */   }
/*     */
/*     */   public FieldAccessor create(JExpression targetObject) {
/* 110 */     return new Accessor(targetObject)
/*     */       {
/*     */         public void unsetValues(JBlock body) {}
/*     */
/*     */
/*     */
/*     */         public JExpression hasSetValue() {
/* 117 */           return JExpr.TRUE;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\UnboxedField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
