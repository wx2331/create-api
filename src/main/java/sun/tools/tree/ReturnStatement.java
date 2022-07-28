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
/*     */ 
/*     */ public class ReturnStatement
/*     */   extends Statement
/*     */ {
/*     */   Expression expr;
/*     */   
/*     */   public ReturnStatement(long paramLong, Expression paramExpression) {
/*  47 */     super(100, paramLong);
/*  48 */     this.expr = paramExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  55 */     checkLabel(paramEnvironment, paramContext);
/*  56 */     paramVset = reach(paramEnvironment, paramVset);
/*  57 */     if (this.expr != null) {
/*  58 */       paramVset = this.expr.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     }
/*     */ 
/*     */     
/*  62 */     if (paramContext.field.isInitializer()) {
/*  63 */       paramEnvironment.error(this.where, "return.inside.static.initializer");
/*  64 */       return DEAD_END;
/*     */     } 
/*     */     
/*  67 */     if (paramContext.field.getType().getReturnType().isType(11)) {
/*  68 */       if (this.expr != null) {
/*  69 */         if (paramContext.field.isConstructor()) {
/*  70 */           paramEnvironment.error(this.where, "return.with.value.constr", paramContext.field);
/*     */         } else {
/*  72 */           paramEnvironment.error(this.where, "return.with.value", paramContext.field);
/*     */         } 
/*  74 */         this.expr = null;
/*     */       }
/*     */     
/*  77 */     } else if (this.expr == null) {
/*  78 */       paramEnvironment.error(this.where, "return.without.value", paramContext.field);
/*     */     } else {
/*  80 */       this.expr = convert(paramEnvironment, paramContext, paramContext.field.getType().getReturnType(), this.expr);
/*     */     } 
/*     */     
/*  83 */     CheckContext checkContext1 = paramContext.getReturnContext();
/*  84 */     if (checkContext1 != null) {
/*  85 */       checkContext1.vsBreak = checkContext1.vsBreak.join(paramVset);
/*     */     }
/*  87 */     CheckContext checkContext2 = paramContext.getTryExitContext();
/*  88 */     if (checkContext2 != null) {
/*  89 */       checkContext2.vsTryExit = checkContext2.vsTryExit.join(paramVset);
/*     */     }
/*  91 */     if (this.expr != null) {
/*     */ 
/*     */       
/*  94 */       Node node = null;
/*  95 */       for (Context context = paramContext; context != null; context = context.prev) {
/*  96 */         if (context.node != null) {
/*     */ 
/*     */           
/*  99 */           if (context.node.op == 47) {
/*     */             break;
/*     */           }
/*     */           
/* 103 */           if (context.node.op == 126) {
/* 104 */             node = context.node; break;
/*     */           } 
/* 106 */           if (context.node.op == 103 && ((CheckContext)context).vsContinue != null)
/*     */           {
/* 108 */             node = context.node; } 
/*     */         } 
/*     */       } 
/* 111 */       if (node != null) {
/* 112 */         if (node.op == 103) {
/* 113 */           ((FinallyStatement)node).needReturnSlot = true;
/*     */         } else {
/* 115 */           ((SynchronizedStatement)node).needReturnSlot = true;
/*     */         } 
/*     */       }
/*     */     } 
/* 119 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 127 */     if (this.expr != null) {
/* 128 */       this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/*     */     }
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 137 */     return 1 + ((this.expr != null) ? this.expr.costInline(paramInt, paramEnvironment, paramContext) : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 144 */     Expression expression = (this.expr != null) ? this.expr.copyInline(paramContext) : null;
/* 145 */     if (!paramBoolean && expression != null) {
/* 146 */       Statement[] arrayOfStatement = { new ExpressionStatement(this.where, expression), new InlineReturnStatement(this.where, null) };
/*     */ 
/*     */ 
/*     */       
/* 150 */       return new CompoundStatement(this.where, arrayOfStatement);
/*     */     } 
/* 152 */     return new InlineReturnStatement(this.where, expression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 159 */     if (this.expr == null) {
/* 160 */       codeFinally(paramEnvironment, paramContext, paramAssembler, (Context)null, (Type)null);
/* 161 */       paramAssembler.add(this.where, 177);
/*     */     } else {
/* 163 */       this.expr.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 164 */       codeFinally(paramEnvironment, paramContext, paramAssembler, (Context)null, this.expr.type);
/* 165 */       paramAssembler.add(this.where, 172 + this.expr.type.getTypeCodeOffset());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 173 */     super.print(paramPrintStream, paramInt);
/* 174 */     paramPrintStream.print("return");
/* 175 */     if (this.expr != null) {
/* 176 */       paramPrintStream.print(" ");
/* 177 */       this.expr.print(paramPrintStream);
/*     */     } 
/* 179 */     paramPrintStream.print(";");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ReturnStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */