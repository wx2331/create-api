/*     */ package com.sun.tools.javac.util;
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
/*     */
/*     */
/*     */
/*     */
/*     */ public class GraphUtils
/*     */ {
/*     */   public static abstract class Node<D>
/*     */   {
/*     */     public final D data;
/*     */
/*     */     public Node(D param1D) {
/*  55 */       this.data = param1D;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public abstract DependencyKind[] getSupportedDependencyKinds();
/*     */
/*     */
/*     */
/*     */
/*     */     public abstract Iterable<? extends Node<D>> getAllDependencies();
/*     */
/*     */
/*     */
/*     */     public abstract String getDependencyName(Node<D> param1Node, DependencyKind param1DependencyKind);
/*     */
/*     */
/*     */
/*     */     public String toString() {
/*  75 */       return this.data.toString();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static abstract class TarjanNode<D>
/*     */     extends Node<D>
/*     */     implements Comparable<TarjanNode<D>>
/*     */   {
/*  84 */     int index = -1;
/*     */     int lowlink;
/*     */     boolean active;
/*     */
/*     */     public TarjanNode(D param1D) {
/*  89 */       super(param1D);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int compareTo(TarjanNode<D> param1TarjanNode) {
/*  97 */       return (this.index < param1TarjanNode.index) ? -1 : ((this.index == param1TarjanNode.index) ? 0 : 1);
/*     */     }
/*     */
/*     */     public abstract Iterable<? extends TarjanNode<D>> getAllDependencies();
/*     */
/*     */     public abstract Iterable<? extends TarjanNode<D>> getDependenciesByKind(DependencyKind param1DependencyKind);
/*     */   }
/*     */
/*     */   public static <D, N extends TarjanNode<D>> List<? extends List<? extends N>> tarjan(Iterable<? extends N> paramIterable) {
/* 106 */     Tarjan<Object, TarjanNode<?>> tarjan = new Tarjan<>();
/* 107 */     return tarjan.findSCC(paramIterable);
/*     */   }
/*     */
/*     */   private static class Tarjan<D, N extends TarjanNode<D>> {
/*     */     int index;
/*     */
/*     */     private Tarjan() {
/* 114 */       this.index = 0;
/*     */
/*     */
/* 117 */       this.sccs = new ListBuffer<>();
/*     */
/*     */
/* 120 */       this.stack = new ListBuffer<>();
/*     */     } ListBuffer<List<N>> sccs; ListBuffer<N> stack;
/*     */     private List<? extends List<? extends N>> findSCC(Iterable<? extends N> param1Iterable) {
/* 123 */       for (TarjanNode tarjanNode : param1Iterable) {
/* 124 */         if (tarjanNode.index == -1) {
/* 125 */           findSCC((N)tarjanNode);
/*     */         }
/*     */       }
/* 128 */       return this.sccs.toList();
/*     */     }
/*     */
/*     */     private void findSCC(N param1N) {
/* 132 */       visitNode(param1N);
/* 133 */       for (TarjanNode tarjanNode1 : param1N.getAllDependencies()) {
/*     */
/* 135 */         TarjanNode tarjanNode2 = tarjanNode1;
/* 136 */         if (tarjanNode2.index == -1) {
/*     */
/* 138 */           findSCC((N)tarjanNode2);
/* 139 */           ((TarjanNode)param1N).lowlink = Math.min(((TarjanNode)param1N).lowlink, tarjanNode2.lowlink); continue;
/* 140 */         }  if (this.stack.contains(tarjanNode2))
/*     */         {
/* 142 */           ((TarjanNode)param1N).lowlink = Math.min(((TarjanNode)param1N).lowlink, tarjanNode2.index);
/*     */         }
/*     */       }
/* 145 */       if (((TarjanNode)param1N).lowlink == ((TarjanNode)param1N).index)
/*     */       {
/* 147 */         addSCC(param1N);
/*     */       }
/*     */     }
/*     */
/*     */     private void visitNode(N param1N) {
/* 152 */       ((TarjanNode)param1N).index = this.index;
/* 153 */       ((TarjanNode)param1N).lowlink = this.index;
/* 154 */       this.index++;
/* 155 */       this.stack.prepend(param1N);
/* 156 */       ((TarjanNode)param1N).active = true;
/*     */     }
/*     */
/*     */
/*     */     private void addSCC(N param1N) {
/* 161 */       ListBuffer<TarjanNode> listBuffer = new ListBuffer();
/*     */       while (true) {
/* 163 */         TarjanNode tarjanNode = (TarjanNode)this.stack.remove();
/* 164 */         tarjanNode.active = false;
/* 165 */         listBuffer.add(tarjanNode);
/* 166 */         if (tarjanNode == param1N) {
/* 167 */           this.sccs.add((List)listBuffer.toList());
/*     */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static <D> String toDot(Iterable<? extends TarjanNode<D>> paramIterable, String paramString1, String paramString2) {
/* 178 */     StringBuilder stringBuilder = new StringBuilder();
/* 179 */     stringBuilder.append(String.format("digraph %s {\n", new Object[] { paramString1 }));
/* 180 */     stringBuilder.append(String.format("label = \"%s\";\n", new Object[] { paramString2 }));
/*     */
/* 182 */     for (TarjanNode<D> tarjanNode : paramIterable) {
/* 183 */       stringBuilder.append(String.format("%s [label = \"%s\"];\n", new Object[] { Integer.valueOf(tarjanNode.hashCode()), tarjanNode.toString() }));
/*     */     }
/*     */
/* 186 */     for (TarjanNode<D> tarjanNode : paramIterable) {
/* 187 */       for (DependencyKind dependencyKind : tarjanNode.getSupportedDependencyKinds()) {
/* 188 */         for (TarjanNode tarjanNode1 : tarjanNode.getDependenciesByKind(dependencyKind)) {
/* 189 */           stringBuilder.append(String.format("%s -> %s [label = \" %s \" style = %s ];\n", new Object[] {
/* 190 */                   Integer.valueOf(tarjanNode.hashCode()), Integer.valueOf(tarjanNode1.hashCode()), tarjanNode.getDependencyName(tarjanNode1, dependencyKind), dependencyKind.getDotStyle() }));
/*     */         }
/*     */       }
/*     */     }
/* 194 */     stringBuilder.append("}\n");
/* 195 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */   public static interface DependencyKind {
/*     */     String getDotStyle();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\GraphUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
