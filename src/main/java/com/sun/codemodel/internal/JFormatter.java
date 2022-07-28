/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JFormatter
/*     */ {
/*     */   private HashMap<String, ReferenceList> collectedReferences;
/*     */   private HashSet<JClass> importedClasses;
/*     */   
/*     */   private enum Mode
/*     */   {
/*  56 */     COLLECTING,
/*     */ 
/*     */ 
/*     */     
/*  60 */     PRINTING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private Mode mode = Mode.PRINTING;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int indentLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String indentSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PrintWriter pw;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char lastChar;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean atBeginningOfLine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPackage javaLang;
/*     */ 
/*     */ 
/*     */   
/*     */   static final char CLOSE_TYPE_ARGS = '￿';
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter(PrintWriter s) {
/* 107 */     this(s, "    ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter(Writer w) {
/* 115 */     this(new PrintWriter(w));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 122 */     this.pw.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrinting() {
/* 132 */     return (this.mode == Mode.PRINTING);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter o() {
/* 139 */     this.indentLevel--;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter i() {
/* 147 */     this.indentLevel++;
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   private boolean needSpace(char c1, char c2) {
/* 152 */     if (c1 == ']' && c2 == '{') return true; 
/* 153 */     if (c1 == ';') return true; 
/* 154 */     if (c1 == Character.MAX_VALUE) {
/*     */       
/* 156 */       if (c2 == '(')
/* 157 */         return false; 
/* 158 */       return true;
/*     */     } 
/* 160 */     if (c1 == ')' && c2 == '{') return true; 
/* 161 */     if (c1 == ',' || c1 == '=') return true; 
/* 162 */     if (c2 == '=') return true; 
/* 163 */     if (Character.isDigit(c1)) {
/* 164 */       if (c2 == '(' || c2 == ')' || c2 == ';' || c2 == ',')
/* 165 */         return false; 
/* 166 */       return true;
/*     */     } 
/* 168 */     if (Character.isJavaIdentifierPart(c1)) {
/* 169 */       switch (c2) {
/*     */         case '+':
/*     */         case '>':
/*     */         case '@':
/*     */         case '{':
/*     */         case '}':
/* 175 */           return true;
/*     */       } 
/* 177 */       return Character.isJavaIdentifierStart(c2);
/*     */     } 
/*     */     
/* 180 */     if (Character.isJavaIdentifierStart(c2)) {
/* 181 */       switch (c1) {
/*     */         case ')':
/*     */         case '+':
/*     */         case ']':
/*     */         case '}':
/* 186 */           return true;
/*     */       } 
/* 188 */       return false;
/*     */     } 
/*     */     
/* 191 */     if (Character.isDigit(c2)) {
/* 192 */       if (c1 == '(') return false; 
/* 193 */       return true;
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */   
/* 198 */   public JFormatter(PrintWriter s, String space) { this.lastChar = Character.MIN_VALUE;
/* 199 */     this.atBeginningOfLine = true; this.pw = s;
/*     */     this.indentSpace = space;
/*     */     this.collectedReferences = new HashMap<>();
/* 202 */     this.importedClasses = new HashSet<>(); } private void spaceIfNeeded(char c) { if (this.atBeginningOfLine) {
/* 203 */       for (int i = 0; i < this.indentLevel; i++)
/* 204 */         this.pw.print(this.indentSpace); 
/* 205 */       this.atBeginningOfLine = false;
/* 206 */     } else if (this.lastChar != '\000' && needSpace(this.lastChar, c)) {
/* 207 */       this.pw.print(' ');
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter p(char c) {
/* 216 */     if (this.mode == Mode.PRINTING) {
/* 217 */       if (c == Character.MAX_VALUE) {
/* 218 */         this.pw.print('>');
/*     */       } else {
/* 220 */         spaceIfNeeded(c);
/* 221 */         this.pw.print(c);
/*     */       } 
/* 223 */       this.lastChar = c;
/*     */     } 
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter p(String s) {
/* 234 */     if (this.mode == Mode.PRINTING) {
/* 235 */       spaceIfNeeded(s.charAt(0));
/* 236 */       this.pw.print(s);
/* 237 */       this.lastChar = s.charAt(s.length() - 1);
/*     */     } 
/* 239 */     return this;
/*     */   }
/*     */   
/*     */   public JFormatter t(JType type) {
/* 243 */     if (type.isReference()) {
/* 244 */       return t((JClass)type);
/*     */     }
/* 246 */     return g(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter t(JClass type) {
/*     */     String shortName;
/*     */     ReferenceList tl;
/* 258 */     switch (this.mode) {
/*     */ 
/*     */       
/*     */       case PRINTING:
/* 262 */         if (this.importedClasses.contains(type)) {
/* 263 */           p(type.name()); break;
/*     */         } 
/* 265 */         if (type.outer() != null) {
/* 266 */           t(type.outer()).p('.').p(type.name()); break;
/*     */         } 
/* 268 */         p(type.fullName());
/*     */         break;
/*     */       
/*     */       case COLLECTING:
/* 272 */         shortName = type.name();
/* 273 */         if (this.collectedReferences.containsKey(shortName)) {
/* 274 */           ((ReferenceList)this.collectedReferences.get(shortName)).add(type); break;
/*     */         } 
/* 276 */         tl = new ReferenceList();
/* 277 */         tl.add(type);
/* 278 */         this.collectedReferences.put(shortName, tl);
/*     */         break;
/*     */     } 
/*     */     
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter id(String id) {
/*     */     ReferenceList tl;
/* 289 */     switch (this.mode) {
/*     */       case PRINTING:
/* 291 */         p(id);
/*     */         break;
/*     */       
/*     */       case COLLECTING:
/* 295 */         if (this.collectedReferences.containsKey(id)) {
/* 296 */           if (!((ReferenceList)this.collectedReferences.get(id)).getClasses().isEmpty()) {
/* 297 */             for (JClass type : ((ReferenceList)this.collectedReferences.get(id)).getClasses()) {
/* 298 */               if (type.outer() != null) {
/* 299 */                 ((ReferenceList)this.collectedReferences.get(id)).setId(false);
/* 300 */                 return this;
/*     */               } 
/*     */             } 
/*     */           }
/* 304 */           ((ReferenceList)this.collectedReferences.get(id)).setId(true);
/*     */           
/*     */           break;
/*     */         } 
/* 308 */         tl = new ReferenceList();
/* 309 */         tl.setId(true);
/* 310 */         this.collectedReferences.put(id, tl);
/*     */         break;
/*     */     } 
/*     */     
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter nl() {
/* 321 */     if (this.mode == Mode.PRINTING) {
/* 322 */       this.pw.println();
/* 323 */       this.lastChar = Character.MIN_VALUE;
/* 324 */       this.atBeginningOfLine = true;
/*     */     } 
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter g(JGenerable g) {
/* 335 */     g.generate(this);
/* 336 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter g(Collection<? extends JGenerable> list) {
/* 343 */     boolean first = true;
/* 344 */     if (!list.isEmpty()) {
/* 345 */       for (JGenerable item : list) {
/* 346 */         if (!first)
/* 347 */           p(','); 
/* 348 */         g(item);
/* 349 */         first = false;
/*     */       } 
/*     */     }
/* 352 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter d(JDeclaration d) {
/* 361 */     d.declare(this);
/* 362 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter s(JStatement s) {
/* 371 */     s.state(this);
/* 372 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter b(JVar v) {
/* 381 */     v.bind(this);
/* 382 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void write(JDefinedClass c) {
/* 390 */     this.mode = Mode.COLLECTING;
/* 391 */     d(c);
/*     */     
/* 393 */     this.javaLang = c.owner()._package("java.lang");
/*     */ 
/*     */     
/* 396 */     for (ReferenceList tl : this.collectedReferences.values()) {
/* 397 */       if (!tl.collisions(c) && !tl.isId()) {
/* 398 */         assert tl.getClasses().size() == 1;
/*     */ 
/*     */         
/* 401 */         this.importedClasses.add(tl.getClasses().get(0));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 406 */     this.importedClasses.add(c);
/*     */ 
/*     */     
/* 409 */     this.mode = Mode.PRINTING;
/*     */     
/* 411 */     assert c.parentContainer().isPackage() : "this method is only for a pacakge-level class";
/* 412 */     JPackage pkg = (JPackage)c.parentContainer();
/* 413 */     if (!pkg.isUnnamed()) {
/* 414 */       nl().d(pkg);
/* 415 */       nl();
/*     */     } 
/*     */ 
/*     */     
/* 419 */     JClass[] imports = (JClass[])this.importedClasses.toArray((Object[])new JClass[this.importedClasses.size()]);
/* 420 */     Arrays.sort((Object[])imports);
/* 421 */     for (JClass clazz : imports) {
/*     */ 
/*     */ 
/*     */       
/* 425 */       if (!supressImport(clazz, c)) {
/* 426 */         if (clazz instanceof JNarrowedClass) {
/* 427 */           clazz = clazz.erasure();
/*     */         }
/*     */         
/* 430 */         p("import").p(clazz.fullName()).p(';').nl();
/*     */       } 
/*     */     } 
/* 433 */     nl();
/*     */     
/* 435 */     d(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean supressImport(JClass clazz, JClass c) {
/* 446 */     if (clazz instanceof JNarrowedClass) {
/* 447 */       clazz = clazz.erasure();
/*     */     }
/* 449 */     if (clazz instanceof JAnonymousClass) {
/* 450 */       clazz = clazz._extends();
/*     */     }
/*     */     
/* 453 */     if (clazz._package().isUnnamed()) {
/* 454 */       return true;
/*     */     }
/* 456 */     String packageName = clazz._package().name();
/* 457 */     if (packageName.equals("java.lang")) {
/* 458 */       return true;
/*     */     }
/* 460 */     if (clazz._package() == c._package())
/*     */     {
/*     */ 
/*     */       
/* 464 */       if (clazz.outer() == null) {
/* 465 */         return true;
/*     */       }
/*     */     }
/* 468 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final class ReferenceList
/*     */   {
/*     */     private final ArrayList<JClass> classes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ReferenceList() {
/* 491 */       this.classes = new ArrayList<>();
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
/*     */     public boolean collisions(JDefinedClass enclosingClass) {
/* 504 */       if (this.classes.size() > 1) {
/* 505 */         return true;
/*     */       }
/*     */       
/* 508 */       if (this.id && this.classes.size() != 0) {
/* 509 */         return true;
/*     */       }
/* 511 */       for (JClass c : this.classes) {
/* 512 */         if (c instanceof JAnonymousClass) {
/* 513 */           c = c._extends();
/*     */         }
/* 515 */         if (c._package() == JFormatter.this.javaLang) {
/*     */           
/* 517 */           Iterator<JDefinedClass> itr = enclosingClass._package().classes();
/* 518 */           while (itr.hasNext()) {
/*     */ 
/*     */ 
/*     */             
/* 522 */             JDefinedClass n = itr.next();
/* 523 */             if (n.name().equals(c.name()))
/* 524 */               return true; 
/*     */           } 
/*     */         } 
/* 527 */         if (c.outer() != null) {
/* 528 */           return true;
/*     */         }
/*     */       } 
/* 531 */       return false;
/*     */     }
/*     */     
/*     */     public void add(JClass clazz) {
/* 535 */       if (!this.classes.contains(clazz))
/* 536 */         this.classes.add(clazz); 
/*     */     }
/*     */     
/*     */     public List<JClass> getClasses() {
/* 540 */       return this.classes;
/*     */     }
/*     */     
/*     */     public void setId(boolean value) {
/* 544 */       this.id = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isId() {
/* 552 */       return (this.id && this.classes.size() == 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */