/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
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
/*     */ public class Node
/*     */   implements Constants, Cloneable
/*     */ {
/*     */   int op;
/*     */   long where;
/*     */   
/*     */   Node(int paramInt, long paramLong) {
/*  46 */     this.op = paramInt;
/*  47 */     this.where = paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOp() {
/*  54 */     return this.op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWhere() {
/*  61 */     return this.where;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression convert(Environment paramEnvironment, Context paramContext, Type paramType, Expression paramExpression) {
/*  68 */     if (paramExpression.type.isType(13) || paramType.isType(13))
/*     */     {
/*  70 */       return paramExpression;
/*     */     }
/*     */     
/*  73 */     if (paramExpression.type.equals(paramType))
/*     */     {
/*  75 */       return paramExpression;
/*     */     }
/*     */     
/*     */     try {
/*  79 */       if (paramExpression.fitsType(paramEnvironment, paramContext, paramType)) {
/*  80 */         return new ConvertExpression(this.where, paramType, paramExpression);
/*     */       }
/*     */       
/*  83 */       if (paramEnvironment.explicitCast(paramExpression.type, paramType)) {
/*  84 */         paramEnvironment.error(this.where, "explicit.cast.needed", opNames[this.op], paramExpression.type, paramType);
/*  85 */         return new ConvertExpression(this.where, paramType, paramExpression);
/*     */       } 
/*  87 */     } catch (ClassNotFound classNotFound) {
/*  88 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/*     */     } 
/*     */ 
/*     */     
/*  92 */     paramEnvironment.error(this.where, "incompatible.type", opNames[this.op], paramExpression.type, paramType);
/*  93 */     return new ConvertExpression(this.where, Type.tError, paramExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 100 */     throw new CompilerError("print");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 108 */       return super.clone();
/* 109 */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/*     */       
/* 111 */       throw (InternalError)(new InternalError()).initCause(cloneNotSupportedException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 120 */     print(new PrintStream(byteArrayOutputStream));
/* 121 */     return byteArrayOutputStream.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */