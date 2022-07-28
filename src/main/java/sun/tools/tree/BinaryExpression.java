/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.Label;
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
/*     */ public class BinaryExpression
/*     */   extends UnaryExpression
/*     */ {
/*     */   Expression left;
/*     */   
/*     */   BinaryExpression(int paramInt, long paramLong, Type paramType, Expression paramExpression1, Expression paramExpression2) {
/*  47 */     super(paramInt, paramLong, paramType, paramExpression2);
/*  48 */     this.left = paramExpression1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression order() {
/*  55 */     if (precedence() > this.left.precedence()) {
/*  56 */       UnaryExpression unaryExpression = (UnaryExpression)this.left;
/*  57 */       this.left = unaryExpression.right;
/*  58 */       unaryExpression.right = order();
/*  59 */       return unaryExpression;
/*     */     } 
/*  61 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  68 */     paramVset = this.left.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  69 */     paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     
/*  71 */     int i = this.left.type.getTypeMask() | this.right.type.getTypeMask();
/*  72 */     if ((i & 0x2000) != 0) {
/*  73 */       return paramVset;
/*     */     }
/*  75 */     selectType(paramEnvironment, paramContext, i);
/*     */     
/*  77 */     if (this.type.isType(13)) {
/*  78 */       paramEnvironment.error(this.where, "invalid.args", opNames[this.op]);
/*     */     }
/*  80 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/*  87 */     switch (this.op) {
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 26:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/*     */       case 33:
/* 107 */         return (this.left.isConstant() && this.right.isConstant());
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Expression eval(int paramInt1, int paramInt2) {
/* 115 */     return this;
/*     */   }
/*     */   Expression eval(long paramLong1, long paramLong2) {
/* 118 */     return this;
/*     */   }
/*     */   Expression eval(float paramFloat1, float paramFloat2) {
/* 121 */     return this;
/*     */   }
/*     */   Expression eval(double paramDouble1, double paramDouble2) {
/* 124 */     return this;
/*     */   }
/*     */   Expression eval(boolean paramBoolean1, boolean paramBoolean2) {
/* 127 */     return this;
/*     */   }
/*     */   Expression eval(String paramString1, String paramString2) {
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   Expression eval() {
/* 134 */     if (this.left.op == this.right.op) {
/* 135 */       switch (this.left.op) {
/*     */         case 62:
/*     */         case 63:
/*     */         case 64:
/*     */         case 65:
/* 140 */           return eval(((IntegerExpression)this.left).value, ((IntegerExpression)this.right).value);
/*     */         case 66:
/* 142 */           return eval(((LongExpression)this.left).value, ((LongExpression)this.right).value);
/*     */         case 67:
/* 144 */           return eval(((FloatExpression)this.left).value, ((FloatExpression)this.right).value);
/*     */         case 68:
/* 146 */           return eval(((DoubleExpression)this.left).value, ((DoubleExpression)this.right).value);
/*     */         case 61:
/* 148 */           return eval(((BooleanExpression)this.left).value, ((BooleanExpression)this.right).value);
/*     */         case 69:
/* 150 */           return eval(((StringExpression)this.left).value, ((StringExpression)this.right).value);
/*     */       } 
/*     */     }
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 160 */     this.left = this.left.inline(paramEnvironment, paramContext);
/* 161 */     this.right = this.right.inline(paramEnvironment, paramContext);
/* 162 */     return (this.left == null) ? this.right : new CommaExpression(this.where, this.left, this.right);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 165 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/* 166 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*     */     try {
/* 168 */       return eval().simplify();
/* 169 */     } catch (ArithmeticException arithmeticException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       return this;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 185 */     BinaryExpression binaryExpression = (BinaryExpression)clone();
/* 186 */     if (this.left != null) {
/* 187 */       binaryExpression.left = this.left.copyInline(paramContext);
/*     */     }
/* 189 */     if (this.right != null) {
/* 190 */       binaryExpression.right = this.right.copyInline(paramContext);
/*     */     }
/* 192 */     return binaryExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 199 */     return 1 + ((this.left != null) ? this.left.costInline(paramInt, paramEnvironment, paramContext) : 0) + ((this.right != null) ? this.right
/* 200 */       .costInline(paramInt, paramEnvironment, paramContext) : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 207 */     throw new CompilerError("codeOperation: " + opNames[this.op]);
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 210 */     if (this.type.isType(0)) {
/* 211 */       Label label1 = new Label();
/* 212 */       Label label2 = new Label();
/*     */       
/* 214 */       codeBranch(paramEnvironment, paramContext, paramAssembler, label1, true);
/* 215 */       paramAssembler.add(true, this.where, 18, new Integer(0));
/* 216 */       paramAssembler.add(true, this.where, 167, label2);
/* 217 */       paramAssembler.add((Instruction)label1);
/* 218 */       paramAssembler.add(true, this.where, 18, new Integer(1));
/* 219 */       paramAssembler.add((Instruction)label2);
/*     */     } else {
/* 221 */       this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 222 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 223 */       codeOperation(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 231 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 232 */     if (this.left != null) {
/* 233 */       this.left.print(paramPrintStream);
/*     */     } else {
/* 235 */       paramPrintStream.print("<null>");
/*     */     } 
/* 237 */     paramPrintStream.print(" ");
/* 238 */     if (this.right != null) {
/* 239 */       this.right.print(paramPrintStream);
/*     */     } else {
/* 241 */       paramPrintStream.print("<null>");
/*     */     } 
/* 243 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */