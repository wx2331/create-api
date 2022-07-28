/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodSet
/*     */ {
/*     */   private boolean frozen = false;
/*  66 */   private final Map lookupMap = new HashMap<>();
/*  67 */   private int count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  74 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(MemberDefinition paramMemberDefinition) {
/*  83 */     if (this.frozen) {
/*  84 */       throw new CompilerError("add()");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  89 */     Identifier identifier = paramMemberDefinition.getName();
/*     */ 
/*     */     
/*  92 */     List<MemberDefinition> list = (List)this.lookupMap.get(identifier);
/*     */     
/*  94 */     if (list == null) {
/*     */ 
/*     */       
/*  97 */       list = new ArrayList();
/*  98 */       this.lookupMap.put(identifier, list);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 103 */     int i = list.size();
/* 104 */     for (byte b = 0; b < i; b++) {
/* 105 */       if (((MemberDefinition)list.get(b))
/* 106 */         .getType().equalArguments(paramMemberDefinition.getType())) {
/* 107 */         throw new CompilerError("duplicate addition");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 112 */     list.add(paramMemberDefinition);
/* 113 */     this.count++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(MemberDefinition paramMemberDefinition) {
/* 122 */     if (this.frozen) {
/* 123 */       throw new CompilerError("replace()");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 128 */     Identifier identifier = paramMemberDefinition.getName();
/*     */ 
/*     */     
/* 131 */     List<MemberDefinition> list = (List)this.lookupMap.get(identifier);
/*     */     
/* 133 */     if (list == null) {
/*     */ 
/*     */       
/* 136 */       list = new ArrayList();
/* 137 */       this.lookupMap.put(identifier, list);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 142 */     int i = list.size();
/* 143 */     for (byte b = 0; b < i; b++) {
/* 144 */       if (((MemberDefinition)list.get(b))
/* 145 */         .getType().equalArguments(paramMemberDefinition.getType())) {
/* 146 */         list.set(b, paramMemberDefinition);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     list.add(paramMemberDefinition);
/* 153 */     this.count++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberDefinition lookupSig(Identifier paramIdentifier, Type paramType) {
/* 163 */     Iterator<MemberDefinition> iterator = lookupName(paramIdentifier);
/*     */ 
/*     */     
/* 166 */     while (iterator.hasNext()) {
/* 167 */       MemberDefinition memberDefinition = iterator.next();
/* 168 */       if (memberDefinition.getType().equalArguments(paramType)) {
/* 169 */         return memberDefinition;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator lookupName(Identifier paramIdentifier) {
/* 184 */     List list = (List)this.lookupMap.get(paramIdentifier);
/* 185 */     if (list == null)
/*     */     {
/*     */       
/* 188 */       return Collections.emptyIterator();
/*     */     }
/* 190 */     return list.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterator() {
/*     */     class MethodIterator
/*     */       implements Iterator
/*     */     {
/* 202 */       Iterator hashIter = MethodSet.this.lookupMap.values().iterator();
/* 203 */       Iterator listIter = Collections.emptyIterator();
/*     */       
/*     */       public boolean hasNext() {
/* 206 */         if (this.listIter.hasNext()) {
/* 207 */           return true;
/*     */         }
/* 209 */         if (this.hashIter.hasNext()) {
/* 210 */           this
/* 211 */             .listIter = ((List)this.hashIter.next()).iterator();
/*     */ 
/*     */           
/* 214 */           if (this.listIter.hasNext()) {
/* 215 */             return true;
/*     */           }
/* 217 */           throw new CompilerError("iterator() in MethodSet");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 224 */         return false;
/*     */       }
/*     */       
/*     */       public Object next() {
/* 228 */         return this.listIter.next();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 232 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     return new MethodIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 249 */     this.frozen = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 256 */     return this.frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 263 */     int i = size();
/* 264 */     StringBuffer stringBuffer = new StringBuffer();
/* 265 */     Iterator<E> iterator = iterator();
/* 266 */     stringBuffer.append("{");
/*     */     
/* 268 */     while (iterator.hasNext()) {
/* 269 */       stringBuffer.append(iterator.next().toString());
/* 270 */       i--;
/* 271 */       if (i > 0) {
/* 272 */         stringBuffer.append(", ");
/*     */       }
/*     */     } 
/* 275 */     stringBuffer.append("}");
/* 276 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\MethodSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */