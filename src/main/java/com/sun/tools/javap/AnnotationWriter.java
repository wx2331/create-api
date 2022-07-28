/*     */ package com.sun.tools.javap;
/*     */
/*     */ import com.sun.tools.classfile.Annotation;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.TypeAnnotation;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class AnnotationWriter
/*     */   extends BasicWriter
/*     */ {
/*     */   element_value_Writer ev_writer;
/*     */   private ClassWriter classWriter;
/*     */   private ConstantWriter constantWriter;
/*     */
/*     */   static AnnotationWriter instance(Context paramContext) {
/*  50 */     AnnotationWriter annotationWriter = paramContext.<AnnotationWriter>get(AnnotationWriter.class);
/*  51 */     if (annotationWriter == null)
/*  52 */       annotationWriter = new AnnotationWriter(paramContext);
/*  53 */     return annotationWriter;
/*     */   }
/*     */   public void write(Annotation paramAnnotation) { write(paramAnnotation, false); }
/*     */   public void write(Annotation paramAnnotation, boolean paramBoolean) { writeDescriptor(paramAnnotation.type_index, paramBoolean); boolean bool = (paramAnnotation.num_element_value_pairs > 0 || !paramBoolean) ? true : false; if (bool) print("(");  for (byte b = 0; b < paramAnnotation.num_element_value_pairs; b++) { if (b > 0) print(",");  write(paramAnnotation.element_value_pairs[b], paramBoolean); }  if (bool) print(")");  }
/*  57 */   public void write(TypeAnnotation paramTypeAnnotation) { write(paramTypeAnnotation, true, false); } public void write(TypeAnnotation paramTypeAnnotation, boolean paramBoolean1, boolean paramBoolean2) { write(paramTypeAnnotation.annotation, paramBoolean2); print(": "); write(paramTypeAnnotation.position, paramBoolean1); } public void write(TypeAnnotation.Position paramPosition, boolean paramBoolean) { byte b; print(paramPosition.type); switch (paramPosition.type) { case INSTANCEOF: case NEW: case CONSTRUCTOR_REFERENCE: case METHOD_REFERENCE: if (paramBoolean) { print(", offset="); print(Integer.valueOf(paramPosition.offset)); }  break;case LOCAL_VARIABLE: case RESOURCE_VARIABLE: if (paramPosition.lvarOffset == null) { print(", lvarOffset is Null!"); break; }  print(", {"); for (b = 0; b < paramPosition.lvarOffset.length; b++) { if (b != 0) print("; ");  if (paramBoolean) { print("start_pc="); print(Integer.valueOf(paramPosition.lvarOffset[b])); }  print(", length="); print(Integer.valueOf(paramPosition.lvarLength[b])); print(", index="); print(Integer.valueOf(paramPosition.lvarIndex[b])); }  print("}"); break;case EXCEPTION_PARAMETER: print(", exception_index="); print(Integer.valueOf(paramPosition.exception_index)); break;case METHOD_RECEIVER: break;case CLASS_TYPE_PARAMETER: case METHOD_TYPE_PARAMETER: print(", param_index="); print(Integer.valueOf(paramPosition.parameter_index)); break;case CLASS_TYPE_PARAMETER_BOUND: case METHOD_TYPE_PARAMETER_BOUND: print(", param_index="); print(Integer.valueOf(paramPosition.parameter_index)); print(", bound_index="); print(Integer.valueOf(paramPosition.bound_index)); break;case CLASS_EXTENDS: print(", type_index="); print(Integer.valueOf(paramPosition.type_index)); break;case THROWS: print(", type_index="); print(Integer.valueOf(paramPosition.type_index)); break;case METHOD_FORMAL_PARAMETER: print(", param_index="); print(Integer.valueOf(paramPosition.parameter_index)); break;case CAST: case CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT: case METHOD_INVOCATION_TYPE_ARGUMENT: case CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT: case METHOD_REFERENCE_TYPE_ARGUMENT: if (paramBoolean) { print(", offset="); print(Integer.valueOf(paramPosition.offset)); }  print(", type_index="); print(Integer.valueOf(paramPosition.type_index)); break;case METHOD_RETURN: case FIELD: break;case UNKNOWN: throw new AssertionError("AnnotationWriter: UNKNOWN target type should never occur!");default: throw new AssertionError("AnnotationWriter: Unknown target type for position: " + paramPosition); }  if (!paramPosition.location.isEmpty()) { print(", location="); print(paramPosition.location); }  } public void write(Annotation.element_value_pair paramelement_value_pair) { write(paramelement_value_pair, false); } protected AnnotationWriter(Context paramContext) { super(paramContext);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 237 */     this.ev_writer = new element_value_Writer(); this.classWriter = ClassWriter.instance(paramContext); this.constantWriter = ConstantWriter.instance(paramContext); }
/*     */   public void write(Annotation.element_value_pair paramelement_value_pair, boolean paramBoolean) { writeIndex(paramelement_value_pair.element_name_index, paramBoolean); print("="); write(paramelement_value_pair.value, paramBoolean); }
/*     */   public void write(Annotation.element_value paramelement_value) { write(paramelement_value, false); } public void write(Annotation.element_value paramelement_value, boolean paramBoolean) { this.ev_writer.write(paramelement_value, paramBoolean); } private void writeDescriptor(int paramInt, boolean paramBoolean) { if (paramBoolean) try { ConstantPool constantPool = (this.classWriter.getClassFile()).constant_pool; Descriptor descriptor = new Descriptor(paramInt); print(descriptor.getFieldType(constantPool)); return; } catch (ConstantPoolException constantPoolException) {  } catch (Descriptor.InvalidDescriptor invalidDescriptor) {}  print("#" + paramInt); } private void writeIndex(int paramInt, boolean paramBoolean) { if (paramBoolean) { print(this.constantWriter.stringValue(paramInt)); } else { print("#" + paramInt); }  } class element_value_Writer implements Annotation.element_value.Visitor<Void, Boolean>
/*     */   {
/* 241 */     public void write(Annotation.element_value param1element_value, boolean param1Boolean) { param1element_value.accept(this, Boolean.valueOf(param1Boolean)); }
/*     */
/*     */
/*     */     public Void visitPrimitive(Annotation.Primitive_element_value param1Primitive_element_value, Boolean param1Boolean) {
/* 245 */       if (param1Boolean.booleanValue()) {
/* 246 */         AnnotationWriter.this.writeIndex(param1Primitive_element_value.const_value_index, param1Boolean.booleanValue());
/*     */       } else {
/* 248 */         AnnotationWriter.this.print((char)param1Primitive_element_value.tag + "#" + param1Primitive_element_value.const_value_index);
/* 249 */       }  return null;
/*     */     }
/*     */
/*     */     public Void visitEnum(Annotation.Enum_element_value param1Enum_element_value, Boolean param1Boolean) {
/* 253 */       if (param1Boolean.booleanValue()) {
/* 254 */         AnnotationWriter.this.writeIndex(param1Enum_element_value.type_name_index, param1Boolean.booleanValue());
/* 255 */         AnnotationWriter.this.print(".");
/* 256 */         AnnotationWriter.this.writeIndex(param1Enum_element_value.const_name_index, param1Boolean.booleanValue());
/*     */       } else {
/* 258 */         AnnotationWriter.this.print((char)param1Enum_element_value.tag + "#" + param1Enum_element_value.type_name_index + ".#" + param1Enum_element_value.const_name_index);
/* 259 */       }  return null;
/*     */     }
/*     */
/*     */     public Void visitClass(Annotation.Class_element_value param1Class_element_value, Boolean param1Boolean) {
/* 263 */       if (param1Boolean.booleanValue()) {
/* 264 */         AnnotationWriter.this.writeIndex(param1Class_element_value.class_info_index, param1Boolean.booleanValue());
/* 265 */         AnnotationWriter.this.print(".class");
/*     */       } else {
/* 267 */         AnnotationWriter.this.print((char)param1Class_element_value.tag + "#" + param1Class_element_value.class_info_index);
/* 268 */       }  return null;
/*     */     }
/*     */
/*     */     public Void visitAnnotation(Annotation.Annotation_element_value param1Annotation_element_value, Boolean param1Boolean) {
/* 272 */       AnnotationWriter.this.print(Character.valueOf((char)param1Annotation_element_value.tag));
/* 273 */       AnnotationWriter.this.write(param1Annotation_element_value.annotation_value, param1Boolean.booleanValue());
/* 274 */       return null;
/*     */     }
/*     */
/*     */     public Void visitArray(Annotation.Array_element_value param1Array_element_value, Boolean param1Boolean) {
/* 278 */       AnnotationWriter.this.print("[");
/* 279 */       for (byte b = 0; b < param1Array_element_value.num_values; b++) {
/* 280 */         if (b > 0)
/* 281 */           AnnotationWriter.this.print(",");
/* 282 */         write(param1Array_element_value.values[b], param1Boolean.booleanValue());
/*     */       }
/* 284 */       AnnotationWriter.this.print("]");
/* 285 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\AnnotationWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
