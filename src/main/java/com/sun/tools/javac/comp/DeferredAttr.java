/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.source.tree.LambdaExpressionTree;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeCopier;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class DeferredAttr
/*      */   extends JCTree.Visitor
/*      */ {
/*   65 */   protected static final Context.Key<DeferredAttr> deferredAttrKey = new Context.Key();
/*      */
/*      */   final Attr attr;
/*      */
/*      */   final Check chk;
/*      */
/*      */   final JCDiagnostic.Factory diags;
/*      */
/*      */   final Enter enter;
/*      */
/*      */   final Infer infer;
/*      */   final Resolve rs;
/*      */   final Log log;
/*      */   final Symtab syms;
/*      */   final TreeMaker make;
/*      */   final Types types;
/*      */
/*      */   public static DeferredAttr instance(Context paramContext) {
/*   83 */     DeferredAttr deferredAttr = (DeferredAttr)paramContext.get(deferredAttrKey);
/*   84 */     if (deferredAttr == null)
/*   85 */       deferredAttr = new DeferredAttr(paramContext);
/*   86 */     return deferredAttr;
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
/*      */   final Flow flow;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   final Names names;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   final TypeEnvs typeEnvs;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   final JCTree stuckTree;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   DeferredTypeCompleter basicCompleter;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   DeferredTypeCompleter dummyCompleter;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   DeferredStuckPolicy dummyStuckPolicy;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected UnenterScanner unenterScanner;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   final DeferredAttrContext emptyDeferredAttrContext;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private EnumSet<JCTree.Tag> deferredCheckerTags;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected DeferredAttr(Context paramContext) {
/*  273 */     this.basicCompleter = new DeferredTypeCompleter()
/*      */       {
/*      */         public Type complete(DeferredType param1DeferredType, Attr.ResultInfo param1ResultInfo, DeferredAttrContext param1DeferredAttrContext)
/*      */         {
/*      */           // Byte code:
/*      */           //   0: getstatic com/sun/tools/javac/comp/DeferredAttr$6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$AttrMode : [I
/*      */           //   3: aload_3
/*      */           //   4: getfield mode : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */           //   7: invokevirtual ordinal : ()I
/*      */           //   10: iaload
/*      */           //   11: lookupswitch default -> 130, 1 -> 36, 2 -> 95
/*      */           //   36: aload_1
/*      */           //   37: getfield mode : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */           //   40: ifnull -> 53
/*      */           //   43: aload_1
/*      */           //   44: getfield mode : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */           //   47: getstatic com/sun/tools/javac/comp/DeferredAttr$AttrMode.SPECULATIVE : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */           //   50: if_acmpne -> 57
/*      */           //   53: iconst_1
/*      */           //   54: goto -> 58
/*      */           //   57: iconst_0
/*      */           //   58: invokestatic check : (Z)V
/*      */           //   61: aload_0
/*      */           //   62: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */           //   65: aload_1
/*      */           //   66: getfield tree : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */           //   69: aload_1
/*      */           //   70: getfield env : Lcom/sun/tools/javac/comp/Env;
/*      */           //   73: aload_2
/*      */           //   74: invokevirtual attribSpeculative : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/comp/Env;Lcom/sun/tools/javac/comp/Attr$ResultInfo;)Lcom/sun/tools/javac/tree/JCTree;
/*      */           //   77: astore #4
/*      */           //   79: aload_1
/*      */           //   80: getfield speculativeCache : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType$SpeculativeCache;
/*      */           //   83: aload #4
/*      */           //   85: aload_2
/*      */           //   86: invokevirtual put : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/comp/Attr$ResultInfo;)V
/*      */           //   89: aload #4
/*      */           //   91: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */           //   94: areturn
/*      */           //   95: aload_1
/*      */           //   96: getfield mode : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */           //   99: ifnull -> 106
/*      */           //   102: iconst_1
/*      */           //   103: goto -> 107
/*      */           //   106: iconst_0
/*      */           //   107: invokestatic check : (Z)V
/*      */           //   110: aload_0
/*      */           //   111: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */           //   114: getfield attr : Lcom/sun/tools/javac/comp/Attr;
/*      */           //   117: aload_1
/*      */           //   118: getfield tree : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */           //   121: aload_1
/*      */           //   122: getfield env : Lcom/sun/tools/javac/comp/Env;
/*      */           //   125: aload_2
/*      */           //   126: invokevirtual attribTree : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/comp/Env;Lcom/sun/tools/javac/comp/Attr$ResultInfo;)Lcom/sun/tools/javac/code/Type;
/*      */           //   129: areturn
/*      */           //   130: invokestatic error : ()V
/*      */           //   133: aconst_null
/*      */           //   134: areturn
/*      */           // Line number table:
/*      */           //   Java source line number -> byte code offset
/*      */           //   #275	-> 0
/*      */           //   #279	-> 36
/*      */           //   #280	-> 61
/*      */           //   #281	-> 79
/*      */           //   #282	-> 89
/*      */           //   #284	-> 95
/*      */           //   #285	-> 110
/*      */           //   #287	-> 130
/*      */           //   #288	-> 133
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
/*  292 */     this.dummyCompleter = new DeferredTypeCompleter() {
/*      */         public Type complete(DeferredType param1DeferredType, Attr.ResultInfo param1ResultInfo, DeferredAttrContext param1DeferredAttrContext) {
/*  294 */           Assert.check((param1DeferredAttrContext.mode == AttrMode.CHECK));
/*  295 */           return param1DeferredType.tree.type = (Type)Type.stuckType;
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
/*  323 */     this.dummyStuckPolicy = new DeferredStuckPolicy()
/*      */       {
/*      */         public boolean isStuck() {
/*  326 */           return false;
/*      */         }
/*      */
/*      */         public Set<Type> stuckVars() {
/*  330 */           return Collections.emptySet();
/*      */         }
/*      */
/*      */         public Set<Type> depVars() {
/*  334 */           return Collections.emptySet();
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
/*  396 */     this.unenterScanner = new UnenterScanner();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1418 */     this
/* 1419 */       .deferredCheckerTags = EnumSet.of(JCTree.Tag.LAMBDA, new JCTree.Tag[] { JCTree.Tag.REFERENCE, JCTree.Tag.PARENS, JCTree.Tag.TYPECAST, JCTree.Tag.CONDEXPR, JCTree.Tag.NEWCLASS, JCTree.Tag.APPLY, JCTree.Tag.LITERAL });
/*      */     paramContext.put(deferredAttrKey, this);
/*      */     this.attr = Attr.instance(paramContext);
/*      */     this.chk = Check.instance(paramContext);
/*      */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*      */     this.enter = Enter.instance(paramContext);
/*      */     this.infer = Infer.instance(paramContext);
/*      */     this.rs = Resolve.instance(paramContext);
/*      */     this.log = Log.instance(paramContext);
/*      */     this.syms = Symtab.instance(paramContext);
/*      */     this.make = TreeMaker.instance(paramContext);
/*      */     this.types = Types.instance(paramContext);
/*      */     this.flow = Flow.instance(paramContext);
/*      */     this.names = Names.instance(paramContext);
/*      */     this.stuckTree = (JCTree)this.make.Ident(this.names.empty).setType((Type)Type.stuckType);
/*      */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*      */     this.emptyDeferredAttrContext = new DeferredAttrContext(AttrMode.CHECK, null, Resolve.MethodResolutionPhase.BOX, this.infer.emptyContext, null, null) {
/*      */         void addDeferredAttrNode(DeferredType param1DeferredType, Attr.ResultInfo param1ResultInfo, DeferredStuckPolicy param1DeferredStuckPolicy) {
/*      */           Assert.error("Empty deferred context!");
/*      */         }
/*      */
/*      */         void complete() {
/*      */           Assert.error("Empty deferred context!");
/*      */         }
/*      */
/*      */         public String toString() {
/*      */           return "Empty deferred context!";
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   public class DeferredType extends Type {
/*      */     public JCTree.JCExpression tree;
/*      */     Env<AttrContext> env;
/*      */     AttrMode mode;
/*      */     SpeculativeCache speculativeCache;
/*      */
/*      */     DeferredType(JCTree.JCExpression param1JCExpression, Env<AttrContext> param1Env) {
/*      */       super(null);
/*      */       this.tree = param1JCExpression;
/*      */       this.env = DeferredAttr.this.attr.copyEnv(param1Env);
/*      */       this.speculativeCache = new SpeculativeCache();
/*      */     }
/*      */
/*      */     public TypeTag getTag() {
/*      */       return TypeTag.DEFERRED;
/*      */     }
/*      */
/*      */     public String toString() {
/*      */       return "DeferredType";
/*      */     }
/*      */
/*      */     class SpeculativeCache {
/*      */       private Map<Symbol, List<Entry>> cache = new WeakHashMap<>();
/*      */
/*      */       class Entry {
/*      */         JCTree speculativeTree;
/*      */         Attr.ResultInfo resultInfo;
/*      */
/*      */         public Entry(JCTree param3JCTree, Attr.ResultInfo param3ResultInfo) {
/*      */           this.speculativeTree = param3JCTree;
/*      */           this.resultInfo = param3ResultInfo;
/*      */         }
/*      */
/*      */         boolean matches(Resolve.MethodResolutionPhase param3MethodResolutionPhase) {
/*      */           return ((this.resultInfo.checkContext.deferredAttrContext()).phase == param3MethodResolutionPhase);
/*      */         }
/*      */       }
/*      */
/*      */       Entry get(Symbol param2Symbol, Resolve.MethodResolutionPhase param2MethodResolutionPhase) {
/*      */         List list = this.cache.get(param2Symbol);
/*      */         if (list == null)
/*      */           return null;
/*      */         for (Entry entry : list) {
/*      */           if (entry.matches(param2MethodResolutionPhase))
/*      */             return entry;
/*      */         }
/*      */         return null;
/*      */       }
/*      */
/*      */       void put(JCTree param2JCTree, Attr.ResultInfo param2ResultInfo) {
/*      */         Symbol symbol = (param2ResultInfo.checkContext.deferredAttrContext()).msym;
/*      */         List list = this.cache.get(symbol);
/*      */         if (list == null)
/*      */           list = List.nil();
/*      */         this.cache.put(symbol, list.prepend(new Entry(param2JCTree, param2ResultInfo)));
/*      */       }
/*      */     }
/*      */
/*      */     class Entry {
/*      */       JCTree speculativeTree;
/*      */       Attr.ResultInfo resultInfo;
/*      */
/*      */       public Entry(JCTree param2JCTree, Attr.ResultInfo param2ResultInfo) {
/*      */         this.speculativeTree = param2JCTree;
/*      */         this.resultInfo = param2ResultInfo;
/*      */       }
/*      */
/*      */       boolean matches(Resolve.MethodResolutionPhase param2MethodResolutionPhase) {
/*      */         return ((this.resultInfo.checkContext.deferredAttrContext()).phase == param2MethodResolutionPhase);
/*      */       }
/*      */     }
/*      */
/*      */     Type speculativeType(Symbol param1Symbol, Resolve.MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       SpeculativeCache.Entry entry = this.speculativeCache.get(param1Symbol, param1MethodResolutionPhase);
/*      */       return (entry != null) ? entry.speculativeTree.type : (Type)Type.noType;
/*      */     }
/*      */
/*      */     Type check(Attr.ResultInfo param1ResultInfo) {
/*      */       DeferredStuckPolicy deferredStuckPolicy;
/*      */       if (param1ResultInfo.pt.hasTag(TypeTag.NONE) || param1ResultInfo.pt.isErroneous()) {
/*      */         deferredStuckPolicy = DeferredAttr.this.dummyStuckPolicy;
/*      */       } else if ((param1ResultInfo.checkContext.deferredAttrContext()).mode == AttrMode.SPECULATIVE || param1ResultInfo.checkContext.deferredAttrContext().insideOverloadPhase()) {
/*      */         deferredStuckPolicy = new OverloadStuckPolicy(param1ResultInfo, this);
/*      */       } else {
/*      */         deferredStuckPolicy = new CheckStuckPolicy(param1ResultInfo, this);
/*      */       }
/*      */       return check(param1ResultInfo, deferredStuckPolicy, DeferredAttr.this.basicCompleter);
/*      */     }
/*      */
/*      */     private Type check(Attr.ResultInfo param1ResultInfo, DeferredStuckPolicy param1DeferredStuckPolicy, DeferredTypeCompleter param1DeferredTypeCompleter) {
/*      */       DeferredAttrContext deferredAttrContext = param1ResultInfo.checkContext.deferredAttrContext();
/*      */       Assert.check((deferredAttrContext != DeferredAttr.this.emptyDeferredAttrContext));
/*      */       if (param1DeferredStuckPolicy.isStuck()) {
/*      */         deferredAttrContext.addDeferredAttrNode(this, param1ResultInfo, param1DeferredStuckPolicy);
/*      */         return (Type)Type.noType;
/*      */       }
/*      */       try {
/*      */         return param1DeferredTypeCompleter.complete(this, param1ResultInfo, deferredAttrContext);
/*      */       } finally {
/*      */         this.mode = deferredAttrContext.mode;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   public enum AttrMode {
/*      */     SPECULATIVE, CHECK;
/*      */   }
/*      */
/*      */   JCTree attribSpeculative(JCTree paramJCTree, Env<AttrContext> paramEnv, Attr.ResultInfo paramResultInfo) {
/*      */     final JCTree newTree = (new TreeCopier(this.make)).copy(paramJCTree);
/*      */     Env<AttrContext> env = paramEnv.dup(jCTree, ((AttrContext)paramEnv.info).dup(((AttrContext)paramEnv.info).scope.dupUnshared()));
/*      */     ((AttrContext)env.info).scope.owner = ((AttrContext)paramEnv.info).scope.owner;
/*      */     Log.DeferredDiagnosticHandler deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log, new Filter<JCDiagnostic>() {
/*      */           public boolean accepts(final JCDiagnostic d) {
/*      */             class PosScanner extends TreeScanner {
/*      */               boolean found = false;
/*      */
/*      */               public void scan(JCTree param2JCTree) {
/*      */                 if (param2JCTree != null && param2JCTree.pos() == d.getDiagnosticPosition())
/*      */                   this.found = true;
/*      */                 super.scan(param2JCTree);
/*      */               }
/*      */             };
/*      */             PosScanner posScanner = new PosScanner();
/*      */             posScanner.scan(newTree);
/*      */             return posScanner.found;
/*      */           }
/*      */         });
/*      */     try {
/*      */       this.attr.attribTree(jCTree, env, paramResultInfo);
/*      */       this.unenterScanner.scan(jCTree);
/*      */       return jCTree;
/*      */     } finally {
/*      */       this.unenterScanner.scan(jCTree);
/*      */       this.log.popDiagnosticHandler((Log.DiagnosticHandler)deferredDiagnosticHandler);
/*      */     }
/*      */   }
/*      */
/*      */   class UnenterScanner extends TreeScanner {
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/*      */       Symbol.ClassSymbol classSymbol = param1JCClassDecl.sym;
/*      */       if (classSymbol == null)
/*      */         return;
/*      */       DeferredAttr.this.typeEnvs.remove((Symbol.TypeSymbol)classSymbol);
/*      */       DeferredAttr.this.chk.compiled.remove(classSymbol.flatname);
/*      */       DeferredAttr.this.syms.classes.remove(classSymbol.flatname);
/*      */       super.visitClassDef(param1JCClassDecl);
/*      */     }
/*      */   }
/*      */
/*      */   class DeferredAttrContext {
/*      */     final AttrMode mode;
/*      */     final Symbol msym;
/*      */     final Resolve.MethodResolutionPhase phase;
/*      */     final Infer.InferenceContext inferenceContext;
/*      */     final DeferredAttrContext parent;
/*      */     final Warner warn;
/*      */     ArrayList<DeferredAttrNode> deferredAttrNodes = new ArrayList<>();
/*      */
/*      */     DeferredAttrContext(AttrMode param1AttrMode, Symbol param1Symbol, Resolve.MethodResolutionPhase param1MethodResolutionPhase, Infer.InferenceContext param1InferenceContext, DeferredAttrContext param1DeferredAttrContext, Warner param1Warner) {
/*      */       this.mode = param1AttrMode;
/*      */       this.msym = param1Symbol;
/*      */       this.phase = param1MethodResolutionPhase;
/*      */       this.parent = param1DeferredAttrContext;
/*      */       this.warn = param1Warner;
/*      */       this.inferenceContext = param1InferenceContext;
/*      */     }
/*      */
/*      */     void addDeferredAttrNode(DeferredType param1DeferredType, Attr.ResultInfo param1ResultInfo, DeferredStuckPolicy param1DeferredStuckPolicy) {
/*      */       this.deferredAttrNodes.add(new DeferredAttrNode(param1DeferredType, param1ResultInfo, param1DeferredStuckPolicy));
/*      */     }
/*      */
/*      */     void complete() {
/*      */       while (!this.deferredAttrNodes.isEmpty()) {
/*      */         LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */         List<Type> list = List.nil();
/*      */         boolean bool = false;
/*      */         for (DeferredAttrNode deferredAttrNode : List.from(this.deferredAttrNodes)) {
/*      */           if (!deferredAttrNode.process(this)) {
/*      */             List list1 = List.from(deferredAttrNode.deferredStuckPolicy.stuckVars()).intersect(this.inferenceContext.restvars());
/*      */             list = list.prependList(list1);
/*      */             for (Type type : List.from(deferredAttrNode.deferredStuckPolicy.depVars()).intersect(this.inferenceContext.restvars())) {
/*      */               Set set = (Set)linkedHashMap.get(type);
/*      */               if (set == null) {
/*      */                 set = new LinkedHashSet();
/*      */                 linkedHashMap.put(type, set);
/*      */               }
/*      */               set.addAll((Collection)list1);
/*      */             }
/*      */             continue;
/*      */           }
/*      */           this.deferredAttrNodes.remove(deferredAttrNode);
/*      */           bool = true;
/*      */         }
/*      */         if (!bool) {
/*      */           if (insideOverloadPhase()) {
/*      */             for (DeferredAttrNode deferredAttrNode : this.deferredAttrNodes)
/*      */               deferredAttrNode.dt.tree.type = (Type)Type.noType;
/*      */             return;
/*      */           }
/*      */           try {
/*      */             this.inferenceContext.solveAny(list, (Map)linkedHashMap, this.warn);
/*      */             this.inferenceContext.notifyChange();
/*      */           } catch (NodeNotFoundException nodeNotFoundException) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     private boolean insideOverloadPhase() {
/*      */       DeferredAttrContext deferredAttrContext = this;
/*      */       if (deferredAttrContext == DeferredAttr.this.emptyDeferredAttrContext)
/*      */         return false;
/*      */       if (deferredAttrContext.mode == AttrMode.SPECULATIVE)
/*      */         return true;
/*      */       return deferredAttrContext.parent.insideOverloadPhase();
/*      */     }
/*      */   }
/*      */
/*      */   class DeferredAttrNode {
/*      */     DeferredType dt;
/*      */     Attr.ResultInfo resultInfo;
/*      */     DeferredStuckPolicy deferredStuckPolicy;
/*      */
/*      */     DeferredAttrNode(DeferredType param1DeferredType, Attr.ResultInfo param1ResultInfo, DeferredStuckPolicy param1DeferredStuckPolicy) {
/*      */       this.dt = param1DeferredType;
/*      */       this.resultInfo = param1ResultInfo;
/*      */       this.deferredStuckPolicy = param1DeferredStuckPolicy;
/*      */     }
/*      */
/*      */     boolean process(DeferredAttrContext param1DeferredAttrContext) {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/comp/DeferredAttr$6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$AttrMode : [I
/*      */       //   3: aload_1
/*      */       //   4: getfield mode : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: lookupswitch default -> 255, 1 -> 36, 2 -> 82
/*      */       //   36: aload_0
/*      */       //   37: getfield deferredStuckPolicy : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */       //   40: invokeinterface isStuck : ()Z
/*      */       //   45: ifeq -> 77
/*      */       //   48: aload_0
/*      */       //   49: getfield dt : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;
/*      */       //   52: aload_0
/*      */       //   53: getfield resultInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   56: aload_0
/*      */       //   57: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   60: getfield dummyStuckPolicy : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */       //   63: new com/sun/tools/javac/comp/DeferredAttr$DeferredAttrNode$StructuralStuckChecker
/*      */       //   66: dup
/*      */       //   67: aload_0
/*      */       //   68: invokespecial <init> : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrNode;)V
/*      */       //   71: invokestatic access$100 : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;Lcom/sun/tools/javac/comp/Attr$ResultInfo;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredTypeCompleter;)Lcom/sun/tools/javac/code/Type;
/*      */       //   74: pop
/*      */       //   75: iconst_1
/*      */       //   76: ireturn
/*      */       //   77: ldc 'Cannot get here'
/*      */       //   79: invokestatic error : (Ljava/lang/String;)V
/*      */       //   82: aload_0
/*      */       //   83: getfield deferredStuckPolicy : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */       //   86: invokeinterface isStuck : ()Z
/*      */       //   91: ifeq -> 191
/*      */       //   94: aload_1
/*      */       //   95: getfield parent : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   98: aload_0
/*      */       //   99: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   102: getfield emptyDeferredAttrContext : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   105: if_acmpeq -> 189
/*      */       //   108: aload_1
/*      */       //   109: getfield parent : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   112: getfield inferenceContext : Lcom/sun/tools/javac/comp/Infer$InferenceContext;
/*      */       //   115: getfield inferencevars : Lcom/sun/tools/javac/util/List;
/*      */       //   118: aload_0
/*      */       //   119: getfield deferredStuckPolicy : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */       //   122: invokeinterface stuckVars : ()Ljava/util/Set;
/*      */       //   127: invokestatic from : (Ljava/lang/Iterable;)Lcom/sun/tools/javac/util/List;
/*      */       //   130: invokestatic containsAny : (Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/util/List;)Z
/*      */       //   133: ifeq -> 189
/*      */       //   136: aload_1
/*      */       //   137: getfield parent : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   140: aload_0
/*      */       //   141: getfield dt : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;
/*      */       //   144: aload_0
/*      */       //   145: getfield resultInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   148: new com/sun/tools/javac/comp/DeferredAttr$DeferredAttrNode$1
/*      */       //   151: dup
/*      */       //   152: aload_0
/*      */       //   153: aload_0
/*      */       //   154: getfield resultInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   157: getfield checkContext : Lcom/sun/tools/javac/comp/Check$CheckContext;
/*      */       //   160: aload_1
/*      */       //   161: invokespecial <init> : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrNode;Lcom/sun/tools/javac/comp/Check$CheckContext;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;)V
/*      */       //   164: invokevirtual dup : (Lcom/sun/tools/javac/comp/Check$CheckContext;)Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   167: aload_0
/*      */       //   168: getfield deferredStuckPolicy : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */       //   171: invokevirtual addDeferredAttrNode : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;Lcom/sun/tools/javac/comp/Attr$ResultInfo;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;)V
/*      */       //   174: aload_0
/*      */       //   175: getfield dt : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;
/*      */       //   178: getfield tree : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   181: getstatic com/sun/tools/javac/code/Type.stuckType : Lcom/sun/tools/javac/code/Type$JCNoType;
/*      */       //   184: putfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   187: iconst_1
/*      */       //   188: ireturn
/*      */       //   189: iconst_0
/*      */       //   190: ireturn
/*      */       //   191: aload_1
/*      */       //   192: invokestatic access$000 : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;)Z
/*      */       //   195: ifne -> 202
/*      */       //   198: iconst_1
/*      */       //   199: goto -> 203
/*      */       //   202: iconst_0
/*      */       //   203: ldc 'attribution shouldn't be happening here'
/*      */       //   205: invokestatic check : (ZLjava/lang/String;)V
/*      */       //   208: aload_0
/*      */       //   209: getfield resultInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   212: aload_1
/*      */       //   213: getfield inferenceContext : Lcom/sun/tools/javac/comp/Infer$InferenceContext;
/*      */       //   216: aload_0
/*      */       //   217: getfield resultInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   220: getfield pt : Lcom/sun/tools/javac/code/Type;
/*      */       //   223: invokevirtual asInstType : (Lcom/sun/tools/javac/code/Type;)Lcom/sun/tools/javac/code/Type;
/*      */       //   226: invokevirtual dup : (Lcom/sun/tools/javac/code/Type;)Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   229: astore_2
/*      */       //   230: aload_0
/*      */       //   231: getfield dt : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;
/*      */       //   234: aload_2
/*      */       //   235: aload_0
/*      */       //   236: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   239: getfield dummyStuckPolicy : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */       //   242: aload_0
/*      */       //   243: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   246: getfield basicCompleter : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredTypeCompleter;
/*      */       //   249: invokestatic access$100 : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredType;Lcom/sun/tools/javac/comp/Attr$ResultInfo;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredTypeCompleter;)Lcom/sun/tools/javac/code/Type;
/*      */       //   252: pop
/*      */       //   253: iconst_1
/*      */       //   254: ireturn
/*      */       //   255: new java/lang/AssertionError
/*      */       //   258: dup
/*      */       //   259: ldc 'Bad mode'
/*      */       //   261: invokespecial <init> : (Ljava/lang/Object;)V
/*      */       //   264: athrow
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #561	-> 0
/*      */       //   #563	-> 36
/*      */       //   #564	-> 48
/*      */       //   #565	-> 75
/*      */       //   #567	-> 77
/*      */       //   #570	-> 82
/*      */       //   #572	-> 94
/*      */       //   #574	-> 122
/*      */       //   #573	-> 130
/*      */       //   #575	-> 136
/*      */       //   #576	-> 164
/*      */       //   #575	-> 171
/*      */       //   #586	-> 174
/*      */       //   #587	-> 187
/*      */       //   #589	-> 189
/*      */       //   #592	-> 191
/*      */       //   #594	-> 208
/*      */       //   #595	-> 223
/*      */       //   #596	-> 230
/*      */       //   #597	-> 253
/*      */       //   #600	-> 255
/*      */     }
/*      */
/*      */     class StructuralStuckChecker extends TreeScanner implements DeferredTypeCompleter {
/*      */       Attr.ResultInfo resultInfo;
/*      */       Infer.InferenceContext inferenceContext;
/*      */       Env<AttrContext> env;
/*      */
/*      */       public Type complete(DeferredType param2DeferredType, Attr.ResultInfo param2ResultInfo, DeferredAttrContext param2DeferredAttrContext) {
/*      */         this.resultInfo = param2ResultInfo;
/*      */         this.inferenceContext = param2DeferredAttrContext.inferenceContext;
/*      */         this.env = param2DeferredType.env;
/*      */         param2DeferredType.tree.accept((JCTree.Visitor)this);
/*      */         param2DeferredType.speculativeCache.put(DeferredAttr.this.stuckTree, param2ResultInfo);
/*      */         return (Type)Type.noType;
/*      */       }
/*      */
/*      */       public void visitLambda(JCTree.JCLambda param2JCLambda) {
/*      */         Check.CheckContext checkContext = this.resultInfo.checkContext;
/*      */         Type type = this.resultInfo.pt;
/*      */         if (!this.inferenceContext.inferencevars.contains(type)) {
/*      */           Type type1 = null;
/*      */           try {
/*      */             type1 = DeferredAttr.this.types.findDescriptorType(type);
/*      */           } catch (Types.FunctionDescriptorLookupError functionDescriptorLookupError) {
/*      */             checkContext.report(null, functionDescriptorLookupError.getDiagnostic());
/*      */           }
/*      */           if (type1.getParameterTypes().length() != param2JCLambda.params.length())
/*      */             checkContext.report((JCDiagnostic.DiagnosticPosition)param2JCLambda, DeferredAttr.this.diags.fragment("incompatible.arg.types.in.lambda", new Object[0]));
/*      */           Type type2 = type1.getReturnType();
/*      */           boolean bool = type2.hasTag(TypeTag.VOID);
/*      */           if (param2JCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION) {
/*      */             boolean bool1 = (!bool || TreeInfo.isExpressionStatement((JCTree.JCExpression)param2JCLambda.getBody())) ? true : false;
/*      */             if (!bool1)
/*      */               this.resultInfo.checkContext.report(param2JCLambda.pos(), DeferredAttr.this.diags.fragment("incompatible.ret.type.in.lambda", new Object[] { this.this$1.this$0.diags.fragment("missing.ret.val", new Object[] { type2 }) }));
/*      */           } else {
/*      */             LambdaBodyStructChecker lambdaBodyStructChecker = new LambdaBodyStructChecker();
/*      */             param2JCLambda.body.accept((JCTree.Visitor)lambdaBodyStructChecker);
/*      */             boolean bool1 = lambdaBodyStructChecker.isVoidCompatible;
/*      */             if (bool) {
/*      */               if (!bool1)
/*      */                 this.resultInfo.checkContext.report(param2JCLambda.pos(), DeferredAttr.this.diags.fragment("unexpected.ret.val", new Object[0]));
/*      */             } else {
/*      */               boolean bool2 = (lambdaBodyStructChecker.isPotentiallyValueCompatible && !canLambdaBodyCompleteNormally(param2JCLambda)) ? true : false;
/*      */               if (!bool2 && !bool1)
/*      */                 DeferredAttr.this.log.error(param2JCLambda.body.pos(), "lambda.body.neither.value.nor.void.compatible", new Object[0]);
/*      */               if (!bool2)
/*      */                 this.resultInfo.checkContext.report(param2JCLambda.pos(), DeferredAttr.this.diags.fragment("incompatible.ret.type.in.lambda", new Object[] { this.this$1.this$0.diags.fragment("missing.ret.val", new Object[] { type2 }) }));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */       boolean canLambdaBodyCompleteNormally(JCTree.JCLambda param2JCLambda) {
/*      */         JCTree.JCLambda jCLambda = (JCTree.JCLambda)(new TreeCopier(DeferredAttr.this.make)).copy((JCTree)param2JCLambda);
/*      */         Env<AttrContext> env = DeferredAttr.this.attr.lambdaEnv(jCLambda, this.env);
/*      */         try {
/*      */           List list = jCLambda.params;
/*      */           while (list.nonEmpty()) {
/*      */             ((JCTree.JCVariableDecl)list.head).vartype = DeferredAttr.this.make.at((JCDiagnostic.DiagnosticPosition)list.head).Type(DeferredAttr.this.syms.errType);
/*      */             list = list.tail;
/*      */           }
/*      */           DeferredAttr.this.attr.attribStats(jCLambda.params, env);
/*      */           DeferredAttr.this.attr.getClass();
/*      */           Attr.ResultInfo resultInfo = new Attr.ResultInfo(DeferredAttr.this.attr, 12, (Type)Type.noType);
/*      */           ((AttrContext)env.info).returnResult = resultInfo;
/*      */           Log.DiscardDiagnosticHandler discardDiagnosticHandler = new Log.DiscardDiagnosticHandler(DeferredAttr.this.log);
/*      */           try {
/*      */             JCTree.JCBlock jCBlock1 = (JCTree.JCBlock)jCLambda.body;
/*      */             DeferredAttr.this.attr.attribStats(jCBlock1.stats, env);
/*      */             DeferredAttr.this.attr.preFlow(jCLambda);
/*      */             DeferredAttr.this.flow.analyzeLambda(env, jCLambda, DeferredAttr.this.make, true);
/*      */           } finally {
/*      */             DeferredAttr.this.log.popDiagnosticHandler((Log.DiagnosticHandler)discardDiagnosticHandler);
/*      */           }
/*      */           return jCLambda.canCompleteNormally;
/*      */         } finally {
/*      */           JCTree.JCBlock jCBlock = (JCTree.JCBlock)jCLambda.body;
/*      */           DeferredAttr.this.unenterScanner.scan(jCBlock.stats);
/*      */           ((AttrContext)env.info).scope.leave();
/*      */         }
/*      */       }
/*      */
/*      */       public void visitNewClass(JCTree.JCNewClass param2JCNewClass) {}
/*      */
/*      */       public void visitApply(JCTree.JCMethodInvocation param2JCMethodInvocation) {}
/*      */
/*      */       public void visitReference(JCTree.JCMemberReference param2JCMemberReference) {
/*      */         Check.CheckContext checkContext = this.resultInfo.checkContext;
/*      */         Type type = this.resultInfo.pt;
/*      */         if (!this.inferenceContext.inferencevars.contains(type)) {
/*      */           try {
/*      */             DeferredAttr.this.types.findDescriptorType(type);
/*      */           } catch (Types.FunctionDescriptorLookupError functionDescriptorLookupError) {
/*      */             checkContext.report(null, functionDescriptorLookupError.getDiagnostic());
/*      */           }
/*      */           Env<AttrContext> env = this.env.dup((JCTree)param2JCMemberReference);
/*      */           JCTree.JCExpression jCExpression = (JCTree.JCExpression)DeferredAttr.this.attribSpeculative((JCTree)param2JCMemberReference.getQualifierExpression(), env, DeferredAttr.this.attr.memberReferenceQualifierResult(param2JCMemberReference));
/*      */           ListBuffer listBuffer = new ListBuffer();
/*      */           for (Type type1 : DeferredAttr.this.types.findDescriptorType(type).getParameterTypes())
/*      */             listBuffer.append(Type.noType);
/*      */           JCTree.JCMemberReference jCMemberReference = (JCTree.JCMemberReference)(new TreeCopier(DeferredAttr.this.make)).copy((JCTree)param2JCMemberReference);
/*      */           jCMemberReference.expr = jCExpression;
/*      */           Symbol symbol = DeferredAttr.this.rs.resolveMemberReferenceByArity(env, jCMemberReference, jCExpression.type, param2JCMemberReference.name, listBuffer.toList(), this.inferenceContext);
/*      */           switch (symbol.kind) {
/*      */             case 134:
/*      */             case 135:
/*      */             case 136:
/*      */             case 138:
/*      */               checkContext.report((JCDiagnostic.DiagnosticPosition)param2JCMemberReference, DeferredAttr.this.diags.fragment("incompatible.arg.types.in.mref", new Object[0]));
/*      */               break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     class LambdaBodyStructChecker extends TreeScanner {
/*      */       boolean isVoidCompatible;
/*      */       boolean isPotentiallyValueCompatible;
/*      */
/*      */       LambdaBodyStructChecker() {
/*      */         this.isVoidCompatible = true;
/*      */         this.isPotentiallyValueCompatible = true;
/*      */       }
/*      */
/*      */       public void visitClassDef(JCTree.JCClassDecl param2JCClassDecl) {}
/*      */
/*      */       public void visitLambda(JCTree.JCLambda param2JCLambda) {}
/*      */
/*      */       public void visitNewClass(JCTree.JCNewClass param2JCNewClass) {}
/*      */
/*      */       public void visitReturn(JCTree.JCReturn param2JCReturn) {
/*      */         if (param2JCReturn.expr != null) {
/*      */           this.isVoidCompatible = false;
/*      */         } else {
/*      */           this.isPotentiallyValueCompatible = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class DeferredTypeMap extends Type.Mapping {
/*      */     DeferredAttrContext deferredAttrContext;
/*      */
/*      */     protected DeferredTypeMap(AttrMode param1AttrMode, Symbol param1Symbol, Resolve.MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(String.format("deferredTypeMap[%s]", new Object[] { param1AttrMode }));
/*      */       this.deferredAttrContext = new DeferredAttrContext(param1AttrMode, param1Symbol, param1MethodResolutionPhase, DeferredAttr.this.infer.emptyContext, DeferredAttr.this.emptyDeferredAttrContext, DeferredAttr.this.types.noWarnings);
/*      */     }
/*      */
/*      */     public Type apply(Type param1Type) {
/*      */       if (!param1Type.hasTag(TypeTag.DEFERRED))
/*      */         return param1Type.map(this);
/*      */       DeferredType deferredType = (DeferredType)param1Type;
/*      */       return typeOf(deferredType);
/*      */     }
/*      */
/*      */     protected Type typeOf(DeferredType param1DeferredType) {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/comp/DeferredAttr$6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$AttrMode : [I
/*      */       //   3: aload_0
/*      */       //   4: getfield deferredAttrContext : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   7: getfield mode : Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */       //   10: invokevirtual ordinal : ()I
/*      */       //   13: iaload
/*      */       //   14: lookupswitch default -> 83, 1 -> 64, 2 -> 40
/*      */       //   40: aload_1
/*      */       //   41: getfield tree : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   44: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   47: ifnonnull -> 56
/*      */       //   50: getstatic com/sun/tools/javac/code/Type.noType : Lcom/sun/tools/javac/code/Type$JCNoType;
/*      */       //   53: goto -> 63
/*      */       //   56: aload_1
/*      */       //   57: getfield tree : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   60: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   63: areturn
/*      */       //   64: aload_1
/*      */       //   65: aload_0
/*      */       //   66: getfield deferredAttrContext : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   69: getfield msym : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   72: aload_0
/*      */       //   73: getfield deferredAttrContext : Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */       //   76: getfield phase : Lcom/sun/tools/javac/comp/Resolve$MethodResolutionPhase;
/*      */       //   79: invokevirtual speculativeType : (Lcom/sun/tools/javac/code/Symbol;Lcom/sun/tools/javac/comp/Resolve$MethodResolutionPhase;)Lcom/sun/tools/javac/code/Type;
/*      */       //   82: areturn
/*      */       //   83: invokestatic error : ()V
/*      */       //   86: aconst_null
/*      */       //   87: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #844	-> 0
/*      */       //   #846	-> 40
/*      */       //   #848	-> 64
/*      */       //   #850	-> 83
/*      */       //   #851	-> 86
/*      */     }
/*      */   }
/*      */
/*      */   public class RecoveryDeferredTypeMap extends DeferredTypeMap {
/*      */     public RecoveryDeferredTypeMap(AttrMode param1AttrMode, Symbol param1Symbol, Resolve.MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(param1AttrMode, param1Symbol, (param1MethodResolutionPhase != null) ? param1MethodResolutionPhase : Resolve.MethodResolutionPhase.BOX);
/*      */     }
/*      */
/*      */     protected Type typeOf(DeferredType param1DeferredType) {
/*      */       Type type = super.typeOf(param1DeferredType);
/*      */       return (type == Type.noType) ? recover(param1DeferredType) : type;
/*      */     }
/*      */
/*      */     private Type recover(DeferredType param1DeferredType) {
/*      */       DeferredAttr.this.attr.getClass();
/*      */       param1DeferredType.check(new Attr.RecoveryInfo(DeferredAttr.this.attr, this.deferredAttrContext) {
/*      */             protected Type check(JCDiagnostic.DiagnosticPosition param2DiagnosticPosition, Type param2Type) {
/*      */               return DeferredAttr.this.chk.checkNonVoid(param2DiagnosticPosition, super.check(param2DiagnosticPosition, param2Type));
/*      */             }
/*      */           });
/*      */       return super.apply(param1DeferredType);
/*      */     }
/*      */   }
/*      */
/*      */   static abstract class FilterScanner extends TreeScanner {
/*      */     final Filter<JCTree> treeFilter = new Filter<JCTree>() {
/*      */         public boolean accepts(JCTree param2JCTree) {
/*      */           return validTags.contains(param2JCTree.getTag());
/*      */         }
/*      */       };
/*      */
/*      */     FilterScanner(final Set<JCTree.Tag> validTags) {}
/*      */
/*      */     public void scan(JCTree param1JCTree) {
/*      */       if (param1JCTree != null)
/*      */         if (this.treeFilter.accepts(param1JCTree)) {
/*      */           super.scan(param1JCTree);
/*      */         } else {
/*      */           skip(param1JCTree);
/*      */         }
/*      */     }
/*      */
/*      */     void skip(JCTree param1JCTree) {}
/*      */   }
/*      */
/*      */   static class PolyScanner extends FilterScanner {
/*      */     PolyScanner() {
/*      */       super(EnumSet.of(JCTree.Tag.CONDEXPR, JCTree.Tag.PARENS, JCTree.Tag.LAMBDA, JCTree.Tag.REFERENCE));
/*      */     }
/*      */   }
/*      */
/*      */   static class LambdaReturnScanner extends FilterScanner {
/*      */     LambdaReturnScanner() {
/*      */       super(EnumSet.of(JCTree.Tag.BLOCK, new JCTree.Tag[] {
/*      */               JCTree.Tag.CASE, JCTree.Tag.CATCH, JCTree.Tag.DOLOOP, JCTree.Tag.FOREACHLOOP, JCTree.Tag.FORLOOP, JCTree.Tag.IF, JCTree.Tag.RETURN, JCTree.Tag.SYNCHRONIZED, JCTree.Tag.SWITCH, JCTree.Tag.TRY,
/*      */               JCTree.Tag.WHILELOOP }));
/*      */     }
/*      */   }
/*      */
/*      */   class CheckStuckPolicy extends PolyScanner implements DeferredStuckPolicy, Infer.FreeTypeListener {
/*      */     Type pt;
/*      */     Infer.InferenceContext inferenceContext;
/*      */     Set<Type> stuckVars = new LinkedHashSet<>();
/*      */     Set<Type> depVars = new LinkedHashSet<>();
/*      */
/*      */     public boolean isStuck() {
/*      */       return !this.stuckVars.isEmpty();
/*      */     }
/*      */
/*      */     public Set<Type> stuckVars() {
/*      */       return this.stuckVars;
/*      */     }
/*      */
/*      */     public Set<Type> depVars() {
/*      */       return this.depVars;
/*      */     }
/*      */
/*      */     public CheckStuckPolicy(Attr.ResultInfo param1ResultInfo, DeferredType param1DeferredType) {
/*      */       this.pt = param1ResultInfo.pt;
/*      */       this.inferenceContext = param1ResultInfo.checkContext.inferenceContext();
/*      */       scan((JCTree)param1DeferredType.tree);
/*      */       if (!this.stuckVars.isEmpty())
/*      */         param1ResultInfo.checkContext.inferenceContext().addFreeTypeListener(List.from(this.stuckVars), this);
/*      */     }
/*      */
/*      */     public void typesInferred(Infer.InferenceContext param1InferenceContext) {
/*      */       this.stuckVars.clear();
/*      */     }
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/*      */       if (this.inferenceContext.inferenceVars().contains(this.pt))
/*      */         this.stuckVars.add(this.pt);
/*      */       if (!DeferredAttr.this.types.isFunctionalInterface(this.pt))
/*      */         return;
/*      */       Type type = DeferredAttr.this.types.findDescriptorType(this.pt);
/*      */       List<Type> list = this.inferenceContext.freeVarsIn(type.getParameterTypes());
/*      */       if (param1JCLambda.paramKind == JCTree.JCLambda.ParameterKind.IMPLICIT && list.nonEmpty()) {
/*      */         this.stuckVars.addAll((Collection<? extends Type>)list);
/*      */         this.depVars.addAll((Collection<? extends Type>)this.inferenceContext.freeVarsIn(type.getReturnType()));
/*      */       }
/*      */       scanLambdaBody(param1JCLambda, type.getReturnType());
/*      */     }
/*      */
/*      */     public void visitReference(JCTree.JCMemberReference param1JCMemberReference) {
/*      */       scan((JCTree)param1JCMemberReference.expr);
/*      */       if (this.inferenceContext.inferenceVars().contains(this.pt)) {
/*      */         this.stuckVars.add(this.pt);
/*      */         return;
/*      */       }
/*      */       if (!DeferredAttr.this.types.isFunctionalInterface(this.pt))
/*      */         return;
/*      */       Type type = DeferredAttr.this.types.findDescriptorType(this.pt);
/*      */       List<Type> list = this.inferenceContext.freeVarsIn(type.getParameterTypes());
/*      */       if (list.nonEmpty() && param1JCMemberReference.overloadKind == JCTree.JCMemberReference.OverloadKind.OVERLOADED) {
/*      */         this.stuckVars.addAll((Collection<? extends Type>)list);
/*      */         this.depVars.addAll((Collection<? extends Type>)this.inferenceContext.freeVarsIn(type.getReturnType()));
/*      */       }
/*      */     }
/*      */
/*      */     void scanLambdaBody(JCTree.JCLambda param1JCLambda, final Type pt) {
/*      */       if (param1JCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION) {
/*      */         Type type = this.pt;
/*      */         try {
/*      */           this.pt = pt;
/*      */           scan(param1JCLambda.body);
/*      */         } finally {
/*      */           this.pt = type;
/*      */         }
/*      */       } else {
/*      */         LambdaReturnScanner lambdaReturnScanner = new LambdaReturnScanner() {
/*      */             public void visitReturn(JCTree.JCReturn param2JCReturn) {
/*      */               if (param2JCReturn.expr != null) {
/*      */                 Type type = CheckStuckPolicy.this.pt;
/*      */                 try {
/*      */                   CheckStuckPolicy.this.pt = pt;
/*      */                   CheckStuckPolicy.this.scan((JCTree)param2JCReturn.expr);
/*      */                 } finally {
/*      */                   CheckStuckPolicy.this.pt = type;
/*      */                 }
/*      */               }
/*      */             }
/*      */           };
/*      */         lambdaReturnScanner.scan(param1JCLambda.body);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class OverloadStuckPolicy extends CheckStuckPolicy implements DeferredStuckPolicy {
/*      */     boolean stuck;
/*      */
/*      */     public boolean isStuck() {
/*      */       return (super.isStuck() || this.stuck);
/*      */     }
/*      */
/*      */     public OverloadStuckPolicy(Attr.ResultInfo param1ResultInfo, DeferredType param1DeferredType) {
/*      */       super(param1ResultInfo, param1DeferredType);
/*      */     }
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/*      */       super.visitLambda(param1JCLambda);
/*      */       if (param1JCLambda.paramKind == JCTree.JCLambda.ParameterKind.IMPLICIT)
/*      */         this.stuck = true;
/*      */     }
/*      */
/*      */     public void visitReference(JCTree.JCMemberReference param1JCMemberReference) {
/*      */       super.visitReference(param1JCMemberReference);
/*      */       if (param1JCMemberReference.overloadKind == JCTree.JCMemberReference.OverloadKind.OVERLOADED)
/*      */         this.stuck = true;
/*      */     }
/*      */   }
/*      */
/*      */   boolean isDeferred(Env<AttrContext> paramEnv, JCTree.JCExpression paramJCExpression) {
/*      */     DeferredChecker deferredChecker = new DeferredChecker(paramEnv);
/*      */     deferredChecker.scan((JCTree)paramJCExpression);
/*      */     return deferredChecker.result.isPoly();
/*      */   }
/*      */
/*      */   enum ArgumentExpressionKind {
/*      */     POLY, NO_POLY, PRIMITIVE;
/*      */
/*      */     public final boolean isPoly() {
/*      */       return (this == POLY);
/*      */     }
/*      */
/*      */     public final boolean isPrimitive() {
/*      */       return (this == PRIMITIVE);
/*      */     }
/*      */
/*      */     static ArgumentExpressionKind standaloneKind(Type param1Type, Types param1Types) {
/*      */       return param1Types.unboxedTypeOrType(param1Type).isPrimitive() ? PRIMITIVE : NO_POLY;
/*      */     }
/*      */
/*      */     static ArgumentExpressionKind methodKind(Symbol param1Symbol, Types param1Types) {
/*      */       Type type = param1Symbol.type.getReturnType();
/*      */       if (param1Symbol.type.hasTag(TypeTag.FORALL) && type.containsAny(((Type.ForAll)param1Symbol.type).tvars))
/*      */         return POLY;
/*      */       return standaloneKind(type, param1Types);
/*      */     }
/*      */   }
/*      */
/*      */   final class DeferredChecker extends FilterScanner {
/*      */     Env<AttrContext> env;
/*      */     ArgumentExpressionKind result;
/*      */     MethodAnalyzer<ArgumentExpressionKind> argumentKindAnalyzer;
/*      */     MethodAnalyzer<Symbol> returnSymbolAnalyzer;
/*      */
/*      */     public DeferredChecker(Env<AttrContext> param1Env) {
/*      */       super(DeferredAttr.this.deferredCheckerTags);
/*      */       this.argumentKindAnalyzer = new MethodAnalyzer<ArgumentExpressionKind>() {
/*      */           public ArgumentExpressionKind process(Symbol.MethodSymbol param2MethodSymbol) {
/*      */             return ArgumentExpressionKind.methodKind((Symbol)param2MethodSymbol, DeferredAttr.this.types);
/*      */           }
/*      */
/*      */           public ArgumentExpressionKind reduce(ArgumentExpressionKind param2ArgumentExpressionKind1, ArgumentExpressionKind param2ArgumentExpressionKind2) {
/*      */             // Byte code:
/*      */             //   0: getstatic com/sun/tools/javac/comp/DeferredAttr$6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$ArgumentExpressionKind : [I
/*      */             //   3: aload_1
/*      */             //   4: invokevirtual ordinal : ()I
/*      */             //   7: iaload
/*      */             //   8: tableswitch default -> 53, 1 -> 36, 2 -> 38, 3 -> 51
/*      */             //   36: aload_2
/*      */             //   37: areturn
/*      */             //   38: aload_2
/*      */             //   39: invokevirtual isPoly : ()Z
/*      */             //   42: ifeq -> 49
/*      */             //   45: aload_2
/*      */             //   46: goto -> 50
/*      */             //   49: aload_1
/*      */             //   50: areturn
/*      */             //   51: aload_1
/*      */             //   52: areturn
/*      */             //   53: invokestatic error : ()V
/*      */             //   56: aconst_null
/*      */             //   57: areturn
/*      */             // Line number table:
/*      */             //   Java source line number -> byte code offset
/*      */             //   #1278	-> 0
/*      */             //   #1279	-> 36
/*      */             //   #1280	-> 38
/*      */             //   #1281	-> 51
/*      */             //   #1283	-> 53
/*      */             //   #1284	-> 56
/*      */           }
/*      */
/*      */           public boolean shouldStop(ArgumentExpressionKind param2ArgumentExpressionKind) {
/*      */             return param2ArgumentExpressionKind.isPoly();
/*      */           }
/*      */         };
/*      */       this.returnSymbolAnalyzer = new MethodAnalyzer<Symbol>() {
/*      */           public Symbol process(Symbol.MethodSymbol param2MethodSymbol) {
/*      */             ArgumentExpressionKind argumentExpressionKind = ArgumentExpressionKind.methodKind((Symbol)param2MethodSymbol, DeferredAttr.this.types);
/*      */             if (argumentExpressionKind == ArgumentExpressionKind.POLY || param2MethodSymbol.getReturnType().hasTag(TypeTag.TYPEVAR))
/*      */               return null;
/*      */             return (Symbol)(param2MethodSymbol.getReturnType()).tsym;
/*      */           }
/*      */
/*      */           public Symbol reduce(Symbol param2Symbol1, Symbol param2Symbol2) {
/*      */             return (param2Symbol1 == DeferredAttr.this.syms.errSymbol) ? param2Symbol2 : ((param2Symbol1 == param2Symbol2) ? param2Symbol1 : null);
/*      */           }
/*      */
/*      */           public boolean shouldStop(Symbol param2Symbol) {
/*      */             return (param2Symbol == null);
/*      */           }
/*      */         };
/*      */       this.env = param1Env;
/*      */     }
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/*      */       this.result = ArgumentExpressionKind.POLY;
/*      */     }
/*      */
/*      */     public void visitReference(JCTree.JCMemberReference param1JCMemberReference) {
/*      */       Env<AttrContext> env = this.env.dup((JCTree)param1JCMemberReference);
/*      */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)DeferredAttr.this.attribSpeculative((JCTree)param1JCMemberReference.getQualifierExpression(), env, DeferredAttr.this.attr.memberReferenceQualifierResult(param1JCMemberReference));
/*      */       JCTree.JCMemberReference jCMemberReference = (JCTree.JCMemberReference)(new TreeCopier(DeferredAttr.this.make)).copy((JCTree)param1JCMemberReference);
/*      */       jCMemberReference.expr = jCExpression;
/*      */       Symbol symbol = DeferredAttr.this.rs.getMemberReference((JCDiagnostic.DiagnosticPosition)param1JCMemberReference, env, jCMemberReference, jCExpression.type, param1JCMemberReference.name);
/*      */       param1JCMemberReference.sym = symbol;
/*      */       if (symbol.kind >= 128 || symbol.type.hasTag(TypeTag.FORALL) || (symbol.flags() & 0x400000000L) != 0L || (TreeInfo.isStaticSelector((JCTree)jCExpression, param1JCMemberReference.name.table.names) && jCExpression.type.isRaw())) {
/*      */         param1JCMemberReference.overloadKind = JCTree.JCMemberReference.OverloadKind.OVERLOADED;
/*      */       } else {
/*      */         param1JCMemberReference.overloadKind = JCTree.JCMemberReference.OverloadKind.UNOVERLOADED;
/*      */       }
/*      */       this.result = ArgumentExpressionKind.POLY;
/*      */     }
/*      */
/*      */     public void visitTypeCast(JCTree.JCTypeCast param1JCTypeCast) {
/*      */       this.result = ArgumentExpressionKind.NO_POLY;
/*      */     }
/*      */
/*      */     public void visitConditional(JCTree.JCConditional param1JCConditional) {
/*      */       scan((JCTree)param1JCConditional.truepart);
/*      */       if (!this.result.isPrimitive()) {
/*      */         this.result = ArgumentExpressionKind.POLY;
/*      */         return;
/*      */       }
/*      */       scan((JCTree)param1JCConditional.falsepart);
/*      */       this.result = reduce(ArgumentExpressionKind.PRIMITIVE);
/*      */     }
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/*      */       this.result = (TreeInfo.isDiamond((JCTree)param1JCNewClass) || DeferredAttr.this.attr.findDiamonds) ? ArgumentExpressionKind.POLY : ArgumentExpressionKind.NO_POLY;
/*      */     }
/*      */
/*      */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/*      */       Name name = TreeInfo.name((JCTree)param1JCMethodInvocation.meth);
/*      */       if (param1JCMethodInvocation.typeargs.nonEmpty() || name == name.table.names._this || name == name.table.names._super) {
/*      */         this.result = ArgumentExpressionKind.NO_POLY;
/*      */         return;
/*      */       }
/*      */       Symbol symbol = quicklyResolveMethod(this.env, param1JCMethodInvocation);
/*      */       if (symbol == null) {
/*      */         this.result = ArgumentExpressionKind.POLY;
/*      */         return;
/*      */       }
/*      */       this.result = analyzeCandidateMethods(symbol, ArgumentExpressionKind.PRIMITIVE, this.argumentKindAnalyzer);
/*      */     }
/*      */
/*      */     private boolean isSimpleReceiver(JCTree param1JCTree) {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/comp/DeferredAttr$6.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   3: aload_1
/*      */       //   4: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 114, 1 -> 52, 2 -> 54, 3 -> 66, 4 -> 66, 5 -> 68, 6 -> 80, 7 -> 82
/*      */       //   52: iconst_1
/*      */       //   53: ireturn
/*      */       //   54: aload_0
/*      */       //   55: aload_1
/*      */       //   56: checkcast com/sun/tools/javac/tree/JCTree$JCFieldAccess
/*      */       //   59: getfield selected : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   62: invokespecial isSimpleReceiver : (Lcom/sun/tools/javac/tree/JCTree;)Z
/*      */       //   65: ireturn
/*      */       //   66: iconst_1
/*      */       //   67: ireturn
/*      */       //   68: aload_0
/*      */       //   69: aload_1
/*      */       //   70: checkcast com/sun/tools/javac/tree/JCTree$JCAnnotatedType
/*      */       //   73: getfield underlyingType : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   76: invokespecial isSimpleReceiver : (Lcom/sun/tools/javac/tree/JCTree;)Z
/*      */       //   79: ireturn
/*      */       //   80: iconst_1
/*      */       //   81: ireturn
/*      */       //   82: aload_1
/*      */       //   83: checkcast com/sun/tools/javac/tree/JCTree$JCNewClass
/*      */       //   86: astore_2
/*      */       //   87: aload_2
/*      */       //   88: getfield encl : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   91: ifnonnull -> 112
/*      */       //   94: aload_2
/*      */       //   95: getfield def : Lcom/sun/tools/javac/tree/JCTree$JCClassDecl;
/*      */       //   98: ifnonnull -> 112
/*      */       //   101: aload_2
/*      */       //   102: invokestatic isDiamond : (Lcom/sun/tools/javac/tree/JCTree;)Z
/*      */       //   105: ifne -> 112
/*      */       //   108: iconst_1
/*      */       //   109: goto -> 113
/*      */       //   112: iconst_0
/*      */       //   113: ireturn
/*      */       //   114: iconst_0
/*      */       //   115: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1247	-> 0
/*      */       //   #1249	-> 52
/*      */       //   #1251	-> 54
/*      */       //   #1254	-> 66
/*      */       //   #1256	-> 68
/*      */       //   #1258	-> 80
/*      */       //   #1260	-> 82
/*      */       //   #1261	-> 87
/*      */       //   #1263	-> 114
/*      */     }
/*      */
/*      */     private ArgumentExpressionKind reduce(ArgumentExpressionKind param1ArgumentExpressionKind) {
/*      */       return this.argumentKindAnalyzer.reduce(this.result, param1ArgumentExpressionKind);
/*      */     }
/*      */
/*      */     public void visitLiteral(JCTree.JCLiteral param1JCLiteral) {
/*      */       Type type = DeferredAttr.this.attr.litType(param1JCLiteral.typetag);
/*      */       this.result = ArgumentExpressionKind.standaloneKind(type, DeferredAttr.this.types);
/*      */     }
/*      */
/*      */     void skip(JCTree param1JCTree) {
/*      */       this.result = ArgumentExpressionKind.NO_POLY;
/*      */     }
/*      */
/*      */     private Symbol quicklyResolveMethod(Env<AttrContext> param1Env, JCTree.JCMethodInvocation param1JCMethodInvocation) {
/*      */       // Byte code:
/*      */       //   0: aload_2
/*      */       //   1: getfield meth : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   4: getstatic com/sun/tools/javac/tree/JCTree$Tag.SELECT : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   7: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   10: ifeq -> 26
/*      */       //   13: aload_2
/*      */       //   14: getfield meth : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   17: checkcast com/sun/tools/javac/tree/JCTree$JCFieldAccess
/*      */       //   20: getfield selected : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   23: goto -> 27
/*      */       //   26: aconst_null
/*      */       //   27: astore_3
/*      */       //   28: aload_3
/*      */       //   29: ifnull -> 42
/*      */       //   32: aload_0
/*      */       //   33: aload_3
/*      */       //   34: invokespecial isSimpleReceiver : (Lcom/sun/tools/javac/tree/JCTree;)Z
/*      */       //   37: ifne -> 42
/*      */       //   40: aconst_null
/*      */       //   41: areturn
/*      */       //   42: aload_3
/*      */       //   43: ifnull -> 208
/*      */       //   46: getstatic com/sun/tools/javac/comp/DeferredAttr$6.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   49: aload_3
/*      */       //   50: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   53: invokevirtual ordinal : ()I
/*      */       //   56: iaload
/*      */       //   57: lookupswitch default -> 181, 6 -> 84, 7 -> 144
/*      */       //   84: aload_0
/*      */       //   85: aload_1
/*      */       //   86: aload_3
/*      */       //   87: checkcast com/sun/tools/javac/tree/JCTree$JCMethodInvocation
/*      */       //   90: invokespecial quicklyResolveMethod : (Lcom/sun/tools/javac/comp/Env;Lcom/sun/tools/javac/tree/JCTree$JCMethodInvocation;)Lcom/sun/tools/javac/code/Symbol;
/*      */       //   93: astore #5
/*      */       //   95: aload #5
/*      */       //   97: ifnonnull -> 102
/*      */       //   100: aconst_null
/*      */       //   101: areturn
/*      */       //   102: aload_0
/*      */       //   103: aload #5
/*      */       //   105: aload_0
/*      */       //   106: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   109: getfield syms : Lcom/sun/tools/javac/code/Symtab;
/*      */       //   112: getfield errSymbol : Lcom/sun/tools/javac/code/Symbol$ClassSymbol;
/*      */       //   115: aload_0
/*      */       //   116: getfield returnSymbolAnalyzer : Lcom/sun/tools/javac/comp/DeferredAttr$MethodAnalyzer;
/*      */       //   119: invokevirtual analyzeCandidateMethods : (Lcom/sun/tools/javac/code/Symbol;Ljava/lang/Object;Lcom/sun/tools/javac/comp/DeferredAttr$MethodAnalyzer;)Ljava/lang/Object;
/*      */       //   122: checkcast com/sun/tools/javac/code/Symbol
/*      */       //   125: astore #6
/*      */       //   127: aload #6
/*      */       //   129: ifnonnull -> 134
/*      */       //   132: aconst_null
/*      */       //   133: areturn
/*      */       //   134: aload #6
/*      */       //   136: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   139: astore #4
/*      */       //   141: goto -> 220
/*      */       //   144: aload_3
/*      */       //   145: checkcast com/sun/tools/javac/tree/JCTree$JCNewClass
/*      */       //   148: astore #7
/*      */       //   150: aload_0
/*      */       //   151: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   154: aload #7
/*      */       //   156: getfield clazz : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   159: aload_1
/*      */       //   160: aload_0
/*      */       //   161: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   164: getfield attr : Lcom/sun/tools/javac/comp/Attr;
/*      */       //   167: getfield unknownTypeExprInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   170: invokevirtual attribSpeculative : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/comp/Env;Lcom/sun/tools/javac/comp/Attr$ResultInfo;)Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   173: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   176: astore #4
/*      */       //   178: goto -> 220
/*      */       //   181: aload_0
/*      */       //   182: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   185: aload_3
/*      */       //   186: aload_1
/*      */       //   187: aload_0
/*      */       //   188: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   191: getfield attr : Lcom/sun/tools/javac/comp/Attr;
/*      */       //   194: getfield unknownTypeExprInfo : Lcom/sun/tools/javac/comp/Attr$ResultInfo;
/*      */       //   197: invokevirtual attribSpeculative : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/comp/Env;Lcom/sun/tools/javac/comp/Attr$ResultInfo;)Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   200: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   203: astore #4
/*      */       //   205: goto -> 220
/*      */       //   208: aload_1
/*      */       //   209: getfield enclClass : Lcom/sun/tools/javac/tree/JCTree$JCClassDecl;
/*      */       //   212: getfield sym : Lcom/sun/tools/javac/code/Symbol$ClassSymbol;
/*      */       //   215: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   218: astore #4
/*      */       //   220: aload #4
/*      */       //   222: getstatic com/sun/tools/javac/code/TypeTag.TYPEVAR : Lcom/sun/tools/javac/code/TypeTag;
/*      */       //   225: invokevirtual hasTag : (Lcom/sun/tools/javac/code/TypeTag;)Z
/*      */       //   228: ifeq -> 241
/*      */       //   231: aload #4
/*      */       //   233: invokevirtual getUpperBound : ()Lcom/sun/tools/javac/code/Type;
/*      */       //   236: astore #4
/*      */       //   238: goto -> 220
/*      */       //   241: aload_0
/*      */       //   242: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   245: getfield types : Lcom/sun/tools/javac/code/Types;
/*      */       //   248: aload #4
/*      */       //   250: invokevirtual capture : (Lcom/sun/tools/javac/code/Type;)Lcom/sun/tools/javac/code/Type;
/*      */       //   253: astore #4
/*      */       //   255: aload_0
/*      */       //   256: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   259: getfield rs : Lcom/sun/tools/javac/comp/Resolve;
/*      */       //   262: aload_2
/*      */       //   263: getfield args : Lcom/sun/tools/javac/util/List;
/*      */       //   266: invokevirtual length : ()I
/*      */       //   269: invokevirtual dummyArgs : (I)Lcom/sun/tools/javac/util/List;
/*      */       //   272: astore #5
/*      */       //   274: aload_2
/*      */       //   275: getfield meth : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   278: invokestatic name : (Lcom/sun/tools/javac/tree/JCTree;)Lcom/sun/tools/javac/util/Name;
/*      */       //   281: astore #6
/*      */       //   283: new com/sun/tools/javac/comp/DeferredAttr$DeferredChecker$2
/*      */       //   286: dup
/*      */       //   287: aload_0
/*      */       //   288: aload_0
/*      */       //   289: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   292: getfield rs : Lcom/sun/tools/javac/comp/Resolve;
/*      */       //   295: dup
/*      */       //   296: invokevirtual getClass : ()Ljava/lang/Class;
/*      */       //   299: pop
/*      */       //   300: aload #6
/*      */       //   302: aload #4
/*      */       //   304: aload #5
/*      */       //   306: invokestatic nil : ()Lcom/sun/tools/javac/util/List;
/*      */       //   309: getstatic com/sun/tools/javac/comp/Resolve$MethodResolutionPhase.VARARITY : Lcom/sun/tools/javac/comp/Resolve$MethodResolutionPhase;
/*      */       //   312: aload_3
/*      */       //   313: invokespecial <init> : (Lcom/sun/tools/javac/comp/DeferredAttr$DeferredChecker;Lcom/sun/tools/javac/comp/Resolve;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/code/Type;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/comp/Resolve$MethodResolutionPhase;Lcom/sun/tools/javac/tree/JCTree$JCExpression;)V
/*      */       //   316: astore #7
/*      */       //   318: aload_0
/*      */       //   319: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   322: getfield rs : Lcom/sun/tools/javac/comp/Resolve;
/*      */       //   325: aload_1
/*      */       //   326: aload_2
/*      */       //   327: aload #4
/*      */       //   329: getfield tsym : Lcom/sun/tools/javac/code/Symbol$TypeSymbol;
/*      */       //   332: aload_0
/*      */       //   333: getfield this$0 : Lcom/sun/tools/javac/comp/DeferredAttr;
/*      */       //   336: getfield rs : Lcom/sun/tools/javac/comp/Resolve;
/*      */       //   339: getfield arityMethodCheck : Lcom/sun/tools/javac/comp/Resolve$MethodCheck;
/*      */       //   342: aload #7
/*      */       //   344: invokevirtual lookupMethod : (Lcom/sun/tools/javac/comp/Env;Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticPosition;Lcom/sun/tools/javac/code/Symbol;Lcom/sun/tools/javac/comp/Resolve$MethodCheck;Lcom/sun/tools/javac/comp/Resolve$LookupHelper;)Lcom/sun/tools/javac/code/Symbol;
/*      */       //   347: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1305	-> 0
/*      */       //   #1309	-> 28
/*      */       //   #1310	-> 40
/*      */       //   #1315	-> 42
/*      */       //   #1316	-> 46
/*      */       //   #1318	-> 84
/*      */       //   #1319	-> 95
/*      */       //   #1320	-> 100
/*      */       //   #1321	-> 102
/*      */       //   #1322	-> 119
/*      */       //   #1323	-> 127
/*      */       //   #1324	-> 132
/*      */       //   #1325	-> 134
/*      */       //   #1326	-> 141
/*      */       //   #1328	-> 144
/*      */       //   #1329	-> 150
/*      */       //   #1330	-> 178
/*      */       //   #1332	-> 181
/*      */       //   #1333	-> 205
/*      */       //   #1336	-> 208
/*      */       //   #1339	-> 220
/*      */       //   #1340	-> 231
/*      */       //   #1343	-> 241
/*      */       //   #1345	-> 255
/*      */       //   #1346	-> 274
/*      */       //   #1348	-> 283
/*      */       //   #1361	-> 318
/*      */     }
/*      */
/*      */     <E> E analyzeCandidateMethods(Symbol param1Symbol, E param1E, MethodAnalyzer<E> param1MethodAnalyzer) {
/*      */       Resolve.AmbiguityError ambiguityError;
/*      */       E e;
/*      */       switch (param1Symbol.kind) {
/*      */         case 16:
/*      */           return param1MethodAnalyzer.process((Symbol.MethodSymbol)param1Symbol);
/*      */         case 129:
/*      */           ambiguityError = (Resolve.AmbiguityError)param1Symbol.baseSymbol();
/*      */           e = param1E;
/*      */           for (Symbol symbol : ambiguityError.ambiguousSyms) {
/*      */             if (symbol.kind == 16) {
/*      */               e = param1MethodAnalyzer.reduce(e, param1MethodAnalyzer.process((Symbol.MethodSymbol)symbol));
/*      */               if (param1MethodAnalyzer.shouldStop(e))
/*      */                 return e;
/*      */             }
/*      */           }
/*      */           return e;
/*      */       }
/*      */       return param1E;
/*      */     }
/*      */   }
/*      */
/*      */   static interface DeferredStuckPolicy {
/*      */     boolean isStuck();
/*      */
/*      */     Set<Type> stuckVars();
/*      */
/*      */     Set<Type> depVars();
/*      */   }
/*      */
/*      */   static interface DeferredTypeCompleter {
/*      */     Type complete(DeferredType param1DeferredType, Attr.ResultInfo param1ResultInfo, DeferredAttrContext param1DeferredAttrContext);
/*      */   }
/*      */
/*      */   static interface MethodAnalyzer<E> {
/*      */     E process(Symbol.MethodSymbol param1MethodSymbol);
/*      */
/*      */     E reduce(E param1E1, E param1E2);
/*      */
/*      */     boolean shouldStop(E param1E);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\DeferredAttr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
