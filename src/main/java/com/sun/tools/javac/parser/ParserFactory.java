/*    */ package com.sun.tools.javac.parser;
/*    */ 
/*    */ import com.sun.tools.javac.code.Source;
/*    */ import com.sun.tools.javac.tree.DocTreeMaker;
/*    */ import com.sun.tools.javac.tree.TreeMaker;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import com.sun.tools.javac.util.Names;
/*    */ import com.sun.tools.javac.util.Options;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParserFactory
/*    */ {
/* 49 */   protected static final Context.Key<ParserFactory> parserFactoryKey = new Context.Key();
/*    */   
/*    */   public static ParserFactory instance(Context paramContext) {
/* 52 */     ParserFactory parserFactory = (ParserFactory)paramContext.get(parserFactoryKey);
/* 53 */     if (parserFactory == null) {
/* 54 */       parserFactory = new ParserFactory(paramContext);
/*    */     }
/* 56 */     return parserFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   final TreeMaker F;
/*    */   final DocTreeMaker docTreeMaker;
/*    */   final Log log;
/*    */   final Tokens tokens;
/*    */   final Source source;
/*    */   final Names names;
/*    */   final Options options;
/*    */   final ScannerFactory scannerFactory;
/*    */   final Locale locale;
/*    */   
/*    */   protected ParserFactory(Context paramContext) {
/* 71 */     paramContext.put(parserFactoryKey, this);
/* 72 */     this.F = TreeMaker.instance(paramContext);
/* 73 */     this.docTreeMaker = DocTreeMaker.instance(paramContext);
/* 74 */     this.log = Log.instance(paramContext);
/* 75 */     this.names = Names.instance(paramContext);
/* 76 */     this.tokens = Tokens.instance(paramContext);
/* 77 */     this.source = Source.instance(paramContext);
/* 78 */     this.options = Options.instance(paramContext);
/* 79 */     this.scannerFactory = ScannerFactory.instance(paramContext);
/* 80 */     this.locale = (Locale)paramContext.get(Locale.class);
/*    */   }
/*    */   
/*    */   public JavacParser newParser(CharSequence paramCharSequence, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 84 */     Scanner scanner = this.scannerFactory.newScanner(paramCharSequence, paramBoolean1);
/* 85 */     return new JavacParser(this, scanner, paramBoolean1, paramBoolean3, paramBoolean2);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\ParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */