/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JForLoop;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JOp;
/*     */ import com.sun.codemodel.internal.JStatement;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
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
/*     */ final class ArrayField
/*     */   extends AbstractListField
/*     */ {
/*     */   private JMethod $setAll;
/*     */   private JMethod $getAll;
/*     */   
/*     */   class Accessor
/*     */     extends AbstractListField.Accessor
/*     */   {
/*     */     protected Accessor(JExpression $target) {
/*  68 */       super($target);
/*     */     }
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/*  72 */       block.assign((JAssignmentTarget)$var, (JExpression)this.$target.invoke(ArrayField.this.$getAll));
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/*  76 */       block.invoke(this.$target, ArrayField.this.$setAll).arg($var);
/*     */     }
/*     */ 
/*     */     
/*     */     public JExpression hasSetValue() {
/*  81 */       return this.field.ne(JExpr._null()).cand(this.field.ref("length").gt(JExpr.lit(0)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ArrayField(ClassOutlineImpl context, CPropertyInfo prop) {
/*  91 */     super(context, prop, false);
/*  92 */     generateArray();
/*     */   }
/*     */   
/*     */   protected final void generateArray() {
/*  96 */     this.field = this.outline.implClass.field(2, (JType)getCoreListType(), this.prop.getName(false));
/*  97 */     annotate((JAnnotatable)this.field);
/*     */ 
/*     */     
/* 100 */     generateAccessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/* 105 */     MethodWriter writer = this.outline.createMethodWriter();
/* 106 */     Accessor acc = create(JExpr._this());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.$getAll = writer.declareMethod((JType)this.exposedType.array(), "get" + this.prop.getName(true));
/* 116 */     writer.javadoc().append(this.prop.javadoc);
/* 117 */     JBlock body = this.$getAll.body();
/*     */     
/* 119 */     body._if(acc.ref(true).eq(JExpr._null()))._then()
/* 120 */       ._return((JExpression)JExpr.newArray(this.exposedType, 0));
/* 121 */     JVar var = body.decl((JType)this.exposedType.array(), "retVal", (JExpression)JExpr.newArray(this.implType, (JExpression)acc.ref(true).ref("length")));
/* 122 */     body.add((JStatement)this.codeModel.ref(System.class).staticInvoke("arraycopy")
/* 123 */         .arg(acc.ref(true)).arg(JExpr.lit(0))
/* 124 */         .arg((JExpression)var)
/* 125 */         .arg(JExpr.lit(0)).arg((JExpression)acc.ref(true).ref("length")));
/* 126 */     body._return(JExpr.direct("retVal"));
/*     */     
/* 128 */     List<Object> returnTypes = listPossibleTypes(this.prop);
/* 129 */     writer.javadoc().addReturn().append("array of\n").append(returnTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     JMethod $get = writer.declareMethod(this.exposedType, "get" + this.prop.getName(true));
/* 137 */     JVar $idx = writer.addParameter((JType)this.codeModel.INT, "idx");
/*     */     
/* 139 */     $get.body()._if(acc.ref(true).eq(JExpr._null()))._then()
/* 140 */       ._throw((JExpression)JExpr._new(this.codeModel.ref(IndexOutOfBoundsException.class)));
/*     */     
/* 142 */     writer.javadoc().append(this.prop.javadoc);
/* 143 */     $get.body()._return((JExpression)acc.ref(true).component((JExpression)$idx));
/*     */     
/* 145 */     writer.javadoc().addReturn().append("one of\n").append(returnTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     JMethod $getLength = writer.declareMethod((JType)this.codeModel.INT, "get" + this.prop.getName(true) + "Length");
/* 152 */     $getLength.body()._if(acc.ref(true).eq(JExpr._null()))._then()
/* 153 */       ._return(JExpr.lit(0));
/* 154 */     $getLength.body()._return((JExpression)acc.ref(true).ref("length"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     this.$setAll = writer.declareMethod((JType)this.codeModel.VOID, "set" + this.prop
/*     */         
/* 163 */         .getName(true));
/*     */     
/* 165 */     writer.javadoc().append(this.prop.javadoc);
/* 166 */     JVar $value = writer.addParameter((JType)this.exposedType.array(), "values");
/* 167 */     JVar $len = this.$setAll.body().decl((JType)this.codeModel.INT, "len", (JExpression)$value.ref("length"));
/*     */     
/* 169 */     this.$setAll.body().assign((JAssignmentTarget)acc
/* 170 */         .ref(true), 
/* 171 */         castToImplTypeArray((JExpression)JExpr.newArray((JType)this.codeModel
/* 172 */             .ref(this.exposedType.erasure().fullName()), (JExpression)$len)));
/*     */ 
/*     */     
/* 175 */     JForLoop _for = this.$setAll.body()._for();
/* 176 */     JVar $i = _for.init((JType)this.codeModel.INT, "i", JExpr.lit(0));
/* 177 */     _for.test(JOp.lt((JExpression)$i, (JExpression)$len));
/* 178 */     _for.update($i.incr());
/* 179 */     _for.body().assign((JAssignmentTarget)acc.ref(true).component((JExpression)$i), castToImplType(acc.box((JExpression)$value.component((JExpression)$i))));
/*     */     
/* 181 */     writer.javadoc().addParam($value)
/* 182 */       .append("allowed objects are\n")
/* 183 */       .append(returnTypes);
/*     */ 
/*     */ 
/*     */     
/* 187 */     JMethod $set = writer.declareMethod(this.exposedType, "set" + this.prop
/*     */         
/* 189 */         .getName(true));
/* 190 */     $idx = writer.addParameter((JType)this.codeModel.INT, "idx");
/* 191 */     $value = writer.addParameter(this.exposedType, "value");
/*     */     
/* 193 */     writer.javadoc().append(this.prop.javadoc);
/*     */     
/* 195 */     body = $set.body();
/* 196 */     body._return(JExpr.assign((JAssignmentTarget)acc.ref(true).component((JExpression)$idx), 
/* 197 */           castToImplType(acc.box((JExpression)$value))));
/*     */     
/* 199 */     writer.javadoc().addParam($value)
/* 200 */       .append("allowed object is\n")
/* 201 */       .append(returnTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JType getRawType() {
/* 207 */     return (JType)this.exposedType.array();
/*     */   }
/*     */   
/*     */   protected JClass getCoreListType() {
/* 211 */     return this.exposedType.array();
/*     */   }
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 215 */     return new Accessor(targetObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JExpression castToImplTypeArray(JExpression exp) {
/* 222 */     return (JExpression)JExpr.cast((JType)this.implType.array(), exp);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\ArrayField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */