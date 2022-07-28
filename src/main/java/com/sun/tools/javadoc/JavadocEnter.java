/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.tools.javac.code.Symbol;
/*    */ import com.sun.tools.javac.comp.Enter;
/*    */ import com.sun.tools.javac.tree.JCTree;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.JCDiagnostic;
/*    */ import com.sun.tools.javac.util.List;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavadocEnter
/*    */   extends Enter
/*    */ {
/*    */   final Messager messager;
/*    */   final DocEnv docenv;
/*    */   
/*    */   public static JavadocEnter instance0(Context paramContext) {
/* 51 */     Enter enter = (Enter)paramContext.get(enterKey);
/* 52 */     if (enter == null)
/* 53 */       enter = new JavadocEnter(paramContext); 
/* 54 */     return (JavadocEnter)enter;
/*    */   }
/*    */   
/*    */   public static void preRegister(Context paramContext) {
/* 58 */     paramContext.put(enterKey, new Context.Factory<Enter>() {
/*    */           public Enter make(Context param1Context) {
/* 60 */             return new JavadocEnter(param1Context);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   protected JavadocEnter(Context paramContext) {
/* 66 */     super(paramContext);
/* 67 */     this.messager = Messager.instance0(paramContext);
/* 68 */     this.docenv = DocEnv.instance(paramContext);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void main(List<JCTree.JCCompilationUnit> paramList) {
/* 77 */     int i = this.messager.nerrors;
/* 78 */     super.main(paramList);
/* 79 */     this.messager.nwarnings += this.messager.nerrors - i;
/* 80 */     this.messager.nerrors = i;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 85 */     super.visitTopLevel(paramJCCompilationUnit);
/* 86 */     if (paramJCCompilationUnit.sourcefile.isNameCompatible("package-info", JavaFileObject.Kind.SOURCE)) {
/* 87 */       this.docenv.makePackageDoc(paramJCCompilationUnit.packge, this.docenv.getTreePath(paramJCCompilationUnit));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/* 93 */     super.visitClassDef(paramJCClassDecl);
/* 94 */     if (paramJCClassDecl.sym == null)
/* 95 */       return;  if (paramJCClassDecl.sym.kind == 2 || paramJCClassDecl.sym.kind == 63) {
/* 96 */       Symbol.ClassSymbol classSymbol = paramJCClassDecl.sym;
/* 97 */       this.docenv.makeClassDoc(classSymbol, this.docenv.getTreePath(this.env.toplevel, paramJCClassDecl));
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void duplicateClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\JavadocEnter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */