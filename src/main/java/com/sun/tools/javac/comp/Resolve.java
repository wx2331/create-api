/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.source.tree.LambdaExpressionTree;
/*      */ import com.sun.source.tree.MemberReferenceTree;
/*      */ import com.sun.tools.javac.api.Formattable;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Kinds;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.FatalError;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.lang.model.element.ElementVisitor;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Resolve
/*      */ {
/*   81 */   protected static final Context.Key<Resolve> resolveKey = new Context.Key();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Names names;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Log log;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Symtab syms;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Attr attr;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   DeferredAttr deferredAttr;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Check chk;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Infer infer;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   ClassReader reader;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   TreeInfo treeinfo;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Types types;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCDiagnostic.Factory diags;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public final boolean boxingEnabled;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public final boolean varargsEnabled;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public final boolean allowMethodHandles;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public final boolean allowFunctionalInterfaceMostSpecific;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public final boolean checkVarargsAccessAfterResolution;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final boolean debugResolve;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final boolean compactMethodDiags;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   final EnumSet<VerboseResolutionMode> verboseResolutionMode;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Scope polymorphicSignatureScope;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final SymbolNotFoundError varNotFound;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final SymbolNotFoundError methodNotFound;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final SymbolNotFoundError methodWithCorrectStaticnessNotFound;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final SymbolNotFoundError typeNotFound;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Types.SimpleVisitor<Void, Env<AttrContext>> accessibilityChecker;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   MethodCheck nilMethodCheck;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   MethodCheck arityMethodCheck;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   MethodCheck resolveMethodCheck;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final InapplicableMethodException inapplicableMethodException;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Warner noteWarner;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   LogResolveHelper basicLogResolveHelper;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   LogResolveHelper methodLogResolveHelper;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private final Formattable.LocalizedString noArgs;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   final List<MethodResolutionPhase> methodResolutionSteps;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   MethodResolutionContext currentResolutionContext;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Resolve(Context paramContext) {
/*  456 */     this.accessibilityChecker = new Types.SimpleVisitor<Void, Env<AttrContext>>()
/*      */       {
/*      */         void visit(List<Type> param1List, Env<AttrContext> param1Env)
/*      */         {
/*  460 */           for (Type type : param1List) {
/*  461 */             visit(type, param1Env);
/*      */           }
/*      */         }
/*      */
/*      */         public Void visitType(Type param1Type, Env<AttrContext> param1Env) {
/*  466 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Void visitArrayType(Type.ArrayType param1ArrayType, Env<AttrContext> param1Env) {
/*  471 */           visit(param1ArrayType.elemtype, param1Env);
/*  472 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Void visitClassType(Type.ClassType param1ClassType, Env<AttrContext> param1Env) {
/*  477 */           visit(param1ClassType.getTypeArguments(), param1Env);
/*  478 */           if (!Resolve.this.isAccessible(param1Env, (Type)param1ClassType, true)) {
/*  479 */             Resolve.this.accessBase(new AccessError((Symbol)param1ClassType.tsym), param1Env.tree.pos(), (Symbol)param1Env.enclClass.sym, (Type)param1ClassType, param1ClassType.tsym.name, true);
/*      */           }
/*  481 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Void visitWildcardType(Type.WildcardType param1WildcardType, Env<AttrContext> param1Env) {
/*  486 */           visit(param1WildcardType.type, param1Env);
/*  487 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Void visitMethodType(Type.MethodType param1MethodType, Env<AttrContext> param1Env) {
/*  492 */           visit(param1MethodType.getParameterTypes(), param1Env);
/*  493 */           visit(param1MethodType.getReturnType(), param1Env);
/*  494 */           visit(param1MethodType.getThrownTypes(), param1Env);
/*  495 */           return null;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  696 */     this.nilMethodCheck = new MethodCheck()
/*      */       {
/*      */         public void argumentsAcceptable(Env<AttrContext> param1Env, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, List<Type> param1List1, List<Type> param1List2, Warner param1Warner) {}
/*      */
/*      */
/*      */         public MethodCheck mostSpecificCheck(List<Type> param1List, boolean param1Boolean) {
/*  702 */           return this;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  792 */     this.arityMethodCheck = new AbstractMethodCheck()
/*      */       {
/*      */         void checkArg(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, boolean param1Boolean, Type param1Type1, Type param1Type2, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner) {}
/*      */
/*      */
/*      */
/*      */
/*      */         public String toString() {
/*  800 */           return "arityMethodCheck";
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  830 */     this.resolveMethodCheck = new AbstractMethodCheck()
/*      */       {
/*      */         void checkArg(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, boolean param1Boolean, Type param1Type1, Type param1Type2, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner)
/*      */         {
/*  834 */           Attr.ResultInfo resultInfo = methodCheckResult(param1Boolean, param1Type2, param1DeferredAttrContext, param1Warner);
/*  835 */           resultInfo.check(param1DiagnosticPosition, param1Type1);
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         public void argumentsAcceptable(Env<AttrContext> param1Env, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, List<Type> param1List1, List<Type> param1List2, Warner param1Warner) {
/*  844 */           super.argumentsAcceptable(param1Env, param1DeferredAttrContext, param1List1, param1List2, param1Warner);
/*      */
/*  846 */           if (param1DeferredAttrContext.phase.isVarargsRequired() && (
/*  847 */             param1DeferredAttrContext.mode == DeferredAttr.AttrMode.CHECK || !Resolve.this.checkVarargsAccessAfterResolution)) {
/*  848 */             varargsAccessible(param1Env, Resolve.this.types.elemtype((Type)param1List2.last()), param1DeferredAttrContext.inferenceContext);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private void varargsAccessible(final Env<AttrContext> env, final Type t, Infer.InferenceContext param1InferenceContext) {
/*  859 */           if (param1InferenceContext.free(t)) {
/*  860 */             param1InferenceContext.addFreeTypeListener(List.of(t), new Infer.FreeTypeListener()
/*      */                 {
/*      */                   public void typesInferred(Infer.InferenceContext param2InferenceContext) {
/*  863 */                     Resolve.null.this.varargsAccessible(env, param2InferenceContext.asInstType(t), param2InferenceContext);
/*      */                   }
/*      */                 });
/*      */           }
/*  867 */           else if (!Resolve.this.isAccessible(env, Resolve.this.types.erasure(t))) {
/*  868 */             Symbol.ClassSymbol classSymbol = env.enclClass.sym;
/*  869 */             reportMC((JCDiagnostic.DiagnosticPosition)env.tree, MethodCheckDiag.INACCESSIBLE_VARARGS, param1InferenceContext, new Object[] { t, Kinds.kindName((Symbol)classSymbol), classSymbol });
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */         private Attr.ResultInfo methodCheckResult(final boolean varargsCheck, Type param1Type, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner) {
/*  876 */           MethodCheckContext methodCheckContext = new MethodCheckContext(!param1DeferredAttrContext.phase.isBoxingRequired(), param1DeferredAttrContext, param1Warner) {
/*  877 */               MethodCheckDiag methodDiag = varargsCheck ? MethodCheckDiag.VARARG_MISMATCH : MethodCheckDiag.ARG_MISMATCH;
/*      */
/*      */
/*      */
/*      */               public void report(JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, JCDiagnostic param2JCDiagnostic) {
/*  882 */                 Resolve.null.this.reportMC(param2DiagnosticPosition, this.methodDiag, this.deferredAttrContext.inferenceContext, new Object[] { param2JCDiagnostic });
/*      */               }
/*      */             };
/*  885 */           return new MethodResultInfo(param1Type, methodCheckContext);
/*      */         }
/*      */
/*      */
/*      */         public MethodCheck mostSpecificCheck(List<Type> param1List, boolean param1Boolean) {
/*  890 */           return new MostSpecificCheck(param1Boolean, param1List);
/*      */         }
/*      */
/*      */
/*      */         public String toString() {
/*  895 */           return "resolveMethodCheck";
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1413 */     this.noteWarner = new Warner();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2280 */     this.basicLogResolveHelper = new LogResolveHelper() {
/*      */         public boolean resolveDiagnosticNeeded(Type param1Type, List<Type> param1List1, List<Type> param1List2) {
/* 2282 */           return !param1Type.isErroneous();
/*      */         }
/*      */         public List<Type> getArgumentTypes(ResolveError param1ResolveError, Symbol param1Symbol, Name param1Name, List<Type> param1List) {
/* 2285 */           return param1List;
/*      */         }
/*      */       };
/*      */
/* 2289 */     this.methodLogResolveHelper = new LogResolveHelper() {
/*      */         public boolean resolveDiagnosticNeeded(Type param1Type, List<Type> param1List1, List<Type> param1List2) {
/* 2291 */           return (!param1Type.isErroneous() &&
/* 2292 */             !Type.isErroneous(param1List1) && (param1List2 == null ||
/* 2293 */             !Type.isErroneous(param1List2)));
/*      */         }
/*      */         public List<Type> getArgumentTypes(ResolveError param1ResolveError, Symbol param1Symbol, Name param1Name, List<Type> param1List) {
/* 2296 */           return Resolve.this.syms.operatorNames.contains(param1Name) ? param1List :
/*      */
/* 2298 */             Type.map(param1List, new ResolveDeferredRecoveryMap(DeferredAttr.AttrMode.SPECULATIVE, param1Symbol, Resolve.this.currentResolutionContext.step));
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3518 */     this.noArgs = new Formattable.LocalizedString("compiler.misc.no.args");
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 4311 */     this.methodResolutionSteps = List.of(MethodResolutionPhase.BASIC, MethodResolutionPhase.BOX, MethodResolutionPhase.VARARITY);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 4399 */     this.currentResolutionContext = null;
/*      */     paramContext.put(resolveKey, this);
/*      */     this.syms = Symtab.instance(paramContext);
/*      */     this.varNotFound = new SymbolNotFoundError(133);
/*      */     this.methodNotFound = new SymbolNotFoundError(136);
/*      */     this.methodWithCorrectStaticnessNotFound = new SymbolNotFoundError(138, "method found has incorrect staticness");
/*      */     this.typeNotFound = new SymbolNotFoundError(137);
/*      */     this.names = Names.instance(paramContext);
/*      */     this.log = Log.instance(paramContext);
/*      */     this.attr = Attr.instance(paramContext);
/*      */     this.deferredAttr = DeferredAttr.instance(paramContext);
/*      */     this.chk = Check.instance(paramContext);
/*      */     this.infer = Infer.instance(paramContext);
/*      */     this.reader = ClassReader.instance(paramContext);
/*      */     this.treeinfo = TreeInfo.instance(paramContext);
/*      */     this.types = Types.instance(paramContext);
/*      */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*      */     Source source = Source.instance(paramContext);
/*      */     this.boxingEnabled = source.allowBoxing();
/*      */     this.varargsEnabled = source.allowVarargs();
/*      */     Options options = Options.instance(paramContext);
/*      */     this.debugResolve = options.isSet("debugresolve");
/*      */     this.compactMethodDiags = (options.isSet(Option.XDIAGS, "compact") || (options.isUnset(Option.XDIAGS) && options.isUnset("rawDiagnostics")));
/*      */     this.verboseResolutionMode = VerboseResolutionMode.getVerboseResolutionMode(options);
/*      */     Target target = Target.instance(paramContext);
/*      */     this.allowMethodHandles = target.hasMethodHandles();
/*      */     this.allowFunctionalInterfaceMostSpecific = source.allowFunctionalInterfaceMostSpecific();
/*      */     this.checkVarargsAccessAfterResolution = source.allowPostApplicabilityVarargsAccessCheck();
/*      */     this.polymorphicSignatureScope = new Scope((Symbol)this.syms.noSymbol);
/*      */     this.inapplicableMethodException = new InapplicableMethodException(this.diags);
/*      */   }
/*      */
/*      */   public static Resolve instance(Context paramContext) {
/*      */     Resolve resolve = (Resolve)paramContext.get(resolveKey);
/*      */     if (resolve == null)
/*      */       resolve = new Resolve(paramContext);
/*      */     return resolve;
/*      */   }
/*      */
/*      */   enum VerboseResolutionMode {
/*      */     SUCCESS("success"),
/*      */     FAILURE("failure"),
/*      */     APPLICABLE("applicable"),
/*      */     INAPPLICABLE("inapplicable"),
/*      */     DEFERRED_INST("deferred-inference"),
/*      */     PREDEF("predef"),
/*      */     OBJECT_INIT("object-init"),
/*      */     INTERNAL("internal");
/*      */     final String opt;
/*      */
/*      */     VerboseResolutionMode(String param1String1) {
/*      */       this.opt = param1String1;
/*      */     }
/*      */
/*      */     static EnumSet<VerboseResolutionMode> getVerboseResolutionMode(Options param1Options) {
/*      */       String str = param1Options.get("verboseResolution");
/*      */       EnumSet<VerboseResolutionMode> enumSet = EnumSet.noneOf(VerboseResolutionMode.class);
/*      */       if (str == null)
/*      */         return enumSet;
/*      */       if (str.contains("all"))
/*      */         enumSet = EnumSet.allOf(VerboseResolutionMode.class);
/*      */       List<String> list = Arrays.asList(str.split(","));
/*      */       for (VerboseResolutionMode verboseResolutionMode : values()) {
/*      */         if (list.contains(verboseResolutionMode.opt)) {
/*      */           enumSet.add(verboseResolutionMode);
/*      */         } else if (list.contains("-" + verboseResolutionMode.opt)) {
/*      */           enumSet.remove(verboseResolutionMode);
/*      */         }
/*      */       }
/*      */       return enumSet;
/*      */     }
/*      */   }
/*      */
/*      */   void reportVerboseResolutionDiagnostic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Name paramName, Type paramType, List<Type> paramList1, List<Type> paramList2, Symbol paramSymbol) {
/*      */     boolean bool = (paramSymbol.kind < 128) ? true : false;
/*      */     if (bool && !this.verboseResolutionMode.contains(VerboseResolutionMode.SUCCESS))
/*      */       return;
/*      */     if (!bool && !this.verboseResolutionMode.contains(VerboseResolutionMode.FAILURE))
/*      */       return;
/*      */     if (paramSymbol.name == this.names.init && paramSymbol.owner == this.syms.objectType.tsym && !this.verboseResolutionMode.contains(VerboseResolutionMode.OBJECT_INIT))
/*      */       return;
/*      */     if (paramType == this.syms.predefClass.type && !this.verboseResolutionMode.contains(VerboseResolutionMode.PREDEF))
/*      */       return;
/*      */     if (this.currentResolutionContext.internalResolution && !this.verboseResolutionMode.contains(VerboseResolutionMode.INTERNAL))
/*      */       return;
/*      */     byte b = 0;
/*      */     byte b1 = -1;
/*      */     ListBuffer listBuffer = new ListBuffer();
/*      */     for (MethodResolutionContext.Candidate candidate : this.currentResolutionContext.candidates) {
/*      */       if (this.currentResolutionContext.step != candidate.step || (candidate.isApplicable() && !this.verboseResolutionMode.contains(VerboseResolutionMode.APPLICABLE)) || (!candidate.isApplicable() && !this.verboseResolutionMode.contains(VerboseResolutionMode.INAPPLICABLE)))
/*      */         continue;
/*      */       listBuffer.append(candidate.isApplicable() ? getVerboseApplicableCandidateDiag(b, candidate.sym, candidate.mtype) : getVerboseInapplicableCandidateDiag(b, candidate.sym, candidate.details));
/*      */       if (candidate.sym == paramSymbol)
/*      */         b1 = b;
/*      */       b++;
/*      */     }
/*      */     String str = bool ? "verbose.resolve.multi" : "verbose.resolve.multi.1";
/*      */     this.deferredAttr.getClass();
/*      */     List<Type> list = Type.map(paramList1, new DeferredAttr.RecoveryDeferredTypeMap(this.deferredAttr, DeferredAttr.AttrMode.SPECULATIVE, paramSymbol, this.currentResolutionContext.step));
/*      */     JCDiagnostic jCDiagnostic = this.diags.note(this.log.currentSource(), paramDiagnosticPosition, str, new Object[] { paramName, paramType.tsym, Integer.valueOf(b1), this.currentResolutionContext.step, methodArguments(list), methodArguments(paramList2) });
/*      */     JCDiagnostic.MultilineDiagnostic multilineDiagnostic = new JCDiagnostic.MultilineDiagnostic(jCDiagnostic, listBuffer.toList());
/*      */     this.log.report((JCDiagnostic)multilineDiagnostic);
/*      */   }
/*      */
/*      */   JCDiagnostic getVerboseApplicableCandidateDiag(int paramInt, Symbol paramSymbol, Type paramType) {
/*      */     JCDiagnostic jCDiagnostic = null;
/*      */     if (paramSymbol.type.hasTag(TypeTag.FORALL))
/*      */       jCDiagnostic = this.diags.fragment("partial.inst.sig", new Object[] { paramType });
/*      */     String str = (jCDiagnostic == null) ? "applicable.method.found" : "applicable.method.found.1";
/*      */     return this.diags.fragment(str, new Object[] { Integer.valueOf(paramInt), paramSymbol, jCDiagnostic });
/*      */   }
/*      */
/*      */   JCDiagnostic getVerboseInapplicableCandidateDiag(int paramInt, Symbol paramSymbol, JCDiagnostic paramJCDiagnostic) {
/*      */     return this.diags.fragment("not.applicable.method.found", new Object[] { Integer.valueOf(paramInt), paramSymbol, paramJCDiagnostic });
/*      */   }
/*      */
/*      */   protected static boolean isStatic(Env<AttrContext> paramEnv) {
/*      */     return (paramEnv.outer != null && ((AttrContext)paramEnv.info).staticLevel > ((AttrContext)paramEnv.outer.info).staticLevel);
/*      */   }
/*      */
/*      */   static boolean isInitializer(Env<AttrContext> paramEnv) {
/*      */     Symbol symbol = ((AttrContext)paramEnv.info).scope.owner;
/*      */     return (symbol.isConstructor() || (symbol.owner.kind == 2 && (symbol.kind == 4 || (symbol.kind == 16 && (symbol.flags() & 0x100000L) != 0L)) && (symbol.flags() & 0x8L) == 0L));
/*      */   }
/*      */
/*      */   public boolean isAccessible(Env<AttrContext> paramEnv, Symbol.TypeSymbol paramTypeSymbol) {
/*      */     return isAccessible(paramEnv, paramTypeSymbol, false);
/*      */   }
/*      */
/*      */   public boolean isAccessible(Env<AttrContext> paramEnv, Symbol.TypeSymbol paramTypeSymbol, boolean paramBoolean) {
/*      */     boolean bool = false;
/*      */     switch ((short)(int)(paramTypeSymbol.flags() & 0x7L)) {
/*      */       case 2:
/*      */         bool = (paramEnv.enclClass.sym.outermostClass() == paramTypeSymbol.owner.outermostClass()) ? true : false;
/*      */         break;
/*      */       case 0:
/*      */         bool = (paramEnv.toplevel.packge == paramTypeSymbol.owner || paramEnv.toplevel.packge == paramTypeSymbol.packge() || (paramEnv.enclMethod != null && (paramEnv.enclMethod.mods.flags & 0x20000000L) != 0L)) ? true : false;
/*      */         break;
/*      */       default:
/*      */         bool = true;
/*      */         break;
/*      */       case 4:
/*      */         bool = (paramEnv.toplevel.packge == paramTypeSymbol.owner || paramEnv.toplevel.packge == paramTypeSymbol.packge() || isInnerSubClass(paramEnv.enclClass.sym, paramTypeSymbol.owner)) ? true : false;
/*      */         break;
/*      */     }
/*      */     return (!paramBoolean || paramTypeSymbol.type.getEnclosingType() == Type.noType) ? bool : ((bool && isAccessible(paramEnv, paramTypeSymbol.type.getEnclosingType(), paramBoolean)));
/*      */   }
/*      */
/*      */   private boolean isInnerSubClass(Symbol.ClassSymbol paramClassSymbol, Symbol paramSymbol) {
/*      */     while (paramClassSymbol != null && !paramClassSymbol.isSubClass(paramSymbol, this.types))
/*      */       paramClassSymbol = paramClassSymbol.owner.enclClass();
/*      */     return (paramClassSymbol != null);
/*      */   }
/*      */
/*      */   boolean isAccessible(Env<AttrContext> paramEnv, Type paramType) {
/*      */     return isAccessible(paramEnv, paramType, false);
/*      */   }
/*      */
/*      */   boolean isAccessible(Env<AttrContext> paramEnv, Type paramType, boolean paramBoolean) {
/*      */     return paramType.hasTag(TypeTag.ARRAY) ? isAccessible(paramEnv, this.types.cvarUpperBound(this.types.elemtype(paramType))) : isAccessible(paramEnv, paramType.tsym, paramBoolean);
/*      */   }
/*      */
/*      */   public boolean isAccessible(Env<AttrContext> paramEnv, Type paramType, Symbol paramSymbol) {
/*      */     return isAccessible(paramEnv, paramType, paramSymbol, false);
/*      */   }
/*      */
/*      */   public boolean isAccessible(Env<AttrContext> paramEnv, Type paramType, Symbol paramSymbol, boolean paramBoolean) {
/*      */     if (paramSymbol.name == this.names.init && paramSymbol.owner != paramType.tsym)
/*      */       return false;
/*      */     switch ((short)(int)(paramSymbol.flags() & 0x7L)) {
/*      */       case 2:
/*      */         return ((paramEnv.enclClass.sym == paramSymbol.owner || paramEnv.enclClass.sym.outermostClass() == paramSymbol.owner.outermostClass()) && paramSymbol.isInheritedIn((Symbol)paramType.tsym, this.types));
/*      */       case 0:
/*      */         return ((paramEnv.toplevel.packge == paramSymbol.owner.owner || paramEnv.toplevel.packge == paramSymbol.packge()) && isAccessible(paramEnv, paramType, paramBoolean) && paramSymbol.isInheritedIn((Symbol)paramType.tsym, this.types) && notOverriddenIn(paramType, paramSymbol));
/*      */       case 4:
/*      */         return ((paramEnv.toplevel.packge == paramSymbol.owner.owner || paramEnv.toplevel.packge == paramSymbol.packge() || isProtectedAccessible(paramSymbol, paramEnv.enclClass.sym, paramType) || (((AttrContext)paramEnv.info).selectSuper && (paramSymbol.flags() & 0x8L) == 0L && paramSymbol.kind != 2)) && isAccessible(paramEnv, paramType, paramBoolean) && notOverriddenIn(paramType, paramSymbol));
/*      */     }
/*      */     return (isAccessible(paramEnv, paramType, paramBoolean) && notOverriddenIn(paramType, paramSymbol));
/*      */   }
/*      */
/*      */   private boolean notOverriddenIn(Type paramType, Symbol paramSymbol) {
/*      */     if (paramSymbol.kind != 16 || paramSymbol.isConstructor() || paramSymbol.isStatic())
/*      */       return true;
/*      */     Symbol.MethodSymbol methodSymbol = ((Symbol.MethodSymbol)paramSymbol).implementation(paramType.tsym, this.types, true);
/*      */     return (methodSymbol == null || methodSymbol == paramSymbol || paramSymbol.owner == ((Symbol)methodSymbol).owner || !this.types.isSubSignature(this.types.memberType(paramType, (Symbol)methodSymbol), this.types.memberType(paramType, paramSymbol)));
/*      */   }
/*      */
/*      */   private boolean isProtectedAccessible(Symbol paramSymbol, Symbol.ClassSymbol paramClassSymbol, Type paramType) {
/*      */     Type type = paramType.hasTag(TypeTag.TYPEVAR) ? paramType.getUpperBound() : paramType;
/*      */     while (paramClassSymbol != null && (!paramClassSymbol.isSubClass(paramSymbol.owner, this.types) || (paramClassSymbol.flags() & 0x200L) != 0L || ((paramSymbol.flags() & 0x8L) == 0L && paramSymbol.kind != 2 && !type.tsym.isSubClass((Symbol)paramClassSymbol, this.types))))
/*      */       paramClassSymbol = paramClassSymbol.owner.enclClass();
/*      */     return (paramClassSymbol != null);
/*      */   }
/*      */
/*      */   void checkAccessibleType(Env<AttrContext> paramEnv, Type paramType) {
/*      */     this.accessibilityChecker.visit(paramType, paramEnv);
/*      */   }
/*      */
/*      */   Type rawInstantiate(Env<AttrContext> paramEnv, Type paramType, Symbol paramSymbol, Attr.ResultInfo paramResultInfo, List<Type> paramList1, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2, Warner paramWarner) throws Infer.InferenceException {
/*      */     Type type = this.types.memberType(paramType, paramSymbol);
/*      */     List<Type> list1 = List.nil();
/*      */     if (paramList2 == null)
/*      */       paramList2 = List.nil();
/*      */     if (type.hasTag(TypeTag.FORALL) || !paramList2.nonEmpty())
/*      */       if (type.hasTag(TypeTag.FORALL) && paramList2.nonEmpty()) {
/*      */         Type.ForAll forAll = (Type.ForAll)type;
/*      */         if (paramList2.length() != forAll.tvars.length())
/*      */           throw this.inapplicableMethodException.setMessage("arg.length.mismatch");
/*      */         List list = forAll.tvars;
/*      */         List<Type> list3 = paramList2;
/*      */         while (list.nonEmpty() && list3.nonEmpty()) {
/*      */           List list4 = this.types.subst(this.types.getBounds((Type.TypeVar)list.head), forAll.tvars, paramList2);
/*      */           for (; list4.nonEmpty(); list4 = list4.tail) {
/*      */             if (!this.types.isSubtypeUnchecked((Type)list3.head, (Type)list4.head, paramWarner))
/*      */               throw this.inapplicableMethodException.setMessage("explicit.param.do.not.conform.to.bounds", new Object[] { list3.head, list4 });
/*      */           }
/*      */           list = list.tail;
/*      */           list3 = list3.tail;
/*      */         }
/*      */         type = this.types.subst(forAll.qtype, forAll.tvars, paramList2);
/*      */       } else if (type.hasTag(TypeTag.FORALL)) {
/*      */         Type.ForAll forAll = (Type.ForAll)type;
/*      */         List list = this.types.newInstances(forAll.tvars);
/*      */         list1 = list1.appendList(list);
/*      */         type = this.types.subst(forAll.qtype, forAll.tvars, list);
/*      */       }
/*      */     boolean bool = (list1.tail != null) ? true : false;
/*      */     List<Type> list2 = paramList1;
/*      */     for (; list2.tail != null && !bool; list2 = list2.tail) {
/*      */       if (((Type)list2.head).hasTag(TypeTag.FORALL))
/*      */         bool = true;
/*      */     }
/*      */     if (bool)
/*      */       return this.infer.instantiateMethod(paramEnv, list1, (Type.MethodType)type, paramResultInfo, (Symbol.MethodSymbol)paramSymbol, paramList1, paramBoolean1, paramBoolean2, this.currentResolutionContext, paramWarner);
/*      */     DeferredAttr.DeferredAttrContext deferredAttrContext = this.currentResolutionContext.deferredAttrContext(paramSymbol, this.infer.emptyContext, paramResultInfo, paramWarner);
/*      */     this.currentResolutionContext.methodCheck.argumentsAcceptable(paramEnv, deferredAttrContext, paramList1, type.getParameterTypes(), paramWarner);
/*      */     deferredAttrContext.complete();
/*      */     return type;
/*      */   }
/*      */
/*      */   Type checkMethod(Env<AttrContext> paramEnv, Type paramType, Symbol paramSymbol, Attr.ResultInfo paramResultInfo, List<Type> paramList1, List<Type> paramList2, Warner paramWarner) {
/*      */     MethodResolutionContext methodResolutionContext = this.currentResolutionContext;
/*      */     try {
/*      */       this.currentResolutionContext = new MethodResolutionContext();
/*      */       this.currentResolutionContext.attrMode = DeferredAttr.AttrMode.CHECK;
/*      */       if (paramEnv.tree.hasTag(JCTree.Tag.REFERENCE))
/*      */         this.currentResolutionContext.methodCheck = new MethodReferenceCheck(paramResultInfo.checkContext.inferenceContext());
/*      */       MethodResolutionPhase methodResolutionPhase = this.currentResolutionContext.step = ((AttrContext)paramEnv.info).pendingResolutionPhase;
/*      */       return rawInstantiate(paramEnv, paramType, paramSymbol, paramResultInfo, paramList1, paramList2, methodResolutionPhase.isBoxingRequired(), methodResolutionPhase.isVarargsRequired(), paramWarner);
/*      */     } finally {
/*      */       this.currentResolutionContext = methodResolutionContext;
/*      */     }
/*      */   }
/*      */
/*      */   Type instantiate(Env<AttrContext> paramEnv, Type paramType, Symbol paramSymbol, Attr.ResultInfo paramResultInfo, List<Type> paramList1, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2, Warner paramWarner) {
/*      */     try {
/*      */       return rawInstantiate(paramEnv, paramType, paramSymbol, paramResultInfo, paramList1, paramList2, paramBoolean1, paramBoolean2, paramWarner);
/*      */     } catch (InapplicableMethodException inapplicableMethodException) {
/*      */       return null;
/*      */     }
/*      */   }
/*      */
/*      */   enum MethodCheckDiag {
/*      */     ARITY_MISMATCH("arg.length.mismatch", "infer.arg.length.mismatch"),
/*      */     ARG_MISMATCH("no.conforming.assignment.exists", "infer.no.conforming.assignment.exists"),
/*      */     VARARG_MISMATCH("varargs.argument.mismatch", "infer.varargs.argument.mismatch"),
/*      */     INACCESSIBLE_VARARGS("inaccessible.varargs.type", "inaccessible.varargs.type");
/*      */     final String basicKey;
/*      */     final String inferKey;
/*      */
/*      */     MethodCheckDiag(String param1String1, String param1String2) {
/*      */       this.basicKey = param1String1;
/*      */       this.inferKey = param1String2;
/*      */     }
/*      */
/*      */     String regex() {
/*      */       return String.format("([a-z]*\\.)*(%s|%s)", new Object[] { this.basicKey, this.inferKey });
/*      */     }
/*      */   }
/*      */
/*      */   abstract class AbstractMethodCheck implements MethodCheck {
/*      */     public void argumentsAcceptable(Env<AttrContext> param1Env, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, List<Type> param1List1, List<Type> param1List2, Warner param1Warner) {
/*      */       boolean bool = param1DeferredAttrContext.phase.isVarargsRequired();
/*      */       JCTree jCTree = treeForDiagnostics(param1Env);
/*      */       List list = TreeInfo.args(jCTree);
/*      */       Infer.InferenceContext inferenceContext = param1DeferredAttrContext.inferenceContext;
/*      */       Type type = bool ? (Type)param1List2.last() : null;
/*      */       if (type == null && param1List1.size() != param1List2.size())
/*      */         reportMC((JCDiagnostic.DiagnosticPosition)jCTree, MethodCheckDiag.ARITY_MISMATCH, inferenceContext, new Object[0]);
/*      */       while (param1List1.nonEmpty() && param1List2.head != type) {
/*      */         JCDiagnostic.DiagnosticPosition diagnosticPosition = (list != null) ? (JCDiagnostic.DiagnosticPosition)list.head : null;
/*      */         checkArg(diagnosticPosition, false, (Type)param1List1.head, (Type)param1List2.head, param1DeferredAttrContext, param1Warner);
/*      */         param1List1 = param1List1.tail;
/*      */         param1List2 = param1List2.tail;
/*      */         list = (list != null) ? list.tail : list;
/*      */       }
/*      */       if (param1List2.head != type)
/*      */         reportMC((JCDiagnostic.DiagnosticPosition)jCTree, MethodCheckDiag.ARITY_MISMATCH, inferenceContext, new Object[0]);
/*      */       if (bool) {
/*      */         Type type1 = Resolve.this.types.elemtype(type);
/*      */         while (param1List1.nonEmpty()) {
/*      */           JCDiagnostic.DiagnosticPosition diagnosticPosition = (list != null) ? (JCDiagnostic.DiagnosticPosition)list.head : null;
/*      */           checkArg(diagnosticPosition, true, (Type)param1List1.head, type1, param1DeferredAttrContext, param1Warner);
/*      */           param1List1 = param1List1.tail;
/*      */           list = (list != null) ? list.tail : list;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     private JCTree treeForDiagnostics(Env<AttrContext> param1Env) {
/*      */       return (((AttrContext)param1Env.info).preferredTreeForDiagnostics != null) ? ((AttrContext)param1Env.info).preferredTreeForDiagnostics : param1Env.tree;
/*      */     }
/*      */
/*      */     abstract void checkArg(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, boolean param1Boolean, Type param1Type1, Type param1Type2, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner);
/*      */
/*      */     protected void reportMC(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, MethodCheckDiag param1MethodCheckDiag, Infer.InferenceContext param1InferenceContext, Object... param1VarArgs) {
/*      */       boolean bool = (param1InferenceContext != Resolve.this.infer.emptyContext) ? true : false;
/*      */       InapplicableMethodException inapplicableMethodException = bool ? Resolve.this.infer.inferenceException : Resolve.this.inapplicableMethodException;
/*      */       if (bool && !param1MethodCheckDiag.inferKey.equals(param1MethodCheckDiag.basicKey)) {
/*      */         Object[] arrayOfObject = new Object[param1VarArgs.length + 1];
/*      */         System.arraycopy(param1VarArgs, 0, arrayOfObject, 1, param1VarArgs.length);
/*      */         arrayOfObject[0] = param1InferenceContext.inferenceVars();
/*      */         param1VarArgs = arrayOfObject;
/*      */       }
/*      */       String str = bool ? param1MethodCheckDiag.inferKey : param1MethodCheckDiag.basicKey;
/*      */       throw inapplicableMethodException.setMessage(Resolve.this.diags.create(JCDiagnostic.DiagnosticType.FRAGMENT, Resolve.this.log.currentSource(), param1DiagnosticPosition, str, param1VarArgs));
/*      */     }
/*      */
/*      */     public MethodCheck mostSpecificCheck(List<Type> param1List, boolean param1Boolean) {
/*      */       return Resolve.this.nilMethodCheck;
/*      */     }
/*      */   }
/*      */
/*      */   List<Type> dummyArgs(int paramInt) {
/*      */     ListBuffer listBuffer = new ListBuffer();
/*      */     for (byte b = 0; b < paramInt; b++)
/*      */       listBuffer.append(Type.noType);
/*      */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   class MethodReferenceCheck extends AbstractMethodCheck {
/*      */     Infer.InferenceContext pendingInferenceContext;
/*      */
/*      */     MethodReferenceCheck(Infer.InferenceContext param1InferenceContext) {
/*      */       this.pendingInferenceContext = param1InferenceContext;
/*      */     }
/*      */
/*      */     void checkArg(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, boolean param1Boolean, Type param1Type1, Type param1Type2, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner) {
/*      */       Attr.ResultInfo resultInfo = methodCheckResult(param1Boolean, param1Type2, param1DeferredAttrContext, param1Warner);
/*      */       resultInfo.check(param1DiagnosticPosition, param1Type1);
/*      */     }
/*      */
/*      */     private Attr.ResultInfo methodCheckResult(final boolean varargsCheck, Type param1Type, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner) {
/*      */       MethodCheckContext methodCheckContext = new MethodCheckContext(!param1DeferredAttrContext.phase.isBoxingRequired(), param1DeferredAttrContext, param1Warner) {
/*      */           MethodCheckDiag methodDiag = varargsCheck ? MethodCheckDiag.VARARG_MISMATCH : MethodCheckDiag.ARG_MISMATCH;
/*      */
/*      */           public boolean compatible(Type param2Type1, Type param2Type2, Warner param2Warner) {
/*      */             param2Type1 = MethodReferenceCheck.this.pendingInferenceContext.asUndetVar(param2Type1);
/*      */             if (param2Type1.hasTag(TypeTag.UNDETVAR) && param2Type2.isPrimitive())
/*      */               param2Type2 = (Resolve.this.types.boxedClass(param2Type2)).type;
/*      */             return super.compatible(param2Type1, param2Type2, param2Warner);
/*      */           }
/*      */
/*      */           public void report(JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, JCDiagnostic param2JCDiagnostic) {
/*      */             MethodReferenceCheck.this.reportMC(param2DiagnosticPosition, this.methodDiag, this.deferredAttrContext.inferenceContext, new Object[] { param2JCDiagnostic });
/*      */           }
/*      */         };
/*      */       return new MethodResultInfo(param1Type, methodCheckContext);
/*      */     }
/*      */
/*      */     public MethodCheck mostSpecificCheck(List<Type> param1List, boolean param1Boolean) {
/*      */       return new MostSpecificCheck(param1Boolean, param1List);
/*      */     }
/*      */   }
/*      */
/*      */   abstract class MethodCheckContext implements Check.CheckContext {
/*      */     boolean strict;
/*      */     DeferredAttr.DeferredAttrContext deferredAttrContext;
/*      */     Warner rsWarner;
/*      */
/*      */     public MethodCheckContext(boolean param1Boolean, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner) {
/*      */       this.strict = param1Boolean;
/*      */       this.deferredAttrContext = param1DeferredAttrContext;
/*      */       this.rsWarner = param1Warner;
/*      */     }
/*      */
/*      */     public boolean compatible(Type param1Type1, Type param1Type2, Warner param1Warner) {
/*      */       Infer.InferenceContext inferenceContext = this.deferredAttrContext.inferenceContext;
/*      */       return this.strict ? Resolve.this.types.isSubtypeUnchecked(inferenceContext.asUndetVar(param1Type1), inferenceContext.asUndetVar(param1Type2), param1Warner) : Resolve.this.types.isConvertible(inferenceContext.asUndetVar(param1Type1), inferenceContext.asUndetVar(param1Type2), param1Warner);
/*      */     }
/*      */
/*      */     public void report(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, JCDiagnostic param1JCDiagnostic) {
/*      */       throw Resolve.this.inapplicableMethodException.setMessage(param1JCDiagnostic);
/*      */     }
/*      */
/*      */     public Warner checkWarner(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Type param1Type1, Type param1Type2) {
/*      */       return this.rsWarner;
/*      */     }
/*      */
/*      */     public Infer.InferenceContext inferenceContext() {
/*      */       return this.deferredAttrContext.inferenceContext;
/*      */     }
/*      */
/*      */     public DeferredAttr.DeferredAttrContext deferredAttrContext() {
/*      */       return this.deferredAttrContext;
/*      */     }
/*      */
/*      */     public String toString() {
/*      */       return "MethodReferenceCheck";
/*      */     }
/*      */   }
/*      */
/*      */   class MethodResultInfo extends Attr.ResultInfo {
/*      */     public MethodResultInfo(Type param1Type, Check.CheckContext param1CheckContext) {
/*      */       super(12, param1Type, param1CheckContext);
/*      */     }
/*      */
/*      */     protected Type check(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Type param1Type) {
/*      */       if (param1Type.hasTag(TypeTag.DEFERRED)) {
/*      */         DeferredAttr.DeferredType deferredType = (DeferredAttr.DeferredType)param1Type;
/*      */         return deferredType.check(this);
/*      */       }
/*      */       Type type1 = U(param1Type);
/*      */       Type type2 = (param1DiagnosticPosition == null || param1DiagnosticPosition.getTree() == null) ? Resolve.this.types.capture(type1) : this.checkContext.inferenceContext().cachedCapture(param1DiagnosticPosition.getTree(), type1, true);
/*      */       return super.check(param1DiagnosticPosition, Resolve.this.chk.checkNonVoid(param1DiagnosticPosition, type2));
/*      */     }
/*      */
/*      */     private Type U(Type param1Type) {
/*      */       return (param1Type == this.pt) ? param1Type : Resolve.this.types.cvarUpperBound(param1Type);
/*      */     }
/*      */
/*      */     protected MethodResultInfo dup(Type param1Type) {
/*      */       return new MethodResultInfo(param1Type, this.checkContext);
/*      */     }
/*      */
/*      */     protected Attr.ResultInfo dup(Check.CheckContext param1CheckContext) {
/*      */       return new MethodResultInfo(this.pt, param1CheckContext);
/*      */     }
/*      */   }
/*      */
/*      */   class MostSpecificCheck implements MethodCheck {
/*      */     boolean strict;
/*      */     List<Type> actuals;
/*      */
/*      */     MostSpecificCheck(boolean param1Boolean, List<Type> param1List) {
/*      */       this.strict = param1Boolean;
/*      */       this.actuals = param1List;
/*      */     }
/*      */
/*      */     public void argumentsAcceptable(Env<AttrContext> param1Env, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, List<Type> param1List1, List<Type> param1List2, Warner param1Warner) {
/*      */       param1List2 = Resolve.this.adjustArgs(param1List2, param1DeferredAttrContext.msym, param1List1.length(), param1DeferredAttrContext.phase.isVarargsRequired());
/*      */       while (param1List2.nonEmpty()) {
/*      */         Attr.ResultInfo resultInfo = methodCheckResult((Type)param1List2.head, param1DeferredAttrContext, param1Warner, (Type)this.actuals.head);
/*      */         resultInfo.check(null, (Type)param1List1.head);
/*      */         param1List1 = param1List1.tail;
/*      */         param1List2 = param1List2.tail;
/*      */         this.actuals = this.actuals.isEmpty() ? this.actuals : this.actuals.tail;
/*      */       }
/*      */     }
/*      */
/*      */     Attr.ResultInfo methodCheckResult(Type param1Type1, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, Warner param1Warner, Type param1Type2) {
/*      */       Resolve.this.attr.getClass();
/*      */       return new Attr.ResultInfo(Resolve.this.attr, 12, param1Type1, new MostSpecificCheckContext(this.strict, param1DeferredAttrContext, param1Warner, param1Type2));
/*      */     }
/*      */
/*      */     class MostSpecificCheckContext extends MethodCheckContext {
/*      */       Type actual;
/*      */
/*      */       public MostSpecificCheckContext(boolean param2Boolean, DeferredAttr.DeferredAttrContext param2DeferredAttrContext, Warner param2Warner, Type param2Type) {
/*      */         super(param2Boolean, param2DeferredAttrContext, param2Warner);
/*      */         this.actual = param2Type;
/*      */       }
/*      */
/*      */       public boolean compatible(Type param2Type1, Type param2Type2, Warner param2Warner) {
/*      */         if (Resolve.this.allowFunctionalInterfaceMostSpecific && unrelatedFunctionalInterfaces(param2Type1, param2Type2) && this.actual != null && this.actual.getTag() == TypeTag.DEFERRED) {
/*      */           DeferredAttr.DeferredType deferredType = (DeferredAttr.DeferredType)this.actual;
/*      */           DeferredAttr.DeferredType.SpeculativeCache.Entry entry = deferredType.speculativeCache.get(this.deferredAttrContext.msym, this.deferredAttrContext.phase);
/*      */           if (entry != null && entry.speculativeTree != Resolve.this.deferredAttr.stuckTree)
/*      */             return functionalInterfaceMostSpecific(param2Type1, param2Type2, entry.speculativeTree, param2Warner);
/*      */         }
/*      */         return super.compatible(param2Type1, param2Type2, param2Warner);
/*      */       }
/*      */
/*      */       private boolean unrelatedFunctionalInterfaces(Type param2Type1, Type param2Type2) {
/*      */         return (Resolve.this.types.isFunctionalInterface(param2Type1.tsym) && Resolve.this.types.isFunctionalInterface(param2Type2.tsym) && Resolve.this.types.asSuper(param2Type1, (Symbol)param2Type2.tsym) == null && Resolve.this.types.asSuper(param2Type2, (Symbol)param2Type1.tsym) == null);
/*      */       }
/*      */
/*      */       private boolean functionalInterfaceMostSpecific(Type param2Type1, Type param2Type2, JCTree param2JCTree, Warner param2Warner) {
/*      */         FunctionalInterfaceMostSpecificChecker functionalInterfaceMostSpecificChecker = new FunctionalInterfaceMostSpecificChecker(param2Type1, param2Type2, param2Warner);
/*      */         functionalInterfaceMostSpecificChecker.scan(param2JCTree);
/*      */         return functionalInterfaceMostSpecificChecker.result;
/*      */       }
/*      */
/*      */       class FunctionalInterfaceMostSpecificChecker extends DeferredAttr.PolyScanner {
/*      */         final Type t;
/*      */         final Type s;
/*      */         final Warner warn;
/*      */         boolean result;
/*      */
/*      */         FunctionalInterfaceMostSpecificChecker(Type param3Type1, Type param3Type2, Warner param3Warner) {
/*      */           this.t = param3Type1;
/*      */           this.s = param3Type2;
/*      */           this.warn = param3Warner;
/*      */           this.result = true;
/*      */         }
/*      */
/*      */         void skip(JCTree param3JCTree) {
/*      */           this.result &= 0x0;
/*      */         }
/*      */
/*      */         public void visitConditional(JCTree.JCConditional param3JCConditional) {
/*      */           scan((JCTree)param3JCConditional.truepart);
/*      */           scan((JCTree)param3JCConditional.falsepart);
/*      */         }
/*      */
/*      */         public void visitReference(JCTree.JCMemberReference param3JCMemberReference) {
/*      */           Type type1 = Resolve.this.types.findDescriptorType(this.t);
/*      */           Type type2 = Resolve.this.types.findDescriptorType(this.s);
/*      */           if (!Resolve.this.types.isSameTypes(type1.getParameterTypes(), MostSpecificCheckContext.this.inferenceContext().asUndetVars(type2.getParameterTypes()))) {
/*      */             this.result &= 0x0;
/*      */           } else {
/*      */             Type type3 = type1.getReturnType();
/*      */             Type type4 = type2.getReturnType();
/*      */             if (type4.hasTag(TypeTag.VOID)) {
/*      */               this.result &= 0x1;
/*      */             } else if (type3.hasTag(TypeTag.VOID)) {
/*      */               this.result &= 0x0;
/*      */             } else if (type3.isPrimitive() != type4.isPrimitive()) {
/*      */               boolean bool = (param3JCMemberReference.refPolyKind == JCTree.JCPolyExpression.PolyKind.STANDALONE && param3JCMemberReference.sym.type.getReturnType().isPrimitive());
/*      */               this.result &= (bool == type3.isPrimitive() && bool != type4.isPrimitive()) ? 1 : 0;
/*      */             } else {
/*      */               this.result &= MostSpecificCheckContext.this.compatible(type3, type4, this.warn);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */         public void visitLambda(JCTree.JCLambda param3JCLambda) {
/*      */           Type type1 = Resolve.this.types.findDescriptorType(this.t);
/*      */           Type type2 = Resolve.this.types.findDescriptorType(this.s);
/*      */           if (!Resolve.this.types.isSameTypes(type1.getParameterTypes(), MostSpecificCheckContext.this.inferenceContext().asUndetVars(type2.getParameterTypes()))) {
/*      */             this.result &= 0x0;
/*      */           } else {
/*      */             Type type3 = type1.getReturnType();
/*      */             Type type4 = type2.getReturnType();
/*      */             if (type4.hasTag(TypeTag.VOID)) {
/*      */               this.result &= 0x1;
/*      */             } else if (type3.hasTag(TypeTag.VOID)) {
/*      */               this.result &= 0x0;
/*      */             } else if (MostSpecificCheckContext.this.unrelatedFunctionalInterfaces(type3, type4)) {
/*      */               for (JCTree.JCExpression jCExpression : lambdaResults(param3JCLambda))
/*      */                 this.result &= MostSpecificCheckContext.this.functionalInterfaceMostSpecific(type3, type4, (JCTree)jCExpression, this.warn);
/*      */             } else if (type3.isPrimitive() != type4.isPrimitive()) {
/*      */               for (JCTree.JCExpression jCExpression : lambdaResults(param3JCLambda)) {
/*      */                 boolean bool = (jCExpression.isStandalone() && jCExpression.type.isPrimitive());
/*      */                 this.result &= (bool == type3.isPrimitive() && bool != type4.isPrimitive()) ? 1 : 0;
/*      */               }
/*      */             } else {
/*      */               this.result &= MostSpecificCheckContext.this.compatible(type3, type4, this.warn);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */         private List<JCTree.JCExpression> lambdaResults(JCTree.JCLambda param3JCLambda) {
/*      */           if (param3JCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION)
/*      */             return List.of(param3JCLambda.body);
/*      */           final ListBuffer buffer = new ListBuffer();
/*      */           DeferredAttr.LambdaReturnScanner lambdaReturnScanner = new DeferredAttr.LambdaReturnScanner() {
/*      */               public void visitReturn(JCTree.JCReturn param4JCReturn) {
/*      */                 if (param4JCReturn.expr != null)
/*      */                   buffer.append(param4JCReturn.expr);
/*      */               }
/*      */             };
/*      */           lambdaReturnScanner.scan(param3JCLambda.body);
/*      */           return listBuffer.toList();
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     public MethodCheck mostSpecificCheck(List<Type> param1List, boolean param1Boolean) {
/*      */       Assert.error("Cannot get here!");
/*      */       return null;
/*      */     }
/*      */   }
/*      */
/*      */   public static class InapplicableMethodException extends RuntimeException {
/*      */     private static final long serialVersionUID = 0L;
/*      */     JCDiagnostic diagnostic;
/*      */     JCDiagnostic.Factory diags;
/*      */
/*      */     InapplicableMethodException(JCDiagnostic.Factory param1Factory) {
/*      */       this.diagnostic = null;
/*      */       this.diags = param1Factory;
/*      */     }
/*      */
/*      */     InapplicableMethodException setMessage() {
/*      */       return setMessage((JCDiagnostic)null);
/*      */     }
/*      */
/*      */     InapplicableMethodException setMessage(String param1String) {
/*      */       return setMessage((param1String != null) ? this.diags.fragment(param1String, new Object[0]) : null);
/*      */     }
/*      */
/*      */     InapplicableMethodException setMessage(String param1String, Object... param1VarArgs) {
/*      */       return setMessage((param1String != null) ? this.diags.fragment(param1String, param1VarArgs) : null);
/*      */     }
/*      */
/*      */     InapplicableMethodException setMessage(JCDiagnostic param1JCDiagnostic) {
/*      */       this.diagnostic = param1JCDiagnostic;
/*      */       return this;
/*      */     }
/*      */
/*      */     public JCDiagnostic getDiagnostic() {
/*      */       return this.diagnostic;
/*      */     }
/*      */   }
/*      */
/*      */   Symbol findField(Env<AttrContext> paramEnv, Type paramType, Name paramName, Symbol.TypeSymbol paramTypeSymbol) {
/*      */     while (paramTypeSymbol.type.hasTag(TypeTag.TYPEVAR))
/*      */       paramTypeSymbol = (paramTypeSymbol.type.getUpperBound()).tsym;
/*      */     Symbol symbol = this.varNotFound;
/*      */     Scope.Entry entry = paramTypeSymbol.members().lookup(paramName);
/*      */     while (entry.scope != null) {
/*      */       if (entry.sym.kind == 4 && (entry.sym.flags_field & 0x1000L) == 0L)
/*      */         return isAccessible(paramEnv, paramType, entry.sym) ? entry.sym : new AccessError(paramEnv, paramType, entry.sym);
/*      */       entry = entry.next();
/*      */     }
/*      */     Type type = this.types.supertype(paramTypeSymbol.type);
/*      */     if (type != null && (type.hasTag(TypeTag.CLASS) || type.hasTag(TypeTag.TYPEVAR))) {
/*      */       Symbol symbol1 = findField(paramEnv, paramType, paramName, type.tsym);
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     List list = this.types.interfaces(paramTypeSymbol.type);
/*      */     for (; symbol.kind != 129 && list.nonEmpty(); list = list.tail) {
/*      */       Symbol symbol1 = findField(paramEnv, paramType, paramName, ((Type)list.head).tsym);
/*      */       if (symbol.exists() && symbol1.exists() && symbol1.owner != symbol.owner) {
/*      */         symbol = new AmbiguityError(symbol, symbol1);
/*      */       } else if (symbol1.kind < symbol.kind) {
/*      */         symbol = symbol1;
/*      */       }
/*      */     }
/*      */     return symbol;
/*      */   }
/*      */
/*      */   public Symbol.VarSymbol resolveInternalField(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, Name paramName) {
/*      */     Symbol symbol = findField(paramEnv, paramType, paramName, paramType.tsym);
/*      */     if (symbol.kind == 4)
/*      */       return (Symbol.VarSymbol)symbol;
/*      */     throw new FatalError(this.diags.fragment("fatal.err.cant.locate.field", new Object[] { paramName }));
/*      */   }
/*      */
/*      */   Symbol findVar(Env<AttrContext> paramEnv, Name paramName) {
/*      */     Symbol symbol1 = this.varNotFound;
/*      */     Env<AttrContext> env = paramEnv;
/*      */     boolean bool = false;
/*      */     while (env.outer != null) {
/*      */       if (isStatic(env))
/*      */         bool = true;
/*      */       Scope.Entry entry = ((AttrContext)env.info).scope.lookup(paramName);
/*      */       while (entry.scope != null && (entry.sym.kind != 4 || (entry.sym.flags_field & 0x1000L) != 0L))
/*      */         entry = entry.next();
/*      */       Symbol symbol = (entry.scope != null) ? entry.sym : findField(env, env.enclClass.sym.type, paramName, (Symbol.TypeSymbol)env.enclClass.sym);
/*      */       if (symbol.exists()) {
/*      */         if (bool && symbol.kind == 4 && symbol.owner.kind == 2 && (symbol.flags() & 0x8L) == 0L)
/*      */           return new StaticError(symbol);
/*      */         return symbol;
/*      */       }
/*      */       if (symbol.kind < symbol1.kind)
/*      */         symbol1 = symbol;
/*      */       if ((env.enclClass.sym.flags() & 0x8L) != 0L)
/*      */         bool = true;
/*      */       env = env.outer;
/*      */     }
/*      */     Symbol symbol2 = findField(paramEnv, this.syms.predefClass.type, paramName, (Symbol.TypeSymbol)this.syms.predefClass);
/*      */     if (symbol2.exists())
/*      */       return symbol2;
/*      */     if (symbol1.exists())
/*      */       return symbol1;
/*      */     Symbol symbol3 = null;
/*      */     for (Scope scope : new Scope[] { (Scope)paramEnv.toplevel.namedImportScope, (Scope)paramEnv.toplevel.starImportScope }) {
/*      */       Scope.Entry entry = scope.lookup(paramName);
/*      */       for (; entry.scope != null; entry = entry.next()) {
/*      */         symbol2 = entry.sym;
/*      */         if (symbol2.kind == 4) {
/*      */           if (symbol1.kind < 129 && symbol2.owner != symbol1.owner)
/*      */             return new AmbiguityError(symbol1, symbol2);
/*      */           if (symbol1.kind >= 4) {
/*      */             symbol3 = (entry.getOrigin()).owner;
/*      */             symbol1 = isAccessible(paramEnv, symbol3.type, symbol2) ? symbol2 : new AccessError(paramEnv, symbol3.type, symbol2);
/*      */           }
/*      */         }
/*      */       }
/*      */       if (symbol1.exists())
/*      */         break;
/*      */     }
/*      */     if (symbol1.kind == 4 && symbol1.owner.type != symbol3.type)
/*      */       return symbol1.clone(symbol3);
/*      */     return symbol1;
/*      */   }
/*      */
/*      */   Symbol selectBest(Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2, Symbol paramSymbol1, Symbol paramSymbol2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*      */     if (paramSymbol1.kind == 63 || !paramSymbol1.isInheritedIn((Symbol)paramType.tsym, this.types))
/*      */       return paramSymbol2;
/*      */     if (paramBoolean2 && (paramSymbol1.flags() & 0x400000000L) == 0L)
/*      */       return (paramSymbol2.kind >= 128) ? new BadVarargsMethod((ResolveError)paramSymbol2.baseSymbol()) : paramSymbol2;
/*      */     Assert.check((paramSymbol1.kind < 129));
/*      */     try {
/*      */       Type type = rawInstantiate(paramEnv, paramType, paramSymbol1, null, paramList1, paramList2, paramBoolean1, paramBoolean2, this.types.noWarnings);
/*      */       if (!paramBoolean3 || this.verboseResolutionMode.contains(VerboseResolutionMode.PREDEF))
/*      */         this.currentResolutionContext.addApplicableCandidate(paramSymbol1, type);
/*      */     } catch (InapplicableMethodException inapplicableMethodException) {
/*      */       if (!paramBoolean3)
/*      */         this.currentResolutionContext.addInapplicableCandidate(paramSymbol1, inapplicableMethodException.getDiagnostic());
/*      */       switch (paramSymbol2.kind) {
/*      */         case 136:
/*      */           return new InapplicableSymbolError(this.currentResolutionContext);
/*      */         case 135:
/*      */           if (paramBoolean3)
/*      */             return paramSymbol2;
/*      */           paramSymbol2 = new InapplicableSymbolsError(this.currentResolutionContext);
/*      */           break;
/*      */       }
/*      */       return paramSymbol2;
/*      */     }
/*      */     if (!isAccessible(paramEnv, paramType, paramSymbol1))
/*      */       return (paramSymbol2.kind == 136) ? new AccessError(paramEnv, paramType, paramSymbol1) : paramSymbol2;
/*      */     return (paramSymbol2.kind > 129) ? paramSymbol1 : mostSpecific(paramList1, paramSymbol1, paramSymbol2, paramEnv, paramType, (paramBoolean1 && paramBoolean3), paramBoolean2);
/*      */   }
/*      */
/*      */   Symbol mostSpecific(List<Type> paramList, Symbol paramSymbol1, Symbol paramSymbol2, Env<AttrContext> paramEnv, Type paramType, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     boolean bool1;
/*      */     boolean bool2;
/*      */     AmbiguityError ambiguityError;
/*      */     int i;
/*      */     int j;
/*      */     switch (paramSymbol2.kind) {
/*      */       case 16:
/*      */         if (paramSymbol1 == paramSymbol2)
/*      */           return paramSymbol1;
/*      */         bool1 = signatureMoreSpecific(paramList, paramEnv, paramType, paramSymbol1, paramSymbol2, paramBoolean1, paramBoolean2);
/*      */         bool2 = signatureMoreSpecific(paramList, paramEnv, paramType, paramSymbol2, paramSymbol1, paramBoolean1, paramBoolean2);
/*      */         if (bool1 && bool2) {
/*      */           Type type1 = this.types.memberType(paramType, paramSymbol1);
/*      */           Type type2 = this.types.memberType(paramType, paramSymbol2);
/*      */           if (!this.types.overrideEquivalent(type1, type2))
/*      */             return ambiguityError(paramSymbol1, paramSymbol2);
/*      */           if ((paramSymbol1.flags() & 0x80000000L) != (paramSymbol2.flags() & 0x80000000L))
/*      */             return ((paramSymbol1.flags() & 0x80000000L) != 0L) ? paramSymbol2 : paramSymbol1;
/*      */           Symbol.TypeSymbol typeSymbol1 = (Symbol.TypeSymbol)paramSymbol1.owner;
/*      */           Symbol.TypeSymbol typeSymbol2 = (Symbol.TypeSymbol)paramSymbol2.owner;
/*      */           if (this.types.asSuper(typeSymbol1.type, (Symbol)typeSymbol2) != null && ((paramSymbol1.owner.flags_field & 0x200L) == 0L || (paramSymbol2.owner.flags_field & 0x200L) != 0L) && paramSymbol1.overrides(paramSymbol2, typeSymbol1, this.types, false))
/*      */             return paramSymbol1;
/*      */           if (this.types.asSuper(typeSymbol2.type, (Symbol)typeSymbol1) != null && ((paramSymbol2.owner.flags_field & 0x200L) == 0L || (paramSymbol1.owner.flags_field & 0x200L) != 0L) && paramSymbol2.overrides(paramSymbol1, typeSymbol2, this.types, false))
/*      */             return paramSymbol2;
/*      */           boolean bool3 = ((paramSymbol1.flags() & 0x400L) != 0L) ? true : false;
/*      */           boolean bool4 = ((paramSymbol2.flags() & 0x400L) != 0L) ? true : false;
/*      */           if (bool3 && !bool4)
/*      */             return paramSymbol2;
/*      */           if (bool4 && !bool3)
/*      */             return paramSymbol1;
/*      */           return ambiguityError(paramSymbol1, paramSymbol2);
/*      */         }
/*      */         if (bool1)
/*      */           return paramSymbol1;
/*      */         if (bool2)
/*      */           return paramSymbol2;
/*      */         return ambiguityError(paramSymbol1, paramSymbol2);
/*      */       case 129:
/*      */         ambiguityError = (AmbiguityError)paramSymbol2.baseSymbol();
/*      */         i = 1;
/*      */         j = 1;
/*      */         for (Symbol symbol1 : ambiguityError.ambiguousSyms) {
/*      */           Symbol symbol2 = mostSpecific(paramList, paramSymbol1, symbol1, paramEnv, paramType, paramBoolean1, paramBoolean2);
/*      */           i &= (symbol2 == paramSymbol1) ? 1 : 0;
/*      */           j &= (symbol2 == symbol1) ? 1 : 0;
/*      */         }
/*      */         if (i != 0)
/*      */           return paramSymbol1;
/*      */         if (j == 0)
/*      */           ambiguityError.addAmbiguousSymbol(paramSymbol1);
/*      */         return ambiguityError;
/*      */     }
/*      */     throw new AssertionError();
/*      */   }
/*      */
/*      */   private boolean signatureMoreSpecific(List<Type> paramList, Env<AttrContext> paramEnv, Type paramType, Symbol paramSymbol1, Symbol paramSymbol2, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     this.noteWarner.clear();
/*      */     int i = Math.max(Math.max(paramSymbol1.type.getParameterTypes().length(), paramList.length()), paramSymbol2.type.getParameterTypes().length());
/*      */     MethodResolutionContext methodResolutionContext = this.currentResolutionContext;
/*      */     try {
/*      */       this.currentResolutionContext = new MethodResolutionContext();
/*      */       this.currentResolutionContext.step = methodResolutionContext.step;
/*      */       this.currentResolutionContext.methodCheck = methodResolutionContext.methodCheck.mostSpecificCheck(paramList, !paramBoolean1);
/*      */       Type type = instantiate(paramEnv, paramType, paramSymbol2, null, adjustArgs(this.types.cvarLowerBounds(this.types.memberType(paramType, paramSymbol1).getParameterTypes()), paramSymbol1, i, paramBoolean2), null, paramBoolean1, paramBoolean2, this.noteWarner);
/*      */       return (type != null && !this.noteWarner.hasLint(Lint.LintCategory.UNCHECKED));
/*      */     } finally {
/*      */       this.currentResolutionContext = methodResolutionContext;
/*      */     }
/*      */   }
/*      */
/*      */   List<Type> adjustArgs(List<Type> paramList, Symbol paramSymbol, int paramInt, boolean paramBoolean) {
/*      */     if ((paramSymbol.flags() & 0x400000000L) != 0L && paramBoolean) {
/*      */       Type type = this.types.elemtype((Type)paramList.last());
/*      */       if (type == null)
/*      */         Assert.error("Bad varargs = " + paramList.last() + " " + paramSymbol);
/*      */       List<Type> list = (paramList.reverse()).tail.prepend(type).reverse();
/*      */       while (list.length() < paramInt)
/*      */         list = list.append(list.last());
/*      */       return list;
/*      */     }
/*      */     return paramList;
/*      */   }
/*      */
/*      */   Type mostSpecificReturnType(Type paramType1, Type paramType2) {
/*      */     Type type1 = paramType1.getReturnType();
/*      */     Type type2 = paramType2.getReturnType();
/*      */     if (paramType1.hasTag(TypeTag.FORALL) && paramType2.hasTag(TypeTag.FORALL))
/*      */       type1 = this.types.subst(type1, paramType1.getTypeArguments(), paramType2.getTypeArguments());
/*      */     if (this.types.isSubtype(type1, type2))
/*      */       return paramType1;
/*      */     if (this.types.isSubtype(type2, type1))
/*      */       return paramType2;
/*      */     if (this.types.returnTypeSubstitutable(paramType1, paramType2))
/*      */       return paramType1;
/*      */     if (this.types.returnTypeSubstitutable(paramType2, paramType1))
/*      */       return paramType2;
/*      */     return null;
/*      */   }
/*      */
/*      */   Symbol ambiguityError(Symbol paramSymbol1, Symbol paramSymbol2) {
/*      */     if (((paramSymbol1.flags() | paramSymbol2.flags()) & 0x40000000000L) != 0L)
/*      */       return ((paramSymbol1.flags() & 0x40000000000L) == 0L) ? paramSymbol1 : paramSymbol2;
/*      */     return new AmbiguityError(paramSymbol1, paramSymbol2);
/*      */   }
/*      */
/*      */   Symbol findMethodInScope(Env<AttrContext> paramEnv, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2, Scope paramScope, Symbol paramSymbol, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
/*      */     for (Symbol symbol : paramScope.getElementsByName(paramName, new LookupFilter(paramBoolean4)))
/*      */       paramSymbol = selectBest(paramEnv, paramType, paramList1, paramList2, symbol, paramSymbol, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */     return paramSymbol;
/*      */   }
/*      */
/*      */   class LookupFilter implements Filter<Symbol> {
/*      */     boolean abstractOk;
/*      */
/*      */     LookupFilter(boolean param1Boolean) {
/*      */       this.abstractOk = param1Boolean;
/*      */     }
/*      */
/*      */     public boolean accepts(Symbol param1Symbol) {
/*      */       long l = param1Symbol.flags();
/*      */       return (param1Symbol.kind == 16 && (l & 0x1000L) == 0L && (this.abstractOk || (l & 0x80000000000L) != 0L || (l & 0x400L) == 0L));
/*      */     }
/*      */   }
/*      */
/*      */   Symbol findMethod(Env<AttrContext> paramEnv, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*      */     SymbolNotFoundError symbolNotFoundError = this.methodNotFound;
/*      */     return findMethod(paramEnv, paramType, paramName, paramList1, paramList2, paramType.tsym.type, symbolNotFoundError, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */   }
/*      */
/*      */   private Symbol findMethod(Env<AttrContext> paramEnv, Type paramType1, Name paramName, List<Type> paramList1, List<Type> paramList2, Type paramType2, Symbol paramSymbol, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*      */     List[] arrayOfList = { List.nil(), List.nil() };
/*      */     InterfaceLookupPhase interfaceLookupPhase = InterfaceLookupPhase.ABSTRACT_OK;
/*      */     for (Symbol.TypeSymbol typeSymbol : superclasses(paramType2)) {
/*      */       paramSymbol = findMethodInScope(paramEnv, paramType1, paramName, paramList1, paramList2, typeSymbol.members(), paramSymbol, paramBoolean1, paramBoolean2, paramBoolean3, true);
/*      */       if (paramName == this.names.init)
/*      */         return paramSymbol;
/*      */       interfaceLookupPhase = (interfaceLookupPhase == null) ? null : interfaceLookupPhase.update((Symbol)typeSymbol, this);
/*      */       if (interfaceLookupPhase != null)
/*      */         for (Type type : this.types.interfaces(typeSymbol.type))
/*      */           arrayOfList[interfaceLookupPhase.ordinal()] = this.types.union(this.types.closure(type), arrayOfList[interfaceLookupPhase.ordinal()]);
/*      */     }
/*      */     Symbol symbol = (paramSymbol.kind < 63 && (paramSymbol.flags() & 0x400L) == 0L) ? paramSymbol : this.methodNotFound;
/*      */     for (InterfaceLookupPhase interfaceLookupPhase1 : InterfaceLookupPhase.values()) {
/*      */       for (Type type : arrayOfList[interfaceLookupPhase1.ordinal()]) {
/*      */         if (!type.isInterface() || (interfaceLookupPhase1 == InterfaceLookupPhase.DEFAULT_OK && (type.tsym.flags() & 0x80000000000L) == 0L))
/*      */           continue;
/*      */         paramSymbol = findMethodInScope(paramEnv, paramType1, paramName, paramList1, paramList2, type.tsym.members(), paramSymbol, paramBoolean1, paramBoolean2, paramBoolean3, true);
/*      */         if (symbol != paramSymbol && symbol.kind < 63 && paramSymbol.kind < 63 && this.types.isSubSignature(symbol.type, paramSymbol.type))
/*      */           paramSymbol = symbol;
/*      */       }
/*      */     }
/*      */     return paramSymbol;
/*      */   }
/*      */
/*      */   enum InterfaceLookupPhase {
/*      */     ABSTRACT_OK {
/*      */       InterfaceLookupPhase update(Symbol param2Symbol, Resolve param2Resolve) {
/*      */         if ((param2Symbol.flags() & 0x4600L) != 0L)
/*      */           return this;
/*      */         return DEFAULT_OK;
/*      */       }
/*      */     },
/*      */     DEFAULT_OK {
/*      */       InterfaceLookupPhase update(Symbol param2Symbol, Resolve param2Resolve) {
/*      */         return this;
/*      */       }
/*      */     };
/*      */
/*      */     abstract InterfaceLookupPhase update(Symbol param1Symbol, Resolve param1Resolve);
/*      */   }
/*      */
/*      */   Iterable<Symbol.TypeSymbol> superclasses(final Type intype) {
/*      */     return new Iterable<Symbol.TypeSymbol>() {
/*      */         public Iterator<Symbol.TypeSymbol> iterator() {
/*      */           return new Iterator<Symbol.TypeSymbol>() {
/*      */               List<Symbol.TypeSymbol> seen = List.nil();
/*      */               Symbol.TypeSymbol currentSym = symbolFor(intype);
/*      */               Symbol.TypeSymbol prevSym = null;
/*      */
/*      */               public boolean hasNext() {
/*      */                 if (this.currentSym == Resolve.this.syms.noSymbol)
/*      */                   this.currentSym = symbolFor(Resolve.this.types.supertype(this.prevSym.type));
/*      */                 return (this.currentSym != null);
/*      */               }
/*      */
/*      */               public Symbol.TypeSymbol next() {
/*      */                 this.prevSym = this.currentSym;
/*      */                 this.currentSym = Resolve.this.syms.noSymbol;
/*      */                 Assert.check((this.prevSym != null || this.prevSym != Resolve.this.syms.noSymbol));
/*      */                 return this.prevSym;
/*      */               }
/*      */
/*      */               public void remove() {
/*      */                 throw new UnsupportedOperationException();
/*      */               }
/*      */
/*      */               Symbol.TypeSymbol symbolFor(Type param2Type) {
/*      */                 if (!param2Type.hasTag(TypeTag.CLASS) && !param2Type.hasTag(TypeTag.TYPEVAR))
/*      */                   return null;
/*      */                 while (param2Type.hasTag(TypeTag.TYPEVAR))
/*      */                   param2Type = param2Type.getUpperBound();
/*      */                 if (this.seen.contains(param2Type.tsym))
/*      */                   return null;
/*      */                 this.seen = this.seen.prepend(param2Type.tsym);
/*      */                 return param2Type.tsym;
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   Symbol findFun(Env<AttrContext> paramEnv, Name paramName, List<Type> paramList1, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     Symbol symbol1 = this.methodNotFound;
/*      */     Env<AttrContext> env = paramEnv;
/*      */     boolean bool = false;
/*      */     while (env.outer != null) {
/*      */       if (isStatic(env))
/*      */         bool = true;
/*      */       Assert.check((((AttrContext)env.info).preferredTreeForDiagnostics == null));
/*      */       ((AttrContext)env.info).preferredTreeForDiagnostics = paramEnv.tree;
/*      */       try {
/*      */         Symbol symbol = findMethod(env, env.enclClass.sym.type, paramName, paramList1, paramList2, paramBoolean1, paramBoolean2, false);
/*      */         if (symbol.exists()) {
/*      */           if (bool && symbol.kind == 16 && symbol.owner.kind == 2 && (symbol.flags() & 0x8L) == 0L)
/*      */             return new StaticError(symbol);
/*      */           return symbol;
/*      */         }
/*      */         if (symbol.kind < symbol1.kind)
/*      */           symbol1 = symbol;
/*      */       } finally {
/*      */         ((AttrContext)env.info).preferredTreeForDiagnostics = null;
/*      */       }
/*      */       if ((env.enclClass.sym.flags() & 0x8L) != 0L)
/*      */         bool = true;
/*      */       env = env.outer;
/*      */     }
/*      */     Symbol symbol2 = findMethod(paramEnv, this.syms.predefClass.type, paramName, paramList1, paramList2, paramBoolean1, paramBoolean2, false);
/*      */     if (symbol2.exists())
/*      */       return symbol2;
/*      */     Scope.Entry entry = paramEnv.toplevel.namedImportScope.lookup(paramName);
/*      */     for (; entry.scope != null; entry = entry.next()) {
/*      */       symbol2 = entry.sym;
/*      */       Type type = (entry.getOrigin()).owner.type;
/*      */       if (symbol2.kind == 16) {
/*      */         if (entry.sym.owner.type != type)
/*      */           symbol2 = symbol2.clone((entry.getOrigin()).owner);
/*      */         if (!isAccessible(paramEnv, type, symbol2))
/*      */           symbol2 = new AccessError(paramEnv, type, symbol2);
/*      */         symbol1 = selectBest(paramEnv, type, paramList1, paramList2, symbol2, symbol1, paramBoolean1, paramBoolean2, false);
/*      */       }
/*      */     }
/*      */     if (symbol1.exists())
/*      */       return symbol1;
/*      */     entry = paramEnv.toplevel.starImportScope.lookup(paramName);
/*      */     for (; entry.scope != null; entry = entry.next()) {
/*      */       symbol2 = entry.sym;
/*      */       Type type = (entry.getOrigin()).owner.type;
/*      */       if (symbol2.kind == 16) {
/*      */         if (entry.sym.owner.type != type)
/*      */           symbol2 = symbol2.clone((entry.getOrigin()).owner);
/*      */         if (!isAccessible(paramEnv, type, symbol2))
/*      */           symbol2 = new AccessError(paramEnv, type, symbol2);
/*      */         symbol1 = selectBest(paramEnv, type, paramList1, paramList2, symbol2, symbol1, paramBoolean1, paramBoolean2, false);
/*      */       }
/*      */     }
/*      */     return symbol1;
/*      */   }
/*      */
/*      */   Symbol loadClass(Env<AttrContext> paramEnv, Name paramName) {
/*      */     try {
/*      */       Symbol.ClassSymbol classSymbol = this.reader.loadClass(paramName);
/*      */       return isAccessible(paramEnv, (Symbol.TypeSymbol)classSymbol) ? (Symbol)classSymbol : new AccessError((Symbol)classSymbol);
/*      */     } catch (ClassReader.BadClassFile badClassFile) {
/*      */       throw badClassFile;
/*      */     } catch (Symbol.CompletionFailure completionFailure) {
/*      */       return this.typeNotFound;
/*      */     }
/*      */   }
/*      */
/*      */   Symbol findImmediateMemberType(Env<AttrContext> paramEnv, Type paramType, Name paramName, Symbol.TypeSymbol paramTypeSymbol) {
/*      */     Scope.Entry entry = paramTypeSymbol.members().lookup(paramName);
/*      */     while (entry.scope != null) {
/*      */       if (entry.sym.kind == 2)
/*      */         return isAccessible(paramEnv, paramType, entry.sym) ? entry.sym : new AccessError(paramEnv, paramType, entry.sym);
/*      */       entry = entry.next();
/*      */     }
/*      */     return this.typeNotFound;
/*      */   }
/*      */
/*      */   Symbol findInheritedMemberType(Env<AttrContext> paramEnv, Type paramType, Name paramName, Symbol.TypeSymbol paramTypeSymbol) {
/*      */     Symbol symbol = this.typeNotFound;
/*      */     Type type = this.types.supertype(paramTypeSymbol.type);
/*      */     if (type != null && type.hasTag(TypeTag.CLASS)) {
/*      */       Symbol symbol1 = findMemberType(paramEnv, paramType, paramName, type.tsym);
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     List list = this.types.interfaces(paramTypeSymbol.type);
/*      */     for (; symbol.kind != 129 && list.nonEmpty(); list = list.tail) {
/*      */       Symbol symbol1 = findMemberType(paramEnv, paramType, paramName, ((Type)list.head).tsym);
/*      */       if (symbol.kind < 129 && symbol1.kind < 129 && symbol1.owner != symbol.owner) {
/*      */         symbol = new AmbiguityError(symbol, symbol1);
/*      */       } else if (symbol1.kind < symbol.kind) {
/*      */         symbol = symbol1;
/*      */       }
/*      */     }
/*      */     return symbol;
/*      */   }
/*      */
/*      */   Symbol findMemberType(Env<AttrContext> paramEnv, Type paramType, Name paramName, Symbol.TypeSymbol paramTypeSymbol) {
/*      */     Symbol symbol = findImmediateMemberType(paramEnv, paramType, paramName, paramTypeSymbol);
/*      */     if (symbol != this.typeNotFound)
/*      */       return symbol;
/*      */     return findInheritedMemberType(paramEnv, paramType, paramName, paramTypeSymbol);
/*      */   }
/*      */
/*      */   Symbol findGlobalType(Env<AttrContext> paramEnv, Scope paramScope, Name paramName) {
/*      */     Symbol symbol = this.typeNotFound;
/*      */     for (Scope.Entry entry = paramScope.lookup(paramName); entry.scope != null; entry = entry.next()) {
/*      */       Symbol symbol1 = loadClass(paramEnv, entry.sym.flatName());
/*      */       if (symbol.kind == 2 && symbol1.kind == 2 && symbol != symbol1)
/*      */         return new AmbiguityError(symbol, symbol1);
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     return symbol;
/*      */   }
/*      */
/*      */   Symbol findTypeVar(Env<AttrContext> paramEnv, Name paramName, boolean paramBoolean) {
/*      */     Scope.Entry entry = ((AttrContext)paramEnv.info).scope.lookup(paramName);
/*      */     for (; entry.scope != null; entry = entry.next()) {
/*      */       if (entry.sym.kind == 2) {
/*      */         if (paramBoolean && entry.sym.type.hasTag(TypeTag.TYPEVAR) && entry.sym.owner.kind == 2)
/*      */           return new StaticError(entry.sym);
/*      */         return entry.sym;
/*      */       }
/*      */     }
/*      */     return this.typeNotFound;
/*      */   }
/*      */
/*      */   Symbol findType(Env<AttrContext> paramEnv, Name paramName) {
/*      */     Symbol symbol = this.typeNotFound;
/*      */     boolean bool = false;
/*      */     for (Env<AttrContext> env = paramEnv; env.outer != null; env = env.outer) {
/*      */       if (isStatic(env))
/*      */         bool = true;
/*      */       Symbol symbol2 = findTypeVar(env, paramName, bool);
/*      */       Symbol symbol1 = findImmediateMemberType(env, env.enclClass.sym.type, paramName, (Symbol.TypeSymbol)env.enclClass.sym);
/*      */       if (symbol2 != this.typeNotFound && (symbol1 == this.typeNotFound || (symbol2.kind == 2 && symbol2.exists() && symbol2.owner.kind == 16)))
/*      */         return symbol2;
/*      */       if (symbol1 == this.typeNotFound)
/*      */         symbol1 = findInheritedMemberType(env, env.enclClass.sym.type, paramName, (Symbol.TypeSymbol)env.enclClass.sym);
/*      */       if (bool && symbol1.kind == 2 && symbol1.type.hasTag(TypeTag.CLASS) && symbol1.type.getEnclosingType().hasTag(TypeTag.CLASS) && env.enclClass.sym.type.isParameterized() && symbol1.type.getEnclosingType().isParameterized())
/*      */         return new StaticError(symbol1);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */       JCTree.JCClassDecl jCClassDecl = env.baseClause ? (JCTree.JCClassDecl)env.tree : env.enclClass;
/*      */       if ((jCClassDecl.sym.flags() & 0x8L) != 0L)
/*      */         bool = true;
/*      */     }
/*      */     if (!paramEnv.tree.hasTag(JCTree.Tag.IMPORT)) {
/*      */       Symbol symbol1 = findGlobalType(paramEnv, (Scope)paramEnv.toplevel.namedImportScope, paramName);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */       symbol1 = findGlobalType(paramEnv, paramEnv.toplevel.packge.members(), paramName);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */       symbol1 = findGlobalType(paramEnv, (Scope)paramEnv.toplevel.starImportScope, paramName);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     return symbol;
/*      */   }
/*      */
/*      */   Symbol findIdent(Env<AttrContext> paramEnv, Name paramName, int paramInt) {
/*      */     Symbol symbol = this.typeNotFound;
/*      */     if ((paramInt & 0x4) != 0) {
/*      */       Symbol symbol1 = findVar(paramEnv, paramName);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     if ((paramInt & 0x2) != 0) {
/*      */       Symbol symbol1 = findType(paramEnv, paramName);
/*      */       if (symbol1.kind == 2)
/*      */         reportDependence((Symbol)paramEnv.enclClass.sym, symbol1);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     if ((paramInt & 0x1) != 0)
/*      */       return (Symbol)this.reader.enterPackage(paramName);
/*      */     return symbol;
/*      */   }
/*      */
/*      */   public void reportDependence(Symbol paramSymbol1, Symbol paramSymbol2) {}
/*      */
/*      */   Symbol findIdentInPackage(Env<AttrContext> paramEnv, Symbol.TypeSymbol paramTypeSymbol, Name paramName, int paramInt) {
/*      */     Name name = Symbol.TypeSymbol.formFullName(paramName, (Symbol)paramTypeSymbol);
/*      */     Symbol symbol = this.typeNotFound;
/*      */     Symbol.PackageSymbol packageSymbol = null;
/*      */     if ((paramInt & 0x1) != 0) {
/*      */       packageSymbol = this.reader.enterPackage(name);
/*      */       if (packageSymbol.exists())
/*      */         return (Symbol)packageSymbol;
/*      */     }
/*      */     if ((paramInt & 0x2) != 0) {
/*      */       Symbol symbol1 = loadClass(paramEnv, name);
/*      */       if (symbol1.exists()) {
/*      */         if (paramName == symbol1.name)
/*      */           return symbol1;
/*      */       } else if (symbol1.kind < symbol.kind) {
/*      */         symbol = symbol1;
/*      */       }
/*      */     }
/*      */     return (packageSymbol != null) ? (Symbol)packageSymbol : symbol;
/*      */   }
/*      */
/*      */   Symbol findIdentInType(Env<AttrContext> paramEnv, Type paramType, Name paramName, int paramInt) {
/*      */     Symbol symbol = this.typeNotFound;
/*      */     if ((paramInt & 0x4) != 0) {
/*      */       Symbol symbol1 = findField(paramEnv, paramType, paramName, paramType.tsym);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     if ((paramInt & 0x2) != 0) {
/*      */       Symbol symbol1 = findMemberType(paramEnv, paramType, paramName, paramType.tsym);
/*      */       if (symbol1.exists())
/*      */         return symbol1;
/*      */       if (symbol1.kind < symbol.kind)
/*      */         symbol = symbol1;
/*      */     }
/*      */     return symbol;
/*      */   }
/*      */
/*      */   Symbol accessInternal(Symbol paramSymbol1, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol2, Type paramType, Name paramName, boolean paramBoolean, List<Type> paramList1, List<Type> paramList2, LogResolveHelper paramLogResolveHelper) {
/*      */     if (paramSymbol1.kind >= 129) {
/*      */       ResolveError resolveError = (ResolveError)paramSymbol1.baseSymbol();
/*      */       paramSymbol1 = resolveError.access(paramName, paramBoolean ? paramType.tsym : this.syms.noSymbol);
/*      */       paramList1 = paramLogResolveHelper.getArgumentTypes(resolveError, paramSymbol1, paramName, paramList1);
/*      */       if (paramLogResolveHelper.resolveDiagnosticNeeded(paramType, paramList1, paramList2))
/*      */         logResolveError(resolveError, paramDiagnosticPosition, paramSymbol2, paramType, paramName, paramList1, paramList2);
/*      */     }
/*      */     return paramSymbol1;
/*      */   }
/*      */
/*      */   Symbol accessMethod(Symbol paramSymbol1, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol2, Type paramType, Name paramName, boolean paramBoolean, List<Type> paramList1, List<Type> paramList2) {
/*      */     return accessInternal(paramSymbol1, paramDiagnosticPosition, paramSymbol2, paramType, paramName, paramBoolean, paramList1, paramList2, this.methodLogResolveHelper);
/*      */   }
/*      */
/*      */   Symbol accessMethod(Symbol paramSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName, boolean paramBoolean, List<Type> paramList1, List<Type> paramList2) {
/*      */     return accessMethod(paramSymbol, paramDiagnosticPosition, (Symbol)paramType.tsym, paramType, paramName, paramBoolean, paramList1, paramList2);
/*      */   }
/*      */
/*      */   Symbol accessBase(Symbol paramSymbol1, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol2, Type paramType, Name paramName, boolean paramBoolean) {
/*      */     return accessInternal(paramSymbol1, paramDiagnosticPosition, paramSymbol2, paramType, paramName, paramBoolean, List.nil(), null, this.basicLogResolveHelper);
/*      */   }
/*      */
/*      */   Symbol accessBase(Symbol paramSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName, boolean paramBoolean) {
/*      */     return accessBase(paramSymbol, paramDiagnosticPosition, (Symbol)paramType.tsym, paramType, paramName, paramBoolean);
/*      */   }
/*      */
/*      */   class ResolveDeferredRecoveryMap extends DeferredAttr.RecoveryDeferredTypeMap {
/*      */     public ResolveDeferredRecoveryMap(DeferredAttr.AttrMode param1AttrMode, Symbol param1Symbol, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1AttrMode, param1Symbol, param1MethodResolutionPhase);
/*      */     }
/*      */
/*      */     protected Type typeOf(DeferredAttr.DeferredType param1DeferredType) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: invokespecial typeOf : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;)Lcom/sun/tools/javac/code/Type;
/*      */       //   5: astore_2
/*      */       //   6: aload_2
/*      */       //   7: invokevirtual isErroneous : ()Z
/*      */       //   10: ifne -> 71
/*      */       //   13: getstatic com/sun/tools/javac/comp/Resolve$15.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   16: aload_1
/*      */       //   17: getfield tree : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   20: invokestatic skipParens : (Lcom/sun/tools/javac/tree/JCTree$JCExpression;)Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   23: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   26: invokevirtual ordinal : ()I
/*      */       //   29: iaload
/*      */       //   30: tableswitch default -> 71, 1 -> 56, 2 -> 56, 3 -> 58
/*      */       //   56: aload_1
/*      */       //   57: areturn
/*      */       //   58: aload_2
/*      */       //   59: getstatic com/sun/tools/javac/code/Type.recoveryType : Lcom/sun/tools/javac/code/Type$JCNoType;
/*      */       //   62: if_acmpne -> 69
/*      */       //   65: aload_1
/*      */       //   66: goto -> 70
/*      */       //   69: aload_2
/*      */       //   70: areturn
/*      */       //   71: aload_2
/*      */       //   72: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2310	-> 0
/*      */       //   #2311	-> 6
/*      */       //   #2312	-> 13
/*      */       //   #2315	-> 56
/*      */       //   #2317	-> 58
/*      */       //   #2321	-> 71
/*      */     }
/*      */   }
/*      */
/*      */   void checkNonAbstract(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/*      */     if ((paramSymbol.flags() & 0x400L) != 0L && (paramSymbol.flags() & 0x80000000000L) == 0L)
/*      */       this.log.error(paramDiagnosticPosition, "abstract.cant.be.accessed.directly", new Object[] { Kinds.kindName(paramSymbol), paramSymbol, paramSymbol.location() });
/*      */   }
/*      */
/*      */   public void printscopes(Scope paramScope) {
/*      */     while (paramScope != null) {
/*      */       if (paramScope.owner != null)
/*      */         System.err.print(paramScope.owner + ": ");
/*      */       for (Scope.Entry entry = paramScope.elems; entry != null; entry = entry.sibling) {
/*      */         if ((entry.sym.flags() & 0x400L) != 0L)
/*      */           System.err.print("abstract ");
/*      */         System.err.print(entry.sym + " ");
/*      */       }
/*      */       System.err.println();
/*      */       paramScope = paramScope.next;
/*      */     }
/*      */   }
/*      */
/*      */   void printscopes(Env<AttrContext> paramEnv) {
/*      */     while (paramEnv.outer != null) {
/*      */       System.err.println("------------------------------");
/*      */       printscopes(((AttrContext)paramEnv.info).scope);
/*      */       paramEnv = paramEnv.outer;
/*      */     }
/*      */   }
/*      */
/*      */   public void printscopes(Type paramType) {
/*      */     while (paramType.hasTag(TypeTag.CLASS)) {
/*      */       printscopes(paramType.tsym.members());
/*      */       paramType = this.types.supertype(paramType);
/*      */     }
/*      */   }
/*      */
/*      */   Symbol resolveIdent(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Name paramName, int paramInt) {
/*      */     return accessBase(findIdent(paramEnv, paramName, paramInt), paramDiagnosticPosition, paramEnv.enclClass.sym.type, paramName, false);
/*      */   }
/*      */
/*      */   Symbol resolveMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Name paramName, List<Type> paramList1, List<Type> paramList2) {
/*      */     return lookupMethod(paramEnv, paramDiagnosticPosition, (Symbol)paramEnv.enclClass.sym, this.resolveMethodCheck, new BasicLookupHelper(paramName, paramEnv.enclClass.sym.type, paramList1, paramList2) {
/*      */           Symbol doLookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */             return Resolve.this.findFun(param1Env, this.name, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired());
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */   Symbol resolveQualifiedMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2) {
/*      */     return resolveQualifiedMethod(paramDiagnosticPosition, paramEnv, (Symbol)paramType.tsym, paramType, paramName, paramList1, paramList2);
/*      */   }
/*      */
/*      */   Symbol resolveQualifiedMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Symbol paramSymbol, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2) {
/*      */     return resolveQualifiedMethod(new MethodResolutionContext(), paramDiagnosticPosition, paramEnv, paramSymbol, paramType, paramName, paramList1, paramList2);
/*      */   }
/*      */
/*      */   private Symbol resolveQualifiedMethod(MethodResolutionContext paramMethodResolutionContext, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Symbol paramSymbol, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2) {
/*      */     return lookupMethod(paramEnv, paramDiagnosticPosition, paramSymbol, paramMethodResolutionContext, new BasicLookupHelper(paramName, paramType, paramList1, paramList2) {
/*      */           Symbol doLookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */             return Resolve.this.findMethod(param1Env, this.site, this.name, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired(), false);
/*      */           }
/*      */
/*      */           Symbol access(Env<AttrContext> param1Env, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */             if (param1Symbol2.kind >= 129) {
/*      */               param1Symbol2 = super.access(param1Env, param1DiagnosticPosition, param1Symbol1, param1Symbol2);
/*      */             } else if (Resolve.this.allowMethodHandles) {
/*      */               Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)param1Symbol2;
/*      */               if ((methodSymbol.flags() & 0x400000000000L) != 0L)
/*      */                 return Resolve.this.findPolymorphicSignatureInstance(param1Env, param1Symbol2, this.argtypes);
/*      */             }
/*      */             return param1Symbol2;
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */   Symbol findPolymorphicSignatureInstance(Env<AttrContext> paramEnv, final Symbol spMethod, List<Type> paramList) {
/*      */     Type type = this.infer.instantiatePolymorphicSignatureInstance(paramEnv, (Symbol.MethodSymbol)spMethod, this.currentResolutionContext, paramList);
/*      */     for (Symbol symbol : this.polymorphicSignatureScope.getElementsByName(spMethod.name)) {
/*      */       if (this.types.isSameType(type, symbol.type))
/*      */         return symbol;
/*      */     }
/*      */     long l = 0x2000000400L | spMethod.flags() & 0x7L;
/*      */     Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(l, spMethod.name, type, spMethod.owner) {
/*      */         public Symbol baseSymbol() {
/*      */           return spMethod;
/*      */         }
/*      */       };
/*      */     if (!type.isErroneous())
/*      */       this.polymorphicSignatureScope.enter((Symbol)methodSymbol);
/*      */     return (Symbol)methodSymbol;
/*      */   }
/*      */
/*      */   public Symbol.MethodSymbol resolveInternalMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2) {
/*      */     MethodResolutionContext methodResolutionContext = new MethodResolutionContext();
/*      */     methodResolutionContext.internalResolution = true;
/*      */     Symbol symbol = resolveQualifiedMethod(methodResolutionContext, paramDiagnosticPosition, paramEnv, (Symbol)paramType.tsym, paramType, paramName, paramList1, paramList2);
/*      */     if (symbol.kind == 16)
/*      */       return (Symbol.MethodSymbol)symbol;
/*      */     throw new FatalError(this.diags.fragment("fatal.err.cant.locate.meth", new Object[] { paramName }));
/*      */   }
/*      */
/*      */   Symbol resolveConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2) {
/*      */     return resolveConstructor(new MethodResolutionContext(), paramDiagnosticPosition, paramEnv, paramType, paramList1, paramList2);
/*      */   }
/*      */
/*      */   private Symbol resolveConstructor(MethodResolutionContext paramMethodResolutionContext, final JCDiagnostic.DiagnosticPosition pos, Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2) {
/*      */     return lookupMethod(paramEnv, pos, (Symbol)paramType.tsym, paramMethodResolutionContext, new BasicLookupHelper(this.names.init, paramType, paramList1, paramList2) {
/*      */           Symbol doLookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */             return Resolve.this.findConstructor(pos, param1Env, this.site, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired());
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */   public Symbol.MethodSymbol resolveInternalConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2) {
/*      */     MethodResolutionContext methodResolutionContext = new MethodResolutionContext();
/*      */     methodResolutionContext.internalResolution = true;
/*      */     Symbol symbol = resolveConstructor(methodResolutionContext, paramDiagnosticPosition, paramEnv, paramType, paramList1, paramList2);
/*      */     if (symbol.kind == 16)
/*      */       return (Symbol.MethodSymbol)symbol;
/*      */     throw new FatalError(this.diags.fragment("fatal.err.cant.locate.ctor", new Object[] { paramType }));
/*      */   }
/*      */
/*      */   Symbol findConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     Symbol symbol = findMethod(paramEnv, paramType, this.names.init, paramList1, paramList2, paramBoolean1, paramBoolean2, false);
/*      */     this.chk.checkDeprecated(paramDiagnosticPosition, ((AttrContext)paramEnv.info).scope.owner, symbol);
/*      */     return symbol;
/*      */   }
/*      */
/*      */   Symbol resolveDiamond(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2) {
/*      */     return lookupMethod(paramEnv, paramDiagnosticPosition, (Symbol)paramType.tsym, this.resolveMethodCheck, new BasicLookupHelper(this.names.init, paramType, paramList1, paramList2) {
/*      */           Symbol doLookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */             return Resolve.this.findDiamond(param1Env, this.site, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired());
/*      */           }
/*      */
/*      */           Symbol access(Env<AttrContext> param1Env, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */             if (param1Symbol2.kind >= 129)
/*      */               if (param1Symbol2.kind != 135 && param1Symbol2.kind != 134) {
/*      */                 param1Symbol2 = super.access(param1Env, param1DiagnosticPosition, param1Symbol1, param1Symbol2);
/*      */               } else {
/*      */                 final JCDiagnostic details = (param1Symbol2.kind == 135) ? (JCDiagnostic)(((InapplicableSymbolError)param1Symbol2.baseSymbol()).errCandidate()).snd : null;
/*      */                 param1Symbol2 = new InapplicableSymbolError(param1Symbol2.kind, "diamondError", Resolve.this.currentResolutionContext) {
/*      */                     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param2DiagnosticType, JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, Symbol param2Symbol, Type param2Type, Name param2Name, List<Type> param2List1, List<Type> param2List2) {
/*      */                       String str = (details == null) ? "cant.apply.diamond" : "cant.apply.diamond.1";
/*      */                       return Resolve.this.diags.create(param2DiagnosticType, Resolve.this.log.currentSource(), param2DiagnosticPosition, str, new Object[] { this.this$1.this$0.diags.fragment("diamond", new Object[] { param2Type.tsym }), this.val$details });
/*      */                     }
/*      */                   };
/*      */                 param1Symbol2 = Resolve.this.accessMethod(param1Symbol2, param1DiagnosticPosition, this.site, Resolve.this.names.init, true, this.argtypes, this.typeargtypes);
/*      */                 ((AttrContext)param1Env.info).pendingResolutionPhase = Resolve.this.currentResolutionContext.step;
/*      */               }
/*      */             return param1Symbol2;
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */   private Symbol findDiamond(Env<AttrContext> paramEnv, Type paramType, List<Type> paramList1, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2) {
/*      */     Symbol symbol = this.methodNotFound;
/*      */     Scope.Entry entry = paramType.tsym.members().lookup(this.names.init);
/*      */     for (; entry.scope != null; entry = entry.next()) {
/*      */       final Symbol sym = entry.sym;
/*      */       if (symbol1.kind == 16 && (symbol1.flags_field & 0x1000L) == 0L) {
/*      */         List list = entry.sym.type.hasTag(TypeTag.FORALL) ? ((Type.ForAll)symbol1.type).tvars : List.nil();
/*      */         Type.ForAll forAll = new Type.ForAll(paramType.tsym.type.getTypeArguments().appendList(list), this.types.createMethodTypeWithReturn((Type)symbol1.type.asMethodType(), paramType));
/*      */         Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(symbol1.flags(), this.names.init, (Type)forAll, (Symbol)paramType.tsym) {
/*      */             public Symbol baseSymbol() {
/*      */               return sym;
/*      */             }
/*      */           };
/*      */         symbol = selectBest(paramEnv, paramType, paramList1, paramList2, (Symbol)methodSymbol, symbol, paramBoolean1, paramBoolean2, false);
/*      */       }
/*      */     }
/*      */     return symbol;
/*      */   }
/*      */
/*      */   Symbol resolveOperator(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCTree.Tag paramTag, Env<AttrContext> paramEnv, List<Type> paramList) {
/*      */     MethodResolutionContext methodResolutionContext = this.currentResolutionContext;
/*      */     try {
/*      */       this.currentResolutionContext = new MethodResolutionContext();
/*      */       Name name = this.treeinfo.operatorName(paramTag);
/*      */       return lookupMethod(paramEnv, paramDiagnosticPosition, (Symbol)this.syms.predefClass, this.currentResolutionContext, new BasicLookupHelper(name, this.syms.predefClass.type, paramList, null, MethodResolutionPhase.BOX) {
/*      */             Symbol doLookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */               return Resolve.this.findMethod(param1Env, this.site, this.name, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired(), true);
/*      */             }
/*      */
/*      */             Symbol access(Env<AttrContext> param1Env, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */               return Resolve.this.accessMethod(param1Symbol2, param1DiagnosticPosition, param1Env.enclClass.sym.type, this.name, false, this.argtypes, null);
/*      */             }
/*      */           });
/*      */     } finally {
/*      */       this.currentResolutionContext = methodResolutionContext;
/*      */     }
/*      */   }
/*      */
/*      */   Symbol resolveUnaryOperator(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCTree.Tag paramTag, Env<AttrContext> paramEnv, Type paramType) {
/*      */     return resolveOperator(paramDiagnosticPosition, paramTag, paramEnv, List.of(paramType));
/*      */   }
/*      */
/*      */   Symbol resolveBinaryOperator(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCTree.Tag paramTag, Env<AttrContext> paramEnv, Type paramType1, Type paramType2) {
/*      */     return resolveOperator(paramDiagnosticPosition, paramTag, paramEnv, List.of(paramType1, paramType2));
/*      */   }
/*      */
/*      */   Symbol getMemberReference(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, JCTree.JCMemberReference paramJCMemberReference, Type paramType, Name paramName) {
/*      */     paramType = this.types.capture(paramType);
/*      */     ReferenceLookupHelper referenceLookupHelper = makeReferenceLookupHelper(paramJCMemberReference, paramType, paramName, List.nil(), null, MethodResolutionPhase.VARARITY);
/*      */     Env<AttrContext> env = paramEnv.dup(paramEnv.tree, ((AttrContext)paramEnv.info).dup());
/*      */     Symbol symbol = lookupMethod(env, paramEnv.tree.pos(), (Symbol)paramType.tsym, this.nilMethodCheck, referenceLookupHelper);
/*      */     ((AttrContext)paramEnv.info).pendingResolutionPhase = ((AttrContext)env.info).pendingResolutionPhase;
/*      */     return symbol;
/*      */   }
/*      */
/*      */   ReferenceLookupHelper makeReferenceLookupHelper(JCTree.JCMemberReference paramJCMemberReference, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2, MethodResolutionPhase paramMethodResolutionPhase) {
/*      */     ConstructorReferenceLookupHelper constructorReferenceLookupHelper;
/*      */     if (!paramName.equals(this.names.init)) {
/*      */       MethodReferenceLookupHelper methodReferenceLookupHelper = new MethodReferenceLookupHelper(paramJCMemberReference, paramName, paramType, paramList1, paramList2, paramMethodResolutionPhase);
/*      */     } else if (paramType.hasTag(TypeTag.ARRAY)) {
/*      */       ArrayConstructorReferenceLookupHelper arrayConstructorReferenceLookupHelper = new ArrayConstructorReferenceLookupHelper(paramJCMemberReference, paramType, paramList1, paramList2, paramMethodResolutionPhase);
/*      */     } else {
/*      */       constructorReferenceLookupHelper = new ConstructorReferenceLookupHelper(paramJCMemberReference, paramType, paramList1, paramList2, paramMethodResolutionPhase);
/*      */     }
/*      */     return constructorReferenceLookupHelper;
/*      */   }
/*      */
/*      */   Symbol resolveMemberReferenceByArity(Env<AttrContext> paramEnv, JCTree.JCMemberReference paramJCMemberReference, Type paramType, Name paramName, List<Type> paramList, Infer.InferenceContext paramInferenceContext) {
/*      */     boolean bool = TreeInfo.isStaticSelector((JCTree)paramJCMemberReference.expr, this.names);
/*      */     paramType = this.types.capture(paramType);
/*      */     ReferenceLookupHelper referenceLookupHelper1 = makeReferenceLookupHelper(paramJCMemberReference, paramType, paramName, paramList, null, MethodResolutionPhase.VARARITY);
/*      */     Env<AttrContext> env1 = paramEnv.dup(paramEnv.tree, ((AttrContext)paramEnv.info).dup());
/*      */     Symbol symbol1 = lookupMethod(env1, paramEnv.tree.pos(), (Symbol)paramType.tsym, this.arityMethodCheck, referenceLookupHelper1);
/*      */     if (bool && !paramName.equals(this.names.init) && !symbol1.isStatic() && symbol1.kind < 128)
/*      */       symbol1 = this.methodNotFound;
/*      */     Symbol symbol2 = this.methodNotFound;
/*      */     ReferenceLookupHelper referenceLookupHelper2 = null;
/*      */     Env<AttrContext> env2 = paramEnv.dup(paramEnv.tree, ((AttrContext)paramEnv.info).dup());
/*      */     if (bool) {
/*      */       referenceLookupHelper2 = referenceLookupHelper1.unboundLookup(paramInferenceContext);
/*      */       symbol2 = lookupMethod(env2, paramEnv.tree.pos(), (Symbol)paramType.tsym, this.arityMethodCheck, referenceLookupHelper2);
/*      */       if (symbol2.isStatic() && symbol2.kind < 128)
/*      */         symbol2 = this.methodNotFound;
/*      */     }
/*      */     Symbol symbol3 = choose(symbol1, symbol2);
/*      */     ((AttrContext)paramEnv.info).pendingResolutionPhase = (symbol3 == symbol2) ? ((AttrContext)env2.info).pendingResolutionPhase : ((AttrContext)env1.info).pendingResolutionPhase;
/*      */     return symbol3;
/*      */   }
/*      */
/*      */   Pair<Symbol, ReferenceLookupHelper> resolveMemberReference(Env<AttrContext> paramEnv, JCTree.JCMemberReference paramJCMemberReference, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2, MethodCheck paramMethodCheck, Infer.InferenceContext paramInferenceContext, DeferredAttr.AttrMode paramAttrMode) {
/*      */     paramType = this.types.capture(paramType);
/*      */     ReferenceLookupHelper referenceLookupHelper1 = makeReferenceLookupHelper(paramJCMemberReference, paramType, paramName, paramList1, paramList2, MethodResolutionPhase.VARARITY);
/*      */     Env<AttrContext> env1 = paramEnv.dup(paramEnv.tree, ((AttrContext)paramEnv.info).dup());
/*      */     boolean bool1 = false;
/*      */     MethodResolutionContext methodResolutionContext = new MethodResolutionContext();
/*      */     methodResolutionContext.methodCheck = paramMethodCheck;
/*      */     Symbol symbol1 = lookupMethod(env1, paramEnv.tree.pos(), (Symbol)paramType.tsym, methodResolutionContext, referenceLookupHelper1), symbol2 = symbol1;
/*      */     SearchResultKind searchResultKind1 = SearchResultKind.NOT_APPLICABLE_MATCH;
/*      */     boolean bool = TreeInfo.isStaticSelector((JCTree)paramJCMemberReference.expr, this.names);
/*      */     boolean bool2 = (bool && paramJCMemberReference.getMode() == MemberReferenceTree.ReferenceMode.INVOKE) ? true : false;
/*      */     if (symbol2.kind != 134 && symbol2.kind != 135 && bool2)
/*      */       if (!symbol2.isStatic()) {
/*      */         bool1 = true;
/*      */         if (hasAnotherApplicableMethod(methodResolutionContext, symbol2, true)) {
/*      */           searchResultKind1 = SearchResultKind.BAD_MATCH_MORE_SPECIFIC;
/*      */         } else {
/*      */           searchResultKind1 = SearchResultKind.BAD_MATCH;
/*      */           if (symbol2.kind < 128)
/*      */             symbol2 = this.methodWithCorrectStaticnessNotFound;
/*      */         }
/*      */       } else if (symbol2.kind < 128) {
/*      */         searchResultKind1 = SearchResultKind.GOOD_MATCH;
/*      */       }
/*      */     Symbol symbol3 = null;
/*      */     Symbol symbol4 = this.methodNotFound;
/*      */     ReferenceLookupHelper referenceLookupHelper2 = null;
/*      */     Env<AttrContext> env2 = paramEnv.dup(paramEnv.tree, ((AttrContext)paramEnv.info).dup());
/*      */     SearchResultKind searchResultKind2 = SearchResultKind.NOT_APPLICABLE_MATCH;
/*      */     boolean bool3 = false;
/*      */     if (bool) {
/*      */       referenceLookupHelper2 = referenceLookupHelper1.unboundLookup(paramInferenceContext);
/*      */       MethodResolutionContext methodResolutionContext1 = new MethodResolutionContext();
/*      */       methodResolutionContext1.methodCheck = paramMethodCheck;
/*      */       symbol4 = symbol3 = lookupMethod(env2, paramEnv.tree.pos(), (Symbol)paramType.tsym, methodResolutionContext1, referenceLookupHelper2);
/*      */       if (symbol4.kind != 135 && symbol4.kind != 134 && bool2)
/*      */         if (symbol4.isStatic()) {
/*      */           bool3 = true;
/*      */           if (hasAnotherApplicableMethod(methodResolutionContext1, symbol4, false)) {
/*      */             searchResultKind2 = SearchResultKind.BAD_MATCH_MORE_SPECIFIC;
/*      */           } else {
/*      */             searchResultKind2 = SearchResultKind.BAD_MATCH;
/*      */             if (symbol4.kind < 128)
/*      */               symbol4 = this.methodWithCorrectStaticnessNotFound;
/*      */           }
/*      */         } else if (symbol4.kind < 128) {
/*      */           searchResultKind2 = SearchResultKind.GOOD_MATCH;
/*      */         }
/*      */     }
/*      */     Symbol symbol5 = choose(symbol2, symbol4);
/*      */     if (symbol5.kind < 128 && (bool1 || bool3)) {
/*      */       if (bool1)
/*      */         symbol2 = this.methodWithCorrectStaticnessNotFound;
/*      */       if (bool3)
/*      */         symbol4 = this.methodWithCorrectStaticnessNotFound;
/*      */       symbol5 = choose(symbol2, symbol4);
/*      */     }
/*      */     if (symbol5 == this.methodWithCorrectStaticnessNotFound && paramAttrMode == DeferredAttr.AttrMode.CHECK) {
/*      */       Symbol symbol = symbol1;
/*      */       String str = "non-static.cant.be.ref";
/*      */       if (bool1 && bool3) {
/*      */         if (searchResultKind2 == SearchResultKind.BAD_MATCH_MORE_SPECIFIC) {
/*      */           symbol = symbol3;
/*      */           str = "static.method.in.unbound.lookup";
/*      */         }
/*      */       } else if (!bool1) {
/*      */         symbol = symbol3;
/*      */         str = "static.method.in.unbound.lookup";
/*      */       }
/*      */       this.log.error(paramJCMemberReference.expr.pos(), "invalid.mref", new Object[] { Kinds.kindName(paramJCMemberReference.getMode()), this.diags.fragment(str, new Object[] { Kinds.kindName(symbol), symbol }) });
/*      */     }
/*      */     Pair<Symbol, ReferenceLookupHelper> pair = new Pair(symbol5, (symbol5 == symbol4) ? referenceLookupHelper2 : referenceLookupHelper1);
/*      */     ((AttrContext)paramEnv.info).pendingResolutionPhase = (symbol5 == symbol4) ? ((AttrContext)env2.info).pendingResolutionPhase : ((AttrContext)env1.info).pendingResolutionPhase;
/*      */     return pair;
/*      */   }
/*      */
/*      */   enum SearchResultKind {
/*      */     GOOD_MATCH, BAD_MATCH_MORE_SPECIFIC, BAD_MATCH, NOT_APPLICABLE_MATCH;
/*      */   }
/*      */
/*      */   boolean hasAnotherApplicableMethod(MethodResolutionContext paramMethodResolutionContext, Symbol paramSymbol, boolean paramBoolean) {
/*      */     for (MethodResolutionContext.Candidate candidate : paramMethodResolutionContext.candidates) {
/*      */       if (paramMethodResolutionContext.step != candidate.step || !candidate.isApplicable() || candidate.sym == paramSymbol)
/*      */         continue;
/*      */       if (candidate.sym.isStatic() == paramBoolean)
/*      */         return true;
/*      */     }
/*      */     return false;
/*      */   }
/*      */
/*      */   private Symbol choose(Symbol paramSymbol1, Symbol paramSymbol2) {
/*      */     if (lookupSuccess(paramSymbol1) && lookupSuccess(paramSymbol2))
/*      */       return ambiguityError(paramSymbol1, paramSymbol2);
/*      */     if (lookupSuccess(paramSymbol1) || (canIgnore(paramSymbol2) && !canIgnore(paramSymbol1)))
/*      */       return paramSymbol1;
/*      */     if (lookupSuccess(paramSymbol2) || (canIgnore(paramSymbol1) && !canIgnore(paramSymbol2)))
/*      */       return paramSymbol2;
/*      */     return paramSymbol1;
/*      */   }
/*      */
/*      */   private boolean lookupSuccess(Symbol paramSymbol) {
/*      */     return (paramSymbol.kind == 16 || paramSymbol.kind == 129);
/*      */   }
/*      */
/*      */   private boolean canIgnore(Symbol paramSymbol) {
/*      */     InapplicableSymbolError inapplicableSymbolError;
/*      */     InapplicableSymbolsError inapplicableSymbolsError;
/*      */     switch (paramSymbol.kind) {
/*      */       case 136:
/*      */         return true;
/*      */       case 135:
/*      */         inapplicableSymbolError = (InapplicableSymbolError)paramSymbol.baseSymbol();
/*      */         return (new MethodResolutionDiagHelper.Template(MethodCheckDiag.ARITY_MISMATCH.regex(), new MethodResolutionDiagHelper.Template[0])).matches((inapplicableSymbolError.errCandidate()).snd);
/*      */       case 134:
/*      */         inapplicableSymbolsError = (InapplicableSymbolsError)paramSymbol.baseSymbol();
/*      */         return inapplicableSymbolsError.filterCandidates(inapplicableSymbolsError.mapCandidates()).isEmpty();
/*      */       case 138:
/*      */         return false;
/*      */     }
/*      */     return false;
/*      */   }
/*      */
/*      */   abstract class LookupHelper {
/*      */     Name name;
/*      */     Type site;
/*      */     List<Type> argtypes;
/*      */     List<Type> typeargtypes;
/*      */     MethodResolutionPhase maxPhase;
/*      */
/*      */     LookupHelper(Name param1Name, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       this.name = param1Name;
/*      */       this.site = param1Type;
/*      */       this.argtypes = param1List1;
/*      */       this.typeargtypes = param1List2;
/*      */       this.maxPhase = param1MethodResolutionPhase;
/*      */     }
/*      */
/*      */     final boolean shouldStop(Symbol param1Symbol, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       return (param1MethodResolutionPhase.ordinal() > this.maxPhase.ordinal() || param1Symbol.kind < 128 || param1Symbol.kind == 129);
/*      */     }
/*      */
/*      */     abstract Symbol lookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase);
/*      */
/*      */     void debug(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol) {}
/*      */
/*      */     abstract Symbol access(Env<AttrContext> param1Env, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol1, Symbol param1Symbol2);
/*      */   }
/*      */
/*      */   abstract class BasicLookupHelper extends LookupHelper {
/*      */     BasicLookupHelper(Name param1Name, Type param1Type, List<Type> param1List1, List<Type> param1List2) {
/*      */       this(param1Name, param1Type, param1List1, param1List2, MethodResolutionPhase.VARARITY);
/*      */     }
/*      */
/*      */     BasicLookupHelper(Name param1Name, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1Name, param1Type, param1List1, param1List2, param1MethodResolutionPhase);
/*      */     }
/*      */
/*      */     final Symbol lookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       Symbol symbol = doLookup(param1Env, param1MethodResolutionPhase);
/*      */       if (symbol.kind == 129) {
/*      */         AmbiguityError ambiguityError = (AmbiguityError)symbol.baseSymbol();
/*      */         symbol = ambiguityError.mergeAbstracts(this.site);
/*      */       }
/*      */       return symbol;
/*      */     }
/*      */
/*      */     abstract Symbol doLookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase);
/*      */
/*      */     Symbol access(Env<AttrContext> param1Env, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */       if (param1Symbol2.kind >= 129)
/*      */         param1Symbol2 = Resolve.this.accessMethod(param1Symbol2, param1DiagnosticPosition, param1Symbol1, this.site, this.name, true, this.argtypes, this.typeargtypes);
/*      */       return param1Symbol2;
/*      */     }
/*      */
/*      */     void debug(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol) {
/*      */       Resolve.this.reportVerboseResolutionDiagnostic(param1DiagnosticPosition, this.name, this.site, this.argtypes, this.typeargtypes, param1Symbol);
/*      */     }
/*      */   }
/*      */
/*      */   abstract class ReferenceLookupHelper extends LookupHelper {
/*      */     JCTree.JCMemberReference referenceTree;
/*      */
/*      */     ReferenceLookupHelper(JCTree.JCMemberReference param1JCMemberReference, Name param1Name, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1Name, param1Type, param1List1, param1List2, param1MethodResolutionPhase);
/*      */       this.referenceTree = param1JCMemberReference;
/*      */     }
/*      */
/*      */     ReferenceLookupHelper unboundLookup(Infer.InferenceContext param1InferenceContext) {
/*      */       return new ReferenceLookupHelper(this.referenceTree, this.name, this.site, this.argtypes, this.typeargtypes, this.maxPhase) {
/*      */           ReferenceLookupHelper unboundLookup(Infer.InferenceContext param2InferenceContext) {
/*      */             return this;
/*      */           }
/*      */
/*      */           Symbol lookup(Env<AttrContext> param2Env, MethodResolutionPhase param2MethodResolutionPhase) {
/*      */             return Resolve.this.methodNotFound;
/*      */           }
/*      */
/*      */           JCTree.JCMemberReference.ReferenceKind referenceKind(Symbol param2Symbol) {
/*      */             Assert.error();
/*      */             return null;
/*      */           }
/*      */         };
/*      */     }
/*      */
/*      */     abstract JCTree.JCMemberReference.ReferenceKind referenceKind(Symbol param1Symbol);
/*      */
/*      */     Symbol access(Env<AttrContext> param1Env, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */       if (param1Symbol2.kind == 129) {
/*      */         AmbiguityError ambiguityError = (AmbiguityError)param1Symbol2.baseSymbol();
/*      */         param1Symbol2 = ambiguityError.mergeAbstracts(this.site);
/*      */       }
/*      */       return param1Symbol2;
/*      */     }
/*      */   }
/*      */
/*      */   class MethodReferenceLookupHelper extends ReferenceLookupHelper {
/*      */     MethodReferenceLookupHelper(JCTree.JCMemberReference param1JCMemberReference, Name param1Name, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1JCMemberReference, param1Name, param1Type, param1List1, param1List2, param1MethodResolutionPhase);
/*      */     }
/*      */
/*      */     final Symbol lookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       return Resolve.this.findMethod(param1Env, this.site, this.name, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired(), Resolve.this.syms.operatorNames.contains(this.name));
/*      */     }
/*      */
/*      */     ReferenceLookupHelper unboundLookup(Infer.InferenceContext param1InferenceContext) {
/*      */       if (TreeInfo.isStaticSelector((JCTree)this.referenceTree.expr, Resolve.this.names) && this.argtypes.nonEmpty() && (((Type)this.argtypes.head).hasTag(TypeTag.NONE) || Resolve.this.types.isSubtypeUnchecked(param1InferenceContext.asUndetVar((Type)this.argtypes.head), this.site)))
/*      */         return new UnboundMethodReferenceLookupHelper(this.referenceTree, this.name, this.site, this.argtypes, this.typeargtypes, this.maxPhase);
/*      */       return super.unboundLookup(param1InferenceContext);
/*      */     }
/*      */
/*      */     JCTree.JCMemberReference.ReferenceKind referenceKind(Symbol param1Symbol) {
/*      */       if (param1Symbol.isStatic())
/*      */         return JCTree.JCMemberReference.ReferenceKind.STATIC;
/*      */       Name name = TreeInfo.name((JCTree)this.referenceTree.getQualifierExpression());
/*      */       return (name != null && name == Resolve.this.names._super) ? JCTree.JCMemberReference.ReferenceKind.SUPER : JCTree.JCMemberReference.ReferenceKind.BOUND;
/*      */     }
/*      */   }
/*      */
/*      */   class UnboundMethodReferenceLookupHelper extends MethodReferenceLookupHelper {
/*      */     UnboundMethodReferenceLookupHelper(JCTree.JCMemberReference param1JCMemberReference, Name param1Name, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1JCMemberReference, param1Name, param1Type, param1List1.tail, param1List2, param1MethodResolutionPhase);
/*      */       if (param1Type.isRaw() && !((Type)param1List1.head).hasTag(TypeTag.NONE)) {
/*      */         Type type = Resolve.this.types.asSuper((Type)param1List1.head, (Symbol)param1Type.tsym);
/*      */         this.site = Resolve.this.types.capture(type);
/*      */       }
/*      */     }
/*      */
/*      */     ReferenceLookupHelper unboundLookup(Infer.InferenceContext param1InferenceContext) {
/*      */       return this;
/*      */     }
/*      */
/*      */     JCTree.JCMemberReference.ReferenceKind referenceKind(Symbol param1Symbol) {
/*      */       return JCTree.JCMemberReference.ReferenceKind.UNBOUND;
/*      */     }
/*      */   }
/*      */
/*      */   class ArrayConstructorReferenceLookupHelper extends ReferenceLookupHelper {
/*      */     ArrayConstructorReferenceLookupHelper(JCTree.JCMemberReference param1JCMemberReference, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1JCMemberReference, Resolve.this.names.init, param1Type, param1List1, param1List2, param1MethodResolutionPhase);
/*      */     }
/*      */
/*      */     protected Symbol lookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       Scope scope = new Scope((Symbol)Resolve.this.syms.arrayClass);
/*      */       Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(1L, this.name, null, (Symbol)this.site.tsym);
/*      */       methodSymbol.type = (Type)new Type.MethodType(List.of(Resolve.this.syms.intType), this.site, List.nil(), (Symbol.TypeSymbol)Resolve.this.syms.methodClass);
/*      */       scope.enter((Symbol)methodSymbol);
/*      */       return Resolve.this.findMethodInScope(param1Env, this.site, this.name, this.argtypes, this.typeargtypes, scope, Resolve.this.methodNotFound, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired(), false, false);
/*      */     }
/*      */
/*      */     JCTree.JCMemberReference.ReferenceKind referenceKind(Symbol param1Symbol) {
/*      */       return JCTree.JCMemberReference.ReferenceKind.ARRAY_CTOR;
/*      */     }
/*      */   }
/*      */
/*      */   class ConstructorReferenceLookupHelper extends ReferenceLookupHelper {
/*      */     boolean needsInference;
/*      */
/*      */     ConstructorReferenceLookupHelper(JCTree.JCMemberReference param1JCMemberReference, Type param1Type, List<Type> param1List1, List<Type> param1List2, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1JCMemberReference, Resolve.this.names.init, param1Type, param1List1, param1List2, param1MethodResolutionPhase);
/*      */       if (param1Type.isRaw()) {
/*      */         this.site = (Type)new Type.ClassType(param1Type.getEnclosingType(), param1Type.tsym.type.getTypeArguments(), param1Type.tsym);
/*      */         this.needsInference = true;
/*      */       }
/*      */     }
/*      */
/*      */     protected Symbol lookup(Env<AttrContext> param1Env, MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       Symbol symbol = this.needsInference ? Resolve.this.findDiamond(param1Env, this.site, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired()) : Resolve.this.findMethod(param1Env, this.site, this.name, this.argtypes, this.typeargtypes, param1MethodResolutionPhase.isBoxingRequired(), param1MethodResolutionPhase.isVarargsRequired(), Resolve.this.syms.operatorNames.contains(this.name));
/*      */       return (symbol.kind != 16 || this.site.getEnclosingType().hasTag(TypeTag.NONE) || Resolve.this.hasEnclosingInstance(param1Env, this.site)) ? symbol : new InvalidSymbolError(132, symbol, null) {
/*      */           JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param2DiagnosticType, JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, Symbol param2Symbol, Type param2Type, Name param2Name, List<Type> param2List1, List<Type> param2List2) {
/*      */             return Resolve.this.diags.create(param2DiagnosticType, Resolve.this.log.currentSource(), param2DiagnosticPosition, "cant.access.inner.cls.constr", new Object[] { param2Type.tsym.name, param2List1, param2Type.getEnclosingType() });
/*      */           }
/*      */         };
/*      */     }
/*      */
/*      */     JCTree.JCMemberReference.ReferenceKind referenceKind(Symbol param1Symbol) {
/*      */       return this.site.getEnclosingType().hasTag(TypeTag.NONE) ? JCTree.JCMemberReference.ReferenceKind.TOPLEVEL : JCTree.JCMemberReference.ReferenceKind.IMPLICIT_INNER;
/*      */     }
/*      */   }
/*      */
/*      */   Symbol lookupMethod(Env<AttrContext> paramEnv, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, MethodCheck paramMethodCheck, LookupHelper paramLookupHelper) {
/*      */     MethodResolutionContext methodResolutionContext = new MethodResolutionContext();
/*      */     methodResolutionContext.methodCheck = paramMethodCheck;
/*      */     return lookupMethod(paramEnv, paramDiagnosticPosition, paramSymbol, methodResolutionContext, paramLookupHelper);
/*      */   }
/*      */
/*      */   Symbol lookupMethod(Env<AttrContext> paramEnv, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, MethodResolutionContext paramMethodResolutionContext, LookupHelper paramLookupHelper) {
/*      */     MethodResolutionContext methodResolutionContext = this.currentResolutionContext;
/*      */     try {
/*      */       Symbol symbol = this.methodNotFound;
/*      */       this.currentResolutionContext = paramMethodResolutionContext;
/*      */       for (MethodResolutionPhase methodResolutionPhase1 : this.methodResolutionSteps) {
/*      */         if (!methodResolutionPhase1.isApplicable(this.boxingEnabled, this.varargsEnabled) || paramLookupHelper.shouldStop(symbol, methodResolutionPhase1))
/*      */           break;
/*      */         MethodResolutionPhase methodResolutionPhase2 = this.currentResolutionContext.step;
/*      */         SymbolNotFoundError symbolNotFoundError = (SymbolNotFoundError)symbol;
/*      */         this.currentResolutionContext.step = methodResolutionPhase1;
/*      */         Symbol symbol1 = paramLookupHelper.lookup(paramEnv, methodResolutionPhase1);
/*      */         paramLookupHelper.debug(paramDiagnosticPosition, symbol1);
/*      */         symbol = methodResolutionPhase1.mergeResults(symbol, symbol1);
/*      */         ((AttrContext)paramEnv.info).pendingResolutionPhase = (symbolNotFoundError == symbol) ? methodResolutionPhase2 : methodResolutionPhase1;
/*      */       }
/*      */       return paramLookupHelper.access(paramEnv, paramDiagnosticPosition, paramSymbol, symbol);
/*      */     } finally {
/*      */       this.currentResolutionContext = methodResolutionContext;
/*      */     }
/*      */   }
/*      */
/*      */   Symbol resolveSelf(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Symbol.TypeSymbol paramTypeSymbol, Name paramName) {
/*      */     Env<AttrContext> env = paramEnv;
/*      */     boolean bool = false;
/*      */     while (env.outer != null) {
/*      */       if (isStatic(env))
/*      */         bool = true;
/*      */       if (env.enclClass.sym == paramTypeSymbol) {
/*      */         Symbol symbol = (((AttrContext)env.info).scope.lookup(paramName)).sym;
/*      */         if (symbol != null) {
/*      */           if (bool)
/*      */             symbol = new StaticError(symbol);
/*      */           return accessBase(symbol, paramDiagnosticPosition, paramEnv.enclClass.sym.type, paramName, true);
/*      */         }
/*      */       }
/*      */       if ((env.enclClass.sym.flags() & 0x8L) != 0L)
/*      */         bool = true;
/*      */       env = env.outer;
/*      */     }
/*      */     if (paramTypeSymbol.isInterface() && paramName == this.names._super && !isStatic(paramEnv) && this.types.isDirectSuperInterface(paramTypeSymbol, (Symbol.TypeSymbol)paramEnv.enclClass.sym)) {
/*      */       for (Type type : pruneInterfaces(paramEnv.enclClass.type)) {
/*      */         if (type.tsym == paramTypeSymbol) {
/*      */           ((AttrContext)paramEnv.info).defaultSuperCallSite = type;
/*      */           return (Symbol)new Symbol.VarSymbol(0L, this.names._super, this.types.asSuper(paramEnv.enclClass.type, (Symbol)paramTypeSymbol), (Symbol)paramEnv.enclClass.sym);
/*      */         }
/*      */       }
/*      */       for (Type type : this.types.interfaces(paramEnv.enclClass.type)) {
/*      */         if (type.tsym.isSubClass((Symbol)paramTypeSymbol, this.types) && type.tsym != paramTypeSymbol) {
/*      */           this.log.error(paramDiagnosticPosition, "illegal.default.super.call", new Object[] { paramTypeSymbol, this.diags.fragment("redundant.supertype", new Object[] { paramTypeSymbol, type }) });
/*      */           return (Symbol)this.syms.errSymbol;
/*      */         }
/*      */       }
/*      */       Assert.error();
/*      */     }
/*      */     this.log.error(paramDiagnosticPosition, "not.encl.class", new Object[] { paramTypeSymbol });
/*      */     return (Symbol)this.syms.errSymbol;
/*      */   }
/*      */
/*      */   private List<Type> pruneInterfaces(Type paramType) {
/*      */     ListBuffer listBuffer = new ListBuffer();
/*      */     for (Type type : this.types.interfaces(paramType)) {
/*      */       boolean bool = true;
/*      */       for (Type type1 : this.types.interfaces(paramType)) {
/*      */         if (type != type1 && this.types.isSubtypeNoCapture(type1, type))
/*      */           bool = false;
/*      */       }
/*      */       if (bool)
/*      */         listBuffer.append(type);
/*      */     }
/*      */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   Symbol resolveSelfContaining(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Symbol paramSymbol, boolean paramBoolean) {
/*      */     Symbol symbol = resolveSelfContainingInternal(paramEnv, paramSymbol, paramBoolean);
/*      */     if (symbol == null) {
/*      */       this.log.error(paramDiagnosticPosition, "encl.class.required", new Object[] { paramSymbol });
/*      */       return (Symbol)this.syms.errSymbol;
/*      */     }
/*      */     return accessBase(symbol, paramDiagnosticPosition, paramEnv.enclClass.sym.type, symbol.name, true);
/*      */   }
/*      */
/*      */   boolean hasEnclosingInstance(Env<AttrContext> paramEnv, Type paramType) {
/*      */     Symbol symbol = resolveSelfContainingInternal(paramEnv, (Symbol)paramType.tsym, false);
/*      */     return (symbol != null && symbol.kind < 128);
/*      */   }
/*      */
/*      */   private Symbol resolveSelfContainingInternal(Env<AttrContext> paramEnv, Symbol paramSymbol, boolean paramBoolean) {
/*      */     Name name = this.names._this;
/*      */     Env<AttrContext> env = paramBoolean ? paramEnv.outer : paramEnv;
/*      */     boolean bool = false;
/*      */     if (env != null)
/*      */       while (env != null && env.outer != null) {
/*      */         if (isStatic(env))
/*      */           bool = true;
/*      */         if (env.enclClass.sym.isSubClass(paramSymbol.owner, this.types)) {
/*      */           Symbol symbol = (((AttrContext)env.info).scope.lookup(name)).sym;
/*      */           if (symbol != null) {
/*      */             if (bool)
/*      */               symbol = new StaticError(symbol);
/*      */             return symbol;
/*      */           }
/*      */         }
/*      */         if ((env.enclClass.sym.flags() & 0x8L) != 0L)
/*      */           bool = true;
/*      */         env = env.outer;
/*      */       }
/*      */     return null;
/*      */   }
/*      */
/*      */   Type resolveImplicitThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType) {
/*      */     return resolveImplicitThis(paramDiagnosticPosition, paramEnv, paramType, false);
/*      */   }
/*      */
/*      */   Type resolveImplicitThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType, boolean paramBoolean) {
/*      */     Type type = (((paramType.tsym.owner.kind & 0x14) != 0) ? resolveSelf(paramDiagnosticPosition, paramEnv, (paramType.getEnclosingType()).tsym, this.names._this) : resolveSelfContaining(paramDiagnosticPosition, paramEnv, (Symbol)paramType.tsym, paramBoolean)).type;
/*      */     if (((AttrContext)paramEnv.info).isSelfCall && type.tsym == paramEnv.enclClass.sym)
/*      */       this.log.error(paramDiagnosticPosition, "cant.ref.before.ctor.called", new Object[] { "this" });
/*      */     return type;
/*      */   }
/*      */
/*      */   public void logAccessErrorInternal(Env<AttrContext> paramEnv, JCTree paramJCTree, Type paramType) {
/*      */     AccessError accessError = new AccessError(paramEnv, paramEnv.enclClass.type, (Symbol)paramType.tsym);
/*      */     logResolveError(accessError, paramJCTree.pos(), (Symbol)paramEnv.enclClass.sym, paramEnv.enclClass.type, null, null, null);
/*      */   }
/*      */
/*      */   private void logResolveError(ResolveError paramResolveError, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Type paramType, Name paramName, List<Type> paramList1, List<Type> paramList2) {
/*      */     JCDiagnostic jCDiagnostic = paramResolveError.getDiagnostic(JCDiagnostic.DiagnosticType.ERROR, paramDiagnosticPosition, paramSymbol, paramType, paramName, paramList1, paramList2);
/*      */     if (jCDiagnostic != null) {
/*      */       jCDiagnostic.setFlag(JCDiagnostic.DiagnosticFlag.RESOLVE_ERROR);
/*      */       this.log.report(jCDiagnostic);
/*      */     }
/*      */   }
/*      */
/*      */   public Object methodArguments(List<Type> paramList) {
/*      */     if (paramList == null || paramList.isEmpty())
/*      */       return this.noArgs;
/*      */     ListBuffer listBuffer = new ListBuffer();
/*      */     for (Type type : paramList) {
/*      */       if (type.hasTag(TypeTag.DEFERRED)) {
/*      */         listBuffer.append(((DeferredAttr.DeferredType)type).tree);
/*      */         continue;
/*      */       }
/*      */       listBuffer.append(type);
/*      */     }
/*      */     return listBuffer;
/*      */   }
/*      */
/*      */   abstract class ResolveError extends Symbol {
/*      */     final String debugName;
/*      */
/*      */     ResolveError(int param1Int, String param1String) {
/*      */       super(param1Int, 0L, null, null, null);
/*      */       this.debugName = param1String;
/*      */     }
/*      */
/*      */     public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/*      */       throw new AssertionError();
/*      */     }
/*      */
/*      */     public String toString() {
/*      */       return this.debugName;
/*      */     }
/*      */
/*      */     public boolean exists() {
/*      */       return false;
/*      */     }
/*      */
/*      */     public boolean isStatic() {
/*      */       return false;
/*      */     }
/*      */
/*      */     protected Symbol access(Name param1Name, TypeSymbol param1TypeSymbol) {
/*      */       return (Symbol)(Resolve.this.types.createErrorType(param1Name, param1TypeSymbol, Resolve.this.syms.errSymbol.type)).tsym;
/*      */     }
/*      */
/*      */     abstract JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2);
/*      */   }
/*      */
/*      */   abstract class InvalidSymbolError extends ResolveError {
/*      */     Symbol sym;
/*      */
/*      */     InvalidSymbolError(int param1Int, Symbol param1Symbol, String param1String) {
/*      */       super(param1Int, param1String);
/*      */       this.sym = param1Symbol;
/*      */     }
/*      */
/*      */     public boolean exists() {
/*      */       return true;
/*      */     }
/*      */
/*      */     public String toString() {
/*      */       return super.toString() + " wrongSym=" + this.sym;
/*      */     }
/*      */
/*      */     public Symbol access(Name param1Name, TypeSymbol param1TypeSymbol) {
/*      */       if ((this.sym.kind & 0x80) == 0 && (this.sym.kind & 0x2) != 0)
/*      */         return (Symbol)(Resolve.this.types.createErrorType(param1Name, param1TypeSymbol, this.sym.type)).tsym;
/*      */       return this.sym;
/*      */     }
/*      */   }
/*      */
/*      */   class SymbolNotFoundError extends ResolveError {
/*      */     SymbolNotFoundError(int param1Int) {
/*      */       this(param1Int, "symbol not found error");
/*      */     }
/*      */
/*      */     SymbolNotFoundError(int param1Int, String param1String) {
/*      */       super(param1Int, param1String);
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       TypeSymbol typeSymbol;
/*      */       param1List1 = (param1List1 == null) ? List.nil() : param1List1;
/*      */       param1List2 = (param1List2 == null) ? List.nil() : param1List2;
/*      */       if (param1Name == Resolve.this.names.error)
/*      */         return null;
/*      */       if (Resolve.this.syms.operatorNames.contains(param1Name)) {
/*      */         boolean bool = (param1List1.size() == 1) ? true : false;
/*      */         String str1 = (param1List1.size() == 1) ? "operator.cant.be.applied" : "operator.cant.be.applied.1";
/*      */         Type type1 = (Type)param1List1.head;
/*      */         Type type2 = !bool ? (Type)param1List1.tail.head : null;
/*      */         return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, str1, new Object[] { param1Name, type1, type2 });
/*      */       }
/*      */       boolean bool1 = false;
/*      */       if (param1Symbol == null)
/*      */         typeSymbol = param1Type.tsym;
/*      */       if (!((Symbol)typeSymbol).name.isEmpty()) {
/*      */         if (((Symbol)typeSymbol).kind == 1 && !param1Type.tsym.exists())
/*      */           return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "doesnt.exist", new Object[] { typeSymbol });
/*      */         bool1 = (!((Symbol)typeSymbol).name.equals(Resolve.this.names._this) && !((Symbol)typeSymbol).name.equals(Resolve.this.names._super)) ? true : false;
/*      */       }
/*      */       boolean bool2 = ((this.kind == 136 || this.kind == 138) && param1Name == Resolve.this.names.init) ? true : false;
/*      */       Kinds.KindName kindName = bool2 ? Kinds.KindName.CONSTRUCTOR : Kinds.absentKind(this.kind);
/*      */       Name name = bool2 ? param1Type.tsym.name : param1Name;
/*      */       String str = getErrorKey(kindName, param1List2.nonEmpty(), bool1);
/*      */       if (bool1)
/*      */         return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, str, new Object[] { kindName, name, param1List2, args(param1List1), getLocationDiag((Symbol)typeSymbol, param1Type) });
/*      */       return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, str, new Object[] { kindName, name, param1List2, args(param1List1) });
/*      */     }
/*      */
/*      */     private Object args(List<Type> param1List) {
/*      */       return param1List.isEmpty() ? param1List : Resolve.this.methodArguments(param1List);
/*      */     }
/*      */
/*      */     private String getErrorKey(Kinds.KindName param1KindName, boolean param1Boolean1, boolean param1Boolean2) {
/*      */       // Byte code:
/*      */       //   0: ldc 'cant.resolve'
/*      */       //   2: astore #4
/*      */       //   4: iload_3
/*      */       //   5: ifeq -> 13
/*      */       //   8: ldc '.location'
/*      */       //   10: goto -> 15
/*      */       //   13: ldc ''
/*      */       //   15: astore #5
/*      */       //   17: getstatic com/sun/tools/javac/comp/Resolve$15.$SwitchMap$com$sun$tools$javac$code$Kinds$KindName : [I
/*      */       //   20: aload_1
/*      */       //   21: invokevirtual ordinal : ()I
/*      */       //   24: iaload
/*      */       //   25: lookupswitch default -> 105, 1 -> 52, 2 -> 52
/*      */       //   52: new java/lang/StringBuilder
/*      */       //   55: dup
/*      */       //   56: invokespecial <init> : ()V
/*      */       //   59: aload #5
/*      */       //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   64: ldc '.args'
/*      */       //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   69: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   72: astore #5
/*      */       //   74: new java/lang/StringBuilder
/*      */       //   77: dup
/*      */       //   78: invokespecial <init> : ()V
/*      */       //   81: aload #5
/*      */       //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   86: iload_2
/*      */       //   87: ifeq -> 95
/*      */       //   90: ldc '.params'
/*      */       //   92: goto -> 97
/*      */       //   95: ldc ''
/*      */       //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   100: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   103: astore #5
/*      */       //   105: new java/lang/StringBuilder
/*      */       //   108: dup
/*      */       //   109: invokespecial <init> : ()V
/*      */       //   112: aload #4
/*      */       //   114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   117: aload #5
/*      */       //   119: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   122: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   125: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3710	-> 0
/*      */       //   #3711	-> 4
/*      */       //   #3712	-> 17
/*      */       //   #3715	-> 52
/*      */       //   #3716	-> 74
/*      */       //   #3719	-> 105
/*      */     }
/*      */
/*      */     private JCDiagnostic getLocationDiag(Symbol param1Symbol, Type param1Type) {
/*      */       if (param1Symbol.kind == 4)
/*      */         return Resolve.this.diags.fragment("location.1", new Object[] { Kinds.kindName(param1Symbol), param1Symbol, param1Symbol.type });
/*      */       return Resolve.this.diags.fragment("location", new Object[] { Kinds.typeKindName(param1Type), param1Type, null });
/*      */     }
/*      */   }
/*      */
/*      */   class InapplicableSymbolError extends ResolveError {
/*      */     protected MethodResolutionContext resolveContext;
/*      */
/*      */     InapplicableSymbolError(MethodResolutionContext param1MethodResolutionContext) {
/*      */       this(135, "inapplicable symbol error", param1MethodResolutionContext);
/*      */     }
/*      */
/*      */     protected InapplicableSymbolError(int param1Int, String param1String, MethodResolutionContext param1MethodResolutionContext) {
/*      */       super(param1Int, param1String);
/*      */       this.resolveContext = param1MethodResolutionContext;
/*      */     }
/*      */
/*      */     public String toString() {
/*      */       return super.toString();
/*      */     }
/*      */
/*      */     public boolean exists() {
/*      */       return true;
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       if (param1Name == Resolve.this.names.error)
/*      */         return null;
/*      */       if (Resolve.this.syms.operatorNames.contains(param1Name)) {
/*      */         boolean bool = (param1List1.size() == 1) ? true : false;
/*      */         String str = (param1List1.size() == 1) ? "operator.cant.be.applied" : "operator.cant.be.applied.1";
/*      */         Type type1 = (Type)param1List1.head;
/*      */         Type type2 = !bool ? (Type)param1List1.tail.head : null;
/*      */         return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, str, new Object[] { param1Name, type1, type2 });
/*      */       }
/*      */       Pair<Symbol, JCDiagnostic> pair = errCandidate();
/*      */       if (Resolve.this.compactMethodDiags)
/*      */         for (Map.Entry<MethodResolutionDiagHelper.Template, MethodResolutionDiagHelper.DiagnosticRewriter> entry : MethodResolutionDiagHelper.rewriters.entrySet()) {
/*      */           if (((MethodResolutionDiagHelper.Template)entry.getKey()).matches(pair.snd)) {
/*      */             JCDiagnostic jCDiagnostic = ((MethodResolutionDiagHelper.DiagnosticRewriter)entry.getValue()).rewriteDiagnostic(Resolve.this.diags, param1DiagnosticPosition, Resolve.this.log.currentSource(), param1DiagnosticType, (JCDiagnostic)pair.snd);
/*      */             jCDiagnostic.setFlag(JCDiagnostic.DiagnosticFlag.COMPRESSED);
/*      */             return jCDiagnostic;
/*      */           }
/*      */         }
/*      */       Symbol symbol = ((Symbol)pair.fst).asMemberOf(param1Type, Resolve.this.types);
/*      */       return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "cant.apply.symbol", new Object[] { Kinds.kindName(symbol), (symbol.name == this.this$0.names.init) ? symbol.owner.name : symbol.name, this.this$0.methodArguments(symbol.type.getParameterTypes()), this.this$0.methodArguments(param1List1), Kinds.kindName(symbol.owner), symbol.owner.type, pair.snd });
/*      */     }
/*      */
/*      */     public Symbol access(Name param1Name, TypeSymbol param1TypeSymbol) {
/*      */       return (Symbol)(Resolve.this.types.createErrorType(param1Name, param1TypeSymbol, Resolve.this.syms.errSymbol.type)).tsym;
/*      */     }
/*      */
/*      */     protected Pair<Symbol, JCDiagnostic> errCandidate() {
/*      */       MethodResolutionContext.Candidate candidate = null;
/*      */       for (MethodResolutionContext.Candidate candidate1 : this.resolveContext.candidates) {
/*      */         if (candidate1.isApplicable())
/*      */           continue;
/*      */         candidate = candidate1;
/*      */       }
/*      */       Assert.checkNonNull(candidate);
/*      */       return new Pair(candidate.sym, candidate.details);
/*      */     }
/*      */   }
/*      */
/*      */   class InapplicableSymbolsError extends InapplicableSymbolError {
/*      */     InapplicableSymbolsError(MethodResolutionContext param1MethodResolutionContext) {
/*      */       super(134, "inapplicable symbols", param1MethodResolutionContext);
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       Map<Symbol, JCDiagnostic> map1 = mapCandidates();
/*      */       Map<Symbol, JCDiagnostic> map2 = Resolve.this.compactMethodDiags ? filterCandidates(map1) : mapCandidates();
/*      */       if (map2.isEmpty())
/*      */         map2 = map1;
/*      */       boolean bool = (map1.size() != map2.size()) ? true : false;
/*      */       if (map2.size() > 1) {
/*      */         JCDiagnostic jCDiagnostic = Resolve.this.diags.create(param1DiagnosticType, null, bool ? EnumSet.<JCDiagnostic.DiagnosticFlag>of(JCDiagnostic.DiagnosticFlag.COMPRESSED) : EnumSet.<JCDiagnostic.DiagnosticFlag>noneOf(JCDiagnostic.DiagnosticFlag.class), Resolve.this.log.currentSource(), param1DiagnosticPosition, "cant.apply.symbols", new Object[] { (param1Name == this.this$0.names.init) ? Kinds.KindName.CONSTRUCTOR : Kinds.absentKind(this.kind), (param1Name == this.this$0.names.init) ? param1Type.tsym.name : param1Name, this.this$0.methodArguments(param1List1) });
/*      */         return (JCDiagnostic)new JCDiagnostic.MultilineDiagnostic(jCDiagnostic, candidateDetails(map2, param1Type));
/*      */       }
/*      */       if (map2.size() == 1) {
/*      */         Map.Entry entry = map2.entrySet().iterator().next();
/*      */         final Pair p = new Pair(entry.getKey(), entry.getValue());
/*      */         JCDiagnostic jCDiagnostic = (new InapplicableSymbolError(this.resolveContext) {
/*      */             protected Pair<Symbol, JCDiagnostic> errCandidate() {
/*      */               return p;
/*      */             }
/*      */           }).getDiagnostic(param1DiagnosticType, param1DiagnosticPosition, param1Symbol, param1Type, param1Name, param1List1, param1List2);
/*      */         if (bool)
/*      */           jCDiagnostic.setFlag(JCDiagnostic.DiagnosticFlag.COMPRESSED);
/*      */         return jCDiagnostic;
/*      */       }
/*      */       return (new SymbolNotFoundError(136)).getDiagnostic(param1DiagnosticType, param1DiagnosticPosition, param1Symbol, param1Type, param1Name, param1List1, param1List2);
/*      */     }
/*      */
/*      */     private Map<Symbol, JCDiagnostic> mapCandidates() {
/*      */       LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */       for (MethodResolutionContext.Candidate candidate : this.resolveContext.candidates) {
/*      */         if (candidate.isApplicable())
/*      */           continue;
/*      */         linkedHashMap.put(candidate.sym, candidate.details);
/*      */       }
/*      */       return (Map)linkedHashMap;
/*      */     }
/*      */
/*      */     Map<Symbol, JCDiagnostic> filterCandidates(Map<Symbol, JCDiagnostic> param1Map) {
/*      */       LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */       for (Map.Entry<Symbol, JCDiagnostic> entry : param1Map.entrySet()) {
/*      */         JCDiagnostic jCDiagnostic = (JCDiagnostic)entry.getValue();
/*      */         if (!(new MethodResolutionDiagHelper.Template(MethodCheckDiag.ARITY_MISMATCH.regex(), new MethodResolutionDiagHelper.Template[0])).matches(jCDiagnostic))
/*      */           linkedHashMap.put(entry.getKey(), jCDiagnostic);
/*      */       }
/*      */       return (Map)linkedHashMap;
/*      */     }
/*      */
/*      */     private List<JCDiagnostic> candidateDetails(Map<Symbol, JCDiagnostic> param1Map, Type param1Type) {
/*      */       List<JCDiagnostic> list = List.nil();
/*      */       for (Map.Entry<Symbol, JCDiagnostic> entry : param1Map.entrySet()) {
/*      */         Symbol symbol = (Symbol)entry.getKey();
/*      */         JCDiagnostic jCDiagnostic = Resolve.this.diags.fragment("inapplicable.method", new Object[] { Kinds.kindName(symbol), symbol.location(param1Type, this.this$0.types), symbol.asMemberOf(param1Type, this.this$0.types), entry.getValue() });
/*      */         list = list.prepend(jCDiagnostic);
/*      */       }
/*      */       return list;
/*      */     }
/*      */   }
/*      */
/*      */   class AccessError extends InvalidSymbolError {
/*      */     private Env<AttrContext> env;
/*      */     private Type site;
/*      */
/*      */     AccessError(Symbol param1Symbol) {
/*      */       this((Env<AttrContext>)null, (Type)null, param1Symbol);
/*      */     }
/*      */
/*      */     AccessError(Env<AttrContext> param1Env, Type param1Type, Symbol param1Symbol) {
/*      */       super(130, param1Symbol, "access error");
/*      */       this.env = param1Env;
/*      */       this.site = param1Type;
/*      */       if (Resolve.this.debugResolve)
/*      */         Resolve.this.log.error("proc.messager", new Object[] { param1Symbol + " @ " + param1Type + " is inaccessible." });
/*      */     }
/*      */
/*      */     public boolean exists() {
/*      */       return false;
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       if (this.sym.owner.type.hasTag(TypeTag.ERROR))
/*      */         return null;
/*      */       if (this.sym.name == Resolve.this.names.init && this.sym.owner != param1Type.tsym)
/*      */         return (new SymbolNotFoundError(136)).getDiagnostic(param1DiagnosticType, param1DiagnosticPosition, param1Symbol, param1Type, param1Name, param1List1, param1List2);
/*      */       if ((this.sym.flags() & 0x1L) != 0L || (this.env != null && this.site != null && !Resolve.this.isAccessible(this.env, this.site)))
/*      */         return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "not.def.access.class.intf.cant.access", new Object[] { this.sym, this.sym.location() });
/*      */       if ((this.sym.flags() & 0x6L) != 0L)
/*      */         return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "report.access", new Object[] { this.sym, Flags.asFlagSet(this.sym.flags() & 0x6L), this.sym.location() });
/*      */       return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "not.def.public.cant.access", new Object[] { this.sym, this.sym.location() });
/*      */     }
/*      */   }
/*      */
/*      */   class StaticError extends InvalidSymbolError {
/*      */     StaticError(Symbol param1Symbol) {
/*      */       super(131, param1Symbol, "static error");
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       Object object = (this.sym.kind == 2 && this.sym.type.hasTag(TypeTag.CLASS)) ? (Resolve.this.types.erasure(this.sym.type)).tsym : this.sym;
/*      */       return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "non-static.cant.be.ref", new Object[] { Kinds.kindName(this.sym), object });
/*      */     }
/*      */   }
/*      */
/*      */   class AmbiguityError extends ResolveError {
/*      */     List<Symbol> ambiguousSyms = List.nil();
/*      */
/*      */     public boolean exists() {
/*      */       return true;
/*      */     }
/*      */
/*      */     AmbiguityError(Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */       super(129, "ambiguity error");
/*      */       this.ambiguousSyms = flatten(param1Symbol2).appendList(flatten(param1Symbol1));
/*      */     }
/*      */
/*      */     private List<Symbol> flatten(Symbol param1Symbol) {
/*      */       if (param1Symbol.kind == 129)
/*      */         return ((AmbiguityError)param1Symbol.baseSymbol()).ambiguousSyms;
/*      */       return List.of(param1Symbol);
/*      */     }
/*      */
/*      */     AmbiguityError addAmbiguousSymbol(Symbol param1Symbol) {
/*      */       this.ambiguousSyms = this.ambiguousSyms.prepend(param1Symbol);
/*      */       return this;
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       List list = this.ambiguousSyms.reverse();
/*      */       Symbol symbol1 = (Symbol)list.head;
/*      */       Symbol symbol2 = (Symbol)list.tail.head;
/*      */       Name name = symbol1.name;
/*      */       if (name == Resolve.this.names.init)
/*      */         name = symbol1.owner.name;
/*      */       return Resolve.this.diags.create(param1DiagnosticType, Resolve.this.log.currentSource(), param1DiagnosticPosition, "ref.ambiguous", new Object[] { name, Kinds.kindName(symbol1), symbol1, symbol1.location(param1Type, this.this$0.types), Kinds.kindName(symbol2), symbol2, symbol2.location(param1Type, this.this$0.types) });
/*      */     }
/*      */
/*      */     Symbol mergeAbstracts(Type param1Type) {
/*      */       List list = this.ambiguousSyms.reverse();
/*      */       for (Symbol symbol : list) {
/*      */         Type type = Resolve.this.types.memberType(param1Type, symbol);
/*      */         boolean bool = true;
/*      */         List<Type> list1 = type.getThrownTypes();
/*      */         for (Symbol symbol1 : list) {
/*      */           Type type1 = Resolve.this.types.memberType(param1Type, symbol1);
/*      */           if ((symbol1.flags() & 0x400L) == 0L || !Resolve.this.types.overrideEquivalent(type, type1) || !Resolve.this.types.isSameTypes(symbol.erasure(Resolve.this.types).getParameterTypes(), symbol1.erasure(Resolve.this.types).getParameterTypes()))
/*      */             return this;
/*      */           Type type2 = Resolve.this.mostSpecificReturnType(type, type1);
/*      */           if (type2 == null || type2 != type) {
/*      */             bool = false;
/*      */             break;
/*      */           }
/*      */           list1 = Resolve.this.chk.intersect(list1, type1.getThrownTypes());
/*      */         }
/*      */         if (bool)
/*      */           return (list1 == type.getThrownTypes()) ? symbol : (Symbol)new MethodSymbol(symbol.flags(), symbol.name, Resolve.this.types.createMethodTypeWithThrown(symbol.type, list1), symbol.owner);
/*      */       }
/*      */       return this;
/*      */     }
/*      */
/*      */     protected Symbol access(Name param1Name, TypeSymbol param1TypeSymbol) {
/*      */       Symbol symbol = (Symbol)this.ambiguousSyms.last();
/*      */       return (symbol.kind == 2) ? (Symbol)(Resolve.this.types.createErrorType(param1Name, param1TypeSymbol, symbol.type)).tsym : symbol;
/*      */     }
/*      */   }
/*      */
/*      */   class BadVarargsMethod extends ResolveError {
/*      */     ResolveError delegatedError;
/*      */
/*      */     BadVarargsMethod(ResolveError param1ResolveError) {
/*      */       super(param1ResolveError.kind, "badVarargs");
/*      */       this.delegatedError = param1ResolveError;
/*      */     }
/*      */
/*      */     public Symbol baseSymbol() {
/*      */       return this.delegatedError.baseSymbol();
/*      */     }
/*      */
/*      */     protected Symbol access(Name param1Name, TypeSymbol param1TypeSymbol) {
/*      */       return this.delegatedError.access(param1Name, param1TypeSymbol);
/*      */     }
/*      */
/*      */     public boolean exists() {
/*      */       return true;
/*      */     }
/*      */
/*      */     JCDiagnostic getDiagnostic(JCDiagnostic.DiagnosticType param1DiagnosticType, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, Type param1Type, Name param1Name, List<Type> param1List1, List<Type> param1List2) {
/*      */       return this.delegatedError.getDiagnostic(param1DiagnosticType, param1DiagnosticPosition, param1Symbol, param1Type, param1Name, param1List1, param1List2);
/*      */     }
/*      */   }
/*      */
/*      */   static class MethodResolutionDiagHelper {
/*      */     static class Template {
/*      */       String regex;
/*      */       Template[] subTemplates;
/*      */
/*      */       Template(String param2String, Template... param2VarArgs) {
/*      */         this.regex = param2String;
/*      */         this.subTemplates = param2VarArgs;
/*      */       }
/*      */
/*      */       boolean matches(Object param2Object) {
/*      */         JCDiagnostic jCDiagnostic = (JCDiagnostic)param2Object;
/*      */         Object[] arrayOfObject = jCDiagnostic.getArgs();
/*      */         if (!jCDiagnostic.getCode().matches(this.regex) || this.subTemplates.length != (jCDiagnostic.getArgs()).length)
/*      */           return false;
/*      */         for (byte b = 0; b < arrayOfObject.length; b++) {
/*      */           if (!this.subTemplates[b].matches(arrayOfObject[b]))
/*      */             return false;
/*      */         }
/*      */         return true;
/*      */       }
/*      */     }
/*      */
/*      */     static final Template skip = new Template("", new Template[0]) {
/*      */         boolean matches(Object param2Object) {
/*      */           return true;
/*      */         }
/*      */       };
/*      */
/*      */     static final Map<Template, DiagnosticRewriter> rewriters = new LinkedHashMap<>();
/*      */
/*      */     static {
/*      */       String str = MethodCheckDiag.ARG_MISMATCH.regex();
/*      */       rewriters.put(new Template(str, new Template[] { skip }), new DiagnosticRewriter() {
/*      */             public JCDiagnostic rewriteDiagnostic(JCDiagnostic.Factory param2Factory, JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, DiagnosticSource param2DiagnosticSource, JCDiagnostic.DiagnosticType param2DiagnosticType, JCDiagnostic param2JCDiagnostic) {
/*      */               JCDiagnostic jCDiagnostic = (JCDiagnostic)param2JCDiagnostic.getArgs()[0];
/*      */               JCDiagnostic.DiagnosticPosition diagnosticPosition = param2JCDiagnostic.getDiagnosticPosition();
/*      */               if (diagnosticPosition == null)
/*      */                 diagnosticPosition = param2DiagnosticPosition;
/*      */               return param2Factory.create(param2DiagnosticType, param2DiagnosticSource, diagnosticPosition, "prob.found.req", new Object[] { jCDiagnostic });
/*      */             }
/*      */           });
/*      */     }
/*      */
/*      */     static interface DiagnosticRewriter {
/*      */       JCDiagnostic rewriteDiagnostic(JCDiagnostic.Factory param2Factory, JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, DiagnosticSource param2DiagnosticSource, JCDiagnostic.DiagnosticType param2DiagnosticType, JCDiagnostic param2JCDiagnostic);
/*      */     }
/*      */   }
/*      */
/*      */   static class Template {
/*      */     String regex;
/*      */     Template[] subTemplates;
/*      */
/*      */     Template(String param1String, Template... param1VarArgs) {
/*      */       this.regex = param1String;
/*      */       this.subTemplates = param1VarArgs;
/*      */     }
/*      */
/*      */     boolean matches(Object param1Object) {
/*      */       JCDiagnostic jCDiagnostic = (JCDiagnostic)param1Object;
/*      */       Object[] arrayOfObject = jCDiagnostic.getArgs();
/*      */       if (!jCDiagnostic.getCode().matches(this.regex) || this.subTemplates.length != (jCDiagnostic.getArgs()).length)
/*      */         return false;
/*      */       for (byte b = 0; b < arrayOfObject.length; b++) {
/*      */         if (!this.subTemplates[b].matches(arrayOfObject[b]))
/*      */           return false;
/*      */       }
/*      */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   enum MethodResolutionPhase {
/*      */     BASIC(false, false),
/*      */     BOX(true, false),
/*      */     VARARITY(true, true) {
/*      */       public Symbol mergeResults(Symbol param2Symbol1, Symbol param2Symbol2) {
/*      */         Assert.check((param2Symbol1.kind >= 128 && param2Symbol1.kind != 129));
/*      */         if (param2Symbol2.kind < 128)
/*      */           return param2Symbol2;
/*      */         switch (param2Symbol1.kind) {
/*      */           case 134:
/*      */           case 135:
/*      */             switch (param2Symbol2.kind) {
/*      */               case 135:
/*      */                 return (param2Symbol1.kind == 134) ? param2Symbol1 : param2Symbol2;
/*      */               case 136:
/*      */                 return param2Symbol1;
/*      */             }
/*      */             return param2Symbol2;
/*      */         }
/*      */         return param2Symbol1;
/*      */       }
/*      */     };
/*      */
/*      */     final boolean isBoxingRequired;
/*      */     final boolean isVarargsRequired;
/*      */
/*      */     MethodResolutionPhase(boolean param1Boolean1, boolean param1Boolean2) {
/*      */       this.isBoxingRequired = param1Boolean1;
/*      */       this.isVarargsRequired = param1Boolean2;
/*      */     }
/*      */
/*      */     public boolean isBoxingRequired() {
/*      */       return this.isBoxingRequired;
/*      */     }
/*      */
/*      */     public boolean isVarargsRequired() {
/*      */       return this.isVarargsRequired;
/*      */     }
/*      */
/*      */     public boolean isApplicable(boolean param1Boolean1, boolean param1Boolean2) {
/*      */       return ((param1Boolean2 || !this.isVarargsRequired) && (param1Boolean1 || !this.isBoxingRequired));
/*      */     }
/*      */
/*      */     public Symbol mergeResults(Symbol param1Symbol1, Symbol param1Symbol2) {
/*      */       return param1Symbol2;
/*      */     }
/*      */   }
/*      */
/*      */   class MethodResolutionContext {
/*      */     private List<Candidate> candidates = List.nil();
/*      */     MethodResolutionPhase step = null;
/*      */     MethodCheck methodCheck = Resolve.this.resolveMethodCheck;
/*      */     private boolean internalResolution = false;
/*      */     private DeferredAttr.AttrMode attrMode = DeferredAttr.AttrMode.SPECULATIVE;
/*      */
/*      */     void addInapplicableCandidate(Symbol param1Symbol, JCDiagnostic param1JCDiagnostic) {
/*      */       Candidate candidate = new Candidate(Resolve.this.currentResolutionContext.step, param1Symbol, param1JCDiagnostic, null);
/*      */       this.candidates = this.candidates.append(candidate);
/*      */     }
/*      */
/*      */     void addApplicableCandidate(Symbol param1Symbol, Type param1Type) {
/*      */       Candidate candidate = new Candidate(Resolve.this.currentResolutionContext.step, param1Symbol, null, param1Type);
/*      */       this.candidates = this.candidates.append(candidate);
/*      */     }
/*      */
/*      */     DeferredAttr.DeferredAttrContext deferredAttrContext(Symbol param1Symbol, Infer.InferenceContext param1InferenceContext, Attr.ResultInfo param1ResultInfo, Warner param1Warner) {
/*      */       DeferredAttr.DeferredAttrContext deferredAttrContext = (param1ResultInfo == null) ? Resolve.this.deferredAttr.emptyDeferredAttrContext : param1ResultInfo.checkContext.deferredAttrContext();
/*      */       Resolve.this.deferredAttr.getClass();
/*      */       return new DeferredAttr.DeferredAttrContext(Resolve.this.deferredAttr, this.attrMode, param1Symbol, this.step, param1InferenceContext, deferredAttrContext, param1Warner);
/*      */     }
/*      */
/*      */     class Candidate {
/*      */       final MethodResolutionPhase step;
/*      */       final Symbol sym;
/*      */       final JCDiagnostic details;
/*      */       final Type mtype;
/*      */
/*      */       private Candidate(MethodResolutionPhase param2MethodResolutionPhase, Symbol param2Symbol, JCDiagnostic param2JCDiagnostic, Type param2Type) {
/*      */         this.step = param2MethodResolutionPhase;
/*      */         this.sym = param2Symbol;
/*      */         this.details = param2JCDiagnostic;
/*      */         this.mtype = param2Type;
/*      */       }
/*      */
/*      */       public boolean equals(Object param2Object) {
/*      */         if (param2Object instanceof Candidate) {
/*      */           Symbol symbol1 = this.sym;
/*      */           Symbol symbol2 = ((Candidate)param2Object).sym;
/*      */           if ((symbol1 != symbol2 && (symbol1.overrides(symbol2, symbol1.owner.type.tsym, Resolve.this.types, false) || symbol2.overrides(symbol1, symbol2.owner.type.tsym, Resolve.this.types, false))) || ((symbol1.isConstructor() || symbol2.isConstructor()) && symbol1.owner != symbol2.owner))
/*      */             return true;
/*      */         }
/*      */         return false;
/*      */       }
/*      */
/*      */       boolean isApplicable() {
/*      */         return (this.mtype != null);
/*      */       }
/*      */     }
/*      */
/*      */     DeferredAttr.AttrMode attrMode() {
/*      */       return this.attrMode;
/*      */     }
/*      */
/*      */     boolean internal() {
/*      */       return this.internalResolution;
/*      */     }
/*      */   }
/*      */
/*      */   class Candidate {
/*      */     final MethodResolutionPhase step;
/*      */     final Symbol sym;
/*      */     final JCDiagnostic details;
/*      */     final Type mtype;
/*      */
/*      */     private Candidate(MethodResolutionPhase param1MethodResolutionPhase, Symbol param1Symbol, JCDiagnostic param1JCDiagnostic, Type param1Type) {
/*      */       this.step = param1MethodResolutionPhase;
/*      */       this.sym = param1Symbol;
/*      */       this.details = param1JCDiagnostic;
/*      */       this.mtype = param1Type;
/*      */     }
/*      */
/*      */     public boolean equals(Object param1Object) {
/*      */       if (param1Object instanceof Candidate) {
/*      */         Symbol symbol1 = this.sym;
/*      */         Symbol symbol2 = ((Candidate)param1Object).sym;
/*      */         if ((symbol1 != symbol2 && (symbol1.overrides(symbol2, symbol1.owner.type.tsym, Resolve.this.types, false) || symbol2.overrides(symbol1, symbol2.owner.type.tsym, Resolve.this.types, false))) || ((symbol1.isConstructor() || symbol2.isConstructor()) && symbol1.owner != symbol2.owner))
/*      */           return true;
/*      */       }
/*      */       return false;
/*      */     }
/*      */
/*      */     boolean isApplicable() {
/*      */       return (this.mtype != null);
/*      */     }
/*      */   }
/*      */
/*      */   static interface LogResolveHelper {
/*      */     boolean resolveDiagnosticNeeded(Type param1Type, List<Type> param1List1, List<Type> param1List2);
/*      */
/*      */     List<Type> getArgumentTypes(ResolveError param1ResolveError, Symbol param1Symbol, Name param1Name, List<Type> param1List);
/*      */   }
/*      */
/*      */   static interface MethodCheck {
/*      */     void argumentsAcceptable(Env<AttrContext> param1Env, DeferredAttr.DeferredAttrContext param1DeferredAttrContext, List<Type> param1List1, List<Type> param1List2, Warner param1Warner);
/*      */
/*      */     MethodCheck mostSpecificCheck(List<Type> param1List, boolean param1Boolean);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Resolve.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
