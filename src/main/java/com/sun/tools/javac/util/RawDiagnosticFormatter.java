/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.file.BaseFileObject;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
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
/*     */ public final class RawDiagnosticFormatter
/*     */   extends AbstractDiagnosticFormatter
/*     */ {
/*     */   public RawDiagnosticFormatter(Options paramOptions) {
/*  58 */     super(null, new SimpleConfiguration(paramOptions,
/*  59 */           EnumSet.of(Configuration.DiagnosticPart.SUMMARY, Configuration.DiagnosticPart.DETAILS, Configuration.DiagnosticPart.SUBDIAGNOSTICS)));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String formatDiagnostic(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/*     */     try {
/*  67 */       StringBuilder stringBuilder = new StringBuilder();
/*  68 */       if (paramJCDiagnostic.getPosition() != -1L) {
/*  69 */         stringBuilder.append(formatSource(paramJCDiagnostic, false, (Locale)null));
/*  70 */         stringBuilder.append(':');
/*  71 */         stringBuilder.append(formatPosition(paramJCDiagnostic, PositionKind.LINE, (Locale)null));
/*  72 */         stringBuilder.append(':');
/*  73 */         stringBuilder.append(formatPosition(paramJCDiagnostic, PositionKind.COLUMN, (Locale)null));
/*  74 */         stringBuilder.append(':');
/*     */       }
/*  76 */       else if (paramJCDiagnostic.getSource() != null && paramJCDiagnostic.getSource().getKind() == JavaFileObject.Kind.CLASS) {
/*  77 */         stringBuilder.append(formatSource(paramJCDiagnostic, false, (Locale)null));
/*  78 */         stringBuilder.append(":-:-:");
/*     */       } else {
/*     */
/*  81 */         stringBuilder.append('-');
/*  82 */       }  stringBuilder.append(' ');
/*  83 */       stringBuilder.append(formatMessage(paramJCDiagnostic, (Locale)null));
/*  84 */       if (displaySource(paramJCDiagnostic)) {
/*  85 */         stringBuilder.append("\n");
/*  86 */         stringBuilder.append(formatSourceLine(paramJCDiagnostic, 0));
/*     */       }
/*  88 */       return stringBuilder.toString();
/*     */     }
/*  90 */     catch (Exception exception) {
/*     */
/*  92 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */   public String formatMessage(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/*  97 */     StringBuilder stringBuilder = new StringBuilder();
/*  98 */     Collection<String> collection = formatArguments(paramJCDiagnostic, paramLocale);
/*  99 */     stringBuilder.append(localize((Locale)null, paramJCDiagnostic.getCode(), collection.toArray()));
/* 100 */     if (paramJCDiagnostic.isMultiline() && getConfiguration().getVisible().contains(Configuration.DiagnosticPart.SUBDIAGNOSTICS)) {
/* 101 */       List<String> list = formatSubdiagnostics(paramJCDiagnostic, null);
/* 102 */       if (list.nonEmpty()) {
/* 103 */         String str = "";
/* 104 */         stringBuilder.append(",{");
/* 105 */         for (String str1 : formatSubdiagnostics(paramJCDiagnostic, null)) {
/* 106 */           stringBuilder.append(str);
/* 107 */           stringBuilder.append("(");
/* 108 */           stringBuilder.append(str1);
/* 109 */           stringBuilder.append(")");
/* 110 */           str = ",";
/*     */         }
/* 112 */         stringBuilder.append('}');
/*     */       }
/*     */     }
/* 115 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */   protected String formatArgument(JCDiagnostic paramJCDiagnostic, Object paramObject, Locale paramLocale) {
/*     */     String str;
/* 121 */     if (paramObject instanceof com.sun.tools.javac.api.Formattable) {
/* 122 */       str = paramObject.toString();
/* 123 */     } else if (paramObject instanceof JCTree.JCExpression) {
/* 124 */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)paramObject;
/* 125 */       str = "@" + jCExpression.getStartPosition();
/* 126 */     } else if (paramObject instanceof BaseFileObject) {
/* 127 */       str = ((BaseFileObject)paramObject).getShortName();
/*     */     } else {
/* 129 */       str = super.formatArgument(paramJCDiagnostic, paramObject, null);
/*     */     }
/* 131 */     return (paramObject instanceof JCDiagnostic) ? ("(" + str + ")") : str;
/*     */   }
/*     */
/*     */
/*     */   protected String localize(Locale paramLocale, String paramString, Object... paramVarArgs) {
/* 136 */     StringBuilder stringBuilder = new StringBuilder();
/* 137 */     stringBuilder.append(paramString);
/* 138 */     String str = ": ";
/* 139 */     for (Object object : paramVarArgs) {
/* 140 */       stringBuilder.append(str);
/* 141 */       stringBuilder.append(object);
/* 142 */       str = ", ";
/*     */     }
/* 144 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */   public boolean isRaw() {
/* 149 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\RawDiagnosticFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
