/*    */ package com.sun.tools.doclets.formats.html;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkOutput;
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
/*    */ public class LinkOutputImpl
/*    */   implements LinkOutput
/*    */ {
/* 52 */   public StringBuilder output = new StringBuilder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void append(Object paramObject) {
/* 59 */     this.output.append((paramObject instanceof String) ? (String)paramObject : ((LinkOutputImpl)paramObject)
/* 60 */         .toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void insert(int paramInt, Object paramObject) {
/* 67 */     this.output.insert(paramInt, paramObject.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return this.output.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\LinkOutputImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */