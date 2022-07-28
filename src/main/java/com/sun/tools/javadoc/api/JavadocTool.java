/*     */ package com.sun.tools.javadoc.api;
/*     */
/*     */ import com.sun.tools.javac.api.ClientCodeWrapper;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javadoc.Main;
/*     */ import com.sun.tools.javadoc.ToolOption;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.DocumentationTool;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JavadocTool
/*     */   implements DocumentationTool
/*     */ {
/*     */   public DocumentationTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1) {
/*  71 */     Context context = new Context();
/*  72 */     return getTask(paramWriter, paramJavaFileManager, paramDiagnosticListener, paramClass, paramIterable, paramIterable1, context);
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
/*     */   public DocumentationTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1, Context paramContext) {
/*     */     try {
/*  85 */       ClientCodeWrapper clientCodeWrapper = ClientCodeWrapper.instance(paramContext);
/*     */
/*  87 */       if (paramIterable != null) {
/*  88 */         for (String str : paramIterable) {
/*  89 */           str.getClass();
/*     */         }
/*     */       }
/*  92 */       if (paramIterable1 != null) {
/*  93 */         paramIterable1 = clientCodeWrapper.wrapJavaFileObjects(paramIterable1);
/*  94 */         for (JavaFileObject javaFileObject : paramIterable1) {
/*  95 */           if (javaFileObject.getKind() != JavaFileObject.Kind.SOURCE)
/*     */           {
/*  97 */             throw new IllegalArgumentException("All compilation units must be of SOURCE kind");
/*     */           }
/*     */         }
/*     */       }
/*     */
/* 102 */       if (paramDiagnosticListener != null) {
/* 103 */         paramContext.put(DiagnosticListener.class, clientCodeWrapper.wrap(paramDiagnosticListener));
/*     */       }
/* 105 */       if (paramWriter == null) {
/* 106 */         paramContext.put(Log.outKey, new PrintWriter(System.err, true));
/* 107 */       } else if (paramWriter instanceof PrintWriter) {
/* 108 */         paramContext.put(Log.outKey, paramWriter);
/*     */       } else {
/* 110 */         paramContext.put(Log.outKey, new PrintWriter(paramWriter, true));
/*     */       }
/* 112 */       if (paramJavaFileManager == null)
/* 113 */         paramJavaFileManager = getStandardFileManager(paramDiagnosticListener, null, null);
/* 114 */       paramJavaFileManager = clientCodeWrapper.wrap(paramJavaFileManager);
/* 115 */       paramContext.put(JavaFileManager.class, paramJavaFileManager);
/*     */
/* 117 */       return new JavadocTaskImpl(paramContext, paramClass, paramIterable, paramIterable1);
/* 118 */     } catch (ClientCodeException clientCodeException) {
/* 119 */       throw new RuntimeException(clientCodeException.getCause());
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset) {
/* 129 */     Context context = new Context();
/* 130 */     context.put(Locale.class, paramLocale);
/* 131 */     if (paramDiagnosticListener != null)
/* 132 */       context.put(DiagnosticListener.class, paramDiagnosticListener);
/* 133 */     PrintWriter printWriter = (paramCharset == null) ? new PrintWriter(System.err, true) : new PrintWriter(new OutputStreamWriter(System.err, paramCharset), true);
/*     */
/*     */
/* 136 */     context.put(Log.outKey, printWriter);
/* 137 */     return (StandardJavaFileManager)new JavacFileManager(context, true, paramCharset);
/*     */   }
/*     */
/*     */
/*     */   public int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String... paramVarArgs) {
/* 142 */     PrintWriter printWriter1 = new PrintWriter((paramOutputStream2 == null) ? System.err : paramOutputStream2, true);
/* 143 */     PrintWriter printWriter2 = new PrintWriter((paramOutputStream1 == null) ? System.out : paramOutputStream1);
/*     */     try {
/* 145 */       String str = "com.sun.tools.doclets.standard.Standard";
/* 146 */       ClassLoader classLoader = getClass().getClassLoader();
/* 147 */       return Main.execute("javadoc", printWriter1, printWriter1, printWriter2, str, classLoader, paramVarArgs);
/*     */     } finally {
/*     */
/* 150 */       printWriter1.flush();
/* 151 */       printWriter2.flush();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Set<SourceVersion> getSourceVersions() {
/* 157 */     return Collections.unmodifiableSet(
/* 158 */         EnumSet.range(SourceVersion.RELEASE_3, SourceVersion.latest()));
/*     */   }
/*     */
/*     */
/*     */   public int isSupportedOption(String paramString) {
/* 163 */     if (paramString == null)
/* 164 */       throw new NullPointerException();
/* 165 */     for (ToolOption toolOption : ToolOption.values()) {
/* 166 */       if (toolOption.opt.equals(paramString))
/* 167 */         return toolOption.hasArg ? 1 : 0;
/*     */     }
/* 169 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\api\JavadocTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
