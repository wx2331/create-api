/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DiagnosticFormatter<D extends javax.tools.Diagnostic<?>>
/*     */ {
/*     */   boolean displaySource(D paramD);
/*     */   
/*     */   String format(D paramD, Locale paramLocale);
/*     */   
/*     */   String formatMessage(D paramD, Locale paramLocale);
/*     */   
/*     */   String formatKind(D paramD, Locale paramLocale);
/*     */   
/*     */   String formatSource(D paramD, boolean paramBoolean, Locale paramLocale);
/*     */   
/*     */   String formatPosition(D paramD, PositionKind paramPositionKind, Locale paramLocale);
/*     */   
/*     */   Configuration getConfiguration();
/*     */   
/*     */   public enum PositionKind
/*     */   {
/* 107 */     START,
/*     */ 
/*     */ 
/*     */     
/* 111 */     END,
/*     */ 
/*     */ 
/*     */     
/* 115 */     LINE,
/*     */ 
/*     */ 
/*     */     
/* 119 */     COLUMN,
/*     */ 
/*     */ 
/*     */     
/* 123 */     OFFSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Configuration
/*     */   {
/*     */     void setVisible(Set<DiagnosticPart> param1Set);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Set<DiagnosticPart> getVisible();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setMultilineLimit(MultilineLimit param1MultilineLimit, int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getMultilineLimit(MultilineLimit param1MultilineLimit);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public enum DiagnosticPart
/*     */     {
/* 162 */       SUMMARY,
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       DETAILS,
/*     */ 
/*     */ 
/*     */       
/* 171 */       SOURCE,
/*     */ 
/*     */ 
/*     */       
/* 175 */       SUBDIAGNOSTICS,
/*     */ 
/*     */ 
/*     */       
/* 179 */       JLS;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public enum MultilineLimit
/*     */     {
/* 210 */       DEPTH,
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 215 */       LENGTH;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\DiagnosticFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */