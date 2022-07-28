/*    */ package com.sun.tools.javah;
/*    */
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Arrays;
/*    */ import java.util.EnumSet;
/*    */ import java.util.Locale;
/*    */ import java.util.Set;
/*    */ import javax.lang.model.SourceVersion;
/*    */ import javax.tools.DiagnosticListener;
/*    */ import javax.tools.JavaFileManager;
/*    */ import javax.tools.JavaFileObject;
/*    */ import javax.tools.StandardJavaFileManager;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public class JavahTool
/*    */   implements NativeHeaderTool
/*    */ {
/*    */   public NativeHeaderTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2) {
/* 55 */     return new JavahTask(paramWriter, paramJavaFileManager, paramDiagnosticListener, paramIterable1, paramIterable2);
/*    */   }
/*    */
/*    */   public StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset) {
/* 59 */     return JavahTask.getDefaultFileManager(paramDiagnosticListener, null);
/*    */   }
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */   public int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String... paramVarArgs) {
/* 67 */     JavahTask javahTask = new JavahTask(JavahTask.getPrintWriterForStream(paramOutputStream1), null, null, Arrays.asList(paramVarArgs), null);
/*    */
/* 69 */     return javahTask.run() ? 0 : 1;
/*    */   }
/*    */
/*    */   public Set<SourceVersion> getSourceVersions() {
/* 73 */     return EnumSet.allOf(SourceVersion.class);
/*    */   }
/*    */
/*    */   public int isSupportedOption(String paramString) {
/* 77 */     JavahTask.Option[] arrayOfOption = JavahTask.recognizedOptions;
/* 78 */     for (byte b = 0; b < arrayOfOption.length; b++) {
/* 79 */       if (arrayOfOption[b].matches(paramString))
/* 80 */         return (arrayOfOption[b]).hasArg ? 1 : 0;
/*    */     }
/* 82 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\JavahTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
