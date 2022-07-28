/*     */ package com.sun.tools.javadoc;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class MethodDocImpl
/*     */   extends ExecutableMemberDocImpl
/*     */   implements MethodDoc
/*     */ {
/*     */   private String name;
/*     */   private String qualifiedName;
/*     */
/*     */   public MethodDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol) {
/*  57 */     super(paramDocEnv, paramMethodSymbol);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public MethodDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) {
/*  64 */     super(paramDocEnv, paramMethodSymbol, paramTreePath);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isMethod() {
/*  75 */     return true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isDefault() {
/*  82 */     return ((this.sym.flags() & 0x80000000000L) != 0L);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isAbstract() {
/*  89 */     return (Modifier.isAbstract(getModifiers()) && !isDefault());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Type returnType() {
/*  99 */     return TypeMaker.getType(this.env, this.sym.type.getReturnType(), false);
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
/*     */   public ClassDoc overriddenClass() {
/* 112 */     Type type = overriddenType();
/* 113 */     return (type != null) ? type.asClassDoc() : null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Type overriddenType() {
/* 122 */     if ((this.sym.flags() & 0x8L) != 0L) {
/* 123 */       return null;
/*     */     }
/*     */
/* 126 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.sym.owner;
/* 127 */     Type type = this.env.types.supertype(classSymbol.type);
/* 128 */     for (; type.hasTag(TypeTag.CLASS);
/* 129 */       type = this.env.types.supertype(type)) {
/* 130 */       Symbol.ClassSymbol classSymbol1 = (Symbol.ClassSymbol)type.tsym;
/* 131 */       for (Scope.Entry entry = membersOf(classSymbol1).lookup(this.sym.name); entry.scope != null; entry = entry.next()) {
/* 132 */         if (this.sym.overrides(entry.sym, (Symbol.TypeSymbol)classSymbol, this.env.types, true)) {
/* 133 */           return TypeMaker.getType(this.env, type);
/*     */         }
/*     */       }
/*     */     }
/* 137 */     return null;
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
/*     */   public MethodDoc overriddenMethod() {
/* 152 */     if ((this.sym.flags() & 0x8L) != 0L) {
/* 153 */       return null;
/*     */     }
/*     */
/*     */
/*     */
/* 158 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.sym.owner;
/* 159 */     Type type = this.env.types.supertype(classSymbol.type);
/* 160 */     for (; type.hasTag(TypeTag.CLASS);
/* 161 */       type = this.env.types.supertype(type)) {
/* 162 */       Symbol.ClassSymbol classSymbol1 = (Symbol.ClassSymbol)type.tsym;
/* 163 */       for (Scope.Entry entry = membersOf(classSymbol1).lookup(this.sym.name); entry.scope != null; entry = entry.next()) {
/* 164 */         if (this.sym.overrides(entry.sym, (Symbol.TypeSymbol)classSymbol, this.env.types, true)) {
/* 165 */           return this.env.getMethodDoc((Symbol.MethodSymbol)entry.sym);
/*     */         }
/*     */       }
/*     */     }
/* 169 */     return null;
/*     */   }
/*     */
/*     */
/*     */   private Scope membersOf(Symbol.ClassSymbol paramClassSymbol) {
/*     */     try {
/* 175 */       return paramClassSymbol.members();
/* 176 */     } catch (Symbol.CompletionFailure completionFailure) {
/*     */
/*     */
/*     */
/*     */
/* 181 */       return membersOf(paramClassSymbol);
/*     */     }
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
/*     */   public boolean overrides(MethodDoc paramMethodDoc) {
/* 197 */     Symbol.MethodSymbol methodSymbol = ((MethodDocImpl)paramMethodDoc).sym;
/* 198 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.sym.owner;
/*     */
/* 200 */     return (this.sym.name == methodSymbol.name && this.sym != methodSymbol &&
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 207 */       !this.sym.isStatic() && this.env.types
/*     */
/*     */
/*     */
/* 211 */       .asSuper(classSymbol.type, methodSymbol.owner) != null && this.sym
/*     */
/*     */
/* 214 */       .overrides((Symbol)methodSymbol, (Symbol.TypeSymbol)classSymbol, this.env.types, false));
/*     */   }
/*     */
/*     */
/*     */   public String name() {
/* 219 */     if (this.name == null) {
/* 220 */       this.name = this.sym.name.toString();
/*     */     }
/* 222 */     return this.name;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String qualifiedName() {
/* 228 */     if (this.qualifiedName == null) {
/* 229 */       this.qualifiedName = this.sym.enclClass().getQualifiedName() + "." + this.sym.name;
/*     */     }
/* 231 */     return this.qualifiedName;
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
/*     */   public String toString() {
/* 243 */     return this.sym.enclClass().getQualifiedName() + "." +
/* 244 */       typeParametersString() + name() + signature();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\MethodDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
