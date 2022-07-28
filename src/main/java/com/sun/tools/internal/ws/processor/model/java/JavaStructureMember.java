/*     */ package com.sun.tools.internal.ws.processor.model.java;
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
/*     */ public class JavaStructureMember
/*     */ {
/*     */   private String name;
/*     */   private JavaType type;
/*     */   
/*     */   public JavaStructureMember() {}
/*     */   
/*     */   public JavaStructureMember(String name, JavaType type, Object owner) {
/*  37 */     this(name, type, owner, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaStructureMember(String name, JavaType type, Object owner, boolean isPublic) {
/*  42 */     this.name = name;
/*  43 */     this.type = type;
/*  44 */     this.owner = owner;
/*  45 */     this.isPublic = isPublic;
/*  46 */     this.constructorPos = -1;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  50 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String s) {
/*  54 */     this.name = s;
/*     */   }
/*     */   
/*     */   public JavaType getType() {
/*  58 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(JavaType t) {
/*  62 */     this.type = t;
/*     */   }
/*     */   
/*     */   public boolean isPublic() {
/*  66 */     return this.isPublic;
/*     */   }
/*     */   
/*     */   public void setPublic(boolean b) {
/*  70 */     this.isPublic = b;
/*     */   }
/*     */   
/*     */   public boolean isInherited() {
/*  74 */     return this.isInherited;
/*     */   }
/*     */   
/*     */   public void setInherited(boolean b) {
/*  78 */     this.isInherited = b;
/*     */   }
/*     */   
/*     */   public String getReadMethod() {
/*  82 */     return this.readMethod;
/*     */   }
/*     */   
/*     */   public void setReadMethod(String readMethod) {
/*  86 */     this.readMethod = readMethod;
/*     */   }
/*     */   
/*     */   public String getWriteMethod() {
/*  90 */     return this.writeMethod;
/*     */   }
/*     */   
/*     */   public void setWriteMethod(String writeMethod) {
/*  94 */     this.writeMethod = writeMethod;
/*     */   }
/*     */   
/*     */   public String getDeclaringClass() {
/*  98 */     return this.declaringClass;
/*     */   }
/*     */   public void setDeclaringClass(String declaringClass) {
/* 101 */     this.declaringClass = declaringClass;
/*     */   }
/*     */   
/*     */   public Object getOwner() {
/* 105 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(Object owner) {
/* 109 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public int getConstructorPos() {
/* 113 */     return this.constructorPos;
/*     */   }
/*     */   
/*     */   public void setConstructorPos(int idx) {
/* 117 */     this.constructorPos = idx;
/*     */   }
/*     */   
/*     */   private boolean isPublic = false;
/*     */   private boolean isInherited = false;
/*     */   private String readMethod;
/*     */   private String writeMethod;
/*     */   private String declaringClass;
/*     */   private Object owner;
/*     */   private int constructorPos;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaStructureMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */