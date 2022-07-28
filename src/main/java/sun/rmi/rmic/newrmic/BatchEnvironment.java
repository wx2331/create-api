/*     */ package sun.rmi.rmic.newrmic;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BatchEnvironment
/*     */ {
/*     */   private final RootDoc rootDoc;
/*     */   private final ClassDoc docRemote;
/*     */   private final ClassDoc docException;
/*     */   private final ClassDoc docRemoteException;
/*     */   private final ClassDoc docRuntimeException;
/*     */   private boolean verbose = false;
/*  71 */   private final List<File> generatedFiles = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BatchEnvironment(RootDoc paramRootDoc) {
/*  77 */     this.rootDoc = paramRootDoc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     this.docRemote = rootDoc().classNamed("java.rmi.Remote");
/*  85 */     this.docException = rootDoc().classNamed("java.lang.Exception");
/*  86 */     this.docRemoteException = rootDoc().classNamed("java.rmi.RemoteException");
/*  87 */     this.docRuntimeException = rootDoc().classNamed("java.lang.RuntimeException");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootDoc rootDoc() {
/*  94 */     return this.rootDoc;
/*     */   }
/*     */   
/*  97 */   public ClassDoc docRemote() { return this.docRemote; }
/*  98 */   public ClassDoc docException() { return this.docException; }
/*  99 */   public ClassDoc docRemoteException() { return this.docRemoteException; } public ClassDoc docRuntimeException() {
/* 100 */     return this.docRuntimeException;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean paramBoolean) {
/* 106 */     this.verbose = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verbose() {
/* 113 */     return this.verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGeneratedFile(File paramFile) {
/* 121 */     this.generatedFiles.add(paramFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<File> generatedFiles() {
/* 128 */     return Collections.unmodifiableList(this.generatedFiles);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void output(String paramString) {
/* 135 */     this.rootDoc.printNotice(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, String... paramVarArgs) {
/* 143 */     this.rootDoc.printError(Resources.getText(paramString, paramVarArgs));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\BatchEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */