/*     */ package com.sun.tools.javac.code;
/*     */
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class Lint
/*     */ {
/*  47 */   protected static final Context.Key<Lint> lintKey = new Context.Key();
/*     */   private final AugmentVisitor augmentor;
/*     */
/*     */   public static Lint instance(Context paramContext) {
/*  51 */     Lint lint = (Lint)paramContext.get(lintKey);
/*  52 */     if (lint == null)
/*  53 */       lint = new Lint(paramContext);
/*  54 */     return lint;
/*     */   }
/*     */
/*     */
/*     */   private final EnumSet<LintCategory> values;
/*     */   private final EnumSet<LintCategory> suppressedValues;
/*     */
/*     */   public Lint augment(Attribute.Compound paramCompound) {
/*  62 */     return this.augmentor.augment(this, paramCompound);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Lint augment(Symbol paramSymbol) {
/*  71 */     Lint lint = this.augmentor.augment(this, paramSymbol.getDeclarationAttributes());
/*  72 */     if (paramSymbol.isDeprecated()) {
/*  73 */       if (lint == this)
/*  74 */         lint = new Lint(this);
/*  75 */       lint.values.remove(LintCategory.DEPRECATION);
/*  76 */       lint.suppressedValues.add(LintCategory.DEPRECATION);
/*     */     }
/*  78 */     return lint;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  86 */   private static final Map<String, LintCategory> map = new ConcurrentHashMap<>(20);
/*     */
/*     */
/*     */
/*     */   protected Lint(Context paramContext) {
/*  91 */     Options options = Options.instance(paramContext);
/*  92 */     this.values = EnumSet.noneOf(LintCategory.class);
/*  93 */     for (Map.Entry<String, LintCategory> entry : map.entrySet()) {
/*  94 */       if (options.lint((String)entry.getKey())) {
/*  95 */         this.values.add((LintCategory)entry.getValue());
/*     */       }
/*     */     }
/*  98 */     this.suppressedValues = EnumSet.noneOf(LintCategory.class);
/*     */
/* 100 */     paramContext.put(lintKey, this);
/* 101 */     this.augmentor = new AugmentVisitor(paramContext);
/*     */   }
/*     */
/*     */   protected Lint(Lint paramLint) {
/* 105 */     this.augmentor = paramLint.augmentor;
/* 106 */     this.values = paramLint.values.clone();
/* 107 */     this.suppressedValues = paramLint.suppressedValues.clone();
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/* 112 */     return "Lint:[values" + this.values + " suppressedValues" + this.suppressedValues + "]";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public enum LintCategory
/*     */   {
/* 124 */     AUXILIARYCLASS("auxiliaryclass"),
/*     */
/*     */
/*     */
/*     */
/* 129 */     CAST("cast"),
/*     */
/*     */
/*     */
/*     */
/* 134 */     CLASSFILE("classfile"),
/*     */
/*     */
/*     */
/*     */
/* 139 */     DEPRECATION("deprecation"),
/*     */
/*     */
/*     */
/*     */
/*     */
/* 145 */     DEP_ANN("dep-ann"),
/*     */
/*     */
/*     */
/*     */
/* 150 */     DIVZERO("divzero"),
/*     */
/*     */
/*     */
/*     */
/* 155 */     EMPTY("empty"),
/*     */
/*     */
/*     */
/*     */
/* 160 */     FALLTHROUGH("fallthrough"),
/*     */
/*     */
/*     */
/*     */
/* 165 */     FINALLY("finally"),
/*     */
/*     */
/*     */
/*     */
/* 170 */     OPTIONS("options"),
/*     */
/*     */
/*     */
/*     */
/* 175 */     OVERLOADS("overloads"),
/*     */
/*     */
/*     */
/*     */
/* 180 */     OVERRIDES("overrides"),
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 187 */     PATH("path"),
/*     */
/*     */
/*     */
/*     */
/* 192 */     PROCESSING("processing"),
/*     */
/*     */
/*     */
/*     */
/* 197 */     RAW("rawtypes"),
/*     */
/*     */
/*     */
/*     */
/* 202 */     SERIAL("serial"),
/*     */
/*     */
/*     */
/*     */
/* 207 */     STATIC("static"),
/*     */
/*     */
/*     */
/*     */
/* 212 */     SUNAPI("sunapi", true),
/*     */
/*     */
/*     */
/*     */
/* 217 */     TRY("try"),
/*     */
/*     */
/*     */
/*     */
/* 222 */     UNCHECKED("unchecked"),
/*     */
/*     */
/*     */
/*     */
/* 227 */     VARARGS("varargs");
/*     */
/*     */     public final String option;
/*     */
/*     */     public final boolean hidden;
/*     */
/*     */     LintCategory(String param1String1, boolean param1Boolean) {
/* 234 */       this.option = param1String1;
/* 235 */       this.hidden = param1Boolean;
/* 236 */       Lint.map.put(param1String1, this);
/*     */     }
/*     */
/*     */     static LintCategory get(String param1String) {
/* 240 */       return (LintCategory)Lint.map.get(param1String);
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
/*     */   public boolean isEnabled(LintCategory paramLintCategory) {
/* 253 */     return this.values.contains(paramLintCategory);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isSuppressed(LintCategory paramLintCategory) {
/* 263 */     return this.suppressedValues.contains(paramLintCategory);
/*     */   }
/*     */
/*     */   protected static class AugmentVisitor
/*     */     implements Attribute.Visitor
/*     */   {
/*     */     private final Context context;
/*     */     private Symtab syms;
/*     */     private Lint parent;
/*     */     private Lint lint;
/*     */
/*     */     AugmentVisitor(Context param1Context) {
/* 275 */       this.context = param1Context;
/*     */     }
/*     */
/*     */     Lint augment(Lint param1Lint, Attribute.Compound param1Compound) {
/* 279 */       initSyms();
/* 280 */       this.parent = param1Lint;
/* 281 */       this.lint = null;
/* 282 */       param1Compound.accept(this);
/* 283 */       return (this.lint == null) ? param1Lint : this.lint;
/*     */     }
/*     */
/*     */     Lint augment(Lint param1Lint, List<Attribute.Compound> param1List) {
/* 287 */       initSyms();
/* 288 */       this.parent = param1Lint;
/* 289 */       this.lint = null;
/* 290 */       for (Attribute.Compound compound : param1List) {
/* 291 */         compound.accept(this);
/*     */       }
/* 293 */       return (this.lint == null) ? param1Lint : this.lint;
/*     */     }
/*     */
/*     */     private void initSyms() {
/* 297 */       if (this.syms == null)
/* 298 */         this.syms = Symtab.instance(this.context);
/*     */     }
/*     */
/*     */     private void suppress(LintCategory param1LintCategory) {
/* 302 */       if (this.lint == null)
/* 303 */         this.lint = new Lint(this.parent);
/* 304 */       this.lint.suppressedValues.add(param1LintCategory);
/* 305 */       this.lint.values.remove(param1LintCategory);
/*     */     }
/*     */
/*     */     public void visitConstant(Attribute.Constant param1Constant) {
/* 309 */       if (param1Constant.type.tsym == this.syms.stringType.tsym) {
/* 310 */         LintCategory lintCategory = LintCategory.get((String)param1Constant.value);
/* 311 */         if (lintCategory != null) {
/* 312 */           suppress(lintCategory);
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void visitClass(Attribute.Class param1Class) {}
/*     */
/*     */
/*     */     public void visitCompound(Attribute.Compound param1Compound) {
/* 323 */       if (param1Compound.type.tsym == this.syms.suppressWarningsType.tsym) {
/* 324 */         List<Pair<Symbol.MethodSymbol, Attribute>> list = param1Compound.values;
/* 325 */         for (; list.nonEmpty(); list = list.tail) {
/* 326 */           Pair pair = (Pair)list.head;
/* 327 */           if (((Symbol.MethodSymbol)pair.fst).name.toString().equals("value")) {
/* 328 */             ((Attribute)pair.snd).accept(this);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     public void visitArray(Attribute.Array param1Array) {
/* 335 */       for (Attribute attribute : param1Array.values)
/* 336 */         attribute.accept(this);
/*     */     }
/*     */
/*     */     public void visitEnum(Attribute.Enum param1Enum) {}
/*     */
/*     */     public void visitError(Attribute.Error param1Error) {}
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Lint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
