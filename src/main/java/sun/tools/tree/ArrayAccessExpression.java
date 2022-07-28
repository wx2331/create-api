/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
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
/*     */ public class ArrayAccessExpression
/*     */   extends UnaryExpression
/*     */ {
/*     */   Expression index;
/*     */   
/*     */   public ArrayAccessExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  58 */     super(48, paramLong, Type.tError, paramExpression1);
/*  59 */     this.index = paramExpression2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  66 */     paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  67 */     if (this.index == null) {
/*  68 */       paramEnvironment.error(this.where, "array.index.required");
/*  69 */       return paramVset;
/*     */     } 
/*  71 */     paramVset = this.index.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  72 */     this.index = convert(paramEnvironment, paramContext, Type.tInt, this.index);
/*     */     
/*  74 */     if (!this.right.type.isType(9)) {
/*  75 */       if (!this.right.type.isType(13)) {
/*  76 */         paramEnvironment.error(this.where, "not.array", this.right.type);
/*     */       }
/*  78 */       return paramVset;
/*     */     } 
/*     */     
/*  81 */     this.type = this.right.type.getElementType();
/*  82 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression) {
/*  88 */     if (this.index == null) {
/*  89 */       paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*  90 */       if (this.right.type == Type.tPackage) {
/*  91 */         FieldExpression.reportFailedPackagePrefix(paramEnvironment, this.right);
/*  92 */         return paramVset;
/*     */       } 
/*     */ 
/*     */       
/*  96 */       if (this.right instanceof TypeExpression) {
/*  97 */         Type type = Type.tArray(this.right.type);
/*  98 */         paramUnaryExpression.right = new TypeExpression(this.where, type);
/*  99 */         return paramVset;
/*     */       } 
/*     */       
/* 102 */       paramEnvironment.error(this.where, "array.index.required");
/* 103 */       return paramVset;
/*     */     } 
/* 105 */     return super.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, paramUnaryExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkLHS(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 113 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression) {
/* 121 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext) {
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext) {
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Type toType(Environment paramEnvironment, Context paramContext) {
/* 144 */     return toType(paramEnvironment, this.right.toType(paramEnvironment, paramContext));
/*     */   }
/*     */   Type toType(Environment paramEnvironment, Type paramType) {
/* 147 */     if (this.index != null) {
/* 148 */       paramEnvironment.error(this.index.where, "array.dim.in.type");
/*     */     }
/* 150 */     return Type.tArray(paramType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 161 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 162 */     this.index = this.index.inlineValue(paramEnvironment, paramContext);
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 170 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 171 */     this.index = this.index.inlineValue(paramEnvironment, paramContext);
/* 172 */     return this;
/*     */   }
/*     */   public Expression inlineLHS(Environment paramEnvironment, Context paramContext) {
/* 175 */     return inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 182 */     ArrayAccessExpression arrayAccessExpression = (ArrayAccessExpression)clone();
/* 183 */     arrayAccessExpression.right = this.right.copyInline(paramContext);
/* 184 */     if (this.index == null) {
/*     */ 
/*     */ 
/*     */       
/* 188 */       arrayAccessExpression.index = null;
/*     */     } else {
/* 190 */       arrayAccessExpression.index = this.index.copyInline(paramContext);
/*     */     } 
/* 192 */     return arrayAccessExpression;
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
/* 203 */     return 1 + this.right.costInline(paramInt, paramEnvironment, paramContext) + this.index
/* 204 */       .costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 215 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 216 */     this.index.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 217 */     return 2;
/*     */   }
/*     */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 220 */     switch (this.type.getTypeCode()) {
/*     */       case 0:
/*     */       case 1:
/* 223 */         paramAssembler.add(this.where, 51);
/*     */         return;
/*     */       case 2:
/* 226 */         paramAssembler.add(this.where, 52);
/*     */         return;
/*     */       case 3:
/* 229 */         paramAssembler.add(this.where, 53);
/*     */         return;
/*     */     } 
/* 232 */     paramAssembler.add(this.where, 46 + this.type.getTypeCodeOffset());
/*     */   }
/*     */   
/*     */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 236 */     switch (this.type.getTypeCode()) {
/*     */       case 0:
/*     */       case 1:
/* 239 */         paramAssembler.add(this.where, 84);
/*     */         return;
/*     */       case 2:
/* 242 */         paramAssembler.add(this.where, 85);
/*     */         return;
/*     */       case 3:
/* 245 */         paramAssembler.add(this.where, 86);
/*     */         return;
/*     */     } 
/* 248 */     paramAssembler.add(this.where, 79 + this.type.getTypeCodeOffset());
/*     */   }
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 252 */     codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 253 */     codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 261 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 262 */     this.right.print(paramPrintStream);
/* 263 */     paramPrintStream.print(" ");
/* 264 */     if (this.index != null) {
/* 265 */       this.index.print(paramPrintStream);
/*     */     } else {
/* 267 */       paramPrintStream.print("<empty>");
/*     */     } 
/* 269 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ArrayAccessExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */