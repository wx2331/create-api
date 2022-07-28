/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferredLintHandler
/*     */ {
/*  46 */   protected static final Context.Key<DeferredLintHandler> deferredLintHandlerKey = new Context.Key();
/*     */   private JCDiagnostic.DiagnosticPosition currentPos;
/*     */   
/*     */   public static DeferredLintHandler instance(Context paramContext) {
/*  50 */     DeferredLintHandler deferredLintHandler = (DeferredLintHandler)paramContext.get(deferredLintHandlerKey);
/*  51 */     if (deferredLintHandler == null)
/*  52 */       deferredLintHandler = new DeferredLintHandler(paramContext); 
/*  53 */     return deferredLintHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<JCDiagnostic.DiagnosticPosition, ListBuffer<LintLogger>> loggersQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DeferredLintHandler(Context paramContext) {
/*  70 */     this.loggersQueue = new HashMap<>();
/*     */     paramContext.put(deferredLintHandlerKey, this);
/*     */     this.currentPos = IMMEDIATE_POSITION;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void report(LintLogger paramLintLogger) {
/*  79 */     if (this.currentPos == IMMEDIATE_POSITION) {
/*  80 */       paramLintLogger.report();
/*     */     } else {
/*  82 */       ListBuffer<LintLogger> listBuffer = this.loggersQueue.get(this.currentPos);
/*  83 */       if (listBuffer == null) {
/*  84 */         this.loggersQueue.put(this.currentPos, listBuffer = new ListBuffer());
/*     */       }
/*  86 */       listBuffer.append(paramLintLogger);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/*  93 */     ListBuffer listBuffer = this.loggersQueue.get(paramDiagnosticPosition);
/*  94 */     if (listBuffer != null) {
/*  95 */       for (LintLogger lintLogger : listBuffer) {
/*  96 */         lintLogger.report();
/*     */       }
/*  98 */       this.loggersQueue.remove(paramDiagnosticPosition);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCDiagnostic.DiagnosticPosition setPos(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 107 */     JCDiagnostic.DiagnosticPosition diagnosticPosition = this.currentPos;
/* 108 */     this.currentPos = paramDiagnosticPosition;
/* 109 */     return diagnosticPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCDiagnostic.DiagnosticPosition immediate() {
/* 116 */     return setPos(IMMEDIATE_POSITION);
/*     */   }
/*     */   
/* 119 */   private static final JCDiagnostic.DiagnosticPosition IMMEDIATE_POSITION = new JCDiagnostic.DiagnosticPosition()
/*     */     {
/*     */       public JCTree getTree() {
/* 122 */         Assert.error();
/* 123 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getStartPosition() {
/* 128 */         Assert.error();
/* 129 */         return -1;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getPreferredPosition() {
/* 134 */         Assert.error();
/* 135 */         return -1;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getEndPosition(EndPosTable param1EndPosTable) {
/* 140 */         Assert.error();
/* 141 */         return -1;
/*     */       }
/*     */     };
/*     */   
/*     */   public static interface LintLogger {
/*     */     void report();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\DeferredLintHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */