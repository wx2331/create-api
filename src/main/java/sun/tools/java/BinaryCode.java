/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
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
/*     */ public class BinaryCode
/*     */   implements Constants
/*     */ {
/*     */   int maxStack;
/*     */   int maxLocals;
/*     */   BinaryExceptionHandler[] exceptionHandlers;
/*     */   BinaryAttribute atts;
/*     */   BinaryConstantPool cpool;
/*     */   byte[] code;
/*     */   
/*     */   public BinaryCode(byte[] paramArrayOfbyte, BinaryConstantPool paramBinaryConstantPool, Environment paramEnvironment) {
/*  50 */     DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(paramArrayOfbyte));
/*     */     try {
/*  52 */       this.cpool = paramBinaryConstantPool;
/*     */       
/*  54 */       this.maxStack = dataInputStream.readUnsignedShort();
/*     */       
/*  56 */       this.maxLocals = dataInputStream.readUnsignedShort();
/*     */       
/*  58 */       int i = dataInputStream.readInt();
/*  59 */       this.code = new byte[i];
/*     */       
/*  61 */       dataInputStream.read(this.code);
/*     */       
/*  63 */       int j = dataInputStream.readUnsignedShort();
/*  64 */       this.exceptionHandlers = new BinaryExceptionHandler[j];
/*  65 */       for (byte b = 0; b < j; b++) {
/*     */         
/*  67 */         int k = dataInputStream.readUnsignedShort();
/*     */         
/*  69 */         int m = dataInputStream.readUnsignedShort();
/*     */         
/*  71 */         int n = dataInputStream.readUnsignedShort();
/*     */         
/*  73 */         ClassDeclaration classDeclaration = paramBinaryConstantPool.getDeclaration(paramEnvironment, dataInputStream.readUnsignedShort());
/*  74 */         this.exceptionHandlers[b] = new BinaryExceptionHandler(k, m, n, classDeclaration);
/*     */       } 
/*     */       
/*  77 */       this.atts = BinaryAttribute.load(dataInputStream, paramBinaryConstantPool, -1);
/*  78 */       if (dataInputStream.available() != 0) {
/*  79 */         System.err.println("Should have exhausted input stream!");
/*     */       }
/*  81 */     } catch (IOException iOException) {
/*  82 */       throw new CompilerError(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BinaryExceptionHandler[] getExceptionHandlers() {
/*  92 */     return this.exceptionHandlers;
/*     */   }
/*     */   public byte[] getCode() {
/*  95 */     return this.code;
/*     */   } public int getMaxStack() {
/*  97 */     return this.maxStack;
/*     */   } public int getMaxLocals() {
/*  99 */     return this.maxLocals;
/*     */   } public BinaryAttribute getAttributes() {
/* 101 */     return this.atts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BinaryCode load(BinaryMember paramBinaryMember, BinaryConstantPool paramBinaryConstantPool, Environment paramEnvironment) {
/* 108 */     byte[] arrayOfByte = paramBinaryMember.getAttribute(idCode);
/* 109 */     return (arrayOfByte != null) ? new BinaryCode(arrayOfByte, paramBinaryConstantPool, paramEnvironment) : null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\BinaryCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */