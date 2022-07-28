/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.JavaFileObject;
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
/*     */ public class JCDiagnostic
/*     */   implements Diagnostic<JavaFileObject>
/*     */ {
/*     */   private final DiagnosticType type;
/*     */   private final DiagnosticSource source;
/*     */   private final DiagnosticPosition position;
/*     */   private final String key;
/*     */   protected final Object[] args;
/*     */   private final Set<DiagnosticFlag> flags;
/*     */   private final Lint.LintCategory lintCategory;
/*     */   private SourcePosition sourcePosition;
/*     */   private DiagnosticFormatter<JCDiagnostic> defaultFormatter;
/*     */   @Deprecated
/*     */   private static DiagnosticFormatter<JCDiagnostic> fragmentFormatter;
/*     */
/*     */   public static class Factory
/*     */   {
/*  53 */     protected static final Context.Key<Factory> diagnosticFactoryKey = new Context.Key<>();
/*     */
/*     */     DiagnosticFormatter<JCDiagnostic> formatter;
/*     */
/*     */     public static Factory instance(Context param1Context) {
/*  58 */       Factory factory = param1Context.<Factory>get(diagnosticFactoryKey);
/*  59 */       if (factory == null)
/*  60 */         factory = new Factory(param1Context);
/*  61 */       return factory;
/*     */     }
/*     */
/*     */
/*     */     final String prefix;
/*     */
/*     */     final Set<DiagnosticFlag> defaultErrorFlags;
/*     */
/*     */     protected Factory(Context param1Context) {
/*  70 */       this(JavacMessages.instance(param1Context), "compiler");
/*  71 */       param1Context.put(diagnosticFactoryKey, this);
/*     */
/*  73 */       final Options options = Options.instance(param1Context);
/*  74 */       initOptions(options);
/*  75 */       options.addListener(new Runnable() {
/*     */             public void run() {
/*  77 */               Factory.this.initOptions(options);
/*     */             }
/*     */           });
/*     */     }
/*     */
/*     */     private void initOptions(Options param1Options) {
/*  83 */       if (param1Options.isSet("onlySyntaxErrorsUnrecoverable")) {
/*  84 */         this.defaultErrorFlags.add(DiagnosticFlag.RECOVERABLE);
/*     */       }
/*     */     }
/*     */
/*     */     public Factory(JavacMessages param1JavacMessages, String param1String) {
/*  89 */       this.prefix = param1String;
/*  90 */       this.formatter = new BasicDiagnosticFormatter(param1JavacMessages);
/*  91 */       this.defaultErrorFlags = EnumSet.of(DiagnosticFlag.MANDATORY);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic error(DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 103 */       return create(DiagnosticType.ERROR, null, this.defaultErrorFlags, param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
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
/*     */     public JCDiagnostic mandatoryWarning(DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 116 */       return create(DiagnosticType.WARNING, null, EnumSet.of(DiagnosticFlag.MANDATORY), param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
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
/*     */     public JCDiagnostic mandatoryWarning(Lint.LintCategory param1LintCategory, DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 131 */       return create(DiagnosticType.WARNING, param1LintCategory, EnumSet.of(DiagnosticFlag.MANDATORY), param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic warning(Lint.LintCategory param1LintCategory, String param1String, Object... param1VarArgs) {
/* 143 */       return create(DiagnosticType.WARNING, param1LintCategory, EnumSet.noneOf(DiagnosticFlag.class), null, null, param1String, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic warning(DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 155 */       return create(DiagnosticType.WARNING, null, EnumSet.noneOf(DiagnosticFlag.class), param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
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
/*     */     public JCDiagnostic warning(Lint.LintCategory param1LintCategory, DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 169 */       return create(DiagnosticType.WARNING, param1LintCategory, EnumSet.noneOf(DiagnosticFlag.class), param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic mandatoryNote(DiagnosticSource param1DiagnosticSource, String param1String, Object... param1VarArgs) {
/* 179 */       return create(DiagnosticType.NOTE, null, EnumSet.of(DiagnosticFlag.MANDATORY), param1DiagnosticSource, null, param1String, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic note(String param1String, Object... param1VarArgs) {
/* 188 */       return create(DiagnosticType.NOTE, null, EnumSet.noneOf(DiagnosticFlag.class), null, null, param1String, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic note(DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 200 */       return create(DiagnosticType.NOTE, null, EnumSet.noneOf(DiagnosticFlag.class), param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public JCDiagnostic fragment(String param1String, Object... param1VarArgs) {
/* 209 */       return create(DiagnosticType.FRAGMENT, null, EnumSet.noneOf(DiagnosticFlag.class), null, null, param1String, param1VarArgs);
/*     */     }
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
/*     */     public JCDiagnostic create(DiagnosticType param1DiagnosticType, DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 223 */       return create(param1DiagnosticType, null, EnumSet.noneOf(DiagnosticFlag.class), param1DiagnosticSource, param1DiagnosticPosition, param1String, param1VarArgs);
/*     */     }
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
/*     */     public JCDiagnostic create(DiagnosticType param1DiagnosticType, Lint.LintCategory param1LintCategory, Set<DiagnosticFlag> param1Set, DiagnosticSource param1DiagnosticSource, DiagnosticPosition param1DiagnosticPosition, String param1String, Object... param1VarArgs) {
/* 238 */       return new JCDiagnostic(this.formatter, param1DiagnosticType, param1LintCategory, param1Set, param1DiagnosticSource, param1DiagnosticPosition, qualify(param1DiagnosticType, param1String), param1VarArgs);
/*     */     }
/*     */
/*     */     protected String qualify(DiagnosticType param1DiagnosticType, String param1String) {
/* 242 */       return this.prefix + "." + param1DiagnosticType.key + "." + param1String;
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
/*     */   @Deprecated
/*     */   public static JCDiagnostic fragment(String paramString, Object... paramVarArgs) {
/* 256 */     return new JCDiagnostic(getFragmentFormatter(), DiagnosticType.FRAGMENT, null,
/*     */
/*     */
/* 259 */         EnumSet.noneOf(DiagnosticFlag.class), null, null, "compiler." + DiagnosticType.FRAGMENT.key + "." + paramString, paramVarArgs);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @Deprecated
/*     */   public static DiagnosticFormatter<JCDiagnostic> getFragmentFormatter() {
/* 268 */     if (fragmentFormatter == null) {
/* 269 */       fragmentFormatter = new BasicDiagnosticFormatter(JavacMessages.getDefaultMessages());
/*     */     }
/* 271 */     return fragmentFormatter;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public enum DiagnosticType
/*     */   {
/* 279 */     FRAGMENT("misc"),
/*     */
/* 281 */     NOTE("note"),
/*     */
/* 283 */     WARNING("warn"),
/*     */
/* 285 */     ERROR("err");
/*     */
/*     */
/*     */     final String key;
/*     */
/*     */
/*     */
/*     */     DiagnosticType(String param1String1) {
/* 293 */       this.key = param1String1;
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
/*     */   public static class SimpleDiagnosticPosition
/*     */     implements DiagnosticPosition
/*     */   {
/*     */     private final int pos;
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
/*     */     public SimpleDiagnosticPosition(int param1Int) {
/* 324 */       this.pos = param1Int;
/*     */     }
/*     */
/*     */     public JCTree getTree() {
/* 328 */       return null;
/*     */     }
/*     */
/*     */     public int getStartPosition() {
/* 332 */       return this.pos;
/*     */     }
/*     */
/*     */     public int getPreferredPosition() {
/* 336 */       return this.pos;
/*     */     }
/*     */
/*     */     public int getEndPosition(EndPosTable param1EndPosTable) {
/* 340 */       return this.pos;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public enum DiagnosticFlag
/*     */   {
/* 347 */     MANDATORY,
/* 348 */     RESOLVE_ERROR,
/* 349 */     SYNTAX,
/* 350 */     RECOVERABLE,
/* 351 */     NON_DEFERRABLE,
/* 352 */     COMPRESSED;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   class SourcePosition
/*     */   {
/*     */     private final int line;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final int column;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     SourcePosition() {
/* 375 */       boolean bool = (JCDiagnostic.this.position == null) ? true : JCDiagnostic.this.position.getPreferredPosition();
/* 376 */       if (bool == -1 || JCDiagnostic.this.source == null) {
/* 377 */         this.line = this.column = -1;
/*     */       } else {
/* 379 */         this.line = JCDiagnostic.this.source.getLineNumber(bool);
/* 380 */         this.column = JCDiagnostic.this.source.getColumnNumber(bool, true);
/*     */       }
/*     */     }
/*     */
/*     */     public int getLineNumber() {
/* 385 */       return this.line;
/*     */     }
/*     */
/*     */     public int getColumnNumber() {
/* 389 */       return this.column;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected JCDiagnostic(DiagnosticFormatter<JCDiagnostic> paramDiagnosticFormatter, DiagnosticType paramDiagnosticType, Lint.LintCategory paramLintCategory, Set<DiagnosticFlag> paramSet, DiagnosticSource paramDiagnosticSource, DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 411 */     if (paramDiagnosticSource == null && paramDiagnosticPosition != null && paramDiagnosticPosition.getPreferredPosition() != -1) {
/* 412 */       throw new IllegalArgumentException();
/*     */     }
/* 414 */     this.defaultFormatter = paramDiagnosticFormatter;
/* 415 */     this.type = paramDiagnosticType;
/* 416 */     this.lintCategory = paramLintCategory;
/* 417 */     this.flags = paramSet;
/* 418 */     this.source = paramDiagnosticSource;
/* 419 */     this.position = paramDiagnosticPosition;
/* 420 */     this.key = paramString;
/* 421 */     this.args = paramVarArgs;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public DiagnosticType getType() {
/* 429 */     return this.type;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<JCDiagnostic> getSubdiagnostics() {
/* 437 */     return List.nil();
/*     */   }
/*     */
/*     */   public boolean isMultiline() {
/* 441 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isMandatory() {
/* 449 */     return this.flags.contains(DiagnosticFlag.MANDATORY);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean hasLintCategory() {
/* 456 */     return (this.lintCategory != null);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public Lint.LintCategory getLintCategory() {
/* 463 */     return this.lintCategory;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public JavaFileObject getSource() {
/* 471 */     if (this.source == null) {
/* 472 */       return null;
/*     */     }
/* 474 */     return this.source.getFile();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public DiagnosticSource getDiagnosticSource() {
/* 482 */     return this.source;
/*     */   }
/*     */
/*     */   protected int getIntStartPosition() {
/* 486 */     return (this.position == null) ? -1 : this.position.getStartPosition();
/*     */   }
/*     */
/*     */   protected int getIntPosition() {
/* 490 */     return (this.position == null) ? -1 : this.position.getPreferredPosition();
/*     */   }
/*     */
/*     */   protected int getIntEndPosition() {
/* 494 */     return (this.position == null) ? -1 : this.position.getEndPosition(this.source.getEndPosTable());
/*     */   }
/*     */
/*     */   public long getStartPosition() {
/* 498 */     return getIntStartPosition();
/*     */   }
/*     */
/*     */   public long getPosition() {
/* 502 */     return getIntPosition();
/*     */   }
/*     */
/*     */   public long getEndPosition() {
/* 506 */     return getIntEndPosition();
/*     */   }
/*     */
/*     */   public DiagnosticPosition getDiagnosticPosition() {
/* 510 */     return this.position;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public long getLineNumber() {
/* 518 */     if (this.sourcePosition == null) {
/* 519 */       this.sourcePosition = new SourcePosition();
/*     */     }
/* 521 */     return this.sourcePosition.getLineNumber();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public long getColumnNumber() {
/* 529 */     if (this.sourcePosition == null) {
/* 530 */       this.sourcePosition = new SourcePosition();
/*     */     }
/* 532 */     return this.sourcePosition.getColumnNumber();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Object[] getArgs() {
/* 540 */     return this.args;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String getPrefix() {
/* 548 */     return getPrefix(this.type);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String getPrefix(DiagnosticType paramDiagnosticType) {
/* 556 */     return this.defaultFormatter.formatKind(this, Locale.getDefault());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String toString() {
/* 564 */     return this.defaultFormatter.format(this, Locale.getDefault());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Kind getKind() {
/* 574 */     switch (this.type) {
/*     */       case SYNTAX:
/* 576 */         return Kind.NOTE;
/*     */       case RESOLVE_ERROR:
/* 578 */         return this.flags.contains(DiagnosticFlag.MANDATORY) ? Kind.MANDATORY_WARNING : Kind.WARNING;
/*     */
/*     */
/*     */       case null:
/* 582 */         return Kind.ERROR;
/*     */     }
/* 584 */     return Kind.OTHER;
/*     */   }
/*     */
/*     */
/*     */   public String getCode() {
/* 589 */     return this.key;
/*     */   }
/*     */
/*     */   public String getMessage(Locale paramLocale) {
/* 593 */     return this.defaultFormatter.formatMessage(this, paramLocale);
/*     */   }
/*     */
/*     */   public void setFlag(DiagnosticFlag paramDiagnosticFlag) {
/* 597 */     this.flags.add(paramDiagnosticFlag);
/*     */
/* 599 */     if (this.type == DiagnosticType.ERROR) {
/* 600 */       switch (paramDiagnosticFlag) {
/*     */         case SYNTAX:
/* 602 */           this.flags.remove(DiagnosticFlag.RECOVERABLE);
/*     */           break;
/*     */         case RESOLVE_ERROR:
/* 605 */           this.flags.add(DiagnosticFlag.RECOVERABLE);
/*     */           break;
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public boolean isFlagSet(DiagnosticFlag paramDiagnosticFlag) {
/* 612 */     return this.flags.contains(paramDiagnosticFlag);
/*     */   }
/*     */
/*     */   public static class MultilineDiagnostic
/*     */     extends JCDiagnostic {
/*     */     private final List<JCDiagnostic> subdiagnostics;
/*     */
/*     */     public MultilineDiagnostic(JCDiagnostic param1JCDiagnostic, List<JCDiagnostic> param1List) {
/* 620 */       super(param1JCDiagnostic.defaultFormatter, param1JCDiagnostic
/* 621 */           .getType(), param1JCDiagnostic
/* 622 */           .getLintCategory(), param1JCDiagnostic
/* 623 */           .flags, param1JCDiagnostic
/* 624 */           .getDiagnosticSource(), param1JCDiagnostic
/* 625 */           .position, param1JCDiagnostic
/* 626 */           .getCode(), param1JCDiagnostic
/* 627 */           .getArgs());
/* 628 */       this.subdiagnostics = param1List;
/*     */     }
/*     */
/*     */
/*     */     public List<JCDiagnostic> getSubdiagnostics() {
/* 633 */       return this.subdiagnostics;
/*     */     }
/*     */
/*     */
/*     */     public boolean isMultiline() {
/* 638 */       return true;
/*     */     }
/*     */   }
/*     */
/*     */   public static interface DiagnosticPosition {
/*     */     JCTree getTree();
/*     */
/*     */     int getStartPosition();
/*     */
/*     */     int getPreferredPosition();
/*     */
/*     */     int getEndPosition(EndPosTable param1EndPosTable);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\JCDiagnostic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
