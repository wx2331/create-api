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
/*     */ public class IfStatement
/*     */   extends Statement
/*     */ {
/*     */   Expression cond;
/*     */   Statement ifTrue;
/*     */   Statement ifFalse;
/*     */   
/*     */   public IfStatement(long paramLong, Expression paramExpression, Statement paramStatement1, Statement paramStatement2) {
/*  49 */     super(90, paramLong);
/*  50 */     this.cond = paramExpression;
/*  51 */     this.ifTrue = paramStatement1;
/*  52 */     this.ifFalse = paramStatement2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  59 */     checkLabel(paramEnvironment, paramContext);
/*  60 */     CheckContext checkContext = new CheckContext(paramContext, this);
/*     */ 
/*     */     
/*  63 */     ConditionVars conditionVars = this.cond.checkCondition(paramEnvironment, checkContext, reach(paramEnvironment, paramVset), paramHashtable);
/*  64 */     this.cond = convert(paramEnvironment, checkContext, Type.tBoolean, this.cond);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     Vset vset1 = conditionVars.vsTrue.clearDeadEnd();
/*  88 */     Vset vset2 = conditionVars.vsFalse.clearDeadEnd();
/*  89 */     vset1 = this.ifTrue.check(paramEnvironment, checkContext, vset1, paramHashtable);
/*  90 */     if (this.ifFalse != null)
/*  91 */       vset2 = this.ifFalse.check(paramEnvironment, checkContext, vset2, paramHashtable); 
/*  92 */     paramVset = vset1.join(vset2.join(checkContext.vsBreak));
/*  93 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 100 */     paramContext = new Context(paramContext, this);
/* 101 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     if (this.ifTrue != null) {
/* 117 */       this.ifTrue = this.ifTrue.inline(paramEnvironment, paramContext);
/*     */     }
/* 119 */     if (this.ifFalse != null) {
/* 120 */       this.ifFalse = this.ifFalse.inline(paramEnvironment, paramContext);
/*     */     }
/* 122 */     if (this.cond.equals(true)) {
/* 123 */       return eliminate(paramEnvironment, this.ifTrue);
/*     */     }
/* 125 */     if (this.cond.equals(false)) {
/* 126 */       return eliminate(paramEnvironment, this.ifFalse);
/*     */     }
/* 128 */     if (this.ifTrue == null && this.ifFalse == null) {
/* 129 */       return eliminate(paramEnvironment, (new ExpressionStatement(this.where, this.cond)).inline(paramEnvironment, paramContext));
/*     */     }
/* 131 */     if (this.ifTrue == null) {
/* 132 */       this.cond = (new NotExpression(this.cond.where, this.cond)).inlineValue(paramEnvironment, paramContext);
/* 133 */       return eliminate(paramEnvironment, new IfStatement(this.where, this.cond, this.ifFalse, null));
/*     */     } 
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 142 */     IfStatement ifStatement = (IfStatement)clone();
/* 143 */     ifStatement.cond = this.cond.copyInline(paramContext);
/* 144 */     if (this.ifTrue != null) {
/* 145 */       ifStatement.ifTrue = this.ifTrue.copyInline(paramContext, paramBoolean);
/*     */     }
/* 147 */     if (this.ifFalse != null) {
/* 148 */       ifStatement.ifFalse = this.ifFalse.copyInline(paramContext, paramBoolean);
/*     */     }
/* 150 */     return ifStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 157 */     int i = 1 + this.cond.costInline(paramInt, paramEnvironment, paramContext);
/* 158 */     if (this.ifTrue != null) {
/* 159 */       i += this.ifTrue.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 161 */     if (this.ifFalse != null) {
/* 162 */       i += this.ifFalse.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 164 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 171 */     CodeContext codeContext = new CodeContext(paramContext, this);
/*     */     
/* 173 */     Label label = new Label();
/* 174 */     this.cond.codeBranch(paramEnvironment, codeContext, paramAssembler, label, false);
/* 175 */     this.ifTrue.code(paramEnvironment, codeContext, paramAssembler);
/* 176 */     if (this.ifFalse != null) {
/* 177 */       Label label1 = new Label();
/* 178 */       paramAssembler.add(true, this.where, 167, label1);
/* 179 */       paramAssembler.add((Instruction)label);
/* 180 */       this.ifFalse.code(paramEnvironment, codeContext, paramAssembler);
/* 181 */       paramAssembler.add((Instruction)label1);
/*     */     } else {
/* 183 */       paramAssembler.add((Instruction)label);
/*     */     } 
/*     */     
/* 186 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 193 */     super.print(paramPrintStream, paramInt);
/* 194 */     paramPrintStream.print("if ");
/* 195 */     this.cond.print(paramPrintStream);
/* 196 */     paramPrintStream.print(" ");
/* 197 */     this.ifTrue.print(paramPrintStream, paramInt);
/* 198 */     if (this.ifFalse != null) {
/* 199 */       paramPrintStream.print(" else ");
/* 200 */       this.ifFalse.print(paramPrintStream, paramInt);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\IfStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */