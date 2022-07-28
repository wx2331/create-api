/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ParseException
/*     */   extends Exception
/*     */ {
/*     */   ParseException(String paramString) {
/*  63 */     super(paramString);
/*  64 */     System.err.println(paramString);
/*  65 */     detected = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParseException(String paramString, boolean paramBoolean) {
/*  75 */     super(paramString);
/*  76 */     System.err.println(paramString);
/*  77 */     if (!paramBoolean) {
/*  78 */       detected = true;
/*     */     }
/*     */   }
/*     */   
/*     */   static ParseException abstractValueBox(Scanner paramScanner) {
/*  83 */     return arg0("abstractValueBox", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException alreadyDeclared(Scanner paramScanner, String paramString) {
/*  88 */     return arg1("alreadyDeclared", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException declNotInSameFile(Scanner paramScanner, String paramString1, String paramString2) {
/*  94 */     return arg2("declNotInSameFile", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException alreadyDefaulted(Scanner paramScanner) {
/*  99 */     return arg0("alreadydefaulted", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException alreadyDerived(Scanner paramScanner, String paramString1, String paramString2) {
/* 104 */     return arg2("alreadyDerived", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException alreadyRaised(Scanner paramScanner, String paramString) {
/* 109 */     return arg1("alreadyRaised", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException attributeNotType(Scanner paramScanner, String paramString) {
/* 115 */     return arg1("attributeNotType", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException badAbstract(Scanner paramScanner, String paramString) {
/* 120 */     return arg1("badAbstract", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException badCustom(Scanner paramScanner) {
/* 125 */     return arg0("badCustom", paramScanner);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException badRepIDAlreadyAssigned(Scanner paramScanner, String paramString) {
/* 131 */     return arg1("badRepIDAlreadyAssigned", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException badRepIDForm(Scanner paramScanner, String paramString) {
/* 137 */     return arg1("badRepIDForm", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException badRepIDPrefix(Scanner paramScanner, String paramString1, String paramString2, String paramString3) {
/* 143 */     return arg3("badRepIDPrefix", paramScanner, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException badState(Scanner paramScanner, String paramString) {
/* 148 */     return arg1("badState", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException branchLabel(Scanner paramScanner, String paramString) {
/* 153 */     return arg1("branchLabel", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException branchName(Scanner paramScanner, String paramString) {
/* 158 */     return arg1("branchName", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException duplicateInit(Scanner paramScanner) {
/* 163 */     return arg0("duplicateInit", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException duplicateState(Scanner paramScanner, String paramString) {
/* 168 */     return arg1("duplicateState", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException elseNoIf(Scanner paramScanner) {
/* 173 */     return arg0("elseNoIf", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException endNoIf(Scanner paramScanner) {
/* 178 */     return arg0("endNoIf", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException evaluationError(Scanner paramScanner, String paramString) {
/* 183 */     return arg1("evaluation", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException forwardEntry(Scanner paramScanner, String paramString) {
/* 188 */     return arg1("forwardEntry", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException forwardedValueBox(Scanner paramScanner, String paramString) {
/* 194 */     return arg1("forwardedValueBox", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException generic(Scanner paramScanner, String paramString) {
/* 199 */     return arg1("generic", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException illegalArray(Scanner paramScanner, String paramString) {
/* 204 */     return arg1("illegalArray", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException illegalException(Scanner paramScanner, String paramString) {
/* 209 */     return arg1("illegalException", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException invalidConst(Scanner paramScanner, String paramString1, String paramString2) {
/* 214 */     return arg2("invalidConst1", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException invalidConst(Scanner paramScanner, String paramString) {
/* 219 */     return arg1("invalidConst2", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException keywordCollision(Scanner paramScanner, String paramString) {
/* 225 */     return arg1("keywordCollision", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException deprecatedKeywordWarning(Scanner paramScanner, String paramString) {
/* 231 */     return arg1Warning("deprecatedKeywordWarning", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException keywordCollisionWarning(Scanner paramScanner, String paramString) {
/* 237 */     return arg1Warning("keywordCollisionWarning", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException methodClash(Scanner paramScanner, String paramString1, String paramString2) {
/* 242 */     return arg2("methodClash", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException moduleNotType(Scanner paramScanner, String paramString) {
/* 247 */     return arg1("moduleNotType", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException nestedValueBox(Scanner paramScanner) {
/* 253 */     return arg0("nestedValueBox", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException noDefault(Scanner paramScanner) {
/* 258 */     return arg0("noDefault", paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException nonAbstractParent(Scanner paramScanner, String paramString1, String paramString2) {
/* 263 */     return arg2("nonAbstractParent", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException nonAbstractParent2(Scanner paramScanner, String paramString1, String paramString2) {
/* 268 */     return arg2("nonAbstractParent2", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException nonAbstractParent3(Scanner paramScanner, String paramString1, String paramString2) {
/* 273 */     return arg2("nonAbstractParent3", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException notANumber(Scanner paramScanner, String paramString) {
/* 278 */     return arg1("notANumber", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException nothing(String paramString) {
/* 283 */     return new ParseException(Util.getMessage("ParseException.nothing", paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException notPositiveInt(Scanner paramScanner, String paramString) {
/* 288 */     return arg1("notPosInt", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException oneway(Scanner paramScanner, String paramString) {
/* 293 */     return arg1("oneway", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException operationNotType(Scanner paramScanner, String paramString) {
/* 299 */     return arg1("operationNotType", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException outOfRange(Scanner paramScanner, String paramString1, String paramString2) {
/* 304 */     return arg2("outOfRange", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException recursive(Scanner paramScanner, String paramString1, String paramString2) {
/* 309 */     return arg2("recursive", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException selfInherit(Scanner paramScanner, String paramString) {
/* 314 */     return arg1("selfInherit", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException stringTooLong(Scanner paramScanner, String paramString1, String paramString2) {
/* 319 */     return arg2("stringTooLong", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException syntaxError(Scanner paramScanner, int paramInt1, int paramInt2) {
/* 324 */     return arg2("syntax1", paramScanner, Token.toString(paramInt1), Token.toString(paramInt2));
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException syntaxError(Scanner paramScanner, String paramString1, String paramString2) {
/* 329 */     return arg2("syntax1", paramScanner, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException syntaxError(Scanner paramScanner, int[] paramArrayOfint, int paramInt) {
/* 334 */     return syntaxError(paramScanner, paramArrayOfint, Token.toString(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException syntaxError(Scanner paramScanner, int[] paramArrayOfint, String paramString) {
/* 339 */     String str = "";
/* 340 */     for (byte b = 0; b < paramArrayOfint.length; b++)
/* 341 */       str = str + " `" + Token.toString(paramArrayOfint[b]) + "'"; 
/* 342 */     return arg2("syntax2", paramScanner, str, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException unclosedComment(String paramString) {
/* 347 */     return new ParseException(Util.getMessage("ParseException.unclosed", paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException undeclaredType(Scanner paramScanner, String paramString) {
/* 352 */     return arg1("undeclaredType", paramScanner, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException warning(Scanner paramScanner, String paramString) {
/* 357 */     scannerInfo(paramScanner);
/* 358 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString, line, pointer };
/* 359 */     return new ParseException(Util.getMessage("ParseException.warning", arrayOfString), true);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException wrongType(Scanner paramScanner, String paramString1, String paramString2, String paramString3) {
/* 364 */     scannerInfo(paramScanner);
/* 365 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString1, paramString3, paramString2, line, pointer };
/* 366 */     return new ParseException(Util.getMessage("ParseException.wrongType", arrayOfString));
/*     */   }
/*     */ 
/*     */   
/*     */   static ParseException wrongExprType(Scanner paramScanner, String paramString1, String paramString2) {
/* 371 */     scannerInfo(paramScanner);
/* 372 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString2, paramString1, line, pointer };
/*     */     
/* 374 */     return new ParseException(Util.getMessage("ParseException.constExprType", arrayOfString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException illegalForwardInheritance(Scanner paramScanner, String paramString1, String paramString2) {
/* 381 */     scannerInfo(paramScanner);
/* 382 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString1, paramString2, line, pointer };
/*     */     
/* 384 */     return new ParseException(Util.getMessage("ParseException.forwardInheritance", arrayOfString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParseException illegalIncompleteTypeReference(Scanner paramScanner, String paramString) {
/* 391 */     scannerInfo(paramScanner);
/* 392 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString, line, pointer };
/*     */     
/* 394 */     return new ParseException(Util.getMessage("ParseException.illegalIncompleteTypeReference", arrayOfString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void scannerInfo(Scanner paramScanner) {
/* 400 */     filename = paramScanner.filename();
/* 401 */     line = paramScanner.lastTokenLine();
/* 402 */     lineNumber = paramScanner.lastTokenLineNumber();
/* 403 */     int i = paramScanner.lastTokenLinePosition();
/* 404 */     pointer = "^";
/* 405 */     if (i > 1) {
/*     */       
/* 407 */       byte[] arrayOfByte = new byte[i - 1];
/* 408 */       for (byte b = 0; b < i - 1; b++)
/* 409 */         arrayOfByte[b] = 32; 
/* 410 */       pointer = new String(arrayOfByte) + pointer;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static ParseException arg0(String paramString, Scanner paramScanner) {
/* 416 */     scannerInfo(paramScanner);
/* 417 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), line, pointer };
/* 418 */     return new ParseException(Util.getMessage("ParseException." + paramString, arrayOfString));
/*     */   }
/*     */ 
/*     */   
/*     */   private static ParseException arg1(String paramString1, Scanner paramScanner, String paramString2) {
/* 423 */     scannerInfo(paramScanner);
/* 424 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString2, line, pointer };
/* 425 */     return new ParseException(Util.getMessage("ParseException." + paramString1, arrayOfString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ParseException arg1Warning(String paramString1, Scanner paramScanner, String paramString2) {
/* 431 */     scannerInfo(paramScanner);
/* 432 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString2, line, pointer };
/* 433 */     return new ParseException(Util.getMessage("ParseException." + paramString1, arrayOfString), true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ParseException arg2(String paramString1, Scanner paramScanner, String paramString2, String paramString3) {
/* 438 */     scannerInfo(paramScanner);
/* 439 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString2, paramString3, line, pointer };
/* 440 */     return new ParseException(Util.getMessage("ParseException." + paramString1, arrayOfString));
/*     */   }
/*     */ 
/*     */   
/*     */   private static ParseException arg3(String paramString1, Scanner paramScanner, String paramString2, String paramString3, String paramString4) {
/* 445 */     scannerInfo(paramScanner);
/* 446 */     String[] arrayOfString = { filename, Integer.toString(lineNumber), paramString2, paramString3, paramString4, line, pointer };
/* 447 */     return new ParseException(Util.getMessage("ParseException." + paramString1, arrayOfString));
/*     */   }
/*     */   
/* 450 */   private static String filename = "";
/* 451 */   private static String line = "";
/* 452 */   private static int lineNumber = 0;
/* 453 */   private static String pointer = "^";
/*     */   static boolean detected = false;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */