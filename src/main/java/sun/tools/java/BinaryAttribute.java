/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BinaryAttribute
/*     */   implements Constants
/*     */ {
/*     */   Identifier name;
/*     */   byte[] data;
/*     */   BinaryAttribute next;
/*     */   
/*     */   BinaryAttribute(Identifier paramIdentifier, byte[] paramArrayOfbyte, BinaryAttribute paramBinaryAttribute) {
/*  50 */     this.name = paramIdentifier;
/*  51 */     this.data = paramArrayOfbyte;
/*  52 */     this.next = paramBinaryAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BinaryAttribute load(DataInputStream paramDataInputStream, BinaryConstantPool paramBinaryConstantPool, int paramInt) throws IOException {
/*  59 */     BinaryAttribute binaryAttribute = null;
/*  60 */     int i = paramDataInputStream.readUnsignedShort();
/*     */     
/*  62 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  64 */       Identifier identifier = paramBinaryConstantPool.getIdentifier(paramDataInputStream.readUnsignedShort());
/*     */       
/*  66 */       int j = paramDataInputStream.readInt();
/*     */       
/*  68 */       if (identifier.equals(idCode) && (paramInt & 0x2) == 0) {
/*  69 */         paramDataInputStream.skipBytes(j);
/*     */       } else {
/*  71 */         byte[] arrayOfByte = new byte[j];
/*  72 */         paramDataInputStream.readFully(arrayOfByte);
/*  73 */         binaryAttribute = new BinaryAttribute(identifier, arrayOfByte, binaryAttribute);
/*     */       } 
/*     */     } 
/*  76 */     return binaryAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void write(BinaryAttribute paramBinaryAttribute, DataOutputStream paramDataOutputStream, BinaryConstantPool paramBinaryConstantPool, Environment paramEnvironment) throws IOException {
/*  84 */     byte b = 0; BinaryAttribute binaryAttribute;
/*  85 */     for (binaryAttribute = paramBinaryAttribute; binaryAttribute != null; binaryAttribute = binaryAttribute.next)
/*  86 */       b++; 
/*  87 */     paramDataOutputStream.writeShort(b);
/*     */ 
/*     */     
/*  90 */     for (binaryAttribute = paramBinaryAttribute; binaryAttribute != null; binaryAttribute = binaryAttribute.next) {
/*  91 */       Identifier identifier = binaryAttribute.name;
/*  92 */       byte[] arrayOfByte = binaryAttribute.data;
/*     */       
/*  94 */       paramDataOutputStream.writeShort(paramBinaryConstantPool.indexString(identifier.toString(), paramEnvironment));
/*     */       
/*  96 */       paramDataOutputStream.writeInt(arrayOfByte.length);
/*     */       
/*  98 */       paramDataOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier getName() {
/* 106 */     return this.name;
/*     */   } public byte[] getData() {
/* 108 */     return this.data;
/*     */   } public BinaryAttribute getNextAttribute() {
/* 110 */     return this.next;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\BinaryAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */