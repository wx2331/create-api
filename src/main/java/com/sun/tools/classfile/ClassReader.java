/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassReader
/*     */ {
/*     */   private DataInputStream in;
/*     */   private ClassFile classFile;
/*     */   private Attribute.Factory attributeFactory;
/*     */   
/*     */   ClassReader(ClassFile paramClassFile, InputStream paramInputStream, Attribute.Factory paramFactory) throws IOException {
/*  43 */     paramClassFile.getClass();
/*  44 */     paramFactory.getClass();
/*     */     
/*  46 */     this.classFile = paramClassFile;
/*  47 */     this.in = new DataInputStream(new BufferedInputStream(paramInputStream));
/*  48 */     this.attributeFactory = paramFactory;
/*     */   }
/*     */   
/*     */   ClassFile getClassFile() {
/*  52 */     return this.classFile;
/*     */   }
/*     */   
/*     */   ConstantPool getConstantPool() {
/*  56 */     return this.classFile.constant_pool;
/*     */   }
/*     */   
/*     */   public Attribute readAttribute() throws IOException {
/*  60 */     int i = readUnsignedShort();
/*  61 */     int j = readInt();
/*  62 */     byte[] arrayOfByte = new byte[j];
/*  63 */     readFully(arrayOfByte);
/*     */     
/*  65 */     DataInputStream dataInputStream = this.in;
/*  66 */     this.in = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
/*     */     try {
/*  68 */       return this.attributeFactory.createAttribute(this, i, arrayOfByte);
/*     */     } finally {
/*  70 */       this.in = dataInputStream;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readFully(byte[] paramArrayOfbyte) throws IOException {
/*  75 */     this.in.readFully(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/*  79 */     return this.in.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public int readUnsignedShort() throws IOException {
/*  83 */     return this.in.readUnsignedShort();
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/*  87 */     return this.in.readInt();
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException {
/*  91 */     return this.in.readLong();
/*     */   }
/*     */   
/*     */   public float readFloat() throws IOException {
/*  95 */     return this.in.readFloat();
/*     */   }
/*     */   
/*     */   public double readDouble() throws IOException {
/*  99 */     return this.in.readDouble();
/*     */   }
/*     */   
/*     */   public String readUTF() throws IOException {
/* 103 */     return this.in.readUTF();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ClassReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */