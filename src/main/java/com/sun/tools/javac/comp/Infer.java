/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.GraphUtils;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Infer
/*      */ {
/*   65 */   protected static final Context.Key<Infer> inferKey = new Context.Key();
/*      */
/*      */   Resolve rs;
/*      */
/*      */   Check chk;
/*      */
/*      */   Symtab syms;
/*      */
/*      */   Types types;
/*      */   JCDiagnostic.Factory diags;
/*      */   Log log;
/*      */   boolean allowGraphInference;
/*      */
/*      */   public static Infer instance(Context paramContext) {
/*   79 */     Infer infer = (Infer)paramContext.get(inferKey);
/*   80 */     if (infer == null)
/*   81 */       infer = new Infer(paramContext);
/*   82 */     return infer;
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Infer(Context paramContext) {
/*  965 */     this.incorporationStepsLegacy = EnumSet.of(IncorporationStep.EQ_CHECK_LEGACY);
/*      */
/*      */
/*  968 */     this
/*  969 */       .incorporationStepsGraph = EnumSet.complementOf(EnumSet.of(IncorporationStep.EQ_CHECK_LEGACY));
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1064 */     this.incorporationCache = new HashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2326 */     this.emptyContext = new InferenceContext(List.nil());
/*      */     paramContext.put(inferKey, this);
/*      */     this.rs = Resolve.instance(paramContext);
/*      */     this.chk = Check.instance(paramContext);
/*      */     this.syms = Symtab.instance(paramContext);
/*      */     this.types = Types.instance(paramContext);
/*      */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*      */     this.log = Log.instance(paramContext);
/*      */     this.inferenceException = new InferenceException(this.diags);
/*      */     Options options = Options.instance(paramContext);
/*      */     this.allowGraphInference = (Source.instance(paramContext).allowGraphInference() && options.isUnset("useLegacyInference"));
/*      */   }
/*      */
/*      */   public static final Type anyPoly = (Type)new Type.JCNoType();
/*      */   protected final InferenceException inferenceException;
/*      */   static final int MAX_INCORPORATION_STEPS = 100;
/*      */   EnumSet<IncorporationStep> incorporationStepsLegacy;
/*      */   EnumSet<IncorporationStep> incorporationStepsGraph;
/*      */   Map<IncorporationBinaryOp, Boolean> incorporationCache;
/*      */   final InferenceContext emptyContext;
/*      */
/*      */   public static class InferenceException extends Resolve.InapplicableMethodException {
/*      */     private static final long serialVersionUID = 0L;
/*      */     List<JCDiagnostic> messages = List.nil();
/*      */
/*      */     InferenceException(JCDiagnostic.Factory param1Factory) {
/*      */       super(param1Factory);
/*      */     }
/*      */
/*      */     Resolve.InapplicableMethodException setMessage() {
/*      */       return this;
/*      */     }
/*      */
/*      */     Resolve.InapplicableMethodException setMessage(JCDiagnostic param1JCDiagnostic) {
/*      */       this.messages = this.messages.append(param1JCDiagnostic);
/*      */       return this;
/*      */     }
/*      */
/*      */     public JCDiagnostic getDiagnostic() {
/*      */       return (JCDiagnostic)this.messages.head;
/*      */     }
/*      */
/*      */     void clear() {
/*      */       this.messages = List.nil();
/*      */     }
/*      */   }
/*      */
/*      */   Type instantiateMethod(Env<AttrContext> paramEnv, List<Type> paramList1, Type.MethodType paramMethodType, Attr.ResultInfo paramResultInfo, Symbol.MethodSymbol paramMethodSymbol, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2, Resolve.MethodResolutionContext paramMethodResolutionContext, Warner paramWarner) throws InferenceException {
/*      */     InferenceContext inferenceContext = new InferenceContext(paramList1);
/*      */     this.inferenceException.clear();
/*      */     try {
/*      */       DeferredAttr.DeferredAttrContext deferredAttrContext = paramMethodResolutionContext.deferredAttrContext((Symbol)paramMethodSymbol, inferenceContext, paramResultInfo, paramWarner);
/*      */       paramMethodResolutionContext.methodCheck.argumentsAcceptable(paramEnv, deferredAttrContext, paramList2, paramMethodType.getParameterTypes(), paramWarner);
/*      */       if (this.allowGraphInference && paramResultInfo != null && !paramWarner.hasNonSilentLint(Lint.LintCategory.UNCHECKED)) {
/*      */         checkWithinBounds(inferenceContext, paramWarner);
/*      */         Type type = generateReturnConstraints(paramEnv.tree, paramResultInfo, paramMethodType, inferenceContext);
/*      */         paramMethodType = (Type.MethodType)this.types.createMethodTypeWithReturn((Type)paramMethodType, type);
/*      */         if (paramResultInfo.checkContext.inferenceContext().free(paramResultInfo.pt)) {
/*      */           inferenceContext.dupTo(paramResultInfo.checkContext.inferenceContext());
/*      */           deferredAttrContext.complete();
/*      */           return (Type)paramMethodType;
/*      */         }
/*      */       }
/*      */       deferredAttrContext.complete();
/*      */       if (this.allowGraphInference) {
/*      */         inferenceContext.solve(paramWarner);
/*      */       } else {
/*      */         inferenceContext.solveLegacy(true, paramWarner, LegacyInferenceSteps.EQ_LOWER.steps);
/*      */       }
/*      */       paramMethodType = (Type.MethodType)inferenceContext.asInstType((Type)paramMethodType);
/*      */       if (!this.allowGraphInference && inferenceContext.restvars().nonEmpty() && paramResultInfo != null && !paramWarner.hasNonSilentLint(Lint.LintCategory.UNCHECKED)) {
/*      */         generateReturnConstraints(paramEnv.tree, paramResultInfo, paramMethodType, inferenceContext);
/*      */         inferenceContext.solveLegacy(false, paramWarner, LegacyInferenceSteps.EQ_UPPER.steps);
/*      */         paramMethodType = (Type.MethodType)inferenceContext.asInstType((Type)paramMethodType);
/*      */       }
/*      */       if (paramResultInfo != null && this.rs.verboseResolutionMode.contains(Resolve.VerboseResolutionMode.DEFERRED_INST))
/*      */         this.log.note(paramEnv.tree.pos, "deferred.method.inst", new Object[] { paramMethodSymbol, paramMethodType, paramResultInfo.pt });
/*      */       return (Type)paramMethodType;
/*      */     } finally {
/*      */       if (paramResultInfo != null || !this.allowGraphInference) {
/*      */         inferenceContext.notifyChange();
/*      */       } else {
/*      */         inferenceContext.notifyChange(inferenceContext.boundedVars());
/*      */       }
/*      */       if (paramResultInfo == null)
/*      */         inferenceContext.captureTypeCache.clear();
/*      */     }
/*      */   }
/*      */
/*      */   Type generateReturnConstraints(JCTree paramJCTree, Attr.ResultInfo paramResultInfo, Type.MethodType paramMethodType, InferenceContext paramInferenceContext) {
/*      */     Type.JCVoidType jCVoidType;
/*      */     Type type3;
/*      */     InferenceContext inferenceContext = paramResultInfo.checkContext.inferenceContext();
/*      */     Type type1 = paramMethodType.getReturnType();
/*      */     if (paramMethodType.getReturnType().containsAny(paramInferenceContext.inferencevars) && inferenceContext != this.emptyContext) {
/*      */       type1 = this.types.capture(type1);
/*      */       for (Type null : type1.getTypeArguments()) {
/*      */         if (type3.hasTag(TypeTag.TYPEVAR) && ((Type.TypeVar)type3).isCaptured())
/*      */           paramInferenceContext.addVar((Type.TypeVar)type3);
/*      */       }
/*      */     }
/*      */     Type type2 = paramInferenceContext.asUndetVar(type1);
/*      */     Type type4 = paramResultInfo.pt;
/*      */     if (type2.hasTag(TypeTag.VOID)) {
/*      */       jCVoidType = this.syms.voidType;
/*      */     } else if (jCVoidType.hasTag(TypeTag.NONE)) {
/*      */       type3 = type1.isPrimitive() ? type1 : this.syms.objectType;
/*      */     } else if (type2.hasTag(TypeTag.UNDETVAR)) {
/*      */       if (paramResultInfo.pt.isReference()) {
/*      */         type3 = generateReturnConstraintsUndetVarToReference(paramJCTree, (Type.UndetVar)type2, type3, paramResultInfo, paramInferenceContext);
/*      */       } else if (type3.isPrimitive()) {
/*      */         type3 = generateReturnConstraintsPrimitive(paramJCTree, (Type.UndetVar)type2, type3, paramResultInfo, paramInferenceContext);
/*      */       }
/*      */     }
/*      */     Assert.check((this.allowGraphInference || !inferenceContext.free(type3)), "legacy inference engine cannot handle constraints on both sides of a subtyping assertion");
/*      */     Warner warner = new Warner();
/*      */     if (!paramResultInfo.checkContext.compatible(type2, inferenceContext.asUndetVar(type3), warner) || (!this.allowGraphInference && warner.hasLint(Lint.LintCategory.UNCHECKED)))
/*      */       throw this.inferenceException.setMessage("infer.no.conforming.instance.exists", new Object[] { paramInferenceContext.restvars(), paramMethodType.getReturnType(), type3 });
/*      */     return type1;
/*      */   }
/*      */
/*      */   private Type generateReturnConstraintsPrimitive(JCTree paramJCTree, Type.UndetVar paramUndetVar, Type paramType, Attr.ResultInfo paramResultInfo, InferenceContext paramInferenceContext) {
/*      */     if (!this.allowGraphInference)
/*      */       return (this.types.boxedClass(paramType)).type;
/*      */     for (Type type1 : paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.UPPER, Type.UndetVar.InferenceBound.LOWER })) {
/*      */       Type type2 = this.types.unboxedType(type1);
/*      */       if (type2 == null || type2.hasTag(TypeTag.NONE))
/*      */         continue;
/*      */       return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */     }
/*      */     return (this.types.boxedClass(paramType)).type;
/*      */   }
/*      */
/*      */   private Type generateReturnConstraintsUndetVarToReference(JCTree paramJCTree, Type.UndetVar paramUndetVar, Type paramType, Attr.ResultInfo paramResultInfo, InferenceContext paramInferenceContext) {
/*      */     Type type = this.types.capture(paramType);
/*      */     if (type == paramType) {
/*      */       for (Type type1 : paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER })) {
/*      */         Type type2 = this.types.capture(type1);
/*      */         if (type2 != type1)
/*      */           return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */       }
/*      */       for (Type type1 : paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER })) {
/*      */         for (Type type2 : paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER })) {
/*      */           if (type1 != type2 && !paramInferenceContext.free(type1) && !paramInferenceContext.free(type2) && commonSuperWithDiffParameterization(type1, type2))
/*      */             return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */         }
/*      */       }
/*      */     }
/*      */     if (paramType.isParameterized())
/*      */       for (Type type1 : paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER })) {
/*      */         Type type2 = this.types.asSuper(type1, (Symbol)paramType.tsym);
/*      */         if (type2 != null && type2.isRaw())
/*      */           return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */       }
/*      */     return paramType;
/*      */   }
/*      */
/*      */   private boolean commonSuperWithDiffParameterization(Type paramType1, Type paramType2) {
/*      */     Pair<Type, Type> pair = getParameterizedSupers(paramType1, paramType2);
/*      */     return (pair != null && !this.types.isSameType((Type)pair.fst, (Type)pair.snd));
/*      */   }
/*      */
/*      */   private Type generateReferenceToTargetConstraint(JCTree paramJCTree, Type.UndetVar paramUndetVar, Type paramType, Attr.ResultInfo paramResultInfo, InferenceContext paramInferenceContext) {
/*      */     paramInferenceContext.solve(List.of(paramUndetVar.qtype), new Warner());
/*      */     paramInferenceContext.notifyChange();
/*      */     Type type = paramResultInfo.checkContext.inferenceContext().cachedCapture(paramJCTree, paramUndetVar.inst, false);
/*      */     if (this.types.isConvertible(type, paramResultInfo.checkContext.inferenceContext().asUndetVar(paramType)))
/*      */       return this.syms.objectType;
/*      */     return paramType;
/*      */   }
/*      */
/*      */   private void instantiateAsUninferredVars(List<Type> paramList, InferenceContext paramInferenceContext) {
/*      */     ListBuffer listBuffer = new ListBuffer();
/*      */     for (Type type : paramList) {
/*      */       Type.UndetVar undetVar = (Type.UndetVar)paramInferenceContext.asUndetVar(type);
/*      */       List list1 = undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER });
/*      */       if (Type.containsAny(list1, paramList)) {
/*      */         Symbol.TypeVariableSymbol typeVariableSymbol = new Symbol.TypeVariableSymbol(4096L, undetVar.qtype.tsym.name, null, undetVar.qtype.tsym.owner);
/*      */         ((Symbol.TypeSymbol)typeVariableSymbol).type = (Type)new Type.TypeVar((Symbol.TypeSymbol)typeVariableSymbol, (Type)this.types.makeIntersectionType(undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }, )), null);
/*      */         listBuffer.append(undetVar);
/*      */         undetVar.inst = ((Symbol.TypeSymbol)typeVariableSymbol).type;
/*      */         continue;
/*      */       }
/*      */       if (list1.nonEmpty()) {
/*      */         undetVar.inst = this.types.glb(list1);
/*      */         continue;
/*      */       }
/*      */       undetVar.inst = this.syms.objectType;
/*      */     }
/*      */     List<Type> list = paramList;
/*      */     for (Type type : listBuffer) {
/*      */       Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */       Type.TypeVar typeVar = (Type.TypeVar)undetVar.inst;
/*      */       typeVar.bound = this.types.glb(paramInferenceContext.asInstTypes(this.types.getBounds(typeVar)));
/*      */       if (typeVar.bound.isErroneous())
/*      */         reportBoundError(undetVar, BoundErrorKind.BAD_UPPER);
/*      */       list = list.tail;
/*      */     }
/*      */   }
/*      */
/*      */   Type instantiatePolymorphicSignatureInstance(Env<AttrContext> paramEnv, Symbol.MethodSymbol paramMethodSymbol, Resolve.MethodResolutionContext paramMethodResolutionContext, List<Type> paramList) {
/*      */     Type type;
/*      */     JCTree.JCTypeCast jCTypeCast;
/*      */     JCTree.JCExpressionStatement jCExpressionStatement;
/*      */     switch (paramEnv.next.tree.getTag()) {
/*      */       case EQ:
/*      */         jCTypeCast = (JCTree.JCTypeCast)paramEnv.next.tree;
/*      */         type = (TreeInfo.skipParens(jCTypeCast.expr) == paramEnv.tree) ? jCTypeCast.clazz.type : this.syms.objectType;
/*      */         break;
/*      */       case LOWER:
/*      */         jCExpressionStatement = (JCTree.JCExpressionStatement)paramEnv.next.tree;
/*      */         type = (TreeInfo.skipParens(jCExpressionStatement.expr) == paramEnv.tree) ? (Type)this.syms.voidType : this.syms.objectType;
/*      */         break;
/*      */       default:
/*      */         type = this.syms.objectType;
/*      */         break;
/*      */     }
/*      */     List list1 = Type.map(paramList, new ImplicitArgType((Symbol)paramMethodSymbol, paramMethodResolutionContext.step));
/*      */     List list2 = (paramMethodSymbol != null) ? paramMethodSymbol.getThrownTypes() : List.of(this.syms.throwableType);
/*      */     return (Type)new Type.MethodType(list1, type, list2, (Symbol.TypeSymbol)this.syms.methodClass);
/*      */   }
/*      */
/*      */   class ImplicitArgType extends DeferredAttr.DeferredTypeMap {
/*      */     public ImplicitArgType(Symbol param1Symbol, Resolve.MethodResolutionPhase param1MethodResolutionPhase) {
/*      */       super(DeferredAttr.AttrMode.SPECULATIVE, param1Symbol, param1MethodResolutionPhase);
/*      */     }
/*      */
/*      */     public Type apply(Type param1Type) {
/*      */       param1Type = Infer.this.types.erasure(super.apply(param1Type));
/*      */       if (param1Type.hasTag(TypeTag.BOT))
/*      */         param1Type = (Infer.this.types.boxedClass((Type)Infer.this.syms.voidType)).type;
/*      */       return param1Type;
/*      */     }
/*      */   }
/*      */
/*      */   public Type instantiateFunctionalInterface(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type> paramList, Check.CheckContext paramCheckContext) {
/*      */     if (this.types.capture(paramType) == paramType)
/*      */       return paramType;
/*      */     Type type1 = paramType.tsym.type;
/*      */     InferenceContext inferenceContext = new InferenceContext(paramType.tsym.type.getTypeArguments());
/*      */     Assert.check((paramList != null));
/*      */     List list1 = this.types.findDescriptorType(type1).getParameterTypes();
/*      */     if (list1.size() != paramList.size()) {
/*      */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("incompatible.arg.types.in.lambda", new Object[0]));
/*      */       return this.types.createErrorType(paramType);
/*      */     }
/*      */     for (Type type : list1) {
/*      */       if (!this.types.isSameType(inferenceContext.asUndetVar(type), (Type)paramList.head)) {
/*      */         paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { paramType }));
/*      */         return this.types.createErrorType(paramType);
/*      */       }
/*      */       paramList = paramList.tail;
/*      */     }
/*      */     try {
/*      */       inferenceContext.solve(inferenceContext.boundedVars(), this.types.noWarnings);
/*      */     } catch (InferenceException inferenceException) {
/*      */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { paramType }));
/*      */     }
/*      */     List list2 = paramType.getTypeArguments();
/*      */     for (Type type : inferenceContext.undetvars) {
/*      */       Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */       if (undetVar.inst == null)
/*      */         undetVar.inst = (Type)list2.head;
/*      */       list2 = list2.tail;
/*      */     }
/*      */     Type type2 = inferenceContext.asInstType(type1);
/*      */     if (!this.chk.checkValidGenericType(type2))
/*      */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { paramType }));
/*      */     paramCheckContext.compatible(type2, paramType, this.types.noWarnings);
/*      */     return type2;
/*      */   }
/*      */
/*      */   void checkWithinBounds(InferenceContext paramInferenceContext, Warner paramWarner) throws InferenceException {
/*      */     MultiUndetVarListener multiUndetVarListener = new MultiUndetVarListener(paramInferenceContext.undetvars);
/*      */     List<Type> list = paramInferenceContext.save();
/*      */     try {
/*      */       do {
/*      */         multiUndetVarListener.reset();
/*      */         if (!this.allowGraphInference)
/*      */           for (Type type : paramInferenceContext.undetvars) {
/*      */             Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */             IncorporationStep.CHECK_BOUNDS.apply(undetVar, paramInferenceContext, paramWarner);
/*      */           }
/*      */         for (Type type : paramInferenceContext.undetvars) {
/*      */           Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */           EnumSet<IncorporationStep> enumSet = this.allowGraphInference ? this.incorporationStepsGraph : this.incorporationStepsLegacy;
/*      */           for (IncorporationStep incorporationStep : enumSet) {
/*      */             if (incorporationStep.accepts(undetVar, paramInferenceContext))
/*      */               incorporationStep.apply(undetVar, paramInferenceContext, paramWarner);
/*      */           }
/*      */         }
/*      */       } while (multiUndetVarListener.changed && this.allowGraphInference);
/*      */     } finally {
/*      */       multiUndetVarListener.detach();
/*      */       if (this.incorporationCache.size() == 100)
/*      */         paramInferenceContext.rollback(list);
/*      */       this.incorporationCache.clear();
/*      */     }
/*      */   }
/*      */
/*      */   class MultiUndetVarListener implements Type.UndetVar.UndetVarListener {
/*      */     boolean changed;
/*      */     List<Type> undetvars;
/*      */
/*      */     public MultiUndetVarListener(List<Type> param1List) {
/*      */       this.undetvars = param1List;
/*      */       for (Type type : param1List) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */         undetVar.listener = this;
/*      */       }
/*      */     }
/*      */
/*      */     public void varChanged(Type.UndetVar param1UndetVar, Set<Type.UndetVar.InferenceBound> param1Set) {
/*      */       if (Infer.this.incorporationCache.size() < 100)
/*      */         this.changed = true;
/*      */     }
/*      */
/*      */     void reset() {
/*      */       this.changed = false;
/*      */     }
/*      */
/*      */     void detach() {
/*      */       for (Type type : this.undetvars) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */         undetVar.listener = null;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private Pair<Type, Type> getParameterizedSupers(Type paramType1, Type paramType2) {
/*      */     Type type1 = this.types.lub(new Type[] { paramType1, paramType2 });
/*      */     if (type1 == this.syms.errType || type1 == this.syms.botType || !type1.isParameterized())
/*      */       return null;
/*      */     Type type2 = this.types.asSuper(paramType1, (Symbol)type1.tsym);
/*      */     Type type3 = this.types.asSuper(paramType2, (Symbol)type1.tsym);
/*      */     return new Pair(type2, type3);
/*      */   }
/*      */
/*      */   enum IncorporationStep {
/*      */     CHECK_BOUNDS {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         param2UndetVar.substBounds(param2InferenceContext.inferenceVars(), param2InferenceContext.instTypes(), infer.types);
/*      */         infer.checkCompatibleUpperBounds(param2UndetVar, param2InferenceContext);
/*      */         if (param2UndetVar.inst != null) {
/*      */           Type type = param2UndetVar.inst;
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })) {
/*      */             if (!isSubtype(type, param2InferenceContext.asUndetVar(type1), param2Warner, infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.UPPER);
/*      */           }
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER })) {
/*      */             if (!isSubtype(param2InferenceContext.asUndetVar(type1), type, param2Warner, infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.LOWER);
/*      */           }
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */             if (!isSameType(type, param2InferenceContext.asUndetVar(type1), infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.EQ);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */       boolean accepts(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return true;
/*      */       }
/*      */     },
/*      */     EQ_CHECK_LEGACY {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         Type type = null;
/*      */         for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */           Assert.check(!param2InferenceContext.free(type1));
/*      */           if (type != null && !isSameType(type1, type, infer))
/*      */             infer.reportBoundError(param2UndetVar, BoundErrorKind.EQ);
/*      */           type = type1;
/*      */           for (Type type2 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER })) {
/*      */             Assert.check(!param2InferenceContext.free(type2));
/*      */             if (!isSubtype(type2, type1, param2Warner, infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.BAD_EQ_LOWER);
/*      */           }
/*      */           for (Type type2 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })) {
/*      */             if (!param2InferenceContext.free(type2) && !isSubtype(type1, type2, param2Warner, infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.BAD_EQ_UPPER);
/*      */           }
/*      */         }
/*      */       }
/*      */     },
/*      */     EQ_CHECK {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */           if (type.containsAny(param2InferenceContext.inferenceVars()))
/*      */             continue;
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })) {
/*      */             if (!isSubtype(type, param2InferenceContext.asUndetVar(type1), param2Warner, infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.BAD_EQ_UPPER);
/*      */           }
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER })) {
/*      */             if (!isSubtype(param2InferenceContext.asUndetVar(type1), type, param2Warner, infer))
/*      */               infer.reportBoundError(param2UndetVar, BoundErrorKind.BAD_EQ_LOWER);
/*      */           }
/*      */         }
/*      */       }
/*      */     },
/*      */     CROSS_UPPER_LOWER {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })) {
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }))
/*      */             isSubtype(param2InferenceContext.asUndetVar(type1), param2InferenceContext.asUndetVar(type), param2Warner, infer);
/*      */         }
/*      */       }
/*      */     },
/*      */     CROSS_UPPER_EQ {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })) {
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ }))
/*      */             isSubtype(param2InferenceContext.asUndetVar(type1), param2InferenceContext.asUndetVar(type), param2Warner, infer);
/*      */         }
/*      */       }
/*      */     },
/*      */     CROSS_EQ_LOWER {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }))
/*      */             isSubtype(param2InferenceContext.asUndetVar(type1), param2InferenceContext.asUndetVar(type), param2Warner, infer);
/*      */         }
/*      */       }
/*      */     },
/*      */     CROSS_UPPER_UPPER {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         List list1 = param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER });
/*      */         List list2 = list1.tail;
/*      */         while (list1.nonEmpty()) {
/*      */           List list = list2;
/*      */           while (list.nonEmpty()) {
/*      */             Type type1 = (Type)list1.head;
/*      */             Type type2 = (Type)list.head;
/*      */             if (type1 != type2 && !type1.hasTag(TypeTag.WILDCARD) && !type2.hasTag(TypeTag.WILDCARD)) {
/*      */               Pair pair = infer.getParameterizedSupers(type1, type2);
/*      */               if (pair != null) {
/*      */                 List list3 = ((Type)pair.fst).allparams();
/*      */                 List list4 = ((Type)pair.snd).allparams();
/*      */                 while (list3.nonEmpty() && list4.nonEmpty()) {
/*      */                   if (!((Type)list3.head).hasTag(TypeTag.WILDCARD) && !((Type)list4.head).hasTag(TypeTag.WILDCARD))
/*      */                     isSameType(param2InferenceContext.asUndetVar((Type)list3.head), param2InferenceContext.asUndetVar((Type)list4.head), infer);
/*      */                   list3 = list3.tail;
/*      */                   list4 = list4.tail;
/*      */                 }
/*      */                 Assert.check((list3.isEmpty() && list4.isEmpty()));
/*      */               }
/*      */             }
/*      */             list = list.tail;
/*      */           }
/*      */           list1 = list1.tail;
/*      */           list2 = list1.tail;
/*      */         }
/*      */       }
/*      */
/*      */       boolean accepts(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return (!param2UndetVar.isCaptured() && param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }).nonEmpty());
/*      */       }
/*      */     },
/*      */     CROSS_EQ_EQ {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */           for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */             if (type != type1)
/*      */               isSameType(param2InferenceContext.asUndetVar(type1), param2InferenceContext.asUndetVar(type), infer);
/*      */           }
/*      */         }
/*      */       }
/*      */     },
/*      */     PROP_UPPER {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })) {
/*      */           if (param2InferenceContext.inferenceVars().contains(type)) {
/*      */             Type.UndetVar undetVar = (Type.UndetVar)param2InferenceContext.asUndetVar(type);
/*      */             if (undetVar.isCaptured())
/*      */               continue;
/*      */             addBound(Type.UndetVar.InferenceBound.LOWER, undetVar, param2InferenceContext.asInstType(param2UndetVar.qtype), infer);
/*      */             for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }))
/*      */               addBound(Type.UndetVar.InferenceBound.LOWER, undetVar, param2InferenceContext.asInstType(type1), infer);
/*      */             for (Type type1 : undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }))
/*      */               addBound(Type.UndetVar.InferenceBound.UPPER, param2UndetVar, param2InferenceContext.asInstType(type1), infer);
/*      */           }
/*      */         }
/*      */       }
/*      */     },
/*      */     PROP_LOWER {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER })) {
/*      */           if (param2InferenceContext.inferenceVars().contains(type)) {
/*      */             Type.UndetVar undetVar = (Type.UndetVar)param2InferenceContext.asUndetVar(type);
/*      */             if (undetVar.isCaptured())
/*      */               continue;
/*      */             addBound(Type.UndetVar.InferenceBound.UPPER, undetVar, param2InferenceContext.asInstType(param2UndetVar.qtype), infer);
/*      */             for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }))
/*      */               addBound(Type.UndetVar.InferenceBound.UPPER, undetVar, param2InferenceContext.asInstType(type1), infer);
/*      */             for (Type type1 : undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }))
/*      */               addBound(Type.UndetVar.InferenceBound.LOWER, param2UndetVar, param2InferenceContext.asInstType(type1), infer);
/*      */           }
/*      */         }
/*      */       }
/*      */     },
/*      */     PROP_EQ {
/*      */       public void apply(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext, Warner param2Warner) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ })) {
/*      */           if (param2InferenceContext.inferenceVars().contains(type)) {
/*      */             Type.UndetVar undetVar = (Type.UndetVar)param2InferenceContext.asUndetVar(type);
/*      */             if (undetVar.isCaptured())
/*      */               continue;
/*      */             addBound(Type.UndetVar.InferenceBound.EQ, undetVar, param2InferenceContext.asInstType(param2UndetVar.qtype), infer);
/*      */             for (Type.UndetVar.InferenceBound inferenceBound : Type.UndetVar.InferenceBound.values()) {
/*      */               for (Type type1 : param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { inferenceBound })) {
/*      */                 if (type1 != undetVar)
/*      */                   addBound(inferenceBound, undetVar, param2InferenceContext.asInstType(type1), infer);
/*      */               }
/*      */             }
/*      */             for (Type.UndetVar.InferenceBound inferenceBound : Type.UndetVar.InferenceBound.values()) {
/*      */               for (Type type1 : undetVar.getBounds(new Type.UndetVar.InferenceBound[] { inferenceBound })) {
/*      */                 if (type1 != param2UndetVar)
/*      */                   addBound(inferenceBound, param2UndetVar, param2InferenceContext.asInstType(type1), infer);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     };
/*      */
/*      */     boolean accepts(Type.UndetVar param1UndetVar, InferenceContext param1InferenceContext) {
/*      */       return !param1UndetVar.isCaptured();
/*      */     }
/*      */
/*      */     boolean isSubtype(Type param1Type1, Type param1Type2, Warner param1Warner, Infer param1Infer) {
/*      */       return doIncorporationOp(IncorporationBinaryOpKind.IS_SUBTYPE, param1Type1, param1Type2, param1Warner, param1Infer);
/*      */     }
/*      */
/*      */     boolean isSameType(Type param1Type1, Type param1Type2, Infer param1Infer) {
/*      */       return doIncorporationOp(IncorporationBinaryOpKind.IS_SAME_TYPE, param1Type1, param1Type2, null, param1Infer);
/*      */     }
/*      */
/*      */     void addBound(Type.UndetVar.InferenceBound param1InferenceBound, Type.UndetVar param1UndetVar, Type param1Type, Infer param1Infer) {
/*      */       doIncorporationOp(opFor(param1InferenceBound), (Type)param1UndetVar, param1Type, null, param1Infer);
/*      */     }
/*      */
/*      */     IncorporationBinaryOpKind opFor(Type.UndetVar.InferenceBound param1InferenceBound) {
/*      */       switch (param1InferenceBound) {
/*      */         case EQ:
/*      */           return IncorporationBinaryOpKind.ADD_EQ_BOUND;
/*      */         case LOWER:
/*      */           return IncorporationBinaryOpKind.ADD_LOWER_BOUND;
/*      */         case UPPER:
/*      */           return IncorporationBinaryOpKind.ADD_UPPER_BOUND;
/*      */       }
/*      */       Assert.error("Can't get here!");
/*      */       return null;
/*      */     }
/*      */
/*      */     boolean doIncorporationOp(IncorporationBinaryOpKind param1IncorporationBinaryOpKind, Type param1Type1, Type param1Type2, Warner param1Warner, Infer param1Infer) {
/*      */       param1Infer.getClass();
/*      */       IncorporationBinaryOp incorporationBinaryOp = new IncorporationBinaryOp(param1IncorporationBinaryOpKind, param1Type1, param1Type2);
/*      */       Boolean bool = param1Infer.incorporationCache.get(incorporationBinaryOp);
/*      */       if (bool == null)
/*      */         param1Infer.incorporationCache.put(incorporationBinaryOp, bool = Boolean.valueOf(incorporationBinaryOp.apply(param1Warner)));
/*      */       return bool.booleanValue();
/*      */     }
/*      */
/*      */     abstract void apply(Type.UndetVar param1UndetVar, InferenceContext param1InferenceContext, Warner param1Warner);
/*      */   }
/*      */
/*      */   enum IncorporationBinaryOpKind {
/*      */     IS_SUBTYPE {
/*      */       boolean apply(Type param2Type1, Type param2Type2, Warner param2Warner, Types param2Types) {
/*      */         return param2Types.isSubtypeUnchecked(param2Type1, param2Type2, param2Warner);
/*      */       }
/*      */     },
/*      */     IS_SAME_TYPE {
/*      */       boolean apply(Type param2Type1, Type param2Type2, Warner param2Warner, Types param2Types) {
/*      */         return param2Types.isSameType(param2Type1, param2Type2);
/*      */       }
/*      */     },
/*      */     ADD_UPPER_BOUND {
/*      */       boolean apply(Type param2Type1, Type param2Type2, Warner param2Warner, Types param2Types) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)param2Type1;
/*      */         undetVar.addBound(Type.UndetVar.InferenceBound.UPPER, param2Type2, param2Types);
/*      */         return true;
/*      */       }
/*      */     },
/*      */     ADD_LOWER_BOUND {
/*      */       boolean apply(Type param2Type1, Type param2Type2, Warner param2Warner, Types param2Types) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)param2Type1;
/*      */         undetVar.addBound(Type.UndetVar.InferenceBound.LOWER, param2Type2, param2Types);
/*      */         return true;
/*      */       }
/*      */     },
/*      */     ADD_EQ_BOUND {
/*      */       boolean apply(Type param2Type1, Type param2Type2, Warner param2Warner, Types param2Types) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)param2Type1;
/*      */         undetVar.addBound(Type.UndetVar.InferenceBound.EQ, param2Type2, param2Types);
/*      */         return true;
/*      */       }
/*      */     };
/*      */
/*      */     abstract boolean apply(Type param1Type1, Type param1Type2, Warner param1Warner, Types param1Types);
/*      */   }
/*      */
/*      */   class IncorporationBinaryOp {
/*      */     IncorporationBinaryOpKind opKind;
/*      */     Type op1;
/*      */     Type op2;
/*      */
/*      */     IncorporationBinaryOp(IncorporationBinaryOpKind param1IncorporationBinaryOpKind, Type param1Type1, Type param1Type2) {
/*      */       this.opKind = param1IncorporationBinaryOpKind;
/*      */       this.op1 = param1Type1;
/*      */       this.op2 = param1Type2;
/*      */     }
/*      */
/*      */     public boolean equals(Object param1Object) {
/*      */       if (!(param1Object instanceof IncorporationBinaryOp))
/*      */         return false;
/*      */       IncorporationBinaryOp incorporationBinaryOp = (IncorporationBinaryOp)param1Object;
/*      */       return (this.opKind == incorporationBinaryOp.opKind && Infer.this.types.isSameType(this.op1, incorporationBinaryOp.op1, true) && Infer.this.types.isSameType(this.op2, incorporationBinaryOp.op2, true));
/*      */     }
/*      */
/*      */     public int hashCode() {
/*      */       int i = this.opKind.hashCode();
/*      */       i *= 127;
/*      */       i += Infer.this.types.hashCode(this.op1);
/*      */       i *= 127;
/*      */       i += Infer.this.types.hashCode(this.op2);
/*      */       return i;
/*      */     }
/*      */
/*      */     boolean apply(Warner param1Warner) {
/*      */       return this.opKind.apply(this.op1, this.op2, param1Warner, Infer.this.types);
/*      */     }
/*      */   }
/*      */
/*      */   void checkCompatibleUpperBounds(Type.UndetVar paramUndetVar, InferenceContext paramInferenceContext) {
/*      */     List list = Type.filter(paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }, ), new BoundFilter(paramInferenceContext));
/*      */     Type type = null;
/*      */     if (list.isEmpty()) {
/*      */       type = this.syms.objectType;
/*      */     } else if (list.tail.isEmpty()) {
/*      */       type = (Type)list.head;
/*      */     } else {
/*      */       type = this.types.glb(list);
/*      */     }
/*      */     if (type == null || type.isErroneous())
/*      */       reportBoundError(paramUndetVar, BoundErrorKind.BAD_UPPER);
/*      */   }
/*      */
/*      */   protected static class BoundFilter implements Filter<Type> {
/*      */     InferenceContext inferenceContext;
/*      */
/*      */     public BoundFilter(InferenceContext param1InferenceContext) {
/*      */       this.inferenceContext = param1InferenceContext;
/*      */     }
/*      */
/*      */     public boolean accepts(Type param1Type) {
/*      */       return (!param1Type.isErroneous() && !this.inferenceContext.free(param1Type) && !param1Type.hasTag(TypeTag.BOT));
/*      */     }
/*      */   }
/*      */
/*      */   enum BoundErrorKind {
/*      */     BAD_UPPER {
/*      */       Resolve.InapplicableMethodException setMessage(InferenceException param2InferenceException, Type.UndetVar param2UndetVar) {
/*      */         return param2InferenceException.setMessage("incompatible.upper.bounds", new Object[] { param2UndetVar.qtype, param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }) });
/*      */       }
/*      */     },
/*      */     BAD_EQ_UPPER {
/*      */       Resolve.InapplicableMethodException setMessage(InferenceException param2InferenceException, Type.UndetVar param2UndetVar) {
/*      */         return param2InferenceException.setMessage("incompatible.eq.upper.bounds", new Object[] { param2UndetVar.qtype, param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ }), param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }) });
/*      */       }
/*      */     },
/*      */     BAD_EQ_LOWER {
/*      */       Resolve.InapplicableMethodException setMessage(InferenceException param2InferenceException, Type.UndetVar param2UndetVar) {
/*      */         return param2InferenceException.setMessage("incompatible.eq.lower.bounds", new Object[] { param2UndetVar.qtype, param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ }), param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }) });
/*      */       }
/*      */     },
/*      */     UPPER {
/*      */       Resolve.InapplicableMethodException setMessage(InferenceException param2InferenceException, Type.UndetVar param2UndetVar) {
/*      */         return param2InferenceException.setMessage("inferred.do.not.conform.to.upper.bounds", new Object[] { param2UndetVar.inst, param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }) });
/*      */       }
/*      */     },
/*      */     LOWER {
/*      */       Resolve.InapplicableMethodException setMessage(InferenceException param2InferenceException, Type.UndetVar param2UndetVar) {
/*      */         return param2InferenceException.setMessage("inferred.do.not.conform.to.lower.bounds", new Object[] { param2UndetVar.inst, param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }) });
/*      */       }
/*      */     },
/*      */     EQ {
/*      */       Resolve.InapplicableMethodException setMessage(InferenceException param2InferenceException, Type.UndetVar param2UndetVar) {
/*      */         return param2InferenceException.setMessage("inferred.do.not.conform.to.eq.bounds", new Object[] { param2UndetVar.inst, param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ }) });
/*      */       }
/*      */     };
/*      */
/*      */     abstract Resolve.InapplicableMethodException setMessage(InferenceException param1InferenceException, Type.UndetVar param1UndetVar);
/*      */   }
/*      */
/*      */   void reportBoundError(Type.UndetVar paramUndetVar, BoundErrorKind paramBoundErrorKind) {
/*      */     throw paramBoundErrorKind.setMessage(this.inferenceException, paramUndetVar);
/*      */   }
/*      */
/*      */   static interface GraphStrategy {
/*      */     GraphSolver.InferenceGraph.Node pickNode(GraphSolver.InferenceGraph param1InferenceGraph) throws NodeNotFoundException;
/*      */
/*      */     boolean done();
/*      */
/*      */     public static class NodeNotFoundException extends RuntimeException {
/*      */       private static final long serialVersionUID = 0L;
/*      */       GraphSolver.InferenceGraph graph;
/*      */
/*      */       public NodeNotFoundException(GraphSolver.InferenceGraph param2InferenceGraph) {
/*      */         this.graph = param2InferenceGraph;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   abstract class LeafSolver implements GraphStrategy {
/*      */     public GraphSolver.InferenceGraph.Node pickNode(GraphSolver.InferenceGraph param1InferenceGraph) {
/*      */       if (param1InferenceGraph.nodes.isEmpty())
/*      */         throw new NodeNotFoundException(param1InferenceGraph);
/*      */       return param1InferenceGraph.nodes.get(0);
/*      */     }
/*      */
/*      */     boolean isSubtype(Type param1Type1, Type param1Type2, Warner param1Warner, Infer param1Infer) {
/*      */       return doIncorporationOp(IncorporationBinaryOpKind.IS_SUBTYPE, param1Type1, param1Type2, param1Warner, param1Infer);
/*      */     }
/*      */
/*      */     boolean isSameType(Type param1Type1, Type param1Type2, Infer param1Infer) {
/*      */       return doIncorporationOp(IncorporationBinaryOpKind.IS_SAME_TYPE, param1Type1, param1Type2, null, param1Infer);
/*      */     }
/*      */
/*      */     void addBound(Type.UndetVar.InferenceBound param1InferenceBound, Type.UndetVar param1UndetVar, Type param1Type, Infer param1Infer) {
/*      */       doIncorporationOp(opFor(param1InferenceBound), (Type)param1UndetVar, param1Type, null, param1Infer);
/*      */     }
/*      */
/*      */     IncorporationBinaryOpKind opFor(Type.UndetVar.InferenceBound param1InferenceBound) {
/*      */       switch (param1InferenceBound) {
/*      */         case EQ:
/*      */           return IncorporationBinaryOpKind.ADD_EQ_BOUND;
/*      */         case LOWER:
/*      */           return IncorporationBinaryOpKind.ADD_LOWER_BOUND;
/*      */         case UPPER:
/*      */           return IncorporationBinaryOpKind.ADD_UPPER_BOUND;
/*      */       }
/*      */       Assert.error("Can't get here!");
/*      */       return null;
/*      */     }
/*      */
/*      */     boolean doIncorporationOp(IncorporationBinaryOpKind param1IncorporationBinaryOpKind, Type param1Type1, Type param1Type2, Warner param1Warner, Infer param1Infer) {
/*      */       param1Infer.getClass();
/*      */       IncorporationBinaryOp incorporationBinaryOp = new IncorporationBinaryOp(param1IncorporationBinaryOpKind, param1Type1, param1Type2);
/*      */       Boolean bool = param1Infer.incorporationCache.get(incorporationBinaryOp);
/*      */       if (bool == null)
/*      */         param1Infer.incorporationCache.put(incorporationBinaryOp, bool = Boolean.valueOf(incorporationBinaryOp.apply(param1Warner)));
/*      */       return bool.booleanValue();
/*      */     }
/*      */   }
/*      */
/*      */   abstract class BestLeafSolver extends LeafSolver {
/*      */     List<Type> varsToSolve;
/*      */     final Map<GraphSolver.InferenceGraph.Node, Pair<List<GraphSolver.InferenceGraph.Node>, Integer>> treeCache;
/*      */     final Pair<List<GraphSolver.InferenceGraph.Node>, Integer> noPath;
/*      */
/*      */     BestLeafSolver(List<Type> param1List) {
/*      */       this.treeCache = new HashMap<>();
/*      */       this.noPath = new Pair(null, Integer.valueOf(2147483647));
/*      */       this.varsToSolve = param1List;
/*      */     }
/*      */
/*      */     Pair<List<GraphSolver.InferenceGraph.Node>, Integer> computeTreeToLeafs(GraphSolver.InferenceGraph.Node param1Node) {
/*      */       Pair<List<GraphSolver.InferenceGraph.Node>, Integer> pair = this.treeCache.get(param1Node);
/*      */       if (pair == null) {
/*      */         if (param1Node.isLeaf()) {
/*      */           pair = new Pair(List.of(param1Node), Integer.valueOf(((ListBuffer)param1Node.data).length()));
/*      */         } else {
/*      */           Pair<List<GraphSolver.InferenceGraph.Node>, Integer> pair1 = new Pair(List.of(param1Node), Integer.valueOf(((ListBuffer)param1Node.data).length()));
/*      */           for (GraphSolver.InferenceGraph.Node node : param1Node.getAllDependencies()) {
/*      */             if (node == param1Node)
/*      */               continue;
/*      */             Pair<List<GraphSolver.InferenceGraph.Node>, Integer> pair2 = computeTreeToLeafs(node);
/*      */             pair1 = new Pair(((List)pair1.fst).prependList((List)pair2.fst), Integer.valueOf(((Integer)pair1.snd).intValue() + ((Integer)pair2.snd).intValue()));
/*      */           }
/*      */           pair = pair1;
/*      */         }
/*      */         this.treeCache.put(param1Node, pair);
/*      */       }
/*      */       return pair;
/*      */     }
/*      */
/*      */     public GraphSolver.InferenceGraph.Node pickNode(GraphSolver.InferenceGraph param1InferenceGraph) {
/*      */       this.treeCache.clear();
/*      */       Pair<List<GraphSolver.InferenceGraph.Node>, Integer> pair = this.noPath;
/*      */       for (GraphSolver.InferenceGraph.Node node : param1InferenceGraph.nodes) {
/*      */         if (!Collections.disjoint((Collection)node.data, (Collection<?>)this.varsToSolve)) {
/*      */           Pair<List<GraphSolver.InferenceGraph.Node>, Integer> pair1 = computeTreeToLeafs(node);
/*      */           if (((Integer)pair1.snd).intValue() < ((Integer)pair.snd).intValue())
/*      */             pair = pair1;
/*      */         }
/*      */       }
/*      */       if (pair == this.noPath)
/*      */         throw new NodeNotFoundException(param1InferenceGraph);
/*      */       return (GraphSolver.InferenceGraph.Node)((List)pair.fst).head;
/*      */     }
/*      */   }
/*      */
/*      */   enum InferenceStep {
/*      */     EQ((String)Type.UndetVar.InferenceBound.EQ) {
/*      */       Type solve(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return (Type)(filterBounds(param2UndetVar, param2InferenceContext)).head;
/*      */       }
/*      */     },
/*      */     LOWER((String)Type.UndetVar.InferenceBound.LOWER) {
/*      */       Type solve(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         List<Type> list = filterBounds(param2UndetVar, param2InferenceContext);
/*      */         Type type = (list.tail.tail == null) ? (Type)list.head : infer.types.lub(list);
/*      */         if (type.isPrimitive() || type.hasTag(TypeTag.ERROR))
/*      */           throw infer.inferenceException.setMessage("no.unique.minimal.instance.exists", new Object[] { param2UndetVar.qtype, list });
/*      */         return type;
/*      */       }
/*      */     },
/*      */     THROWS((String)Type.UndetVar.InferenceBound.UPPER) {
/*      */       public boolean accepts(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         if ((param2UndetVar.qtype.tsym.flags() & 0x800000000000L) == 0L)
/*      */           return false;
/*      */         if (param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER, Type.UndetVar.InferenceBound.UPPER }).diff(param2UndetVar.getDeclaredBounds()).nonEmpty())
/*      */           return false;
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         for (Type type : param2UndetVar.getDeclaredBounds()) {
/*      */           if (!param2UndetVar.isInterface() && infer.types.asSuper(infer.syms.runtimeExceptionType, (Symbol)type.tsym) != null)
/*      */             return true;
/*      */         }
/*      */         return false;
/*      */       }
/*      */
/*      */       Type solve(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return (param2InferenceContext.infer()).syms.runtimeExceptionType;
/*      */       }
/*      */     },
/*      */     UPPER((String)Type.UndetVar.InferenceBound.UPPER) {
/*      */       Type solve(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         List<Type> list = filterBounds(param2UndetVar, param2InferenceContext);
/*      */         Type type = (list.tail.tail == null) ? (Type)list.head : infer.types.glb(list);
/*      */         if (type.isPrimitive() || type.hasTag(TypeTag.ERROR))
/*      */           throw infer.inferenceException.setMessage("no.unique.maximal.instance.exists", new Object[] { param2UndetVar.qtype, list });
/*      */         return type;
/*      */       }
/*      */     },
/*      */     UPPER_LEGACY((String)Type.UndetVar.InferenceBound.UPPER) {
/*      */       public boolean accepts(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return (!param2InferenceContext.free(param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { this.ib })) && !param2UndetVar.isCaptured());
/*      */       }
/*      */
/*      */       Type solve(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return UPPER.solve(param2UndetVar, param2InferenceContext);
/*      */       }
/*      */     },
/*      */     CAPTURED((String)Type.UndetVar.InferenceBound.UPPER) {
/*      */       public boolean accepts(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         return (param2UndetVar.isCaptured() && !param2InferenceContext.free(param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER, Type.UndetVar.InferenceBound.LOWER })));
/*      */       }
/*      */
/*      */       Type solve(Type.UndetVar param2UndetVar, InferenceContext param2InferenceContext) {
/*      */         Infer infer = param2InferenceContext.infer();
/*      */         Type type1 = UPPER.filterBounds(param2UndetVar, param2InferenceContext).nonEmpty() ? UPPER.solve(param2UndetVar, param2InferenceContext) : infer.syms.objectType;
/*      */         Type type2 = LOWER.filterBounds(param2UndetVar, param2InferenceContext).nonEmpty() ? LOWER.solve(param2UndetVar, param2InferenceContext) : infer.syms.botType;
/*      */         Type.CapturedType capturedType = (Type.CapturedType)param2UndetVar.qtype;
/*      */         return (Type)new Type.CapturedType(capturedType.tsym.name, capturedType.tsym.owner, type1, type2, capturedType.wildcard);
/*      */       }
/*      */     };
/*      */
/*      */     final Type.UndetVar.InferenceBound ib;
/*      */
/*      */     InferenceStep(Type.UndetVar.InferenceBound param1InferenceBound) {
/*      */       this.ib = param1InferenceBound;
/*      */     }
/*      */
/*      */     public boolean accepts(Type.UndetVar param1UndetVar, InferenceContext param1InferenceContext) {
/*      */       return (filterBounds(param1UndetVar, param1InferenceContext).nonEmpty() && !param1UndetVar.isCaptured());
/*      */     }
/*      */
/*      */     List<Type> filterBounds(Type.UndetVar param1UndetVar, InferenceContext param1InferenceContext) {
/*      */       return Type.filter(param1UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { this.ib }, ), new BoundFilter(param1InferenceContext));
/*      */     }
/*      */
/*      */     abstract Type solve(Type.UndetVar param1UndetVar, InferenceContext param1InferenceContext);
/*      */   }
/*      */
/*      */   enum LegacyInferenceSteps {
/*      */     EQ_LOWER((String)EnumSet.of(InferenceStep.EQ, InferenceStep.LOWER)),
/*      */     EQ_UPPER((String)EnumSet.of(InferenceStep.EQ, InferenceStep.UPPER_LEGACY));
/*      */     final EnumSet<InferenceStep> steps;
/*      */
/*      */     LegacyInferenceSteps(EnumSet<InferenceStep> param1EnumSet) {
/*      */       this.steps = param1EnumSet;
/*      */     }
/*      */   }
/*      */
/*      */   enum GraphInferenceSteps {
/*      */     EQ((String)EnumSet.of(InferenceStep.EQ)),
/*      */     EQ_LOWER((String)EnumSet.of(InferenceStep.EQ, InferenceStep.LOWER)),
/*      */     EQ_LOWER_THROWS_UPPER_CAPTURED((String)EnumSet.of(InferenceStep.EQ, InferenceStep.LOWER, InferenceStep.UPPER, InferenceStep.THROWS, InferenceStep.CAPTURED));
/*      */     final EnumSet<InferenceStep> steps;
/*      */
/*      */     GraphInferenceSteps(EnumSet<InferenceStep> param1EnumSet) {
/*      */       this.steps = param1EnumSet;
/*      */     }
/*      */   }
/*      */
/*      */   enum DependencyKind implements GraphUtils.DependencyKind {
/*      */     BOUND("dotted"),
/*      */     STUCK("dashed");
/*      */     final String dotSyle;
/*      */
/*      */     DependencyKind(String param1String1) {
/*      */       this.dotSyle = param1String1;
/*      */     }
/*      */
/*      */     public String getDotStyle() {
/*      */       return this.dotSyle;
/*      */     }
/*      */   }
/*      */
/*      */   class GraphSolver {
/*      */     InferenceContext inferenceContext;
/*      */     Map<Type, Set<Type>> stuckDeps;
/*      */     Warner warn;
/*      */
/*      */     GraphSolver(InferenceContext param1InferenceContext, Map<Type, Set<Type>> param1Map, Warner param1Warner) {
/*      */       this.inferenceContext = param1InferenceContext;
/*      */       this.stuckDeps = param1Map;
/*      */       this.warn = param1Warner;
/*      */     }
/*      */
/*      */     void solve(GraphStrategy param1GraphStrategy) {
/*      */       Infer.this.checkWithinBounds(this.inferenceContext, this.warn);
/*      */       InferenceGraph inferenceGraph = new InferenceGraph(this.stuckDeps);
/*      */       while (!param1GraphStrategy.done()) {
/*      */         InferenceGraph.Node node = param1GraphStrategy.pickNode(inferenceGraph);
/*      */         List list = List.from((Iterable)node.data);
/*      */         List<Type> list1 = this.inferenceContext.save();
/*      */         try {
/*      */           label22: while (Type.containsAny(this.inferenceContext.restvars(), list)) {
/*      */             for (GraphInferenceSteps graphInferenceSteps : GraphInferenceSteps.values()) {
/*      */               if (this.inferenceContext.solveBasic(list, graphInferenceSteps.steps)) {
/*      */                 Infer.this.checkWithinBounds(this.inferenceContext, this.warn);
/*      */                 continue label22;
/*      */               }
/*      */             }
/*      */             throw Infer.this.inferenceException.setMessage();
/*      */           }
/*      */         } catch (InferenceException inferenceException) {
/*      */           this.inferenceContext.rollback(list1);
/*      */           Infer.this.instantiateAsUninferredVars(list, this.inferenceContext);
/*      */           Infer.this.checkWithinBounds(this.inferenceContext, this.warn);
/*      */         }
/*      */         inferenceGraph.deleteNode(node);
/*      */       }
/*      */     }
/*      */
/*      */     class InferenceGraph {
/*      */       ArrayList<Node> nodes;
/*      */
/*      */       class Node extends GraphUtils.TarjanNode<ListBuffer<Type>> {
/*      */         EnumMap<DependencyKind, Set<Node>> deps;
/*      */
/*      */         Node(Type param3Type) {
/*      */           super(ListBuffer.of(param3Type));
/*      */           this.deps = new EnumMap<>(DependencyKind.class);
/*      */         }
/*      */
/*      */         public GraphUtils.DependencyKind[] getSupportedDependencyKinds() {
/*      */           return (GraphUtils.DependencyKind[]) DependencyKind.values();
/*      */         }
/*      */
/*      */         public String getDependencyName(GraphUtils.Node<ListBuffer<Type>> param3Node, GraphUtils.DependencyKind param3DependencyKind) {
/*      */           if (param3DependencyKind == DependencyKind.STUCK)
/*      */             return "";
/*      */           StringBuilder stringBuilder = new StringBuilder();
/*      */           String str = "";
/*      */           for (Type type : this.data) {
/*      */             Type.UndetVar undetVar = (Type.UndetVar) GraphSolver.this.inferenceContext.asUndetVar(type);
/*      */             for (Type type1 : undetVar.getBounds(Type.UndetVar.InferenceBound.values())) {
/*      */               if (type1.containsAny(List.from((Iterable)param3Node.data))) {
/*      */                 stringBuilder.append(str);
/*      */                 stringBuilder.append(type1);
/*      */                 str = ",";
/*      */               }
/*      */             }
/*      */           }
/*      */           return stringBuilder.toString();
/*      */         }
/*      */
/*      */         public Iterable<? extends Node> getAllDependencies() {
/*      */           return getDependencies(DependencyKind.values());
/*      */         }
/*      */
/*      */         public Iterable<? extends GraphUtils.TarjanNode<ListBuffer<Type>>> getDependenciesByKind(GraphUtils.DependencyKind param3DependencyKind) {
/*      */           return (Iterable)getDependencies(new DependencyKind[] { (DependencyKind)param3DependencyKind });
/*      */         }
/*      */
/*      */         protected Set<Node> getDependencies(DependencyKind... param3VarArgs) {
/*      */           LinkedHashSet<Node> linkedHashSet = new LinkedHashSet();
/*      */           for (DependencyKind dependencyKind : param3VarArgs) {
/*      */             Set set = this.deps.get(dependencyKind);
/*      */             if (set != null)
/*      */               linkedHashSet.addAll(set);
/*      */           }
/*      */           return linkedHashSet;
/*      */         }
/*      */
/*      */         protected void addDependency(DependencyKind param3DependencyKind, Node param3Node) {
/*      */           Set<Node> set = this.deps.get(param3DependencyKind);
/*      */           if (set == null) {
/*      */             set = new LinkedHashSet();
/*      */             this.deps.put(param3DependencyKind, set);
/*      */           }
/*      */           set.add(param3Node);
/*      */         }
/*      */
/*      */         protected void addDependencies(DependencyKind param3DependencyKind, Set<Node> param3Set) {
/*      */           for (Node node : param3Set)
/*      */             addDependency(param3DependencyKind, node);
/*      */         }
/*      */
/*      */         protected Set<DependencyKind> removeDependency(Node param3Node) {
/*      */           HashSet<DependencyKind> hashSet = new HashSet();
/*      */           for (DependencyKind dependencyKind : DependencyKind.values()) {
/*      */             Set set = this.deps.get(dependencyKind);
/*      */             if (set != null && set.remove(param3Node))
/*      */               hashSet.add(dependencyKind);
/*      */           }
/*      */           return hashSet;
/*      */         }
/*      */
/*      */         protected Set<Node> closure(DependencyKind... param3VarArgs) {
/*      */           boolean bool = true;
/*      */           HashSet<Node> hashSet = new HashSet();
/*      */           hashSet.add(this);
/*      */           while (bool) {
/*      */             bool = false;
/*      */             for (Node node : new HashSet(hashSet))
/*      */               bool = hashSet.addAll(node.getDependencies(param3VarArgs));
/*      */           }
/*      */           return hashSet;
/*      */         }
/*      */
/*      */         protected boolean isLeaf() {
/*      */           Set<Node> set = getDependencies(new DependencyKind[] { DependencyKind.BOUND, DependencyKind.STUCK });
/*      */           if (set.isEmpty())
/*      */             return true;
/*      */           for (Node node : set) {
/*      */             if (node != this)
/*      */               return false;
/*      */           }
/*      */           return true;
/*      */         }
/*      */
/*      */         protected void mergeWith(List<? extends Node> param3List) {
/*      */           for (Node node : param3List) {
/*      */             Assert.check((((ListBuffer)node.data).length() == 1), "Attempt to merge a compound node!");
/*      */             ((ListBuffer)this.data).appendList((ListBuffer)node.data);
/*      */             for (DependencyKind dependencyKind : DependencyKind.values()) {
/*      */               addDependencies(dependencyKind, node.getDependencies(new DependencyKind[] { dependencyKind }));
/*      */             }
/*      */           }
/*      */           EnumMap<DependencyKind, Object> enumMap = new EnumMap<>(DependencyKind.class);
/*      */           for (DependencyKind dependencyKind : DependencyKind.values()) {
/*      */             for (Node node : getDependencies(new DependencyKind[] { dependencyKind })) {
/*      */               Set<Node> set = (Set)enumMap.get(dependencyKind);
/*      */               if (set == null) {
/*      */                 set = new LinkedHashSet();
/*      */                 enumMap.put(dependencyKind, set);
/*      */               }
/*      */               if (((ListBuffer)this.data).contains(((ListBuffer)node.data).first())) {
/*      */                 set.add(this);
/*      */                 continue;
/*      */               }
/*      */               set.add(node);
/*      */             }
/*      */           }
/*      */           this.deps = (EnumMap)enumMap;
/*      */         }
/*      */
/*      */         private void graphChanged(Node param3Node1, Node param3Node2) {
/*      */           for (DependencyKind dependencyKind : removeDependency(param3Node1)) {
/*      */             if (param3Node2 != null)
/*      */               addDependency(dependencyKind, param3Node2);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */       InferenceGraph(Map<Type, Set<Type>> param2Map) {
/*      */         initNodes(param2Map);
/*      */       }
/*      */
/*      */       public Node findNode(Type param2Type) {
/*      */         for (Node node : this.nodes) {
/*      */           if (((ListBuffer)node.data).contains(param2Type))
/*      */             return node;
/*      */         }
/*      */         return null;
/*      */       }
/*      */
/*      */       public void deleteNode(Node param2Node) {
/*      */         Assert.check(this.nodes.contains(param2Node));
/*      */         this.nodes.remove(param2Node);
/*      */         notifyUpdate(param2Node, null);
/*      */       }
/*      */
/*      */       void notifyUpdate(Node param2Node1, Node param2Node2) {
/*      */         for (Node node : this.nodes)
/*      */           node.graphChanged(param2Node1, param2Node2);
/*      */       }
/*      */
/*      */       void initNodes(Map<Type, Set<Type>> param2Map) {
/*      */         this.nodes = new ArrayList<>();
/*      */         for (Type type : GraphSolver.this.inferenceContext.restvars())
/*      */           this.nodes.add(new Node(type));
/*      */         for (Node node : this.nodes) {
/*      */           Type type = (Type)((ListBuffer)node.data).first();
/*      */           Set set = param2Map.get(type);
/*      */           for (Node node1 : this.nodes) {
/*      */             Type type1 = (Type)((ListBuffer)node1.data).first();
/*      */             Type.UndetVar undetVar = (Type.UndetVar) GraphSolver.this.inferenceContext.asUndetVar(type);
/*      */             if (Type.containsAny(undetVar.getBounds(Type.UndetVar.InferenceBound.values()), List.of(type1)))
/*      */               node.addDependency(DependencyKind.BOUND, node1);
/*      */             if (set != null && set.contains(type1))
/*      */               node.addDependency(DependencyKind.STUCK, node1);
/*      */           }
/*      */         }
/*      */         ArrayList<Object> arrayList = new ArrayList();
/*      */         for (List list : GraphUtils.tarjan(this.nodes)) {
/*      */           if (list.length() > 1) {
/*      */             Node node = (Node)list.head;
/*      */             node.mergeWith(list.tail);
/*      */             for (Node node1 : list)
/*      */               notifyUpdate(node1, node);
/*      */           }
/*      */           arrayList.add(list.head);
/*      */         }
/*      */         this.nodes = (ArrayList)arrayList;
/*      */       }
/*      */
/*      */       String toDot() {
/*      */         StringBuilder stringBuilder = new StringBuilder();
/*      */         for (Type type : GraphSolver.this.inferenceContext.undetvars) {
/*      */           Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */           stringBuilder.append(String.format("var %s - upper bounds = %s, lower bounds = %s, eq bounds = %s\\n", new Object[] { undetVar.qtype, undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }), undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }), undetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ }) }));
/*      */         }
/*      */         return GraphUtils.toDot(this.nodes, "inferenceGraph" + hashCode(), stringBuilder.toString());
/*      */       }
/*      */     }
/*      */
/*      */     class Node extends GraphUtils.TarjanNode<ListBuffer<Type>> {
/*      */       EnumMap<DependencyKind, Set<Node>> deps;
/*      */
/*      */       Node(Type param2Type) {
/*      */         super(ListBuffer.of(param2Type));
/*      */         this.deps = new EnumMap<>(DependencyKind.class);
/*      */       }
/*      */
/*      */       public GraphUtils.DependencyKind[] getSupportedDependencyKinds() {
/*      */         return (GraphUtils.DependencyKind[]) DependencyKind.values();
/*      */       }
/*      */
/*      */       public String getDependencyName(GraphUtils.Node<ListBuffer<Type>> param2Node, GraphUtils.DependencyKind param2DependencyKind) {
/*      */         if (param2DependencyKind == DependencyKind.STUCK)
/*      */           return "";
/*      */         StringBuilder stringBuilder = new StringBuilder();
/*      */         String str = "";
/*      */         for (Type type : this.data) {
/*      */           Type.UndetVar undetVar = (Type.UndetVar) GraphSolver.this.inferenceContext.asUndetVar(type);
/*      */           for (Type type1 : undetVar.getBounds(Type.UndetVar.InferenceBound.values())) {
/*      */             if (type1.containsAny(List.from((Iterable)param2Node.data))) {
/*      */               stringBuilder.append(str);
/*      */               stringBuilder.append(type1);
/*      */               str = ",";
/*      */             }
/*      */           }
/*      */         }
/*      */         return stringBuilder.toString();
/*      */       }
/*      */
/*      */       public Iterable<? extends Node> getAllDependencies() {
/*      */         return getDependencies(DependencyKind.values());
/*      */       }
/*      */
/*      */       public Iterable<? extends GraphUtils.TarjanNode<ListBuffer<Type>>> getDependenciesByKind(GraphUtils.DependencyKind param2DependencyKind) {
/*      */         return (Iterable)getDependencies(new DependencyKind[] { (DependencyKind)param2DependencyKind });
/*      */       }
/*      */
/*      */       protected Set<Node> getDependencies(DependencyKind... param2VarArgs) {
/*      */         LinkedHashSet<Node> linkedHashSet = new LinkedHashSet();
/*      */         for (DependencyKind dependencyKind : param2VarArgs) {
/*      */           Set set = this.deps.get(dependencyKind);
/*      */           if (set != null)
/*      */             linkedHashSet.addAll(set);
/*      */         }
/*      */         return linkedHashSet;
/*      */       }
/*      */
/*      */       protected void addDependency(DependencyKind param2DependencyKind, Node param2Node) {
/*      */         Set<Node> set = this.deps.get(param2DependencyKind);
/*      */         if (set == null) {
/*      */           set = new LinkedHashSet();
/*      */           this.deps.put(param2DependencyKind, set);
/*      */         }
/*      */         set.add(param2Node);
/*      */       }
/*      */
/*      */       protected void addDependencies(DependencyKind param2DependencyKind, Set<Node> param2Set) {
/*      */         for (Node node : param2Set)
/*      */           addDependency(param2DependencyKind, node);
/*      */       }
/*      */
/*      */       protected Set<DependencyKind> removeDependency(Node param2Node) {
/*      */         HashSet<DependencyKind> hashSet = new HashSet();
/*      */         for (DependencyKind dependencyKind : DependencyKind.values()) {
/*      */           Set set = this.deps.get(dependencyKind);
/*      */           if (set != null && set.remove(param2Node))
/*      */             hashSet.add(dependencyKind);
/*      */         }
/*      */         return hashSet;
/*      */       }
/*      */
/*      */       protected Set<Node> closure(DependencyKind... param2VarArgs) {
/*      */         boolean bool = true;
/*      */         HashSet<Node> hashSet = new HashSet();
/*      */         hashSet.add(this);
/*      */         while (bool) {
/*      */           bool = false;
/*      */           for (Node node : new HashSet(hashSet))
/*      */             bool = hashSet.addAll(node.getDependencies(param2VarArgs));
/*      */         }
/*      */         return hashSet;
/*      */       }
/*      */
/*      */       protected boolean isLeaf() {
/*      */         Set<Node> set = getDependencies(new DependencyKind[] { DependencyKind.BOUND, DependencyKind.STUCK });
/*      */         if (set.isEmpty())
/*      */           return true;
/*      */         for (Node node : set) {
/*      */           if (node != this)
/*      */             return false;
/*      */         }
/*      */         return true;
/*      */       }
/*      */
/*      */       protected void mergeWith(List<? extends Node> param2List) {
/*      */         for (Node node : param2List) {
/*      */           Assert.check((((ListBuffer)node.data).length() == 1), "Attempt to merge a compound node!");
/*      */           ((ListBuffer)this.data).appendList((ListBuffer)node.data);
/*      */           for (DependencyKind dependencyKind : DependencyKind.values()) {
/*      */             addDependencies(dependencyKind, node.getDependencies(new DependencyKind[] { dependencyKind }));
/*      */           }
/*      */         }
/*      */         EnumMap<DependencyKind, Object> enumMap = new EnumMap<>(DependencyKind.class);
/*      */         for (DependencyKind dependencyKind : DependencyKind.values()) {
/*      */           for (Node node : getDependencies(new DependencyKind[] { dependencyKind })) {
/*      */             Set<Node> set = (Set)enumMap.get(dependencyKind);
/*      */             if (set == null) {
/*      */               set = new LinkedHashSet();
/*      */               enumMap.put(dependencyKind, set);
/*      */             }
/*      */             if (((ListBuffer)this.data).contains(((ListBuffer)node.data).first())) {
/*      */               set.add(this);
/*      */               continue;
/*      */             }
/*      */             set.add(node);
/*      */           }
/*      */         }
/*      */         this.deps = (EnumMap)enumMap;
/*      */       }
/*      */
/*      */       private void graphChanged(Node param2Node1, Node param2Node2) {
/*      */         for (DependencyKind dependencyKind : removeDependency(param2Node1)) {
/*      */           if (param2Node2 != null)
/*      */             addDependency(dependencyKind, param2Node2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class InferenceContext {
/*      */     List<Type> undetvars;
/*      */     List<Type> inferencevars;
/*      */     Map<FreeTypeListener, List<Type>> freeTypeListeners = new HashMap<>();
/*      */     List<FreeTypeListener> freetypeListeners = List.nil();
/*      */     Type.Mapping fromTypeVarFun;
/*      */     Map<JCTree, Type> captureTypeCache;
/*      */
/*      */     public InferenceContext(List<Type> param1List) {
/*      */       this.fromTypeVarFun = new Type.Mapping("fromTypeVarFunWithBounds") {
/*      */           public Type apply(Type param2Type) {
/*      */             if (param2Type.hasTag(TypeTag.TYPEVAR)) {
/*      */               Type.TypeVar typeVar = (Type.TypeVar)param2Type;
/*      */               if (typeVar.isCaptured())
/*      */                 return (Type)new Type.CapturedUndetVar((Type.CapturedType)typeVar, Infer.this.types);
/*      */               return (Type)new Type.UndetVar(typeVar, Infer.this.types);
/*      */             }
/*      */             return param2Type.map(this);
/*      */           }
/*      */         };
/*      */       this.captureTypeCache = new HashMap<>();
/*      */       this.undetvars = Type.map(param1List, this.fromTypeVarFun);
/*      */       this.inferencevars = param1List;
/*      */     }
/*      */
/*      */     void addVar(Type.TypeVar param1TypeVar) {
/*      */       this.undetvars = this.undetvars.prepend(this.fromTypeVarFun.apply((Type)param1TypeVar));
/*      */       this.inferencevars = this.inferencevars.prepend(param1TypeVar);
/*      */     }
/*      */
/*      */     List<Type> inferenceVars() {
/*      */       return this.inferencevars;
/*      */     }
/*      */
/*      */     List<Type> restvars() {
/*      */       return filterVars(new Filter<Type.UndetVar>() {
/*      */             public boolean accepts(Type.UndetVar param2UndetVar) {
/*      */               return (param2UndetVar.inst == null);
/*      */             }
/*      */           });
/*      */     }
/*      */
/*      */     List<Type> instvars() {
/*      */       return filterVars(new Filter<Type.UndetVar>() {
/*      */             public boolean accepts(Type.UndetVar param2UndetVar) {
/*      */               return (param2UndetVar.inst != null);
/*      */             }
/*      */           });
/*      */     }
/*      */
/*      */     final List<Type> boundedVars() {
/*      */       return filterVars(new Filter<Type.UndetVar>() {
/*      */             public boolean accepts(Type.UndetVar param2UndetVar) {
/*      */               return param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }).diff(param2UndetVar.getDeclaredBounds()).appendList(param2UndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER })).nonEmpty();
/*      */             }
/*      */           });
/*      */     }
/*      */
/*      */     private List<Type> filterVars(Filter<Type.UndetVar> param1Filter) {
/*      */       ListBuffer listBuffer = new ListBuffer();
/*      */       for (Type type : this.undetvars) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */         if (param1Filter.accepts(undetVar))
/*      */           listBuffer.append(undetVar.qtype);
/*      */       }
/*      */       return listBuffer.toList();
/*      */     }
/*      */
/*      */     final boolean free(Type param1Type) {
/*      */       return param1Type.containsAny(this.inferencevars);
/*      */     }
/*      */
/*      */     final boolean free(List<Type> param1List) {
/*      */       for (Type type : param1List) {
/*      */         if (free(type))
/*      */           return true;
/*      */       }
/*      */       return false;
/*      */     }
/*      */
/*      */     final List<Type> freeVarsIn(Type param1Type) {
/*      */       ListBuffer listBuffer = new ListBuffer();
/*      */       for (Type type : inferenceVars()) {
/*      */         if (param1Type.contains(type))
/*      */           listBuffer.add(type);
/*      */       }
/*      */       return listBuffer.toList();
/*      */     }
/*      */
/*      */     final List<Type> freeVarsIn(List<Type> param1List) {
/*      */       ListBuffer listBuffer1 = new ListBuffer();
/*      */       for (Type type : param1List)
/*      */         listBuffer1.appendList(freeVarsIn(type));
/*      */       ListBuffer listBuffer2 = new ListBuffer();
/*      */       for (Type type : listBuffer1) {
/*      */         if (!listBuffer2.contains(type))
/*      */           listBuffer2.add(type);
/*      */       }
/*      */       return listBuffer2.toList();
/*      */     }
/*      */
/*      */     final Type asUndetVar(Type param1Type) {
/*      */       return Infer.this.types.subst(param1Type, this.inferencevars, this.undetvars);
/*      */     }
/*      */
/*      */     final List<Type> asUndetVars(List<Type> param1List) {
/*      */       ListBuffer listBuffer = new ListBuffer();
/*      */       for (Type type : param1List)
/*      */         listBuffer.append(asUndetVar(type));
/*      */       return listBuffer.toList();
/*      */     }
/*      */
/*      */     List<Type> instTypes() {
/*      */       ListBuffer listBuffer = new ListBuffer();
/*      */       for (Type type : this.undetvars) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */         listBuffer.append((undetVar.inst != null) ? undetVar.inst : undetVar.qtype);
/*      */       }
/*      */       return listBuffer.toList();
/*      */     }
/*      */
/*      */     Type asInstType(Type param1Type) {
/*      */       return Infer.this.types.subst(param1Type, this.inferencevars, instTypes());
/*      */     }
/*      */
/*      */     List<Type> asInstTypes(List<Type> param1List) {
/*      */       ListBuffer listBuffer = new ListBuffer();
/*      */       for (Type type : param1List)
/*      */         listBuffer.append(asInstType(type));
/*      */       return listBuffer.toList();
/*      */     }
/*      */
/*      */     void addFreeTypeListener(List<Type> param1List, FreeTypeListener param1FreeTypeListener) {
/*      */       this.freeTypeListeners.put(param1FreeTypeListener, freeVarsIn(param1List));
/*      */     }
/*      */
/*      */     void notifyChange() {
/*      */       notifyChange(this.inferencevars.diff(restvars()));
/*      */     }
/*      */
/*      */     void notifyChange(List<Type> param1List) {
/*      */       InferenceException inferenceException = null;
/*      */       for (Map.Entry<?, ?> entry : (new HashMap<>(this.freeTypeListeners)).entrySet()) {
/*      */         if (!Type.containsAny((List)entry.getValue(), this.inferencevars.diff(param1List)))
/*      */           try {
/*      */             ((FreeTypeListener)entry.getKey()).typesInferred(this);
/*      */             this.freeTypeListeners.remove(entry.getKey());
/*      */           } catch (InferenceException inferenceException1) {
/*      */             if (inferenceException == null)
/*      */               inferenceException = inferenceException1;
/*      */           }
/*      */       }
/*      */       if (inferenceException != null)
/*      */         throw inferenceException;
/*      */     }
/*      */
/*      */     List<Type> save() {
/*      */       ListBuffer listBuffer = new ListBuffer();
/*      */       for (Type type : this.undetvars) {
/*      */         Type.UndetVar undetVar1 = (Type.UndetVar)type;
/*      */         Type.UndetVar undetVar2 = new Type.UndetVar((Type.TypeVar)undetVar1.qtype, Infer.this.types);
/*      */         for (Type.UndetVar.InferenceBound inferenceBound : Type.UndetVar.InferenceBound.values()) {
/*      */           for (Type type1 : undetVar1.getBounds(new Type.UndetVar.InferenceBound[] { inferenceBound }))
/*      */             undetVar2.addBound(inferenceBound, type1, Infer.this.types);
/*      */         }
/*      */         undetVar2.inst = undetVar1.inst;
/*      */         listBuffer.add(undetVar2);
/*      */       }
/*      */       return listBuffer.toList();
/*      */     }
/*      */
/*      */     void rollback(List<Type> param1List) {
/*      */       Assert.check((param1List != null && param1List.length() == this.undetvars.length()));
/*      */       for (Type type : this.undetvars) {
/*      */         Type.UndetVar undetVar1 = (Type.UndetVar)type;
/*      */         Type.UndetVar undetVar2 = (Type.UndetVar)param1List.head;
/*      */         for (Type.UndetVar.InferenceBound inferenceBound : Type.UndetVar.InferenceBound.values()) {
/*      */           undetVar1.setBounds(inferenceBound, undetVar2.getBounds(new Type.UndetVar.InferenceBound[] { inferenceBound }));
/*      */         }
/*      */         undetVar1.inst = undetVar2.inst;
/*      */         param1List = param1List.tail;
/*      */       }
/*      */     }
/*      */
/*      */     void dupTo(InferenceContext param1InferenceContext) {
/*      */       param1InferenceContext.inferencevars = param1InferenceContext.inferencevars.appendList(this.inferencevars.diff(param1InferenceContext.inferencevars));
/*      */       param1InferenceContext.undetvars = param1InferenceContext.undetvars.appendList(this.undetvars.diff(param1InferenceContext.undetvars));
/*      */       for (Type type : this.inferencevars) {
/*      */         param1InferenceContext.freeTypeListeners.put(new FreeTypeListener() {
/*      */               public void typesInferred(InferenceContext param2InferenceContext) {
/*      */                 InferenceContext.this.notifyChange();
/*      */               }
/*      */             },  List.of(type));
/*      */       }
/*      */     }
/*      */
/*      */     private void solve(GraphStrategy param1GraphStrategy, Warner param1Warner) {
/*      */       solve(param1GraphStrategy, new HashMap<>(), param1Warner);
/*      */     }
/*      */
/*      */     private void solve(GraphStrategy param1GraphStrategy, Map<Type, Set<Type>> param1Map, Warner param1Warner) {
/*      */       GraphSolver graphSolver = new GraphSolver(this, param1Map, param1Warner);
/*      */       graphSolver.solve(param1GraphStrategy);
/*      */     }
/*      */
/*      */     public void solve(Warner param1Warner) {
/*      */       solve(new LeafSolver() {
/*      */             public boolean done() {
/*      */               return InferenceContext.this.restvars().isEmpty();
/*      */             }
/*      */           },  param1Warner);
/*      */     }
/*      */
/*      */     public void solve(final List<Type> vars, Warner param1Warner) {
/*      */       solve(new BestLeafSolver(vars) {
/*      */             public boolean done() {
/*      */               return !InferenceContext.this.free(InferenceContext.this.asInstTypes(vars));
/*      */             }
/*      */           }param1Warner);
/*      */     }
/*      */
/*      */     public void solveAny(List<Type> param1List, Map<Type, Set<Type>> param1Map, Warner param1Warner) {
/*      */       solve(new BestLeafSolver(param1List.intersect(restvars())) {
/*      */             public boolean done() {
/*      */               return InferenceContext.this.instvars().intersect(this.varsToSolve).nonEmpty();
/*      */             }
/*      */           }param1Map, param1Warner);
/*      */     }
/*      */
/*      */     private boolean solveBasic(EnumSet<InferenceStep> param1EnumSet) {
/*      */       return solveBasic(this.inferencevars, param1EnumSet);
/*      */     }
/*      */
/*      */     private boolean solveBasic(List<Type> param1List, EnumSet<InferenceStep> param1EnumSet) {
/*      */       boolean bool = false;
/*      */       for (Type type : param1List.intersect(restvars())) {
/*      */         Type.UndetVar undetVar = (Type.UndetVar)asUndetVar(type);
/*      */         for (InferenceStep inferenceStep : param1EnumSet) {
/*      */           if (inferenceStep.accepts(undetVar, this)) {
/*      */             undetVar.inst = inferenceStep.solve(undetVar, this);
/*      */             bool = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       return bool;
/*      */     }
/*      */
/*      */     public void solveLegacy(boolean param1Boolean, Warner param1Warner, EnumSet<InferenceStep> param1EnumSet) {
/*      */       while (true) {
/*      */         boolean bool = !solveBasic(param1EnumSet) ? true : false;
/*      */         if (restvars().isEmpty() || param1Boolean)
/*      */           break;
/*      */         if (bool) {
/*      */           Infer.this.instantiateAsUninferredVars(restvars(), this);
/*      */           break;
/*      */         }
/*      */         for (Type type : this.undetvars) {
/*      */           Type.UndetVar undetVar = (Type.UndetVar)type;
/*      */           undetVar.substBounds(inferenceVars(), instTypes(), Infer.this.types);
/*      */         }
/*      */       }
/*      */       Infer.this.checkWithinBounds(this, param1Warner);
/*      */     }
/*      */
/*      */     private Infer infer() {
/*      */       return Infer.this;
/*      */     }
/*      */
/*      */     public String toString() {
/*      */       return "Inference vars: " + this.inferencevars + '\n' + "Undet vars: " + this.undetvars;
/*      */     }
/*      */
/*      */     Type cachedCapture(JCTree param1JCTree, Type param1Type, boolean param1Boolean) {
/*      */       Type type1 = this.captureTypeCache.get(param1JCTree);
/*      */       if (type1 != null)
/*      */         return type1;
/*      */       Type type2 = Infer.this.types.capture(param1Type);
/*      */       if (type2 != param1Type && !param1Boolean)
/*      */         this.captureTypeCache.put(param1JCTree, type2);
/*      */       return type2;
/*      */     }
/*      */   }
/*      */
/*      */   static interface FreeTypeListener {
/*      */     void typesInferred(InferenceContext param1InferenceContext);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Infer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
