/*    */ package com.sun.tools.doclets.internal.toolkit.builders;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
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
/*    */ public class XMLNode
/*    */ {
/*    */   final XMLNode parent;
/*    */   final String name;
/*    */   final Map<String, String> attrs;
/*    */   final List<XMLNode> children;
/*    */   
/*    */   XMLNode(XMLNode paramXMLNode, String paramString) {
/* 43 */     this.parent = paramXMLNode;
/* 44 */     this.name = paramString;
/* 45 */     this.attrs = new HashMap<>();
/* 46 */     this.children = new ArrayList<>();
/*    */     
/* 48 */     if (paramXMLNode != null) {
/* 49 */       paramXMLNode.children.add(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 54 */     StringBuilder stringBuilder = new StringBuilder();
/* 55 */     stringBuilder.append("<");
/* 56 */     stringBuilder.append(this.name);
/* 57 */     for (Map.Entry<String, String> entry : this.attrs.entrySet())
/* 58 */       stringBuilder.append(" " + (String)entry.getKey() + "=\"" + (String)entry.getValue() + "\""); 
/* 59 */     if (this.children.size() == 0) {
/* 60 */       stringBuilder.append("/>");
/*    */     } else {
/* 62 */       stringBuilder.append(">");
/* 63 */       for (XMLNode xMLNode : this.children)
/* 64 */         stringBuilder.append(xMLNode.toString()); 
/* 65 */       stringBuilder.append("</" + this.name + ">");
/*    */     } 
/* 67 */     return stringBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\XMLNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */