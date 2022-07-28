/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.DeferredLintHandler;
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
/*      */ import com.sun.tools.javac.jvm.Profile;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Abort;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.MandatoryWarningHandler;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.tools.JavaFileManager;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Check
/*      */ {
/*   67 */   protected static final Context.Key<Check> checkKey = new Context.Key(); private final Names names; private final Log log; private final Resolve rs; private final Symtab syms; private final Enter enter; private final DeferredAttr deferredAttr; private final Infer infer; private final Types types; private final JCDiagnostic.Factory diags; private boolean warnOnSyntheticConflicts; private boolean suppressAbortOnBadClassFile; private boolean enableSunApiLintControl;
/*      */   private final TreeInfo treeinfo;
/*      */   private final JavaFileManager fileManager;
/*      */   private final Profile profile;
/*      */   private final boolean warnOnAccessToSensitiveMembers;
/*      */   private Lint lint;
/*      */   private Symbol.MethodSymbol method;
/*      */   boolean allowGenerics;
/*      */   boolean allowVarargs;
/*      */   boolean allowAnnotations;
/*      */   boolean allowCovariantReturns;
/*      */   boolean allowSimplifiedVarargs;
/*      */   boolean allowDefaultMethods;
/*      */   boolean allowStrictMethodClashCheck;
/*      */   boolean complexInference;
/*      */   char syntheticNameChar;
/*      */   public Map<Name, Symbol.ClassSymbol> compiled;
/*      */   private MandatoryWarningHandler deprecationHandler;
/*      */   private MandatoryWarningHandler uncheckedHandler;
/*      */   private MandatoryWarningHandler sunApiHandler;
/*      */   private DeferredLintHandler deferredLintHandler;
/*      */   CheckContext basicHandler;
/*      */   private static final boolean ignoreAnnotatedCasts = true;
/*      */   Types.UnaryVisitor<Boolean> isTypeArgErroneous;
/*      */   Warner overrideWarner;
/*      */   private Filter<Symbol> equalsHasCodeFilter;
/*      */   private Set<Name> defaultTargets;
/*      */   private final Name[] dfltTargetMeta;
/*      */
/*      */   public static Check instance(Context paramContext) {
/*   97 */     Check check = (Check)paramContext.get(checkKey);
/*   98 */     if (check == null)
/*   99 */       check = new Check(paramContext);
/*  100 */     return check;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Check(Context paramContext) {
/*  196 */     this.compiled = new HashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  496 */     this.basicHandler = new CheckContext() {
/*      */         public void report(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, JCDiagnostic param1JCDiagnostic) {
/*  498 */           Check.this.log.error(param1DiagnosticPosition, "prob.found.req", new Object[] { param1JCDiagnostic });
/*      */         }
/*      */         public boolean compatible(Type param1Type1, Type param1Type2, Warner param1Warner) {
/*  501 */           return Check.this.types.isAssignable(param1Type1, param1Type2, param1Warner);
/*      */         }
/*      */
/*      */         public Warner checkWarner(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Type param1Type1, Type param1Type2) {
/*  505 */           return Check.this.convertWarner(param1DiagnosticPosition, param1Type1, param1Type2);
/*      */         }
/*      */
/*      */         public Infer.InferenceContext inferenceContext() {
/*  509 */           return Check.this.infer.emptyContext;
/*      */         }
/*      */
/*      */         public DeferredAttr.DeferredAttrContext deferredAttrContext() {
/*  513 */           return Check.this.deferredAttr.emptyDeferredAttrContext;
/*      */         }
/*      */
/*      */
/*      */         public String toString() {
/*  518 */           return "CheckContext: basicHandler";
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
/* 1007 */     this.isTypeArgErroneous = new Types.UnaryVisitor<Boolean>() {
/*      */         public Boolean visitType(Type param1Type, Void param1Void) {
/* 1009 */           return Boolean.valueOf(param1Type.isErroneous());
/*      */         }
/*      */
/*      */         public Boolean visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 1013 */           return (Boolean)visit(param1TypeVar.getUpperBound());
/*      */         }
/*      */
/*      */         public Boolean visitCapturedType(Type.CapturedType param1CapturedType, Void param1Void) {
/* 1017 */           return Boolean.valueOf((((Boolean)visit(param1CapturedType.getUpperBound())).booleanValue() || ((Boolean)
/* 1018 */               visit(param1CapturedType.getLowerBound())).booleanValue()));
/*      */         }
/*      */
/*      */         public Boolean visitWildcardType(Type.WildcardType param1WildcardType, Void param1Void) {
/* 1022 */           return (Boolean)visit(param1WildcardType.type);
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
/* 1751 */     this.overrideWarner = new Warner();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1993 */     this.equalsHasCodeFilter = new Filter<Symbol>()
/*      */       {
/* 1995 */         public boolean accepts(Symbol param1Symbol) { return (Symbol.MethodSymbol.implementation_filter.accepts(param1Symbol) && (param1Symbol
/* 1996 */             .flags() & 0x200000000000L) == 0L); }
/*      */       }; paramContext.put(checkKey, this); this.names = Names.instance(paramContext); this.dfltTargetMeta = new Name[] { this.names.PACKAGE, this.names.TYPE, this.names.FIELD, this.names.METHOD, this.names.CONSTRUCTOR, this.names.ANNOTATION_TYPE, this.names.LOCAL_VARIABLE, this.names.PARAMETER }; this.log = Log.instance(paramContext); this.rs = Resolve.instance(paramContext); this.syms = Symtab.instance(paramContext); this.enter = Enter.instance(paramContext); this.deferredAttr = DeferredAttr.instance(paramContext); this.infer = Infer.instance(paramContext); this.types = Types.instance(paramContext); this.diags = JCDiagnostic.Factory.instance(paramContext); Options options = Options.instance(paramContext); this.lint = Lint.instance(paramContext); this.treeinfo = TreeInfo.instance(paramContext); this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class); Source source = Source.instance(paramContext); this.allowGenerics = source.allowGenerics(); this.allowVarargs = source.allowVarargs(); this.allowAnnotations = source.allowAnnotations(); this.allowCovariantReturns = source.allowCovariantReturns(); this.allowSimplifiedVarargs = source.allowSimplifiedVarargs(); this.allowDefaultMethods = source.allowDefaultMethods(); this.allowStrictMethodClashCheck = source.allowStrictMethodClashCheck(); this.complexInference = options.isSet("complexinference"); this.warnOnSyntheticConflicts = options.isSet("warnOnSyntheticConflicts"); this.suppressAbortOnBadClassFile = options.isSet("suppressAbortOnBadClassFile"); this.enableSunApiLintControl = options.isSet("enableSunApiLintControl"); this.warnOnAccessToSensitiveMembers = options.isSet("warnOnAccessToSensitiveMembers"); Target target = Target.instance(paramContext); this.syntheticNameChar = target.syntheticNameChar(); this.profile = Profile.instance(paramContext); boolean bool1 = this.lint.isEnabled(Lint.LintCategory.DEPRECATION); boolean bool2 = this.lint.isEnabled(Lint.LintCategory.UNCHECKED); boolean bool3 = this.lint.isEnabled(Lint.LintCategory.SUNAPI); boolean bool4 = source.enforceMandatoryWarnings(); this.deprecationHandler = new MandatoryWarningHandler(this.log, bool1, bool4, "deprecated", Lint.LintCategory.DEPRECATION); this.uncheckedHandler = new MandatoryWarningHandler(this.log, bool2, bool4, "unchecked", Lint.LintCategory.UNCHECKED); this.sunApiHandler = new MandatoryWarningHandler(this.log, bool3, bool4, "sunapi", null); this.deferredLintHandler = DeferredLintHandler.instance(paramContext);
/*      */   }
/*      */   Lint setLint(Lint paramLint) { Lint lint = this.lint; this.lint = paramLint; return lint; }
/*      */   Symbol.MethodSymbol setMethod(Symbol.MethodSymbol paramMethodSymbol) { Symbol.MethodSymbol methodSymbol = this.method; this.method = paramMethodSymbol; return methodSymbol; }
/*      */   void warnDeprecated(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) { if (!this.lint.isSuppressed(Lint.LintCategory.DEPRECATION)) this.deprecationHandler.report(paramDiagnosticPosition, "has.been.deprecated", new Object[] { paramSymbol, paramSymbol.location() });  }
/*      */   public void warnUnchecked(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) { if (!this.lint.isSuppressed(Lint.LintCategory.UNCHECKED)) this.uncheckedHandler.report(paramDiagnosticPosition, paramString, paramVarArgs);  }
/*      */   void warnUnsafeVararg(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) { if (this.lint.isEnabled(Lint.LintCategory.VARARGS) && this.allowSimplifiedVarargs) this.log.warning(Lint.LintCategory.VARARGS, paramDiagnosticPosition, paramString, paramVarArgs);  }
/*      */   public void warnSunApi(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) { if (!this.lint.isSuppressed(Lint.LintCategory.SUNAPI)) this.sunApiHandler.report(paramDiagnosticPosition, paramString, paramVarArgs);  }
/*      */   public void warnStatic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) { if (this.lint.isEnabled(Lint.LintCategory.STATIC)) this.log.warning(Lint.LintCategory.STATIC, paramDiagnosticPosition, paramString, paramVarArgs);  }
/*      */   public void reportDeferredDiagnostics() { this.deprecationHandler.reportDeferredDiagnostic(); this.uncheckedHandler.reportDeferredDiagnostic(); this.sunApiHandler.reportDeferredDiagnostic(); } public Type completionError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.CompletionFailure paramCompletionFailure) { this.log.error(JCDiagnostic.DiagnosticFlag.NON_DEFERRABLE, paramDiagnosticPosition, "cant.access", new Object[] { paramCompletionFailure.sym, paramCompletionFailure.getDetailValue() }); if (paramCompletionFailure instanceof com.sun.tools.javac.jvm.ClassReader.BadClassFile && !this.suppressAbortOnBadClassFile) throw new Abort();  return this.syms.errType; } Type typeTagError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Object paramObject1, Object paramObject2) { if (paramObject2 instanceof Type && ((Type)paramObject2).hasTag(TypeTag.VOID)) { this.log.error(paramDiagnosticPosition, "illegal.start.of.type", new Object[0]); return this.syms.errType; }  this.log.error(paramDiagnosticPosition, "type.found.req", new Object[] { paramObject2, paramObject1 }); return this.types.createErrorType((paramObject2 instanceof Type) ? (Type)paramObject2 : this.syms.errType); } void earlyRefError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) { this.log.error(paramDiagnosticPosition, "cant.ref.before.ctor.called", new Object[] { paramSymbol }); } void duplicateError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) { if (!paramSymbol.type.isErroneous()) { Symbol symbol = paramSymbol.location(); if (symbol.kind == 16 && ((Symbol.MethodSymbol)symbol).isStaticOrInstanceInit()) { this.log.error(paramDiagnosticPosition, "already.defined.in.clinit", new Object[] { Kinds.kindName(paramSymbol), paramSymbol, Kinds.kindName(paramSymbol.location()), Kinds.kindName((Symbol)paramSymbol.location().enclClass()), paramSymbol.location().enclClass() }); } else { this.log.error(paramDiagnosticPosition, "already.defined", new Object[] { Kinds.kindName(paramSymbol), paramSymbol, Kinds.kindName(paramSymbol.location()), paramSymbol.location() }); }  }  } void varargsDuplicateError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol1, Symbol paramSymbol2) { if (!paramSymbol1.type.isErroneous() && !paramSymbol2.type.isErroneous()) this.log.error(paramDiagnosticPosition, "array.and.varargs", new Object[] { paramSymbol1, paramSymbol2, paramSymbol2.location() });  } void checkTransparentVar(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol, Scope paramScope) { if (paramScope.next != null) { Scope.Entry entry = paramScope.next.lookup(paramVarSymbol.name); for (; entry.scope != null && entry.sym.owner == paramVarSymbol.owner; entry = entry.next()) { if (entry.sym.kind == 4 && (entry.sym.owner.kind & 0x14) != 0 && paramVarSymbol.name != this.names.error) { duplicateError(paramDiagnosticPosition, entry.sym); return; }  }  }  } void checkTransparentClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol, Scope paramScope) { if (paramScope.next != null) { Scope.Entry entry = paramScope.next.lookup(paramClassSymbol.name); for (; entry.scope != null && entry.sym.owner == paramClassSymbol.owner; entry = entry.next()) { if (entry.sym.kind == 2 && !entry.sym.type.hasTag(TypeTag.TYPEVAR) && (entry.sym.owner.kind & 0x14) != 0 && paramClassSymbol.name != this.names.error) { duplicateError(paramDiagnosticPosition, entry.sym); return; }  }  }  } boolean checkUniqueClassName(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Name paramName, Scope paramScope) { for (Scope.Entry entry = paramScope.lookup(paramName); entry.scope == paramScope; entry = entry.next()) { if (entry.sym.kind == 2 && entry.sym.name != this.names.error) { duplicateError(paramDiagnosticPosition, entry.sym); return false; }  }  for (Symbol symbol = paramScope.owner; symbol != null; symbol = symbol.owner) { if (symbol.kind == 2 && symbol.name == paramName && symbol.name != this.names.error) { duplicateError(paramDiagnosticPosition, symbol); return true; }  }  return true; } Name localClassName(Symbol.ClassSymbol paramClassSymbol) { for (byte b = 1;; b++) { Name name = this.names.fromString("" + (paramClassSymbol.owner.enclClass()).flatname + this.syntheticNameChar + b + paramClassSymbol.name); if (this.compiled.get(name) == null) return name;  }  } static class NestedCheckContext implements CheckContext {
/* 2007 */     CheckContext enclosingContext; NestedCheckContext(CheckContext param1CheckContext) { this.enclosingContext = param1CheckContext; } public boolean compatible(Type param1Type1, Type param1Type2, Warner param1Warner) { return this.enclosingContext.compatible(param1Type1, param1Type2, param1Warner); } public void report(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, JCDiagnostic param1JCDiagnostic) { this.enclosingContext.report(param1DiagnosticPosition, param1JCDiagnostic); } public Warner checkWarner(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Type param1Type1, Type param1Type2) { return this.enclosingContext.checkWarner(param1DiagnosticPosition, param1Type1, param1Type2); } public Infer.InferenceContext inferenceContext() { return this.enclosingContext.inferenceContext(); } public DeferredAttr.DeferredAttrContext deferredAttrContext() { return this.enclosingContext.deferredAttrContext(); } } Type checkType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) { return checkType(paramDiagnosticPosition, paramType1, paramType2, this.basicHandler); } public void checkClassOverrideEqualsAndHashIfNeeded(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) { if (paramClassSymbol == (Symbol.ClassSymbol)this.syms.objectType.tsym || paramClassSymbol
/* 2008 */       .isInterface() || paramClassSymbol.isEnum() || (paramClassSymbol
/* 2009 */       .flags() & 0x2000L) != 0L || (paramClassSymbol
/* 2010 */       .flags() & 0x400L) != 0L)
/*      */       return;
/* 2012 */     if (paramClassSymbol.isAnonymous()) {
/* 2013 */       List list = this.types.interfaces(paramClassSymbol.type);
/* 2014 */       if (list != null && !list.isEmpty() && ((Type)list.head).tsym == this.syms.comparatorType.tsym)
/*      */         return;
/*      */     }
/* 2017 */     checkClassOverrideEqualsAndHash(paramDiagnosticPosition, paramClassSymbol); } Type checkType(final JCDiagnostic.DiagnosticPosition pos, final Type found, final Type req, final CheckContext checkContext) { Infer.InferenceContext inferenceContext = checkContext.inferenceContext(); if (inferenceContext.free(req) || inferenceContext.free(found)) inferenceContext.addFreeTypeListener(List.of(req, found), new Infer.FreeTypeListener() { public void typesInferred(Infer.InferenceContext param1InferenceContext) { Check.this.checkType(pos, param1InferenceContext.asInstType(found), param1InferenceContext.asInstType(req), checkContext); } }
/*      */         );  if (req.hasTag(TypeTag.ERROR)) return req;  if (req.hasTag(TypeTag.NONE)) return found;  if (checkContext.compatible(found, req, checkContext.checkWarner(pos, found, req))) return found;  if (found.isNumeric() && req.isNumeric()) { checkContext.report(pos, this.diags.fragment("possible.loss.of.precision", new Object[] { found, req })); return this.types.createErrorType(found); }  checkContext.report(pos, this.diags.fragment("inconvertible.types", new Object[] { found, req })); return this.types.createErrorType(found); } Type checkCastable(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) { return checkCastable(paramDiagnosticPosition, paramType1, paramType2, this.basicHandler); } Type checkCastable(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, CheckContext paramCheckContext) { if (this.types.isCastable(paramType1, paramType2, castWarner(paramDiagnosticPosition, paramType1, paramType2))) return paramType2;  paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("inconvertible.types", new Object[] { paramType1, paramType2 })); return this.types.createErrorType(paramType1); } public void checkRedundantCast(Env<AttrContext> paramEnv, final JCTree.JCTypeCast tree) { if (!tree.type.isErroneous() && this.types.isSameType(tree.expr.type, tree.clazz.type) && !TreeInfo.containsTypeAnnotation(tree.clazz) && !is292targetTypeCast(tree)) this.deferredLintHandler.report(new DeferredLintHandler.LintLogger() { public void report() { if (Check.this.lint.isEnabled(Lint.LintCategory.CAST)) Check.this.log.warning(Lint.LintCategory.CAST, tree.pos(), "redundant.cast", new Object[] { this.val$tree.expr.type });  } }
/*      */         );  } private boolean is292targetTypeCast(JCTree.JCTypeCast paramJCTypeCast) { boolean bool = false; JCTree.JCExpression jCExpression = TreeInfo.skipParens(paramJCTypeCast.expr); if (jCExpression.hasTag(JCTree.Tag.APPLY)) { JCTree.JCMethodInvocation jCMethodInvocation = (JCTree.JCMethodInvocation)jCExpression; Symbol symbol = TreeInfo.symbol((JCTree)jCMethodInvocation.meth); bool = (symbol != null && symbol.kind == 16 && (symbol.flags() & 0x2000000000L) != 0L) ? true : false; }  return bool; } private boolean checkExtends(Type paramType1, Type paramType2) { if (paramType1.isUnbound()) return true;  if (!paramType1.hasTag(TypeTag.WILDCARD)) { paramType1 = this.types.cvarUpperBound(paramType1); return this.types.isSubtype(paramType1, paramType2); }  if (paramType1.isExtendsBound()) return this.types.isCastable(paramType2, this.types.wildUpperBound(paramType1), this.types.noWarnings);  if (paramType1.isSuperBound()) return !this.types.notSoftSubtype(this.types.wildLowerBound(paramType1), paramType2);  return true; } Type checkNonVoid(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { if (paramType.hasTag(TypeTag.VOID)) { this.log.error(paramDiagnosticPosition, "void.not.allowed.here", new Object[0]); return this.types.createErrorType(paramType); }  return paramType; } Type checkClassOrArrayType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { if (!paramType.hasTag(TypeTag.CLASS) && !paramType.hasTag(TypeTag.ARRAY) && !paramType.hasTag(TypeTag.ERROR)) return typeTagError(paramDiagnosticPosition, this.diags.fragment("type.req.class.array", new Object[0]), asTypeParam(paramType));  return paramType; } Type checkClassType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { if (!paramType.hasTag(TypeTag.CLASS) && !paramType.hasTag(TypeTag.ERROR)) return typeTagError(paramDiagnosticPosition, this.diags.fragment("type.req.class", new Object[0]), asTypeParam(paramType));  return paramType; } private Object asTypeParam(Type paramType) { return paramType.hasTag(TypeTag.TYPEVAR) ? this.diags.fragment("type.parameter", new Object[] { paramType }) : paramType; } Type checkConstructorRefType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { paramType = checkClassOrArrayType(paramDiagnosticPosition, paramType); if (paramType.hasTag(TypeTag.CLASS)) { if ((paramType.tsym.flags() & 0x600L) != 0L) { this.log.error(paramDiagnosticPosition, "abstract.cant.be.instantiated", new Object[] { paramType.tsym }); paramType = this.types.createErrorType(paramType); } else if ((paramType.tsym.flags() & 0x4000L) != 0L) { this.log.error(paramDiagnosticPosition, "enum.cant.be.instantiated", new Object[0]); paramType = this.types.createErrorType(paramType); } else { paramType = checkClassType(paramDiagnosticPosition, paramType, true); }  } else if (paramType.hasTag(TypeTag.ARRAY) && !this.types.isReifiable(((Type.ArrayType)paramType).elemtype)) { this.log.error(paramDiagnosticPosition, "generic.array.creation", new Object[0]); paramType = this.types.createErrorType(paramType); }  return paramType; } Type checkClassType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, boolean paramBoolean) { paramType = checkClassType(paramDiagnosticPosition, paramType); if (paramBoolean && paramType.isParameterized()) { List list = paramType.getTypeArguments(); while (list.nonEmpty()) { if (((Type)list.head).hasTag(TypeTag.WILDCARD)) return typeTagError(paramDiagnosticPosition, this.diags.fragment("type.req.exact", new Object[0]), list.head);  list = list.tail; }  }  return paramType; } Type checkRefType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { if (paramType.isReference()) return paramType;  return typeTagError(paramDiagnosticPosition, this.diags.fragment("type.req.ref", new Object[0]), paramType); } List<Type> checkRefTypes(List<JCTree.JCExpression> paramList, List<Type> paramList1) { List<JCTree.JCExpression> list = paramList; for (List<Type> list1 = paramList1; list1.nonEmpty(); list1 = list1.tail) { list1.head = checkRefType(((JCTree.JCExpression)list.head).pos(), (Type)list1.head); list = list.tail; }  return paramList1; } Type checkNullOrRefType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { if (paramType.isReference() || paramType.hasTag(TypeTag.BOT)) return paramType;  return typeTagError(paramDiagnosticPosition, this.diags.fragment("type.req.ref", new Object[0]), paramType); } boolean checkDisjoint(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, long paramLong1, long paramLong2, long paramLong3) { if ((paramLong1 & paramLong2) != 0L && (paramLong1 & paramLong3) != 0L) { this.log.error(paramDiagnosticPosition, "illegal.combination.of.modifiers", new Object[] { Flags.asFlagSet(TreeInfo.firstFlag(paramLong1 & paramLong2)), Flags.asFlagSet(TreeInfo.firstFlag(paramLong1 & paramLong3)) }); return false; }  return true; } Type checkDiamond(JCTree.JCNewClass paramJCNewClass, Type paramType) { if (!TreeInfo.isDiamond((JCTree)paramJCNewClass) || paramType.isErroneous()) return checkClassType(paramJCNewClass.clazz.pos(), paramType, true);  if (paramJCNewClass.def != null) { this.log.error(paramJCNewClass.clazz.pos(), "cant.apply.diamond.1", new Object[] { paramType, this.diags.fragment("diamond.and.anon.class", new Object[] { paramType }) }); return this.types.createErrorType(paramType); }  if (paramType.tsym.type.getTypeArguments().isEmpty()) { this.log.error(paramJCNewClass.clazz.pos(), "cant.apply.diamond.1", new Object[] { paramType, this.diags.fragment("diamond.non.generic", new Object[] { paramType }) }); return this.types.createErrorType(paramType); }  if (paramJCNewClass.typeargs != null && paramJCNewClass.typeargs.nonEmpty()) { this.log.error(paramJCNewClass.clazz.pos(), "cant.apply.diamond.1", new Object[] { paramType, this.diags.fragment("diamond.and.explicit.params", new Object[] { paramType }) }); return this.types.createErrorType(paramType); }  return paramType; } void checkVarargsMethodDecl(Env<AttrContext> paramEnv, JCTree.JCMethodDecl paramJCMethodDecl) { Symbol.MethodSymbol methodSymbol = paramJCMethodDecl.sym; if (!this.allowSimplifiedVarargs) return;  boolean bool = (methodSymbol.attribute((Symbol)this.syms.trustMeType.tsym) != null) ? true : false; Type type = null; if (methodSymbol.isVarArgs()) type = this.types.elemtype(((JCTree.JCVariableDecl)paramJCMethodDecl.params.last()).type);  if (bool && !isTrustMeAllowedOnMethod((Symbol)methodSymbol)) { if (type != null) { this.log.error((JCDiagnostic.DiagnosticPosition)paramJCMethodDecl, "varargs.invalid.trustme.anno", new Object[] { this.syms.trustMeType.tsym, this.diags.fragment("varargs.trustme.on.virtual.varargs", new Object[] { methodSymbol }) }); } else { this.log.error((JCDiagnostic.DiagnosticPosition)paramJCMethodDecl, "varargs.invalid.trustme.anno", new Object[] { this.syms.trustMeType.tsym, this.diags.fragment("varargs.trustme.on.non.varargs.meth", new Object[] { methodSymbol }) }); }  } else if (bool && type != null && this.types.isReifiable(type)) { warnUnsafeVararg((JCDiagnostic.DiagnosticPosition)paramJCMethodDecl, "varargs.redundant.trustme.anno", new Object[] { this.syms.trustMeType.tsym, this.diags.fragment("varargs.trustme.on.reifiable.varargs", new Object[] { type }) }); } else if (!bool && type != null && !this.types.isReifiable(type)) { warnUnchecked(((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).pos(), "unchecked.varargs.non.reifiable.type", new Object[] { type }); }  } private boolean isTrustMeAllowedOnMethod(Symbol paramSymbol) { return ((paramSymbol.flags() & 0x400000000L) != 0L && (paramSymbol.isConstructor() || (paramSymbol.flags() & 0x18L) != 0L)); } Type checkMethod(final Type mtype, final Symbol sym, final Env<AttrContext> env, final List<JCTree.JCExpression> argtrees, final List<Type> argtypes, final boolean useVarargs, Infer.InferenceContext paramInferenceContext) { if (paramInferenceContext.free(mtype)) { paramInferenceContext.addFreeTypeListener(List.of(mtype), new Infer.FreeTypeListener() { public void typesInferred(Infer.InferenceContext param1InferenceContext) { Check.this.checkMethod(param1InferenceContext.asInstType(mtype), sym, env, argtrees, argtypes, useVarargs, param1InferenceContext); } }
/*      */         ); return mtype; }  Type type1 = mtype; List list1 = type1.getParameterTypes(); List list2 = sym.type.getParameterTypes(); if (list2.length() != list1.length()) list2 = list1;  Type type2 = useVarargs ? (Type)list1.last() : null; if (sym.name == this.names.init && sym.owner == this.syms.enumSym) { list1 = list1.tail.tail; list2 = list2.tail.tail; }  List<JCTree.JCExpression> list = argtrees; if (list != null) { while (list1.head != type2) { JCTree jCTree = (JCTree)list.head; Warner warner = convertWarner(jCTree.pos(), jCTree.type, (Type)list2.head); assertConvertible(jCTree, jCTree.type, (Type)list1.head, warner); list = list.tail; list1 = list1.tail; list2 = list2.tail; }  if (useVarargs) { Type type = this.types.elemtype(type2); while (list.tail != null) { JCTree jCTree = (JCTree)list.head; Warner warner = convertWarner(jCTree.pos(), jCTree.type, type); assertConvertible(jCTree, jCTree.type, type, warner); list = list.tail; }  } else if ((sym.flags() & 0x400400000000L) == 17179869184L && this.allowVarargs) { Type type3 = (Type)type1.getParameterTypes().last(); Type type4 = (Type)argtypes.last(); if (this.types.isSubtypeUnchecked(type4, this.types.elemtype(type3)) && !this.types.isSameType(this.types.erasure(type3), this.types.erasure(type4))) this.log.warning(((JCTree.JCExpression)argtrees.last()).pos(), "inexact.non-varargs.call", new Object[] { this.types.elemtype(type3), type3 });  }  }  if (useVarargs) { Type type = (Type)type1.getParameterTypes().last(); if (!this.types.isReifiable(type) && (!this.allowSimplifiedVarargs || sym.attribute((Symbol)this.syms.trustMeType.tsym) == null || !isTrustMeAllowedOnMethod(sym))) warnUnchecked(env.tree.pos(), "unchecked.generic.array.creation", new Object[] { type });  if ((sym.baseSymbol().flags() & 0x400000000000L) == 0L) TreeInfo.setVarargsElement(env.tree, this.types.elemtype(type));  }  JCTree.JCPolyExpression.PolyKind polyKind = (sym.type.hasTag(TypeTag.FORALL) && sym.type.getReturnType().containsAny(((Type.ForAll)sym.type).tvars)) ? JCTree.JCPolyExpression.PolyKind.POLY : JCTree.JCPolyExpression.PolyKind.STANDALONE; TreeInfo.setPolyKind(env.tree, polyKind); return type1; } private void assertConvertible(JCTree paramJCTree, Type paramType1, Type paramType2, Warner paramWarner) { if (this.types.isConvertible(paramType1, paramType2, paramWarner)) return;  if (paramType2.isCompound() && this.types.isSubtype(paramType1, this.types.supertype(paramType2)) && this.types.isSubtypeUnchecked(paramType1, this.types.interfaces(paramType2), paramWarner)) return;  } public boolean checkValidGenericType(Type paramType) { return (firstIncompatibleTypeArg(paramType) == null); } private Type firstIncompatibleTypeArg(Type paramType) { List list1 = paramType.tsym.type.allparams(); List list2 = paramType.allparams(); List list3 = paramType.getTypeArguments(); List list4 = paramType.tsym.type.getTypeArguments(); ListBuffer listBuffer = new ListBuffer(); while (list3.nonEmpty() && list4.nonEmpty()) { listBuffer.append(this.types.subst(((Type)list4.head).getUpperBound(), list1, list2)); list3 = list3.tail; list4 = list4.tail; }  list3 = paramType.getTypeArguments(); List list5 = this.types.substBounds(list1, list1, this.types.capture(paramType).allparams()); while (list3.nonEmpty() && list5.nonEmpty()) { ((Type)list3.head).withTypeVar((Type)list5.head); list3 = list3.tail; list5 = list5.tail; }  list3 = paramType.getTypeArguments(); List list6 = listBuffer.toList(); while (list3.nonEmpty() && list6.nonEmpty()) { Type type = (Type)list3.head; if (!isTypeArgErroneous(type) && !((Type)list6.head).isErroneous() && !checkExtends(type, (Type)list6.head)) return (Type)list3.head;  list3 = list3.tail; list6 = list6.tail; }  list3 = paramType.getTypeArguments(); list6 = listBuffer.toList(); for (Type type : this.types.capture(paramType).getTypeArguments()) { if (type.hasTag(TypeTag.TYPEVAR) && type.getUpperBound().isErroneous() && !((Type)list6.head).isErroneous() && !isTypeArgErroneous((Type)list3.head)) return (Type)list3.head;  list6 = list6.tail; list3 = list3.tail; }  return null; } boolean isTypeArgErroneous(Type paramType) { return ((Boolean)this.isTypeArgErroneous.visit(paramType)).booleanValue(); } long checkFlags(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, long paramLong, Symbol paramSymbol, JCTree paramJCTree) { long l1, l2 = 0L; switch (paramSymbol.kind) { case 4: if (TreeInfo.isReceiverParam(paramJCTree)) { long l = 8589934592L; break; }  if (paramSymbol.owner.kind != 2) { long l = 8589934608L; break; }  if ((paramSymbol.owner.flags_field & 0x200L) != 0L) long l = l2 = 25L;  l1 = 16607L; break;case 16: if (paramSymbol.name == this.names.init) { if ((paramSymbol.owner.flags_field & 0x4000L) != 0L) { l2 = 2L; l1 = 2L; } else { l1 = 7L; }  } else if ((paramSymbol.owner.flags_field & 0x200L) != 0L) { if ((paramSymbol.owner.flags_field & 0x2000L) != 0L) { l1 = 1025L; l2 = 1025L; } else if ((paramLong & 0x80000000008L) != 0L) { l1 = 8796093025289L; l2 = 1L; if ((paramLong & 0x80000000000L) != 0L) l2 |= 0x400L;  } else { l1 = l2 = 1025L; }  } else { l1 = 3391L; }  if (((paramLong | l2) & 0x400L) == 0L || (paramLong & 0x80000000000L) != 0L) l2 |= paramSymbol.owner.flags_field & 0x800L;  break;case 2: if (paramSymbol.isLocal()) { l1 = 23568L; if (paramSymbol.name.isEmpty()) { l1 |= 0x8L; l2 |= 0x10L; }  if ((paramSymbol.owner.flags_field & 0x8L) == 0L && (paramLong & 0x4000L) != 0L) this.log.error(paramDiagnosticPosition, "enums.must.be.static", new Object[0]);  } else if (paramSymbol.owner.kind == 2) { l1 = 24087L; if (paramSymbol.owner.owner.kind == 1 || (paramSymbol.owner.flags_field & 0x8L) != 0L) { l1 |= 0x8L; } else if ((paramLong & 0x4000L) != 0L) { this.log.error(paramDiagnosticPosition, "enums.must.be.static", new Object[0]); }  if ((paramLong & 0x4200L) != 0L) l2 = 8L;  } else { l1 = 32273L; }  if ((paramLong & 0x200L) != 0L) l2 |= 0x400L;  if ((paramLong & 0x4000L) != 0L) { l1 &= 0xFFFFFFFFFFFFFBEFL; l2 |= implicitEnumFinalFlag(paramJCTree); }  l2 |= paramSymbol.owner.flags_field & 0x800L; break;default: throw new AssertionError(); }  long l3 = paramLong & 0x80000000FFFL & (l1 ^ 0xFFFFFFFFFFFFFFFFL); if (l3 != 0L) { if ((l3 & 0x200L) != 0L) { this.log.error(paramDiagnosticPosition, "intf.not.allowed.here", new Object[0]); l1 |= 0x200L; } else { this.log.error(paramDiagnosticPosition, "mod.not.allowed.here", new Object[] { Flags.asFlagSet(l3) }); }  } else if ((paramSymbol.kind != 2 && !checkDisjoint(paramDiagnosticPosition, paramLong, 1024L, 8796093022218L)) || !checkDisjoint(paramDiagnosticPosition, paramLong, 8L, 8796093022208L) || !checkDisjoint(paramDiagnosticPosition, paramLong, 1536L, 304L) || !checkDisjoint(paramDiagnosticPosition, paramLong, 1L, 6L) || !checkDisjoint(paramDiagnosticPosition, paramLong, 2L, 5L) || !checkDisjoint(paramDiagnosticPosition, paramLong, 16L, 64L) || paramSymbol.kind == 2 || checkDisjoint(paramDiagnosticPosition, paramLong, 1280L, 2048L)) {  }  return paramLong & (l1 | 0xFFFFF7FFFFFFF000L) | l2; } private long implicitEnumFinalFlag(JCTree paramJCTree) { if (!paramJCTree.hasTag(JCTree.Tag.CLASSDEF)) return 0L;  class SpecialTreeVisitor extends JCTree.Visitor {
/*      */       boolean specialized = false; public void visitTree(JCTree param1JCTree) {} public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) { if ((param1JCVariableDecl.mods.flags & 0x4000L) != 0L && param1JCVariableDecl.init instanceof JCTree.JCNewClass && ((JCTree.JCNewClass)param1JCVariableDecl.init).def != null) this.specialized = true;  } }; SpecialTreeVisitor specialTreeVisitor = new SpecialTreeVisitor(); JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)paramJCTree; for (JCTree jCTree : jCClassDecl.defs) { jCTree.accept(specialTreeVisitor); if (specialTreeVisitor.specialized) return 0L;  }  return 16L; } void validate(JCTree paramJCTree, Env<AttrContext> paramEnv) { validate(paramJCTree, paramEnv, true); } void validate(JCTree paramJCTree, Env<AttrContext> paramEnv, boolean paramBoolean) { (new Validator(paramEnv)).validateTree(paramJCTree, paramBoolean, true); } void validate(List<? extends JCTree> paramList, Env<AttrContext> paramEnv) { for (List<? extends JCTree> list = paramList; list.nonEmpty(); list = list.tail) validate((JCTree)list.head, paramEnv);  } class Validator extends JCTree.Visitor {
/* 2022 */     boolean checkRaw; boolean isOuter; Env<AttrContext> env; Validator(Env<AttrContext> param1Env) { this.env = param1Env; } public void visitTypeArray(JCTree.JCArrayTypeTree param1JCArrayTypeTree) { validateTree((JCTree)param1JCArrayTypeTree.elemtype, this.checkRaw, this.isOuter); } public void visitTypeApply(JCTree.JCTypeApply param1JCTypeApply) { if (param1JCTypeApply.type.hasTag(TypeTag.CLASS)) { List list1 = param1JCTypeApply.arguments; List list2 = param1JCTypeApply.type.tsym.type.getTypeArguments(); Type type = Check.this.firstIncompatibleTypeArg(param1JCTypeApply.type); if (type != null) for (JCTree jCTree : param1JCTypeApply.arguments) { if (jCTree.type == type) Check.this.log.error((JCDiagnostic.DiagnosticPosition)jCTree, "not.within.bounds", new Object[] { type, list2.head });  list2 = list2.tail; }   list2 = param1JCTypeApply.type.tsym.type.getTypeArguments(); boolean bool = (param1JCTypeApply.type.tsym.flatName() == Check.this.names.java_lang_Class) ? true : false; while (list1.nonEmpty() && list2.nonEmpty()) { validateTree((JCTree)list1.head, (!this.isOuter || !bool), false); list1 = list1.tail; list2 = list2.tail; }  if (param1JCTypeApply.type.getEnclosingType().isRaw()) Check.this.log.error(param1JCTypeApply.pos(), "improperly.formed.type.inner.raw.param", new Object[0]);  if (param1JCTypeApply.clazz.hasTag(JCTree.Tag.SELECT)) visitSelectInternal((JCTree.JCFieldAccess)param1JCTypeApply.clazz);  }  } public void visitTypeParameter(JCTree.JCTypeParameter param1JCTypeParameter) { validateTrees(param1JCTypeParameter.bounds, true, this.isOuter); Check.this.checkClassBounds(param1JCTypeParameter.pos(), param1JCTypeParameter.type); } public void visitWildcard(JCTree.JCWildcard param1JCWildcard) { if (param1JCWildcard.inner != null) validateTree(param1JCWildcard.inner, true, this.isOuter);  } public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) { if (param1JCFieldAccess.type.hasTag(TypeTag.CLASS)) { visitSelectInternal(param1JCFieldAccess); if (param1JCFieldAccess.selected.type.isParameterized() && param1JCFieldAccess.type.tsym.type.getTypeArguments().nonEmpty()) Check.this.log.error(param1JCFieldAccess.pos(), "improperly.formed.type.param.missing", new Object[0]);  }  } public void visitSelectInternal(JCTree.JCFieldAccess param1JCFieldAccess) { if (param1JCFieldAccess.type.tsym.isStatic() && param1JCFieldAccess.selected.type.isParameterized()) { Check.this.log.error(param1JCFieldAccess.pos(), "cant.select.static.class.from.param.type", new Object[0]); } else { param1JCFieldAccess.selected.accept(this); }  } public void visitAnnotatedType(JCTree.JCAnnotatedType param1JCAnnotatedType) { param1JCAnnotatedType.underlyingType.accept(this); } public void visitTypeIdent(JCTree.JCPrimitiveTypeTree param1JCPrimitiveTypeTree) { if (param1JCPrimitiveTypeTree.type.hasTag(TypeTag.VOID)) Check.this.log.error(param1JCPrimitiveTypeTree.pos(), "void.not.allowed.here", new Object[0]);  super.visitTypeIdent(param1JCPrimitiveTypeTree); } public void visitTree(JCTree param1JCTree) {} public void validateTree(JCTree param1JCTree, boolean param1Boolean1, boolean param1Boolean2) { if (param1JCTree != null) { boolean bool = this.checkRaw; this.checkRaw = param1Boolean1; this.isOuter = param1Boolean2; try { param1JCTree.accept(this); if (param1Boolean1) Check.this.checkRaw(param1JCTree, this.env);  } catch (Symbol.CompletionFailure completionFailure) { Check.this.completionError(param1JCTree.pos(), completionFailure); } finally { this.checkRaw = bool; }  }  } public void validateTrees(List<? extends JCTree> param1List, boolean param1Boolean1, boolean param1Boolean2) { for (List<? extends JCTree> list = param1List; list.nonEmpty(); list = list.tail) validateTree((JCTree)list.head, param1Boolean1, param1Boolean2);  } } void checkRaw(JCTree paramJCTree, Env<AttrContext> paramEnv) { if (this.lint.isEnabled(Lint.LintCategory.RAW) && paramJCTree.type.hasTag(TypeTag.CLASS) && !TreeInfo.isDiamond(paramJCTree) && !withinAnonConstr(paramEnv) && paramJCTree.type.isRaw()) this.log.warning(Lint.LintCategory.RAW, paramJCTree.pos(), "raw.class.use", new Object[] { paramJCTree.type, paramJCTree.type.tsym.type });  } private boolean withinAnonConstr(Env<AttrContext> paramEnv) { return (paramEnv.enclClass.name.isEmpty() && paramEnv.enclMethod != null && paramEnv.enclMethod.name == this.names.init); } boolean subset(Type paramType, List<Type> paramList) { for (List<Type> list = paramList; list.nonEmpty(); list = list.tail) { if (this.types.isSubtype(paramType, (Type)list.head)) return true;  }  return false; } boolean intersects(Type paramType, List<Type> paramList) { for (List<Type> list = paramList; list.nonEmpty(); list = list.tail) { if (this.types.isSubtype(paramType, (Type)list.head) || this.types.isSubtype((Type)list.head, paramType)) return true;  }  return false; } List<Type> incl(Type paramType, List<Type> paramList) { return subset(paramType, paramList) ? paramList : excl(paramType, paramList).prepend(paramType); } List<Type> excl(Type paramType, List<Type> paramList) { if (paramList.isEmpty()) return paramList;  List<Type> list = excl(paramType, paramList.tail); if (this.types.isSubtype((Type)paramList.head, paramType)) return list;  if (list == paramList.tail) return paramList;  return list.prepend(paramList.head); } List<Type> union(List<Type> paramList1, List<Type> paramList2) { List<Type> list1 = paramList1; for (List<Type> list2 = paramList2; list2.nonEmpty(); list2 = list2.tail) list1 = incl((Type)list2.head, list1);  return list1; } List<Type> diff(List<Type> paramList1, List<Type> paramList2) { List<Type> list1 = paramList1; for (List<Type> list2 = paramList2; list2.nonEmpty(); list2 = list2.tail) list1 = excl((Type)list2.head, list1);  return list1; } public List<Type> intersect(List<Type> paramList1, List<Type> paramList2) { List<Type> list1 = List.nil(); List<Type> list2; for (list2 = paramList1; list2.nonEmpty(); list2 = list2.tail) { if (subset((Type)list2.head, paramList2)) list1 = incl((Type)list2.head, list1);  }  for (list2 = paramList2; list2.nonEmpty(); list2 = list2.tail) { if (subset((Type)list2.head, paramList1)) list1 = incl((Type)list2.head, list1);  }  return list1; } boolean isUnchecked(Symbol.ClassSymbol paramClassSymbol) { return (paramClassSymbol.kind == 63 || paramClassSymbol.isSubClass((Symbol)this.syms.errorType.tsym, this.types) || paramClassSymbol.isSubClass((Symbol)this.syms.runtimeExceptionType.tsym, this.types)); } private void checkClassOverrideEqualsAndHash(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) { if (this.lint.isEnabled(Lint.LintCategory.OVERRIDES))
/*      */
/* 2024 */     { Symbol.MethodSymbol methodSymbol1 = (Symbol.MethodSymbol)(this.syms.objectType.tsym.members().lookup(this.names.equals)).sym;
/*      */
/* 2026 */       Symbol.MethodSymbol methodSymbol2 = (Symbol.MethodSymbol)(this.syms.objectType.tsym.members().lookup(this.names.hashCode)).sym;
/* 2027 */       boolean bool1 = ((this.types.implementation(methodSymbol1, (Symbol.TypeSymbol)paramClassSymbol, false, this.equalsHasCodeFilter)).owner == paramClassSymbol) ? true : false;
/*      */
/* 2029 */       boolean bool2 = (this.types.implementation(methodSymbol2, (Symbol.TypeSymbol)paramClassSymbol, false, this.equalsHasCodeFilter) != methodSymbol2) ? true : false;
/*      */
/*      */
/* 2032 */       if (bool1 && !bool2)
/* 2033 */         this.log.warning(Lint.LintCategory.OVERRIDES, paramDiagnosticPosition, "override.equals.but.not.hashcode", new Object[] { paramClassSymbol });  }  } boolean isUnchecked(Type paramType) { return paramType.hasTag(TypeTag.TYPEVAR) ? isUnchecked(this.types.supertype(paramType)) : (paramType.hasTag(TypeTag.CLASS) ? isUnchecked((Symbol.ClassSymbol)paramType.tsym) : paramType.hasTag(TypeTag.BOT)); } boolean isUnchecked(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { try { return isUnchecked(paramType); } catch (Symbol.CompletionFailure completionFailure) { completionError(paramDiagnosticPosition, completionFailure); return true; }  } boolean isHandled(Type paramType, List<Type> paramList) { return (isUnchecked(paramType) || subset(paramType, paramList)); } List<Type> unhandled(List<Type> paramList1, List<Type> paramList2) { List<Type> list1 = List.nil(); for (List<Type> list2 = paramList1; list2.nonEmpty(); list2 = list2.tail) { if (!isHandled((Type)list2.head, paramList2)) list1 = list1.prepend(list2.head);  }  return list1; } static int protection(long paramLong) { switch ((short)(int)(paramLong & 0x7L)) { case 2: return 3;case 4: return 1;default: return 0;case 0: break; }  return 2; } Object cannotOverride(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2) { String str; if ((paramMethodSymbol2.owner.flags() & 0x200L) == 0L) { str = "cant.override"; } else if ((paramMethodSymbol1.owner.flags() & 0x200L) == 0L) { str = "cant.implement"; } else { str = "clashes.with"; }  return this.diags.fragment(str, new Object[] { paramMethodSymbol1, paramMethodSymbol1.location(), paramMethodSymbol2, paramMethodSymbol2.location() }); } Object uncheckedOverrides(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2) { String str; if ((paramMethodSymbol2.owner.flags() & 0x200L) == 0L) { str = "unchecked.override"; } else if ((paramMethodSymbol1.owner.flags() & 0x200L) == 0L) { str = "unchecked.implement"; } else { str = "unchecked.clash.with"; }  return this.diags.fragment(str, new Object[] { paramMethodSymbol1, paramMethodSymbol1.location(), paramMethodSymbol2, paramMethodSymbol2.location() }); } Object varargsOverrides(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2) { String str; if ((paramMethodSymbol2.owner.flags() & 0x200L) == 0L) { str = "varargs.override"; } else if ((paramMethodSymbol1.owner.flags() & 0x200L) == 0L) { str = "varargs.implement"; } else { str = "varargs.clash.with"; }  return this.diags.fragment(str, new Object[] { paramMethodSymbol1, paramMethodSymbol1.location(), paramMethodSymbol2, paramMethodSymbol2.location() }); } void checkOverride(JCTree paramJCTree, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Symbol.ClassSymbol paramClassSymbol) { if ((paramMethodSymbol1.flags() & 0x80001000L) != 0L || (paramMethodSymbol2.flags() & 0x1000L) != 0L) return;  if ((paramMethodSymbol1.flags() & 0x8L) != 0L && (paramMethodSymbol2.flags() & 0x8L) == 0L) { this.log.error(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.static", new Object[] { cannotOverride(paramMethodSymbol1, paramMethodSymbol2) }); paramMethodSymbol1.flags_field |= 0x200000000000L; return; }  if ((paramMethodSymbol2.flags() & 0x10L) != 0L || ((paramMethodSymbol1.flags() & 0x8L) == 0L && (paramMethodSymbol2.flags() & 0x8L) != 0L)) { this.log.error(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.meth", new Object[] { cannotOverride(paramMethodSymbol1, paramMethodSymbol2), Flags.asFlagSet(paramMethodSymbol2.flags() & 0x18L) }); paramMethodSymbol1.flags_field |= 0x200000000000L; return; }  if ((paramMethodSymbol1.owner.flags() & 0x2000L) != 0L) return;  if ((paramClassSymbol.flags() & 0x200L) == 0L && protection(paramMethodSymbol1.flags()) > protection(paramMethodSymbol2.flags())) { this.log.error(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.weaker.access", new Object[] { cannotOverride(paramMethodSymbol1, paramMethodSymbol2), (paramMethodSymbol2.flags() == 0L) ? "package" : Flags.asFlagSet(paramMethodSymbol2.flags() & 0x7L) }); paramMethodSymbol1.flags_field |= 0x200000000000L; return; }  Type type1 = this.types.memberType(paramClassSymbol.type, (Symbol)paramMethodSymbol1); Type type2 = this.types.memberType(paramClassSymbol.type, (Symbol)paramMethodSymbol2); List list1 = type1.getTypeArguments(); List list2 = type2.getTypeArguments(); Type type3 = type1.getReturnType(); Type type4 = this.types.subst(type2.getReturnType(), list2, list1); this.overrideWarner.clear(); boolean bool = this.types.returnTypeSubstitutable(type1, type2, type4, this.overrideWarner); if (!bool) { if (this.allowCovariantReturns || paramMethodSymbol1.owner == paramClassSymbol || !paramMethodSymbol1.owner.isSubClass(paramMethodSymbol2.owner, this.types)) { this.log.error(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.incompatible.ret", new Object[] { cannotOverride(paramMethodSymbol1, paramMethodSymbol2), type3, type4 }); paramMethodSymbol1.flags_field |= 0x200000000000L; return; }  } else if (this.overrideWarner.hasNonSilentLint(Lint.LintCategory.UNCHECKED)) { warnUnchecked(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.unchecked.ret", new Object[] { uncheckedOverrides(paramMethodSymbol1, paramMethodSymbol2), type3, type4 }); }  List<Type> list3 = this.types.subst(type2.getThrownTypes(), list2, list1); List<Type> list4 = unhandled(type1.getThrownTypes(), this.types.erasure(list3)); List<Type> list5 = unhandled(type1.getThrownTypes(), list3); if (list4.nonEmpty()) { this.log.error(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.meth.doesnt.throw", new Object[] { cannotOverride(paramMethodSymbol1, paramMethodSymbol2), list5.head }); paramMethodSymbol1.flags_field |= 0x200000000000L; return; }  if (list5.nonEmpty()) { warnUnchecked(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.unchecked.thrown", new Object[] { cannotOverride(paramMethodSymbol1, paramMethodSymbol2), list5.head }); return; }  if (((paramMethodSymbol1.flags() ^ paramMethodSymbol2.flags()) & 0x400000000L) != 0L && this.lint.isEnabled(Lint.LintCategory.OVERRIDES)) this.log.warning(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), ((paramMethodSymbol1.flags() & 0x400000000L) != 0L) ? "override.varargs.missing" : "override.varargs.extra", new Object[] { varargsOverrides(paramMethodSymbol1, paramMethodSymbol2) });  if ((paramMethodSymbol2.flags() & 0x80000000L) != 0L) this.log.warning(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), "override.bridge", new Object[] { uncheckedOverrides(paramMethodSymbol1, paramMethodSymbol2) });  if (!isDeprecatedOverrideIgnorable(paramMethodSymbol2, paramClassSymbol)) { Lint lint = setLint(this.lint.augment((Symbol)paramMethodSymbol1)); try { checkDeprecated(TreeInfo.diagnosticPositionFor((Symbol)paramMethodSymbol1, paramJCTree), (Symbol)paramMethodSymbol1, (Symbol)paramMethodSymbol2); } finally { setLint(lint); }  }  } private boolean isDeprecatedOverrideIgnorable(Symbol.MethodSymbol paramMethodSymbol, Symbol.ClassSymbol paramClassSymbol) { Symbol.ClassSymbol classSymbol = paramMethodSymbol.enclClass(); Type type = this.types.supertype(paramClassSymbol.type); if (!type.hasTag(TypeTag.CLASS)) return true;  Symbol.MethodSymbol methodSymbol = paramMethodSymbol.implementation(type.tsym, this.types, false); if (classSymbol != null && (classSymbol.flags() & 0x200L) != 0L) { List list = this.types.interfaces(paramClassSymbol.type); return list.contains(classSymbol.type) ? false : ((methodSymbol != null)); }  return (methodSymbol != paramMethodSymbol); } public void checkCompatibleConcretes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) { Type type1 = this.types.supertype(paramType); if (!type1.hasTag(TypeTag.CLASS)) return;  Type type2 = type1; for (; type2.hasTag(TypeTag.CLASS) && type2.tsym.type.isParameterized(); type2 = this.types.supertype(type2)) { Scope.Entry entry = (type2.tsym.members()).elems; for (; entry != null; entry = entry.sibling) { Symbol symbol = entry.sym; if (symbol.kind == 16 && (symbol.flags() & 0x80001008L) == 0L && symbol.isInheritedIn((Symbol)paramType.tsym, this.types) && ((Symbol.MethodSymbol)symbol).implementation(paramType.tsym, this.types, true) == symbol) { Type type = this.types.memberType(type2, symbol); int i = type.getParameterTypes().length(); if (type != symbol.type) { Type type3 = type1; for (; type3.hasTag(TypeTag.CLASS); type3 = this.types.supertype(type3)) { Scope.Entry entry1 = type3.tsym.members().lookup(symbol.name); for (; entry1.scope != null; entry1 = entry1.next()) { Symbol symbol1 = entry1.sym; if (symbol1 != symbol && symbol1.kind == 16 && (symbol1.flags() & 0x80001008L) == 0L && symbol1.type.getParameterTypes().length() == i && symbol1.isInheritedIn((Symbol)paramType.tsym, this.types) && ((Symbol.MethodSymbol)symbol1).implementation(paramType.tsym, this.types, true) == symbol1) { Type type4 = this.types.memberType(type3, symbol1); if (this.types.overrideEquivalent(type, type4)) this.log.error(paramDiagnosticPosition, "concrete.inheritance.conflict", new Object[] { symbol, type2, symbol1, type3, type1 });  }  }  }  }  }  }  }  } public boolean checkCompatibleAbstracts(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) { return checkCompatibleAbstracts(paramDiagnosticPosition, paramType1, paramType2, this.types.makeIntersectionType(paramType1, paramType2)); } public boolean checkCompatibleAbstracts(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, Type paramType3) { if ((paramType3.tsym.flags() & 0x1000000L) != 0L) { paramType1 = this.types.capture(paramType1); paramType2 = this.types.capture(paramType2); }  return (firstIncompatibility(paramDiagnosticPosition, paramType1, paramType2, paramType3) == null); } private Symbol firstIncompatibility(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, Type paramType3) { HashMap<Object, Object> hashMap2, hashMap1 = new HashMap<>(); closure(paramType1, (Map)hashMap1); if (paramType1 == paramType2) { hashMap2 = hashMap1; } else { closure(paramType2, (Map)hashMap1, (Map)(hashMap2 = new HashMap<>())); }  for (Type type : hashMap1.values()) { for (Type type1 : hashMap2.values()) { Symbol symbol = firstDirectIncompatibility(paramDiagnosticPosition, type, type1, paramType3); if (symbol != null) return symbol;  }  }  return null; }
/*      */   private void closure(Type paramType, Map<Symbol.TypeSymbol, Type> paramMap) { if (!paramType.hasTag(TypeTag.CLASS)) return;  if (paramMap.put(paramType.tsym, paramType) == null) { closure(this.types.supertype(paramType), paramMap); for (Type type : this.types.interfaces(paramType)) closure(type, paramMap);  }  }
/*      */   private void closure(Type paramType, Map<Symbol.TypeSymbol, Type> paramMap1, Map<Symbol.TypeSymbol, Type> paramMap2) { if (!paramType.hasTag(TypeTag.CLASS)) return;  if (paramMap1.get(paramType.tsym) != null) return;  if (paramMap2.put(paramType.tsym, paramType) == null) { closure(this.types.supertype(paramType), paramMap1, paramMap2); for (Type type : this.types.interfaces(paramType)) closure(type, paramMap1, paramMap2);  }  }
/*      */   private Symbol firstDirectIncompatibility(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, Type paramType3) { for (Scope.Entry entry = (paramType1.tsym.members()).elems; entry != null; entry = entry.sibling) { Symbol symbol = entry.sym; Type type = null; if (symbol.kind == 16 && symbol.isInheritedIn((Symbol)paramType3.tsym, this.types) && (symbol.flags() & 0x1000L) == 0L) { Symbol.MethodSymbol methodSymbol = ((Symbol.MethodSymbol)symbol).implementation(paramType3.tsym, this.types, false); if (methodSymbol == null || (methodSymbol.flags() & 0x400L) != 0L) for (Scope.Entry entry1 = paramType2.tsym.members().lookup(symbol.name); entry1.scope != null; entry1 = entry1.next()) { Symbol symbol1 = entry1.sym; if (symbol != symbol1 && symbol1.kind == 16 && symbol1.isInheritedIn((Symbol)paramType3.tsym, this.types) && (symbol1.flags() & 0x1000L) == 0L) { if (type == null) type = this.types.memberType(paramType1, symbol);  Type type1 = this.types.memberType(paramType2, symbol1); if (this.types.overrideEquivalent(type, type1)) { List list1 = type.getTypeArguments(); List list2 = type1.getTypeArguments(); Type type2 = type.getReturnType(); Type type3 = this.types.subst(type1.getReturnType(), list2, list1); boolean bool = (this.types.isSameType(type2, type3) || (!type2.isPrimitiveOrVoid() && !type3.isPrimitiveOrVoid() && (this.types.covariantReturnType(type2, type3, this.types.noWarnings) || this.types.covariantReturnType(type3, type2, this.types.noWarnings))) || checkCommonOverriderIn(symbol, symbol1, paramType3)) ? true : false; if (!bool) { this.log.error(paramDiagnosticPosition, "types.incompatible.diff.ret", new Object[] { paramType1, paramType2, symbol1.name + "(" + this.types.memberType(paramType2, symbol1).getParameterTypes() + ")" }); return symbol1; }  } else if (checkNameClash((Symbol.ClassSymbol)paramType3.tsym, symbol, symbol1) && !checkCommonOverriderIn(symbol, symbol1, paramType3)) { this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.override", new Object[] { symbol, symbol.location(), symbol1, symbol1.location() }); return symbol1; }  }  }   }  }  return null; }
/*      */   boolean checkCommonOverriderIn(Symbol paramSymbol1, Symbol paramSymbol2, Type paramType) { HashMap<Object, Object> hashMap = new HashMap<>(); Type type1 = this.types.memberType(paramType, paramSymbol1); Type type2 = this.types.memberType(paramType, paramSymbol2); closure(paramType, (Map)hashMap); for (Type type : hashMap.values()) { for (Scope.Entry entry = type.tsym.members().lookup(paramSymbol1.name); entry.scope != null; entry = entry.next()) { Symbol symbol = entry.sym; if (symbol != paramSymbol1 && symbol != paramSymbol2 && symbol.kind == 16 && (symbol.flags() & 0x80001000L) == 0L) { Type type3 = this.types.memberType(paramType, symbol); if (this.types.overrideEquivalent(type3, type1) && this.types.overrideEquivalent(type3, type2) && this.types.returnTypeSubstitutable(type3, type1) && this.types.returnTypeSubstitutable(type3, type2)) return true;  }  }  }  return false; }
/*      */   void checkOverride(JCTree.JCMethodDecl paramJCMethodDecl, Symbol.MethodSymbol paramMethodSymbol) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)paramMethodSymbol.owner; if ((classSymbol.flags() & 0x4000L) != 0L && this.names.finalize.equals(paramMethodSymbol.name) && paramMethodSymbol.overrides((Symbol)this.syms.enumFinalFinalize, (Symbol.TypeSymbol)classSymbol, this.types, false)) { this.log.error(paramJCMethodDecl.pos(), "enum.no.finalize", new Object[0]); return; }  for (Type type = classSymbol.type; type.hasTag(TypeTag.CLASS); type = this.types.supertype(type)) { if (type != classSymbol.type) checkOverride((JCTree)paramJCMethodDecl, type, classSymbol, paramMethodSymbol);  for (Type type1 : this.types.interfaces(type)) checkOverride((JCTree)paramJCMethodDecl, type1, classSymbol, paramMethodSymbol);  }  if (paramMethodSymbol.attribute((Symbol)this.syms.overrideType.tsym) != null && !isOverrider((Symbol)paramMethodSymbol)) { JCDiagnostic.DiagnosticPosition diagnosticPosition = paramJCMethodDecl.pos(); for (JCTree.JCAnnotation jCAnnotation : (paramJCMethodDecl.getModifiers()).annotations) { if (jCAnnotation.annotationType.type.tsym == this.syms.overrideType.tsym) { diagnosticPosition = jCAnnotation.pos(); break; }  }  this.log.error(diagnosticPosition, "method.does.not.override.superclass", new Object[0]); }  }
/*      */   void checkOverride(JCTree paramJCTree, Type paramType, Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol) { Symbol.TypeSymbol typeSymbol = paramType.tsym; Scope.Entry entry = typeSymbol.members().lookup(paramMethodSymbol.name); while (entry.scope != null) { if (paramMethodSymbol.overrides(entry.sym, (Symbol.TypeSymbol)paramClassSymbol, this.types, false) && (entry.sym.flags() & 0x400L) == 0L) checkOverride(paramJCTree, paramMethodSymbol, (Symbol.MethodSymbol)entry.sym, paramClassSymbol);  entry = entry.next(); }  }
/* 2040 */   private boolean checkNameClash(Symbol.ClassSymbol paramClassSymbol, Symbol paramSymbol1, Symbol paramSymbol2) { ClashFilter clashFilter = new ClashFilter(paramClassSymbol.type);
/* 2041 */     return (clashFilter.accepts(paramSymbol1) && clashFilter
/* 2042 */       .accepts(paramSymbol2) && this.types
/* 2043 */       .hasSameArgs(paramSymbol1.erasure(this.types), paramSymbol2.erasure(this.types))); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkAllDefined(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) {
/* 2052 */     Symbol.MethodSymbol methodSymbol = this.types.firstUnimplementedAbstract(paramClassSymbol);
/* 2053 */     if (methodSymbol != null) {
/*      */
/*      */
/* 2056 */       Symbol.MethodSymbol methodSymbol1 = new Symbol.MethodSymbol(methodSymbol.flags(), methodSymbol.name, this.types.memberType(paramClassSymbol.type, (Symbol)methodSymbol), methodSymbol.owner);
/* 2057 */       this.log.error(paramDiagnosticPosition, "does.not.override.abstract", new Object[] { paramClassSymbol, methodSymbol1, methodSymbol1
/* 2058 */             .location() });
/*      */     }
/*      */   }
/*      */
/*      */   void checkNonCyclicDecl(JCTree.JCClassDecl paramJCClassDecl) {
/* 2063 */     CycleChecker cycleChecker = new CycleChecker();
/* 2064 */     cycleChecker.scan((JCTree)paramJCClassDecl);
/* 2065 */     if (!cycleChecker.errorFound && !cycleChecker.partialCheck)
/* 2066 */       paramJCClassDecl.sym.flags_field |= 0x40000000L;
/*      */   }
/*      */
/*      */   class CycleChecker
/*      */     extends TreeScanner
/*      */   {
/* 2072 */     List<Symbol> seenClasses = List.nil();
/*      */     boolean errorFound = false;
/*      */     boolean partialCheck = false;
/*      */
/*      */     private void checkSymbol(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol) {
/* 2077 */       if (param1Symbol != null && param1Symbol.kind == 2) {
/* 2078 */         Env<AttrContext> env = Check.this.enter.getEnv((Symbol.TypeSymbol)param1Symbol);
/* 2079 */         if (env != null) {
/* 2080 */           DiagnosticSource diagnosticSource = Check.this.log.currentSource();
/*      */           try {
/* 2082 */             Check.this.log.useSource(env.toplevel.sourcefile);
/* 2083 */             scan(env.tree);
/*      */           } finally {
/*      */
/* 2086 */             Check.this.log.useSource(diagnosticSource.getFile());
/*      */           }
/* 2088 */         } else if (param1Symbol.kind == 2) {
/* 2089 */           checkClass(param1DiagnosticPosition, param1Symbol, List.nil());
/*      */         }
/*      */       } else {
/*      */
/* 2093 */         this.partialCheck = true;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 2099 */       super.visitSelect(param1JCFieldAccess);
/* 2100 */       checkSymbol(param1JCFieldAccess.pos(), param1JCFieldAccess.sym);
/*      */     }
/*      */
/*      */
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 2105 */       checkSymbol(param1JCIdent.pos(), param1JCIdent.sym);
/*      */     }
/*      */
/*      */
/*      */     public void visitTypeApply(JCTree.JCTypeApply param1JCTypeApply) {
/* 2110 */       scan((JCTree)param1JCTypeApply.clazz);
/*      */     }
/*      */
/*      */
/*      */     public void visitTypeArray(JCTree.JCArrayTypeTree param1JCArrayTypeTree) {
/* 2115 */       scan((JCTree)param1JCArrayTypeTree.elemtype);
/*      */     }
/*      */
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 2120 */       List<JCTree> list = List.nil();
/* 2121 */       if (param1JCClassDecl.getExtendsClause() != null) {
/* 2122 */         list = list.prepend(param1JCClassDecl.getExtendsClause());
/*      */       }
/* 2124 */       if (param1JCClassDecl.getImplementsClause() != null) {
/* 2125 */         for (JCTree jCTree : param1JCClassDecl.getImplementsClause()) {
/* 2126 */           list = list.prepend(jCTree);
/*      */         }
/*      */       }
/* 2129 */       checkClass(param1JCClassDecl.pos(), (Symbol)param1JCClassDecl.sym, list);
/*      */     }
/*      */
/*      */     void checkClass(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol, List<JCTree> param1List) {
/* 2133 */       if ((param1Symbol.flags_field & 0x40000000L) != 0L)
/*      */         return;
/* 2135 */       if (this.seenClasses.contains(param1Symbol)) {
/* 2136 */         this.errorFound = true;
/* 2137 */         Check.this.noteCyclic(param1DiagnosticPosition, (Symbol.ClassSymbol)param1Symbol);
/* 2138 */       } else if (!param1Symbol.type.isErroneous()) {
/*      */         try {
/* 2140 */           this.seenClasses = this.seenClasses.prepend(param1Symbol);
/* 2141 */           if (param1Symbol.type.hasTag(TypeTag.CLASS)) {
/* 2142 */             if (param1List.nonEmpty()) {
/* 2143 */               scan(param1List);
/*      */             } else {
/*      */
/* 2146 */               Type.ClassType classType = (Type.ClassType)param1Symbol.type;
/* 2147 */               if (classType.supertype_field == null || classType.interfaces_field == null) {
/*      */
/*      */
/* 2150 */                 this.partialCheck = true;
/*      */                 return;
/*      */               }
/* 2153 */               checkSymbol(param1DiagnosticPosition, (Symbol)classType.supertype_field.tsym);
/* 2154 */               for (Type type : classType.interfaces_field) {
/* 2155 */                 checkSymbol(param1DiagnosticPosition, (Symbol)type.tsym);
/*      */               }
/*      */             }
/* 2158 */             if (param1Symbol.owner.kind == 2) {
/* 2159 */               checkSymbol(param1DiagnosticPosition, param1Symbol.owner);
/*      */             }
/*      */           }
/*      */         } finally {
/* 2163 */           this.seenClasses = this.seenClasses.tail;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkNonCyclic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 2176 */     checkNonCyclicInternal(paramDiagnosticPosition, paramType);
/*      */   }
/*      */
/*      */
/*      */   void checkNonCyclic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type.TypeVar paramTypeVar) {
/* 2181 */     checkNonCyclic1(paramDiagnosticPosition, (Type)paramTypeVar, List.nil());
/*      */   }
/*      */
/*      */
/*      */   private void checkNonCyclic1(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type.TypeVar> paramList) {
/* 2186 */     if (paramType.hasTag(TypeTag.TYPEVAR) && (paramType.tsym.flags() & 0x10000000L) != 0L)
/*      */       return;
/* 2188 */     if (paramList.contains(paramType)) {
/* 2189 */       Type.TypeVar typeVar = (Type.TypeVar)paramType.unannotatedType();
/* 2190 */       typeVar.bound = this.types.createErrorType(paramType);
/* 2191 */       this.log.error(paramDiagnosticPosition, "cyclic.inheritance", new Object[] { paramType });
/* 2192 */     } else if (paramType.hasTag(TypeTag.TYPEVAR)) {
/* 2193 */       Type.TypeVar typeVar = (Type.TypeVar)paramType.unannotatedType();
/* 2194 */       paramList = paramList.prepend(typeVar);
/* 2195 */       for (Type type : this.types.getBounds(typeVar)) {
/* 2196 */         checkNonCyclic1(paramDiagnosticPosition, type, paramList);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean checkNonCyclicInternal(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 2208 */     boolean bool = true;
/*      */
/* 2210 */     Symbol.TypeSymbol typeSymbol = paramType.tsym;
/* 2211 */     if ((((Symbol)typeSymbol).flags_field & 0x40000000L) != 0L) return true;
/*      */
/* 2213 */     if ((((Symbol)typeSymbol).flags_field & 0x8000000L) != 0L) {
/* 2214 */       noteCyclic(paramDiagnosticPosition, (Symbol.ClassSymbol)typeSymbol);
/* 2215 */     } else if (!((Symbol)typeSymbol).type.isErroneous()) {
/*      */       try {
/* 2217 */         ((Symbol)typeSymbol).flags_field |= 0x8000000L;
/* 2218 */         if (((Symbol)typeSymbol).type.hasTag(TypeTag.CLASS)) {
/* 2219 */           Type.ClassType classType = (Type.ClassType)((Symbol)typeSymbol).type;
/* 2220 */           if (classType.interfaces_field != null)
/* 2221 */             for (List list = classType.interfaces_field; list.nonEmpty(); list = list.tail)
/* 2222 */               bool &= checkNonCyclicInternal(paramDiagnosticPosition, (Type)list.head);
/* 2223 */           if (classType.supertype_field != null) {
/* 2224 */             Type type = classType.supertype_field;
/* 2225 */             if (type != null && type.hasTag(TypeTag.CLASS))
/* 2226 */               bool &= checkNonCyclicInternal(paramDiagnosticPosition, type);
/*      */           }
/* 2228 */           if (((Symbol)typeSymbol).owner.kind == 2)
/* 2229 */             bool &= checkNonCyclicInternal(paramDiagnosticPosition, ((Symbol)typeSymbol).owner.type);
/*      */         }
/*      */       } finally {
/* 2232 */         ((Symbol)typeSymbol).flags_field &= 0xFFFFFFFFF7FFFFFFL;
/*      */       }
/*      */     }
/* 2235 */     if (bool)
/* 2236 */       bool = ((((Symbol)typeSymbol).flags_field & 0x10000000L) == 0L && ((Symbol)typeSymbol).completer == null);
/* 2237 */     if (bool) ((Symbol)typeSymbol).flags_field |= 0x40000000L;
/* 2238 */     return bool;
/*      */   }
/*      */
/*      */
/*      */   private void noteCyclic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) {
/* 2243 */     this.log.error(paramDiagnosticPosition, "cyclic.inheritance", new Object[] { paramClassSymbol });
/* 2244 */     for (List list = this.types.interfaces(paramClassSymbol.type); list.nonEmpty(); list = list.tail)
/* 2245 */       list.head = this.types.createErrorType((Symbol.ClassSymbol)((Type)list.head).tsym, (Type)Type.noType);
/* 2246 */     Type type = this.types.supertype(paramClassSymbol.type);
/* 2247 */     if (type.hasTag(TypeTag.CLASS))
/* 2248 */       ((Type.ClassType)paramClassSymbol.type).supertype_field = this.types.createErrorType((Symbol.ClassSymbol)type.tsym, (Type)Type.noType);
/* 2249 */     paramClassSymbol.type = this.types.createErrorType(paramClassSymbol, paramClassSymbol.type);
/* 2250 */     paramClassSymbol.flags_field |= 0x40000000L;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkImplementations(JCTree.JCClassDecl paramJCClassDecl) {
/* 2258 */     checkImplementations((JCTree)paramJCClassDecl, paramJCClassDecl.sym, paramJCClassDecl.sym);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void checkImplementations(JCTree paramJCTree, Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2) {
/* 2265 */     for (List list = this.types.closure(paramClassSymbol2.type); list.nonEmpty(); list = list.tail) {
/* 2266 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)((Type)list.head).tsym;
/* 2267 */       if ((this.allowGenerics || paramClassSymbol1 != classSymbol) && (classSymbol.flags() & 0x400L) != 0L) {
/* 2268 */         for (Scope.Entry entry = (classSymbol.members()).elems; entry != null; entry = entry.sibling) {
/* 2269 */           if (entry.sym.kind == 16 && (entry.sym
/* 2270 */             .flags() & 0x408L) == 1024L) {
/* 2271 */             Symbol.MethodSymbol methodSymbol1 = (Symbol.MethodSymbol)entry.sym;
/* 2272 */             Symbol.MethodSymbol methodSymbol2 = methodSymbol1.implementation((Symbol.TypeSymbol)paramClassSymbol1, this.types, false);
/* 2273 */             if (methodSymbol2 != null && methodSymbol2 != methodSymbol1 && (methodSymbol2.owner
/* 2274 */               .flags() & 0x200L) == (paramClassSymbol1
/* 2275 */               .flags() & 0x200L))
/*      */             {
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2282 */               checkOverride(paramJCTree, methodSymbol2, methodSymbol1, paramClassSymbol1);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkCompatibleSupertypes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 2296 */     List list1 = this.types.interfaces(paramType);
/* 2297 */     Type type = this.types.supertype(paramType);
/* 2298 */     if (type.hasTag(TypeTag.CLASS) && (type.tsym
/* 2299 */       .flags() & 0x400L) != 0L)
/* 2300 */       list1 = list1.prepend(type);
/* 2301 */     for (List list2 = list1; list2.nonEmpty(); list2 = list2.tail) {
/* 2302 */       if (this.allowGenerics && !((Type)list2.head).getTypeArguments().isEmpty() &&
/* 2303 */         !checkCompatibleAbstracts(paramDiagnosticPosition, (Type)list2.head, (Type)list2.head, paramType))
/*      */         return;
/* 2305 */       for (List list = list1; list != list2; list = list.tail) {
/* 2306 */         if (!checkCompatibleAbstracts(paramDiagnosticPosition, (Type)list2.head, (Type)list.head, paramType))
/*      */           return;
/*      */       }
/* 2309 */     }  checkCompatibleConcretes(paramDiagnosticPosition, paramType);
/*      */   }
/*      */
/*      */   void checkConflicts(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Symbol.TypeSymbol paramTypeSymbol) {
/* 2313 */     for (Type type = paramTypeSymbol.type; type != Type.noType; type = this.types.supertype(type)) {
/* 2314 */       for (Scope.Entry entry = type.tsym.members().lookup(paramSymbol.name); entry.scope == type.tsym.members(); entry = entry.next()) {
/*      */
/* 2316 */         if (paramSymbol.kind == entry.sym.kind && this.types
/* 2317 */           .isSameType(this.types.erasure(paramSymbol.type), this.types.erasure(entry.sym.type)) && paramSymbol != entry.sym && (paramSymbol
/*      */
/* 2319 */           .flags() & 0x1000L) != (entry.sym.flags() & 0x1000L) && (paramSymbol
/* 2320 */           .flags() & 0x200000L) == 0L && (entry.sym.flags() & 0x200000L) == 0L && (paramSymbol
/* 2321 */           .flags() & 0x80000000L) == 0L && (entry.sym.flags() & 0x80000000L) == 0L) {
/* 2322 */           syntheticError(paramDiagnosticPosition, ((entry.sym.flags() & 0x1000L) == 0L) ? entry.sym : paramSymbol);
/*      */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkOverrideClashes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Symbol.MethodSymbol paramMethodSymbol) {
/* 2337 */     ClashFilter clashFilter = new ClashFilter(paramType);
/*      */
/*      */
/*      */
/* 2341 */     List list = List.nil();
/* 2342 */     boolean bool = false;
/* 2343 */     for (Symbol symbol : this.types.membersClosure(paramType, false).getElementsByName(paramMethodSymbol.name, clashFilter)) {
/* 2344 */       if (!paramMethodSymbol.overrides(symbol, paramType.tsym, this.types, false)) {
/* 2345 */         if (symbol == paramMethodSymbol) {
/*      */           continue;
/*      */         }
/*      */
/* 2349 */         if (!bool) {
/* 2350 */           list = list.prepend(symbol);
/*      */         }
/*      */
/*      */         continue;
/*      */       }
/* 2355 */       if (symbol != paramMethodSymbol) {
/* 2356 */         bool = true;
/* 2357 */         list = List.nil();
/*      */       }
/*      */
/*      */
/* 2361 */       for (Symbol symbol1 : this.types.membersClosure(paramType, false).getElementsByName(paramMethodSymbol.name, clashFilter)) {
/* 2362 */         if (symbol1 == symbol) {
/*      */           continue;
/*      */         }
/* 2365 */         if (!this.types.isSubSignature(paramMethodSymbol.type, this.types.memberType(paramType, symbol1), this.allowStrictMethodClashCheck) && this.types
/* 2366 */           .hasSameArgs(symbol1.erasure(this.types), symbol.erasure(this.types))) {
/* 2367 */           paramMethodSymbol.flags_field |= 0x40000000000L;
/* 2368 */           String str = (symbol == paramMethodSymbol) ? "name.clash.same.erasure.no.override" : "name.clash.same.erasure.no.override.1";
/*      */
/*      */
/* 2371 */           this.log.error(paramDiagnosticPosition, str, new Object[] { paramMethodSymbol, paramMethodSymbol
/*      */
/* 2373 */                 .location(), symbol1, symbol1
/* 2374 */                 .location(), symbol, symbol
/* 2375 */                 .location() });
/*      */
/*      */           return;
/*      */         }
/*      */       }
/*      */     }
/* 2381 */     if (!bool) {
/* 2382 */       for (Symbol.MethodSymbol methodSymbol : list) {
/* 2383 */         checkPotentiallyAmbiguousOverloads(paramDiagnosticPosition, paramType, paramMethodSymbol, methodSymbol);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkHideClashes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Symbol.MethodSymbol paramMethodSymbol) {
/* 2396 */     ClashFilter clashFilter = new ClashFilter(paramType);
/*      */
/* 2398 */     for (Symbol symbol : this.types.membersClosure(paramType, true).getElementsByName(paramMethodSymbol.name, clashFilter)) {
/*      */
/*      */
/* 2401 */       if (!this.types.isSubSignature(paramMethodSymbol.type, this.types.memberType(paramType, symbol), this.allowStrictMethodClashCheck)) {
/* 2402 */         if (this.types.hasSameArgs(symbol.erasure(this.types), paramMethodSymbol.erasure(this.types))) {
/* 2403 */           this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.hide", new Object[] { paramMethodSymbol, paramMethodSymbol
/*      */
/* 2405 */                 .location(), symbol, symbol
/* 2406 */                 .location() });
/*      */           return;
/*      */         }
/* 2409 */         checkPotentiallyAmbiguousOverloads(paramDiagnosticPosition, paramType, paramMethodSymbol, (Symbol.MethodSymbol)symbol);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private class ClashFilter
/*      */     implements Filter<Symbol>
/*      */   {
/*      */     Type site;
/*      */
/*      */     ClashFilter(Type param1Type) {
/* 2421 */       this.site = param1Type;
/*      */     }
/*      */
/*      */     boolean shouldSkip(Symbol param1Symbol) {
/* 2425 */       return ((param1Symbol.flags() & 0x40000000000L) != 0L && param1Symbol.owner == this.site.tsym);
/*      */     }
/*      */
/*      */
/*      */     public boolean accepts(Symbol param1Symbol) {
/* 2430 */       return (param1Symbol.kind == 16 && (param1Symbol
/* 2431 */         .flags() & 0x1000L) == 0L &&
/* 2432 */         !shouldSkip(param1Symbol) && param1Symbol
/* 2433 */         .isInheritedIn((Symbol)this.site.tsym, Check.this.types) &&
/* 2434 */         !param1Symbol.isConstructor());
/*      */     }
/*      */   }
/*      */
/*      */   void checkDefaultMethodClashes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 2439 */     DefaultMethodClashFilter defaultMethodClashFilter = new DefaultMethodClashFilter(paramType);
/* 2440 */     for (Symbol symbol : this.types.membersClosure(paramType, false).getElements(defaultMethodClashFilter)) {
/* 2441 */       Assert.check((symbol.kind == 16));
/* 2442 */       List list = this.types.interfaceCandidates(paramType, (Symbol.MethodSymbol)symbol);
/* 2443 */       if (list.size() > 1) {
/* 2444 */         ListBuffer listBuffer1 = new ListBuffer();
/* 2445 */         ListBuffer listBuffer2 = new ListBuffer();
/* 2446 */         for (Symbol.MethodSymbol methodSymbol : list) {
/* 2447 */           if ((methodSymbol.flags() & 0x80000000000L) != 0L) {
/* 2448 */             listBuffer2 = listBuffer2.append(methodSymbol);
/* 2449 */           } else if ((methodSymbol.flags() & 0x400L) != 0L) {
/* 2450 */             listBuffer1 = listBuffer1.append(methodSymbol);
/*      */           }
/* 2452 */           if (listBuffer2.nonEmpty() && listBuffer2.size() + listBuffer1.size() >= 2) {
/*      */             String str;
/*      */
/*      */
/*      */
/* 2457 */             Symbol symbol2, symbol1 = (Symbol)listBuffer2.first();
/*      */
/* 2459 */             if (listBuffer2.size() > 1) {
/* 2460 */               str = "types.incompatible.unrelated.defaults";
/* 2461 */               symbol2 = (Symbol)(listBuffer2.toList()).tail.head;
/*      */             } else {
/* 2463 */               str = "types.incompatible.abstract.default";
/* 2464 */               symbol2 = (Symbol)listBuffer1.first();
/*      */             }
/* 2466 */             this.log.error(paramDiagnosticPosition, str, new Object[] {
/* 2467 */                   Kinds.kindName((Symbol)paramType.tsym), paramType, symbol.name, this.types
/* 2468 */                   .memberType(paramType, symbol).getParameterTypes(), symbol1
/* 2469 */                   .location(), symbol2.location()
/*      */                 });
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private class DefaultMethodClashFilter
/*      */     implements Filter<Symbol>
/*      */   {
/*      */     Type site;
/*      */
/*      */     DefaultMethodClashFilter(Type param1Type) {
/* 2483 */       this.site = param1Type;
/*      */     }
/*      */
/*      */     public boolean accepts(Symbol param1Symbol) {
/* 2487 */       return (param1Symbol.kind == 16 && (param1Symbol
/* 2488 */         .flags() & 0x80000000000L) != 0L && param1Symbol
/* 2489 */         .isInheritedIn((Symbol)this.site.tsym, Check.this.types) &&
/* 2490 */         !param1Symbol.isConstructor());
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkPotentiallyAmbiguousOverloads(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2) {
/* 2502 */     if (paramMethodSymbol1 != paramMethodSymbol2 && this.allowDefaultMethods && this.lint
/*      */
/* 2504 */       .isEnabled(Lint.LintCategory.OVERLOADS) && (paramMethodSymbol1
/* 2505 */       .flags() & 0x1000000000000L) == 0L && (paramMethodSymbol2
/* 2506 */       .flags() & 0x1000000000000L) == 0L) {
/* 2507 */       Type type1 = this.types.memberType(paramType, (Symbol)paramMethodSymbol1);
/* 2508 */       Type type2 = this.types.memberType(paramType, (Symbol)paramMethodSymbol2);
/*      */
/* 2510 */       if (type1.hasTag(TypeTag.FORALL) && type2.hasTag(TypeTag.FORALL) && this.types
/* 2511 */         .hasSameBounds((Type.ForAll)type1, (Type.ForAll)type2)) {
/* 2512 */         type2 = this.types.subst(type2, ((Type.ForAll)type2).tvars, ((Type.ForAll)type1).tvars);
/*      */       }
/*      */
/* 2515 */       int i = Math.max(type1.getParameterTypes().length(), type2.getParameterTypes().length());
/* 2516 */       List<Type> list1 = this.rs.adjustArgs(type1.getParameterTypes(), (Symbol)paramMethodSymbol1, i, true);
/* 2517 */       List<Type> list2 = this.rs.adjustArgs(type2.getParameterTypes(), (Symbol)paramMethodSymbol2, i, true);
/*      */
/* 2519 */       if (list1.length() != list2.length())
/* 2520 */         return;  boolean bool = false;
/* 2521 */       while (list1.nonEmpty() && list2.nonEmpty()) {
/* 2522 */         Type type3 = (Type)list1.head;
/* 2523 */         Type type4 = (Type)list2.head;
/* 2524 */         if (!this.types.isSubtype(type4, type3) && !this.types.isSubtype(type3, type4)) {
/* 2525 */           if (this.types.isFunctionalInterface(type3) && this.types.isFunctionalInterface(type4) && this.types
/* 2526 */             .findDescriptorType(type3).getParameterTypes().length() > 0 && this.types
/* 2527 */             .findDescriptorType(type3).getParameterTypes().length() == this.types
/* 2528 */             .findDescriptorType(type4).getParameterTypes().length()) {
/* 2529 */             bool = true;
/*      */           } else {
/*      */             break;
/*      */           }
/*      */         }
/* 2534 */         list1 = list1.tail;
/* 2535 */         list2 = list2.tail;
/*      */       }
/* 2537 */       if (bool) {
/*      */
/*      */
/* 2540 */         paramMethodSymbol1.flags_field |= 0x1000000000000L;
/* 2541 */         paramMethodSymbol2.flags_field |= 0x1000000000000L;
/* 2542 */         this.log.warning(Lint.LintCategory.OVERLOADS, paramDiagnosticPosition, "potentially.ambiguous.overload", new Object[] { paramMethodSymbol1, paramMethodSymbol1
/* 2543 */               .location(), paramMethodSymbol2, paramMethodSymbol2
/* 2544 */               .location() });
/*      */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void checkElemAccessFromSerializableLambda(JCTree paramJCTree) {
/* 2551 */     if (this.warnOnAccessToSensitiveMembers) {
/* 2552 */       Symbol symbol = TreeInfo.symbol(paramJCTree);
/* 2553 */       if ((symbol.kind & 0x14) == 0) {
/*      */         return;
/*      */       }
/*      */
/* 2557 */       if (symbol.kind == 4 && ((
/* 2558 */         symbol.flags() & 0x200000000L) != 0L || symbol
/* 2559 */         .isLocal() || symbol.name == this.names._this || symbol.name == this.names._super)) {
/*      */         return;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/* 2566 */       if (!this.types.isSubtype(symbol.owner.type, this.syms.serializableType) &&
/* 2567 */         isEffectivelyNonPublic(symbol)) {
/* 2568 */         this.log.warning(paramJCTree.pos(), "access.to.sensitive.member.from.serializable.element", new Object[] { symbol });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private boolean isEffectivelyNonPublic(Symbol paramSymbol) {
/* 2575 */     if (paramSymbol.packge() == this.syms.rootPackage) {
/* 2576 */       return false;
/*      */     }
/*      */
/* 2579 */     while (paramSymbol.kind != 1) {
/* 2580 */       if ((paramSymbol.flags() & 0x1L) == 0L) {
/* 2581 */         return true;
/*      */       }
/* 2583 */       paramSymbol = paramSymbol.owner;
/*      */     }
/* 2585 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void syntheticError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 2591 */     if (!paramSymbol.type.isErroneous()) {
/* 2592 */       if (this.warnOnSyntheticConflicts) {
/* 2593 */         this.log.warning(paramDiagnosticPosition, "synthetic.name.conflict", new Object[] { paramSymbol, paramSymbol.location() });
/*      */       } else {
/*      */
/* 2596 */         this.log.error(paramDiagnosticPosition, "synthetic.name.conflict", new Object[] { paramSymbol, paramSymbol.location() });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkClassBounds(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 2607 */     checkClassBounds(paramDiagnosticPosition, new HashMap<>(), paramType);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkClassBounds(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Map<Symbol.TypeSymbol, Type> paramMap, Type paramType) {
/* 2617 */     if (paramType.isErroneous())
/* 2618 */       return;  for (List list = this.types.interfaces(paramType); list.nonEmpty(); list = list.tail) {
/* 2619 */       Type type1 = (Type)list.head;
/* 2620 */       Type type2 = paramMap.put(type1.tsym, type1);
/* 2621 */       if (type2 != null) {
/* 2622 */         List list1 = type2.allparams();
/* 2623 */         List list2 = type1.allparams();
/* 2624 */         if (!this.types.containsTypeEquivalent(list1, list2))
/* 2625 */           this.log.error(paramDiagnosticPosition, "cant.inherit.diff.arg", new Object[] { type1.tsym,
/* 2626 */                 Type.toString(list1),
/* 2627 */                 Type.toString(list2) });
/*      */       }
/* 2629 */       checkClassBounds(paramDiagnosticPosition, paramMap, type1);
/*      */     }
/* 2631 */     Type type = this.types.supertype(paramType);
/* 2632 */     if (type != Type.noType) checkClassBounds(paramDiagnosticPosition, paramMap, type);
/*      */
/*      */   }
/*      */
/*      */
/*      */
/*      */   void checkNotRepeated(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Set<Type> paramSet) {
/* 2639 */     if (paramSet.contains(paramType)) {
/* 2640 */       this.log.error(paramDiagnosticPosition, "repeated.interface", new Object[0]);
/*      */     } else {
/* 2642 */       paramSet.add(paramType);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void validateAnnotationTree(JCTree paramJCTree) {
/*      */     class AnnotationValidator
/*      */       extends TreeScanner
/*      */     {
/*      */       public void visitAnnotation(JCTree.JCAnnotation param1JCAnnotation) {
/* 2657 */         if (!param1JCAnnotation.type.isErroneous()) {
/* 2658 */           super.visitAnnotation(param1JCAnnotation);
/* 2659 */           Check.this.validateAnnotation(param1JCAnnotation);
/*      */         }
/*      */       }
/*      */     };
/* 2663 */     paramJCTree.accept((JCTree.Visitor)new AnnotationValidator());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void validateAnnotationType(JCTree paramJCTree) {
/* 2675 */     if (paramJCTree != null) {
/* 2676 */       validateAnnotationType(paramJCTree.pos(), paramJCTree.type);
/*      */     }
/*      */   }
/*      */
/*      */   void validateAnnotationType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 2681 */     if (paramType.isPrimitive())
/* 2682 */       return;  if (this.types.isSameType(paramType, this.syms.stringType))
/* 2683 */       return;  if ((paramType.tsym.flags() & 0x4000L) != 0L)
/* 2684 */       return;  if ((paramType.tsym.flags() & 0x2000L) != 0L)
/* 2685 */       return;  if ((this.types.cvarLowerBound(paramType)).tsym == this.syms.classType.tsym)
/* 2686 */       return;  if (this.types.isArray(paramType) && !this.types.isArray(this.types.elemtype(paramType))) {
/* 2687 */       validateAnnotationType(paramDiagnosticPosition, this.types.elemtype(paramType));
/*      */       return;
/*      */     }
/* 2690 */     this.log.error(paramDiagnosticPosition, "invalid.annotation.member.type", new Object[0]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void validateAnnotationMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.MethodSymbol paramMethodSymbol) {
/* 2702 */     for (Type type = this.syms.annotationType; type.hasTag(TypeTag.CLASS); type = this.types.supertype(type)) {
/* 2703 */       Scope scope = type.tsym.members();
/* 2704 */       for (Scope.Entry entry = scope.lookup(paramMethodSymbol.name); entry.scope != null; entry = entry.next()) {
/* 2705 */         if (entry.sym.kind == 16 && (entry.sym
/* 2706 */           .flags() & 0x5L) != 0L && this.types
/* 2707 */           .overrideEquivalent(paramMethodSymbol.type, entry.sym.type)) {
/* 2708 */           this.log.error(paramDiagnosticPosition, "intf.annotation.member.clash", new Object[] { entry.sym, type });
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void validateAnnotations(List<JCTree.JCAnnotation> paramList, Symbol paramSymbol) {
/* 2716 */     for (JCTree.JCAnnotation jCAnnotation : paramList) {
/* 2717 */       validateAnnotation(jCAnnotation, paramSymbol);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void validateTypeAnnotations(List<JCTree.JCAnnotation> paramList, boolean paramBoolean) {
/* 2723 */     for (JCTree.JCAnnotation jCAnnotation : paramList) {
/* 2724 */       validateTypeAnnotation(jCAnnotation, paramBoolean);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void validateAnnotation(JCTree.JCAnnotation paramJCAnnotation, Symbol paramSymbol) {
/* 2730 */     validateAnnotationTree((JCTree)paramJCAnnotation);
/*      */
/* 2732 */     if (!annotationApplicable(paramJCAnnotation, paramSymbol)) {
/* 2733 */       this.log.error(paramJCAnnotation.pos(), "annotation.type.not.applicable", new Object[0]);
/*      */     }
/* 2735 */     if (paramJCAnnotation.annotationType.type.tsym == this.syms.functionalInterfaceType.tsym) {
/* 2736 */       if (paramSymbol.kind != 2) {
/* 2737 */         this.log.error(paramJCAnnotation.pos(), "bad.functional.intf.anno", new Object[0]);
/* 2738 */       } else if (!paramSymbol.isInterface() || (paramSymbol.flags() & 0x2000L) != 0L) {
/* 2739 */         this.log.error(paramJCAnnotation.pos(), "bad.functional.intf.anno.1", new Object[] { this.diags.fragment("not.a.functional.intf", new Object[] { paramSymbol }) });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   public void validateTypeAnnotation(JCTree.JCAnnotation paramJCAnnotation, boolean paramBoolean) {
/* 2745 */     Assert.checkNonNull(paramJCAnnotation.type, "annotation tree hasn't been attributed yet: " + paramJCAnnotation);
/* 2746 */     validateAnnotationTree((JCTree)paramJCAnnotation);
/*      */
/* 2748 */     if (paramJCAnnotation.hasTag(JCTree.Tag.TYPE_ANNOTATION) &&
/* 2749 */       !paramJCAnnotation.annotationType.type.isErroneous() &&
/* 2750 */       !isTypeAnnotation(paramJCAnnotation, paramBoolean)) {
/* 2751 */       this.log.error(paramJCAnnotation.pos(), "annotation.type.not.applicable", new Object[0]);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void validateRepeatable(Symbol.TypeSymbol paramTypeSymbol, Attribute.Compound paramCompound, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2765 */     Assert.check(this.types.isSameType(paramCompound.type, this.syms.repeatableType));
/*      */
/* 2767 */     Type type = null;
/* 2768 */     List list = paramCompound.values;
/* 2769 */     if (!list.isEmpty()) {
/* 2770 */       Assert.check((((Symbol.MethodSymbol)((Pair)list.head).fst).name == this.names.value));
/* 2771 */       type = ((Attribute.Class)((Pair)list.head).snd).getValue();
/*      */     }
/*      */
/* 2774 */     if (type == null) {
/*      */       return;
/*      */     }
/*      */
/*      */
/* 2779 */     validateValue(type.tsym, paramTypeSymbol, paramDiagnosticPosition);
/* 2780 */     validateRetention((Symbol)type.tsym, (Symbol)paramTypeSymbol, paramDiagnosticPosition);
/* 2781 */     validateDocumented((Symbol)type.tsym, (Symbol)paramTypeSymbol, paramDiagnosticPosition);
/* 2782 */     validateInherited((Symbol)type.tsym, (Symbol)paramTypeSymbol, paramDiagnosticPosition);
/* 2783 */     validateTarget((Symbol)type.tsym, (Symbol)paramTypeSymbol, paramDiagnosticPosition);
/* 2784 */     validateDefault((Symbol)type.tsym, paramDiagnosticPosition);
/*      */   }
/*      */
/*      */   private void validateValue(Symbol.TypeSymbol paramTypeSymbol1, Symbol.TypeSymbol paramTypeSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2788 */     Scope.Entry entry = paramTypeSymbol1.members().lookup(this.names.value);
/* 2789 */     if (entry.scope != null && entry.sym.kind == 16) {
/* 2790 */       Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.sym;
/* 2791 */       Type type = methodSymbol.getReturnType();
/* 2792 */       if (!type.hasTag(TypeTag.ARRAY) || !this.types.isSameType(((Type.ArrayType)type).elemtype, paramTypeSymbol2.type)) {
/* 2793 */         this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.value.return", new Object[] { paramTypeSymbol1, type, this.types
/* 2794 */               .makeArrayType(paramTypeSymbol2.type) });
/*      */       }
/*      */     } else {
/* 2797 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.no.value", new Object[] { paramTypeSymbol1 });
/*      */     }
/*      */   }
/*      */
/*      */   private void validateRetention(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2802 */     Attribute.RetentionPolicy retentionPolicy1 = this.types.getRetention(paramSymbol1);
/* 2803 */     Attribute.RetentionPolicy retentionPolicy2 = this.types.getRetention(paramSymbol2);
/*      */
/* 2805 */     boolean bool = false;
/* 2806 */     switch (retentionPolicy2) {
/*      */       case UNCHECKED:
/* 2808 */         if (retentionPolicy1 != Attribute.RetentionPolicy.RUNTIME) {
/* 2809 */           bool = true;
/*      */         }
/*      */         break;
/*      */       case VARARGS:
/* 2813 */         if (retentionPolicy1 == Attribute.RetentionPolicy.SOURCE)
/* 2814 */           bool = true;
/*      */         break;
/*      */     }
/* 2817 */     if (bool) {
/* 2818 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.retention", new Object[] { paramSymbol1, retentionPolicy1, paramSymbol2, retentionPolicy2 });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void validateDocumented(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2825 */     if (paramSymbol2.attribute((Symbol)this.syms.documentedType.tsym) != null &&
/* 2826 */       paramSymbol1.attribute((Symbol)this.syms.documentedType.tsym) == null) {
/* 2827 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.not.documented", new Object[] { paramSymbol1, paramSymbol2 });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void validateInherited(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2833 */     if (paramSymbol2.attribute((Symbol)this.syms.inheritedType.tsym) != null &&
/* 2834 */       paramSymbol1.attribute((Symbol)this.syms.inheritedType.tsym) == null) {
/* 2835 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.not.inherited", new Object[] { paramSymbol1, paramSymbol2 });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void validateTarget(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/*      */     Set<Name> set1, set2;
/* 2847 */     Attribute.Array array1 = getAttributeTargetAttribute(paramSymbol1);
/* 2848 */     if (array1 == null) {
/* 2849 */       set1 = getDefaultTargetSet();
/*      */     } else {
/* 2851 */       set1 = new HashSet();
/* 2852 */       for (Attribute attribute : array1.values) {
/* 2853 */         if (attribute instanceof Attribute.Enum) {
/*      */
/*      */
/* 2856 */           Attribute.Enum enum_ = (Attribute.Enum)attribute;
/* 2857 */           set1.add(enum_.value.name);
/*      */         }
/*      */       }
/*      */     }
/*      */
/* 2862 */     Attribute.Array array2 = getAttributeTargetAttribute(paramSymbol2);
/* 2863 */     if (array2 == null) {
/* 2864 */       set2 = getDefaultTargetSet();
/*      */     } else {
/* 2866 */       set2 = new HashSet();
/* 2867 */       for (Attribute attribute : array2.values) {
/* 2868 */         if (attribute instanceof Attribute.Enum) {
/*      */
/*      */
/* 2871 */           Attribute.Enum enum_ = (Attribute.Enum)attribute;
/* 2872 */           set2.add(enum_.value.name);
/*      */         }
/*      */       }
/*      */     }
/* 2876 */     if (!isTargetSubsetOf(set1, set2)) {
/* 2877 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.incompatible.target", new Object[] { paramSymbol1, paramSymbol2 });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private Set<Name> getDefaultTargetSet() {
/* 2883 */     if (this.defaultTargets == null) {
/* 2884 */       HashSet<Name> hashSet = new HashSet();
/* 2885 */       hashSet.add(this.names.ANNOTATION_TYPE);
/* 2886 */       hashSet.add(this.names.CONSTRUCTOR);
/* 2887 */       hashSet.add(this.names.FIELD);
/* 2888 */       hashSet.add(this.names.LOCAL_VARIABLE);
/* 2889 */       hashSet.add(this.names.METHOD);
/* 2890 */       hashSet.add(this.names.PACKAGE);
/* 2891 */       hashSet.add(this.names.PARAMETER);
/* 2892 */       hashSet.add(this.names.TYPE);
/*      */
/* 2894 */       this.defaultTargets = Collections.unmodifiableSet(hashSet);
/*      */     }
/*      */
/* 2897 */     return this.defaultTargets;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean isTargetSubsetOf(Set<Name> paramSet1, Set<Name> paramSet2) {
/* 2909 */     for (Name name : paramSet1) {
/* 2910 */       boolean bool = false;
/* 2911 */       for (Name name1 : paramSet2) {
/* 2912 */         if (name1 == name) {
/* 2913 */           bool = true; break;
/*      */         }
/* 2915 */         if (name1 == this.names.TYPE && name == this.names.ANNOTATION_TYPE) {
/* 2916 */           bool = true; break;
/*      */         }
/* 2918 */         if (name1 == this.names.TYPE_USE && (name == this.names.TYPE || name == this.names.ANNOTATION_TYPE || name == this.names.TYPE_PARAMETER)) {
/*      */
/*      */
/*      */
/* 2922 */           bool = true;
/*      */           break;
/*      */         }
/*      */       }
/* 2926 */       if (!bool)
/* 2927 */         return false;
/*      */     }
/* 2929 */     return true;
/*      */   }
/*      */
/*      */
/*      */   private void validateDefault(Symbol paramSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2934 */     Scope scope = paramSymbol.members();
/* 2935 */     for (Symbol symbol : scope.getElements()) {
/* 2936 */       if (symbol.name != this.names.value && symbol.kind == 16 && ((Symbol.MethodSymbol)symbol).defaultValue == null)
/*      */       {
/*      */
/* 2939 */         this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.elem.nondefault", new Object[] { paramSymbol, symbol });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   boolean isOverrider(Symbol paramSymbol) {
/* 2949 */     if (paramSymbol.kind != 16 || paramSymbol.isStatic())
/* 2950 */       return false;
/* 2951 */     Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)paramSymbol;
/* 2952 */     Symbol.TypeSymbol typeSymbol = (Symbol.TypeSymbol)methodSymbol.owner;
/* 2953 */     for (Type type : this.types.closure(typeSymbol.type)) {
/* 2954 */       if (type == typeSymbol.type)
/*      */         continue;
/* 2956 */       Scope scope = type.tsym.members();
/* 2957 */       for (Scope.Entry entry = scope.lookup(methodSymbol.name); entry.scope != null; entry = entry.next()) {
/* 2958 */         if (!entry.sym.isStatic() && methodSymbol.overrides(entry.sym, typeSymbol, this.types, true))
/* 2959 */           return true;
/*      */       }
/*      */     }
/* 2962 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   protected boolean isTypeAnnotation(JCTree.JCAnnotation paramJCAnnotation, boolean paramBoolean) {
/* 2968 */     Attribute.Compound compound = paramJCAnnotation.annotationType.type.tsym.attribute((Symbol)this.syms.annotationTargetType.tsym);
/* 2969 */     if (compound == null)
/*      */     {
/* 2971 */       return false;
/*      */     }
/*      */
/* 2974 */     Attribute attribute = compound.member(this.names.value);
/* 2975 */     if (!(attribute instanceof Attribute.Array)) {
/* 2976 */       return false;
/*      */     }
/*      */
/* 2979 */     Attribute.Array array = (Attribute.Array)attribute;
/* 2980 */     for (Attribute attribute1 : array.values) {
/* 2981 */       if (!(attribute1 instanceof Attribute.Enum)) {
/* 2982 */         return false;
/*      */       }
/* 2984 */       Attribute.Enum enum_ = (Attribute.Enum)attribute1;
/*      */
/* 2986 */       if (enum_.value.name == this.names.TYPE_USE)
/* 2987 */         return true;
/* 2988 */       if (paramBoolean && enum_.value.name == this.names.TYPE_PARAMETER)
/* 2989 */         return true;
/*      */     }
/* 2991 */     return false;
/*      */   }
/*      */
/*      */   boolean annotationApplicable(JCTree.JCAnnotation paramJCAnnotation, Symbol paramSymbol) {
/*      */     Name[] arrayOfName;
/* 2996 */     Attribute.Array array = getAttributeTargetAttribute((Symbol)paramJCAnnotation.annotationType.type.tsym);
/*      */
/*      */
/* 2999 */     if (array == null) {
/* 3000 */       arrayOfName = defaultTargetMetaInfo(paramJCAnnotation, paramSymbol);
/*      */     } else {
/*      */
/* 3003 */       arrayOfName = new Name[array.values.length];
/* 3004 */       for (byte b = 0; b < array.values.length; b++) {
/* 3005 */         Attribute attribute = array.values[b];
/* 3006 */         if (!(attribute instanceof Attribute.Enum)) {
/* 3007 */           return true;
/*      */         }
/* 3009 */         Attribute.Enum enum_ = (Attribute.Enum)attribute;
/* 3010 */         arrayOfName[b] = enum_.value.name;
/*      */       }
/*      */     }
/* 3013 */     for (Name name : arrayOfName) {
/* 3014 */       if (name == this.names.TYPE) {
/* 3015 */         if (paramSymbol.kind == 2) return true;
/* 3016 */       } else if (name == this.names.FIELD) {
/* 3017 */         if (paramSymbol.kind == 4 && paramSymbol.owner.kind != 16) return true;
/* 3018 */       } else if (name == this.names.METHOD) {
/* 3019 */         if (paramSymbol.kind == 16 && !paramSymbol.isConstructor()) return true;
/* 3020 */       } else if (name == this.names.PARAMETER) {
/* 3021 */         if (paramSymbol.kind == 4 && paramSymbol.owner.kind == 16 && (paramSymbol
/*      */
/* 3023 */           .flags() & 0x200000000L) != 0L) {
/* 3024 */           return true;
/*      */         }
/* 3026 */       } else if (name == this.names.CONSTRUCTOR) {
/* 3027 */         if (paramSymbol.kind == 16 && paramSymbol.isConstructor()) return true;
/* 3028 */       } else if (name == this.names.LOCAL_VARIABLE) {
/* 3029 */         if (paramSymbol.kind == 4 && paramSymbol.owner.kind == 16 && (paramSymbol
/* 3030 */           .flags() & 0x200000000L) == 0L) {
/* 3031 */           return true;
/*      */         }
/* 3033 */       } else if (name == this.names.ANNOTATION_TYPE) {
/* 3034 */         if (paramSymbol.kind == 2 && (paramSymbol.flags() & 0x2000L) != 0L) {
/* 3035 */           return true;
/*      */         }
/* 3037 */       } else if (name == this.names.PACKAGE) {
/* 3038 */         if (paramSymbol.kind == 1) return true;
/* 3039 */       } else if (name == this.names.TYPE_USE) {
/* 3040 */         if (paramSymbol.kind == 2 || paramSymbol.kind == 4 || (paramSymbol.kind == 16 &&
/*      */
/* 3042 */           !paramSymbol.isConstructor() &&
/* 3043 */           !paramSymbol.type.getReturnType().hasTag(TypeTag.VOID)) || (paramSymbol.kind == 16 && paramSymbol
/* 3044 */           .isConstructor())) {
/* 3045 */           return true;
/*      */         }
/* 3047 */       } else if (name == this.names.TYPE_PARAMETER) {
/* 3048 */         if (paramSymbol.kind == 2 && paramSymbol.type.hasTag(TypeTag.TYPEVAR)) {
/* 3049 */           return true;
/*      */         }
/*      */       } else {
/* 3052 */         return true;
/*      */       }
/* 3054 */     }  return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   Attribute.Array getAttributeTargetAttribute(Symbol paramSymbol) {
/* 3060 */     Attribute.Compound compound = paramSymbol.attribute((Symbol)this.syms.annotationTargetType.tsym);
/* 3061 */     if (compound == null) return null;
/* 3062 */     Attribute attribute = compound.member(this.names.value);
/* 3063 */     if (!(attribute instanceof Attribute.Array)) return null;
/* 3064 */     return (Attribute.Array)attribute;
/*      */   }
/*      */
/*      */
/*      */   private Name[] defaultTargetMetaInfo(JCTree.JCAnnotation paramJCAnnotation, Symbol paramSymbol) {
/* 3069 */     return this.dfltTargetMeta;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean validateAnnotationDeferErrors(JCTree.JCAnnotation paramJCAnnotation) {
/* 3078 */     boolean bool = false;
/* 3079 */     Log.DiscardDiagnosticHandler discardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */     try {
/* 3081 */       bool = validateAnnotation(paramJCAnnotation);
/*      */     } finally {
/* 3083 */       this.log.popDiagnosticHandler((Log.DiagnosticHandler)discardDiagnosticHandler);
/*      */     }
/* 3085 */     return bool;
/*      */   }
/*      */
/*      */   private boolean validateAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 3089 */     boolean bool = true;
/*      */
/* 3091 */     LinkedHashSet<Symbol.MethodSymbol> linkedHashSet = new LinkedHashSet();
/* 3092 */     Scope.Entry entry = (paramJCAnnotation.annotationType.type.tsym.members()).elems;
/* 3093 */     for (; entry != null;
/* 3094 */       entry = entry.sibling) {
/* 3095 */       if (entry.sym.kind == 16 && entry.sym.name != this.names.clinit && (entry.sym
/* 3096 */         .flags() & 0x1000L) == 0L) {
/* 3097 */         linkedHashSet.add((Symbol.MethodSymbol)entry.sym);
/*      */       }
/*      */     }
/* 3100 */     for (JCTree jCTree : paramJCAnnotation.args) {
/* 3101 */       if (!jCTree.hasTag(JCTree.Tag.ASSIGN))
/* 3102 */         continue;  JCTree.JCAssign jCAssign1 = (JCTree.JCAssign)jCTree;
/* 3103 */       Symbol symbol1 = TreeInfo.symbol((JCTree)jCAssign1.lhs);
/* 3104 */       if (symbol1 != null && !symbol1.type.isErroneous() &&
/* 3105 */         !linkedHashSet.remove(symbol1)) {
/* 3106 */         bool = false;
/* 3107 */         this.log.error(jCAssign1.lhs.pos(), "duplicate.annotation.member.value", new Object[] { symbol1.name, paramJCAnnotation.type });
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/* 3113 */     List list = List.nil();
/* 3114 */     for (Symbol.MethodSymbol methodSymbol : linkedHashSet) {
/* 3115 */       if (methodSymbol.defaultValue == null && !methodSymbol.type.isErroneous()) {
/* 3116 */         list = list.append(methodSymbol.name);
/*      */       }
/*      */     }
/* 3119 */     list = list.reverse();
/* 3120 */     if (list.nonEmpty()) {
/* 3121 */       bool = false;
/* 3122 */       String str = (list.size() > 1) ? "annotation.missing.default.value.1" : "annotation.missing.default.value";
/*      */
/*      */
/* 3125 */       this.log.error(paramJCAnnotation.pos(), str, new Object[] { paramJCAnnotation.type, list });
/*      */     }
/*      */
/*      */
/*      */
/* 3130 */     if (paramJCAnnotation.annotationType.type.tsym != this.syms.annotationTargetType.tsym || paramJCAnnotation.args.tail == null)
/*      */     {
/* 3132 */       return bool;
/*      */     }
/* 3134 */     if (!((JCTree.JCExpression)paramJCAnnotation.args.head).hasTag(JCTree.Tag.ASSIGN)) return false;
/* 3135 */     JCTree.JCAssign jCAssign = (JCTree.JCAssign)paramJCAnnotation.args.head;
/* 3136 */     Symbol symbol = TreeInfo.symbol((JCTree)jCAssign.lhs);
/* 3137 */     if (symbol.name != this.names.value) return false;
/* 3138 */     JCTree.JCExpression jCExpression = jCAssign.rhs;
/* 3139 */     if (!jCExpression.hasTag(JCTree.Tag.NEWARRAY)) return false;
/* 3140 */     JCTree.JCNewArray jCNewArray = (JCTree.JCNewArray)jCExpression;
/* 3141 */     HashSet<Symbol> hashSet = new HashSet();
/* 3142 */     for (JCTree jCTree : jCNewArray.elems) {
/* 3143 */       if (!hashSet.add(TreeInfo.symbol(jCTree))) {
/* 3144 */         bool = false;
/* 3145 */         this.log.error(jCTree.pos(), "repeated.annotation.target", new Object[0]);
/*      */       }
/*      */     }
/* 3148 */     return bool;
/*      */   }
/*      */
/*      */   void checkDeprecatedAnnotation(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 3152 */     if (this.allowAnnotations && this.lint
/* 3153 */       .isEnabled(Lint.LintCategory.DEP_ANN) && (paramSymbol
/* 3154 */       .flags() & 0x20000L) != 0L &&
/* 3155 */       !this.syms.deprecatedType.isErroneous() && paramSymbol
/* 3156 */       .attribute((Symbol)this.syms.deprecatedType.tsym) == null) {
/* 3157 */       this.log.warning(Lint.LintCategory.DEP_ANN, paramDiagnosticPosition, "missing.deprecated.annotation", new Object[0]);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void checkDeprecated(final JCDiagnostic.DiagnosticPosition pos, Symbol paramSymbol1, final Symbol s) {
/* 3163 */     if ((s.flags() & 0x20000L) != 0L && (paramSymbol1
/* 3164 */       .flags() & 0x20000L) == 0L && s
/* 3165 */       .outermostClass() != paramSymbol1.outermostClass()) {
/* 3166 */       this.deferredLintHandler.report(new DeferredLintHandler.LintLogger()
/*      */           {
/*      */             public void report() {
/* 3169 */               Check.this.warnDeprecated(pos, s);
/*      */             }
/*      */           });
/*      */     }
/*      */   }
/*      */
/*      */   void checkSunAPI(final JCDiagnostic.DiagnosticPosition pos, final Symbol s) {
/* 3176 */     if ((s.flags() & 0x4000000000L) != 0L)
/* 3177 */       this.deferredLintHandler.report(new DeferredLintHandler.LintLogger() {
/*      */             public void report() {
/* 3179 */               if (Check.this.enableSunApiLintControl) {
/* 3180 */                 Check.this.warnSunApi(pos, "sun.proprietary", new Object[] { this.val$s });
/*      */               } else {
/* 3182 */                 Check.this.log.mandatoryWarning(pos, "sun.proprietary", new Object[] { this.val$s });
/*      */               }
/*      */             }
/*      */           });
/*      */   }
/*      */
/*      */   void checkProfile(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 3189 */     if (this.profile != Profile.DEFAULT && (paramSymbol.flags() & 0x200000000000L) != 0L) {
/* 3190 */       this.log.error(paramDiagnosticPosition, "not.in.profile", new Object[] { paramSymbol, this.profile });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkNonCyclicElements(JCTree.JCClassDecl paramJCClassDecl) {
/* 3201 */     if ((paramJCClassDecl.sym.flags_field & 0x2000L) == 0L)
/* 3202 */       return;  Assert.check(((paramJCClassDecl.sym.flags_field & 0x8000000L) == 0L));
/*      */     try {
/* 3204 */       paramJCClassDecl.sym.flags_field |= 0x8000000L;
/* 3205 */       for (JCTree jCTree : paramJCClassDecl.defs) {
/* 3206 */         if (!jCTree.hasTag(JCTree.Tag.METHODDEF))
/* 3207 */           continue;  JCTree.JCMethodDecl jCMethodDecl = (JCTree.JCMethodDecl)jCTree;
/* 3208 */         checkAnnotationResType(jCMethodDecl.pos(), jCMethodDecl.restype.type);
/*      */       }
/*      */     } finally {
/* 3211 */       paramJCClassDecl.sym.flags_field &= 0xFFFFFFFFF7FFFFFFL;
/* 3212 */       paramJCClassDecl.sym.flags_field |= 0x800000000L;
/*      */     }
/*      */   }
/*      */
/*      */   void checkNonCyclicElementsInternal(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol) {
/* 3217 */     if ((paramTypeSymbol.flags_field & 0x800000000L) != 0L)
/*      */       return;
/* 3219 */     if ((paramTypeSymbol.flags_field & 0x8000000L) != 0L) {
/* 3220 */       this.log.error(paramDiagnosticPosition, "cyclic.annotation.element", new Object[0]);
/*      */       return;
/*      */     }
/*      */     try {
/* 3224 */       paramTypeSymbol.flags_field |= 0x8000000L;
/* 3225 */       for (Scope.Entry entry = (paramTypeSymbol.members()).elems; entry != null; entry = entry.sibling) {
/* 3226 */         Symbol symbol = entry.sym;
/* 3227 */         if (symbol.kind == 16)
/*      */         {
/* 3229 */           checkAnnotationResType(paramDiagnosticPosition, ((Symbol.MethodSymbol)symbol).type.getReturnType()); }
/*      */       }
/*      */     } finally {
/* 3232 */       paramTypeSymbol.flags_field &= 0xFFFFFFFFF7FFFFFFL;
/* 3233 */       paramTypeSymbol.flags_field |= 0x800000000L;
/*      */     }
/*      */   }
/*      */
/*      */   void checkAnnotationResType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/* 3238 */     switch (paramType.getTag()) {
/*      */       case UNCHECKED:
/* 3240 */         if ((paramType.tsym.flags() & 0x2000L) != 0L)
/* 3241 */           checkNonCyclicElementsInternal(paramDiagnosticPosition, paramType.tsym);
/*      */         break;
/*      */       case VARARGS:
/* 3244 */         checkAnnotationResType(paramDiagnosticPosition, this.types.elemtype(paramType));
/*      */         break;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkCyclicConstructors(JCTree.JCClassDecl paramJCClassDecl) {
/* 3259 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*      */
/*      */
/* 3262 */     for (List list = paramJCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 3263 */       JCTree.JCMethodInvocation jCMethodInvocation = TreeInfo.firstConstructorCall((JCTree)list.head);
/* 3264 */       if (jCMethodInvocation != null) {
/* 3265 */         JCTree.JCMethodDecl jCMethodDecl = (JCTree.JCMethodDecl)list.head;
/* 3266 */         if (TreeInfo.name((JCTree)jCMethodInvocation.meth) == this.names._this) {
/* 3267 */           hashMap.put(jCMethodDecl.sym, TreeInfo.symbol((JCTree)jCMethodInvocation.meth));
/*      */         } else {
/* 3269 */           jCMethodDecl.sym.flags_field |= 0x40000000L;
/*      */         }
/*      */       }
/*      */     }
/*      */
/* 3274 */     Symbol[] arrayOfSymbol = new Symbol[0];
/* 3275 */     arrayOfSymbol = (Symbol[])hashMap.keySet().toArray((Object[])arrayOfSymbol);
/* 3276 */     for (Symbol symbol : arrayOfSymbol) {
/* 3277 */       checkCyclicConstructor(paramJCClassDecl, symbol, (Map)hashMap);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void checkCyclicConstructor(JCTree.JCClassDecl paramJCClassDecl, Symbol paramSymbol, Map<Symbol, Symbol> paramMap) {
/* 3286 */     if (paramSymbol != null && (paramSymbol.flags_field & 0x40000000L) == 0L) {
/* 3287 */       if ((paramSymbol.flags_field & 0x8000000L) != 0L) {
/* 3288 */         this.log.error(TreeInfo.diagnosticPositionFor(paramSymbol, (JCTree)paramJCClassDecl), "recursive.ctor.invocation", new Object[0]);
/*      */       } else {
/*      */
/* 3291 */         paramSymbol.flags_field |= 0x8000000L;
/* 3292 */         checkCyclicConstructor(paramJCClassDecl, paramMap.remove(paramSymbol), paramMap);
/* 3293 */         paramSymbol.flags_field &= 0xFFFFFFFFF7FFFFFFL;
/*      */       }
/* 3295 */       paramSymbol.flags_field |= 0x40000000L;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   int checkOperator(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.OperatorSymbol paramOperatorSymbol, JCTree.Tag paramTag, Type paramType1, Type paramType2) {
/* 3317 */     if (paramOperatorSymbol.opcode == 277) {
/* 3318 */       this.log.error(paramDiagnosticPosition, "operator.cant.be.applied.1", new Object[] { this.treeinfo
/*      */
/* 3320 */             .operatorName(paramTag), paramType1, paramType2 });
/*      */     }
/*      */
/* 3323 */     return paramOperatorSymbol.opcode;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void checkDivZero(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Type paramType) {
/* 3334 */     if (paramType.constValue() != null && this.lint
/* 3335 */       .isEnabled(Lint.LintCategory.DIVZERO) && paramType
/* 3336 */       .getTag().isSubRangeOf(TypeTag.LONG) && ((Number)paramType
/* 3337 */       .constValue()).longValue() == 0L) {
/* 3338 */       int i = ((Symbol.OperatorSymbol)paramSymbol).opcode;
/* 3339 */       if (i == 108 || i == 112 || i == 109 || i == 113)
/*      */       {
/* 3341 */         this.log.warning(Lint.LintCategory.DIVZERO, paramDiagnosticPosition, "div.zero", new Object[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void checkEmptyIf(JCTree.JCIf paramJCIf) {
/* 3350 */     if (paramJCIf.thenpart.hasTag(JCTree.Tag.SKIP) && paramJCIf.elsepart == null && this.lint
/* 3351 */       .isEnabled(Lint.LintCategory.EMPTY)) {
/* 3352 */       this.log.warning(Lint.LintCategory.EMPTY, paramJCIf.thenpart.pos(), "empty.if", new Object[0]);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   boolean checkUnique(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope) {
/* 3361 */     if (paramSymbol.type.isErroneous())
/* 3362 */       return true;
/* 3363 */     if (paramSymbol.owner.name == this.names.any) return false;
/* 3364 */     for (Scope.Entry entry = paramScope.lookup(paramSymbol.name); entry.scope == paramScope; entry = entry.next()) {
/* 3365 */       if (paramSymbol != entry.sym && (entry.sym
/* 3366 */         .flags() & 0x40000000000L) == 0L && paramSymbol.kind == entry.sym.kind && paramSymbol.name != this.names.error && (paramSymbol.kind != 16 || this.types
/*      */
/*      */
/*      */
/* 3370 */         .hasSameArgs(paramSymbol.type, entry.sym.type) || this.types
/* 3371 */         .hasSameArgs(this.types.erasure(paramSymbol.type), this.types.erasure(entry.sym.type)))) {
/* 3372 */         if ((paramSymbol.flags() & 0x400000000L) != (entry.sym.flags() & 0x400000000L)) {
/* 3373 */           varargsDuplicateError(paramDiagnosticPosition, paramSymbol, entry.sym);
/* 3374 */           return true;
/* 3375 */         }  if (paramSymbol.kind == 16 && !this.types.hasSameArgs(paramSymbol.type, entry.sym.type, false)) {
/* 3376 */           duplicateErasureError(paramDiagnosticPosition, paramSymbol, entry.sym);
/* 3377 */           paramSymbol.flags_field |= 0x40000000000L;
/* 3378 */           return true;
/*      */         }
/* 3380 */         duplicateError(paramDiagnosticPosition, entry.sym);
/* 3381 */         return false;
/*      */       }
/*      */     }
/*      */
/* 3385 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void duplicateErasureError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol1, Symbol paramSymbol2) {
/* 3391 */     if (!paramSymbol1.type.isErroneous() && !paramSymbol2.type.isErroneous()) {
/* 3392 */       this.log.error(paramDiagnosticPosition, "name.clash.same.erasure", new Object[] { paramSymbol1, paramSymbol2 });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   boolean checkUniqueImport(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope) {
/* 3403 */     return checkUniqueImport(paramDiagnosticPosition, paramSymbol, paramScope, false);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   boolean checkUniqueStaticImport(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope) {
/* 3413 */     return checkUniqueImport(paramDiagnosticPosition, paramSymbol, paramScope, true);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean checkUniqueImport(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope, boolean paramBoolean) {
/* 3424 */     for (Scope.Entry entry = paramScope.lookup(paramSymbol.name); entry.scope != null; entry = entry.next()) {
/*      */
/* 3426 */       boolean bool = (entry.scope == paramScope) ? true : false;
/* 3427 */       if ((bool || paramSymbol != entry.sym) && paramSymbol.kind == entry.sym.kind && paramSymbol.name != this.names.error && (!paramBoolean ||
/*      */
/*      */
/* 3430 */         !entry.isStaticallyImported())) {
/* 3431 */         if (!entry.sym.type.isErroneous())
/* 3432 */           if (!bool) {
/* 3433 */             if (paramBoolean) {
/* 3434 */               this.log.error(paramDiagnosticPosition, "already.defined.static.single.import", new Object[] { entry.sym });
/*      */             } else {
/* 3436 */               this.log.error(paramDiagnosticPosition, "already.defined.single.import", new Object[] { entry.sym });
/*      */             }
/* 3438 */           } else if (paramSymbol != entry.sym) {
/* 3439 */             this.log.error(paramDiagnosticPosition, "already.defined.this.unit", new Object[] { entry.sym });
/*      */           }
/* 3441 */         return false;
/*      */       }
/*      */     }
/* 3444 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void checkCanonical(JCTree paramJCTree) {
/* 3450 */     if (!isCanonical(paramJCTree))
/* 3451 */       this.log.error(paramJCTree.pos(), "import.requires.canonical", new Object[] {
/* 3452 */             TreeInfo.symbol(paramJCTree) });
/*      */   }
/*      */
/*      */   private boolean isCanonical(JCTree paramJCTree) {
/* 3456 */     while (paramJCTree.hasTag(JCTree.Tag.SELECT)) {
/* 3457 */       JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)paramJCTree;
/* 3458 */       if (jCFieldAccess.sym.owner != TreeInfo.symbol((JCTree)jCFieldAccess.selected))
/* 3459 */         return false;
/* 3460 */       JCTree.JCExpression jCExpression = jCFieldAccess.selected;
/*      */     }
/* 3462 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */   void checkForBadAuxiliaryClassAccess(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Symbol.ClassSymbol paramClassSymbol) {
/* 3468 */     if (this.lint.isEnabled(Lint.LintCategory.AUXILIARYCLASS) && (paramClassSymbol
/* 3469 */       .flags() & 0x100000000000L) != 0L && this.rs
/* 3470 */       .isAccessible(paramEnv, (Symbol.TypeSymbol)paramClassSymbol) &&
/* 3471 */       !this.fileManager.isSameFile(paramClassSymbol.sourcefile, paramEnv.toplevel.sourcefile))
/*      */     {
/* 3473 */       this.log.warning(paramDiagnosticPosition, "auxiliary.class.accessed.from.outside.of.its.source.file", new Object[] { paramClassSymbol, paramClassSymbol.sourcefile });
/*      */     }
/*      */   }
/*      */
/*      */   private class ConversionWarner extends Warner {
/*      */     final String uncheckedKey;
/*      */     final Type found;
/*      */     final Type expected;
/*      */
/*      */     public ConversionWarner(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, String param1String, Type param1Type1, Type param1Type2) {
/* 3483 */       super(param1DiagnosticPosition);
/* 3484 */       this.uncheckedKey = param1String;
/* 3485 */       this.found = param1Type1;
/* 3486 */       this.expected = param1Type2;
/*      */     }
/*      */
/*      */
/*      */     public void warn(Lint.LintCategory param1LintCategory) {
/* 3491 */       boolean bool = this.warned;
/* 3492 */       super.warn(param1LintCategory);
/* 3493 */       if (bool)
/* 3494 */         return;  switch (param1LintCategory) {
/*      */         case UNCHECKED:
/* 3496 */           Check.this.warnUnchecked(pos(), "prob.found.req", new Object[] { Check.access$1100(this.this$0).fragment(this.uncheckedKey, new Object[0]), this.found, this.expected });
/*      */           return;
/*      */         case VARARGS:
/* 3499 */           if (Check.this.method != null && Check.this
/* 3500 */             .method.attribute((Symbol)Check.this.syms.trustMeType.tsym) != null && Check.this
/* 3501 */             .isTrustMeAllowedOnMethod((Symbol)Check.this.method) &&
/* 3502 */             !Check.this.types.isReifiable((Type)Check.this.method.type.getParameterTypes().last())) {
/* 3503 */             Check.this.warnUnsafeVararg(pos(), "varargs.unsafe.use.varargs.param", new Object[] { (Check.access$1200(this.this$0)).params.last() });
/*      */           }
/*      */           return;
/*      */       }
/* 3507 */       throw new AssertionError("Unexpected lint: " + param1LintCategory);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public Warner castWarner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) {
/* 3513 */     return new ConversionWarner(paramDiagnosticPosition, "unchecked.cast.to.type", paramType1, paramType2);
/*      */   }
/*      */
/*      */   public Warner convertWarner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) {
/* 3517 */     return new ConversionWarner(paramDiagnosticPosition, "unchecked.assign", paramType1, paramType2);
/*      */   }
/*      */
/*      */   public void checkFunctionalInterface(JCTree.JCClassDecl paramJCClassDecl, Symbol.ClassSymbol paramClassSymbol) {
/* 3521 */     Attribute.Compound compound = paramClassSymbol.attribute((Symbol)this.syms.functionalInterfaceType.tsym);
/*      */
/* 3523 */     if (compound != null)
/*      */       try {
/* 3525 */         this.types.findDescriptorSymbol((Symbol.TypeSymbol)paramClassSymbol);
/* 3526 */       } catch (Types.FunctionDescriptorLookupError functionDescriptorLookupError) {
/* 3527 */         JCDiagnostic.DiagnosticPosition diagnosticPosition = paramJCClassDecl.pos();
/* 3528 */         for (JCTree.JCAnnotation jCAnnotation : (paramJCClassDecl.getModifiers()).annotations) {
/* 3529 */           if (jCAnnotation.annotationType.type.tsym == this.syms.functionalInterfaceType.tsym) {
/* 3530 */             diagnosticPosition = jCAnnotation.pos();
/*      */             break;
/*      */           }
/*      */         }
/* 3534 */         this.log.error(diagnosticPosition, "bad.functional.intf.anno.1", new Object[] { functionDescriptorLookupError.getDiagnostic() });
/*      */       }
/*      */   }
/*      */
/*      */   public static interface CheckContext {
/*      */     boolean compatible(Type param1Type1, Type param1Type2, Warner param1Warner);
/*      */
/*      */     void report(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, JCDiagnostic param1JCDiagnostic);
/*      */
/*      */     Warner checkWarner(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Type param1Type1, Type param1Type2);
/*      */
/*      */     Infer.InferenceContext inferenceContext();
/*      */
/*      */     DeferredAttr.DeferredAttrContext deferredAttrContext();
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Check.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
