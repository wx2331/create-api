/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class List<A>
/*     */   extends AbstractCollection<A>
/*     */   implements List<A>
/*     */ {
/*     */   public A head;
/*     */   public List<A> tail;
/*     */   
/*     */   List(A paramA, List<A> paramList) {
/*  66 */     this.tail = paramList;
/*  67 */     this.head = paramA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> nil() {
/*  74 */     return (List)EMPTY_LIST;
/*     */   }
/*     */   
/*  77 */   private static final List<?> EMPTY_LIST = new List<Object>(null, null) {
/*     */       public List<Object> setTail(List<Object> param1List) {
/*  79 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean isEmpty() {
/*  82 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> filter(List<A> paramList, A paramA) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic checkNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   4: pop
/*     */     //   5: invokestatic nil : ()Lcom/sun/tools/javac/util/List;
/*     */     //   8: astore_2
/*     */     //   9: aload_0
/*     */     //   10: invokevirtual iterator : ()Ljava/util/Iterator;
/*     */     //   13: astore_3
/*     */     //   14: aload_3
/*     */     //   15: invokeinterface hasNext : ()Z
/*     */     //   20: ifeq -> 55
/*     */     //   23: aload_3
/*     */     //   24: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   29: astore #4
/*     */     //   31: aload #4
/*     */     //   33: ifnull -> 52
/*     */     //   36: aload #4
/*     */     //   38: aload_1
/*     */     //   39: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   42: ifne -> 52
/*     */     //   45: aload_2
/*     */     //   46: aload #4
/*     */     //   48: invokevirtual prepend : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/List;
/*     */     //   51: astore_2
/*     */     //   52: goto -> 14
/*     */     //   55: aload_2
/*     */     //   56: invokevirtual reverse : ()Lcom/sun/tools/javac/util/List;
/*     */     //   59: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #89	-> 0
/*     */     //   #90	-> 5
/*     */     //   #91	-> 9
/*     */     //   #92	-> 31
/*     */     //   #93	-> 45
/*     */     //   #95	-> 52
/*     */     //   #96	-> 55
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> intersect(List<A> paramList) {
/* 100 */     ListBuffer<A> listBuffer = new ListBuffer();
/* 101 */     for (A a : this) {
/* 102 */       if (paramList.contains(a)) {
/* 103 */         listBuffer.append(a);
/*     */       }
/*     */     } 
/* 106 */     return listBuffer.toList();
/*     */   }
/*     */   
/*     */   public List<A> diff(List<A> paramList) {
/* 110 */     ListBuffer<A> listBuffer = new ListBuffer();
/* 111 */     for (A a : this) {
/* 112 */       if (!paramList.contains(a)) {
/* 113 */         listBuffer.append(a);
/*     */       }
/*     */     } 
/* 116 */     return listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> take(int paramInt) {
/* 123 */     ListBuffer<A> listBuffer = new ListBuffer();
/* 124 */     byte b = 0;
/* 125 */     for (A a : this) {
/* 126 */       if (b++ == paramInt)
/* 127 */         break;  listBuffer.append(a);
/*     */     } 
/* 129 */     return listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> of(A paramA) {
/* 135 */     return new List<>(paramA, nil());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> of(A paramA1, A paramA2) {
/* 141 */     return new List<>(paramA1, of(paramA2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> of(A paramA1, A paramA2, A paramA3) {
/* 147 */     return new List<>(paramA1, of(paramA2, paramA3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> of(A paramA1, A paramA2, A paramA3, A... paramVarArgs) {
/* 154 */     return new List<>(paramA1, new List<>(paramA2, new List<>(paramA3, from(paramVarArgs))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> from(A[] paramArrayOfA) {
/* 162 */     List<?> list = nil();
/* 163 */     if (paramArrayOfA != null)
/* 164 */       for (int i = paramArrayOfA.length - 1; i >= 0; i--)
/* 165 */         list = new List(paramArrayOfA[i], list);  
/* 166 */     return (List)list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A> List<A> from(Iterable<? extends A> paramIterable) {
/*     */     // Byte code:
/*     */     //   0: new com/sun/tools/javac/util/ListBuffer
/*     */     //   3: dup
/*     */     //   4: invokespecial <init> : ()V
/*     */     //   7: astore_1
/*     */     //   8: aload_0
/*     */     //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   14: astore_2
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface hasNext : ()Z
/*     */     //   21: ifeq -> 40
/*     */     //   24: aload_2
/*     */     //   25: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   30: astore_3
/*     */     //   31: aload_1
/*     */     //   32: aload_3
/*     */     //   33: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*     */     //   36: pop
/*     */     //   37: goto -> 15
/*     */     //   40: aload_1
/*     */     //   41: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*     */     //   44: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #170	-> 0
/*     */     //   #171	-> 8
/*     */     //   #172	-> 31
/*     */     //   #173	-> 37
/*     */     //   #174	-> 40
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <A> List<A> fill(int paramInt, A paramA) {
/* 183 */     List<?> list = nil();
/* 184 */     for (byte b = 0; b < paramInt; ) { list = new List(paramA, list); b++; }
/* 185 */      return (List)list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 192 */     return (this.tail == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nonEmpty() {
/* 199 */     return (this.tail != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 206 */     List<A> list = this;
/* 207 */     byte b = 0;
/* 208 */     while (list.tail != null) {
/* 209 */       list = list.tail;
/* 210 */       b++;
/*     */     } 
/* 212 */     return b;
/*     */   }
/*     */   
/*     */   public int size() {
/* 216 */     return length();
/*     */   }
/*     */   
/*     */   public List<A> setTail(List<A> paramList) {
/* 220 */     this.tail = paramList;
/* 221 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> prepend(A paramA) {
/* 228 */     return new List(paramA, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> prependList(List<A> paramList) {
/* 235 */     if (isEmpty()) return paramList; 
/* 236 */     if (paramList.isEmpty()) return this; 
/* 237 */     if (paramList.tail.isEmpty()) return prepend(paramList.head);
/*     */     
/* 239 */     List<A> list1 = this;
/* 240 */     List<A> list2 = paramList.reverse();
/* 241 */     Assert.check((list2 != paramList));
/*     */ 
/*     */     
/* 244 */     while (list2.nonEmpty()) {
/* 245 */       List<A> list = list2;
/* 246 */       list2 = list2.tail;
/* 247 */       list.setTail(list1);
/* 248 */       list1 = list;
/*     */     } 
/* 250 */     return list1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> reverse() {
/* 259 */     if (isEmpty() || this.tail.isEmpty()) {
/* 260 */       return this;
/*     */     }
/* 262 */     List<?> list = nil();
/* 263 */     for (List<A> list1 = this; list1.nonEmpty(); list1 = list1.tail)
/* 264 */       list = new List(list1.head, (List)list); 
/* 265 */     return (List)list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> append(A paramA) {
/* 272 */     return of(paramA).prependList(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> appendList(List<A> paramList) {
/* 279 */     return paramList.prependList(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<A> appendList(ListBuffer<A> paramListBuffer) {
/* 287 */     return appendList(paramListBuffer.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] paramArrayOfT) {
/* 295 */     byte b = 0;
/* 296 */     List<A> list = this;
/* 297 */     T[] arrayOfT = paramArrayOfT;
/* 298 */     while (list.nonEmpty() && b < paramArrayOfT.length) {
/* 299 */       arrayOfT[b] = (T)list.head;
/* 300 */       list = list.tail;
/* 301 */       b++;
/*     */     } 
/* 303 */     if (list.isEmpty()) {
/* 304 */       if (b < paramArrayOfT.length)
/* 305 */         paramArrayOfT[b] = null; 
/* 306 */       return paramArrayOfT;
/*     */     } 
/*     */     
/* 309 */     paramArrayOfT = (T[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), size());
/* 310 */     return toArray(paramArrayOfT);
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 314 */     return toArray(new Object[size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(String paramString) {
/* 320 */     if (isEmpty()) {
/* 321 */       return "";
/*     */     }
/* 323 */     StringBuilder stringBuilder = new StringBuilder();
/* 324 */     stringBuilder.append(this.head);
/* 325 */     for (List<A> list = this.tail; list.nonEmpty(); list = list.tail) {
/* 326 */       stringBuilder.append(paramString);
/* 327 */       stringBuilder.append(list.head);
/*     */     } 
/* 329 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 337 */     return toString(",");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 345 */     List<A> list = this;
/* 346 */     int i = 1;
/* 347 */     while (list.tail != null) {
/* 348 */       i = i * 31 + ((list.head == null) ? 0 : list.head.hashCode());
/* 349 */       list = list.tail;
/*     */     } 
/* 351 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 359 */     if (paramObject instanceof List)
/* 360 */       return equals(this, (List)paramObject); 
/* 361 */     if (paramObject instanceof List) {
/* 362 */       List<A> list = this;
/* 363 */       Iterator<Object> iterator = ((List)paramObject).iterator();
/* 364 */       while (list.tail != null && iterator.hasNext()) {
/* 365 */         Object object = iterator.next();
/* 366 */         if ((list.head == null) ? (object == null) : list.head.equals(object)) {
/*     */           
/* 368 */           list = list.tail; continue;
/*     */         }  return false;
/* 370 */       }  return (list.isEmpty() && !iterator.hasNext());
/*     */     } 
/* 372 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(List<?> paramList1, List<?> paramList2) {
/* 378 */     while (paramList1.tail != null && paramList2.tail != null) {
/* 379 */       if (paramList1.head == null)
/* 380 */       { if (paramList2.head != null) return false;
/*     */          }
/* 382 */       else if (!paramList1.head.equals(paramList2.head)) { return false; }
/*     */       
/* 384 */       paramList1 = paramList1.tail;
/* 385 */       paramList2 = paramList2.tail;
/*     */     } 
/* 387 */     return (paramList1.tail == null && paramList2.tail == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object paramObject) {
/* 394 */     List<A> list = this;
/* 395 */     while (list.tail != null) {
/* 396 */       if (paramObject == null)
/* 397 */       { if (list.head == null) return true;
/*     */          }
/* 399 */       else if (list.head.equals(paramObject)) { return true; }
/*     */       
/* 401 */       list = list.tail;
/*     */     } 
/* 403 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public A last() {
/*     */     A a;
/* 409 */     Object object = null;
/* 410 */     List<A> list = this;
/* 411 */     while (list.tail != null) {
/* 412 */       a = list.head;
/* 413 */       list = list.tail;
/*     */     } 
/* 415 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> List<T> convert(Class<T> paramClass, List<?> paramList) {
/* 420 */     if (paramList == null)
/* 421 */       return null; 
/* 422 */     for (Object object : paramList)
/* 423 */       paramClass.cast(object); 
/* 424 */     return (List)paramList;
/*     */   }
/*     */   
/* 427 */   private static final Iterator<?> EMPTYITERATOR = new Iterator() {
/*     */       public boolean hasNext() {
/* 429 */         return false;
/*     */       }
/*     */       public Object next() {
/* 432 */         throw new NoSuchElementException();
/*     */       }
/*     */       public void remove() {
/* 435 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static <A> Iterator<A> emptyIterator() {
/* 441 */     return (Iterator)EMPTYITERATOR;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<A> iterator() {
/* 446 */     if (this.tail == null)
/* 447 */       return emptyIterator(); 
/* 448 */     return new Iterator<A>() {
/* 449 */         List<A> elems = List.this;
/*     */         public boolean hasNext() {
/* 451 */           return (this.elems.tail != null);
/*     */         }
/*     */         public A next() {
/* 454 */           if (this.elems.tail == null)
/* 455 */             throw new NoSuchElementException(); 
/* 456 */           A a = this.elems.head;
/* 457 */           this.elems = this.elems.tail;
/* 458 */           return a;
/*     */         }
/*     */         public void remove() {
/* 461 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public A get(int paramInt) {
/* 467 */     if (paramInt < 0) {
/* 468 */       throw new IndexOutOfBoundsException(String.valueOf(paramInt));
/*     */     }
/* 470 */     List<A> list = this;
/* 471 */     for (int i = paramInt; i-- > 0 && !list.isEmpty(); list = list.tail);
/*     */ 
/*     */     
/* 474 */     if (list.isEmpty())
/* 475 */       throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + 
/* 476 */           size()); 
/* 477 */     return list.head;
/*     */   }
/*     */   
/*     */   public boolean addAll(int paramInt, Collection<? extends A> paramCollection) {
/* 481 */     if (paramCollection.isEmpty())
/* 482 */       return false; 
/* 483 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public A set(int paramInt, A paramA) {
/* 487 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void add(int paramInt, A paramA) {
/* 491 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public A remove(int paramInt) {
/* 495 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int indexOf(Object paramObject) {
/* 499 */     byte b = 0;
/* 500 */     for (List<A> list = this; list.tail != null; list = list.tail, b++) {
/* 501 */       if ((list.head == null) ? (paramObject == null) : list.head.equals(paramObject))
/* 502 */         return b; 
/*     */     } 
/* 504 */     return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object paramObject) {
/* 508 */     byte b = -1;
/* 509 */     byte b1 = 0;
/* 510 */     for (List<A> list = this; list.tail != null; list = list.tail, b1++) {
/* 511 */       if ((list.head == null) ? (paramObject == null) : list.head.equals(paramObject))
/* 512 */         b = b1; 
/*     */     } 
/* 514 */     return b;
/*     */   }
/*     */   
/*     */   public ListIterator<A> listIterator() {
/* 518 */     return Collections.<A>unmodifiableList(new ArrayList<>(this)).listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator<A> listIterator(int paramInt) {
/* 522 */     return Collections.<A>unmodifiableList(new ArrayList<>(this)).listIterator(paramInt);
/*     */   }
/*     */   
/*     */   public List<A> subList(int paramInt1, int paramInt2) {
/* 526 */     if (paramInt1 < 0 || paramInt2 > size() || paramInt1 > paramInt2) {
/* 527 */       throw new IllegalArgumentException();
/*     */     }
/* 529 */     ArrayList<A> arrayList = new ArrayList(paramInt2 - paramInt1);
/* 530 */     int i = 0;
/* 531 */     for (List<A> list = this; list.tail != null && 
/* 532 */       i != paramInt2; list = list.tail, i++) {
/*     */       
/* 534 */       if (i >= paramInt1) {
/* 535 */         arrayList.add(list.head);
/*     */       }
/*     */     } 
/* 538 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\List.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */