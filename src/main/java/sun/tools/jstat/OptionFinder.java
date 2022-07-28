/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.util.List;
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
/*    */ public class OptionFinder
/*    */ {
/*    */   private static final boolean debug = false;
/*    */   List<URL> optionsSources;
/*    */   
/*    */   public OptionFinder(List<URL> paramList) {
/* 45 */     this.optionsSources = paramList;
/*    */   }
/*    */   
/*    */   public OptionFormat getOptionFormat(String paramString, boolean paramBoolean) {
/* 49 */     OptionFormat optionFormat1 = getOptionFormat(paramString, this.optionsSources);
/* 50 */     OptionFormat optionFormat2 = null;
/* 51 */     if (optionFormat1 != null && paramBoolean) {
/*    */       
/* 53 */       optionFormat2 = getOptionFormat("timestamp", this.optionsSources);
/* 54 */       if (optionFormat2 != null) {
/* 55 */         ColumnFormat columnFormat = (ColumnFormat)optionFormat2.getSubFormat(0);
/* 56 */         optionFormat1.insertSubFormat(0, columnFormat);
/*    */       } 
/*    */     } 
/* 59 */     return optionFormat1;
/*    */   }
/*    */   
/*    */   protected OptionFormat getOptionFormat(String paramString, List<URL> paramList) {
/* 63 */     OptionFormat optionFormat = null;
/* 64 */     for (URL uRL : paramList) {
/*    */       
/*    */       try {
/* 67 */         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
/* 68 */         optionFormat = (new Parser(bufferedReader)).parse(paramString);
/* 69 */         if (optionFormat != null)
/*    */           break; 
/* 71 */       } catch (IOException iOException) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 77 */       catch (ParserException parserException) {
/*    */         
/* 79 */         System.err.println(uRL + ": " + parserException.getMessage());
/* 80 */         System.err.println("Parsing of " + uRL + " aborted");
/*    */       } 
/*    */     } 
/* 83 */     return optionFormat;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\OptionFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */