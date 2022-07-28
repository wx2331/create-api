/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.comp.MemberEnter;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavadocMemberEnter
/*     */   extends MemberEnter
/*     */ {
/*     */   final DocEnv docenv;
/*     */   
/*     */   public static JavadocMemberEnter instance0(Context paramContext) {
/*  52 */     MemberEnter memberEnter = (MemberEnter)paramContext.get(memberEnterKey);
/*  53 */     if (memberEnter == null)
/*  54 */       memberEnter = new JavadocMemberEnter(paramContext); 
/*  55 */     return (JavadocMemberEnter)memberEnter;
/*     */   }
/*     */   
/*     */   public static void preRegister(Context paramContext) {
/*  59 */     paramContext.put(memberEnterKey, new Context.Factory<MemberEnter>() {
/*     */           public MemberEnter make(Context param1Context) {
/*  61 */             return new JavadocMemberEnter(param1Context);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavadocMemberEnter(Context paramContext) {
/*  69 */     super(paramContext);
/*  70 */     this.docenv = DocEnv.instance(paramContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  75 */     super.visitMethodDef(paramJCMethodDecl);
/*  76 */     Symbol.MethodSymbol methodSymbol = paramJCMethodDecl.sym;
/*  77 */     if (methodSymbol == null || methodSymbol.kind != 16)
/*  78 */       return;  TreePath treePath = this.docenv.getTreePath(this.env.toplevel, this.env.enclClass, (JCTree)paramJCMethodDecl);
/*  79 */     if (methodSymbol.isConstructor()) {
/*  80 */       this.docenv.makeConstructorDoc(methodSymbol, treePath);
/*  81 */     } else if (isAnnotationTypeElement(methodSymbol)) {
/*  82 */       this.docenv.makeAnnotationTypeElementDoc(methodSymbol, treePath);
/*     */     } else {
/*  84 */       this.docenv.makeMethodDoc(methodSymbol, treePath);
/*     */     } 
/*     */     
/*  87 */     paramJCMethodDecl.body = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  92 */     if (paramJCVariableDecl.init != null) {
/*  93 */       boolean bool = ((paramJCVariableDecl.mods.flags & 0x10L) != 0L || (this.env.enclClass.mods.flags & 0x200L) != 0L) ? true : false;
/*     */       
/*  95 */       if (!bool || containsNonConstantExpression(paramJCVariableDecl.init))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 100 */         paramJCVariableDecl.init = null;
/*     */       }
/*     */     } 
/* 103 */     super.visitVarDef(paramJCVariableDecl);
/* 104 */     if (paramJCVariableDecl.sym != null && paramJCVariableDecl.sym.kind == 4 && 
/*     */       
/* 106 */       !isParameter(paramJCVariableDecl.sym)) {
/* 107 */       this.docenv.makeFieldDoc(paramJCVariableDecl.sym, this.docenv.getTreePath(this.env.toplevel, this.env.enclClass, (JCTree)paramJCVariableDecl));
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isAnnotationTypeElement(Symbol.MethodSymbol paramMethodSymbol) {
/* 112 */     return ClassDocImpl.isAnnotationType(paramMethodSymbol.enclClass());
/*     */   }
/*     */   
/*     */   private static boolean isParameter(Symbol.VarSymbol paramVarSymbol) {
/* 116 */     return ((paramVarSymbol.flags() & 0x200000000L) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean containsNonConstantExpression(JCTree.JCExpression paramJCExpression) {
/* 125 */     return (new MaybeConstantExpressionScanner()).containsNonConstantExpression(paramJCExpression);
/*     */   }
/*     */   
/*     */   private static class MaybeConstantExpressionScanner extends JCTree.Visitor {
/*     */     boolean maybeConstantExpr;
/*     */     
/*     */     private MaybeConstantExpressionScanner() {
/* 132 */       this.maybeConstantExpr = true;
/*     */     }
/*     */     public boolean containsNonConstantExpression(JCTree.JCExpression param1JCExpression) {
/* 135 */       scan((JCTree)param1JCExpression);
/* 136 */       return !this.maybeConstantExpr;
/*     */     }
/*     */ 
/*     */     
/*     */     public void scan(JCTree param1JCTree) {
/* 141 */       if (this.maybeConstantExpr && param1JCTree != null) {
/* 142 */         param1JCTree.accept(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitTree(JCTree param1JCTree) {
/* 148 */       this.maybeConstantExpr = false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitBinary(JCTree.JCBinary param1JCBinary) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/javadoc/JavadocMemberEnter$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*     */       //   3: aload_1
/*     */       //   4: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: tableswitch default -> 103, 1 -> 100, 2 -> 100, 3 -> 100, 4 -> 100, 5 -> 100, 6 -> 100, 7 -> 100, 8 -> 100, 9 -> 100, 10 -> 100, 11 -> 100, 12 -> 100, 13 -> 100, 14 -> 100, 15 -> 100, 16 -> 100, 17 -> 100, 18 -> 100, 19 -> 100
/*     */       //   100: goto -> 108
/*     */       //   103: aload_0
/*     */       //   104: iconst_0
/*     */       //   105: putfield maybeConstantExpr : Z
/*     */       //   108: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #153	-> 0
/*     */       //   #161	-> 100
/*     */       //   #163	-> 103
/*     */       //   #165	-> 108
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitConditional(JCTree.JCConditional param1JCConditional) {
/* 169 */       scan((JCTree)param1JCConditional.cond);
/* 170 */       scan((JCTree)param1JCConditional.truepart);
/* 171 */       scan((JCTree)param1JCConditional.falsepart);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitIdent(JCTree.JCIdent param1JCIdent) {}
/*     */ 
/*     */     
/*     */     public void visitLiteral(JCTree.JCLiteral param1JCLiteral) {}
/*     */ 
/*     */     
/*     */     public void visitParens(JCTree.JCParens param1JCParens) {
/* 182 */       scan((JCTree)param1JCParens.expr);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 187 */       scan((JCTree)param1JCFieldAccess.selected);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitTypeCast(JCTree.JCTypeCast param1JCTypeCast) {
/* 192 */       scan(param1JCTypeCast.clazz);
/* 193 */       scan((JCTree)param1JCTypeCast.expr);
/*     */     }
/*     */     
/*     */     public void visitTypeIdent(JCTree.JCPrimitiveTypeTree param1JCPrimitiveTypeTree) {}
/*     */     
/*     */     public void visitUnary(JCTree.JCUnary param1JCUnary) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/javadoc/JavadocMemberEnter$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*     */       //   3: aload_1
/*     */       //   4: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: tableswitch default -> 43, 20 -> 40, 21 -> 40, 22 -> 40, 23 -> 40
/*     */       //   40: goto -> 48
/*     */       //   43: aload_0
/*     */       //   44: iconst_0
/*     */       //   45: putfield maybeConstantExpr : Z
/*     */       //   48: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       //   #203	-> 40
/*     */       //   #205	-> 43
/*     */       //   #207	-> 48
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\JavadocMemberEnter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */