/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CodeTaglet
/*    */   extends BaseInlineTaglet
/*    */ {
/*    */   private static final String NAME = "code";
/*    */   
/*    */   public static void register(Map<String, Taglet> paramMap) {
/* 57 */     paramMap.remove("code");
/* 58 */     paramMap.put("code", new CodeTaglet());
/*    */   }
/*    */   
/*    */   public String getName() {
/* 62 */     return "code";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter) {
/* 69 */     return paramTagletWriter.codeTagOutput(paramTag);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\CodeTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */