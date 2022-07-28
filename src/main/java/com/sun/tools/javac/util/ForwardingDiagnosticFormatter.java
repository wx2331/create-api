/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ForwardingDiagnosticFormatter<D extends Diagnostic<?>, F extends DiagnosticFormatter<D>>
/*     */   implements DiagnosticFormatter<D>
/*     */ {
/*     */   protected F formatter;
/*     */   protected ForwardingConfiguration configuration;
/*     */
/*     */   public ForwardingDiagnosticFormatter(F paramF) {
/*  60 */     this.formatter = paramF;
/*  61 */     this.configuration = new ForwardingConfiguration(paramF.getConfiguration());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public F getDelegatedFormatter() {
/*  69 */     return this.formatter;
/*     */   }
/*     */
/*     */   public Configuration getConfiguration() {
/*  73 */     return this.configuration;
/*     */   }
/*     */
/*     */   public boolean displaySource(D paramD) {
/*  77 */     return this.formatter.displaySource((Diagnostic)paramD);
/*     */   }
/*     */
/*     */   public String format(D paramD, Locale paramLocale) {
/*  81 */     return this.formatter.format((Diagnostic)paramD, paramLocale);
/*     */   }
/*     */
/*     */   public String formatKind(D paramD, Locale paramLocale) {
/*  85 */     return this.formatter.formatKind((Diagnostic)paramD, paramLocale);
/*     */   }
/*     */
/*     */   public String formatMessage(D paramD, Locale paramLocale) {
/*  89 */     return this.formatter.formatMessage((Diagnostic)paramD, paramLocale);
/*     */   }
/*     */
/*     */   public String formatPosition(D paramD, PositionKind paramPositionKind, Locale paramLocale) {
/*  93 */     return this.formatter.formatPosition((Diagnostic)paramD, paramPositionKind, paramLocale);
/*     */   }
/*     */
/*     */   public String formatSource(D paramD, boolean paramBoolean, Locale paramLocale) {
/*  97 */     return this.formatter.formatSource((Diagnostic)paramD, paramBoolean, paramLocale);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public static class ForwardingConfiguration
/*     */     implements Configuration
/*     */   {
/*     */     protected Configuration configuration;
/*     */
/*     */
/*     */
/*     */     public ForwardingConfiguration(Configuration param1Configuration) {
/* 110 */       this.configuration = param1Configuration;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public Configuration getDelegatedConfiguration() {
/* 118 */       return this.configuration;
/*     */     }
/*     */
/*     */     public int getMultilineLimit(MultilineLimit param1MultilineLimit) {
/* 122 */       return this.configuration.getMultilineLimit(param1MultilineLimit);
/*     */     }
/*     */
/*     */     public Set<DiagnosticPart> getVisible() {
/* 126 */       return this.configuration.getVisible();
/*     */     }
/*     */
/*     */     public void setMultilineLimit(MultilineLimit param1MultilineLimit, int param1Int) {
/* 130 */       this.configuration.setMultilineLimit(param1MultilineLimit, param1Int);
/*     */     }
/*     */
/*     */     public void setVisible(Set<DiagnosticPart> param1Set) {
/* 134 */       this.configuration.setVisible(param1Set);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\ForwardingDiagnosticFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
