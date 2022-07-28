/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.oql.OQLEngine;
/*    */ import com.sun.tools.hat.internal.oql.OQLException;
/*    */ import com.sun.tools.hat.internal.oql.ObjectVisitor;
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
/*    */ class OQLQuery
/*    */   extends QueryHandler
/*    */ {
/*    */   private OQLEngine engine;
/*    */   
/*    */   public OQLQuery(OQLEngine paramOQLEngine) {
/* 46 */     this.engine = paramOQLEngine;
/*    */   }
/*    */   
/*    */   public void run() {
/* 50 */     startHtml("Object Query Language (OQL) query");
/* 51 */     String str = null;
/* 52 */     if (this.query != null && !this.query.equals("")) {
/* 53 */       int i = this.query.indexOf("?query=");
/* 54 */       if (i != -1 && this.query.length() > 7) {
/* 55 */         str = this.query.substring(i + 7);
/*    */       }
/*    */     } 
/* 58 */     this.out.println("<p align='center'><table>");
/* 59 */     this.out.println("<tr><td><b>");
/* 60 */     this.out.println("<a href='/'>All Classes (excluding platform)</a>");
/* 61 */     this.out.println("</b></td>");
/* 62 */     this.out.println("<td><b><a href='/oqlhelp/'>OQL Help</a></b></td></tr>");
/* 63 */     this.out.println("</table></p>");
/* 64 */     this.out.println("<form action='/oql/' method='get'>");
/* 65 */     this.out.println("<p align='center'>");
/* 66 */     this.out.println("<textarea name='query' cols=80 rows=10>");
/* 67 */     if (str != null) {
/* 68 */       println(str);
/*    */     }
/* 70 */     this.out.println("</textarea>");
/* 71 */     this.out.println("</p>");
/* 72 */     this.out.println("<p align='center'>");
/* 73 */     this.out.println("<input type='submit' value='Execute'></input>");
/* 74 */     this.out.println("</p>");
/* 75 */     this.out.println("</form>");
/* 76 */     if (str != null) {
/* 77 */       executeQuery(str);
/*    */     }
/* 79 */     endHtml();
/*    */   }
/*    */   
/*    */   private void executeQuery(String paramString) {
/*    */     try {
/* 84 */       this.out.println("<table border='1'>");
/* 85 */       this.engine.executeQuery(paramString, new ObjectVisitor() {
/*    */             public boolean visit(Object param1Object) {
/* 87 */               OQLQuery.this.out.println("<tr><td>");
/*    */               try {
/* 89 */                 OQLQuery.this.out.println(OQLQuery.this.engine.toHtml(param1Object));
/* 90 */               } catch (Exception exception) {
/* 91 */                 OQLQuery.this.printException(exception);
/*    */               } 
/* 93 */               OQLQuery.this.out.println("</td></tr>");
/* 94 */               return false;
/*    */             }
/*    */           });
/* 97 */       this.out.println("</table>");
/* 98 */     } catch (OQLException oQLException) {
/* 99 */       printException((Throwable)oQLException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\OQLQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */