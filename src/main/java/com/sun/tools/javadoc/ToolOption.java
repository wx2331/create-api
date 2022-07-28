/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ToolOption
/*     */ {
/*  45 */   BOOTCLASSPATH("-bootclasspath", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  48 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/*  52 */   CLASSPATH("-classpath", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  55 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/*  59 */   CP("-cp", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  62 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/*  66 */   EXTDIRS("-extdirs", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  69 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/*  73 */   SOURCEPATH("-sourcepath", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  76 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/*  80 */   SYSCLASSPATH("-sysclasspath", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  83 */       param1Helper.setCompilerOpt("-bootclasspath", param1String);
/*     */     }
/*     */   },
/*     */   
/*  87 */   ENCODING("-encoding", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  90 */       param1Helper.encoding = param1String;
/*  91 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/*  95 */   SOURCE("-source", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/*  98 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/* 102 */   XMAXERRS("-Xmaxerrs", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/* 105 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */   
/* 109 */   XMAXWARNS("-Xmaxwarns", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/* 112 */       param1Helper.setCompilerOpt(this.opt, param1String);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */   
/* 118 */   DOCLET("-doclet", true),
/*     */   
/* 120 */   DOCLETPATH("-docletpath", true),
/*     */ 
/*     */ 
/*     */   
/* 124 */   SUBPACKAGES("-subpackages", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/* 127 */       param1Helper.addToList(param1Helper.subPackages, param1String);
/*     */     }
/*     */   },
/*     */   
/* 131 */   EXCLUDE("-exclude", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/* 134 */       param1Helper.addToList(param1Helper.excludedPackages, param1String);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */   
/* 140 */   PACKAGE("-package")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 143 */       param1Helper.setFilter(-9223372036854775803L);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/* 148 */   PRIVATE("-private")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 151 */       param1Helper.setFilter(-9223372036854775801L);
/*     */     }
/*     */   },
/*     */   
/* 155 */   PROTECTED("-protected")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 158 */       param1Helper.setFilter(5L);
/*     */     }
/*     */   },
/*     */   
/* 162 */   PUBLIC("-public")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 165 */       param1Helper.setFilter(1L);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */   
/* 171 */   PROMPT("-prompt")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 174 */       param1Helper.compOpts.put("-prompt", "-prompt");
/* 175 */       param1Helper.promptOnError = true;
/*     */     }
/*     */   },
/*     */   
/* 179 */   QUIET("-quiet")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 182 */       param1Helper.quiet = true;
/*     */     }
/*     */   },
/*     */   
/* 186 */   VERBOSE("-verbose")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 189 */       param1Helper.compOpts.put("-verbose", "");
/*     */     }
/*     */   },
/*     */   
/* 193 */   XWERROR("-Xwerror")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 196 */       param1Helper.rejectWarnings = true;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   BREAKITERATOR("-breakiterator")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 206 */       param1Helper.breakiterator = true;
/*     */     }
/*     */   },
/*     */   
/* 210 */   LOCALE("-locale", true)
/*     */   {
/*     */     public void process(Helper param1Helper, String param1String) {
/* 213 */       param1Helper.docLocale = param1String;
/*     */     }
/*     */   },
/*     */   
/* 217 */   OVERVIEW("-overview", true),
/*     */   
/* 219 */   XCLASSES("-Xclasses")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 222 */       param1Helper.docClasses = true;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   HELP("-help")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 232 */       param1Helper.usage();
/*     */     }
/*     */   },
/*     */   
/* 236 */   X("-X")
/*     */   {
/*     */     public void process(Helper param1Helper) {
/* 239 */       param1Helper.Xusage();
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */   
/*     */   public final String opt;
/*     */   
/*     */   public final boolean hasArg;
/*     */ 
/*     */   
/*     */   ToolOption(String paramString1, boolean paramBoolean) {
/* 251 */     this.opt = paramString1;
/* 252 */     this.hasArg = paramBoolean;
/*     */   }
/*     */   
/*     */   void process(Helper paramHelper, String paramString) {}
/*     */   
/*     */   void process(Helper paramHelper) {}
/*     */   
/*     */   static ToolOption get(String paramString) {
/* 260 */     for (ToolOption toolOption : values()) {
/* 261 */       if (paramString.equals(toolOption.opt))
/* 262 */         return toolOption; 
/*     */     } 
/* 264 */     return null;
/*     */   }
/*     */   static abstract class Helper { final ListBuffer<String[]> options; final ListBuffer<String> subPackages; final ListBuffer<String> excludedPackages;
/*     */     
/*     */     Helper() {
/* 269 */       this.options = new ListBuffer();
/*     */ 
/*     */       
/* 272 */       this.subPackages = new ListBuffer();
/*     */ 
/*     */       
/* 275 */       this.excludedPackages = new ListBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 281 */       this.encoding = null;
/*     */ 
/*     */       
/* 284 */       this.breakiterator = false;
/*     */ 
/*     */       
/* 287 */       this.quiet = false;
/*     */ 
/*     */       
/* 290 */       this.docClasses = false;
/*     */ 
/*     */       
/* 293 */       this.rejectWarnings = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 299 */       this.docLocale = "";
/*     */ 
/*     */       
/* 302 */       this.showAccess = null;
/*     */     }
/*     */     Options compOpts; String encoding; boolean breakiterator; boolean quiet; boolean docClasses; boolean rejectWarnings;
/*     */     boolean promptOnError;
/*     */     String docLocale;
/*     */     ModifierFilter showAccess;
/*     */     
/*     */     protected void addToList(ListBuffer<String> param1ListBuffer, String param1String) {
/* 310 */       StringTokenizer stringTokenizer = new StringTokenizer(param1String, ":");
/*     */       
/* 312 */       while (stringTokenizer.hasMoreTokens()) {
/* 313 */         String str = stringTokenizer.nextToken();
/* 314 */         param1ListBuffer.append(str);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void setFilter(long param1Long) {
/* 319 */       if (this.showAccess != null) {
/* 320 */         usageError("main.incompatible.access.flags", new Object[0]);
/*     */       }
/* 322 */       this.showAccess = new ModifierFilter(param1Long);
/*     */     }
/*     */     
/*     */     private void setCompilerOpt(String param1String1, String param1String2) {
/* 326 */       if (this.compOpts.get(param1String1) != null) {
/* 327 */         usageError("main.option.already.seen", new Object[] { param1String1 });
/*     */       }
/* 329 */       this.compOpts.put(param1String1, param1String2);
/*     */     }
/*     */     
/*     */     abstract void usage();
/*     */     
/*     */     abstract void Xusage();
/*     */     
/*     */     abstract void usageError(String param1String, Object... param1VarArgs); }
/*     */ 
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ToolOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */