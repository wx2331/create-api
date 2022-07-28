/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DirectoryLoader
/*     */   extends ClassLoader
/*     */ {
/*     */   private Hashtable cache;
/*     */   private File root;
/*     */   
/*     */   public DirectoryLoader(File paramFile) {
/*  54 */     this.cache = new Hashtable<>();
/*  55 */     if (paramFile == null || !paramFile.isDirectory()) {
/*  56 */       throw new IllegalArgumentException();
/*     */     }
/*  58 */     this.root = paramFile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DirectoryLoader() {}
/*     */ 
/*     */   
/*     */   public Class loadClass(String paramString) throws ClassNotFoundException {
/*  67 */     return loadClass(paramString, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Class loadClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/*  82 */     Class<?> clazz = (Class)this.cache.get(paramString);
/*     */     
/*  84 */     if (clazz == null) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/*  90 */         clazz = findSystemClass(paramString);
/*     */       }
/*  92 */       catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */ 
/*     */         
/*  96 */         byte[] arrayOfByte = getClassFileData(paramString);
/*     */         
/*  98 */         if (arrayOfByte == null) {
/*  99 */           throw new ClassNotFoundException();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 104 */         clazz = defineClass(arrayOfByte, 0, arrayOfByte.length);
/*     */         
/* 106 */         if (clazz == null) {
/* 107 */           throw new ClassFormatError();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 112 */         if (paramBoolean) resolveClass(clazz);
/*     */ 
/*     */ 
/*     */         
/* 116 */         this.cache.put(paramString, clazz);
/*     */       } 
/*     */     }
/*     */     
/* 120 */     return clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] getClassFileData(String paramString) {
/* 129 */     byte[] arrayOfByte = null;
/* 130 */     FileInputStream fileInputStream = null;
/*     */ 
/*     */ 
/*     */     
/* 134 */     File file = new File(this.root, paramString.replace('.', File.separatorChar) + ".class");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 139 */       fileInputStream = new FileInputStream(file);
/* 140 */       arrayOfByte = new byte[fileInputStream.available()];
/* 141 */       fileInputStream.read(arrayOfByte);
/* 142 */     } catch (ThreadDeath threadDeath) {
/* 143 */       throw threadDeath;
/* 144 */     } catch (Throwable throwable) {
/*     */ 
/*     */     
/*     */     } finally {
/* 148 */       if (fileInputStream != null) {
/*     */         try {
/* 150 */           fileInputStream.close();
/* 151 */         } catch (ThreadDeath threadDeath) {
/* 152 */           throw threadDeath;
/* 153 */         } catch (Throwable throwable) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 158 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\DirectoryLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */