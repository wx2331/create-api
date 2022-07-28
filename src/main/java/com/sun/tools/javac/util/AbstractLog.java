/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLog
/*     */ {
/*     */   protected JCDiagnostic.Factory diags;
/*     */   protected DiagnosticSource source;
/*     */   protected Map<JavaFileObject, DiagnosticSource> sourceMap;
/*     */   
/*     */   AbstractLog(JCDiagnostic.Factory paramFactory) {
/*  49 */     this.diags = paramFactory;
/*  50 */     this.sourceMap = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaFileObject useSource(JavaFileObject paramJavaFileObject) {
/*  56 */     JavaFileObject javaFileObject = (this.source == null) ? null : this.source.getFile();
/*  57 */     this.source = getSource(paramJavaFileObject);
/*  58 */     return javaFileObject;
/*     */   }
/*     */   
/*     */   protected DiagnosticSource getSource(JavaFileObject paramJavaFileObject) {
/*  62 */     if (paramJavaFileObject == null)
/*  63 */       return DiagnosticSource.NO_SOURCE; 
/*  64 */     DiagnosticSource diagnosticSource = this.sourceMap.get(paramJavaFileObject);
/*  65 */     if (diagnosticSource == null) {
/*  66 */       diagnosticSource = new DiagnosticSource(paramJavaFileObject, this);
/*  67 */       this.sourceMap.put(paramJavaFileObject, diagnosticSource);
/*     */     } 
/*  69 */     return diagnosticSource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DiagnosticSource currentSource() {
/*  75 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object... paramVarArgs) {
/*  84 */     report(this.diags.error(this.source, null, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/*  94 */     report(this.diags.error(this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(JCDiagnostic.DiagnosticFlag paramDiagnosticFlag, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 105 */     JCDiagnostic jCDiagnostic = this.diags.error(this.source, paramDiagnosticPosition, paramString, paramVarArgs);
/* 106 */     jCDiagnostic.setFlag(paramDiagnosticFlag);
/* 107 */     report(jCDiagnostic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(int paramInt, String paramString, Object... paramVarArgs) {
/* 117 */     report(this.diags.error(this.source, wrap(paramInt), paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(JCDiagnostic.DiagnosticFlag paramDiagnosticFlag, int paramInt, String paramString, Object... paramVarArgs) {
/* 128 */     JCDiagnostic jCDiagnostic = this.diags.error(this.source, wrap(paramInt), paramString, paramVarArgs);
/* 129 */     jCDiagnostic.setFlag(paramDiagnosticFlag);
/* 130 */     report(jCDiagnostic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String paramString, Object... paramVarArgs) {
/* 139 */     report(this.diags.warning(this.source, null, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(Lint.LintCategory paramLintCategory, String paramString, Object... paramVarArgs) {
/* 149 */     report(this.diags.warning(paramLintCategory, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 159 */     report(this.diags.warning(this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(Lint.LintCategory paramLintCategory, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 170 */     report(this.diags.warning(paramLintCategory, this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(int paramInt, String paramString, Object... paramVarArgs) {
/* 180 */     report(this.diags.warning(this.source, wrap(paramInt), paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mandatoryWarning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 189 */     report(this.diags.mandatoryWarning(this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mandatoryWarning(Lint.LintCategory paramLintCategory, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 199 */     report(this.diags.mandatoryWarning(paramLintCategory, this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void note(String paramString, Object... paramVarArgs) {
/* 207 */     report(this.diags.note(this.source, null, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void note(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 215 */     report(this.diags.note(this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void note(int paramInt, String paramString, Object... paramVarArgs) {
/* 223 */     report(this.diags.note(this.source, wrap(paramInt), paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void note(JavaFileObject paramJavaFileObject, String paramString, Object... paramVarArgs) {
/* 231 */     report(this.diags.note(getSource(paramJavaFileObject), null, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mandatoryNote(JavaFileObject paramJavaFileObject, String paramString, Object... paramVarArgs) {
/* 239 */     report(this.diags.mandatoryNote(getSource(paramJavaFileObject), paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   protected abstract void report(JCDiagnostic paramJCDiagnostic);
/*     */   
/*     */   protected abstract void directError(String paramString, Object... paramVarArgs);
/*     */   
/*     */   private JCDiagnostic.DiagnosticPosition wrap(int paramInt) {
/* 247 */     return (paramInt == -1) ? null : new JCDiagnostic.SimpleDiagnosticPosition(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\AbstractLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */