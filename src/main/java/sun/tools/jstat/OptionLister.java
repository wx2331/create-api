/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URL;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
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
/*    */ public class OptionLister
/*    */ {
/*    */   private static final boolean debug = false;
/*    */   private List<URL> sources;
/*    */   
/*    */   public OptionLister(List<URL> paramList) {
/* 43 */     this.sources = paramList;
/*    */   }
/*    */   
/*    */   public void print(PrintStream paramPrintStream) {
/* 47 */     Comparator<OptionFormat> comparator = new Comparator<OptionFormat>() {
/*    */         public int compare(OptionFormat param1OptionFormat1, OptionFormat param1OptionFormat2) {
/* 49 */           OptionFormat optionFormat1 = param1OptionFormat1;
/* 50 */           OptionFormat optionFormat2 = param1OptionFormat2;
/* 51 */           return optionFormat1.getName().compareTo(optionFormat2.getName());
/*    */         }
/*    */       };
/*    */     
/* 55 */     TreeSet<OptionFormat> treeSet = new TreeSet<>(comparator);
/*    */     
/* 57 */     for (URL uRL : this.sources) {
/*    */       
/*    */       try {
/* 60 */         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
/* 61 */         Set<OptionFormat> set = (new Parser(bufferedReader)).parseOptions();
/* 62 */         treeSet.addAll(set);
/* 63 */       } catch (IOException iOException) {
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 68 */       catch (ParserException parserException) {
/*    */         
/* 70 */         System.err.println(uRL + ": " + parserException.getMessage());
/* 71 */         System.err.println("Parsing of " + uRL + " aborted");
/*    */       } 
/*    */     } 
/*    */     
/* 75 */     for (OptionFormat optionFormat : treeSet) {
/* 76 */       if (optionFormat.getName().compareTo("timestamp") == 0) {
/*    */         continue;
/*    */       }
/*    */       
/* 80 */       paramPrintStream.println("-" + optionFormat.getName());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\OptionLister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */