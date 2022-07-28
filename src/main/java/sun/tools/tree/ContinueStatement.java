/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContinueStatement
/*     */   extends Statement
/*     */ {
/*     */   Identifier lbl;
/*     */   
/*     */   public ContinueStatement(long paramLong, Identifier paramIdentifier) {
/*  47 */     super(99, paramLong);
/*  48 */     this.lbl = paramIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  56 */     checkLabel(paramEnvironment, paramContext);
/*  57 */     reach(paramEnvironment, paramVset);
/*     */ 
/*     */ 
/*     */     
/*  61 */     CheckContext checkContext1 = (CheckContext)(new CheckContext(paramContext, this)).getContinueContext(this.lbl);
/*  62 */     if (checkContext1 != null) {
/*  63 */       switch (checkContext1.node.op) {
/*     */         case 92:
/*     */         case 93:
/*     */         case 94:
/*  67 */           if (checkContext1.frameNumber != paramContext.frameNumber) {
/*  68 */             paramEnvironment.error(this.where, "branch.to.uplevel", this.lbl);
/*     */           }
/*  70 */           checkContext1.vsContinue = checkContext1.vsContinue.join(paramVset);
/*     */           break;
/*     */         default:
/*  73 */           paramEnvironment.error(this.where, "invalid.continue");
/*     */           break;
/*     */       } 
/*  76 */     } else if (this.lbl != null) {
/*  77 */       paramEnvironment.error(this.where, "label.not.found", this.lbl);
/*     */     } else {
/*  79 */       paramEnvironment.error(this.where, "invalid.continue");
/*     */     } 
/*     */     
/*  82 */     CheckContext checkContext2 = paramContext.getTryExitContext();
/*  83 */     if (checkContext2 != null) {
/*  84 */       checkContext2.vsTryExit = checkContext2.vsTryExit.join(paramVset);
/*     */     }
/*  86 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/*  93 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 100 */     CodeContext codeContext = (CodeContext)paramContext.getContinueContext(this.lbl);
/* 101 */     codeFinally(paramEnvironment, paramContext, paramAssembler, codeContext, (Type)null);
/* 102 */     paramAssembler.add(this.where, 167, codeContext.contLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 109 */     super.print(paramPrintStream, paramInt);
/* 110 */     paramPrintStream.print("continue");
/* 111 */     if (this.lbl != null) {
/* 112 */       paramPrintStream.print(" " + this.lbl);
/*     */     }
/* 114 */     paramPrintStream.print(";");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ContinueStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */