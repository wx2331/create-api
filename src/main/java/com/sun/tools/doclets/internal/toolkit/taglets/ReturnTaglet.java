/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Doc;
/*    */ import com.sun.javadoc.MethodDoc;
/*    */ import com.sun.javadoc.ProgramElementDoc;
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.javadoc.Type;
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
/*    */ public class ReturnTaglet
/*    */   extends BaseExecutableMemberTaglet
/*    */   implements InheritableTaglet
/*    */ {
/*    */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput) {
/* 54 */     Tag[] arrayOfTag = paramInput.element.tags("return");
/* 55 */     if (arrayOfTag.length > 0) {
/* 56 */       paramOutput.holder = (Doc)paramInput.element;
/* 57 */       paramOutput.holderTag = arrayOfTag[0];
/* 58 */       paramOutput
/* 59 */         .inlineTags = paramInput.isFirstSentence ? arrayOfTag[0].firstSentenceTags() : arrayOfTag[0].inlineTags();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean inConstructor() {
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) {
/* 78 */     Type type = ((MethodDoc)paramDoc).returnType();
/* 79 */     Tag[] arrayOfTag = paramDoc.tags(this.name);
/*    */ 
/*    */     
/* 82 */     if (type.isPrimitive() && type.typeName().equals("void")) {
/* 83 */       if (arrayOfTag.length > 0) {
/* 84 */         paramTagletWriter.getMsgRetriever().warning(paramDoc.position(), "doclet.Return_tag_on_void_method", new Object[0]);
/*    */       }
/*    */       
/* 87 */       return null;
/*    */     } 
/*    */     
/* 90 */     if (arrayOfTag.length == 0) {
/*    */       
/* 92 */       DocFinder.Output output = DocFinder.search(new DocFinder.Input((ProgramElementDoc)paramDoc, this));
/* 93 */       (new Tag[1])[0] = output.holderTag; arrayOfTag = (output.holderTag == null) ? arrayOfTag : new Tag[1];
/*    */     } 
/* 95 */     return (arrayOfTag.length > 0) ? paramTagletWriter.returnTagOutput(arrayOfTag[0]) : null;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\ReturnTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */