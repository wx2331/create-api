/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
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
/*     */ public class WhileStatement
/*     */   extends Statement
/*     */ {
/*     */   Expression cond;
/*     */   Statement body;
/*     */   
/*     */   public WhileStatement(long paramLong, Expression paramExpression, Statement paramStatement) {
/*  48 */     super(93, paramLong);
/*  49 */     this.cond = paramExpression;
/*  50 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  57 */     checkLabel(paramEnvironment, paramContext);
/*  58 */     CheckContext checkContext = new CheckContext(paramContext, this);
/*     */     
/*  60 */     Vset vset = paramVset.copy();
/*     */ 
/*     */ 
/*     */     
/*  64 */     ConditionVars conditionVars = this.cond.checkCondition(paramEnvironment, checkContext, reach(paramEnvironment, paramVset), paramHashtable);
/*  65 */     this.cond = convert(paramEnvironment, checkContext, Type.tBoolean, this.cond);
/*     */     
/*  67 */     paramVset = this.body.check(paramEnvironment, checkContext, conditionVars.vsTrue, paramHashtable);
/*  68 */     paramVset = paramVset.join(checkContext.vsContinue);
/*     */     
/*  70 */     paramContext.checkBackBranch(paramEnvironment, this, vset, paramVset);
/*     */     
/*  72 */     paramVset = checkContext.vsBreak.join(conditionVars.vsFalse);
/*  73 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/*  80 */     paramContext = new Context(paramContext, this);
/*  81 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/*  82 */     if (this.body != null) {
/*  83 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/*  92 */     return 1 + this.cond.costInline(paramInt, paramEnvironment, paramContext) + ((this.body != null) ? this.body
/*  93 */       .costInline(paramInt, paramEnvironment, paramContext) : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 100 */     WhileStatement whileStatement = (WhileStatement)clone();
/* 101 */     whileStatement.cond = this.cond.copyInline(paramContext);
/* 102 */     if (this.body != null) {
/* 103 */       whileStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 105 */     return whileStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 112 */     CodeContext codeContext = new CodeContext(paramContext, this);
/*     */     
/* 114 */     paramAssembler.add(this.where, 167, codeContext.contLabel);
/*     */     
/* 116 */     Label label = new Label();
/* 117 */     paramAssembler.add((Instruction)label);
/*     */     
/* 119 */     if (this.body != null) {
/* 120 */       this.body.code(paramEnvironment, codeContext, paramAssembler);
/*     */     }
/*     */     
/* 123 */     paramAssembler.add((Instruction)codeContext.contLabel);
/* 124 */     this.cond.codeBranch(paramEnvironment, codeContext, paramAssembler, label, true);
/* 125 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 132 */     super.print(paramPrintStream, paramInt);
/* 133 */     paramPrintStream.print("while ");
/* 134 */     this.cond.print(paramPrintStream);
/* 135 */     if (this.body != null) {
/* 136 */       paramPrintStream.print(" ");
/* 137 */       this.body.print(paramPrintStream, paramInt);
/*     */     } else {
/* 139 */       paramPrintStream.print(";");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\WhileStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */