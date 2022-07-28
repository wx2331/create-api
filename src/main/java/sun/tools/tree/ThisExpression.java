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
/*     */ public class ThisExpression
/*     */   extends Expression
/*     */ {
/*     */   LocalMember field;
/*     */   Expression implementation;
/*     */   Expression outerArg;
/*     */   
/*     */   public ThisExpression(long paramLong) {
/*  48 */     super(82, paramLong, Type.tObject);
/*     */   }
/*     */   protected ThisExpression(int paramInt, long paramLong) {
/*  51 */     super(paramInt, paramLong, Type.tObject);
/*     */   }
/*     */   public ThisExpression(long paramLong, LocalMember paramLocalMember) {
/*  54 */     super(82, paramLong, Type.tObject);
/*  55 */     this.field = paramLocalMember;
/*  56 */     paramLocalMember.readcount++;
/*     */   }
/*     */   public ThisExpression(long paramLong, Context paramContext) {
/*  59 */     super(82, paramLong, Type.tObject);
/*  60 */     this.field = paramContext.getLocalField(idThis);
/*  61 */     this.field.readcount++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThisExpression(long paramLong, Expression paramExpression) {
/*  68 */     this(paramLong);
/*  69 */     this.outerArg = paramExpression;
/*     */   }
/*     */   
/*     */   public Expression getImplementation() {
/*  73 */     if (this.implementation != null)
/*  74 */       return this.implementation; 
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression getOuterArg() {
/*  84 */     return this.outerArg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  91 */     if (paramContext.field.isStatic()) {
/*  92 */       paramEnvironment.error(this.where, "undef.var", opNames[this.op]);
/*  93 */       this.type = Type.tError;
/*  94 */       return paramVset;
/*     */     } 
/*  96 */     if (this.field == null) {
/*  97 */       this.field = paramContext.getLocalField(idThis);
/*  98 */       this.field.readcount++;
/*     */     } 
/* 100 */     if (this.field.scopeNumber < paramContext.frameNumber)
/*     */     {
/* 102 */       this.implementation = paramContext.makeReference(paramEnvironment, this.field);
/*     */     }
/* 104 */     if (!paramVset.testVar(this.field.number)) {
/* 105 */       paramEnvironment.error(this.where, "access.inst.before.super", opNames[this.op]);
/*     */     }
/* 107 */     if (this.field == null) {
/* 108 */       this.type = paramContext.field.getClassDeclaration().getType();
/*     */     } else {
/* 110 */       this.type = this.field.getType();
/*     */     } 
/* 112 */     return paramVset;
/*     */   }
/*     */   
/*     */   public boolean isNonNull() {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext) {
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext) {
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 134 */     if (this.implementation != null)
/* 135 */       return this.implementation.inlineValue(paramEnvironment, paramContext); 
/* 136 */     if (this.field != null && this.field.isInlineable(paramEnvironment, false)) {
/* 137 */       Expression expression = (Expression)this.field.getValue(paramEnvironment);
/*     */       
/* 139 */       if (expression != null) {
/* 140 */         expression = expression.copyInline(paramContext);
/* 141 */         expression.type = this.type;
/* 142 */         return expression;
/*     */       } 
/*     */     } 
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 152 */     if (this.implementation != null)
/* 153 */       return this.implementation.copyInline(paramContext); 
/* 154 */     ThisExpression thisExpression = (ThisExpression)clone();
/* 155 */     if (this.field == null) {
/*     */       
/* 157 */       thisExpression.field = paramContext.getLocalField(idThis);
/* 158 */       thisExpression.field.readcount++;
/*     */     } else {
/* 160 */       thisExpression.field = this.field.getCurrentInlineCopy(paramContext);
/*     */     } 
/* 162 */     if (this.outerArg != null) {
/* 163 */       thisExpression.outerArg = this.outerArg.copyInline(paramContext);
/*     */     }
/* 165 */     return thisExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 172 */     paramAssembler.add(this.where, 25, new Integer(this.field.number));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 179 */     if (this.outerArg != null) {
/* 180 */       paramPrintStream.print("(outer=");
/* 181 */       this.outerArg.print(paramPrintStream);
/* 182 */       paramPrintStream.print(" ");
/*     */     } 
/*     */     
/* 185 */     String str = (this.field == null) ? "" : (this.field.getClassDefinition().getName().getFlatName().getName() + ".");
/* 186 */     str = str + opNames[this.op];
/* 187 */     paramPrintStream.print(str + "#" + ((this.field != null) ? this.field.hashCode() : 0));
/* 188 */     if (this.outerArg != null)
/* 189 */       paramPrintStream.print(")"); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ThisExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */