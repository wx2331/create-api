/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
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
/*     */
/*     */
/*     */ public class BasicDiagnosticFormatter
/*     */   extends AbstractDiagnosticFormatter
/*     */ {
/*     */   public BasicDiagnosticFormatter(Options paramOptions, JavacMessages paramJavacMessages) {
/*  76 */     super(paramJavacMessages, new BasicConfiguration(paramOptions));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public BasicDiagnosticFormatter(JavacMessages paramJavacMessages) {
/*  85 */     super(paramJavacMessages, new BasicConfiguration());
/*     */   }
/*     */
/*     */   public String formatDiagnostic(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/*  89 */     if (paramLocale == null)
/*  90 */       paramLocale = this.messages.getCurrentLocale();
/*  91 */     String str = selectFormat(paramJCDiagnostic);
/*  92 */     StringBuilder stringBuilder = new StringBuilder();
/*  93 */     for (byte b = 0; b < str.length(); b++) {
/*  94 */       char c = str.charAt(b);
/*  95 */       boolean bool = false;
/*  96 */       if (c == '%' && b < str.length() - 1) {
/*  97 */         bool = true;
/*  98 */         c = str.charAt(++b);
/*     */       }
/* 100 */       stringBuilder.append(bool ? formatMeta(c, paramJCDiagnostic, paramLocale) : String.valueOf(c));
/*     */     }
/* 102 */     if (this.depth == 0) {
/* 103 */       return addSourceLineIfNeeded(paramJCDiagnostic, stringBuilder.toString());
/*     */     }
/* 105 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */   public String formatMessage(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 109 */     int i = 0;
/* 110 */     StringBuilder stringBuilder = new StringBuilder();
/* 111 */     Collection<String> collection = formatArguments(paramJCDiagnostic, paramLocale);
/* 112 */     String str = localize(paramLocale, paramJCDiagnostic.getCode(), collection.toArray());
/* 113 */     String[] arrayOfString = str.split("\n");
/* 114 */     if (getConfiguration().getVisible().contains(Configuration.DiagnosticPart.SUMMARY)) {
/* 115 */       i += getConfiguration().getIndentation(Configuration.DiagnosticPart.SUMMARY);
/* 116 */       stringBuilder.append(indent(arrayOfString[0], i));
/*     */     }
/* 118 */     if (arrayOfString.length > 1 && getConfiguration().getVisible().contains(Configuration.DiagnosticPart.DETAILS)) {
/* 119 */       i += getConfiguration().getIndentation(Configuration.DiagnosticPart.DETAILS);
/* 120 */       for (byte b = 1; b < arrayOfString.length; b++) {
/* 121 */         stringBuilder.append("\n" + indent(arrayOfString[b], i));
/*     */       }
/*     */     }
/* 124 */     if (paramJCDiagnostic.isMultiline() && getConfiguration().getVisible().contains(Configuration.DiagnosticPart.SUBDIAGNOSTICS)) {
/* 125 */       i += getConfiguration().getIndentation(Configuration.DiagnosticPart.SUBDIAGNOSTICS);
/* 126 */       for (String str1 : formatSubdiagnostics(paramJCDiagnostic, paramLocale)) {
/* 127 */         stringBuilder.append("\n" + indent(str1, i));
/*     */       }
/*     */     }
/* 130 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */   protected String addSourceLineIfNeeded(JCDiagnostic paramJCDiagnostic, String paramString) {
/* 134 */     if (!displaySource(paramJCDiagnostic)) {
/* 135 */       return paramString;
/*     */     }
/* 137 */     BasicConfiguration basicConfiguration = getConfiguration();
/* 138 */     int i = basicConfiguration.getIndentation(Configuration.DiagnosticPart.SOURCE);
/* 139 */     String str = "\n" + formatSourceLine(paramJCDiagnostic, i);
/* 140 */     boolean bool = (paramString.indexOf("\n") == -1) ? true : false;
/* 141 */     if (bool || getConfiguration().getSourcePosition() == BasicConfiguration.SourcePosition.BOTTOM) {
/* 142 */       return paramString + str;
/*     */     }
/* 144 */     return paramString.replaceFirst("\n", Matcher.quoteReplacement(str) + "\n");
/*     */   }
/*     */
/*     */   protected String formatMeta(char paramChar, JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/*     */     boolean bool;
/* 149 */     switch (paramChar) {
/*     */       case 'b':
/* 151 */         return formatSource(paramJCDiagnostic, false, paramLocale);
/*     */       case 'e':
/* 153 */         return formatPosition(paramJCDiagnostic, PositionKind.END, paramLocale);
/*     */       case 'f':
/* 155 */         return formatSource(paramJCDiagnostic, true, paramLocale);
/*     */       case 'l':
/* 157 */         return formatPosition(paramJCDiagnostic, PositionKind.LINE, paramLocale);
/*     */       case 'c':
/* 159 */         return formatPosition(paramJCDiagnostic, PositionKind.COLUMN, paramLocale);
/*     */       case 'o':
/* 161 */         return formatPosition(paramJCDiagnostic, PositionKind.OFFSET, paramLocale);
/*     */       case 'p':
/* 163 */         return formatKind(paramJCDiagnostic, paramLocale);
/*     */       case 's':
/* 165 */         return formatPosition(paramJCDiagnostic, PositionKind.START, paramLocale);
/*     */
/*     */       case 't':
/* 168 */         switch (paramJCDiagnostic.getType()) {
/*     */           case FRAGMENT:
/* 170 */             bool = false;
/*     */             break;
/*     */           case ERROR:
/* 173 */             bool = (paramJCDiagnostic.getIntPosition() == -1) ? true : false;
/*     */             break;
/*     */           default:
/* 176 */             bool = true; break;
/*     */         }
/* 178 */         if (bool) {
/* 179 */           return formatKind(paramJCDiagnostic, paramLocale);
/*     */         }
/* 181 */         return "";
/*     */
/*     */       case 'm':
/* 184 */         return formatMessage(paramJCDiagnostic, paramLocale);
/*     */       case 'L':
/* 186 */         return formatLintCategory(paramJCDiagnostic, paramLocale);
/*     */       case '_':
/* 188 */         return " ";
/*     */       case '%':
/* 190 */         return "%";
/*     */     }
/* 192 */     return String.valueOf(paramChar);
/*     */   }
/*     */
/*     */
/*     */   private String selectFormat(JCDiagnostic paramJCDiagnostic) {
/* 197 */     DiagnosticSource diagnosticSource = paramJCDiagnostic.getDiagnosticSource();
/* 198 */     String str = getConfiguration().getFormat(BasicConfiguration.BasicFormatKind.DEFAULT_NO_POS_FORMAT);
/* 199 */     if (diagnosticSource != null && diagnosticSource != DiagnosticSource.NO_SOURCE) {
/* 200 */       if (paramJCDiagnostic.getIntPosition() != -1) {
/* 201 */         str = getConfiguration().getFormat(BasicConfiguration.BasicFormatKind.DEFAULT_POS_FORMAT);
/* 202 */       } else if (diagnosticSource.getFile() != null && diagnosticSource
/* 203 */         .getFile().getKind() == JavaFileObject.Kind.CLASS) {
/* 204 */         str = getConfiguration().getFormat(BasicConfiguration.BasicFormatKind.DEFAULT_CLASS_FORMAT);
/*     */       }
/*     */     }
/* 207 */     return str;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public BasicConfiguration getConfiguration() {
/* 213 */     return (BasicConfiguration)super.getConfiguration();
/*     */   }
/*     */
/*     */   public static class BasicConfiguration
/*     */     extends SimpleConfiguration
/*     */   {
/*     */     protected Map<DiagnosticPart, Integer> indentationLevels;
/*     */     protected Map<BasicFormatKind, String> availableFormats;
/*     */     protected SourcePosition sourcePosition;
/*     */
/*     */     public BasicConfiguration(Options param1Options) {
/* 224 */       super(param1Options, EnumSet.of(DiagnosticPart.SUMMARY, DiagnosticPart.DETAILS, DiagnosticPart.SUBDIAGNOSTICS, DiagnosticPart.SOURCE));
/*     */
/*     */
/*     */
/* 228 */       initFormat();
/* 229 */       initIndentation();
/* 230 */       if (param1Options.isSet("oldDiags"))
/* 231 */         initOldFormat();
/* 232 */       String str1 = param1Options.get("diagsFormat");
/* 233 */       if (str1 != null)
/* 234 */         if (str1.equals("OLD")) {
/* 235 */           initOldFormat();
/*     */         } else {
/* 237 */           initFormats(str1);
/*     */         }
/* 239 */       String str2 = null;
/* 240 */       if ((str2 = param1Options.get("sourcePosition")) != null && str2
/* 241 */         .equals("bottom")) {
/* 242 */         setSourcePosition(SourcePosition.BOTTOM);
/*     */       } else {
/* 244 */         setSourcePosition(SourcePosition.AFTER_SUMMARY);
/* 245 */       }  String str3 = param1Options.get("diagsIndentation");
/* 246 */       if (str3 != null) {
/* 247 */         String[] arrayOfString = str3.split("\\|");
/*     */         try {
/* 249 */           switch (arrayOfString.length) {
/*     */             case 5:
/* 251 */               setIndentation(DiagnosticPart.JLS,
/* 252 */                   Integer.parseInt(arrayOfString[4]));
/*     */             case 4:
/* 254 */               setIndentation(DiagnosticPart.SUBDIAGNOSTICS,
/* 255 */                   Integer.parseInt(arrayOfString[3]));
/*     */             case 3:
/* 257 */               setIndentation(DiagnosticPart.SOURCE,
/* 258 */                   Integer.parseInt(arrayOfString[2]));
/*     */             case 2:
/* 260 */               setIndentation(DiagnosticPart.DETAILS,
/* 261 */                   Integer.parseInt(arrayOfString[1])); break;
/*     */           }
/* 263 */           setIndentation(DiagnosticPart.SUMMARY,
/* 264 */               Integer.parseInt(arrayOfString[0]));
/*     */
/*     */         }
/* 267 */         catch (NumberFormatException numberFormatException) {
/* 268 */           initIndentation();
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     public BasicConfiguration() {
/* 274 */       super(EnumSet.of(DiagnosticPart.SUMMARY, DiagnosticPart.DETAILS, DiagnosticPart.SUBDIAGNOSTICS, DiagnosticPart.SOURCE));
/*     */
/*     */
/*     */
/* 278 */       initFormat();
/* 279 */       initIndentation();
/*     */     }
/*     */
/*     */     private void initFormat() {
/* 283 */       initFormats("%f:%l:%_%p%L%m", "%p%L%m", "%f:%_%p%L%m");
/*     */     }
/*     */
/*     */     private void initOldFormat() {
/* 287 */       initFormats("%f:%l:%_%t%L%m", "%p%L%m", "%f:%_%t%L%m");
/*     */     }
/*     */
/*     */     private void initFormats(String param1String1, String param1String2, String param1String3) {
/* 291 */       this.availableFormats = new EnumMap<>(BasicFormatKind.class);
/* 292 */       setFormat(BasicFormatKind.DEFAULT_POS_FORMAT, param1String1);
/* 293 */       setFormat(BasicFormatKind.DEFAULT_NO_POS_FORMAT, param1String2);
/* 294 */       setFormat(BasicFormatKind.DEFAULT_CLASS_FORMAT, param1String3);
/*     */     }
/*     */
/*     */
/*     */     private void initFormats(String param1String) {
/* 299 */       String[] arrayOfString = param1String.split("\\|");
/* 300 */       switch (arrayOfString.length) {
/*     */         case 3:
/* 302 */           setFormat(BasicFormatKind.DEFAULT_CLASS_FORMAT, arrayOfString[2]);
/*     */         case 2:
/* 304 */           setFormat(BasicFormatKind.DEFAULT_NO_POS_FORMAT, arrayOfString[1]); break;
/*     */       }
/* 306 */       setFormat(BasicFormatKind.DEFAULT_POS_FORMAT, arrayOfString[0]);
/*     */     }
/*     */
/*     */
/*     */     private void initIndentation() {
/* 311 */       this.indentationLevels = new HashMap<>();
/* 312 */       setIndentation(DiagnosticPart.SUMMARY, 0);
/* 313 */       setIndentation(DiagnosticPart.DETAILS, 2);
/* 314 */       setIndentation(DiagnosticPart.SUBDIAGNOSTICS, 4);
/* 315 */       setIndentation(DiagnosticPart.SOURCE, 0);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int getIndentation(DiagnosticPart param1DiagnosticPart) {
/* 325 */       return ((Integer)this.indentationLevels.get(param1DiagnosticPart)).intValue();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setIndentation(DiagnosticPart param1DiagnosticPart, int param1Int) {
/* 336 */       this.indentationLevels.put(param1DiagnosticPart, Integer.valueOf(param1Int));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setSourcePosition(SourcePosition param1SourcePosition) {
/* 345 */       this.sourcePosition = param1SourcePosition;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public SourcePosition getSourcePosition() {
/* 354 */       return this.sourcePosition;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public enum SourcePosition
/*     */     {
/* 366 */       BOTTOM,
/*     */
/*     */
/*     */
/*     */
/* 371 */       AFTER_SUMMARY;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setFormat(BasicFormatKind param1BasicFormatKind, String param1String) {
/* 381 */       this.availableFormats.put(param1BasicFormatKind, param1String);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String getFormat(BasicFormatKind param1BasicFormatKind) {
/* 390 */       return this.availableFormats.get(param1BasicFormatKind);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public enum BasicFormatKind
/*     */     {
/* 401 */       DEFAULT_POS_FORMAT,
/*     */
/*     */
/*     */
/* 405 */       DEFAULT_NO_POS_FORMAT,
/*     */
/*     */
/*     */
/* 409 */       DEFAULT_CLASS_FORMAT; } } public enum SourcePosition { BOTTOM, AFTER_SUMMARY; } public enum BasicFormatKind { DEFAULT_POS_FORMAT, DEFAULT_NO_POS_FORMAT, DEFAULT_CLASS_FORMAT; }
/*     */
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\BasicDiagnosticFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
