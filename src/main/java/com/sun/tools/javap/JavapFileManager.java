/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import com.sun.tools.javac.file.JavacFileManager;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import java.io.PrintWriter;
/*    */ import java.nio.charset.Charset;
/*    */ import javax.tools.DiagnosticListener;
/*    */ import javax.tools.JavaFileObject;
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
/*    */ public class JavapFileManager
/*    */   extends JavacFileManager
/*    */ {
/*    */   private JavapFileManager(Context paramContext, Charset paramCharset) {
/* 46 */     super(paramContext, true, paramCharset);
/* 47 */     setSymbolFileEnabled(false);
/*    */   }
/*    */   
/*    */   public static JavapFileManager create(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, PrintWriter paramPrintWriter) {
/* 51 */     Context context = new Context();
/*    */     
/* 53 */     if (paramDiagnosticListener != null)
/* 54 */       context.put(DiagnosticListener.class, paramDiagnosticListener); 
/* 55 */     context.put(Log.outKey, paramPrintWriter);
/*    */     
/* 57 */     return new JavapFileManager(context, null);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\JavapFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */