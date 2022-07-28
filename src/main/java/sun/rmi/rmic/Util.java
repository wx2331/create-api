/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.File;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */   implements Constants
/*     */ {
/*     */   public static File getOutputDirectoryFor(Identifier paramIdentifier, File paramFile, BatchEnvironment paramBatchEnvironment) {
/*  62 */     File file = null;
/*  63 */     String str1 = paramIdentifier.getFlatName().toString().replace('.', '$');
/*  64 */     String str2 = str1;
/*  65 */     String str3 = null;
/*  66 */     String str4 = paramIdentifier.getQualifier().toString();
/*     */     
/*  68 */     if (str4.length() > 0) {
/*  69 */       str2 = str4 + "." + str1;
/*  70 */       str3 = str4.replace('.', File.separatorChar);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  75 */     if (paramFile != null) {
/*     */ 
/*     */ 
/*     */       
/*  79 */       if (str3 != null)
/*     */       {
/*     */ 
/*     */         
/*  83 */         file = new File(paramFile, str3);
/*     */ 
/*     */ 
/*     */         
/*  87 */         ensureDirectory(file, paramBatchEnvironment);
/*     */       
/*     */       }
/*     */       else
/*     */       {
/*     */         
/*  93 */         file = paramFile;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  99 */       String str = System.getProperty("user.dir");
/* 100 */       File file1 = new File(str);
/*     */ 
/*     */ 
/*     */       
/* 104 */       if (str3 == null) {
/*     */ 
/*     */ 
/*     */         
/* 108 */         file = file1;
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 114 */         file = new File(file1, str3);
/*     */ 
/*     */ 
/*     */         
/* 118 */         ensureDirectory(file, paramBatchEnvironment);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 124 */     return file;
/*     */   }
/*     */   
/*     */   private static void ensureDirectory(File paramFile, BatchEnvironment paramBatchEnvironment) {
/* 128 */     if (!paramFile.exists()) {
/* 129 */       paramFile.mkdirs();
/* 130 */       if (!paramFile.exists()) {
/* 131 */         paramBatchEnvironment.error(0L, "rmic.cannot.create.dir", paramFile.getAbsolutePath());
/* 132 */         throw new InternalError();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */