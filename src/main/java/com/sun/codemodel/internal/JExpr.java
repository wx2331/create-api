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
/*     */ public abstract class JExpr
/*     */ {
/*     */   public static JExpression assign(JAssignmentTarget lhs, JExpression rhs) {
/*  40 */     return new JAssignment(lhs, rhs);
/*     */   }
/*     */   
/*     */   public static JExpression assignPlus(JAssignmentTarget lhs, JExpression rhs) {
/*  44 */     return new JAssignment(lhs, rhs, "+");
/*     */   }
/*     */   
/*     */   public static JInvocation _new(JClass c) {
/*  48 */     return new JInvocation(c);
/*     */   }
/*     */   
/*     */   public static JInvocation _new(JType t) {
/*  52 */     return new JInvocation(t);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(String method) {
/*  56 */     return new JInvocation((JExpression)null, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JMethod method) {
/*  60 */     return new JInvocation((JExpression)null, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JExpression lhs, JMethod method) {
/*  64 */     return new JInvocation(lhs, method);
/*     */   }
/*     */   
/*     */   public static JInvocation invoke(JExpression lhs, String method) {
/*  68 */     return new JInvocation(lhs, method);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(String field) {
/*  72 */     return new JFieldRef((JExpression)null, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(JExpression lhs, JVar field) {
/*  76 */     return new JFieldRef(lhs, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef ref(JExpression lhs, String field) {
/*  80 */     return new JFieldRef(lhs, field);
/*     */   }
/*     */   
/*     */   public static JFieldRef refthis(String field) {
/*  84 */     return new JFieldRef(null, field, true);
/*     */   }
/*     */   
/*     */   public static JExpression dotclass(final JClass cl) {
/*  88 */     return new JExpressionImpl() {
/*     */         public void generate(JFormatter f) {
/*     */           JClass c;
/*  91 */           if (cl instanceof JNarrowedClass) {
/*  92 */             c = ((JNarrowedClass)cl).basis;
/*     */           } else {
/*  94 */             c = cl;
/*  95 */           }  f.g(c).p(".class");
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static JArrayCompRef component(JExpression lhs, JExpression index) {
/* 101 */     return new JArrayCompRef(lhs, index);
/*     */   }
/*     */   
/*     */   public static JCast cast(JType type, JExpression expr) {
/* 105 */     return new JCast(type, expr);
/*     */   }
/*     */   
/*     */   public static JArray newArray(JType type) {
/* 109 */     return newArray(type, (JExpression)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JArray newArray(JType type, JExpression size) {
/* 120 */     return new JArray(type.erasure(), size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JArray newArray(JType type, int size) {
/* 130 */     return newArray(type, lit(size));
/*     */   }
/*     */ 
/*     */   
/* 134 */   private static final JExpression __this = new JAtom("this");
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression _this() {
/* 139 */     return __this;
/*     */   }
/* 141 */   private static final JExpression __super = new JAtom("super");
/*     */ 
/*     */ 
/*     */   
/*     */   public static JExpression _super() {
/* 146 */     return __super;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 151 */   private static final JExpression __null = new JAtom("null");
/*     */   public static JExpression _null() {
/* 153 */     return __null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static final JExpression TRUE = new JAtom("true");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static final JExpression FALSE = new JAtom("false"); static final String charEscape = "\b\t\n\f\r\"'\\";
/*     */   
/*     */   public static JExpression lit(boolean b) {
/* 167 */     return b ? TRUE : FALSE;
/*     */   }
/*     */   static final String charMacro = "btnfr\"'\\";
/*     */   public static JExpression lit(int n) {
/* 171 */     return new JAtom(Integer.toString(n));
/*     */   }
/*     */   
/*     */   public static JExpression lit(long n) {
/* 175 */     return new JAtom(Long.toString(n) + "L");
/*     */   }
/*     */   
/*     */   public static JExpression lit(float f) {
/* 179 */     if (f == Float.NEGATIVE_INFINITY)
/*     */     {
/* 181 */       return new JAtom("java.lang.Float.NEGATIVE_INFINITY");
/*     */     }
/* 183 */     if (f == Float.POSITIVE_INFINITY)
/*     */     {
/* 185 */       return new JAtom("java.lang.Float.POSITIVE_INFINITY");
/*     */     }
/* 187 */     if (Float.isNaN(f))
/*     */     {
/* 189 */       return new JAtom("java.lang.Float.NaN");
/*     */     }
/*     */ 
/*     */     
/* 193 */     return new JAtom(Float.toString(f) + "F");
/*     */   }
/*     */ 
/*     */   
/*     */   public static JExpression lit(double d) {
/* 198 */     if (d == Double.NEGATIVE_INFINITY)
/*     */     {
/* 200 */       return new JAtom("java.lang.Double.NEGATIVE_INFINITY");
/*     */     }
/* 202 */     if (d == Double.POSITIVE_INFINITY)
/*     */     {
/* 204 */       return new JAtom("java.lang.Double.POSITIVE_INFINITY");
/*     */     }
/* 206 */     if (Double.isNaN(d))
/*     */     {
/* 208 */       return new JAtom("java.lang.Double.NaN");
/*     */     }
/*     */ 
/*     */     
/* 212 */     return new JAtom(Double.toString(d) + "D");
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
/*     */   public static String quotify(char quote, String s) {
/* 224 */     int n = s.length();
/* 225 */     StringBuilder sb = new StringBuilder(n + 2);
/* 226 */     sb.append(quote);
/* 227 */     for (int i = 0; i < n; i++) {
/* 228 */       char c = s.charAt(i);
/* 229 */       int j = "\b\t\n\f\r\"'\\".indexOf(c);
/* 230 */       if (j >= 0) {
/* 231 */         if ((quote == '"' && c == '\'') || (quote == '\'' && c == '"')) {
/* 232 */           sb.append(c);
/*     */         } else {
/* 234 */           sb.append('\\');
/* 235 */           sb.append("btnfr\"'\\".charAt(j));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 246 */       else if (c < ' ' || '~' < c) {
/*     */         
/* 248 */         sb.append("\\u");
/* 249 */         String hex = Integer.toHexString(c & Character.MAX_VALUE);
/* 250 */         for (int k = hex.length(); k < 4; k++)
/* 251 */           sb.append('0'); 
/* 252 */         sb.append(hex);
/*     */       } else {
/* 254 */         sb.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 258 */     sb.append(quote);
/* 259 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static JExpression lit(char c) {
/* 263 */     return new JAtom(quotify('\'', "" + c));
/*     */   }
/*     */   
/*     */   public static JExpression lit(String s) {
/* 267 */     return new JStringLiteral(s);
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
/*     */   public static JExpression direct(final String source) {
/* 283 */     return new JExpressionImpl() {
/*     */         public void generate(JFormatter f) {
/* 285 */           f.p('(').p(source).p(')');
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */