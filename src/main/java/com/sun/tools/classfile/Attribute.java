/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
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
/*     */ public abstract class Attribute
/*     */ {
/*     */   public static final String AnnotationDefault = "AnnotationDefault";
/*     */   public static final String BootstrapMethods = "BootstrapMethods";
/*     */   public static final String CharacterRangeTable = "CharacterRangeTable";
/*     */   public static final String Code = "Code";
/*     */   public static final String ConstantValue = "ConstantValue";
/*     */   public static final String CompilationID = "CompilationID";
/*     */   public static final String Deprecated = "Deprecated";
/*     */   public static final String EnclosingMethod = "EnclosingMethod";
/*     */   public static final String Exceptions = "Exceptions";
/*     */   public static final String InnerClasses = "InnerClasses";
/*     */   public static final String LineNumberTable = "LineNumberTable";
/*     */   public static final String LocalVariableTable = "LocalVariableTable";
/*     */   public static final String LocalVariableTypeTable = "LocalVariableTypeTable";
/*     */   public static final String MethodParameters = "MethodParameters";
/*     */   public static final String RuntimeVisibleAnnotations = "RuntimeVisibleAnnotations";
/*     */   public static final String RuntimeInvisibleAnnotations = "RuntimeInvisibleAnnotations";
/*     */   public static final String RuntimeVisibleParameterAnnotations = "RuntimeVisibleParameterAnnotations";
/*     */   public static final String RuntimeInvisibleParameterAnnotations = "RuntimeInvisibleParameterAnnotations";
/*     */   public static final String RuntimeVisibleTypeAnnotations = "RuntimeVisibleTypeAnnotations";
/*     */   public static final String RuntimeInvisibleTypeAnnotations = "RuntimeInvisibleTypeAnnotations";
/*     */   public static final String Signature = "Signature";
/*     */   public static final String SourceDebugExtension = "SourceDebugExtension";
/*     */   public static final String SourceFile = "SourceFile";
/*     */   public static final String SourceID = "SourceID";
/*     */   public static final String StackMap = "StackMap";
/*     */   public static final String StackMapTable = "StackMapTable";
/*     */   public static final String Synthetic = "Synthetic";
/*     */   public final int attribute_name_index;
/*     */   public final int attribute_length;
/*     */   
/*     */   public static class Factory
/*     */   {
/*     */     private Map<String, Class<? extends Attribute>> standardAttributes;
/*     */     
/*     */     public Attribute createAttribute(ClassReader param1ClassReader, int param1Int, byte[] param1ArrayOfbyte) throws IOException {
/*     */       String str;
/*  76 */       if (this.standardAttributes == null) {
/*  77 */         init();
/*     */       }
/*     */       
/*  80 */       ConstantPool constantPool = param1ClassReader.getConstantPool();
/*     */       
/*     */       try {
/*  83 */         String str1 = constantPool.getUTF8Value(param1Int);
/*  84 */         Class clazz = this.standardAttributes.get(str1);
/*  85 */         if (clazz != null) {
/*     */           try {
/*  87 */             Class[] arrayOfClass = { ClassReader.class, int.class, int.class };
/*  88 */             Constructor<Attribute> constructor = clazz.getDeclaredConstructor(arrayOfClass);
/*  89 */             return constructor.newInstance(new Object[] { param1ClassReader, Integer.valueOf(param1Int), Integer.valueOf(param1ArrayOfbyte.length) });
/*  90 */           } catch (Throwable throwable) {
/*  91 */             str = throwable.toString();
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  96 */           str = "unknown attribute";
/*     */         } 
/*  98 */       } catch (ConstantPoolException constantPoolException) {
/*  99 */         str = constantPoolException.toString();
/*     */       } 
/*     */       
/* 102 */       return new DefaultAttribute(param1ClassReader, param1Int, param1ArrayOfbyte, str);
/*     */     }
/*     */     
/*     */     protected void init() {
/* 106 */       this.standardAttributes = new HashMap<>();
/* 107 */       this.standardAttributes.put("AnnotationDefault", AnnotationDefault_attribute.class);
/* 108 */       this.standardAttributes.put("BootstrapMethods", BootstrapMethods_attribute.class);
/* 109 */       this.standardAttributes.put("CharacterRangeTable", CharacterRangeTable_attribute.class);
/* 110 */       this.standardAttributes.put("Code", Code_attribute.class);
/* 111 */       this.standardAttributes.put("CompilationID", CompilationID_attribute.class);
/* 112 */       this.standardAttributes.put("ConstantValue", ConstantValue_attribute.class);
/* 113 */       this.standardAttributes.put("Deprecated", Deprecated_attribute.class);
/* 114 */       this.standardAttributes.put("EnclosingMethod", EnclosingMethod_attribute.class);
/* 115 */       this.standardAttributes.put("Exceptions", Exceptions_attribute.class);
/* 116 */       this.standardAttributes.put("InnerClasses", InnerClasses_attribute.class);
/* 117 */       this.standardAttributes.put("LineNumberTable", LineNumberTable_attribute.class);
/* 118 */       this.standardAttributes.put("LocalVariableTable", LocalVariableTable_attribute.class);
/* 119 */       this.standardAttributes.put("LocalVariableTypeTable", LocalVariableTypeTable_attribute.class);
/* 120 */       this.standardAttributes.put("MethodParameters", MethodParameters_attribute.class);
/* 121 */       this.standardAttributes.put("RuntimeInvisibleAnnotations", RuntimeInvisibleAnnotations_attribute.class);
/* 122 */       this.standardAttributes.put("RuntimeInvisibleParameterAnnotations", RuntimeInvisibleParameterAnnotations_attribute.class);
/* 123 */       this.standardAttributes.put("RuntimeVisibleAnnotations", RuntimeVisibleAnnotations_attribute.class);
/* 124 */       this.standardAttributes.put("RuntimeVisibleParameterAnnotations", RuntimeVisibleParameterAnnotations_attribute.class);
/* 125 */       this.standardAttributes.put("RuntimeVisibleTypeAnnotations", RuntimeVisibleTypeAnnotations_attribute.class);
/* 126 */       this.standardAttributes.put("RuntimeInvisibleTypeAnnotations", RuntimeInvisibleTypeAnnotations_attribute.class);
/* 127 */       this.standardAttributes.put("Signature", Signature_attribute.class);
/* 128 */       this.standardAttributes.put("SourceDebugExtension", SourceDebugExtension_attribute.class);
/* 129 */       this.standardAttributes.put("SourceFile", SourceFile_attribute.class);
/* 130 */       this.standardAttributes.put("SourceID", SourceID_attribute.class);
/* 131 */       this.standardAttributes.put("StackMap", StackMap_attribute.class);
/* 132 */       this.standardAttributes.put("StackMapTable", StackMapTable_attribute.class);
/* 133 */       this.standardAttributes.put("Synthetic", Synthetic_attribute.class);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Attribute read(ClassReader paramClassReader) throws IOException {
/* 140 */     return paramClassReader.readAttribute();
/*     */   }
/*     */   
/*     */   protected Attribute(int paramInt1, int paramInt2) {
/* 144 */     this.attribute_name_index = paramInt1;
/* 145 */     this.attribute_length = paramInt2;
/*     */   }
/*     */   
/*     */   public String getName(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 149 */     return paramConstantPool.getUTF8Value(this.attribute_name_index);
/*     */   }
/*     */   
/*     */   public abstract <R, D> R accept(Visitor<R, D> paramVisitor, D paramD);
/*     */   
/*     */   public int byteLength() {
/* 155 */     return 6 + this.attribute_length;
/*     */   }
/*     */   
/*     */   public static interface Visitor<R, P> {
/*     */     R visitBootstrapMethods(BootstrapMethods_attribute param1BootstrapMethods_attribute, P param1P);
/*     */     
/*     */     R visitDefault(DefaultAttribute param1DefaultAttribute, P param1P);
/*     */     
/*     */     R visitAnnotationDefault(AnnotationDefault_attribute param1AnnotationDefault_attribute, P param1P);
/*     */     
/*     */     R visitCharacterRangeTable(CharacterRangeTable_attribute param1CharacterRangeTable_attribute, P param1P);
/*     */     
/*     */     R visitCode(Code_attribute param1Code_attribute, P param1P);
/*     */     
/*     */     R visitCompilationID(CompilationID_attribute param1CompilationID_attribute, P param1P);
/*     */     
/*     */     R visitConstantValue(ConstantValue_attribute param1ConstantValue_attribute, P param1P);
/*     */     
/*     */     R visitDeprecated(Deprecated_attribute param1Deprecated_attribute, P param1P);
/*     */     
/*     */     R visitEnclosingMethod(EnclosingMethod_attribute param1EnclosingMethod_attribute, P param1P);
/*     */     
/*     */     R visitExceptions(Exceptions_attribute param1Exceptions_attribute, P param1P);
/*     */     
/*     */     R visitInnerClasses(InnerClasses_attribute param1InnerClasses_attribute, P param1P);
/*     */     
/*     */     R visitLineNumberTable(LineNumberTable_attribute param1LineNumberTable_attribute, P param1P);
/*     */     
/*     */     R visitLocalVariableTable(LocalVariableTable_attribute param1LocalVariableTable_attribute, P param1P);
/*     */     
/*     */     R visitLocalVariableTypeTable(LocalVariableTypeTable_attribute param1LocalVariableTypeTable_attribute, P param1P);
/*     */     
/*     */     R visitMethodParameters(MethodParameters_attribute param1MethodParameters_attribute, P param1P);
/*     */     
/*     */     R visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute param1RuntimeVisibleAnnotations_attribute, P param1P);
/*     */     
/*     */     R visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute param1RuntimeInvisibleAnnotations_attribute, P param1P);
/*     */     
/*     */     R visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute param1RuntimeVisibleParameterAnnotations_attribute, P param1P);
/*     */     
/*     */     R visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute param1RuntimeInvisibleParameterAnnotations_attribute, P param1P);
/*     */     
/*     */     R visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute param1RuntimeVisibleTypeAnnotations_attribute, P param1P);
/*     */     
/*     */     R visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute param1RuntimeInvisibleTypeAnnotations_attribute, P param1P);
/*     */     
/*     */     R visitSignature(Signature_attribute param1Signature_attribute, P param1P);
/*     */     
/*     */     R visitSourceDebugExtension(SourceDebugExtension_attribute param1SourceDebugExtension_attribute, P param1P);
/*     */     
/*     */     R visitSourceFile(SourceFile_attribute param1SourceFile_attribute, P param1P);
/*     */     
/*     */     R visitSourceID(SourceID_attribute param1SourceID_attribute, P param1P);
/*     */     
/*     */     R visitStackMap(StackMap_attribute param1StackMap_attribute, P param1P);
/*     */     
/*     */     R visitStackMapTable(StackMapTable_attribute param1StackMapTable_attribute, P param1P);
/*     */     
/*     */     R visitSynthetic(Synthetic_attribute param1Synthetic_attribute, P param1P);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */