/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.java.ClassDefinition;
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
/*     */ 
/*     */ 
/*     */ public class CastExpression
/*     */   extends BinaryExpression
/*     */ {
/*     */   public CastExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  45 */     super(34, paramLong, paramExpression1.type, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  52 */     this.type = this.left.toType(paramEnvironment, paramContext);
/*  53 */     paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     
/*  55 */     if (this.type.isType(13) || this.right.type.isType(13))
/*     */     {
/*  57 */       return paramVset;
/*     */     }
/*     */     
/*  60 */     if (this.type.equals(this.right.type))
/*     */     {
/*  62 */       return paramVset;
/*     */     }
/*     */     
/*     */     try {
/*  66 */       if (paramEnvironment.explicitCast(this.right.type, this.type)) {
/*  67 */         this.right = new ConvertExpression(this.where, this.type, this.right);
/*  68 */         return paramVset;
/*     */       } 
/*  70 */     } catch (ClassNotFound classNotFound) {
/*  71 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/*     */     } 
/*     */ 
/*     */     
/*  75 */     paramEnvironment.error(this.where, "invalid.cast", this.right.type, this.type);
/*  76 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/*  83 */     if (this.type.inMask(1792) && !this.type.equals(Type.tString))
/*     */     {
/*  85 */       return false;
/*     */     }
/*  87 */     return this.right.isConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  94 */     return this.right.inline(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  97 */     return this.right.inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 102 */     if (paramContext == null) {
/* 103 */       return 1 + this.right.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */     
/* 106 */     ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */ 
/*     */     
/*     */     try {
/* 110 */       if (this.left.type.isType(9) || classDefinition
/* 111 */         .permitInlinedAccess(paramEnvironment, paramEnvironment
/* 112 */           .getClassDeclaration(this.left.type)))
/* 113 */         return 1 + this.right.costInline(paramInt, paramEnvironment, paramContext); 
/* 114 */     } catch (ClassNotFound classNotFound) {}
/*     */     
/* 116 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 125 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 126 */     if (this.type.isType(13)) {
/* 127 */       this.left.print(paramPrintStream);
/*     */     } else {
/* 129 */       paramPrintStream.print(this.type);
/*     */     } 
/* 131 */     paramPrintStream.print(" ");
/* 132 */     this.right.print(paramPrintStream);
/* 133 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\CastExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */