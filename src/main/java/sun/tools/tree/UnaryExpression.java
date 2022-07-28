/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.java.CompilerError;
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
/*     */ public class UnaryExpression
/*     */   extends Expression
/*     */ {
/*     */   Expression right;
/*     */   
/*     */   UnaryExpression(int paramInt, long paramLong, Type paramType, Expression paramExpression) {
/*  45 */     super(paramInt, paramLong, paramType);
/*  46 */     this.right = paramExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression order() {
/*  53 */     if (precedence() > this.right.precedence()) {
/*  54 */       UnaryExpression unaryExpression = (UnaryExpression)this.right;
/*  55 */       this.right = unaryExpression.right;
/*  56 */       unaryExpression.right = order();
/*  57 */       return unaryExpression;
/*     */     } 
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/*  66 */     throw new CompilerError("selectType: " + opNames[this.op]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  73 */     paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     
/*  75 */     int i = this.right.type.getTypeMask();
/*  76 */     selectType(paramEnvironment, paramContext, i);
/*  77 */     if ((i & 0x2000) == 0 && this.type.isType(13)) {
/*  78 */       paramEnvironment.error(this.where, "invalid.arg", opNames[this.op]);
/*     */     }
/*  80 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/*  87 */     switch (this.op) {
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case 38:
/*     */       case 55:
/*     */       case 56:
/*  94 */         return this.right.isConstant();
/*     */     } 
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression eval(int paramInt) {
/* 103 */     return this;
/*     */   }
/*     */   Expression eval(long paramLong) {
/* 106 */     return this;
/*     */   }
/*     */   Expression eval(float paramFloat) {
/* 109 */     return this;
/*     */   }
/*     */   Expression eval(double paramDouble) {
/* 112 */     return this;
/*     */   }
/*     */   Expression eval(boolean paramBoolean) {
/* 115 */     return this;
/*     */   }
/*     */   Expression eval(String paramString) {
/* 118 */     return this;
/*     */   }
/*     */   Expression eval() {
/* 121 */     switch (this.right.op) {
/*     */       case 62:
/*     */       case 63:
/*     */       case 64:
/*     */       case 65:
/* 126 */         return eval(((IntegerExpression)this.right).value);
/*     */       case 66:
/* 128 */         return eval(((LongExpression)this.right).value);
/*     */       case 67:
/* 130 */         return eval(((FloatExpression)this.right).value);
/*     */       case 68:
/* 132 */         return eval(((DoubleExpression)this.right).value);
/*     */       case 61:
/* 134 */         return eval(((BooleanExpression)this.right).value);
/*     */       case 69:
/* 136 */         return eval(((StringExpression)this.right).value);
/*     */     } 
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 145 */     return this.right.inline(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 148 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*     */     try {
/* 150 */       return eval().simplify();
/* 151 */     } catch (ArithmeticException arithmeticException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       return this;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 169 */     UnaryExpression unaryExpression = (UnaryExpression)clone();
/* 170 */     if (this.right != null) {
/* 171 */       unaryExpression.right = this.right.copyInline(paramContext);
/*     */     }
/* 173 */     return unaryExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 180 */     return 1 + this.right.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 187 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 188 */     this.right.print(paramPrintStream);
/* 189 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\UnaryExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */