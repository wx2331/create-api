/*     */ package com.sun.tools.javah;
/*     */
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.DiagnosticListener;
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
/*     */
/*     */
/*     */ public class Util
/*     */ {
/*     */   public static class Exit
/*     */     extends Error
/*     */   {
/*     */     private static final long serialVersionUID = 430820978114067221L;
/*     */     public final int exitValue;
/*     */     public final Throwable cause;
/*     */
/*     */     Exit(int param1Int) {
/*  58 */       this(param1Int, null);
/*     */     }
/*     */
/*     */     Exit(int param1Int, Throwable param1Throwable) {
/*  62 */       super(param1Throwable);
/*  63 */       this.exitValue = param1Int;
/*  64 */       this.cause = param1Throwable;
/*     */     }
/*     */
/*     */     Exit(Exit param1Exit) {
/*  68 */       this(param1Exit.exitValue, param1Exit.cause);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public boolean verbose = false;
/*     */
/*     */
/*     */   public PrintWriter log;
/*     */
/*     */   public DiagnosticListener<? super JavaFileObject> dl;
/*     */
/*     */   private ResourceBundle m;
/*     */
/*     */
/*     */   Util(PrintWriter paramPrintWriter, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener) {
/*  84 */     this.log = paramPrintWriter;
/*  85 */     this.dl = paramDiagnosticListener;
/*     */   }
/*     */
/*     */   public void log(String paramString) {
/*  89 */     this.log.println(paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void initMessages() throws Exit {
/*     */     try {
/* 100 */       this.m = ResourceBundle.getBundle("com.sun.tools.javah.resources.l10n");
/* 101 */     } catch (MissingResourceException missingResourceException) {
/* 102 */       fatal("Error loading resources.  Please file a bug report.", missingResourceException);
/*     */     }
/*     */   }
/*     */
/*     */   private String getText(String paramString, Object... paramVarArgs) throws Exit {
/* 107 */     if (this.m == null)
/* 108 */       initMessages();
/*     */     try {
/* 110 */       return MessageFormat.format(this.m.getString(paramString), paramVarArgs);
/* 111 */     } catch (MissingResourceException missingResourceException) {
/* 112 */       fatal("Key " + paramString + " not found in resources.", missingResourceException);
/*     */
/* 114 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void usage() throws Exit {
/* 121 */     this.log.println(getText("usage", new Object[0]));
/*     */   }
/*     */
/*     */   public void version() throws Exit {
/* 125 */     this.log.println(getText("javah.version", new Object[] {
/* 126 */             System.getProperty("java.version"), null
/*     */           }));
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void bug(String paramString) throws Exit {
/* 133 */     bug(paramString, null);
/*     */   }
/*     */
/*     */   public void bug(String paramString, Exception paramException) throws Exit {
/* 137 */     this.dl.report(createDiagnostic(Diagnostic.Kind.ERROR, paramString, new Object[0]));
/* 138 */     this.dl.report(createDiagnostic(Diagnostic.Kind.NOTE, "bug.report", new Object[0]));
/* 139 */     throw new Exit(11, paramException);
/*     */   }
/*     */
/*     */   public void error(String paramString, Object... paramVarArgs) throws Exit {
/* 143 */     this.dl.report(createDiagnostic(Diagnostic.Kind.ERROR, paramString, paramVarArgs));
/* 144 */     throw new Exit(15);
/*     */   }
/*     */
/*     */   private void fatal(String paramString, Exception paramException) throws Exit {
/* 148 */     this.dl.report(createDiagnostic(Diagnostic.Kind.ERROR, "", new Object[] { paramString }));
/* 149 */     throw new Exit(10, paramException);
/*     */   }
/*     */
/*     */
/*     */   private Diagnostic<JavaFileObject> createDiagnostic(final Diagnostic.Kind kind, final String code, Object... args) {
/* 154 */     return new Diagnostic<JavaFileObject>() {
/*     */         public String getCode() {
/* 156 */           return code;
/*     */         }
/*     */         public long getColumnNumber() {
/* 159 */           return -1L;
/*     */         }
/*     */         public long getEndPosition() {
/* 162 */           return -1L;
/*     */         }
/*     */         public Kind getKind() {
/* 165 */           return kind;
/*     */         }
/*     */         public long getLineNumber() {
/* 168 */           return -1L;
/*     */         }
/*     */         public String getMessage(Locale param1Locale) {
/* 171 */           if (code.length() == 0)
/* 172 */             return (String)args[0];
/* 173 */           return Util.this.getText(code, args);
/*     */         }
/*     */         public long getPosition() {
/* 176 */           return -1L;
/*     */         }
/*     */         public JavaFileObject getSource() {
/* 179 */           return null;
/*     */         }
/*     */         public long getStartPosition() {
/* 182 */           return -1L;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
