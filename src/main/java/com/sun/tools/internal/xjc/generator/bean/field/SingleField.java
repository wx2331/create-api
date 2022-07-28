/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
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
/*     */ public class SingleField
/*     */   extends AbstractFieldWithVar
/*     */ {
/*     */   protected SingleField(ClassOutlineImpl context, CPropertyInfo prop) {
/*  66 */     this(context, prop, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleField(ClassOutlineImpl context, CPropertyInfo prop, boolean forcePrimitiveAccess) {
/*  76 */     super(context, prop); JType getterType;
/*  77 */     assert !this.exposedType.isPrimitive() && !this.implType.isPrimitive();
/*     */     
/*  79 */     createField();
/*     */     
/*  81 */     MethodWriter writer = context.createMethodWriter();
/*  82 */     NameConverter nc = context.parent().getModel().getNameConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     JExpression defaultValue = null;
/*  93 */     if (prop.defaultValue != null) {
/*  94 */       defaultValue = prop.defaultValue.compute((Outline)this.outline.parent());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  99 */     if ((getOptions()).enableIntrospection) {
/* 100 */       if (forcePrimitiveAccess) {
/* 101 */         getterType = this.exposedType.unboxify();
/*     */       } else {
/* 103 */         getterType = this.exposedType;
/*     */       } 
/* 105 */     } else if (defaultValue != null || forcePrimitiveAccess) {
/* 106 */       getterType = this.exposedType.unboxify();
/*     */     } else {
/* 108 */       getterType = this.exposedType;
/*     */     } 
/*     */     
/* 111 */     JMethod $get = writer.declareMethod(getterType, getGetterMethod());
/* 112 */     String javadoc = prop.javadoc;
/* 113 */     if (javadoc.length() == 0)
/* 114 */       javadoc = Messages.DEFAULT_GETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }); 
/* 115 */     writer.javadoc().append(javadoc);
/*     */ 
/*     */     
/* 118 */     if (defaultValue == null) {
/* 119 */       $get.body()._return((JExpression)ref());
/*     */     } else {
/* 121 */       JConditional cond = $get.body()._if(ref().eq(JExpr._null()));
/* 122 */       cond._then()._return(defaultValue);
/* 123 */       cond._else()._return((JExpression)ref());
/*     */     } 
/*     */     
/* 126 */     List<Object> possibleTypes = listPossibleTypes(prop);
/* 127 */     writer.javadoc().addReturn()
/* 128 */       .append("possible object is\n")
/* 129 */       .append(possibleTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     JMethod $set = writer.declareMethod((JType)this.codeModel.VOID, "set" + prop.getName(true));
/* 136 */     JType setterType = this.exposedType;
/* 137 */     if (forcePrimitiveAccess) setterType = setterType.unboxify(); 
/* 138 */     JVar $value = writer.addParameter(setterType, "value");
/* 139 */     JBlock body = $set.body();
/* 140 */     if ($value.type().equals(this.implType)) {
/* 141 */       body.assign((JAssignmentTarget)JExpr._this().ref((JVar)ref()), (JExpression)$value);
/*     */     } else {
/* 143 */       body.assign((JAssignmentTarget)JExpr._this().ref((JVar)ref()), castToImplType((JExpression)$value));
/*     */     } 
/*     */ 
/*     */     
/* 147 */     writer.javadoc().append(Messages.DEFAULT_SETTER_JAVADOC.format(new Object[] { nc.toVariableName(prop.getName(true)) }));
/* 148 */     writer.javadoc().addParam($value)
/* 149 */       .append("allowed object is\n")
/* 150 */       .append(possibleTypes);
/*     */   }
/*     */   
/*     */   public final JType getFieldType() {
/* 154 */     return this.implType;
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/* 158 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   protected class Accessor extends AbstractFieldWithVar.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 163 */       super($target);
/*     */     }
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 167 */       body.assign((JAssignmentTarget)this.$ref, JExpr._null());
/*     */     }
/*     */     public JExpression hasSetValue() {
/* 170 */       return this.$ref.ne(JExpr._null());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\SingleField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */