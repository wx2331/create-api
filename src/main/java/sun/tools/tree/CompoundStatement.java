/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.java.Environment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompoundStatement
/*     */   extends Statement
/*     */ {
/*     */   Statement[] args;
/*     */   
/*     */   public CompoundStatement(long paramLong, Statement[] paramArrayOfStatement) {
/*  47 */     super(105, paramLong);
/*  48 */     this.args = paramArrayOfStatement;
/*     */     
/*  50 */     for (byte b = 0; b < paramArrayOfStatement.length; b++) {
/*  51 */       if (paramArrayOfStatement[b] == null) {
/*  52 */         paramArrayOfStatement[b] = new CompoundStatement(paramLong, new Statement[0]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertStatement(Statement paramStatement) {
/*  62 */     Statement[] arrayOfStatement = new Statement[1 + this.args.length];
/*  63 */     arrayOfStatement[0] = paramStatement;
/*  64 */     for (byte b = 0; b < this.args.length; b++) {
/*  65 */       arrayOfStatement[b + 1] = this.args[b];
/*     */     }
/*  67 */     this.args = arrayOfStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  74 */     checkLabel(paramEnvironment, paramContext);
/*  75 */     if (this.args.length > 0) {
/*  76 */       paramVset = reach(paramEnvironment, paramVset);
/*  77 */       CheckContext checkContext = new CheckContext(paramContext, this);
/*     */       
/*  79 */       Environment environment = Context.newEnvironment(paramEnvironment, checkContext);
/*  80 */       for (byte b = 0; b < this.args.length; b++) {
/*  81 */         paramVset = this.args[b].checkBlockStatement(environment, checkContext, paramVset, paramHashtable);
/*     */       }
/*  83 */       paramVset = paramVset.join(checkContext.vsBreak);
/*     */     } 
/*  85 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/*  92 */     paramContext = new Context(paramContext, this);
/*  93 */     boolean bool = false;
/*  94 */     int i = 0; int j;
/*  95 */     for (j = 0; j < this.args.length; j++) {
/*  96 */       Statement statement = this.args[j];
/*  97 */       if (statement != null) {
/*  98 */         if ((statement = statement.inline(paramEnvironment, paramContext)) != null) {
/*  99 */           if (statement.op == 105 && statement.labels == null) {
/* 100 */             i += ((CompoundStatement)statement).args.length;
/*     */           } else {
/* 102 */             i++;
/*     */           } 
/* 104 */           bool = true;
/*     */         } 
/* 106 */         this.args[j] = statement;
/*     */       } 
/*     */     } 
/* 109 */     switch (i) {
/*     */       case 0:
/* 111 */         return null;
/*     */       
/*     */       case 1:
/* 114 */         for (j = this.args.length; j-- > 0;) {
/* 115 */           if (this.args[j] != null) {
/* 116 */             return eliminate(paramEnvironment, this.args[j]);
/*     */           }
/*     */         } 
/*     */         break;
/*     */     } 
/* 121 */     if (bool || i != this.args.length) {
/* 122 */       Statement[] arrayOfStatement = new Statement[i];
/* 123 */       for (int k = this.args.length; k-- > 0; ) {
/* 124 */         Statement statement = this.args[k];
/* 125 */         if (statement != null) {
/* 126 */           if (statement.op == 105 && statement.labels == null) {
/* 127 */             Statement[] arrayOfStatement1 = ((CompoundStatement)statement).args;
/* 128 */             for (int m = arrayOfStatement1.length; m-- > 0;)
/* 129 */               arrayOfStatement[--i] = arrayOfStatement1[m]; 
/*     */             continue;
/*     */           } 
/* 132 */           arrayOfStatement[--i] = statement;
/*     */         } 
/*     */       } 
/*     */       
/* 136 */       this.args = arrayOfStatement;
/*     */     } 
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 145 */     CompoundStatement compoundStatement = (CompoundStatement)clone();
/* 146 */     compoundStatement.args = new Statement[this.args.length];
/* 147 */     for (byte b = 0; b < this.args.length; b++) {
/* 148 */       compoundStatement.args[b] = this.args[b].copyInline(paramContext, paramBoolean);
/*     */     }
/* 150 */     return compoundStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 157 */     int i = 0;
/* 158 */     for (byte b = 0; b < this.args.length && i < paramInt; b++) {
/* 159 */       i += this.args[b].costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 161 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 168 */     CodeContext codeContext = new CodeContext(paramContext, this);
/* 169 */     for (byte b = 0; b < this.args.length; b++) {
/* 170 */       this.args[b].code(paramEnvironment, codeContext, paramAssembler);
/*     */     }
/* 172 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression firstConstructor() {
/* 179 */     return (this.args.length > 0) ? this.args[0].firstConstructor() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 186 */     super.print(paramPrintStream, paramInt);
/* 187 */     paramPrintStream.print("{\n");
/* 188 */     for (byte b = 0; b < this.args.length; b++) {
/* 189 */       printIndent(paramPrintStream, paramInt + 1);
/* 190 */       if (this.args[b] != null) {
/* 191 */         this.args[b].print(paramPrintStream, paramInt + 1);
/*     */       } else {
/* 193 */         paramPrintStream.print("<empty>");
/*     */       } 
/* 195 */       paramPrintStream.print("\n");
/*     */     } 
/* 197 */     printIndent(paramPrintStream, paramInt);
/* 198 */     paramPrintStream.print("}");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\CompoundStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */