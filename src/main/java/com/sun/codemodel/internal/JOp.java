/*     */ package com.sun.codemodel.internal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JOp
/*     */ {
/*     */   static boolean hasTopOp(JExpression e) {
/*  44 */     return (e instanceof UnaryOp || e instanceof BinaryOp);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class UnaryOp
/*     */     extends JExpressionImpl
/*     */   {
/*     */     protected String op;
/*     */     protected JExpression e;
/*     */     protected boolean opFirst = true;
/*     */     
/*     */     UnaryOp(String op, JExpression e) {
/*  56 */       this.op = op;
/*  57 */       this.e = e;
/*     */     }
/*     */     
/*     */     UnaryOp(JExpression e, String op) {
/*  61 */       this.op = op;
/*  62 */       this.e = e;
/*  63 */       this.opFirst = false;
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/*  67 */       if (this.opFirst) {
/*  68 */         f.p('(').p(this.op).g(this.e).p(')');
/*     */       } else {
/*  70 */         f.p('(').g(this.e).p(this.op).p(')');
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static JExpression minus(JExpression e) {
/*  76 */     return new UnaryOp("-", e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression not(JExpression e) {
/*  83 */     if (e == JExpr.TRUE) return JExpr.FALSE; 
/*  84 */     if (e == JExpr.FALSE) return JExpr.TRUE; 
/*  85 */     return new UnaryOp("!", e);
/*     */   }
/*     */   
/*     */   public static JExpression complement(JExpression e) {
/*  89 */     return new UnaryOp("~", e);
/*     */   }
/*     */   
/*     */   private static class TightUnaryOp
/*     */     extends UnaryOp {
/*     */     TightUnaryOp(JExpression e, String op) {
/*  95 */       super(e, op);
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/*  99 */       if (this.opFirst) {
/* 100 */         f.p(this.op).g(this.e);
/*     */       } else {
/* 102 */         f.g(this.e).p(this.op);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static JExpression incr(JExpression e) {
/* 108 */     return new TightUnaryOp(e, "++");
/*     */   }
/*     */   
/*     */   public static JExpression decr(JExpression e) {
/* 112 */     return new TightUnaryOp(e, "--");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BinaryOp
/*     */     extends JExpressionImpl
/*     */   {
/*     */     String op;
/*     */     
/*     */     JExpression left;
/*     */     JGenerable right;
/*     */     
/*     */     BinaryOp(String op, JExpression left, JGenerable right) {
/* 125 */       this.left = left;
/* 126 */       this.op = op;
/* 127 */       this.right = right;
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/* 131 */       f.p('(').g(this.left).p(this.op).g(this.right).p(')');
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static JExpression plus(JExpression left, JExpression right) {
/* 137 */     return new BinaryOp("+", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression minus(JExpression left, JExpression right) {
/* 141 */     return new BinaryOp("-", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression mul(JExpression left, JExpression right) {
/* 145 */     return new BinaryOp("*", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression div(JExpression left, JExpression right) {
/* 149 */     return new BinaryOp("/", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression mod(JExpression left, JExpression right) {
/* 153 */     return new BinaryOp("%", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression shl(JExpression left, JExpression right) {
/* 157 */     return new BinaryOp("<<", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression shr(JExpression left, JExpression right) {
/* 161 */     return new BinaryOp(">>", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression shrz(JExpression left, JExpression right) {
/* 165 */     return new BinaryOp(">>>", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression band(JExpression left, JExpression right) {
/* 169 */     return new BinaryOp("&", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression bor(JExpression left, JExpression right) {
/* 173 */     return new BinaryOp("|", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression cand(JExpression left, JExpression right) {
/* 177 */     if (left == JExpr.TRUE) return right; 
/* 178 */     if (right == JExpr.TRUE) return left; 
/* 179 */     if (left == JExpr.FALSE) return left; 
/* 180 */     if (right == JExpr.FALSE) return right; 
/* 181 */     return new BinaryOp("&&", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression cor(JExpression left, JExpression right) {
/* 185 */     if (left == JExpr.TRUE) return left; 
/* 186 */     if (right == JExpr.TRUE) return right; 
/* 187 */     if (left == JExpr.FALSE) return right; 
/* 188 */     if (right == JExpr.FALSE) return left; 
/* 189 */     return new BinaryOp("||", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression xor(JExpression left, JExpression right) {
/* 193 */     return new BinaryOp("^", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression lt(JExpression left, JExpression right) {
/* 197 */     return new BinaryOp("<", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression lte(JExpression left, JExpression right) {
/* 201 */     return new BinaryOp("<=", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression gt(JExpression left, JExpression right) {
/* 205 */     return new BinaryOp(">", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression gte(JExpression left, JExpression right) {
/* 209 */     return new BinaryOp(">=", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression eq(JExpression left, JExpression right) {
/* 213 */     return new BinaryOp("==", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression ne(JExpression left, JExpression right) {
/* 217 */     return new BinaryOp("!=", left, right);
/*     */   }
/*     */   
/*     */   public static JExpression _instanceof(JExpression left, JType right) {
/* 221 */     return new BinaryOp("instanceof", left, right);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TernaryOp
/*     */     extends JExpressionImpl
/*     */   {
/*     */     String op1;
/*     */     
/*     */     String op2;
/*     */     JExpression e1;
/*     */     JExpression e2;
/*     */     JExpression e3;
/*     */     
/*     */     TernaryOp(String op1, String op2, JExpression e1, JExpression e2, JExpression e3) {
/* 236 */       this.e1 = e1;
/* 237 */       this.op1 = op1;
/* 238 */       this.e2 = e2;
/* 239 */       this.op2 = op2;
/* 240 */       this.e3 = e3;
/*     */     }
/*     */     
/*     */     public void generate(JFormatter f) {
/* 244 */       f.p('(').g(this.e1).p(this.op1).g(this.e2).p(this.op2).g(this.e3).p(')');
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression cond(JExpression cond, JExpression ifTrue, JExpression ifFalse) {
/* 251 */     return new TernaryOp("?", ":", cond, ifTrue, ifFalse);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JOp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */