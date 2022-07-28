/*     */ package com.sun.tools.internal.xjc.reader.gbind;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Element
/*     */   extends Expression
/*     */   implements ElementSet
/*     */ {
/*  55 */   final Set<Element> foreEdges = new LinkedHashSet<>();
/*  56 */   final Set<Element> backEdges = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Element prevPostOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConnectedComponent cc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ElementSet lastSet() {
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   boolean isNullable() {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSource() {
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSink() {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   void buildDAG(ElementSet incoming) {
/* 103 */     incoming.addNext(this);
/*     */   }
/*     */   
/*     */   public void addNext(Element element) {
/* 107 */     this.foreEdges.add(element);
/* 108 */     element.backEdges.add(this);
/*     */   }
/*     */   
/*     */   public boolean contains(ElementSet rhs) {
/* 112 */     return (this == rhs || rhs == ElementSet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Element> iterator() {
/* 122 */     return Collections.<Element>singleton(this).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Element assignDfsPostOrder(Element prev) {
/* 132 */     if (this.prevPostOrder != null) {
/* 133 */       return prev;
/*     */     }
/* 135 */     this.prevPostOrder = this;
/*     */     
/* 137 */     for (Element next : this.foreEdges) {
/* 138 */       prev = next.assignDfsPostOrder(prev);
/*     */     }
/* 140 */     this.prevPostOrder = prev;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildStronglyConnectedComponents(List<ConnectedComponent> ccs) {
/* 151 */     List<Element> visitedElements = new ArrayList<>();
/*     */     
/* 153 */     for (Element cur = this; cur != cur.prevPostOrder; cur = cur.prevPostOrder) {
/*     */       
/* 155 */       if (visitedElements.contains(cur)) {
/*     */         break;
/*     */       }
/*     */       
/* 159 */       visitedElements.add(cur);
/*     */ 
/*     */       
/* 162 */       if (!cur.belongsToSCC()) {
/*     */ 
/*     */ 
/*     */         
/* 166 */         ConnectedComponent cc = new ConnectedComponent();
/* 167 */         ccs.add(cc);
/*     */         
/* 169 */         cur.formConnectedComponent(cc);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean belongsToSCC() {
/* 174 */     return (this.cc != null || isSource() || isSink());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formConnectedComponent(ConnectedComponent group) {
/* 181 */     if (belongsToSCC()) {
/*     */       return;
/*     */     }
/* 184 */     this.cc = group;
/* 185 */     group.add(this);
/* 186 */     for (Element prev : this.backEdges) {
/* 187 */       prev.formConnectedComponent(group);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasSelfLoop() {
/* 192 */     assert this.foreEdges.contains(this) == this.backEdges.contains(this);
/*     */     
/* 194 */     return this.foreEdges.contains(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean checkCutSet(ConnectedComponent cc, Set<Element> visited) {
/* 207 */     assert belongsToSCC();
/*     */     
/* 209 */     if (isSink())
/*     */     {
/*     */       
/* 212 */       return false;
/*     */     }
/* 214 */     if (!visited.add(this)) {
/* 215 */       return true;
/*     */     }
/* 217 */     if (this.cc == cc) {
/* 218 */       return true;
/*     */     }
/* 220 */     for (Element next : this.foreEdges) {
/* 221 */       if (!next.checkCutSet(cc, visited))
/*     */       {
/* 223 */         return false;
/*     */       }
/*     */     } 
/* 226 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\gbind\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */