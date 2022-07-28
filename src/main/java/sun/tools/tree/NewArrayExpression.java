/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.ArrayData;
/*     */ import sun.tools.asm.Assembler;
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
/*     */ 
/*     */ public class NewArrayExpression
/*     */   extends NaryExpression
/*     */ {
/*     */   Expression init;
/*     */   
/*     */   public NewArrayExpression(long paramLong, Expression paramExpression, Expression[] paramArrayOfExpression) {
/*  47 */     super(41, paramLong, Type.tError, paramExpression, paramArrayOfExpression);
/*     */   }
/*     */   
/*     */   public NewArrayExpression(long paramLong, Expression paramExpression1, Expression[] paramArrayOfExpression, Expression paramExpression2) {
/*  51 */     this(paramLong, paramExpression1, paramArrayOfExpression);
/*  52 */     this.init = paramExpression2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  59 */     this.type = this.right.toType(paramEnvironment, paramContext);
/*     */     
/*  61 */     boolean bool = (this.init != null) ? true : false;
/*  62 */     for (byte b = 0; b < this.args.length; b++) {
/*  63 */       Expression expression = this.args[b];
/*  64 */       if (expression == null) {
/*  65 */         if (b == 0 && !bool) {
/*  66 */           paramEnvironment.error(this.where, "array.dim.missing");
/*     */         }
/*  68 */         bool = true;
/*     */       } else {
/*  70 */         if (bool) {
/*  71 */           paramEnvironment.error(expression.where, "invalid.array.dim");
/*     */         }
/*  73 */         paramVset = expression.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  74 */         this.args[b] = convert(paramEnvironment, paramContext, Type.tInt, expression);
/*     */       } 
/*  76 */       this.type = Type.tArray(this.type);
/*     */     } 
/*  78 */     if (this.init != null) {
/*  79 */       paramVset = this.init.checkInitializer(paramEnvironment, paramContext, paramVset, this.type, paramHashtable);
/*  80 */       this.init = convert(paramEnvironment, paramContext, this.type, this.init);
/*     */     } 
/*  82 */     return paramVset;
/*     */   }
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/*  86 */     NewArrayExpression newArrayExpression = (NewArrayExpression)super.copyInline(paramContext);
/*  87 */     if (this.init != null) {
/*  88 */       newArrayExpression.init = this.init.copyInline(paramContext);
/*     */     }
/*  90 */     return newArrayExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  97 */     Expression expression = null;
/*  98 */     for (byte b = 0; b < this.args.length; b++) {
/*  99 */       if (this.args[b] != null) {
/* 100 */         expression = (expression != null) ? new CommaExpression(this.where, expression, this.args[b]) : this.args[b];
/*     */       }
/*     */     } 
/* 103 */     if (this.init != null)
/* 104 */       expression = (expression != null) ? new CommaExpression(this.where, expression, this.init) : this.init; 
/* 105 */     return (expression != null) ? expression.inline(paramEnvironment, paramContext) : null;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 108 */     if (this.init != null)
/* 109 */       return this.init.inlineValue(paramEnvironment, paramContext); 
/* 110 */     for (byte b = 0; b < this.args.length; b++) {
/* 111 */       if (this.args[b] != null) {
/* 112 */         this.args[b] = this.args[b].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */     } 
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 122 */     byte b1 = 0;
/* 123 */     for (byte b2 = 0; b2 < this.args.length; b2++) {
/* 124 */       if (this.args[b2] != null) {
/* 125 */         this.args[b2].codeValue(paramEnvironment, paramContext, paramAssembler);
/* 126 */         b1++;
/*     */       } 
/*     */     } 
/* 129 */     if (this.args.length > 1) {
/* 130 */       paramAssembler.add(this.where, 197, new ArrayData(this.type, b1));
/*     */       
/*     */       return;
/*     */     } 
/* 134 */     switch (this.type.getElementType().getTypeCode()) {
/*     */       case 0:
/* 136 */         paramAssembler.add(this.where, 188, new Integer(4)); return;
/*     */       case 1:
/* 138 */         paramAssembler.add(this.where, 188, new Integer(8)); return;
/*     */       case 3:
/* 140 */         paramAssembler.add(this.where, 188, new Integer(9)); return;
/*     */       case 2:
/* 142 */         paramAssembler.add(this.where, 188, new Integer(5)); return;
/*     */       case 4:
/* 144 */         paramAssembler.add(this.where, 188, new Integer(10)); return;
/*     */       case 5:
/* 146 */         paramAssembler.add(this.where, 188, new Integer(11)); return;
/*     */       case 6:
/* 148 */         paramAssembler.add(this.where, 188, new Integer(6)); return;
/*     */       case 7:
/* 150 */         paramAssembler.add(this.where, 188, new Integer(7)); return;
/*     */       case 9:
/* 152 */         paramAssembler.add(this.where, 189, this.type.getElementType()); return;
/*     */       case 10:
/* 154 */         paramAssembler.add(this.where, 189, paramEnvironment
/* 155 */             .getClassDeclaration(this.type.getElementType()));
/*     */         return;
/*     */     } 
/* 158 */     throw new CompilerError("codeValue");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\NewArrayExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */