/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassNotFound;
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
/*     */ public class ConvertExpression
/*     */   extends UnaryExpression
/*     */ {
/*     */   public ConvertExpression(long paramLong, Type paramType, Expression paramExpression) {
/*  44 */     super(55, paramLong, paramType, paramExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  51 */     return this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */   Expression simplify() {
/*     */     int i;
/*     */     long l;
/*     */     float f;
/*     */     double d;
/*  58 */     switch (this.right.op) {
/*     */       case 62:
/*     */       case 63:
/*     */       case 64:
/*     */       case 65:
/*  63 */         i = ((IntegerExpression)this.right).value;
/*  64 */         switch (this.type.getTypeCode()) { case 1:
/*  65 */             return new ByteExpression(this.right.where, (byte)i);
/*  66 */           case 2: return new CharExpression(this.right.where, (char)i);
/*  67 */           case 3: return new ShortExpression(this.right.where, (short)i);
/*  68 */           case 4: return new IntExpression(this.right.where, i);
/*  69 */           case 5: return new LongExpression(this.right.where, i);
/*  70 */           case 6: return new FloatExpression(this.right.where, i);
/*  71 */           case 7: return new DoubleExpression(this.right.where, i); }
/*     */ 
/*     */         
/*     */         break;
/*     */       case 66:
/*  76 */         l = ((LongExpression)this.right).value;
/*  77 */         switch (this.type.getTypeCode()) { case 1:
/*  78 */             return new ByteExpression(this.right.where, (byte)(int)l);
/*  79 */           case 2: return new CharExpression(this.right.where, (char)(int)l);
/*  80 */           case 3: return new ShortExpression(this.right.where, (short)(int)l);
/*  81 */           case 4: return new IntExpression(this.right.where, (int)l);
/*  82 */           case 6: return new FloatExpression(this.right.where, (float)l);
/*  83 */           case 7: return new DoubleExpression(this.right.where, l); }
/*     */ 
/*     */         
/*     */         break;
/*     */       case 67:
/*  88 */         f = ((FloatExpression)this.right).value;
/*  89 */         switch (this.type.getTypeCode()) { case 1:
/*  90 */             return new ByteExpression(this.right.where, (byte)(int)f);
/*  91 */           case 2: return new CharExpression(this.right.where, (char)(int)f);
/*  92 */           case 3: return new ShortExpression(this.right.where, (short)(int)f);
/*  93 */           case 4: return new IntExpression(this.right.where, (int)f);
/*  94 */           case 5: return new LongExpression(this.right.where, (long)f);
/*  95 */           case 7: return new DoubleExpression(this.right.where, f); }
/*     */ 
/*     */         
/*     */         break;
/*     */       case 68:
/* 100 */         d = ((DoubleExpression)this.right).value;
/* 101 */         switch (this.type.getTypeCode()) { case 1:
/* 102 */             return new ByteExpression(this.right.where, (byte)(int)d);
/* 103 */           case 2: return new CharExpression(this.right.where, (char)(int)d);
/* 104 */           case 3: return new ShortExpression(this.right.where, (short)(int)d);
/* 105 */           case 4: return new IntExpression(this.right.where, (int)d);
/* 106 */           case 5: return new LongExpression(this.right.where, (long)d);
/* 107 */           case 6: return new FloatExpression(this.right.where, (float)d); }
/*     */ 
/*     */         
/*     */         break;
/*     */     } 
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(int paramInt) {
/* 119 */     return this.right.equals(paramInt);
/*     */   }
/*     */   public boolean equals(boolean paramBoolean) {
/* 122 */     return this.right.equals(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 131 */     if (this.right.type.inMask(1792) && this.type.inMask(1792)) {
/*     */       try {
/* 133 */         if (!paramEnvironment.implicitCast(this.right.type, this.type))
/* 134 */           return inlineValue(paramEnvironment, paramContext); 
/* 135 */       } catch (ClassNotFound classNotFound) {
/* 136 */         throw new CompilerError(classNotFound);
/*     */       } 
/*     */     }
/* 139 */     return super.inline(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 146 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 147 */     codeConversion(paramEnvironment, paramContext, paramAssembler, this.right.type, this.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 154 */     paramPrintStream.print("(" + opNames[this.op] + " " + this.type.toString() + " ");
/* 155 */     this.right.print(paramPrintStream);
/* 156 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ConvertExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */