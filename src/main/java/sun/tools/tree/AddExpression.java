/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
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
/*     */ public class AddExpression
/*     */   extends BinaryArithmeticExpression
/*     */ {
/*     */   public AddExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  42 */     super(29, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/*  49 */     if (this.left.type == Type.tString && !this.right.type.isType(11)) {
/*  50 */       this.type = Type.tString; return;
/*     */     } 
/*  52 */     if (this.right.type == Type.tString && !this.left.type.isType(11)) {
/*  53 */       this.type = Type.tString;
/*     */       return;
/*     */     } 
/*  56 */     super.selectType(paramEnvironment, paramContext, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNonNull() {
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression eval(int paramInt1, int paramInt2) {
/*  68 */     return new IntExpression(this.where, paramInt1 + paramInt2);
/*     */   }
/*     */   Expression eval(long paramLong1, long paramLong2) {
/*  71 */     return new LongExpression(this.where, paramLong1 + paramLong2);
/*     */   }
/*     */   Expression eval(float paramFloat1, float paramFloat2) {
/*  74 */     return new FloatExpression(this.where, paramFloat1 + paramFloat2);
/*     */   }
/*     */   Expression eval(double paramDouble1, double paramDouble2) {
/*  77 */     return new DoubleExpression(this.where, paramDouble1 + paramDouble2);
/*     */   }
/*     */   Expression eval(String paramString1, String paramString2) {
/*  80 */     return new StringExpression(this.where, paramString1 + paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  90 */     if (this.type == Type.tString && isConstant()) {
/*  91 */       StringBuffer stringBuffer = inlineValueSB(paramEnvironment, paramContext, new StringBuffer());
/*  92 */       if (stringBuffer != null)
/*     */       {
/*  94 */         return new StringExpression(this.where, stringBuffer.toString());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  99 */     return super.inlineValue(paramEnvironment, paramContext);
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
/*     */   protected StringBuffer inlineValueSB(Environment paramEnvironment, Context paramContext, StringBuffer paramStringBuffer) {
/* 150 */     if (this.type != Type.tString)
/*     */     {
/*     */       
/* 153 */       return super.inlineValueSB(paramEnvironment, paramContext, paramStringBuffer);
/*     */     }
/*     */     
/* 156 */     paramStringBuffer = this.left.inlineValueSB(paramEnvironment, paramContext, paramStringBuffer);
/* 157 */     if (paramStringBuffer != null) {
/* 158 */       paramStringBuffer = this.right.inlineValueSB(paramEnvironment, paramContext, paramStringBuffer);
/*     */     }
/* 160 */     return paramStringBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/* 167 */     if (!this.type.isType(10)) {
/*     */       
/* 169 */       if (this.type.inMask(62)) {
/* 170 */         if (this.left.equals(0)) {
/* 171 */           return this.right;
/*     */         }
/* 173 */         if (this.right.equals(0)) {
/* 174 */           return this.left;
/*     */         }
/*     */       } 
/* 177 */     } else if (this.right.type.isType(8)) {
/* 178 */       this.right = new StringExpression(this.right.where, "null");
/* 179 */     } else if (this.left.type.isType(8)) {
/* 180 */       this.left = new StringExpression(this.left.where, "null");
/*     */     } 
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 189 */     return (this.type.isType(10) ? 12 : 1) + this.left
/* 190 */       .costInline(paramInt, paramEnvironment, paramContext) + this.right
/* 191 */       .costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 198 */     paramAssembler.add(this.where, 96 + this.type.getTypeCodeOffset());
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
/*     */   void codeAppend(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, ClassDeclaration paramClassDeclaration, boolean paramBoolean) throws ClassNotFound, AmbiguousMember {
/* 210 */     if (this.type.isType(10)) {
/* 211 */       this.left.codeAppend(paramEnvironment, paramContext, paramAssembler, paramClassDeclaration, paramBoolean);
/* 212 */       this.right.codeAppend(paramEnvironment, paramContext, paramAssembler, paramClassDeclaration, false);
/*     */     } else {
/* 214 */       super.codeAppend(paramEnvironment, paramContext, paramAssembler, paramClassDeclaration, paramBoolean);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 219 */     if (this.type.isType(10)) {
/*     */       
/*     */       try {
/* 222 */         if (this.left.equals("")) {
/* 223 */           this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 224 */           this.right.ensureString(paramEnvironment, paramContext, paramAssembler);
/*     */           return;
/*     */         } 
/* 227 */         if (this.right.equals("")) {
/* 228 */           this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 229 */           this.left.ensureString(paramEnvironment, paramContext, paramAssembler);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 234 */         ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(idJavaLangStringBuffer);
/* 235 */         ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */         
/* 237 */         codeAppend(paramEnvironment, paramContext, paramAssembler, classDeclaration, true);
/*     */ 
/*     */         
/* 240 */         MemberDefinition memberDefinition = classDeclaration.getClassDefinition(paramEnvironment).matchMethod(paramEnvironment, classDefinition, idToString);
/*     */ 
/*     */         
/* 243 */         paramAssembler.add(this.where, 182, memberDefinition);
/* 244 */       } catch (ClassNotFound classNotFound) {
/* 245 */         throw new CompilerError(classNotFound);
/* 246 */       } catch (AmbiguousMember ambiguousMember) {
/* 247 */         throw new CompilerError(ambiguousMember);
/*     */       } 
/*     */     } else {
/* 250 */       super.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\AddExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */