/*     */ package com.sun.tools.javac.main;
/*     */ 
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
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
/*     */ public enum Option
/*     */ {
/*  68 */   G("-g", "opt.g", OptionKind.STANDARD, OptionGroup.BASIC),
/*     */   
/*  70 */   G_NONE("-g:none", "opt.g.none", OptionKind.STANDARD, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/*  73 */       param1OptionHelper.put("-g:", "none");
/*  74 */       return false;
/*     */     }
/*     */   },
/*     */   
/*  78 */   G_CUSTOM("-g:", "opt.g.lines.vars.source", OptionKind.STANDARD, OptionGroup.BASIC, ChoiceKind.ANYOF, new String[] { "lines", "vars", "source"
/*     */     
/*     */     }),
/*  81 */   XLINT("-Xlint", "opt.Xlint", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/*  83 */   XLINT_CUSTOM("-Xlint:", "opt.Xlint.suboptlist", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ANYOF, 
/*  84 */     getXLintChoices()),
/*     */   
/*  86 */   XDOCLINT("-Xdoclint", "opt.Xdoclint", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/*  88 */   XDOCLINT_CUSTOM("-Xdoclint:", "opt.Xdoclint.subopts", "opt.Xdoclint.custom", OptionKind.EXTENDED, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean matches(String param1String) {
/*  91 */       return DocLint.isValidOption(param1String
/*  92 */           .replace(XDOCLINT_CUSTOM.text, "-Xmsgs:"));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/*  97 */       String str1 = param1OptionHelper.get(XDOCLINT_CUSTOM);
/*  98 */       String str2 = (str1 == null) ? param1String : (str1 + " " + param1String);
/*  99 */       param1OptionHelper.put(XDOCLINT_CUSTOM.text, str2);
/* 100 */       return false;
/*     */     }
/*     */   },
/*     */ 
/*     */   
/* 105 */   NOWARN("-nowarn", "opt.nowarn", OptionKind.STANDARD, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 108 */       param1OptionHelper.put("-Xlint:none", param1String);
/* 109 */       return false;
/*     */     }
/*     */   },
/*     */   
/* 113 */   VERBOSE("-verbose", "opt.verbose", OptionKind.STANDARD, OptionGroup.BASIC),
/*     */ 
/*     */   
/* 116 */   DEPRECATION("-deprecation", "opt.deprecation", OptionKind.STANDARD, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 119 */       param1OptionHelper.put("-Xlint:deprecation", param1String);
/* 120 */       return false;
/*     */     }
/*     */   },
/*     */   
/* 124 */   CLASSPATH("-classpath", "opt.arg.path", "opt.classpath", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 126 */   CP("-cp", "opt.arg.path", "opt.classpath", OptionKind.STANDARD, OptionGroup.FILEMANAGER)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 129 */       return super.process(param1OptionHelper, "-classpath", param1String2);
/*     */     }
/*     */   },
/*     */   
/* 133 */   SOURCEPATH("-sourcepath", "opt.arg.path", "opt.sourcepath", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 135 */   BOOTCLASSPATH("-bootclasspath", "opt.arg.path", "opt.bootclasspath", OptionKind.STANDARD, OptionGroup.FILEMANAGER)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 138 */       param1OptionHelper.remove("-Xbootclasspath/p:");
/* 139 */       param1OptionHelper.remove("-Xbootclasspath/a:");
/* 140 */       return super.process(param1OptionHelper, param1String1, param1String2);
/*     */     }
/*     */   },
/*     */   
/* 144 */   XBOOTCLASSPATH_PREPEND("-Xbootclasspath/p:", "opt.arg.path", "opt.Xbootclasspath.p", OptionKind.EXTENDED, OptionGroup.FILEMANAGER),
/*     */   
/* 146 */   XBOOTCLASSPATH_APPEND("-Xbootclasspath/a:", "opt.arg.path", "opt.Xbootclasspath.a", OptionKind.EXTENDED, OptionGroup.FILEMANAGER),
/*     */   
/* 148 */   XBOOTCLASSPATH("-Xbootclasspath:", "opt.arg.path", "opt.bootclasspath", OptionKind.EXTENDED, OptionGroup.FILEMANAGER)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 151 */       param1OptionHelper.remove("-Xbootclasspath/p:");
/* 152 */       param1OptionHelper.remove("-Xbootclasspath/a:");
/* 153 */       return super.process(param1OptionHelper, "-bootclasspath", param1String2);
/*     */     }
/*     */   },
/*     */   
/* 157 */   EXTDIRS("-extdirs", "opt.arg.dirs", "opt.extdirs", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 159 */   DJAVA_EXT_DIRS("-Djava.ext.dirs=", "opt.arg.dirs", "opt.extdirs", OptionKind.EXTENDED, OptionGroup.FILEMANAGER)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 162 */       return super.process(param1OptionHelper, "-extdirs", param1String2);
/*     */     }
/*     */   },
/*     */   
/* 166 */   ENDORSEDDIRS("-endorseddirs", "opt.arg.dirs", "opt.endorseddirs", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 168 */   DJAVA_ENDORSED_DIRS("-Djava.endorsed.dirs=", "opt.arg.dirs", "opt.endorseddirs", OptionKind.EXTENDED, OptionGroup.FILEMANAGER)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 171 */       return super.process(param1OptionHelper, "-endorseddirs", param1String2);
/*     */     }
/*     */   },
/*     */   
/* 175 */   PROC("-proc:", "opt.proc.none.only", OptionKind.STANDARD, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "none", "only"
/*     */     }),
/* 177 */   PROCESSOR("-processor", "opt.arg.class.list", "opt.processor", OptionKind.STANDARD, OptionGroup.BASIC),
/*     */   
/* 179 */   PROCESSORPATH("-processorpath", "opt.arg.path", "opt.processorpath", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 181 */   PARAMETERS("-parameters", "opt.parameters", OptionKind.STANDARD, OptionGroup.BASIC),
/*     */   
/* 183 */   D("-d", "opt.arg.directory", "opt.d", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 185 */   S("-s", "opt.arg.directory", "opt.sourceDest", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 187 */   H("-h", "opt.arg.directory", "opt.headerDest", OptionKind.STANDARD, OptionGroup.FILEMANAGER),
/*     */   
/* 189 */   IMPLICIT("-implicit:", "opt.implicit", OptionKind.STANDARD, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "none", "class"
/*     */     }),
/* 191 */   ENCODING("-encoding", "opt.arg.encoding", "opt.encoding", OptionKind.STANDARD, OptionGroup.FILEMANAGER)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 194 */       return super.process(param1OptionHelper, param1String1, param1String2);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/* 199 */   SOURCE("-source", "opt.arg.release", "opt.source", OptionKind.STANDARD, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 202 */       Source source = Source.lookup(param1String2);
/* 203 */       if (source == null) {
/* 204 */         param1OptionHelper.error("err.invalid.source", new Object[] { param1String2 });
/* 205 */         return true;
/*     */       } 
/* 207 */       return super.process(param1OptionHelper, param1String1, param1String2);
/*     */     }
/*     */   },
/*     */   
/* 211 */   TARGET("-target", "opt.arg.release", "opt.target", OptionKind.STANDARD, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 214 */       Target target = Target.lookup(param1String2);
/* 215 */       if (target == null) {
/* 216 */         param1OptionHelper.error("err.invalid.target", new Object[] { param1String2 });
/* 217 */         return true;
/*     */       } 
/* 219 */       return super.process(param1OptionHelper, param1String1, param1String2);
/*     */     }
/*     */   },
/*     */   
/* 223 */   PROFILE("-profile", "opt.arg.profile", "opt.profile", OptionKind.STANDARD, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/* 226 */       Profile profile = Profile.lookup(param1String2);
/* 227 */       if (profile == null) {
/* 228 */         param1OptionHelper.error("err.invalid.profile", new Object[] { param1String2 });
/* 229 */         return true;
/*     */       } 
/* 231 */       return super.process(param1OptionHelper, param1String1, param1String2);
/*     */     }
/*     */   },
/*     */   
/* 235 */   VERSION("-version", "opt.version", OptionKind.STANDARD, OptionGroup.INFO)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 238 */       Log log = param1OptionHelper.getLog();
/* 239 */       String str = param1OptionHelper.getOwnName();
/* 240 */       log.printLines(Log.PrefixKind.JAVAC, "version", new Object[] { str, JavaCompiler.version() });
/* 241 */       return super.process(param1OptionHelper, param1String);
/*     */     }
/*     */   },
/*     */   
/* 245 */   FULLVERSION("-fullversion", null, OptionKind.HIDDEN, OptionGroup.INFO)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 248 */       Log log = param1OptionHelper.getLog();
/* 249 */       String str = param1OptionHelper.getOwnName();
/* 250 */       log.printLines(Log.PrefixKind.JAVAC, "fullVersion", new Object[] { str, JavaCompiler.fullVersion() });
/* 251 */       return super.process(param1OptionHelper, param1String);
/*     */     }
/*     */   },
/*     */   
/* 255 */   DIAGS("-XDdiags=", null, OptionKind.HIDDEN, OptionGroup.INFO)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 258 */       param1String = param1String.substring(param1String.indexOf('=') + 1);
/* 259 */       String str = param1String.contains("%") ? "-XDdiagsFormat=" : "-XDdiags=";
/*     */ 
/*     */       
/* 262 */       str = str + param1String;
/* 263 */       if (XD.matches(str)) {
/* 264 */         return XD.process(param1OptionHelper, str);
/*     */       }
/* 266 */       return false;
/*     */     }
/*     */   },
/*     */   
/* 270 */   HELP("-help", "opt.help", OptionKind.STANDARD, OptionGroup.INFO)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 273 */       Log log = param1OptionHelper.getLog();
/* 274 */       String str = param1OptionHelper.getOwnName();
/* 275 */       log.printLines(Log.PrefixKind.JAVAC, "msg.usage.header", new Object[] { str });
/* 276 */       for (Option option : null.getJavaCompilerOptions()) {
/* 277 */         option.help(log, OptionKind.STANDARD);
/*     */       }
/* 279 */       log.printNewline();
/* 280 */       return super.process(param1OptionHelper, param1String);
/*     */     }
/*     */   },
/*     */   
/* 284 */   A("-A", "opt.arg.key.equals.value", "opt.A", OptionKind.STANDARD, OptionGroup.BASIC, true)
/*     */   {
/*     */     public boolean matches(String param1String) {
/* 287 */       return param1String.startsWith("-A");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasArg() {
/* 292 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 298 */       int i = param1String.length();
/* 299 */       if (i == 2) {
/* 300 */         param1OptionHelper.error("err.empty.A.argument", new Object[0]);
/* 301 */         return true;
/*     */       } 
/* 303 */       int j = param1String.indexOf('=');
/* 304 */       String str = param1String.substring(2, (j != -1) ? j : i);
/* 305 */       if (!JavacProcessingEnvironment.isValidOptionName(str)) {
/* 306 */         param1OptionHelper.error("err.invalid.A.key", new Object[] { param1String });
/* 307 */         return true;
/*     */       } 
/* 309 */       return process(param1OptionHelper, param1String, param1String);
/*     */     }
/*     */   },
/*     */   
/* 313 */   X("-X", "opt.X", OptionKind.STANDARD, OptionGroup.INFO)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 316 */       Log log = param1OptionHelper.getLog();
/* 317 */       for (Option option : null.getJavaCompilerOptions()) {
/* 318 */         option.help(log, OptionKind.EXTENDED);
/*     */       }
/* 320 */       log.printNewline();
/* 321 */       log.printLines(Log.PrefixKind.JAVAC, "msg.usage.nonstandard.footer", new Object[0]);
/* 322 */       return super.process(param1OptionHelper, param1String);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */   
/* 328 */   J("-J", "opt.arg.flag", "opt.J", OptionKind.STANDARD, OptionGroup.INFO, true)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 331 */       throw new AssertionError("the -J flag should be caught by the launcher.");
/*     */     }
/*     */   },
/*     */ 
/*     */   
/* 336 */   MOREINFO("-moreinfo", null, OptionKind.HIDDEN, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 339 */       Type.moreInfo = true;
/* 340 */       return super.process(param1OptionHelper, param1String);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/* 345 */   WERROR("-Werror", "opt.Werror", OptionKind.STANDARD, OptionGroup.BASIC),
/*     */ 
/*     */ 
/*     */   
/* 349 */   PROMPT("-prompt", null, OptionKind.HIDDEN, OptionGroup.BASIC),
/*     */ 
/*     */   
/* 352 */   DOE("-doe", null, OptionKind.HIDDEN, OptionGroup.BASIC),
/*     */ 
/*     */   
/* 355 */   PRINTSOURCE("-printsource", null, OptionKind.HIDDEN, OptionGroup.BASIC),
/*     */ 
/*     */   
/* 358 */   WARNUNCHECKED("-warnunchecked", null, OptionKind.HIDDEN, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 361 */       param1OptionHelper.put("-Xlint:unchecked", param1String);
/* 362 */       return false;
/*     */     }
/*     */   },
/*     */   
/* 366 */   XMAXERRS("-Xmaxerrs", "opt.arg.number", "opt.maxerrs", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/* 368 */   XMAXWARNS("-Xmaxwarns", "opt.arg.number", "opt.maxwarns", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/* 370 */   XSTDOUT("-Xstdout", "opt.arg.file", "opt.Xstdout", OptionKind.EXTENDED, OptionGroup.INFO)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String1, String param1String2) {
/*     */       try {
/* 374 */         Log log = param1OptionHelper.getLog();
/*     */         
/* 376 */         log.setWriters(new PrintWriter(new FileWriter(param1String2), true));
/* 377 */       } catch (IOException iOException) {
/* 378 */         param1OptionHelper.error("err.error.writing.file", new Object[] { param1String2, iOException });
/* 379 */         return true;
/*     */       } 
/* 381 */       return super.process(param1OptionHelper, param1String1, param1String2);
/*     */     }
/*     */   },
/*     */   
/* 385 */   XPRINT("-Xprint", "opt.print", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/* 387 */   XPRINTROUNDS("-XprintRounds", "opt.printRounds", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/* 389 */   XPRINTPROCESSORINFO("-XprintProcessorInfo", "opt.printProcessorInfo", OptionKind.EXTENDED, OptionGroup.BASIC),
/*     */   
/* 391 */   XPREFER("-Xprefer:", "opt.prefer", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "source", "newer"
/*     */     
/*     */     }),
/* 394 */   XPKGINFO("-Xpkginfo:", "opt.pkginfo", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "always", "legacy", "nonempty"
/*     */     
/*     */     }),
/* 397 */   O("-O", null, OptionKind.HIDDEN, OptionGroup.BASIC),
/*     */ 
/*     */   
/* 400 */   XJCOV("-Xjcov", null, OptionKind.HIDDEN, OptionGroup.BASIC),
/*     */   
/* 402 */   PLUGIN("-Xplugin:", "opt.arg.plugin", "opt.plugin", OptionKind.EXTENDED, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 405 */       String str1 = param1String.substring(param1String.indexOf(':') + 1);
/* 406 */       String str2 = param1OptionHelper.get(PLUGIN);
/* 407 */       param1OptionHelper.put(PLUGIN.text, (str2 == null) ? str1 : (str2 + Character.MIN_VALUE + str1.trim()));
/* 408 */       return false;
/*     */     }
/*     */   },
/*     */   
/* 412 */   XDIAGS("-Xdiags:", "opt.diags", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "compact", "verbose"
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 418 */   XD("-XD", null, OptionKind.HIDDEN, OptionGroup.BASIC)
/*     */   {
/*     */     public boolean matches(String param1String) {
/* 421 */       return param1String.startsWith(this.text);
/*     */     }
/*     */     
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 425 */       param1String = param1String.substring(this.text.length());
/* 426 */       int i = param1String.indexOf('=');
/* 427 */       String str1 = (i < 0) ? param1String : param1String.substring(0, i);
/* 428 */       String str2 = (i < 0) ? param1String : param1String.substring(i + 1);
/* 429 */       param1OptionHelper.put(str1, str2);
/* 430 */       return false;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */   
/* 436 */   AT("@", "opt.arg.file", "opt.AT", OptionKind.STANDARD, OptionGroup.INFO, true)
/*     */   {
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 439 */       throw new AssertionError("the @ flag should be caught by CommandLine.");
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 451 */   SOURCEFILE("sourcefile", null, OptionKind.HIDDEN, OptionGroup.INFO)
/*     */   {
/*     */     public boolean matches(String param1String) {
/* 454 */       return (param1String.endsWith(".java") || 
/* 455 */         SourceVersion.isName(param1String));
/*     */     }
/*     */     
/*     */     public boolean process(OptionHelper param1OptionHelper, String param1String) {
/* 459 */       if (param1String.endsWith(".java")) {
/* 460 */         File file = new File(param1String);
/* 461 */         if (!file.exists()) {
/* 462 */           param1OptionHelper.error("err.file.not.found", new Object[] { file });
/* 463 */           return true;
/*     */         } 
/* 465 */         if (!file.isFile()) {
/* 466 */           param1OptionHelper.error("err.file.not.file", new Object[] { file });
/* 467 */           return true;
/*     */         } 
/* 469 */         param1OptionHelper.addFile(file);
/*     */       } else {
/* 471 */         param1OptionHelper.addClassName(param1String);
/*     */       } 
/* 473 */       return false;
/*     */     } };
/*     */   public final String text; final OptionKind kind; final OptionGroup group; final String argsNameKey; final String descrKey;
/*     */   final boolean hasSuffix;
/*     */   final ChoiceKind choiceKind;
/*     */   final Map<String, Boolean> choices;
/*     */   
/* 480 */   public enum OptionKind { STANDARD,
/*     */     
/* 482 */     EXTENDED,
/*     */     
/* 484 */     HIDDEN; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum OptionGroup
/*     */   {
/* 492 */     BASIC,
/*     */ 
/*     */     
/* 495 */     FILEMANAGER,
/*     */     
/* 497 */     INFO,
/*     */     
/* 499 */     OPERAND;
/*     */   }
/*     */ 
/*     */   
/*     */   enum ChoiceKind
/*     */   {
/* 505 */     ONEOF,
/*     */     
/* 507 */     ANYOF;
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
/*     */   private static Map<String, Boolean> createChoices(String... paramVarArgs) {
/* 567 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/* 568 */     for (String str : paramVarArgs)
/* 569 */       linkedHashMap.put(str, Boolean.valueOf(false)); 
/* 570 */     return (Map)linkedHashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Option(String paramString1, String paramString2, String paramString3, OptionKind paramOptionKind, OptionGroup paramOptionGroup, ChoiceKind paramChoiceKind, Map<String, Boolean> paramMap, boolean paramBoolean) {
/* 577 */     this.text = paramString1;
/* 578 */     this.argsNameKey = paramString2;
/* 579 */     this.descrKey = paramString3;
/* 580 */     this.kind = paramOptionKind;
/* 581 */     this.group = paramOptionGroup;
/* 582 */     this.choiceKind = paramChoiceKind;
/* 583 */     this.choices = paramMap;
/* 584 */     char c = paramString1.charAt(paramString1.length() - 1);
/* 585 */     this.hasSuffix = (paramBoolean || c == ':' || c == '=');
/*     */   }
/*     */   
/*     */   public String getText() {
/* 589 */     return this.text;
/*     */   }
/*     */   
/*     */   public OptionKind getKind() {
/* 593 */     return this.kind;
/*     */   }
/*     */   
/*     */   public boolean hasArg() {
/* 597 */     return (this.argsNameKey != null && !this.hasSuffix);
/*     */   }
/*     */   
/*     */   public boolean matches(String paramString) {
/* 601 */     if (!this.hasSuffix) {
/* 602 */       return paramString.equals(this.text);
/*     */     }
/* 604 */     if (!paramString.startsWith(this.text)) {
/* 605 */       return false;
/*     */     }
/* 607 */     if (this.choices != null) {
/* 608 */       String str = paramString.substring(this.text.length());
/* 609 */       if (this.choiceKind == ChoiceKind.ONEOF) {
/* 610 */         return this.choices.keySet().contains(str);
/*     */       }
/* 612 */       for (String str1 : str.split(",+")) {
/* 613 */         if (!this.choices.keySet().contains(str1)) {
/* 614 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 619 */     return true;
/*     */   }
/*     */   
/*     */   public boolean process(OptionHelper paramOptionHelper, String paramString1, String paramString2) {
/* 623 */     if (this.choices != null) {
/* 624 */       if (this.choiceKind == ChoiceKind.ONEOF) {
/*     */         
/* 626 */         for (String str : this.choices.keySet())
/* 627 */           paramOptionHelper.remove(paramString1 + str); 
/* 628 */         String str1 = paramString1 + paramString2;
/* 629 */         paramOptionHelper.put(str1, str1);
/*     */ 
/*     */         
/* 632 */         String str2 = paramString1.substring(0, paramString1.length() - 1);
/* 633 */         paramOptionHelper.put(str2, paramString2);
/*     */       } else {
/*     */         
/* 636 */         for (String str1 : paramString2.split(",+")) {
/* 637 */           String str2 = paramString1 + str1;
/* 638 */           paramOptionHelper.put(str2, str2);
/*     */         } 
/*     */       } 
/*     */     }
/* 642 */     paramOptionHelper.put(paramString1, paramString2);
/* 643 */     return false;
/*     */   }
/*     */   
/*     */   public boolean process(OptionHelper paramOptionHelper, String paramString) {
/* 647 */     if (this.hasSuffix) {
/* 648 */       return process(paramOptionHelper, this.text, paramString.substring(this.text.length()));
/*     */     }
/* 650 */     return process(paramOptionHelper, paramString, paramString);
/*     */   }
/*     */   
/*     */   void help(Log paramLog, OptionKind paramOptionKind) {
/* 654 */     if (this.kind != paramOptionKind) {
/*     */       return;
/*     */     }
/* 657 */     paramLog.printRawLines(Log.WriterKind.NOTICE, 
/* 658 */         String.format("  %-26s %s", new Object[] {
/* 659 */             helpSynopsis(paramLog), paramLog
/* 660 */             .localize(Log.PrefixKind.JAVAC, this.descrKey, new Object[0])
/*     */           }));
/*     */   }
/*     */   
/*     */   private String helpSynopsis(Log paramLog) {
/* 665 */     StringBuilder stringBuilder = new StringBuilder();
/* 666 */     stringBuilder.append(this.text);
/* 667 */     if (this.argsNameKey == null) {
/* 668 */       if (this.choices != null) {
/* 669 */         String str = "{";
/* 670 */         for (Map.Entry<String, Boolean> entry : this.choices.entrySet()) {
/* 671 */           if (!((Boolean)entry.getValue()).booleanValue()) {
/* 672 */             stringBuilder.append(str);
/* 673 */             stringBuilder.append((String)entry.getKey());
/* 674 */             str = ",";
/*     */           } 
/*     */         } 
/* 677 */         stringBuilder.append("}");
/*     */       } 
/*     */     } else {
/* 680 */       if (!this.hasSuffix)
/* 681 */         stringBuilder.append(" "); 
/* 682 */       stringBuilder.append(paramLog.localize(Log.PrefixKind.JAVAC, this.argsNameKey, new Object[0]));
/*     */     } 
/*     */ 
/*     */     
/* 686 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum PkgInfo
/*     */   {
/* 698 */     ALWAYS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 706 */     LEGACY,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 711 */     NONEMPTY;
/*     */     
/*     */     public static PkgInfo get(Options param1Options) {
/* 714 */       String str = param1Options.get(Option.XPKGINFO);
/* 715 */       return (str == null) ? LEGACY : 
/*     */         
/* 717 */         valueOf(StringUtils.toUpperCase(str));
/*     */     }
/*     */   }
/*     */   
/*     */   private static Map<String, Boolean> getXLintChoices() {
/* 722 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/* 723 */     linkedHashMap.put("all", Boolean.valueOf(false));
/* 724 */     for (Lint.LintCategory lintCategory : Lint.LintCategory.values())
/* 725 */       linkedHashMap.put(lintCategory.option, Boolean.valueOf(lintCategory.hidden)); 
/* 726 */     for (Lint.LintCategory lintCategory : Lint.LintCategory.values())
/* 727 */       linkedHashMap.put("-" + lintCategory.option, Boolean.valueOf(lintCategory.hidden)); 
/* 728 */     linkedHashMap.put("none", Boolean.valueOf(false));
/* 729 */     return (Map)linkedHashMap;
/*     */   }
/*     */   
/*     */   static Set<Option> getJavaCompilerOptions() {
/* 733 */     return EnumSet.allOf(Option.class);
/*     */   }
/*     */   
/*     */   public static Set<Option> getJavacFileManagerOptions() {
/* 737 */     return getOptions(EnumSet.of(OptionGroup.FILEMANAGER));
/*     */   }
/*     */   
/*     */   public static Set<Option> getJavacToolOptions() {
/* 741 */     return getOptions(EnumSet.of(OptionGroup.BASIC));
/*     */   }
/*     */   
/*     */   static Set<Option> getOptions(Set<OptionGroup> paramSet) {
/* 745 */     EnumSet<Option> enumSet = EnumSet.noneOf(Option.class);
/* 746 */     for (Option option : values()) {
/* 747 */       if (paramSet.contains(option.group))
/* 748 */         enumSet.add(option); 
/* 749 */     }  return Collections.unmodifiableSet(enumSet);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\main\Option.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */