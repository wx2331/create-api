/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.ClassNotFound;
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
/*     */ public class ConditionalExpression
/*     */   extends BinaryExpression
/*     */ {
/*     */   Expression cond;
/*     */   
/*     */   public ConditionalExpression(long paramLong, Expression paramExpression1, Expression paramExpression2, Expression paramExpression3) {
/*  47 */     super(13, paramLong, Type.tError, paramExpression2, paramExpression3);
/*  48 */     this.cond = paramExpression1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression order() {
/*  55 */     if (precedence() > this.cond.precedence()) {
/*  56 */       UnaryExpression unaryExpression = (UnaryExpression)this.cond;
/*  57 */       this.cond = unaryExpression.right;
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
/*  68 */     ConditionVars conditionVars = this.cond.checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  69 */     paramVset = this.left.checkValue(paramEnvironment, paramContext, conditionVars.vsTrue, paramHashtable).join(this.right
/*  70 */         .checkValue(paramEnvironment, paramContext, conditionVars.vsFalse, paramHashtable));
/*  71 */     this.cond = convert(paramEnvironment, paramContext, Type.tBoolean, this.cond);
/*     */     
/*  73 */     int i = this.left.type.getTypeMask() | this.right.type.getTypeMask();
/*  74 */     if ((i & 0x2000) != 0) {
/*  75 */       this.type = Type.tError;
/*  76 */       return paramVset;
/*     */     } 
/*  78 */     if (this.left.type.equals(this.right.type)) {
/*  79 */       this.type = this.left.type;
/*  80 */     } else if ((i & 0x80) != 0) {
/*  81 */       this.type = Type.tDouble;
/*  82 */     } else if ((i & 0x40) != 0) {
/*  83 */       this.type = Type.tFloat;
/*  84 */     } else if ((i & 0x20) != 0) {
/*  85 */       this.type = Type.tLong;
/*  86 */     } else if ((i & 0x700) != 0) {
/*     */ 
/*     */       
/*     */       try {
/*  90 */         this.type = paramEnvironment.implicitCast(this.right.type, this.left.type) ? this.left.type : this.right.type;
/*     */       }
/*  92 */       catch (ClassNotFound classNotFound) {
/*  93 */         this.type = Type.tError;
/*     */       } 
/*  95 */     } else if ((i & 0x4) != 0 && this.left.fitsType(paramEnvironment, paramContext, Type.tChar) && this.right.fitsType(paramEnvironment, paramContext, Type.tChar)) {
/*  96 */       this.type = Type.tChar;
/*  97 */     } else if ((i & 0x8) != 0 && this.left.fitsType(paramEnvironment, paramContext, Type.tShort) && this.right.fitsType(paramEnvironment, paramContext, Type.tShort)) {
/*  98 */       this.type = Type.tShort;
/*  99 */     } else if ((i & 0x2) != 0 && this.left.fitsType(paramEnvironment, paramContext, Type.tByte) && this.right.fitsType(paramEnvironment, paramContext, Type.tByte)) {
/* 100 */       this.type = Type.tByte;
/*     */     } else {
/* 102 */       this.type = Type.tInt;
/*     */     } 
/*     */     
/* 105 */     this.left = convert(paramEnvironment, paramContext, this.type, this.left);
/* 106 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/* 107 */     return paramVset;
/*     */   }
/*     */   
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 111 */     paramVset = this.cond.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 112 */     this.cond = convert(paramEnvironment, paramContext, Type.tBoolean, this.cond);
/* 113 */     return this.left.check(paramEnvironment, paramContext, paramVset.copy(), paramHashtable).join(this.right.check(paramEnvironment, paramContext, paramVset, paramHashtable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 120 */     return (this.cond.isConstant() && this.left.isConstant() && this.right.isConstant());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/* 127 */     if (this.cond.equals(true)) {
/* 128 */       return this.left;
/*     */     }
/* 130 */     if (this.cond.equals(false)) {
/* 131 */       return this.right;
/*     */     }
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 140 */     this.left = this.left.inline(paramEnvironment, paramContext);
/* 141 */     this.right = this.right.inline(paramEnvironment, paramContext);
/* 142 */     if (this.left == null && this.right == null) {
/* 143 */       return this.cond.inline(paramEnvironment, paramContext);
/*     */     }
/* 145 */     if (this.left == null) {
/* 146 */       this.left = this.right;
/* 147 */       this.right = null;
/* 148 */       this.cond = new NotExpression(this.where, this.cond);
/*     */     } 
/* 150 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/* 151 */     return simplify();
/*     */   }
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 155 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/* 156 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/* 157 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 158 */     return simplify();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 169 */     return 1 + this.cond
/* 170 */       .costInline(paramInt, paramEnvironment, paramContext) + this.left
/* 171 */       .costInline(paramInt, paramEnvironment, paramContext) + ((this.right == null) ? 0 : this.right
/* 172 */       .costInline(paramInt, paramEnvironment, paramContext));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 179 */     ConditionalExpression conditionalExpression = (ConditionalExpression)clone();
/* 180 */     conditionalExpression.cond = this.cond.copyInline(paramContext);
/* 181 */     conditionalExpression.left = this.left.copyInline(paramContext);
/*     */ 
/*     */ 
/*     */     
/* 185 */     conditionalExpression.right = (this.right == null) ? null : this.right.copyInline(paramContext);
/*     */     
/* 187 */     return conditionalExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 194 */     Label label1 = new Label();
/* 195 */     Label label2 = new Label();
/*     */     
/* 197 */     this.cond.codeBranch(paramEnvironment, paramContext, paramAssembler, label1, false);
/* 198 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 199 */     paramAssembler.add(this.where, 167, label2);
/* 200 */     paramAssembler.add((Instruction)label1);
/* 201 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 202 */     paramAssembler.add((Instruction)label2);
/*     */   }
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 205 */     Label label = new Label();
/* 206 */     this.cond.codeBranch(paramEnvironment, paramContext, paramAssembler, label, false);
/* 207 */     this.left.code(paramEnvironment, paramContext, paramAssembler);
/* 208 */     if (this.right != null) {
/* 209 */       Label label1 = new Label();
/* 210 */       paramAssembler.add(this.where, 167, label1);
/* 211 */       paramAssembler.add((Instruction)label);
/* 212 */       this.right.code(paramEnvironment, paramContext, paramAssembler);
/* 213 */       paramAssembler.add((Instruction)label1);
/*     */     } else {
/* 215 */       paramAssembler.add((Instruction)label);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 223 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 224 */     this.cond.print(paramPrintStream);
/* 225 */     paramPrintStream.print(" ");
/* 226 */     this.left.print(paramPrintStream);
/* 227 */     paramPrintStream.print(" ");
/* 228 */     if (this.right != null) {
/* 229 */       this.right.print(paramPrintStream);
/*     */     } else {
/* 231 */       paramPrintStream.print("<null>");
/*     */     } 
/* 233 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ConditionalExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */