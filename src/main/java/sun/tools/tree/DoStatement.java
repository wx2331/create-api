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
/*     */ public class DoStatement
/*     */   extends Statement
/*     */ {
/*     */   Statement body;
/*     */   Expression cond;
/*     */   
/*     */   public DoStatement(long paramLong, Statement paramStatement, Expression paramExpression) {
/*  48 */     super(94, paramLong);
/*  49 */     this.body = paramStatement;
/*  50 */     this.cond = paramExpression;
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
/*  61 */     paramVset = this.body.check(paramEnvironment, checkContext, reach(paramEnvironment, paramVset), paramHashtable);
/*  62 */     paramVset = paramVset.join(checkContext.vsContinue);
/*     */ 
/*     */ 
/*     */     
/*  66 */     ConditionVars conditionVars = this.cond.checkCondition(paramEnvironment, checkContext, paramVset, paramHashtable);
/*  67 */     this.cond = convert(paramEnvironment, checkContext, Type.tBoolean, this.cond);
/*     */     
/*  69 */     paramContext.checkBackBranch(paramEnvironment, this, vset, conditionVars.vsTrue);
/*     */     
/*  71 */     paramVset = checkContext.vsBreak.join(conditionVars.vsFalse);
/*  72 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/*  79 */     paramContext = new Context(paramContext, this);
/*  80 */     if (this.body != null) {
/*  81 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/*  83 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/*  91 */     DoStatement doStatement = (DoStatement)clone();
/*  92 */     doStatement.cond = this.cond.copyInline(paramContext);
/*  93 */     if (this.body != null) {
/*  94 */       doStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/*  96 */     return doStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 103 */     return 1 + this.cond.costInline(paramInt, paramEnvironment, paramContext) + ((this.body != null) ? this.body
/* 104 */       .costInline(paramInt, paramEnvironment, paramContext) : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 111 */     Label label = new Label();
/* 112 */     paramAssembler.add((Instruction)label);
/*     */     
/* 114 */     CodeContext codeContext = new CodeContext(paramContext, this);
/*     */     
/* 116 */     if (this.body != null) {
/* 117 */       this.body.code(paramEnvironment, codeContext, paramAssembler);
/*     */     }
/* 119 */     paramAssembler.add((Instruction)codeContext.contLabel);
/* 120 */     this.cond.codeBranch(paramEnvironment, codeContext, paramAssembler, label, true);
/* 121 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 128 */     super.print(paramPrintStream, paramInt);
/* 129 */     paramPrintStream.print("do ");
/* 130 */     this.body.print(paramPrintStream, paramInt);
/* 131 */     paramPrintStream.print(" while ");
/* 132 */     this.cond.print(paramPrintStream);
/* 133 */     paramPrintStream.print(";");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\DoStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */