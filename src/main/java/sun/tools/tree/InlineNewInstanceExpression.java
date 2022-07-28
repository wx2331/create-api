/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
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
/*     */ 
/*     */ 
/*     */ public class InlineNewInstanceExpression
/*     */   extends Expression
/*     */ {
/*     */   MemberDefinition field;
/*     */   Statement body;
/*     */   
/*     */   InlineNewInstanceExpression(long paramLong, Type paramType, MemberDefinition paramMemberDefinition, Statement paramStatement) {
/*  48 */     super(151, paramLong, paramType);
/*  49 */     this.field = paramMemberDefinition;
/*  50 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  56 */     return inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  59 */     if (this.body != null) {
/*  60 */       LocalMember localMember = this.field.getArguments().elementAt(0);
/*  61 */       Context context = new Context(paramContext, this);
/*  62 */       context.declare(paramEnvironment, localMember);
/*  63 */       this.body = this.body.inline(paramEnvironment, context);
/*     */     } 
/*  65 */     if (this.body != null && this.body.op == 149) {
/*  66 */       this.body = null;
/*     */     }
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/*  75 */     InlineNewInstanceExpression inlineNewInstanceExpression = (InlineNewInstanceExpression)clone();
/*  76 */     inlineNewInstanceExpression.body = this.body.copyInline(paramContext, true);
/*  77 */     return inlineNewInstanceExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  84 */     codeCommon(paramEnvironment, paramContext, paramAssembler, false);
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  87 */     codeCommon(paramEnvironment, paramContext, paramAssembler, true);
/*     */   }
/*     */   
/*     */   private void codeCommon(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/*  91 */     paramAssembler.add(this.where, 187, this.field.getClassDeclaration());
/*  92 */     if (this.body != null) {
/*  93 */       LocalMember localMember = this.field.getArguments().elementAt(0);
/*  94 */       CodeContext codeContext = new CodeContext(paramContext, this);
/*  95 */       codeContext.declare(paramEnvironment, localMember);
/*  96 */       paramAssembler.add(this.where, 58, new Integer(localMember.number));
/*  97 */       this.body.code(paramEnvironment, codeContext, paramAssembler);
/*  98 */       paramAssembler.add((Instruction)codeContext.breakLabel);
/*  99 */       if (paramBoolean) {
/* 100 */         paramAssembler.add(this.where, 25, new Integer(localMember.number));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 109 */     LocalMember localMember = this.field.getArguments().elementAt(0);
/* 110 */     paramPrintStream.println("(" + opNames[this.op] + "#" + localMember.hashCode() + "=" + this.field.hashCode());
/* 111 */     if (this.body != null) {
/* 112 */       this.body.print(paramPrintStream, 1);
/*     */     } else {
/* 114 */       paramPrintStream.print("<empty>");
/*     */     } 
/* 116 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\InlineNewInstanceExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */