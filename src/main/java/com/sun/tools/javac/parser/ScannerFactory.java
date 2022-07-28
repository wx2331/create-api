/*    */ package com.sun.tools.javac.parser;
/*    */ 
/*    */ import com.sun.tools.javac.code.Source;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import com.sun.tools.javac.util.Names;
/*    */ import java.nio.CharBuffer;
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
/*    */ public class ScannerFactory
/*    */ {
/* 46 */   public static final Context.Key<ScannerFactory> scannerFactoryKey = new Context.Key();
/*    */   final Log log;
/*    */   final Names names;
/*    */   
/*    */   public static ScannerFactory instance(Context paramContext) {
/* 51 */     ScannerFactory scannerFactory = (ScannerFactory)paramContext.get(scannerFactoryKey);
/* 52 */     if (scannerFactory == null)
/* 53 */       scannerFactory = new ScannerFactory(paramContext); 
/* 54 */     return scannerFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   final Source source;
/*    */   
/*    */   final Tokens tokens;
/*    */ 
/*    */   
/*    */   protected ScannerFactory(Context paramContext) {
/* 64 */     paramContext.put(scannerFactoryKey, this);
/* 65 */     this.log = Log.instance(paramContext);
/* 66 */     this.names = Names.instance(paramContext);
/* 67 */     this.source = Source.instance(paramContext);
/* 68 */     this.tokens = Tokens.instance(paramContext);
/*    */   }
/*    */   
/*    */   public Scanner newScanner(CharSequence paramCharSequence, boolean paramBoolean) {
/* 72 */     if (paramCharSequence instanceof CharBuffer) {
/* 73 */       CharBuffer charBuffer = (CharBuffer)paramCharSequence;
/* 74 */       if (paramBoolean) {
/* 75 */         return new Scanner(this, new JavadocTokenizer(this, charBuffer));
/*    */       }
/* 77 */       return new Scanner(this, charBuffer);
/*    */     } 
/* 79 */     char[] arrayOfChar = paramCharSequence.toString().toCharArray();
/* 80 */     return newScanner(arrayOfChar, arrayOfChar.length, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public Scanner newScanner(char[] paramArrayOfchar, int paramInt, boolean paramBoolean) {
/* 85 */     if (paramBoolean) {
/* 86 */       return new Scanner(this, new JavadocTokenizer(this, paramArrayOfchar, paramInt));
/*    */     }
/* 88 */     return new Scanner(this, paramArrayOfchar, paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\ScannerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */