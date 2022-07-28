/*     */ package com.sun.tools.internal.ws.processor.model.java;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.ModelException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class JavaStructureType
/*     */   extends JavaType
/*     */ {
/*     */   public JavaStructureType() {}
/*     */   
/*     */   public JavaStructureType(String name, boolean present, Object owner) {
/*  41 */     super(name, present, "null");
/*  42 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public void add(JavaStructureMember m) {
/*  46 */     if (this.membersByName.containsKey(m.getName())) {
/*  47 */       throw new ModelException("model.uniqueness.javastructuretype", new Object[] { m
/*  48 */             .getName(), getRealName() });
/*     */     }
/*  50 */     this.members.add(m);
/*  51 */     this.membersByName.put(m.getName(), m);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaStructureMember getMemberByName(String name) {
/*  56 */     if (this.membersByName.size() != this.members.size()) {
/*  57 */       initializeMembersByName();
/*     */     }
/*  59 */     return this.membersByName.get(name);
/*     */   }
/*     */   
/*     */   public Iterator getMembers() {
/*  63 */     return this.members.iterator();
/*     */   }
/*     */   
/*     */   public int getMembersCount() {
/*  67 */     return this.members.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JavaStructureMember> getMembersList() {
/*  72 */     return this.members;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMembersList(List<JavaStructureMember> l) {
/*  77 */     this.members = l;
/*     */   }
/*     */   
/*     */   private void initializeMembersByName() {
/*  81 */     this.membersByName = new HashMap<>();
/*  82 */     if (this.members != null) {
/*  83 */       for (JavaStructureMember m : this.members) {
/*  84 */         if (m.getName() != null && this.membersByName
/*  85 */           .containsKey(m.getName()))
/*     */         {
/*  87 */           throw new ModelException("model.uniqueness", new Object[0]);
/*     */         }
/*  89 */         this.membersByName.put(m.getName(), m);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  95 */     return this.isAbstract;
/*     */   }
/*     */   
/*     */   public void setAbstract(boolean isAbstract) {
/*  99 */     this.isAbstract = isAbstract;
/*     */   }
/*     */   
/*     */   public JavaStructureType getSuperclass() {
/* 103 */     return this.superclass;
/*     */   }
/*     */   
/*     */   public void setSuperclass(JavaStructureType superclassType) {
/* 107 */     this.superclass = superclassType;
/*     */   }
/*     */   
/*     */   public void addSubclass(JavaStructureType subclassType) {
/* 111 */     this.subclasses.add(subclassType);
/* 112 */     subclassType.setSuperclass(this);
/*     */   }
/*     */   
/*     */   public Iterator getSubclasses() {
/* 116 */     if (this.subclasses == null || this.subclasses.size() == 0) {
/* 117 */       return null;
/*     */     }
/* 119 */     return this.subclasses.iterator();
/*     */   }
/*     */   
/*     */   public Set getSubclassesSet() {
/* 123 */     return this.subclasses;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSubclassesSet(Set s) {
/* 128 */     this.subclasses = s;
/* 129 */     for (Iterator<JavaStructureType> iter = s.iterator(); iter.hasNext();) {
/* 130 */       ((JavaStructureType)iter.next()).setSuperclass(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator getAllSubclasses() {
/* 135 */     Set subs = getAllSubclassesSet();
/* 136 */     if (subs.size() == 0) {
/* 137 */       return null;
/*     */     }
/* 139 */     return subs.iterator();
/*     */   }
/*     */   
/*     */   public Set getAllSubclassesSet() {
/* 143 */     Set transitiveSet = new HashSet();
/* 144 */     Iterator<JavaStructureType> subs = this.subclasses.iterator();
/* 145 */     while (subs.hasNext()) {
/* 146 */       transitiveSet.addAll(((JavaStructureType)subs
/* 147 */           .next()).getAllSubclassesSet());
/*     */     }
/* 149 */     transitiveSet.addAll(this.subclasses);
/* 150 */     return transitiveSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOwner() {
/* 156 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwner(Object owner) {
/* 162 */     this.owner = owner;
/*     */   }
/*     */   
/* 165 */   private List<JavaStructureMember> members = new ArrayList<>();
/* 166 */   private Map<String, JavaStructureMember> membersByName = new HashMap<>();
/*     */ 
/*     */   
/* 169 */   private Set subclasses = new HashSet();
/*     */   private JavaStructureType superclass;
/*     */   private Object owner;
/*     */   private boolean isAbstract = false;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaStructureType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */