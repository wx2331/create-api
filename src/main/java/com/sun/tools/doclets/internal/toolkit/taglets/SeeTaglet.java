/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Doc;
/*    */ import com.sun.javadoc.ProgramElementDoc;
/*    */ import com.sun.javadoc.SeeTag;
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
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
/*    */ public class SeeTaglet
/*    */   extends BaseTaglet
/*    */   implements InheritableTaglet
/*    */ {
/*    */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput) {
/* 53 */     SeeTag[] arrayOfSeeTag = paramInput.element.seeTags();
/* 54 */     if (arrayOfSeeTag.length > 0) {
/* 55 */       paramOutput.holder = (Doc)paramInput.element;
/* 56 */       paramOutput.holderTag = (Tag)arrayOfSeeTag[0];
/* 57 */       paramOutput
/* 58 */         .inlineTags = paramInput.isFirstSentence ? arrayOfSeeTag[0].firstSentenceTags() : arrayOfSeeTag[0].inlineTags();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) {
/* 66 */     SeeTag[] arrayOfSeeTag = paramDoc.seeTags();
/* 67 */     if (arrayOfSeeTag.length == 0 && paramDoc instanceof com.sun.javadoc.MethodDoc) {
/*    */       
/* 69 */       DocFinder.Output output = DocFinder.search(new DocFinder.Input((ProgramElementDoc)paramDoc, this));
/* 70 */       if (output.holder != null) {
/* 71 */         arrayOfSeeTag = output.holder.seeTags();
/*    */       }
/*    */     } 
/* 74 */     return paramTagletWriter.seeTagOutput(paramDoc, arrayOfSeeTag);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\SeeTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */