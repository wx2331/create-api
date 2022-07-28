/*     */ package com.sun.tools.javap;
/*     */
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class StackMapWriter
/*     */   extends InstructionDetailWriter
/*     */ {
/*     */   private Map<Integer, StackMap> map;
/*     */   private ClassWriter classWriter;
/*     */   private final StackMapTable_attribute.verification_type_info[] empty;
/*     */
/*     */   static StackMapWriter instance(Context paramContext) {
/*  56 */     StackMapWriter stackMapWriter = paramContext.<StackMapWriter>get(StackMapWriter.class);
/*  57 */     if (stackMapWriter == null)
/*  58 */       stackMapWriter = new StackMapWriter(paramContext);
/*  59 */     return stackMapWriter;
/*     */   }
/*     */
/*     */   protected StackMapWriter(Context paramContext) {
/*  63 */     super(paramContext);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 290 */     this.empty = new StackMapTable_attribute.verification_type_info[0];
/*     */     paramContext.put(StackMapWriter.class, this);
/*     */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*     */     setStackMap((StackMapTable_attribute)paramCode_attribute.attributes.get("StackMapTable"));
/*     */   }
/*     */
/*     */   void setStackMap(StackMapTable_attribute paramStackMapTable_attribute) {
/*     */     String[] arrayOfString;
/*     */     if (paramStackMapTable_attribute == null) {
/*     */       this.map = null;
/*     */       return;
/*     */     }
/*     */     Method method = this.classWriter.getMethod();
/*     */     Descriptor descriptor = method.descriptor;
/*     */     try {
/*     */       ConstantPool constantPool = (this.classWriter.getClassFile()).constant_pool;
/*     */       String str = descriptor.getParameterTypes(constantPool);
/*     */       arrayOfString = str.substring(1, str.length() - 1).split("[, ]+");
/*     */     } catch (ConstantPoolException constantPoolException) {
/*     */       return;
/*     */     } catch (Descriptor.InvalidDescriptor invalidDescriptor) {
/*     */       return;
/*     */     }
/*     */     boolean bool = method.access_flags.is(8);
/*     */     StackMapTable_attribute.verification_type_info[] arrayOfVerification_type_info = new StackMapTable_attribute.verification_type_info[(bool ? 0 : 1) + arrayOfString.length];
/*     */     if (!bool)
/*     */       arrayOfVerification_type_info[0] = new CustomVerificationTypeInfo("this");
/*     */     for (byte b1 = 0; b1 < arrayOfString.length; b1++)
/*     */       arrayOfVerification_type_info[(bool ? 0 : 1) + b1] = new CustomVerificationTypeInfo(arrayOfString[b1].replace(".", "/"));
/*     */     this.map = new HashMap<>();
/*     */     StackMapBuilder stackMapBuilder = new StackMapBuilder();
/*     */     int i = -1;
/*     */     this.map.put(Integer.valueOf(i), new StackMap(arrayOfVerification_type_info, this.empty));
/*     */     for (byte b2 = 0; b2 < paramStackMapTable_attribute.entries.length; b2++)
/*     */       i = ((Integer)paramStackMapTable_attribute.entries[b2].accept(stackMapBuilder, Integer.valueOf(i))).intValue();
/*     */   }
/*     */
/*     */   public void writeInitialDetails() {
/*     */     writeDetails(-1);
/*     */   }
/*     */
/*     */   public void writeDetails(Instruction paramInstruction) {
/*     */     writeDetails(paramInstruction.getPC());
/*     */   }
/*     */
/*     */   private void writeDetails(int paramInt) {
/*     */     if (this.map == null)
/*     */       return;
/*     */     StackMap stackMap = this.map.get(Integer.valueOf(paramInt));
/*     */     if (stackMap != null) {
/*     */       print("StackMap locals: ", stackMap.locals);
/*     */       print("StackMap stack: ", stackMap.stack);
/*     */     }
/*     */   }
/*     */
/*     */   void print(String paramString, StackMapTable_attribute.verification_type_info[] paramArrayOfverification_type_info) {
/*     */     print(paramString);
/*     */     for (byte b = 0; b < paramArrayOfverification_type_info.length; b++) {
/*     */       print(" ");
/*     */       print(paramArrayOfverification_type_info[b]);
/*     */     }
/*     */     println();
/*     */   }
/*     */
/*     */   void print(StackMapTable_attribute.verification_type_info paramverification_type_info) {
/*     */     if (paramverification_type_info == null) {
/*     */       print("ERROR");
/*     */       return;
/*     */     }
/*     */     switch (paramverification_type_info.tag) {
/*     */       case -1:
/*     */         print(((CustomVerificationTypeInfo)paramverification_type_info).text);
/*     */         break;
/*     */       case 0:
/*     */         print("top");
/*     */         break;
/*     */       case 1:
/*     */         print("int");
/*     */         break;
/*     */       case 2:
/*     */         print("float");
/*     */         break;
/*     */       case 4:
/*     */         print("long");
/*     */         break;
/*     */       case 3:
/*     */         print("double");
/*     */         break;
/*     */       case 5:
/*     */         print("null");
/*     */         break;
/*     */       case 6:
/*     */         print("uninit_this");
/*     */         break;
/*     */       case 7:
/*     */         try {
/*     */           ConstantPool constantPool = (this.classWriter.getClassFile()).constant_pool;
/*     */           ConstantPool.CONSTANT_Class_info cONSTANT_Class_info = constantPool.getClassInfo(((StackMapTable_attribute.Object_variable_info)paramverification_type_info).cpool_index);
/*     */           print(constantPool.getUTF8Value(cONSTANT_Class_info.name_index));
/*     */         } catch (ConstantPoolException constantPoolException) {
/*     */           print("??");
/*     */         }
/*     */         break;
/*     */       case 8:
/*     */         print(Integer.valueOf(((StackMapTable_attribute.Uninitialized_variable_info)paramverification_type_info).offset));
/*     */         break;
/*     */     }
/*     */   }
/*     */
/*     */   class StackMapBuilder implements StackMapTable_attribute.stack_map_frame.Visitor<Integer, Integer> {
/*     */     public Integer visit_same_frame(StackMapTable_attribute.same_frame param1same_frame, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1same_frame.getOffsetDelta() + 1;
/*     */       StackMap stackMap = (StackMap)StackMapWriter.this.map.get(param1Integer);
/*     */       assert stackMap != null;
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */
/*     */     public Integer visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame param1same_locals_1_stack_item_frame, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1same_locals_1_stack_item_frame.getOffsetDelta() + 1;
/*     */       StackMap stackMap1 = (StackMap)StackMapWriter.this.map.get(param1Integer);
/*     */       assert stackMap1 != null;
/*     */       StackMap stackMap2 = new StackMap(stackMap1.locals, param1same_locals_1_stack_item_frame.stack);
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap2);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */
/*     */     public Integer visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended param1same_locals_1_stack_item_frame_extended, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1same_locals_1_stack_item_frame_extended.getOffsetDelta() + 1;
/*     */       StackMap stackMap1 = (StackMap)StackMapWriter.this.map.get(param1Integer);
/*     */       assert stackMap1 != null;
/*     */       StackMap stackMap2 = new StackMap(stackMap1.locals, param1same_locals_1_stack_item_frame_extended.stack);
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap2);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */
/*     */     public Integer visit_chop_frame(StackMapTable_attribute.chop_frame param1chop_frame, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1chop_frame.getOffsetDelta() + 1;
/*     */       StackMap stackMap1 = (StackMap)StackMapWriter.this.map.get(param1Integer);
/*     */       assert stackMap1 != null;
/*     */       int j = 251 - param1chop_frame.frame_type;
/*     */       StackMapTable_attribute.verification_type_info[] arrayOfVerification_type_info = Arrays.<StackMapTable_attribute.verification_type_info>copyOf(stackMap1.locals, stackMap1.locals.length - j);
/*     */       StackMap stackMap2 = new StackMap(arrayOfVerification_type_info, StackMapWriter.this.empty);
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap2);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */
/*     */     public Integer visit_same_frame_extended(StackMapTable_attribute.same_frame_extended param1same_frame_extended, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1same_frame_extended.getOffsetDelta();
/*     */       StackMap stackMap = (StackMap)StackMapWriter.this.map.get(param1Integer);
/*     */       assert stackMap != null;
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */
/*     */     public Integer visit_append_frame(StackMapTable_attribute.append_frame param1append_frame, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1append_frame.getOffsetDelta() + 1;
/*     */       StackMap stackMap1 = (StackMap)StackMapWriter.this.map.get(param1Integer);
/*     */       assert stackMap1 != null;
/*     */       StackMapTable_attribute.verification_type_info[] arrayOfVerification_type_info = new StackMapTable_attribute.verification_type_info[stackMap1.locals.length + param1append_frame.locals.length];
/*     */       System.arraycopy(stackMap1.locals, 0, arrayOfVerification_type_info, 0, stackMap1.locals.length);
/*     */       System.arraycopy(param1append_frame.locals, 0, arrayOfVerification_type_info, stackMap1.locals.length, param1append_frame.locals.length);
/*     */       StackMap stackMap2 = new StackMap(arrayOfVerification_type_info, StackMapWriter.this.empty);
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap2);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */
/*     */     public Integer visit_full_frame(StackMapTable_attribute.full_frame param1full_frame, Integer param1Integer) {
/*     */       int i = param1Integer.intValue() + param1full_frame.getOffsetDelta() + 1;
/*     */       StackMap stackMap = new StackMap(param1full_frame.locals, param1full_frame.stack);
/*     */       StackMapWriter.this.map.put(Integer.valueOf(i), stackMap);
/*     */       return Integer.valueOf(i);
/*     */     }
/*     */   }
/*     */
/*     */   static class StackMap {
/*     */     private final StackMapTable_attribute.verification_type_info[] locals;
/*     */     private final StackMapTable_attribute.verification_type_info[] stack;
/*     */
/*     */     StackMap(StackMapTable_attribute.verification_type_info[] param1ArrayOfverification_type_info1, StackMapTable_attribute.verification_type_info[] param1ArrayOfverification_type_info2) {
/*     */       this.locals = param1ArrayOfverification_type_info1;
/*     */       this.stack = param1ArrayOfverification_type_info2;
/*     */     }
/*     */   }
/*     */
/*     */   static class CustomVerificationTypeInfo extends StackMapTable_attribute.verification_type_info {
/*     */     private String text;
/*     */
/*     */     public CustomVerificationTypeInfo(String param1String) {
/*     */       super(-1);
/*     */       this.text = param1String;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\StackMapWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
