/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassPath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathLoader
/*     */   extends ClassLoader
/*     */ {
/*     */   private ClassPath classPath;
/*     */   
/*     */   public ClassPathLoader(ClassPath paramClassPath) {
/*  43 */     this.classPath = paramClassPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class findClass(String paramString) throws ClassNotFoundException {
/*  49 */     byte[] arrayOfByte = loadClassData(paramString);
/*  50 */     return defineClass(paramString, arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] loadClassData(String paramString) throws ClassNotFoundException {
/*  61 */     String str = paramString.replace('.', File.separatorChar) + ".class";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     ClassFile classFile = this.classPath.getFile(str);
/*     */     
/*  69 */     if (classFile != null) {
/*     */ 
/*     */ 
/*     */       
/*  73 */       IOException iOException = null;
/*  74 */       byte[] arrayOfByte = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  81 */         DataInputStream dataInputStream = new DataInputStream(classFile.getInputStream());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  88 */         arrayOfByte = new byte[(int)classFile.length()];
/*     */         
/*     */         try {
/*  91 */           dataInputStream.readFully(arrayOfByte);
/*  92 */         } catch (IOException iOException1) {
/*     */ 
/*     */           
/*  95 */           arrayOfByte = null;
/*  96 */           iOException = iOException1;
/*     */         } finally {
/*     */ 
/*     */           
/* 100 */           try { dataInputStream.close(); } catch (IOException iOException1) {}
/*     */         } 
/* 102 */       } catch (IOException iOException1) {
/*     */ 
/*     */         
/* 105 */         iOException = iOException1;
/*     */       } 
/*     */       
/* 108 */       if (arrayOfByte == null) {
/* 109 */         throw new ClassNotFoundException(paramString, iOException);
/*     */       }
/* 111 */       return arrayOfByte;
/*     */     } 
/*     */ 
/*     */     
/* 115 */     throw new ClassNotFoundException(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\ClassPathLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */