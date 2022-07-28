/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDeclaration;
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
/*     */ public class ThrowStatement
/*     */   extends Statement
/*     */ {
/*     */   Expression expr;
/*     */   
/*     */   public ThrowStatement(long paramLong, Expression paramExpression) {
/*  46 */     super(104, paramLong);
/*  47 */     this.expr = paramExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable<ClassDeclaration, ThrowStatement> paramHashtable) {
/*  54 */     checkLabel(paramEnvironment, paramContext);
/*     */     try {
/*  56 */       paramVset = reach(paramEnvironment, paramVset);
/*  57 */       this.expr.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  58 */       if (this.expr.type.isType(10)) {
/*  59 */         ClassDeclaration classDeclaration1 = paramEnvironment.getClassDeclaration(this.expr.type);
/*  60 */         if (paramHashtable.get(classDeclaration1) == null) {
/*  61 */           paramHashtable.put(classDeclaration1, this);
/*     */         }
/*  63 */         ClassDefinition classDefinition = classDeclaration1.getClassDefinition(paramEnvironment);
/*     */         
/*  65 */         ClassDeclaration classDeclaration2 = paramEnvironment.getClassDeclaration(idJavaLangThrowable);
/*  66 */         if (!classDefinition.subClassOf(paramEnvironment, classDeclaration2)) {
/*  67 */           paramEnvironment.error(this.where, "throw.not.throwable", classDefinition);
/*     */         }
/*  69 */         this.expr = convert(paramEnvironment, paramContext, Type.tObject, this.expr);
/*  70 */       } else if (!this.expr.type.isType(13)) {
/*  71 */         paramEnvironment.error(this.expr.where, "throw.not.throwable", this.expr.type);
/*     */       } 
/*  73 */     } catch (ClassNotFound classNotFound) {
/*  74 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/*     */     } 
/*  76 */     CheckContext checkContext = paramContext.getTryExitContext();
/*  77 */     if (checkContext != null) {
/*  78 */       checkContext.vsTryExit = checkContext.vsTryExit.join(paramVset);
/*     */     }
/*  80 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/*  87 */     this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/*  95 */     ThrowStatement throwStatement = (ThrowStatement)clone();
/*  96 */     throwStatement.expr = this.expr.copyInline(paramContext);
/*  97 */     return throwStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 104 */     return 1 + this.expr.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 111 */     this.expr.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 112 */     paramAssembler.add(this.where, 191);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 119 */     super.print(paramPrintStream, paramInt);
/* 120 */     paramPrintStream.print("throw ");
/* 121 */     this.expr.print(paramPrintStream);
/* 122 */     paramPrintStream.print(":");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ThrowStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */