/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
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
/*     */ 
/*     */ public class NotExpression
/*     */   extends UnaryExpression
/*     */ {
/*     */   public NotExpression(long paramLong, Expression paramExpression) {
/*  44 */     super(37, paramLong, Type.tBoolean, paramExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/*  51 */     this.right = convert(paramEnvironment, paramContext, Type.tBoolean, this.right);
/*     */   }
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
/*     */   public void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars) {
/*  69 */     this.right.checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, paramConditionVars);
/*  70 */     this.right = convert(paramEnvironment, paramContext, Type.tBoolean, this.right);
/*     */     
/*  72 */     Vset vset = paramConditionVars.vsFalse;
/*  73 */     paramConditionVars.vsFalse = paramConditionVars.vsTrue;
/*  74 */     paramConditionVars.vsTrue = vset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression eval(boolean paramBoolean) {
/*  81 */     return new BooleanExpression(this.where, !paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/*  89 */     switch (this.right.op) {
/*     */       case 37:
/*  91 */         return ((NotExpression)this.right).right;
/*     */       
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */         break;
/*     */       
/*     */       default:
/* 102 */         return this;
/*     */     } 
/*     */ 
/*     */     
/* 106 */     BinaryExpression binaryExpression = (BinaryExpression)this.right;
/* 107 */     if (binaryExpression.left.type.inMask(192)) {
/* 108 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 112 */     switch (this.right.op) {
/*     */       case 20:
/* 114 */         return new NotEqualExpression(this.where, binaryExpression.left, binaryExpression.right);
/*     */       case 19:
/* 116 */         return new EqualExpression(this.where, binaryExpression.left, binaryExpression.right);
/*     */       case 24:
/* 118 */         return new GreaterOrEqualExpression(this.where, binaryExpression.left, binaryExpression.right);
/*     */       case 23:
/* 120 */         return new GreaterExpression(this.where, binaryExpression.left, binaryExpression.right);
/*     */       case 22:
/* 122 */         return new LessOrEqualExpression(this.where, binaryExpression.left, binaryExpression.right);
/*     */       case 21:
/* 124 */         return new LessExpression(this.where, binaryExpression.left, binaryExpression.right);
/*     */     } 
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean) {
/* 133 */     this.right.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, !paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 141 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 142 */     paramAssembler.add(this.where, 18, new Integer(1));
/* 143 */     paramAssembler.add(this.where, 130);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\NotExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */