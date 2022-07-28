/*     */ package com.sun.tools.javac.code;
/*     */
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Filter;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.util.Iterator;
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
/*     */
/*     */
/*     */
/*     */
/*     */ public class Scope
/*     */ {
/*     */   private int shared;
/*     */   public Scope next;
/*     */   public Symbol owner;
/*     */   Entry[] table;
/*     */   int hashMask;
/*     */   public Entry elems;
/*  74 */   int nelems = 0;
/*     */
/*     */
/*     */
/*  78 */   List<ScopeListener> listeners = List.nil();
/*     */
/*     */
/*     */
/*     */
/*  83 */   private static final Entry sentinel = new Entry(null, null, null, null);
/*     */
/*     */
/*     */
/*     */   private static final int INITIAL_SIZE = 16;
/*     */
/*     */
/*     */
/*  91 */   public static final Scope emptyScope = new Scope(null, null, new Entry[0]);
/*     */
/*     */
/*     */
/*     */
/*     */   private Scope(Scope paramScope, Symbol paramSymbol, Entry[] paramArrayOfEntry) {
/*  97 */     this.next = paramScope;
/*  98 */     Assert.check((emptyScope == null || paramSymbol != null));
/*  99 */     this.owner = paramSymbol;
/* 100 */     this.table = paramArrayOfEntry;
/* 101 */     this.hashMask = paramArrayOfEntry.length - 1;
/*     */   }
/*     */
/*     */
/*     */   private Scope(Scope paramScope, Symbol paramSymbol, Entry[] paramArrayOfEntry, int paramInt) {
/* 106 */     this(paramScope, paramSymbol, paramArrayOfEntry);
/* 107 */     this.nelems = paramInt;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public Scope(Symbol paramSymbol) {
/* 114 */     this(null, paramSymbol, new Entry[16]);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Scope dup() {
/* 123 */     return dup(this.owner);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Scope dup(Symbol paramSymbol) {
/* 132 */     Scope scope = new Scope(this, paramSymbol, this.table, this.nelems);
/* 133 */     this.shared++;
/*     */
/*     */
/* 136 */     return scope;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Scope dupUnshared() {
/* 144 */     return new Scope(this, this.owner, (Entry[])this.table.clone(), this.nelems);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public Scope leave() {
/* 151 */     Assert.check((this.shared == 0));
/* 152 */     if (this.table != this.next.table) return this.next;
/* 153 */     while (this.elems != null) {
/* 154 */       int i = getIndex(this.elems.sym.name);
/* 155 */       Entry entry = this.table[i];
/* 156 */       Assert.check((entry == this.elems), this.elems.sym);
/* 157 */       this.table[i] = this.elems.shadowed;
/* 158 */       this.elems = this.elems.sibling;
/*     */     }
/* 160 */     Assert.check((this.next.shared > 0));
/* 161 */     this.next.shared--;
/* 162 */     this.next.nelems = this.nelems;
/*     */
/*     */
/* 165 */     return this.next;
/*     */   }
/*     */
/*     */
/*     */
/*     */   private void dble() {
/* 171 */     Assert.check((this.shared == 0));
/* 172 */     Entry[] arrayOfEntry1 = this.table;
/* 173 */     Entry[] arrayOfEntry2 = new Entry[arrayOfEntry1.length * 2];
/* 174 */     for (Scope scope = this; scope != null; scope = scope.next) {
/* 175 */       if (scope.table == arrayOfEntry1) {
/* 176 */         Assert.check((scope == this || scope.shared != 0));
/* 177 */         scope.table = arrayOfEntry2;
/* 178 */         scope.hashMask = arrayOfEntry2.length - 1;
/*     */       }
/*     */     }
/* 181 */     byte b = 0;
/* 182 */     for (int i = arrayOfEntry1.length; --i >= 0; ) {
/* 183 */       Entry entry = arrayOfEntry1[i];
/* 184 */       if (entry != null && entry != sentinel) {
/* 185 */         this.table[getIndex(entry.sym.name)] = entry;
/* 186 */         b++;
/*     */       }
/*     */     }
/*     */
/*     */
/* 191 */     this.nelems = b;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void enter(Symbol paramSymbol) {
/* 197 */     Assert.check((this.shared == 0));
/* 198 */     enter(paramSymbol, this);
/*     */   }
/*     */
/*     */   public void enter(Symbol paramSymbol, Scope paramScope) {
/* 202 */     enter(paramSymbol, paramScope, paramScope, false);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void enter(Symbol paramSymbol, Scope paramScope1, Scope paramScope2, boolean paramBoolean) {
/* 211 */     Assert.check((this.shared == 0));
/* 212 */     if (this.nelems * 3 >= this.hashMask * 2)
/* 213 */       dble();
/* 214 */     int i = getIndex(paramSymbol.name);
/* 215 */     Entry entry1 = this.table[i];
/* 216 */     if (entry1 == null) {
/* 217 */       entry1 = sentinel;
/* 218 */       this.nelems++;
/*     */     }
/* 220 */     Entry entry2 = makeEntry(paramSymbol, entry1, this.elems, paramScope1, paramScope2, paramBoolean);
/* 221 */     this.table[i] = entry2;
/* 222 */     this.elems = entry2;
/*     */
/*     */
/* 225 */     for (List<ScopeListener> list = this.listeners; list.nonEmpty(); list = list.tail) {
/* 226 */       ((ScopeListener)list.head).symbolAdded(paramSymbol, this);
/*     */     }
/*     */   }
/*     */
/*     */   Entry makeEntry(Symbol paramSymbol, Entry paramEntry1, Entry paramEntry2, Scope paramScope1, Scope paramScope2, boolean paramBoolean) {
/* 231 */     return new Entry(paramSymbol, paramEntry1, paramEntry2, paramScope1);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void addScopeListener(ScopeListener paramScopeListener) {
/* 241 */     this.listeners = this.listeners.prepend(paramScopeListener);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void remove(final Symbol sym) {
/* 247 */     Assert.check((this.shared == 0));
/* 248 */     Entry entry1 = lookup(sym.name, new Filter<Symbol>()
/*     */         {
/*     */           public boolean accepts(Symbol param1Symbol) {
/* 251 */             return (param1Symbol == sym);
/*     */           }
/*     */         });
/* 254 */     if (entry1.scope == null) {
/*     */       return;
/*     */     }
/* 257 */     int i = getIndex(sym.name);
/* 258 */     Entry entry2 = this.table[i];
/* 259 */     if (entry2 == entry1)
/* 260 */     { this.table[i] = entry1.shadowed; }
/*     */     else { while (true) {
/* 262 */         if (entry2.shadowed == entry1) {
/* 263 */           entry2.shadowed = entry1.shadowed;
/*     */           break;
/*     */         }
/* 266 */         entry2 = entry2.shadowed;
/*     */       }  }
/*     */
/*     */
/* 270 */     entry2 = this.elems;
/* 271 */     if (entry2 == entry1)
/* 272 */     { this.elems = entry1.sibling; }
/*     */     else { while (true) {
/* 274 */         if (entry2.sibling == entry1) {
/* 275 */           entry2.sibling = entry1.sibling;
/*     */           break;
/*     */         }
/* 278 */         entry2 = entry2.sibling;
/*     */       }  }
/*     */
/*     */
/* 282 */     for (List<ScopeListener> list = this.listeners; list.nonEmpty(); list = list.tail) {
/* 283 */       ((ScopeListener)list.head).symbolRemoved(sym, this);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void enterIfAbsent(Symbol paramSymbol) {
/* 290 */     Assert.check((this.shared == 0));
/* 291 */     Entry entry = lookup(paramSymbol.name);
/* 292 */     for (; entry.scope == this && entry.sym.kind != paramSymbol.kind; entry = entry.next());
/* 293 */     if (entry.scope != this) enter(paramSymbol);
/*     */
/*     */   }
/*     */
/*     */
/*     */
/*     */   public boolean includes(Symbol paramSymbol) {
/* 300 */     Entry entry = lookup(paramSymbol.name);
/* 301 */     for (; entry.scope == this;
/* 302 */       entry = entry.next()) {
/* 303 */       if (entry.sym == paramSymbol) return true;
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */
/* 308 */   static final Filter<Symbol> noFilter = new Filter<Symbol>() {
/*     */       public boolean accepts(Symbol param1Symbol) {
/* 310 */         return true;
/*     */       }
/*     */     };
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Entry lookup(Name paramName) {
/* 321 */     return lookup(paramName, noFilter);
/*     */   }
/*     */
/*     */   public Entry lookup(Name paramName, Filter<Symbol> paramFilter) {
/* 325 */     Entry entry = this.table[getIndex(paramName)];
/* 326 */     if (entry == null || entry == sentinel)
/* 327 */       return sentinel;
/* 328 */     while (entry.scope != null && (entry.sym.name != paramName || !paramFilter.accepts(entry.sym)))
/* 329 */       entry = entry.shadowed;
/* 330 */     return entry;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   int getIndex(Name paramName) {
/* 348 */     int i = paramName.hashCode();
/* 349 */     int j = i & this.hashMask;
/*     */
/*     */
/* 352 */     int k = this.hashMask - (i + (i >> 16) << 1);
/* 353 */     int m = -1;
/*     */     while (true) {
/* 355 */       Entry entry = this.table[j];
/* 356 */       if (entry == null)
/* 357 */         return (m >= 0) ? m : j;
/* 358 */       if (entry == sentinel) {
/*     */
/*     */
/* 361 */         if (m < 0)
/* 362 */           m = j;
/* 363 */       } else if (entry.sym.name == paramName) {
/* 364 */         return j;
/* 365 */       }  j = j + k & this.hashMask;
/*     */     }
/*     */   }
/*     */
/*     */   public boolean anyMatch(Filter<Symbol> paramFilter) {
/* 370 */     return getElements(paramFilter).iterator().hasNext();
/*     */   }
/*     */
/*     */   public Iterable<Symbol> getElements() {
/* 374 */     return getElements(noFilter);
/*     */   }
/*     */
/*     */   public Iterable<Symbol> getElements(final Filter<Symbol> sf) {
/* 378 */     return new Iterable<Symbol>() {
/*     */         public Iterator<Symbol> iterator() {
/* 380 */           return new Iterator<Symbol>()
/*     */             {
/*     */               private Scope currScope;
/*     */
/*     */               private Entry currEntry;
/*     */
/*     */
/*     */               public boolean hasNext() {
/* 388 */                 return (this.currEntry != null);
/*     */               }
/*     */
/*     */               public Symbol next() {
/* 392 */                 Symbol symbol = (this.currEntry == null) ? null : this.currEntry.sym;
/* 393 */                 if (this.currEntry != null) {
/* 394 */                   this.currEntry = this.currEntry.sibling;
/*     */                 }
/* 396 */                 update();
/* 397 */                 return symbol;
/*     */               }
/*     */
/*     */               public void remove() {
/* 401 */                 throw new UnsupportedOperationException();
/*     */               }
/*     */
/*     */               private void update() {
/* 405 */                 skipToNextMatchingEntry();
/* 406 */                 while (this.currEntry == null && this.currScope.next != null) {
/* 407 */                   this.currScope = this.currScope.next;
/* 408 */                   this.currEntry = this.currScope.elems;
/* 409 */                   skipToNextMatchingEntry();
/*     */                 }
/*     */               }
/*     */
/*     */               void skipToNextMatchingEntry() {
/* 414 */                 while (this.currEntry != null && !sf.accepts(this.currEntry.sym)) {
/* 415 */                   this.currEntry = this.currEntry.sibling;
/*     */                 }
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */   public Iterable<Symbol> getElementsByName(Name paramName) {
/* 424 */     return getElementsByName(paramName, noFilter);
/*     */   }
/*     */
/*     */   public Iterable<Symbol> getElementsByName(final Name name, final Filter<Symbol> sf) {
/* 428 */     return new Iterable<Symbol>() {
/*     */         public Iterator<Symbol> iterator() {
/* 430 */           return new Iterator<Symbol>() {
/* 431 */               Entry currentEntry = Scope.this.lookup(name, sf);
/*     */
/*     */               public boolean hasNext() {
/* 434 */                 return (this.currentEntry.scope != null);
/*     */               }
/*     */               public Symbol next() {
/* 437 */                 Entry entry = this.currentEntry;
/* 438 */                 this.currentEntry = this.currentEntry.next(sf);
/* 439 */                 return entry.sym;
/*     */               }
/*     */               public void remove() {
/* 442 */                 throw new UnsupportedOperationException();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */   public String toString() {
/* 450 */     StringBuilder stringBuilder = new StringBuilder();
/* 451 */     stringBuilder.append("Scope[");
/* 452 */     for (Scope scope = this; scope != null; scope = scope.next) {
/* 453 */       if (scope != this) stringBuilder.append(" | ");
/* 454 */       for (Entry entry = scope.elems; entry != null; entry = entry.sibling) {
/* 455 */         if (entry != scope.elems) stringBuilder.append(", ");
/* 456 */         stringBuilder.append(entry.sym);
/*     */       }
/*     */     }
/* 459 */     stringBuilder.append("]");
/* 460 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class Entry
/*     */   {
/*     */     public Symbol sym;
/*     */
/*     */
/*     */
/*     */
/*     */     private Entry shadowed;
/*     */
/*     */
/*     */
/*     */     public Entry sibling;
/*     */
/*     */
/*     */
/*     */     public Scope scope;
/*     */
/*     */
/*     */
/*     */
/*     */     public Entry(Symbol param1Symbol, Entry param1Entry1, Entry param1Entry2, Scope param1Scope) {
/* 488 */       this.sym = param1Symbol;
/* 489 */       this.shadowed = param1Entry1;
/* 490 */       this.sibling = param1Entry2;
/* 491 */       this.scope = param1Scope;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public Entry next() {
/* 498 */       return this.shadowed;
/*     */     }
/*     */
/*     */     public Entry next(Filter<Symbol> param1Filter) {
/* 502 */       if (this.shadowed.sym == null || param1Filter.accepts(this.shadowed.sym)) return this.shadowed;
/* 503 */       return this.shadowed.next(param1Filter);
/*     */     }
/*     */
/*     */     public boolean isStaticallyImported() {
/* 507 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public Scope getOrigin() {
/* 517 */       return this.scope;
/*     */     }
/*     */   }
/*     */
/*     */   public static class ImportScope
/*     */     extends Scope {
/*     */     public ImportScope(Symbol param1Symbol) {
/* 524 */       super(param1Symbol);
/*     */     }
/*     */
/*     */
/*     */
/*     */     Entry makeEntry(Symbol param1Symbol, Entry param1Entry1, Entry param1Entry2, Scope param1Scope1, final Scope origin, final boolean staticallyImported) {
/* 530 */       return new Entry(param1Symbol, param1Entry1, param1Entry2, param1Scope1)
/*     */         {
/*     */           public Scope getOrigin() {
/* 533 */             return origin;
/*     */           }
/*     */
/*     */
/*     */           public boolean isStaticallyImported() {
/* 538 */             return staticallyImported;
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */
/*     */   public static class StarImportScope
/*     */     extends ImportScope implements ScopeListener {
/*     */     public StarImportScope(Symbol param1Symbol) {
/* 547 */       super(param1Symbol);
/*     */     }
/*     */
/*     */     public void importAll(Scope param1Scope) {
/* 551 */       for (Entry entry = param1Scope.elems; entry != null; entry = entry.sibling) {
/* 552 */         if (entry.sym.kind == 2 && !includes(entry.sym)) {
/* 553 */           enter(entry.sym, param1Scope);
/*     */         }
/*     */       }
/* 556 */       param1Scope.addScopeListener(this);
/*     */     }
/*     */
/*     */     public void symbolRemoved(Symbol param1Symbol, Scope param1Scope) {
/* 560 */       remove(param1Symbol);
/*     */     }
/*     */
/*     */     public void symbolAdded(Symbol param1Symbol, Scope param1Scope) {}
/*     */   }
/*     */
/*     */   public static class DelegatedScope
/*     */     extends Scope
/*     */   {
/*     */     Scope delegatee;
/* 570 */     public static final Entry[] emptyTable = new Entry[0];
/*     */
/*     */     public DelegatedScope(Scope param1Scope) {
/* 573 */       super(param1Scope, param1Scope.owner, emptyTable);
/* 574 */       this.delegatee = param1Scope;
/*     */     }
/*     */     public Scope dup() {
/* 577 */       return new DelegatedScope(this.next);
/*     */     }
/*     */     public Scope dupUnshared() {
/* 580 */       return new DelegatedScope(this.next);
/*     */     }
/*     */     public Scope leave() {
/* 583 */       return this.next;
/*     */     }
/*     */
/*     */
/*     */     public void enter(Symbol param1Symbol) {}
/*     */
/*     */     public void enter(Symbol param1Symbol, Scope param1Scope) {}
/*     */
/*     */     public void remove(Symbol param1Symbol) {
/* 592 */       throw new AssertionError(param1Symbol);
/*     */     }
/*     */     public Entry lookup(Name param1Name) {
/* 595 */       return this.delegatee.lookup(param1Name);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class CompoundScope
/*     */     extends Scope
/*     */     implements ScopeListener
/*     */   {
/* 607 */     public static final Entry[] emptyTable = new Entry[0];
/*     */
/* 609 */     private List<Scope> subScopes = List.nil();
/* 610 */     private int mark = 0;
/*     */
/*     */     public CompoundScope(Symbol param1Symbol) {
/* 613 */       super(null, param1Symbol, emptyTable);
/*     */     }
/*     */
/*     */     public void addSubScope(Scope param1Scope) {
/* 617 */       if (param1Scope != null) {
/* 618 */         this.subScopes = this.subScopes.prepend(param1Scope);
/* 619 */         param1Scope.addScopeListener(this);
/* 620 */         this.mark++;
/* 621 */         for (ScopeListener scopeListener : this.listeners) {
/* 622 */           scopeListener.symbolAdded(null, this);
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     public void symbolAdded(Symbol param1Symbol, Scope param1Scope) {
/* 628 */       this.mark++;
/* 629 */       for (ScopeListener scopeListener : this.listeners) {
/* 630 */         scopeListener.symbolAdded(param1Symbol, param1Scope);
/*     */       }
/*     */     }
/*     */
/*     */     public void symbolRemoved(Symbol param1Symbol, Scope param1Scope) {
/* 635 */       this.mark++;
/* 636 */       for (ScopeListener scopeListener : this.listeners) {
/* 637 */         scopeListener.symbolRemoved(param1Symbol, param1Scope);
/*     */       }
/*     */     }
/*     */
/*     */     public int getMark() {
/* 642 */       return this.mark;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 647 */       StringBuilder stringBuilder = new StringBuilder();
/* 648 */       stringBuilder.append("CompoundScope{");
/* 649 */       String str = "";
/* 650 */       for (Scope scope : this.subScopes) {
/* 651 */         stringBuilder.append(str);
/* 652 */         stringBuilder.append(scope);
/* 653 */         str = ",";
/*     */       }
/* 655 */       stringBuilder.append("}");
/* 656 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */
/*     */     public Iterable<Symbol> getElements(final Filter<Symbol> sf) {
/* 661 */       return new Iterable<Symbol>() {
/*     */           public Iterator<Symbol> iterator() {
/* 663 */             return new CompoundScopeIterator(CompoundScope.this.subScopes) {
/*     */                 Iterator<Symbol> nextIterator(Scope param3Scope) {
/* 665 */                   return param3Scope.getElements(sf).iterator();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */
/*     */
/*     */     public Iterable<Symbol> getElementsByName(final Name name, final Filter<Symbol> sf) {
/* 674 */       return new Iterable<Symbol>() {
/*     */           public Iterator<Symbol> iterator() {
/* 676 */             return new CompoundScopeIterator(CompoundScope.this.subScopes) {
/*     */                 Iterator<Symbol> nextIterator(Scope param3Scope) {
/* 678 */                   return param3Scope.getElementsByName(name, sf).iterator();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */
/*     */     abstract class CompoundScopeIterator
/*     */       implements Iterator<Symbol> {
/*     */       private Iterator<Symbol> currentIterator;
/*     */       private List<Scope> scopesToScan;
/*     */
/*     */       public CompoundScopeIterator(List<Scope> param2List) {
/* 691 */         this.scopesToScan = param2List;
/* 692 */         update();
/*     */       }
/*     */
/*     */
/*     */
/*     */       public boolean hasNext() {
/* 698 */         return (this.currentIterator != null);
/*     */       }
/*     */
/*     */       public Symbol next() {
/* 702 */         Symbol symbol = this.currentIterator.next();
/* 703 */         if (!this.currentIterator.hasNext()) {
/* 704 */           update();
/*     */         }
/* 706 */         return symbol;
/*     */       }
/*     */
/*     */       public void remove() {
/* 710 */         throw new UnsupportedOperationException();
/*     */       }
/*     */
/*     */       private void update() {
/* 714 */         while (this.scopesToScan.nonEmpty()) {
/* 715 */           this.currentIterator = nextIterator((Scope)this.scopesToScan.head);
/* 716 */           this.scopesToScan = this.scopesToScan.tail;
/* 717 */           if (this.currentIterator.hasNext())
/*     */             return;
/* 719 */         }  this.currentIterator = null;
/*     */       }
/*     */
/*     */       abstract Iterator<Symbol> nextIterator(Scope param2Scope); }
/*     */
/*     */     public Entry lookup(Name param1Name, Filter<Symbol> param1Filter) {
/* 725 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public Scope dup(Symbol param1Symbol) {
/* 730 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public void enter(Symbol param1Symbol, Scope param1Scope1, Scope param1Scope2, boolean param1Boolean) {
/* 735 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public void remove(Symbol param1Symbol) {
/* 740 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */
/*     */   public static class ErrorScope
/*     */     extends Scope {
/*     */     ErrorScope(Scope param1Scope, Symbol param1Symbol, Entry[] param1ArrayOfEntry) {
/* 747 */       super(param1Scope, param1Symbol, param1ArrayOfEntry);
/*     */     }
/*     */     public ErrorScope(Symbol param1Symbol) {
/* 750 */       super(param1Symbol);
/*     */     }
/*     */     public Scope dup() {
/* 753 */       return new ErrorScope(this, this.owner, this.table);
/*     */     }
/*     */     public Scope dupUnshared() {
/* 756 */       return new ErrorScope(this, this.owner, (Entry[])this.table.clone());
/*     */     }
/*     */     public Entry lookup(Name param1Name) {
/* 759 */       Entry entry = super.lookup(param1Name);
/* 760 */       if (entry.scope == null) {
/* 761 */         return new Entry(this.owner, null, null, null);
/*     */       }
/* 763 */       return entry;
/*     */     }
/*     */   }
/*     */
/*     */   public static interface ScopeListener {
/*     */     void symbolAdded(Symbol param1Symbol, Scope param1Scope);
/*     */
/*     */     void symbolRemoved(Symbol param1Symbol, Scope param1Scope);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Scope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
