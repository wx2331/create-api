/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InlineMethodExpression
/*     */   extends Expression
/*     */ {
/*     */   MemberDefinition field;
/*     */   Statement body;
/*     */   
/*     */   InlineMethodExpression(long paramLong, Type paramType, MemberDefinition paramMemberDefinition, Statement paramStatement) {
/*  47 */     super(150, paramLong, paramType);
/*  48 */     this.field = paramMemberDefinition;
/*  49 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  55 */     this.body = this.body.inline(paramEnvironment, new Context(paramContext, this));
/*  56 */     if (this.body == null)
/*  57 */       return null; 
/*  58 */     if (this.body.op == 149) {
/*  59 */       Expression expression = ((InlineReturnStatement)this.body).expr;
/*  60 */       if (expression != null && this.type.isType(11)) {
/*  61 */         throw new CompilerError("value on inline-void return");
/*     */       }
/*  63 */       return expression;
/*     */     } 
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  74 */     return inline(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/*  81 */     InlineMethodExpression inlineMethodExpression = (InlineMethodExpression)clone();
/*  82 */     if (this.body != null) {
/*  83 */       inlineMethodExpression.body = this.body.copyInline(paramContext, true);
/*     */     }
/*  85 */     return inlineMethodExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  93 */     super.code(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  96 */     CodeContext codeContext = new CodeContext(paramContext, this);
/*  97 */     this.body.code(paramEnvironment, codeContext, paramAssembler);
/*  98 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 105 */     paramPrintStream.print("(" + opNames[this.op] + "\n");
/* 106 */     this.body.print(paramPrintStream, 1);
/* 107 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\InlineMethodExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */