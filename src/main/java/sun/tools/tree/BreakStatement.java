/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
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
/*     */ public class BreakStatement
/*     */   extends Statement
/*     */ {
/*     */   Identifier lbl;
/*     */   
/*     */   public BreakStatement(long paramLong, Identifier paramIdentifier) {
/*  47 */     super(98, paramLong);
/*  48 */     this.lbl = paramIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  55 */     reach(paramEnvironment, paramVset);
/*  56 */     checkLabel(paramEnvironment, paramContext);
/*  57 */     CheckContext checkContext1 = (CheckContext)(new CheckContext(paramContext, this)).getBreakContext(this.lbl);
/*  58 */     if (checkContext1 != null) {
/*  59 */       if (checkContext1.frameNumber != paramContext.frameNumber) {
/*  60 */         paramEnvironment.error(this.where, "branch.to.uplevel", this.lbl);
/*     */       }
/*  62 */       checkContext1.vsBreak = checkContext1.vsBreak.join(paramVset);
/*     */     }
/*  64 */     else if (this.lbl != null) {
/*  65 */       paramEnvironment.error(this.where, "label.not.found", this.lbl);
/*     */     } else {
/*  67 */       paramEnvironment.error(this.where, "invalid.break");
/*     */     } 
/*     */     
/*  70 */     CheckContext checkContext2 = paramContext.getTryExitContext();
/*  71 */     if (checkContext2 != null) {
/*  72 */       checkContext2.vsTryExit = checkContext2.vsTryExit.join(paramVset);
/*     */     }
/*  74 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/*  81 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  88 */     CodeContext codeContext1 = new CodeContext(paramContext, this);
/*  89 */     CodeContext codeContext2 = (CodeContext)codeContext1.getBreakContext(this.lbl);
/*  90 */     codeFinally(paramEnvironment, paramContext, paramAssembler, codeContext2, (Type)null);
/*  91 */     paramAssembler.add(this.where, 167, codeContext2.breakLabel);
/*  92 */     paramAssembler.add((Instruction)codeContext1.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/*  99 */     super.print(paramPrintStream, paramInt);
/* 100 */     paramPrintStream.print("break");
/* 101 */     if (this.lbl != null) {
/* 102 */       paramPrintStream.print(" " + this.lbl);
/*     */     }
/* 104 */     paramPrintStream.print(";");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BreakStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */