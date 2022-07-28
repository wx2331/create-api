/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ClassFile
/*     */ {
/*     */   public final int magic;
/*     */   public final int minor_version;
/*     */   public final int major_version;
/*     */   public final ConstantPool constant_pool;
/*     */   public final AccessFlags access_flags;
/*     */   public final int this_class;
/*     */   public final int super_class;
/*     */   public final int[] interfaces;
/*     */   public final Field[] fields;
/*     */   public final Method[] methods;
/*     */   public final Attributes attributes;
/*     */
/*     */   public static ClassFile read(File paramFile) throws IOException, ConstantPoolException {
/*  47 */     return read(paramFile.toPath(), new Attribute.Factory());
/*     */   }
/*     */
/*     */
/*     */   public static ClassFile read(Path paramPath) throws IOException, ConstantPoolException {
/*  52 */     return read(paramPath, new Attribute.Factory());
/*     */   }
/*     */
/*     */
/*     */   public static ClassFile read(Path paramPath, Attribute.Factory paramFactory) throws IOException, ConstantPoolException {
/*  57 */     try (InputStream inputStream = Files.newInputStream(paramPath, new java.nio.file.OpenOption[0])) {
/*  58 */       return new ClassFile(inputStream, paramFactory);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static ClassFile read(File paramFile, Attribute.Factory paramFactory) throws IOException, ConstantPoolException {
/*  64 */     return read(paramFile.toPath(), paramFactory);
/*     */   }
/*     */
/*     */
/*     */   public static ClassFile read(InputStream paramInputStream) throws IOException, ConstantPoolException {
/*  69 */     return new ClassFile(paramInputStream, new Attribute.Factory());
/*     */   }
/*     */
/*     */
/*     */   public static ClassFile read(InputStream paramInputStream, Attribute.Factory paramFactory) throws IOException, ConstantPoolException {
/*  74 */     return new ClassFile(paramInputStream, paramFactory);
/*     */   }
/*     */
/*     */   ClassFile(InputStream paramInputStream, Attribute.Factory paramFactory) throws IOException, ConstantPoolException {
/*  78 */     ClassReader classReader = new ClassReader(this, paramInputStream, paramFactory);
/*  79 */     this.magic = classReader.readInt();
/*  80 */     this.minor_version = classReader.readUnsignedShort();
/*  81 */     this.major_version = classReader.readUnsignedShort();
/*  82 */     this.constant_pool = new ConstantPool(classReader);
/*  83 */     this.access_flags = new AccessFlags(classReader);
/*  84 */     this.this_class = classReader.readUnsignedShort();
/*  85 */     this.super_class = classReader.readUnsignedShort();
/*     */
/*  87 */     int i = classReader.readUnsignedShort();
/*  88 */     this.interfaces = new int[i]; int j;
/*  89 */     for (j = 0; j < i; j++) {
/*  90 */       this.interfaces[j] = classReader.readUnsignedShort();
/*     */     }
/*  92 */     j = classReader.readUnsignedShort();
/*  93 */     this.fields = new Field[j]; int k;
/*  94 */     for (k = 0; k < j; k++) {
/*  95 */       this.fields[k] = new Field(classReader);
/*     */     }
/*  97 */     k = classReader.readUnsignedShort();
/*  98 */     this.methods = new Method[k];
/*  99 */     for (byte b = 0; b < k; b++) {
/* 100 */       this.methods[b] = new Method(classReader);
/*     */     }
/* 102 */     this.attributes = new Attributes(classReader);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public ClassFile(int paramInt1, int paramInt2, int paramInt3, ConstantPool paramConstantPool, AccessFlags paramAccessFlags, int paramInt4, int paramInt5, int[] paramArrayOfint, Field[] paramArrayOfField, Method[] paramArrayOfMethod, Attributes paramAttributes) {
/* 109 */     this.magic = paramInt1;
/* 110 */     this.minor_version = paramInt2;
/* 111 */     this.major_version = paramInt3;
/* 112 */     this.constant_pool = paramConstantPool;
/* 113 */     this.access_flags = paramAccessFlags;
/* 114 */     this.this_class = paramInt4;
/* 115 */     this.super_class = paramInt5;
/* 116 */     this.interfaces = paramArrayOfint;
/* 117 */     this.fields = paramArrayOfField;
/* 118 */     this.methods = paramArrayOfMethod;
/* 119 */     this.attributes = paramAttributes;
/*     */   }
/*     */
/*     */   public String getName() throws ConstantPoolException {
/* 123 */     return this.constant_pool.getClassInfo(this.this_class).getName();
/*     */   }
/*     */
/*     */   public String getSuperclassName() throws ConstantPoolException {
/* 127 */     return this.constant_pool.getClassInfo(this.super_class).getName();
/*     */   }
/*     */
/*     */   public String getInterfaceName(int paramInt) throws ConstantPoolException {
/* 131 */     return this.constant_pool.getClassInfo(this.interfaces[paramInt]).getName();
/*     */   }
/*     */
/*     */   public Attribute getAttribute(String paramString) {
/* 135 */     return this.attributes.get(paramString);
/*     */   }
/*     */
/*     */   public boolean isClass() {
/* 139 */     return !isInterface();
/*     */   }
/*     */
/*     */   public boolean isInterface() {
/* 143 */     return this.access_flags.is(512);
/*     */   }
/*     */
/*     */   public int byteLength() {
/* 147 */     return 8 + this.constant_pool
/*     */
/*     */
/* 150 */       .byteLength() + 2 + 2 + 2 +
/*     */
/*     */
/*     */
/* 154 */       byteLength(this.interfaces) +
/* 155 */       byteLength(this.fields) +
/* 156 */       byteLength(this.methods) + this.attributes
/* 157 */       .byteLength();
/*     */   }
/*     */
/*     */   private int byteLength(int[] paramArrayOfint) {
/* 161 */     return 2 + 2 * paramArrayOfint.length;
/*     */   }
/*     */
/*     */   private int byteLength(Field[] paramArrayOfField) {
/* 165 */     int i = 2;
/* 166 */     for (Field field : paramArrayOfField)
/* 167 */       i += field.byteLength();
/* 168 */     return i;
/*     */   }
/*     */
/*     */   private int byteLength(Method[] paramArrayOfMethod) {
/* 172 */     int i = 2;
/* 173 */     for (Method method : paramArrayOfMethod)
/* 174 */       i += method.byteLength();
/* 175 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ClassFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
