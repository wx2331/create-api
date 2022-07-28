/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
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
/*     */ 
/*     */ public class DeclarationStatement
/*     */   extends Statement
/*     */ {
/*     */   int mod;
/*     */   Expression type;
/*     */   Statement[] args;
/*     */   
/*     */   public DeclarationStatement(long paramLong, int paramInt, Expression paramExpression, Statement[] paramArrayOfStatement) {
/*  48 */     super(107, paramLong);
/*  49 */     this.mod = paramInt;
/*  50 */     this.type = paramExpression;
/*  51 */     this.args = paramArrayOfStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  59 */     paramEnvironment.error(this.where, "invalid.decl");
/*  60 */     return checkBlockStatement(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */   Vset checkBlockStatement(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  63 */     if (this.labels != null) {
/*  64 */       paramEnvironment.error(this.where, "declaration.with.label", this.labels[0]);
/*     */     }
/*  66 */     paramVset = reach(paramEnvironment, paramVset);
/*  67 */     Type type = this.type.toType(paramEnvironment, paramContext);
/*     */     
/*  69 */     for (byte b = 0; b < this.args.length; b++) {
/*  70 */       paramVset = this.args[b].checkDeclaration(paramEnvironment, paramContext, paramVset, this.mod, type, paramHashtable);
/*     */     }
/*     */     
/*  73 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/*  80 */     byte b1 = 0;
/*  81 */     for (byte b2 = 0; b2 < this.args.length; b2++) {
/*  82 */       this.args[b2] = this.args[b2].inline(paramEnvironment, paramContext); if (this.args[b2].inline(paramEnvironment, paramContext) != null) {
/*  83 */         b1++;
/*     */       }
/*     */     } 
/*  86 */     return (b1 == 0) ? null : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/*  93 */     DeclarationStatement declarationStatement = (DeclarationStatement)clone();
/*  94 */     if (this.type != null) {
/*  95 */       declarationStatement.type = this.type.copyInline(paramContext);
/*     */     }
/*  97 */     declarationStatement.args = new Statement[this.args.length];
/*  98 */     for (byte b = 0; b < this.args.length; b++) {
/*  99 */       if (this.args[b] != null) {
/* 100 */         declarationStatement.args[b] = this.args[b].copyInline(paramContext, paramBoolean);
/*     */       }
/*     */     } 
/* 103 */     return declarationStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 110 */     int i = 1;
/* 111 */     for (byte b = 0; b < this.args.length; b++) {
/* 112 */       if (this.args[b] != null) {
/* 113 */         i += this.args[b].costInline(paramInt, paramEnvironment, paramContext);
/*     */       }
/*     */     } 
/* 116 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 124 */     for (byte b = 0; b < this.args.length; b++) {
/* 125 */       if (this.args[b] != null) {
/* 126 */         this.args[b].code(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 135 */     paramPrintStream.print("declare ");
/* 136 */     super.print(paramPrintStream, paramInt);
/* 137 */     this.type.print(paramPrintStream);
/* 138 */     paramPrintStream.print(" ");
/* 139 */     for (byte b = 0; b < this.args.length; b++) {
/* 140 */       if (b > 0) {
/* 141 */         paramPrintStream.print(", ");
/*     */       }
/* 143 */       if (this.args[b] != null) {
/* 144 */         this.args[b].print(paramPrintStream);
/*     */       } else {
/* 146 */         paramPrintStream.print("<empty>");
/*     */       } 
/*     */     } 
/* 149 */     paramPrintStream.print(";");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\DeclarationStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */