/*     */ package sun.rmi.rmic.newrmic.jrmp;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.rmi.rmic.newrmic.BatchEnvironment;
/*     */ import sun.rmi.rmic.newrmic.Generator;
/*     */ import sun.rmi.rmic.newrmic.IndentingWriter;
/*     */ import sun.rmi.rmic.newrmic.Main;
/*     */ import sun.rmi.rmic.newrmic.Resources;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JrmpGenerator
/*     */   implements Generator
/*     */ {
/*  58 */   private static final Map<String, Constants.StubVersion> versionOptions = new HashMap<>();
/*     */   
/*     */   static {
/*  61 */     versionOptions.put("-v1.1", Constants.StubVersion.V1_1);
/*  62 */     versionOptions.put("-vcompat", Constants.StubVersion.VCOMPAT);
/*  63 */     versionOptions.put("-v1.2", Constants.StubVersion.V1_2);
/*     */   }
/*     */   
/*  66 */   private static final Set<String> bootstrapClassNames = new HashSet<>();
/*     */   
/*     */   static {
/*  69 */     bootstrapClassNames.add("java.lang.Exception");
/*  70 */     bootstrapClassNames.add("java.rmi.Remote");
/*  71 */     bootstrapClassNames.add("java.rmi.RemoteException");
/*  72 */     bootstrapClassNames.add("java.lang.RuntimeException");
/*     */   }
/*     */ 
/*     */   
/*  76 */   private Constants.StubVersion version = Constants.StubVersion.V1_2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain) {
/*  89 */     String str = null;
/*  90 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  91 */       String str1 = paramArrayOfString[b];
/*  92 */       if (versionOptions.containsKey(str1)) {
/*  93 */         if (str != null && !str.equals(str1)) {
/*  94 */           paramMain.error("rmic.cannot.use.both", new String[] { str, str1 });
/*  95 */           return false;
/*     */         } 
/*  97 */         str = str1;
/*  98 */         this.version = versionOptions.get(str1);
/*  99 */         paramArrayOfString[b] = null;
/*     */       } 
/*     */     } 
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends BatchEnvironment> envClass() {
/* 110 */     return BatchEnvironment.class;
/*     */   }
/*     */   
/*     */   public Set<String> bootstrapClassNames() {
/* 114 */     return Collections.unmodifiableSet(bootstrapClassNames);
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
/*     */   public void generate(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc, File paramFile) {
/* 126 */     RemoteClass remoteClass = RemoteClass.forClass(paramBatchEnvironment, paramClassDoc);
/* 127 */     if (remoteClass == null) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     StubSkeletonWriter stubSkeletonWriter = new StubSkeletonWriter(paramBatchEnvironment, remoteClass, this.version);
/*     */ 
/*     */     
/* 134 */     File file1 = sourceFileForClass(stubSkeletonWriter.stubClassName(), paramFile);
/*     */     try {
/* 136 */       IndentingWriter indentingWriter = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(file1)));
/*     */       
/* 138 */       stubSkeletonWriter.writeStub(indentingWriter);
/* 139 */       indentingWriter.close();
/* 140 */       if (paramBatchEnvironment.verbose()) {
/* 141 */         paramBatchEnvironment.output(Resources.getText("rmic.wrote", new String[] { file1
/* 142 */                 .getPath() }));
/*     */       }
/* 144 */       paramBatchEnvironment.addGeneratedFile(file1);
/* 145 */     } catch (IOException iOException) {
/* 146 */       paramBatchEnvironment.error("rmic.cant.write", new String[] { file1.toString() });
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 151 */     File file2 = sourceFileForClass(stubSkeletonWriter.skeletonClassName(), paramFile);
/* 152 */     if (this.version == Constants.StubVersion.V1_1 || this.version == Constants.StubVersion.VCOMPAT) {
/*     */ 
/*     */       
/*     */       try {
/* 156 */         IndentingWriter indentingWriter = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(file2)));
/*     */ 
/*     */         
/* 159 */         stubSkeletonWriter.writeSkeleton(indentingWriter);
/* 160 */         indentingWriter.close();
/* 161 */         if (paramBatchEnvironment.verbose()) {
/* 162 */           paramBatchEnvironment.output(Resources.getText("rmic.wrote", new String[] { file2
/* 163 */                   .getPath() }));
/*     */         }
/* 165 */         paramBatchEnvironment.addGeneratedFile(file2);
/* 166 */       } catch (IOException iOException) {
/* 167 */         paramBatchEnvironment.error("rmic.cant.write", new String[] { file2.toString() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } else {
/* 179 */       File file = classFileForClass(stubSkeletonWriter.skeletonClassName(), paramFile);
/*     */       
/* 181 */       file2.delete();
/* 182 */       file.delete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File sourceFileForClass(String paramString, File paramFile) {
/* 193 */     return fileForClass(paramString, paramFile, ".java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File classFileForClass(String paramString, File paramFile) {
/* 202 */     return fileForClass(paramString, paramFile, ".class");
/*     */   }
/*     */   
/*     */   private File fileForClass(String paramString1, File paramFile, String paramString2) {
/* 206 */     int i = paramString1.lastIndexOf('.');
/* 207 */     String str = paramString1.substring(i + 1) + paramString2;
/* 208 */     if (i != -1) {
/* 209 */       String str1 = paramString1.substring(0, i);
/* 210 */       String str2 = str1.replace('.', File.separatorChar);
/* 211 */       File file = new File(paramFile, str2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 218 */       if (!file.exists()) {
/* 219 */         file.mkdirs();
/*     */       }
/* 221 */       return new File(file, str);
/*     */     } 
/* 223 */     return new File(paramFile, str);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\jrmp\JrmpGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */