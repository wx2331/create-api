/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.JavacMessages;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Messager
/*     */   extends Log
/*     */   implements DocErrorReporter
/*     */ {
/*  55 */   public static final SourcePosition NOPOS = null; final String programName;
/*     */   private Locale locale;
/*     */   
/*     */   public static Messager instance0(Context paramContext) {
/*  59 */     Log log = (Log)paramContext.get(logKey);
/*  60 */     if (log == null || !(log instanceof Messager))
/*  61 */       throw new InternalError("no messager instance!"); 
/*  62 */     return (Messager)log;
/*     */   }
/*     */   private final JavacMessages messages; private final JCDiagnostic.Factory javadocDiags;
/*     */   
/*     */   public static void preRegister(Context paramContext, final String programName) {
/*  67 */     paramContext.put(logKey, new Context.Factory<Log>() {
/*     */           public Log make(Context param1Context) {
/*  69 */             return new Messager(param1Context, programName);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void preRegister(Context paramContext, final String programName, final PrintWriter errWriter, final PrintWriter warnWriter, final PrintWriter noticeWriter) {
/*  79 */     paramContext.put(logKey, new Context.Factory<Log>() {
/*     */           public Log make(Context param1Context) {
/*  81 */             return new Messager(param1Context, programName, errWriter, warnWriter, noticeWriter);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ExitJavadoc
/*     */     extends Error
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   static final PrintWriter defaultErrWriter = new PrintWriter(System.err);
/* 103 */   static final PrintWriter defaultWarnWriter = new PrintWriter(System.err);
/* 104 */   static final PrintWriter defaultNoticeWriter = new PrintWriter(System.out);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Messager(Context paramContext, String paramString) {
/* 111 */     this(paramContext, paramString, defaultErrWriter, defaultWarnWriter, defaultNoticeWriter);
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
/*     */   
/*     */   protected Messager(Context paramContext, String paramString, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3) {
/* 127 */     super(paramContext, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3);
/* 128 */     this.messages = JavacMessages.instance(paramContext);
/* 129 */     this.messages.add("com.sun.tools.javadoc.resources.javadoc");
/* 130 */     this.javadocDiags = new JCDiagnostic.Factory(this.messages, "javadoc");
/* 131 */     this.programName = paramString;
/*     */   }
/*     */   
/*     */   public void setLocale(Locale paramLocale) {
/* 135 */     this.locale = paramLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getText(String paramString, Object... paramVarArgs) {
/* 145 */     return this.messages.getLocalizedString(this.locale, paramString, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printError(String paramString) {
/* 155 */     printError((SourcePosition)null, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printError(SourcePosition paramSourcePosition, String paramString) {
/* 166 */     if (this.diagListener != null) {
/* 167 */       report(JCDiagnostic.DiagnosticType.ERROR, paramSourcePosition, paramString);
/*     */       
/*     */       return;
/*     */     } 
/* 171 */     if (this.nerrors < this.MaxErrors) {
/* 172 */       String str = (paramSourcePosition == null) ? this.programName : paramSourcePosition.toString();
/* 173 */       this.errWriter.println(str + ": " + getText("javadoc.error", new Object[0]) + " - " + paramString);
/* 174 */       this.errWriter.flush();
/* 175 */       prompt();
/* 176 */       this.nerrors++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printWarning(String paramString) {
/* 187 */     printWarning((SourcePosition)null, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printWarning(SourcePosition paramSourcePosition, String paramString) {
/* 198 */     if (this.diagListener != null) {
/* 199 */       report(JCDiagnostic.DiagnosticType.WARNING, paramSourcePosition, paramString);
/*     */       
/*     */       return;
/*     */     } 
/* 203 */     if (this.nwarnings < this.MaxWarnings) {
/* 204 */       String str = (paramSourcePosition == null) ? this.programName : paramSourcePosition.toString();
/* 205 */       this.warnWriter.println(str + ": " + getText("javadoc.warning", new Object[0]) + " - " + paramString);
/* 206 */       this.warnWriter.flush();
/* 207 */       this.nwarnings++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printNotice(String paramString) {
/* 218 */     printNotice((SourcePosition)null, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printNotice(SourcePosition paramSourcePosition, String paramString) {
/* 229 */     if (this.diagListener != null) {
/* 230 */       report(JCDiagnostic.DiagnosticType.NOTE, paramSourcePosition, paramString);
/*     */       
/*     */       return;
/*     */     } 
/* 234 */     if (paramSourcePosition == null) {
/* 235 */       this.noticeWriter.println(paramString);
/*     */     } else {
/* 237 */       this.noticeWriter.println(paramSourcePosition + ": " + paramString);
/* 238 */     }  this.noticeWriter.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(SourcePosition paramSourcePosition, String paramString, Object... paramVarArgs) {
/* 247 */     printError(paramSourcePosition, getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(SourcePosition paramSourcePosition, String paramString, Object... paramVarArgs) {
/* 256 */     printWarning(paramSourcePosition, getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notice(String paramString, Object... paramVarArgs) {
/* 265 */     printNotice(getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nerrors() {
/* 272 */     return this.nerrors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int nwarnings() {
/* 278 */     return this.nwarnings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exitNotice() {
/* 284 */     if (this.nerrors > 0) {
/* 285 */       notice((this.nerrors > 1) ? "main.errors" : "main.error", new Object[] { "" + this.nerrors });
/*     */     }
/*     */     
/* 288 */     if (this.nwarnings > 0) {
/* 289 */       notice((this.nwarnings > 1) ? "main.warnings" : "main.warning", new Object[] { "" + this.nwarnings });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exit() {
/* 300 */     throw new ExitJavadoc();
/*     */   } private void report(JCDiagnostic.DiagnosticType paramDiagnosticType, SourcePosition paramSourcePosition, String paramString) {
/*     */     Object object;
/*     */     String str;
/* 304 */     switch (paramDiagnosticType) {
/*     */       case ERROR:
/*     */       case WARNING:
/* 307 */         object = (paramSourcePosition == null) ? this.programName : paramSourcePosition;
/* 308 */         report(this.javadocDiags.create(paramDiagnosticType, null, null, "msg", new Object[] { object, paramString }));
/*     */         return;
/*     */       
/*     */       case NOTE:
/* 312 */         str = (paramSourcePosition == null) ? "msg" : "pos.msg";
/* 313 */         report(this.javadocDiags.create(paramDiagnosticType, null, null, str, new Object[] { paramSourcePosition, paramString }));
/*     */         return;
/*     */     } 
/*     */     
/* 317 */     throw new IllegalArgumentException(paramDiagnosticType.toString());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\Messager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */