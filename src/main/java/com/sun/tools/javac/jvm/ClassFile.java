/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static final int JAVA_MAGIC = -889275714;
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Unicode = 2;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameandType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   public static final int REF_getField = 1;
/*     */   public static final int REF_getStatic = 2;
/*     */   public static final int REF_putField = 3;
/*     */   public static final int REF_putStatic = 4;
/*     */   public static final int REF_invokeVirtual = 5;
/*     */   public static final int REF_invokeStatic = 6;
/*     */   public static final int REF_invokeSpecial = 7;
/*     */   public static final int REF_newInvokeSpecial = 8;
/*     */   public static final int REF_invokeInterface = 9;
/*     */   public static final int MAX_PARAMETERS = 255;
/*     */   public static final int MAX_DIMENSIONS = 255;
/*     */   public static final int MAX_CODE = 65535;
/*     */   public static final int MAX_LOCALS = 65535;
/*     */   public static final int MAX_STACK = 65535;
/*     */   
/*     */   public enum Version
/*     */   {
/* 106 */     V45_3(45, 3),
/* 107 */     V49(49, 0),
/* 108 */     V50(50, 0),
/* 109 */     V51(51, 0),
/* 110 */     V52(52, 0);
/*     */     Version(int param1Int1, int param1Int2) {
/* 112 */       this.major = param1Int1;
/* 113 */       this.minor = param1Int2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final int major;
/*     */ 
/*     */     
/*     */     public final int minor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] internalize(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 127 */     byte[] arrayOfByte = new byte[paramInt2];
/* 128 */     for (byte b = 0; b < paramInt2; b++) {
/* 129 */       byte b1 = paramArrayOfbyte[paramInt1 + b];
/* 130 */       if (b1 == 47) { arrayOfByte[b] = 46; }
/* 131 */       else { arrayOfByte[b] = b1; }
/*     */     
/* 133 */     }  return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] internalize(Name paramName) {
/* 140 */     return internalize(paramName.getByteArray(), paramName.getByteOffset(), paramName.getByteLength());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] externalize(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 147 */     byte[] arrayOfByte = new byte[paramInt2];
/* 148 */     for (byte b = 0; b < paramInt2; b++) {
/* 149 */       byte b1 = paramArrayOfbyte[paramInt1 + b];
/* 150 */       if (b1 == 46) { arrayOfByte[b] = 47; }
/* 151 */       else { arrayOfByte[b] = b1; }
/*     */     
/* 153 */     }  return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] externalize(Name paramName) {
/* 160 */     return externalize(paramName.getByteArray(), paramName.getByteOffset(), paramName.getByteLength());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NameAndType
/*     */   {
/*     */     Name name;
/*     */     
/*     */     Types.UniqueType uniqueType;
/*     */     
/*     */     Types types;
/*     */ 
/*     */     
/*     */     NameAndType(Name param1Name, Type param1Type, Types param1Types) {
/* 175 */       this.name = param1Name;
/* 176 */       this.uniqueType = new Types.UniqueType(param1Type, param1Types);
/* 177 */       this.types = param1Types;
/*     */     }
/*     */     
/*     */     void setType(Type param1Type) {
/* 181 */       this.uniqueType = new Types.UniqueType(param1Type, this.types);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 186 */       return (param1Object instanceof NameAndType && this.name == ((NameAndType)param1Object).name && this.uniqueType
/*     */         
/* 188 */         .equals(((NameAndType)param1Object).uniqueType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 193 */       return this.name.hashCode() * this.uniqueType.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\ClassFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */