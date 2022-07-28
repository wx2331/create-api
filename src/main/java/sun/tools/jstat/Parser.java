/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Parser
/*     */ {
/*  40 */   private static boolean pdebug = Boolean.getBoolean("jstat.parser.debug");
/*  41 */   private static boolean ldebug = Boolean.getBoolean("jstat.lex.debug");
/*     */   
/*     */   private static final char OPENBLOCK = '{';
/*     */   
/*     */   private static final char CLOSEBLOCK = '}';
/*     */   
/*     */   private static final char DOUBLEQUOTE = '"';
/*     */   
/*     */   private static final char PERCENT_CHAR = '%';
/*     */   
/*     */   private static final char OPENPAREN = '(';
/*     */   private static final char CLOSEPAREN = ')';
/*     */   private static final char OPERATOR_PLUS = '+';
/*     */   private static final char OPERATOR_MINUS = '-';
/*     */   private static final char OPERATOR_MULTIPLY = '*';
/*     */   private static final char OPERATOR_DIVIDE = '/';
/*     */   private static final String OPTION = "option";
/*     */   private static final String COLUMN = "column";
/*     */   private static final String DATA = "data";
/*     */   private static final String HEADER = "header";
/*     */   private static final String WIDTH = "width";
/*     */   private static final String FORMAT = "format";
/*     */   private static final String ALIGN = "align";
/*     */   private static final String SCALE = "scale";
/*     */   private static final String START = "option";
/*  66 */   private static final Set scaleKeyWords = Scale.keySet();
/*  67 */   private static final Set alignKeyWords = Alignment.keySet();
/*  68 */   private static String[] otherKeyWords = new String[] { "option", "column", "data", "header", "width", "format", "align", "scale" };
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static char[] infixOps = new char[] { '+', '-', '*', '/' };
/*     */ 
/*     */ 
/*     */   
/*  76 */   private static char[] delimiters = new char[] { '{', '}', '%', '(', ')' };
/*     */   
/*     */   private static Set<String> reservedWords;
/*     */   
/*     */   private StreamTokenizer st;
/*     */   
/*     */   private String filename;
/*     */   
/*     */   private Token lookahead;
/*     */   
/*     */   private Token previous;
/*     */   private int columnCount;
/*     */   private OptionFormat optionFormat;
/*     */   
/*     */   public Parser(String paramString) throws FileNotFoundException {
/*  91 */     this.filename = paramString;
/*  92 */     BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
/*     */   }
/*     */   
/*     */   public Parser(Reader paramReader) {
/*  96 */     this.st = new StreamTokenizer(paramReader);
/*     */ 
/*     */     
/*  99 */     this.st.ordinaryChar(47);
/* 100 */     this.st.wordChars(95, 95);
/* 101 */     this.st.slashSlashComments(true);
/* 102 */     this.st.slashStarComments(true);
/*     */     
/* 104 */     reservedWords = new HashSet<>(); byte b;
/* 105 */     for (b = 0; b < otherKeyWords.length; b++) {
/* 106 */       reservedWords.add(otherKeyWords[b]);
/*     */     }
/*     */     
/* 109 */     for (b = 0; b < delimiters.length; b++) {
/* 110 */       this.st.ordinaryChar(delimiters[b]);
/*     */     }
/*     */     
/* 113 */     for (b = 0; b < infixOps.length; b++) {
/* 114 */       this.st.ordinaryChar(infixOps[b]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushBack() {
/* 123 */     this.lookahead = this.previous;
/* 124 */     this.st.pushBack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextToken() throws ParserException, IOException {
/* 133 */     int i = this.st.nextToken();
/* 134 */     this.previous = this.lookahead;
/* 135 */     this.lookahead = new Token(this.st.ttype, this.st.sval, this.st.nval);
/* 136 */     log(ldebug, "lookahead = " + this.lookahead);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Token matchOne(Set paramSet) throws ParserException, IOException {
/* 145 */     if (this.lookahead.ttype == -3 && paramSet
/* 146 */       .contains(this.lookahead.sval)) {
/* 147 */       Token token = this.lookahead;
/* 148 */       nextToken();
/* 149 */       return token;
/*     */     } 
/* 151 */     throw new SyntaxException(this.st.lineno(), paramSet, this.lookahead);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void match(int paramInt, String paramString) throws ParserException, IOException {
/* 160 */     if (this.lookahead.ttype == paramInt && this.lookahead.sval.compareTo(paramString) == 0) {
/* 161 */       nextToken();
/*     */     } else {
/* 163 */       throw new SyntaxException(this.st.lineno(), new Token(paramInt, paramString), this.lookahead);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void match(int paramInt) throws ParserException, IOException {
/* 172 */     if (this.lookahead.ttype == paramInt) {
/* 173 */       nextToken();
/*     */     } else {
/* 175 */       throw new SyntaxException(this.st.lineno(), new Token(paramInt), this.lookahead);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void match(char paramChar) throws ParserException, IOException {
/* 183 */     if (this.lookahead.ttype == paramChar) {
/* 184 */       nextToken();
/*     */     } else {
/*     */       
/* 187 */       throw new SyntaxException(this.st.lineno(), new Token(paramChar), this.lookahead);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void matchQuotedString() throws ParserException, IOException {
/* 197 */     match('"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void matchNumber() throws ParserException, IOException {
/* 204 */     match(-2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void matchID() throws ParserException, IOException {
/* 211 */     match(-3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void match(String paramString) throws ParserException, IOException {
/* 218 */     match(-3, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isReservedWord(String paramString) {
/* 225 */     return reservedWords.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isInfixOperator(char paramChar) {
/* 232 */     for (byte b = 0; b < infixOps.length; b++) {
/* 233 */       if (paramChar == infixOps[b]) {
/* 234 */         return true;
/*     */       }
/*     */     } 
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void scaleStmt(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/* 246 */     match("scale");
/* 247 */     Token token = matchOne(scaleKeyWords);
/* 248 */     paramColumnFormat.setScale(Scale.toScale(token.sval));
/* 249 */     String str = token.sval;
/* 250 */     log(pdebug, "Parsed: scale -> " + str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void alignStmt(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/* 259 */     match("align");
/* 260 */     Token token = matchOne(alignKeyWords);
/* 261 */     paramColumnFormat.setAlignment(Alignment.toAlignment(token.sval));
/* 262 */     String str = token.sval;
/* 263 */     log(pdebug, "Parsed: align -> " + str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void headerStmt(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/* 271 */     match("header");
/* 272 */     String str = this.lookahead.sval;
/* 273 */     matchQuotedString();
/* 274 */     paramColumnFormat.setHeader(str);
/* 275 */     log(pdebug, "Parsed: header -> " + str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void widthStmt(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/* 283 */     match("width");
/* 284 */     double d = this.lookahead.nval;
/* 285 */     matchNumber();
/* 286 */     paramColumnFormat.setWidth((int)d);
/* 287 */     log(pdebug, "Parsed: width -> " + d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatStmt(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/* 295 */     match("format");
/* 296 */     String str = this.lookahead.sval;
/* 297 */     matchQuotedString();
/* 298 */     paramColumnFormat.setFormat(str);
/* 299 */     log(pdebug, "Parsed: format -> " + str);
/*     */   }
/*     */ 
/*     */   
/*     */   private Expression primary() throws ParserException, IOException {
/*     */     String str;
/*     */     double d;
/* 306 */     Expression expression = null;
/*     */     
/* 308 */     switch (this.lookahead.ttype) {
/*     */       case 40:
/* 310 */         match('(');
/* 311 */         expression = expression();
/* 312 */         match(')');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 333 */         log(pdebug, "Parsed: primary -> " + expression);
/* 334 */         return expression;case -3: str = this.lookahead.sval; if (isReservedWord(str)) throw new SyntaxException(this.st.lineno(), "IDENTIFIER", "Reserved Word: " + this.lookahead.sval);  matchID(); expression = new Identifier(str); log(pdebug, "Parsed: ID -> " + str); log(pdebug, "Parsed: primary -> " + expression); return expression;case -2: d = this.lookahead.nval; matchNumber(); expression = new Literal(new Double(d)); log(pdebug, "Parsed: number -> " + d); log(pdebug, "Parsed: primary -> " + expression); return expression;
/*     */     } 
/*     */     throw new SyntaxException(this.st.lineno(), "IDENTIFIER", this.lookahead);
/*     */   }
/*     */ 
/*     */   
/*     */   private Expression unary() throws ParserException, IOException {
/* 341 */     Expression expression = null;
/* 342 */     Operator operator = null;
/*     */     
/*     */     while (true) {
/* 345 */       switch (this.lookahead.ttype) {
/*     */         case 43:
/* 347 */           match('+');
/* 348 */           operator = Operator.PLUS;
/*     */           break;
/*     */         case 45:
/* 351 */           match('-');
/* 352 */           operator = Operator.MINUS;
/*     */           break;
/*     */         default:
/* 355 */           expression = primary();
/* 356 */           log(pdebug, "Parsed: unary -> " + expression);
/* 357 */           return expression;
/*     */       } 
/* 359 */       Expression expression1 = new Expression();
/* 360 */       expression1.setOperator(operator);
/* 361 */       expression1.setRight(expression);
/* 362 */       log(pdebug, "Parsed: unary -> " + expression1);
/* 363 */       expression1.setLeft(new Literal(new Double(0.0D)));
/* 364 */       expression = expression1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression multExpression() throws ParserException, IOException {
/* 372 */     Expression expression = unary();
/* 373 */     Operator operator = null;
/*     */     
/*     */     while (true) {
/* 376 */       switch (this.lookahead.ttype) {
/*     */         case 42:
/* 378 */           match('*');
/* 379 */           operator = Operator.MULTIPLY;
/*     */           break;
/*     */         case 47:
/* 382 */           match('/');
/* 383 */           operator = Operator.DIVIDE;
/*     */           break;
/*     */         default:
/* 386 */           log(pdebug, "Parsed: multExpression -> " + expression);
/* 387 */           return expression;
/*     */       } 
/* 389 */       Expression expression1 = new Expression();
/* 390 */       expression1.setOperator(operator);
/* 391 */       expression1.setLeft(expression);
/* 392 */       expression1.setRight(unary());
/* 393 */       expression = expression1;
/* 394 */       log(pdebug, "Parsed: multExpression -> " + expression);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression addExpression() throws ParserException, IOException {
/* 402 */     Expression expression = multExpression();
/* 403 */     Operator operator = null;
/*     */     
/*     */     while (true) {
/* 406 */       switch (this.lookahead.ttype) {
/*     */         case 43:
/* 408 */           match('+');
/* 409 */           operator = Operator.PLUS;
/*     */           break;
/*     */         case 45:
/* 412 */           match('-');
/* 413 */           operator = Operator.MINUS;
/*     */           break;
/*     */         default:
/* 416 */           log(pdebug, "Parsed: addExpression -> " + expression);
/* 417 */           return expression;
/*     */       } 
/* 419 */       Expression expression1 = new Expression();
/* 420 */       expression1.setOperator(operator);
/* 421 */       expression1.setLeft(expression);
/* 422 */       expression1.setRight(multExpression());
/* 423 */       expression = expression1;
/* 424 */       log(pdebug, "Parsed: addExpression -> " + expression);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression expression() throws ParserException, IOException {
/* 432 */     Expression expression = addExpression();
/* 433 */     log(pdebug, "Parsed: expression -> " + expression);
/* 434 */     return expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dataStmt(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/* 441 */     match("data");
/* 442 */     Expression expression = expression();
/* 443 */     paramColumnFormat.setExpression(expression);
/* 444 */     log(pdebug, "Parsed: data -> " + expression);
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
/*     */   private void statementList(ColumnFormat paramColumnFormat) throws ParserException, IOException {
/*     */     while (true) {
/* 459 */       if (this.lookahead.ttype != -3) {
/*     */         return;
/*     */       }
/*     */       
/* 463 */       if (this.lookahead.sval.compareTo("data") == 0) {
/* 464 */         dataStmt(paramColumnFormat); continue;
/* 465 */       }  if (this.lookahead.sval.compareTo("header") == 0) {
/* 466 */         headerStmt(paramColumnFormat); continue;
/* 467 */       }  if (this.lookahead.sval.compareTo("width") == 0) {
/* 468 */         widthStmt(paramColumnFormat); continue;
/* 469 */       }  if (this.lookahead.sval.compareTo("format") == 0) {
/* 470 */         formatStmt(paramColumnFormat); continue;
/* 471 */       }  if (this.lookahead.sval.compareTo("align") == 0) {
/* 472 */         alignStmt(paramColumnFormat); continue;
/* 473 */       }  if (this.lookahead.sval.compareTo("scale") == 0) {
/* 474 */         scaleStmt(paramColumnFormat);
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void optionList(OptionFormat paramOptionFormat) throws ParserException, IOException {
/*     */     while (true) {
/* 489 */       if (this.lookahead.ttype != -3) {
/*     */         return;
/*     */       }
/*     */       
/* 493 */       match("column");
/* 494 */       match('{');
/* 495 */       ColumnFormat columnFormat = new ColumnFormat(this.columnCount++);
/* 496 */       statementList(columnFormat);
/* 497 */       match('}');
/* 498 */       columnFormat.validate();
/* 499 */       paramOptionFormat.addSubFormat(columnFormat);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OptionFormat optionStmt() throws ParserException, IOException {
/* 507 */     match("option");
/* 508 */     String str = this.lookahead.sval;
/* 509 */     matchID();
/* 510 */     match('{');
/* 511 */     OptionFormat optionFormat = new OptionFormat(str);
/* 512 */     optionList(optionFormat);
/* 513 */     match('}');
/* 514 */     return optionFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OptionFormat parse(String paramString) throws ParserException, IOException {
/* 522 */     nextToken();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 529 */     while (this.lookahead.ttype != -1) {
/*     */       
/* 531 */       if (this.lookahead.ttype != -3 || this.lookahead.sval
/* 532 */         .compareTo("option") != 0) {
/*     */         
/* 534 */         nextToken();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 539 */       match("option");
/*     */       
/* 541 */       if (this.lookahead.ttype == -3 && this.lookahead.sval
/* 542 */         .compareTo(paramString) == 0) {
/*     */         
/* 544 */         pushBack();
/* 545 */         return optionStmt();
/*     */       } 
/*     */       
/* 548 */       nextToken();
/*     */     } 
/*     */     
/* 551 */     return null;
/*     */   }
/*     */   
/*     */   public Set<OptionFormat> parseOptions() throws ParserException, IOException {
/* 555 */     HashSet<OptionFormat> hashSet = new HashSet();
/*     */     
/* 557 */     nextToken();
/*     */     
/* 559 */     while (this.lookahead.ttype != -1) {
/*     */       
/* 561 */       if (this.lookahead.ttype != -3 || this.lookahead.sval
/* 562 */         .compareTo("option") != 0) {
/*     */         
/* 564 */         nextToken();
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 570 */       OptionFormat optionFormat = optionStmt();
/* 571 */       hashSet.add(optionFormat);
/*     */     } 
/* 573 */     return hashSet;
/*     */   }
/*     */   
/*     */   OptionFormat getOptionFormat() {
/* 577 */     return this.optionFormat;
/*     */   }
/*     */   
/*     */   private void log(boolean paramBoolean, String paramString) {
/* 581 */     if (paramBoolean)
/* 582 */       System.out.println(paramString); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */