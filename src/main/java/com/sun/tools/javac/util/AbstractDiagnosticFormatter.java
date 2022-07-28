/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Printer;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.file.BaseFileObject;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.Pretty;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public abstract class AbstractDiagnosticFormatter
/*     */   implements DiagnosticFormatter<JCDiagnostic>
/*     */ {
/*     */   protected JavacMessages messages;
/*     */   private SimpleConfiguration config;
/*  85 */   protected int depth = 0;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  92 */   private List<Type> allCaptured = List.nil();
/*     */
/*     */
/*     */
/*     */
/*     */   protected Printer printer;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String formatKind(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 104 */     switch (paramJCDiagnostic.getType()) { case PARENS:
/* 105 */         return "";
/* 106 */       case LAMBDA: return localize(paramLocale, "compiler.note.note", new Object[0]);
/* 107 */       case REFERENCE: return localize(paramLocale, "compiler.warn.warning", new Object[0]);
/* 108 */       case CONDEXPR: return localize(paramLocale, "compiler.err.error", new Object[0]); }
/*     */
/* 110 */     throw new AssertionError("Unknown diagnostic type: " + paramJCDiagnostic.getType());
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String format(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 116 */     this.allCaptured = List.nil();
/* 117 */     return formatDiagnostic(paramJCDiagnostic, paramLocale);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String formatPosition(JCDiagnostic paramJCDiagnostic, PositionKind paramPositionKind, Locale paramLocale) {
/* 123 */     Assert.check((paramJCDiagnostic.getPosition() != -1L));
/* 124 */     return String.valueOf(getPosition(paramJCDiagnostic, paramPositionKind));
/*     */   }
/*     */
/*     */   private long getPosition(JCDiagnostic paramJCDiagnostic, PositionKind paramPositionKind) {
/* 128 */     switch (paramPositionKind) { case PARENS:
/* 129 */         return paramJCDiagnostic.getIntStartPosition();
/* 130 */       case LAMBDA: return paramJCDiagnostic.getIntEndPosition();
/* 131 */       case REFERENCE: return paramJCDiagnostic.getLineNumber();
/* 132 */       case CONDEXPR: return paramJCDiagnostic.getColumnNumber();
/* 133 */       case null: return paramJCDiagnostic.getIntPosition(); }
/*     */
/* 135 */     throw new AssertionError("Unknown diagnostic position: " + paramPositionKind);
/*     */   }
/*     */
/*     */
/*     */   public String formatSource(JCDiagnostic paramJCDiagnostic, boolean paramBoolean, Locale paramLocale) {
/* 140 */     JavaFileObject javaFileObject = paramJCDiagnostic.getSource();
/* 141 */     if (javaFileObject == null)
/* 142 */       throw new IllegalArgumentException();
/* 143 */     if (paramBoolean)
/* 144 */       return javaFileObject.getName();
/* 145 */     if (javaFileObject instanceof BaseFileObject) {
/* 146 */       return ((BaseFileObject)javaFileObject).getShortName();
/*     */     }
/* 148 */     return BaseFileObject.getSimpleName(javaFileObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected Collection<String> formatArguments(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 159 */     ListBuffer<String> listBuffer = new ListBuffer();
/* 160 */     for (Object object : paramJCDiagnostic.getArgs()) {
/* 161 */       listBuffer.append(formatArgument(paramJCDiagnostic, object, paramLocale));
/*     */     }
/* 163 */     return listBuffer.toList();
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
/*     */   protected String formatArgument(JCDiagnostic paramJCDiagnostic, Object paramObject, Locale paramLocale) {
/* 175 */     if (paramObject instanceof JCDiagnostic) {
/* 176 */       String str = null;
/* 177 */       this.depth++;
/*     */       try {
/* 179 */         str = formatMessage((JCDiagnostic)paramObject, paramLocale);
/*     */       } finally {
/*     */
/* 182 */         this.depth--;
/*     */       }
/* 184 */       return str;
/*     */     }
/* 186 */     if (paramObject instanceof JCTree.JCExpression) {
/* 187 */       return expr2String((JCTree.JCExpression)paramObject);
/*     */     }
/* 189 */     if (paramObject instanceof Iterable) {
/* 190 */       return formatIterable(paramJCDiagnostic, (Iterable)paramObject, paramLocale);
/*     */     }
/* 192 */     if (paramObject instanceof Type) {
/* 193 */       return this.printer.visit((Type)paramObject, paramLocale);
/*     */     }
/* 195 */     if (paramObject instanceof Symbol) {
/* 196 */       return this.printer.visit((Symbol)paramObject, paramLocale);
/*     */     }
/* 198 */     if (paramObject instanceof JavaFileObject) {
/* 199 */       return ((JavaFileObject)paramObject).getName();
/*     */     }
/* 201 */     if (paramObject instanceof Profile) {
/* 202 */       return ((Profile)paramObject).name;
/*     */     }
/* 204 */     if (paramObject instanceof Formattable) {
/* 205 */       return ((Formattable)paramObject).toString(paramLocale, this.messages);
/*     */     }
/*     */
/* 208 */     return String.valueOf(paramObject);
/*     */   }
/*     */
/*     */
/*     */   private String expr2String(JCTree.JCExpression paramJCExpression) {
/* 213 */     switch (paramJCExpression.getTag()) {
/*     */       case PARENS:
/* 215 */         return expr2String(((JCTree.JCParens)paramJCExpression).expr);
/*     */       case LAMBDA:
/*     */       case REFERENCE:
/*     */       case CONDEXPR:
/* 219 */         return Pretty.toSimpleString((JCTree)paramJCExpression);
/*     */     }
/* 221 */     Assert.error("unexpected tree kind " + paramJCExpression.getKind());
/* 222 */     return null;
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
/*     */   protected String formatIterable(JCDiagnostic paramJCDiagnostic, Iterable<?> paramIterable, Locale paramLocale) {
/* 235 */     StringBuilder stringBuilder = new StringBuilder();
/* 236 */     String str = "";
/* 237 */     for (Object object : paramIterable) {
/* 238 */       stringBuilder.append(str);
/* 239 */       stringBuilder.append(formatArgument(paramJCDiagnostic, object, paramLocale));
/* 240 */       str = ",";
/*     */     }
/* 242 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected List<String> formatSubdiagnostics(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 253 */     List<?> list = List.nil();
/* 254 */     int i = this.config.getMultilineLimit(Configuration.MultilineLimit.DEPTH);
/* 255 */     if (i == -1 || this.depth < i) {
/* 256 */       this.depth++;
/*     */       try {
/* 258 */         int j = this.config.getMultilineLimit(Configuration.MultilineLimit.LENGTH);
/* 259 */         byte b = 0;
/* 260 */         for (JCDiagnostic jCDiagnostic : paramJCDiagnostic.getSubdiagnostics()) {
/* 261 */           if (j == -1 || b < j) {
/* 262 */             list = list.append(formatSubdiagnostic(paramJCDiagnostic, jCDiagnostic, paramLocale));
/* 263 */             b++;
/*     */           }
/*     */
/*     */         }
/*     */
/*     */       } finally {
/*     */
/* 270 */         this.depth--;
/*     */       }
/*     */     }
/* 273 */     return (List)list;
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
/*     */   protected String formatSubdiagnostic(JCDiagnostic paramJCDiagnostic1, JCDiagnostic paramJCDiagnostic2, Locale paramLocale) {
/* 285 */     return formatMessage(paramJCDiagnostic2, paramLocale);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected String formatSourceLine(JCDiagnostic paramJCDiagnostic, int paramInt) {
/* 292 */     StringBuilder stringBuilder = new StringBuilder();
/* 293 */     DiagnosticSource diagnosticSource = paramJCDiagnostic.getDiagnosticSource();
/* 294 */     int i = paramJCDiagnostic.getIntPosition();
/* 295 */     if (paramJCDiagnostic.getIntPosition() == -1)
/* 296 */       throw new AssertionError();
/* 297 */     String str = (diagnosticSource == null) ? null : diagnosticSource.getLine(i);
/* 298 */     if (str == null)
/* 299 */       return "";
/* 300 */     stringBuilder.append(indent(str, paramInt));
/* 301 */     int j = diagnosticSource.getColumnNumber(i, false);
/* 302 */     if (this.config.isCaretEnabled()) {
/* 303 */       stringBuilder.append("\n");
/* 304 */       for (byte b = 0; b < j - 1; b++) {
/* 305 */         stringBuilder.append((str.charAt(b) == '\t') ? "\t" : " ");
/*     */       }
/* 307 */       stringBuilder.append(indent("^", paramInt));
/*     */     }
/* 309 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */   protected String formatLintCategory(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 313 */     Lint.LintCategory lintCategory = paramJCDiagnostic.getLintCategory();
/* 314 */     if (lintCategory == null)
/* 315 */       return "";
/* 316 */     return localize(paramLocale, "compiler.warn.lintOption", new Object[] { lintCategory.option });
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
/*     */   protected String localize(Locale paramLocale, String paramString, Object... paramVarArgs) {
/* 328 */     return this.messages.getLocalizedString(paramLocale, paramString, paramVarArgs);
/*     */   }
/*     */
/*     */   public boolean displaySource(JCDiagnostic paramJCDiagnostic) {
/* 332 */     return (this.config.getVisible().contains(Configuration.DiagnosticPart.SOURCE) && paramJCDiagnostic
/* 333 */       .getType() != JCDiagnostic.DiagnosticType.FRAGMENT && paramJCDiagnostic
/* 334 */       .getIntPosition() != -1);
/*     */   }
/*     */
/*     */   public boolean isRaw() {
/* 338 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected String indentString(int paramInt) {
/* 349 */     String str = "                        ";
/* 350 */     if (paramInt <= str.length()) {
/* 351 */       return str.substring(0, paramInt);
/*     */     }
/* 353 */     StringBuilder stringBuilder = new StringBuilder();
/* 354 */     for (byte b = 0; b < paramInt; b++)
/* 355 */       stringBuilder.append(" ");
/* 356 */     return stringBuilder.toString();
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
/*     */   protected String indent(String paramString, int paramInt) {
/* 370 */     String str1 = indentString(paramInt);
/* 371 */     StringBuilder stringBuilder = new StringBuilder();
/* 372 */     String str2 = "";
/* 373 */     for (String str : paramString.split("\n")) {
/* 374 */       stringBuilder.append(str2);
/* 375 */       stringBuilder.append(str1 + str);
/* 376 */       str2 = "\n";
/*     */     }
/* 378 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */   public SimpleConfiguration getConfiguration() {
/* 382 */     return this.config;
/*     */   }
/*     */
/*     */   public static class SimpleConfiguration
/*     */     implements Configuration {
/*     */     protected Map<MultilineLimit, Integer> multilineLimits;
/*     */     protected EnumSet<DiagnosticPart> visibleParts;
/*     */     protected boolean caretEnabled;
/*     */
/*     */     public SimpleConfiguration(Set<DiagnosticPart> param1Set) {
/* 392 */       this.multilineLimits = new HashMap<>();
/* 393 */       setVisible(param1Set);
/* 394 */       setMultilineLimit(MultilineLimit.DEPTH, -1);
/* 395 */       setMultilineLimit(MultilineLimit.LENGTH, -1);
/* 396 */       setCaretEnabled(true);
/*     */     }
/*     */
/*     */
/*     */     public SimpleConfiguration(Options param1Options, Set<DiagnosticPart> param1Set) {
/* 401 */       this(param1Set);
/* 402 */       String str1 = null;
/* 403 */       if ((str1 = param1Options.get("showSource")) != null)
/* 404 */         if (str1.equals("true")) {
/* 405 */           setVisiblePart(DiagnosticPart.SOURCE, true);
/* 406 */         } else if (str1.equals("false")) {
/* 407 */           setVisiblePart(DiagnosticPart.SOURCE, false);
/*     */         }
/* 409 */       String str2 = param1Options.get("diags");
/* 410 */       if (str2 != null) {
/* 411 */         List<String> list = Arrays.asList(str2.split(","));
/* 412 */         if (list.contains("short")) {
/* 413 */           setVisiblePart(DiagnosticPart.DETAILS, false);
/* 414 */           setVisiblePart(DiagnosticPart.SUBDIAGNOSTICS, false);
/*     */         }
/* 416 */         if (list.contains("source"))
/* 417 */           setVisiblePart(DiagnosticPart.SOURCE, true);
/* 418 */         if (list.contains("-source"))
/* 419 */           setVisiblePart(DiagnosticPart.SOURCE, false);
/*     */       }
/* 421 */       String str3 = null;
/* 422 */       if ((str3 = param1Options.get("multilinePolicy")) != null) {
/* 423 */         if (str3.equals("disabled")) {
/* 424 */           setVisiblePart(DiagnosticPart.SUBDIAGNOSTICS, false);
/* 425 */         } else if (str3.startsWith("limit:")) {
/* 426 */           String str = str3.substring("limit:".length());
/* 427 */           String[] arrayOfString = str.split(":");
/*     */           try {
/* 429 */             switch (arrayOfString.length) {
/*     */               case 2:
/* 431 */                 if (!arrayOfString[1].equals("*")) {
/* 432 */                   setMultilineLimit(MultilineLimit.DEPTH, Integer.parseInt(arrayOfString[1]));
/*     */                 }
/*     */               case 1:
/* 435 */                 if (!arrayOfString[0].equals("*")) {
/* 436 */                   setMultilineLimit(MultilineLimit.LENGTH, Integer.parseInt(arrayOfString[0]));
/*     */                 }
/*     */                 break;
/*     */             }
/* 440 */           } catch (NumberFormatException numberFormatException) {
/* 441 */             setMultilineLimit(MultilineLimit.DEPTH, -1);
/* 442 */             setMultilineLimit(MultilineLimit.LENGTH, -1);
/*     */           }
/*     */         }
/*     */       }
/* 446 */       String str4 = null;
/* 447 */       if ((str4 = param1Options.get("showCaret")) != null && str4
/* 448 */         .equals("false")) {
/* 449 */         setCaretEnabled(false);
/*     */       } else {
/* 451 */         setCaretEnabled(true);
/*     */       }
/*     */     }
/*     */     public int getMultilineLimit(MultilineLimit param1MultilineLimit) {
/* 455 */       return ((Integer)this.multilineLimits.get(param1MultilineLimit)).intValue();
/*     */     }
/*     */
/*     */     public EnumSet<DiagnosticPart> getVisible() {
/* 459 */       return EnumSet.copyOf(this.visibleParts);
/*     */     }
/*     */
/*     */     public void setMultilineLimit(MultilineLimit param1MultilineLimit, int param1Int) {
/* 463 */       this.multilineLimits.put(param1MultilineLimit, Integer.valueOf((param1Int < -1) ? -1 : param1Int));
/*     */     }
/*     */
/*     */
/*     */     public void setVisible(Set<DiagnosticPart> param1Set) {
/* 468 */       this.visibleParts = EnumSet.copyOf(param1Set);
/*     */     }
/*     */
/*     */     public void setVisiblePart(DiagnosticPart param1DiagnosticPart, boolean param1Boolean) {
/* 472 */       if (param1Boolean) {
/* 473 */         this.visibleParts.add(param1DiagnosticPart);
/*     */       } else {
/* 475 */         this.visibleParts.remove(param1DiagnosticPart);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setCaretEnabled(boolean param1Boolean) {
/* 485 */       this.caretEnabled = param1Boolean;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isCaretEnabled() {
/* 494 */       return this.caretEnabled;
/*     */     }
/*     */   }
/*     */
/*     */   public Printer getPrinter() {
/* 499 */     return this.printer;
/*     */   }
/*     */
/*     */   public void setPrinter(Printer paramPrinter) {
/* 503 */     this.printer = paramPrinter;
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
/*     */   protected AbstractDiagnosticFormatter(JavacMessages paramJavacMessages, SimpleConfiguration paramSimpleConfiguration) {
/* 515 */     this.printer = new Printer()
/*     */       {
/*     */         protected String localize(Locale param1Locale, String param1String, Object... param1VarArgs)
/*     */         {
/* 519 */           return AbstractDiagnosticFormatter.this.localize(param1Locale, param1String, param1VarArgs);
/*     */         }
/*     */
/*     */         protected String capturedVarId(Type.CapturedType param1CapturedType, Locale param1Locale) {
/* 523 */           return "" + (AbstractDiagnosticFormatter.this.allCaptured.indexOf(param1CapturedType) + 1);
/*     */         }
/*     */
/*     */         public String visitCapturedType(Type.CapturedType param1CapturedType, Locale param1Locale) {
/* 527 */           if (!AbstractDiagnosticFormatter.this.allCaptured.contains(param1CapturedType)) {
/* 528 */             AbstractDiagnosticFormatter.this.allCaptured = (List)AbstractDiagnosticFormatter.this.allCaptured.append(param1CapturedType);
/*     */           }
/* 530 */           return super.visitCapturedType(param1CapturedType, param1Locale);
/*     */         }
/*     */       };
/*     */     this.messages = paramJavacMessages;
/*     */     this.config = paramSimpleConfiguration;
/*     */   }
/*     */
/*     */   protected abstract String formatDiagnostic(JCDiagnostic paramJCDiagnostic, Locale paramLocale);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\AbstractDiagnosticFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
