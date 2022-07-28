/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationValueImpl
/*     */   implements AnnotationValue
/*     */ {
/*     */   private final DocEnv env;
/*     */   private final Attribute attr;
/*     */   
/*     */   AnnotationValueImpl(DocEnv paramDocEnv, Attribute paramAttribute) {
/*  53 */     this.env = paramDocEnv;
/*  54 */     this.attr = paramAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object value() {
/*  69 */     ValueVisitor valueVisitor = new ValueVisitor();
/*  70 */     this.attr.accept(valueVisitor);
/*  71 */     return valueVisitor.value;
/*     */   }
/*     */   
/*     */   private class ValueVisitor implements Attribute.Visitor {
/*     */     public Object value;
/*     */     
/*     */     public void visitConstant(Attribute.Constant param1Constant) {
/*  78 */       if (param1Constant.type.hasTag(TypeTag.BOOLEAN)) {
/*     */         
/*  80 */         this.value = Boolean.valueOf(
/*  81 */             (((Integer)param1Constant.value).intValue() != 0));
/*     */       } else {
/*  83 */         this.value = param1Constant.value;
/*     */       } 
/*     */     }
/*     */     private ValueVisitor() {}
/*     */     public void visitClass(Attribute.Class param1Class) {
/*  88 */       this.value = TypeMaker.getType(AnnotationValueImpl.this.env, 
/*  89 */           AnnotationValueImpl.this.env.types.erasure(param1Class.classType));
/*     */     }
/*     */     
/*     */     public void visitEnum(Attribute.Enum param1Enum) {
/*  93 */       this.value = AnnotationValueImpl.this.env.getFieldDoc(param1Enum.value);
/*     */     }
/*     */     
/*     */     public void visitCompound(Attribute.Compound param1Compound) {
/*  97 */       this.value = new AnnotationDescImpl(AnnotationValueImpl.this.env, param1Compound);
/*     */     }
/*     */     
/*     */     public void visitArray(Attribute.Array param1Array) {
/* 101 */       AnnotationValue[] arrayOfAnnotationValue = new AnnotationValue[param1Array.values.length];
/* 102 */       for (byte b = 0; b < arrayOfAnnotationValue.length; b++) {
/* 103 */         arrayOfAnnotationValue[b] = new AnnotationValueImpl(AnnotationValueImpl.this.env, param1Array.values[b]);
/*     */       }
/* 105 */       this.value = arrayOfAnnotationValue;
/*     */     }
/*     */     
/*     */     public void visitError(Attribute.Error param1Error) {
/* 109 */       this.value = "<error>";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     ToStringVisitor toStringVisitor = new ToStringVisitor();
/* 122 */     this.attr.accept(toStringVisitor);
/* 123 */     return toStringVisitor.toString();
/*     */   }
/*     */   
/*     */   private class ToStringVisitor implements Attribute.Visitor {
/* 127 */     private final StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     
/*     */     public String toString() {
/* 131 */       return this.sb.toString();
/*     */     }
/*     */     
/*     */     public void visitConstant(Attribute.Constant param1Constant) {
/* 135 */       if (param1Constant.type.hasTag(TypeTag.BOOLEAN)) {
/*     */         
/* 137 */         this.sb.append((((Integer)param1Constant.value).intValue() != 0));
/*     */       } else {
/* 139 */         this.sb.append(FieldDocImpl.constantValueExpression(param1Constant.value));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void visitClass(Attribute.Class param1Class) {
/* 144 */       this.sb.append(param1Class);
/*     */     }
/*     */     
/*     */     public void visitEnum(Attribute.Enum param1Enum) {
/* 148 */       this.sb.append(param1Enum);
/*     */     }
/*     */     
/*     */     public void visitCompound(Attribute.Compound param1Compound) {
/* 152 */       this.sb.append(new AnnotationDescImpl(AnnotationValueImpl.this.env, param1Compound));
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitArray(Attribute.Array param1Array) {
/* 157 */       if (param1Array.values.length != 1) this.sb.append('{');
/*     */       
/* 159 */       boolean bool = true;
/* 160 */       for (Attribute attribute : param1Array.values) {
/* 161 */         if (bool) {
/* 162 */           bool = false;
/*     */         } else {
/* 164 */           this.sb.append(", ");
/*     */         } 
/* 166 */         attribute.accept(this);
/*     */       } 
/*     */       
/* 169 */       if (param1Array.values.length != 1) this.sb.append('}'); 
/*     */     }
/*     */     
/*     */     public void visitError(Attribute.Error param1Error) {
/* 173 */       this.sb.append("<error>");
/*     */     }
/*     */     
/*     */     private ToStringVisitor() {}
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\AnnotationValueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */