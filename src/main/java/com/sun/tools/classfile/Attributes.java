/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class Attributes
/*     */   implements Iterable<Attribute>
/*     */ {
/*     */   public final Attribute[] attrs;
/*     */   public final Map<String, Attribute> map;
/*     */   
/*     */   Attributes(ClassReader paramClassReader) throws IOException {
/*  42 */     this.map = new HashMap<>();
/*  43 */     int i = paramClassReader.readUnsignedShort();
/*  44 */     this.attrs = new Attribute[i];
/*  45 */     for (byte b = 0; b < i; b++) {
/*  46 */       Attribute attribute = Attribute.read(paramClassReader);
/*  47 */       this.attrs[b] = attribute;
/*     */       try {
/*  49 */         this.map.put(attribute.getName(paramClassReader.getConstantPool()), attribute);
/*  50 */       } catch (ConstantPoolException constantPoolException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Attributes(ConstantPool paramConstantPool, Attribute[] paramArrayOfAttribute) {
/*  57 */     this.attrs = paramArrayOfAttribute;
/*  58 */     this.map = new HashMap<>();
/*  59 */     for (byte b = 0; b < paramArrayOfAttribute.length; b++) {
/*  60 */       Attribute attribute = paramArrayOfAttribute[b];
/*     */       try {
/*  62 */         this.map.put(attribute.getName(paramConstantPool), attribute);
/*  63 */       } catch (ConstantPoolException constantPoolException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Attribute> iterator() {
/*  70 */     return Arrays.<Attribute>asList(this.attrs).iterator();
/*     */   }
/*     */   
/*     */   public Attribute get(int paramInt) {
/*  74 */     return this.attrs[paramInt];
/*     */   }
/*     */   
/*     */   public Attribute get(String paramString) {
/*  78 */     return this.map.get(paramString);
/*     */   }
/*     */   
/*     */   public int getIndex(ConstantPool paramConstantPool, String paramString) {
/*  82 */     for (byte b = 0; b < this.attrs.length; b++) {
/*  83 */       Attribute attribute = this.attrs[b];
/*     */       try {
/*  85 */         if (attribute != null && attribute.getName(paramConstantPool).equals(paramString))
/*  86 */           return b; 
/*  87 */       } catch (ConstantPoolException constantPoolException) {}
/*     */     } 
/*     */ 
/*     */     
/*  91 */     return -1;
/*     */   }
/*     */   
/*     */   public int size() {
/*  95 */     return this.attrs.length;
/*     */   }
/*     */   
/*     */   public int byteLength() {
/*  99 */     int i = 2;
/* 100 */     for (Attribute attribute : this.attrs)
/* 101 */       i += attribute.byteLength(); 
/* 102 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Attributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */