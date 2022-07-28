/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import javax.tools.JavaFileObject;
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
/*     */ public class Todo
/*     */   extends AbstractQueue<Env<AttrContext>>
/*     */ {
/*  46 */   protected static final Context.Key<Todo> todoKey = new Context.Key();
/*     */   
/*     */   LinkedList<Env<AttrContext>> contents;
/*     */   
/*     */   public static Todo instance(Context paramContext) {
/*  51 */     Todo todo = (Todo)paramContext.get(todoKey);
/*  52 */     if (todo == null)
/*  53 */       todo = new Todo(paramContext); 
/*  54 */     return todo;
/*     */   }
/*     */ 
/*     */   
/*     */   LinkedList<Queue<Env<AttrContext>>> contentsByFile;
/*     */   Map<JavaFileObject, FileQueue> fileMap;
/*     */   
/*     */   public void append(Env<AttrContext> paramEnv) {
/*     */     add(paramEnv);
/*     */   }
/*     */   
/*     */   public Iterator<Env<AttrContext>> iterator() {
/*     */     return this.contents.iterator();
/*     */   }
/*     */   
/*     */   public int size() {
/*     */     return this.contents.size();
/*     */   }
/*     */   
/*     */   public boolean offer(Env<AttrContext> paramEnv) {
/*     */     if (this.contents.add(paramEnv)) {
/*     */       if (this.contentsByFile != null) {
/*     */         addByFile(paramEnv);
/*     */       }
/*     */       return true;
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public Env<AttrContext> poll() {
/*     */     if (size() == 0) {
/*     */       return null;
/*     */     }
/*     */     Env<AttrContext> env = this.contents.remove(0);
/*     */     if (this.contentsByFile != null) {
/*     */       removeByFile(env);
/*     */     }
/*     */     return env;
/*     */   }
/*     */   
/*     */   public Env<AttrContext> peek() {
/*     */     return (size() == 0) ? null : this.contents.get(0);
/*     */   }
/*     */   
/*     */   public Queue<Queue<Env<AttrContext>>> groupByFile() {
/*     */     if (this.contentsByFile == null) {
/*     */       this.contentsByFile = new LinkedList<>();
/*     */       for (Env<AttrContext> env : this.contents) {
/*     */         addByFile(env);
/*     */       }
/*     */     } 
/*     */     return this.contentsByFile;
/*     */   }
/*     */   
/*     */   private void addByFile(Env<AttrContext> paramEnv) {
/*     */     JavaFileObject javaFileObject = paramEnv.toplevel.sourcefile;
/*     */     if (this.fileMap == null) {
/*     */       this.fileMap = new HashMap<>();
/*     */     }
/*     */     FileQueue fileQueue = this.fileMap.get(javaFileObject);
/*     */     if (fileQueue == null) {
/*     */       fileQueue = new FileQueue();
/*     */       this.fileMap.put(javaFileObject, fileQueue);
/*     */       this.contentsByFile.add(fileQueue);
/*     */     } 
/*     */     fileQueue.fileContents.add(paramEnv);
/*     */   }
/*     */   
/*     */   private void removeByFile(Env<AttrContext> paramEnv) {
/*     */     JavaFileObject javaFileObject = paramEnv.toplevel.sourcefile;
/*     */     FileQueue fileQueue = this.fileMap.get(javaFileObject);
/*     */     if (fileQueue == null) {
/*     */       return;
/*     */     }
/*     */     if (fileQueue.fileContents.remove(paramEnv) && fileQueue.isEmpty()) {
/*     */       this.fileMap.remove(javaFileObject);
/*     */       this.contentsByFile.remove(fileQueue);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Todo(Context paramContext) {
/* 135 */     this.contents = new LinkedList<>();
/*     */     paramContext.put(todoKey, this);
/*     */   }
/*     */   
/*     */   class FileQueue
/*     */     extends AbstractQueue<Env<AttrContext>> {
/*     */     public Iterator<Env<AttrContext>> iterator() {
/* 142 */       return this.fileContents.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 147 */       return this.fileContents.size();
/*     */     }
/*     */     
/*     */     public boolean offer(Env<AttrContext> param1Env) {
/* 151 */       if (this.fileContents.offer(param1Env)) {
/* 152 */         Todo.this.contents.add(param1Env);
/* 153 */         return true;
/*     */       } 
/* 155 */       return false;
/*     */     }
/*     */     
/*     */     public Env<AttrContext> poll() {
/* 159 */       if (this.fileContents.size() == 0)
/* 160 */         return null; 
/* 161 */       Env<AttrContext> env = this.fileContents.remove(0);
/* 162 */       Todo.this.contents.remove(env);
/* 163 */       return env;
/*     */     }
/*     */     
/*     */     public Env<AttrContext> peek() {
/* 167 */       return (this.fileContents.size() == 0) ? null : this.fileContents.get(0);
/*     */     }
/*     */     
/* 170 */     LinkedList<Env<AttrContext>> fileContents = new LinkedList<>();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Todo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */