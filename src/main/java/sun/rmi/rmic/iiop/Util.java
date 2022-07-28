/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import com.sun.corba.se.impl.util.PackagePrefixChecker;
/*     */ import java.io.File;
/*     */ import sun.rmi.rmic.Constants;
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
/*     */ public final class Util
/*     */   implements Constants
/*     */ {
/*     */   public static String packagePrefix() {
/*  48 */     return PackagePrefixChecker.packagePrefix();
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
/*     */   private static File getOutputDirectoryFor(Identifier paramIdentifier, File paramFile, BatchEnvironment paramBatchEnvironment, boolean paramBoolean) {
/*  63 */     File file = null;
/*  64 */     String str1 = paramIdentifier.getFlatName().toString().replace('.', '$');
/*  65 */     String str2 = str1;
/*  66 */     String str3 = null;
/*  67 */     String str4 = paramIdentifier.getQualifier().toString();
/*     */ 
/*     */     
/*  70 */     str4 = correctPackageName(str4, paramBoolean, paramBatchEnvironment.getStandardPackage());
/*     */     
/*  72 */     if (str4.length() > 0) {
/*  73 */       str2 = str4 + "." + str1;
/*  74 */       str3 = str4.replace('.', File.separatorChar);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (paramFile != null) {
/*     */ 
/*     */ 
/*     */       
/*  83 */       if (str3 != null)
/*     */       {
/*     */ 
/*     */         
/*  87 */         file = new File(paramFile, str3);
/*     */ 
/*     */ 
/*     */         
/*  91 */         ensureDirectory(file, paramBatchEnvironment);
/*     */       
/*     */       }
/*     */       else
/*     */       {
/*     */         
/*  97 */         file = paramFile;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 103 */       String str = System.getProperty("user.dir");
/* 104 */       File file1 = new File(str);
/*     */ 
/*     */ 
/*     */       
/* 108 */       if (str3 == null) {
/*     */ 
/*     */ 
/*     */         
/* 112 */         file = file1;
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 118 */         file = new File(file1, str3);
/*     */ 
/*     */ 
/*     */         
/* 122 */         ensureDirectory(file, paramBatchEnvironment);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 128 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getOutputDirectoryForIDL(Identifier paramIdentifier, File paramFile, BatchEnvironment paramBatchEnvironment) {
/* 134 */     return getOutputDirectoryFor(paramIdentifier, paramFile, paramBatchEnvironment, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getOutputDirectoryForStub(Identifier paramIdentifier, File paramFile, BatchEnvironment paramBatchEnvironment) {
/* 140 */     return getOutputDirectoryFor(paramIdentifier, paramFile, paramBatchEnvironment, false);
/*     */   }
/*     */   
/*     */   private static void ensureDirectory(File paramFile, BatchEnvironment paramBatchEnvironment) {
/* 144 */     if (!paramFile.exists()) {
/* 145 */       paramFile.mkdirs();
/* 146 */       if (!paramFile.exists()) {
/* 147 */         paramBatchEnvironment.error(0L, "rmic.cannot.create.dir", paramFile.getAbsolutePath());
/* 148 */         throw new InternalError();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String correctPackageName(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/* 155 */     if (paramBoolean1) {
/* 156 */       return paramString;
/*     */     }
/* 158 */     if (paramBoolean2) {
/* 159 */       return paramString;
/*     */     }
/* 161 */     return PackagePrefixChecker.correctPackageName(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOffendingPackage(String paramString) {
/* 167 */     return PackagePrefixChecker.isOffendingPackage(paramString);
/*     */   }
/*     */   
/*     */   public static boolean hasOffendingPrefix(String paramString) {
/* 171 */     return PackagePrefixChecker.hasOffendingPrefix(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */