/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.MethodDoc;
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
/*    */ public class TaggedMethodFinder
/*    */   extends MethodFinder
/*    */ {
/*    */   public boolean isCorrectMethod(MethodDoc paramMethodDoc) {
/* 42 */     return 
/* 43 */       ((paramMethodDoc.paramTags()).length + (paramMethodDoc.tags("return")).length + (paramMethodDoc.throwsTags()).length + (paramMethodDoc.seeTags()).length > 0);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\TaggedMethodFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */