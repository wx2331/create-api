/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.tools.javac.code.Printer;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.JavacMessages;
/*     */ import com.sun.tools.javac.util.LayoutCharacters;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.File;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SeeTagImpl
/*     */   extends TagImpl
/*     */   implements SeeTag, LayoutCharacters
/*     */ {
/*     */   private String where;
/*     */   private String what;
/*     */   private PackageDoc referencedPackage;
/*     */   private ClassDoc referencedClass;
/*     */   private MemberDoc referencedMember;
/*  71 */   String label = ""; private static final boolean showRef = false;
/*     */   
/*     */   SeeTagImpl(DocImpl paramDocImpl, String paramString1, String paramString2) {
/*  74 */     super(paramDocImpl, paramString1, paramString2);
/*  75 */     parseSeeString();
/*  76 */     if (this.where != null) {
/*  77 */       ClassDocImpl classDocImpl = null;
/*  78 */       if (paramDocImpl instanceof MemberDoc) {
/*     */         
/*  80 */         classDocImpl = (ClassDocImpl)((ProgramElementDoc)paramDocImpl).containingClass();
/*  81 */       } else if (paramDocImpl instanceof ClassDoc) {
/*  82 */         classDocImpl = (ClassDocImpl)paramDocImpl;
/*     */       } 
/*  84 */       findReferenced(classDocImpl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void showRef() {
/*     */     Symbol.PackageSymbol packageSymbol;
/*  93 */     if (this.referencedMember != null) {
/*  94 */       if (this.referencedMember instanceof MethodDocImpl)
/*  95 */       { Symbol.MethodSymbol methodSymbol = ((MethodDocImpl)this.referencedMember).sym; }
/*  96 */       else if (this.referencedMember instanceof FieldDocImpl)
/*  97 */       { Symbol.VarSymbol varSymbol = ((FieldDocImpl)this.referencedMember).sym; }
/*     */       else
/*  99 */       { Symbol.MethodSymbol methodSymbol = ((ConstructorDocImpl)this.referencedMember).sym; } 
/* 100 */     } else if (this.referencedClass != null) {
/* 101 */       Symbol.ClassSymbol classSymbol = ((ClassDocImpl)this.referencedClass).tsym;
/* 102 */     } else if (this.referencedPackage != null) {
/* 103 */       packageSymbol = ((PackageDocImpl)this.referencedPackage).sym;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 107 */     final JavacMessages messages = JavacMessages.instance((docenv()).context);
/* 108 */     Locale locale = Locale.getDefault();
/* 109 */     Printer printer = new Printer() {
/*     */         int count;
/*     */         
/*     */         protected String localize(Locale param1Locale, String param1String, Object... param1VarArgs) {
/* 113 */           return messages.getLocalizedString(param1Locale, param1String, param1VarArgs);
/*     */         }
/*     */         
/*     */         protected String capturedVarId(Type.CapturedType param1CapturedType, Locale param1Locale) {
/* 117 */           return "CAP#" + ++this.count;
/*     */         }
/*     */       };
/*     */     
/* 121 */     String str1 = this.text.replaceAll("\\s+", " ");
/* 122 */     int i = str1.indexOf(" ");
/* 123 */     int j = str1.indexOf("(");
/* 124 */     int k = str1.indexOf(")");
/*     */ 
/*     */     
/* 127 */     String str2 = (i == -1) ? str1 : ((j == -1 || i < j) ? str1.substring(0, i) : str1.substring(0, k + 1));
/*     */     
/* 129 */     File file = new File(this.holder.position().file().getAbsoluteFile().toURI().normalize());
/*     */     
/* 131 */     StringBuilder stringBuilder = new StringBuilder();
/* 132 */     stringBuilder.append("+++ ").append(file).append(": ")
/* 133 */       .append(name()).append(" ").append(str2).append(": ");
/* 134 */     stringBuilder.append(packageSymbol.getKind()).append(" ");
/* 135 */     if (((Symbol)packageSymbol).kind == 16 || ((Symbol)packageSymbol).kind == 4)
/* 136 */       stringBuilder.append(printer.visit(((Symbol)packageSymbol).owner, locale)).append("."); 
/* 137 */     stringBuilder.append(printer.visit((Symbol)packageSymbol, locale));
/*     */     
/* 139 */     System.err.println(stringBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String referencedClassName() {
/* 150 */     return this.where;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageDoc referencedPackage() {
/* 160 */     return this.referencedPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc referencedClass() {
/* 170 */     return this.referencedClass;
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
/*     */   public String referencedMemberName() {
/* 182 */     return this.what;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberDoc referencedMember() {
/* 193 */     return this.referencedMember;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseSeeString() {
/* 201 */     int i = this.text.length();
/* 202 */     if (i == 0) {
/*     */       return;
/*     */     }
/* 205 */     switch (this.text.charAt(0)) {
/*     */       case '<':
/* 207 */         if (this.text.charAt(i - 1) != '>') {
/* 208 */           docenv().warning(this.holder, "tag.see.no_close_bracket_on_url", this.name, this.text);
/*     */         }
/*     */         return;
/*     */ 
/*     */       
/*     */       case '"':
/* 214 */         if (i == 1 || this.text.charAt(i - 1) != '"') {
/* 215 */           docenv().warning(this.holder, "tag.see.no_close_quote", this.name, this.text);
/*     */         }
/*     */         return;
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
/* 230 */     byte b1 = 0;
/* 231 */     int j = 0;
/* 232 */     byte b2 = 0;
/*     */     int k;
/* 234 */     for (k = b2; k < i; k += Character.charCount(n)) {
/* 235 */       int n = this.text.codePointAt(k);
/* 236 */       switch (n) { case 40:
/* 237 */           b1++; break;
/* 238 */         case 41: b1--; break;
/*     */         case 35: case 46: case 91: case 93: break;
/*     */         case 44:
/* 241 */           if (b1 <= 0) {
/* 242 */             docenv().warning(this.holder, "tag.see.malformed_see_tag", this.name, this.text); return;
/*     */           } 
/*     */           break;
/*     */         case 9:
/*     */         case 10:
/*     */         case 13:
/*     */         case 32:
/* 249 */           if (b1 == 0) {
/* 250 */             j = k;
/* 251 */             k = i;
/*     */           } 
/*     */           break;
/*     */         default:
/* 255 */           if (!Character.isJavaIdentifierPart(n)) {
/* 256 */             docenv().warning(this.holder, "tag.see.illegal_character", this.name, "" + n, this.text);
/*     */           }
/*     */           break; }
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 263 */     if (b1 != 0) {
/* 264 */       docenv().warning(this.holder, "tag.see.malformed_see_tag", this.name, this.text);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 270 */     String str1 = "";
/* 271 */     String str2 = "";
/*     */     
/* 273 */     if (j > 0) {
/* 274 */       str1 = this.text.substring(b2, j);
/* 275 */       str2 = this.text.substring(j + 1);
/*     */ 
/*     */       
/* 278 */       for (byte b = 0; b < str2.length(); b++) {
/* 279 */         char c = str2.charAt(b);
/* 280 */         if (c != ' ' && c != '\t' && c != '\n') {
/* 281 */           this.label = str2.substring(b);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 286 */       str1 = this.text;
/* 287 */       this.label = "";
/*     */     } 
/*     */     
/* 290 */     int m = str1.indexOf('#');
/* 291 */     if (m >= 0) {
/*     */       
/* 293 */       this.where = str1.substring(0, m);
/* 294 */       this.what = str1.substring(m + 1);
/*     */     }
/* 296 */     else if (str1.indexOf('(') >= 0) {
/* 297 */       docenv().warning(this.holder, "tag.see.missing_sharp", this.name, this.text);
/*     */ 
/*     */       
/* 300 */       this.where = "";
/* 301 */       this.what = str1;
/*     */     }
/*     */     else {
/*     */       
/* 305 */       this.where = str1;
/* 306 */       this.what = null;
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
/*     */   private void findReferenced(ClassDocImpl paramClassDocImpl) {
/* 319 */     if (this.where.length() > 0) {
/* 320 */       if (paramClassDocImpl != null) {
/* 321 */         this.referencedClass = paramClassDocImpl.findClass(this.where);
/*     */       } else {
/* 323 */         this.referencedClass = docenv().lookupClass(this.where);
/*     */       } 
/* 325 */       if (this.referencedClass == null && holder() instanceof ProgramElementDoc) {
/* 326 */         this.referencedClass = docenv().lookupClass(((ProgramElementDoc)
/* 327 */             holder()).containingPackage().name() + "." + this.where);
/*     */       }
/*     */       
/* 330 */       if (this.referencedClass == null) {
/*     */         
/* 332 */         this.referencedPackage = docenv().lookupPackage(this.where);
/*     */         return;
/*     */       } 
/*     */     } else {
/* 336 */       if (paramClassDocImpl == null) {
/* 337 */         docenv().warning(this.holder, "tag.see.class_not_specified", this.name, this.text);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 342 */       this.referencedClass = paramClassDocImpl;
/*     */     } 
/*     */     
/* 345 */     this.where = this.referencedClass.qualifiedName();
/*     */     
/* 347 */     if (this.what == null) {
/*     */       return;
/*     */     }
/* 350 */     int i = this.what.indexOf('(');
/* 351 */     String str = (i >= 0) ? this.what.substring(0, i) : this.what;
/*     */     
/* 353 */     if (i > 0) {
/*     */ 
/*     */       
/* 356 */       String[] arrayOfString = (new ParameterParseMachine(this.what.substring(i, this.what.length()))).parseParameters();
/* 357 */       if (arrayOfString != null) {
/* 358 */         this.referencedMember = findExecutableMember(str, arrayOfString, this.referencedClass);
/*     */       } else {
/*     */         
/* 361 */         this.referencedMember = null;
/*     */       } 
/*     */     } else {
/*     */       
/* 365 */       this.referencedMember = findExecutableMember(str, (String[])null, this.referencedClass);
/*     */ 
/*     */       
/* 368 */       FieldDoc fieldDoc = ((ClassDocImpl)this.referencedClass).findField(str);
/*     */       
/* 370 */       if (this.referencedMember == null || (fieldDoc != null && fieldDoc
/*     */         
/* 372 */         .containingClass()
/* 373 */         .subclassOf(this.referencedMember.containingClass()))) {
/* 374 */         this.referencedMember = (MemberDoc)fieldDoc;
/*     */       }
/*     */     } 
/* 377 */     if (this.referencedMember == null) {
/* 378 */       docenv().warning(this.holder, "tag.see.can_not_find_member", this.name, this.what, this.where);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberDoc findReferencedMethod(String paramString, String[] paramArrayOfString, ClassDoc paramClassDoc) {
/* 387 */     MemberDoc memberDoc = findExecutableMember(paramString, paramArrayOfString, paramClassDoc);
/* 388 */     ClassDoc[] arrayOfClassDoc = paramClassDoc.innerClasses();
/* 389 */     if (memberDoc == null) {
/* 390 */       for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 391 */         memberDoc = findReferencedMethod(paramString, paramArrayOfString, arrayOfClassDoc[b]);
/* 392 */         if (memberDoc != null) {
/* 393 */           return memberDoc;
/*     */         }
/*     */       } 
/*     */     }
/* 397 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private MemberDoc findExecutableMember(String paramString, String[] paramArrayOfString, ClassDoc paramClassDoc) {
/* 402 */     if (paramString.equals(paramClassDoc.name())) {
/* 403 */       return (MemberDoc)((ClassDocImpl)paramClassDoc).findConstructor(paramString, paramArrayOfString);
/*     */     }
/*     */     
/* 406 */     return ((ClassDocImpl)paramClassDoc).findMethod(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   class ParameterParseMachine
/*     */   {
/*     */     static final int START = 0;
/*     */     
/*     */     static final int TYPE = 1;
/*     */     
/*     */     static final int NAME = 2;
/*     */     
/*     */     static final int TNSPACE = 3;
/*     */     
/*     */     static final int ARRAYDECORATION = 4;
/*     */     
/*     */     static final int ARRAYSPACE = 5;
/*     */     
/*     */     String parameters;
/*     */     StringBuilder typeId;
/*     */     ListBuffer<String> paramList;
/*     */     
/*     */     ParameterParseMachine(String param1String) {
/* 429 */       this.parameters = param1String;
/* 430 */       this.paramList = new ListBuffer();
/* 431 */       this.typeId = new StringBuilder();
/*     */     }
/*     */     
/*     */     public String[] parseParameters() {
/* 435 */       if (this.parameters.equals("()")) {
/* 436 */         return new String[0];
/*     */       }
/* 438 */       byte b1 = 0;
/* 439 */       byte b2 = 0;
/* 440 */       this.parameters = this.parameters.substring(1, this.parameters.length() - 1);
/*     */       int i;
/* 442 */       for (i = 0; i < this.parameters.length(); i += Character.charCount(j)) {
/* 443 */         int j = this.parameters.codePointAt(i);
/* 444 */         switch (b1) {
/*     */           case false:
/* 446 */             if (Character.isJavaIdentifierStart(j)) {
/* 447 */               this.typeId.append(Character.toChars(j));
/* 448 */               b1 = 1;
/*     */             } 
/* 450 */             b2 = 0;
/*     */             break;
/*     */           case true:
/* 453 */             if (Character.isJavaIdentifierPart(j) || j == 46) {
/* 454 */               this.typeId.append(Character.toChars(j));
/* 455 */             } else if (j == 91) {
/* 456 */               this.typeId.append('[');
/* 457 */               b1 = 4;
/* 458 */             } else if (Character.isWhitespace(j)) {
/* 459 */               b1 = 3;
/* 460 */             } else if (j == 44) {
/* 461 */               addTypeToParamList();
/* 462 */               b1 = 0;
/*     */             } 
/* 464 */             b2 = 1;
/*     */             break;
/*     */           case true:
/* 467 */             if (Character.isJavaIdentifierStart(j)) {
/* 468 */               if (b2 == 4) {
/* 469 */                 SeeTagImpl.this.docenv().warning(SeeTagImpl.this.holder, "tag.missing_comma_space", SeeTagImpl.this.name, "(" + this.parameters + ")");
/*     */ 
/*     */ 
/*     */                 
/* 473 */                 return (String[])null;
/*     */               } 
/* 475 */               addTypeToParamList();
/* 476 */               b1 = 2;
/* 477 */             } else if (j == 91) {
/* 478 */               this.typeId.append('[');
/* 479 */               b1 = 4;
/* 480 */             } else if (j == 44) {
/* 481 */               addTypeToParamList();
/* 482 */               b1 = 0;
/*     */             } 
/* 484 */             b2 = 3;
/*     */             break;
/*     */           case true:
/* 487 */             if (j == 93) {
/* 488 */               this.typeId.append(']');
/* 489 */               b1 = 3;
/* 490 */             } else if (!Character.isWhitespace(j)) {
/* 491 */               SeeTagImpl.this.docenv().warning(SeeTagImpl.this.holder, "tag.illegal_char_in_arr_dim", SeeTagImpl.this.name, "(" + this.parameters + ")");
/*     */ 
/*     */ 
/*     */               
/* 495 */               return (String[])null;
/*     */             } 
/* 497 */             b2 = 4;
/*     */             break;
/*     */           case true:
/* 500 */             if (j == 44) {
/* 501 */               b1 = 0;
/*     */             }
/* 503 */             b2 = 2;
/*     */             break;
/*     */         } 
/*     */       } 
/* 507 */       if (b1 == 4 || (b1 == 0 && b2 == 3))
/*     */       {
/* 509 */         SeeTagImpl.this.docenv().warning(SeeTagImpl.this.holder, "tag.illegal_see_tag", "(" + this.parameters + ")");
/*     */       }
/*     */ 
/*     */       
/* 513 */       if (this.typeId.length() > 0) {
/* 514 */         this.paramList.append(this.typeId.toString());
/*     */       }
/* 516 */       return (String[])this.paramList.toArray((Object[])new String[this.paramList.length()]);
/*     */     }
/*     */     
/*     */     void addTypeToParamList() {
/* 520 */       if (this.typeId.length() > 0) {
/* 521 */         this.paramList.append(this.typeId.toString());
/* 522 */         this.typeId.setLength(0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String kind() {
/* 532 */     return "@see";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String label() {
/* 539 */     return this.label;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\SeeTagImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */