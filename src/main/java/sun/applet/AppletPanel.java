/*      */ package sun.applet;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.applet.AppletStub;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Font;
/*      */ import java.awt.Frame;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.JarURLConnection;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessControlException;
/*      */ import java.security.AccessController;
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.security.Policy;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.EmbeddedFrame;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.misc.MessageUtils;
/*      */ import sun.misc.PerformanceLogger;
/*      */ import sun.misc.Queue;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AppletPanel
/*      */   extends Panel
/*      */   implements AppletStub, Runnable
/*      */ {
/*      */   Applet applet;
/*      */   protected boolean doInit = true;
/*      */   protected AppletClassLoader loader;
/*      */   public static final int APPLET_DISPOSE = 0;
/*      */   public static final int APPLET_LOAD = 1;
/*      */   public static final int APPLET_INIT = 2;
/*      */   public static final int APPLET_START = 3;
/*      */   public static final int APPLET_STOP = 4;
/*      */   public static final int APPLET_DESTROY = 5;
/*      */   public static final int APPLET_QUIT = 6;
/*      */   public static final int APPLET_ERROR = 7;
/*      */   public static final int APPLET_RESIZE = 51234;
/*      */   public static final int APPLET_LOADING = 51235;
/*      */   public static final int APPLET_LOADING_COMPLETED = 51236;
/*      */   protected int status;
/*      */   protected Thread handler;
/*  119 */   Dimension defaultAppletSize = new Dimension(10, 10);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  124 */   Dimension currentAppletSize = new Dimension(10, 10);
/*      */   
/*  126 */   MessageUtils mu = new MessageUtils();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  132 */   Thread loaderThread = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean loadAbortRequest = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  150 */   private static int threadGroupNumber = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private AppletListener listeners;
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setupAppletAppContext() {}
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void createAppletThread() {
/*  163 */     String str1 = "applet-" + getCode();
/*  164 */     this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
/*  165 */     this.loader.grab();
/*      */ 
/*      */ 
/*      */     
/*  169 */     String str2 = getParameter("codebase_lookup");
/*      */     
/*  171 */     if (str2 != null && str2.equals("false")) {
/*  172 */       this.loader.setCodebaseLookup(false);
/*      */     } else {
/*  174 */       this.loader.setCodebaseLookup(true);
/*      */     } 
/*      */     
/*  177 */     ThreadGroup threadGroup = this.loader.getThreadGroup();
/*      */     
/*  179 */     this.handler = new Thread(threadGroup, this, "thread " + str1);
/*      */     
/*  181 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/*  184 */             AppletPanel.this.handler.setContextClassLoader(AppletPanel.this.loader);
/*  185 */             return null;
/*      */           }
/*      */         });
/*  188 */     this.handler.start();
/*      */   }
/*      */   
/*      */   void joinAppletThread() throws InterruptedException {
/*  192 */     if (this.handler != null) {
/*  193 */       this.handler.join();
/*  194 */       this.handler = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   void release() {
/*  199 */     if (this.loader != null) {
/*  200 */       this.loader.release();
/*  201 */       this.loader = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void init() {
/*      */     try {
/*  211 */       this.defaultAppletSize.width = getWidth();
/*  212 */       this.currentAppletSize.width = this.defaultAppletSize.width;
/*      */ 
/*      */       
/*  215 */       this.defaultAppletSize.height = getHeight();
/*  216 */       this.currentAppletSize.height = this.defaultAppletSize.height;
/*      */     }
/*  218 */     catch (NumberFormatException numberFormatException) {
/*      */ 
/*      */       
/*  221 */       this.status = 7;
/*  222 */       showAppletStatus("badattribute.exception");
/*  223 */       showAppletLog("badattribute.exception");
/*  224 */       showAppletException(numberFormatException);
/*      */     } 
/*      */     
/*  227 */     setLayout(new BorderLayout());
/*      */     
/*  229 */     createAppletThread();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Dimension minimumSize() {
/*  237 */     return new Dimension(this.defaultAppletSize.width, this.defaultAppletSize.height);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Dimension preferredSize() {
/*  246 */     return new Dimension(this.currentAppletSize.width, this.currentAppletSize.height);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  255 */   private Queue queue = null;
/*      */ 
/*      */   
/*      */   public synchronized void addAppletListener(AppletListener paramAppletListener) {
/*  259 */     this.listeners = AppletEventMulticaster.add(this.listeners, paramAppletListener);
/*      */   }
/*      */   
/*      */   public synchronized void removeAppletListener(AppletListener paramAppletListener) {
/*  263 */     this.listeners = AppletEventMulticaster.remove(this.listeners, paramAppletListener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispatchAppletEvent(int paramInt, Object paramObject) {
/*  271 */     if (this.listeners != null) {
/*  272 */       AppletEvent appletEvent = new AppletEvent(this, paramInt, paramObject);
/*  273 */       this.listeners.appletStateChanged(appletEvent);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendEvent(int paramInt) {
/*  281 */     synchronized (this) {
/*  282 */       if (this.queue == null)
/*      */       {
/*  284 */         this.queue = new Queue();
/*      */       }
/*  286 */       Integer integer = Integer.valueOf(paramInt);
/*  287 */       this.queue.enqueue(integer);
/*  288 */       notifyAll();
/*      */     } 
/*  290 */     if (paramInt == 6) {
/*      */       try {
/*  292 */         joinAppletThread();
/*  293 */       } catch (InterruptedException interruptedException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  298 */       if (this.loader == null)
/*  299 */         this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey()); 
/*  300 */       release();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized AppletEvent getNextEvent() throws InterruptedException {
/*  308 */     while (this.queue == null || this.queue.isEmpty()) {
/*  309 */       wait();
/*      */     }
/*  311 */     Integer integer = this.queue.dequeue();
/*  312 */     return new AppletEvent(this, integer.intValue(), null);
/*      */   }
/*      */   
/*      */   boolean emptyEventQueue() {
/*  316 */     if (this.queue == null || this.queue.isEmpty()) {
/*  317 */       return true;
/*      */     }
/*  319 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setExceptionStatus(AccessControlException paramAccessControlException) {
/*  330 */     Permission permission = paramAccessControlException.getPermission();
/*  331 */     if (permission instanceof RuntimePermission && 
/*  332 */       permission.getName().startsWith("modifyThread")) {
/*  333 */       if (this.loader == null)
/*  334 */         this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey()); 
/*  335 */       this.loader.setExceptionStatus();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*  372 */     Thread thread = Thread.currentThread();
/*  373 */     if (thread == this.loaderThread) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  378 */       runLoader();
/*      */       
/*      */       return;
/*      */     } 
/*  382 */     boolean bool = false;
/*  383 */     while (!bool && !thread.isInterrupted()) {
/*      */       AppletEvent appletEvent;
/*      */       try {
/*  386 */         appletEvent = getNextEvent();
/*  387 */       } catch (InterruptedException interruptedException) {
/*  388 */         showAppletStatus("bail");
/*      */         
/*      */         return;
/*      */       } 
/*      */       try {
/*      */         Font font;
/*  394 */         switch (appletEvent.getID()) {
/*      */           case 1:
/*  396 */             if (!okToLoad()) {
/*      */               break;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  407 */             if (this.loaderThread == null) {
/*      */ 
/*      */               
/*  410 */               setLoaderThread(new Thread(this));
/*  411 */               this.loaderThread.start();
/*      */               
/*  413 */               this.loaderThread.join();
/*  414 */               setLoaderThread((Thread)null);
/*      */             } 
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/*  424 */             if (this.status != 1 && this.status != 5) {
/*  425 */               showAppletStatus("notloaded");
/*      */               break;
/*      */             } 
/*  428 */             this.applet.resize(this.defaultAppletSize);
/*  429 */             if (this.doInit) {
/*  430 */               if (PerformanceLogger.loggingEnabled()) {
/*  431 */                 PerformanceLogger.setTime("Applet Init");
/*  432 */                 PerformanceLogger.outputLog();
/*      */               } 
/*  434 */               this.applet.init();
/*      */             } 
/*      */ 
/*      */             
/*  438 */             font = getFont();
/*  439 */             if (font == null || ("dialog"
/*  440 */               .equals(font.getFamily().toLowerCase(Locale.ENGLISH)) && font
/*  441 */               .getSize() == 12 && font.getStyle() == 0)) {
/*  442 */               setFont(new Font("Dialog", 0, 12));
/*      */             }
/*      */             
/*  445 */             this.doInit = true;
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  450 */               final AppletPanel p = this;
/*  451 */               Runnable runnable = new Runnable()
/*      */                 {
/*      */                   public void run() {
/*  454 */                     p.validate();
/*      */                   }
/*      */                 };
/*  457 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, runnable);
/*      */             }
/*  459 */             catch (InterruptedException interruptedException) {
/*      */             
/*  461 */             } catch (InvocationTargetException invocationTargetException) {}
/*      */ 
/*      */             
/*  464 */             this.status = 2;
/*  465 */             showAppletStatus("inited");
/*      */             break;
/*      */ 
/*      */           
/*      */           case 3:
/*  470 */             if (this.status != 2 && this.status != 4) {
/*  471 */               showAppletStatus("notinited");
/*      */               break;
/*      */             } 
/*  474 */             this.applet.resize(this.currentAppletSize);
/*  475 */             this.applet.start();
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  480 */               final AppletPanel p = this;
/*  481 */               final Applet a = this.applet;
/*  482 */               Runnable runnable = new Runnable()
/*      */                 {
/*      */                   public void run() {
/*  485 */                     p.validate();
/*  486 */                     a.setVisible(true);
/*      */ 
/*      */ 
/*      */                     
/*  490 */                     if (AppletPanel.this.hasInitialFocus()) {
/*  491 */                       AppletPanel.this.setDefaultFocus();
/*      */                     }
/*      */                   }
/*      */                 };
/*  495 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, runnable);
/*      */             }
/*  497 */             catch (InterruptedException interruptedException) {
/*      */             
/*  499 */             } catch (InvocationTargetException invocationTargetException) {}
/*      */ 
/*      */             
/*  502 */             this.status = 3;
/*  503 */             showAppletStatus("started");
/*      */             break;
/*      */ 
/*      */           
/*      */           case 4:
/*  508 */             if (this.status != 3) {
/*  509 */               showAppletStatus("notstarted");
/*      */               break;
/*      */             } 
/*  512 */             this.status = 4;
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  517 */               final Applet a = this.applet;
/*  518 */               Runnable runnable = new Runnable()
/*      */                 {
/*      */                   public void run() {
/*  521 */                     a.setVisible(false);
/*      */                   }
/*      */                 };
/*  524 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, runnable);
/*      */             }
/*  526 */             catch (InterruptedException interruptedException) {
/*      */             
/*  528 */             } catch (InvocationTargetException invocationTargetException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  538 */               this.applet.stop();
/*  539 */             } catch (AccessControlException accessControlException) {
/*  540 */               setExceptionStatus(accessControlException);
/*      */               
/*  542 */               throw accessControlException;
/*      */             } 
/*  544 */             showAppletStatus("stopped");
/*      */             break;
/*      */           
/*      */           case 5:
/*  548 */             if (this.status != 4 && this.status != 2) {
/*  549 */               showAppletStatus("notstopped");
/*      */               break;
/*      */             } 
/*  552 */             this.status = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  560 */               this.applet.destroy();
/*  561 */             } catch (AccessControlException accessControlException) {
/*  562 */               setExceptionStatus(accessControlException);
/*      */               
/*  564 */               throw accessControlException;
/*      */             } 
/*  566 */             showAppletStatus("destroyed");
/*      */             break;
/*      */           
/*      */           case 0:
/*  570 */             if (this.status != 5 && this.status != 1) {
/*  571 */               showAppletStatus("notdestroyed");
/*      */               break;
/*      */             } 
/*  574 */             this.status = 0;
/*      */             
/*      */             try {
/*  577 */               final Applet a = this.applet;
/*  578 */               Runnable runnable = new Runnable()
/*      */                 {
/*      */                   public void run() {
/*  581 */                     AppletPanel.this.remove(a);
/*      */                   }
/*      */                 };
/*  584 */               AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, runnable);
/*      */             }
/*  586 */             catch (InterruptedException interruptedException) {
/*      */ 
/*      */             
/*  589 */             } catch (InvocationTargetException invocationTargetException) {}
/*      */ 
/*      */             
/*  592 */             this.applet = null;
/*  593 */             showAppletStatus("disposed");
/*  594 */             bool = true;
/*      */             break;
/*      */           
/*      */           case 6:
/*      */             return;
/*      */         } 
/*  600 */       } catch (Exception exception) {
/*  601 */         this.status = 7;
/*  602 */         if (exception.getMessage() != null) {
/*  603 */           showAppletStatus("exception2", exception.getClass().getName(), exception
/*  604 */               .getMessage());
/*      */         } else {
/*  606 */           showAppletStatus("exception", exception.getClass().getName());
/*      */         } 
/*  608 */         showAppletException(exception);
/*  609 */       } catch (ThreadDeath threadDeath) {
/*  610 */         showAppletStatus("death");
/*      */         return;
/*  612 */       } catch (Error error) {
/*  613 */         this.status = 7;
/*  614 */         if (error.getMessage() != null) {
/*  615 */           showAppletStatus("error2", error.getClass().getName(), error
/*  616 */               .getMessage());
/*      */         } else {
/*  618 */           showAppletStatus("error", error.getClass().getName());
/*      */         } 
/*  620 */         showAppletException(error);
/*      */       } 
/*  622 */       clearLoadAbortRequest();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Component getMostRecentFocusOwnerForWindow(Window paramWindow) {
/*  633 */     Method method = AccessController.<Method>doPrivileged(new PrivilegedAction<Method>()
/*      */         {
/*      */           public Object run() {
/*  636 */             Method method = null;
/*      */             try {
/*  638 */               method = KeyboardFocusManager.class.getDeclaredMethod("getMostRecentFocusOwner", new Class[] { Window.class });
/*      */ 
/*      */               
/*  641 */               method.setAccessible(true);
/*  642 */             } catch (Exception exception) {
/*      */               
/*  644 */               exception.printStackTrace();
/*      */             } 
/*  646 */             return method;
/*      */           }
/*      */         });
/*  649 */     if (method != null) {
/*      */       
/*      */       try {
/*  652 */         return (Component)method.invoke(null, new Object[] { paramWindow });
/*  653 */       } catch (Exception exception) {
/*      */         
/*  655 */         exception.printStackTrace();
/*      */       } 
/*      */     }
/*      */     
/*  659 */     return paramWindow.getMostRecentFocusOwner();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setDefaultFocus() {
/*  667 */     Component component = null;
/*  668 */     Container container = getParent();
/*      */     
/*  670 */     if (container != null) {
/*  671 */       if (container instanceof Window) {
/*  672 */         component = getMostRecentFocusOwnerForWindow((Window)container);
/*  673 */         if (component == container || component == null)
/*      */         {
/*  675 */           component = container.getFocusTraversalPolicy().getInitialComponent((Window)container);
/*      */         }
/*  677 */       } else if (container.isFocusCycleRoot()) {
/*      */         
/*  679 */         component = container.getFocusTraversalPolicy().getDefaultComponent(container);
/*      */       } 
/*      */     }
/*      */     
/*  683 */     if (component != null) {
/*  684 */       if (container instanceof EmbeddedFrame) {
/*  685 */         ((EmbeddedFrame)container).synthesizeWindowActivation(true);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  691 */       component.requestFocusInWindow();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void runLoader() {
/*  702 */     if (this.status != 0) {
/*  703 */       showAppletStatus("notdisposed");
/*      */       
/*      */       return;
/*      */     } 
/*  707 */     dispatchAppletEvent(51235, (Object)null);
/*      */ 
/*      */ 
/*      */     
/*  711 */     this.status = 1;
/*      */ 
/*      */     
/*  714 */     this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  720 */     String str = getCode();
/*      */ 
/*      */ 
/*      */     
/*  724 */     setupAppletAppContext();
/*      */     
/*      */     try {
/*  727 */       loadJarFiles(this.loader);
/*  728 */       this.applet = createApplet(this.loader);
/*  729 */     } catch (ClassNotFoundException classNotFoundException) {
/*  730 */       this.status = 7;
/*  731 */       showAppletStatus("notfound", str);
/*  732 */       showAppletLog("notfound", str);
/*  733 */       showAppletException(classNotFoundException);
/*      */       return;
/*  735 */     } catch (InstantiationException instantiationException) {
/*  736 */       this.status = 7;
/*  737 */       showAppletStatus("nocreate", str);
/*  738 */       showAppletLog("nocreate", str);
/*  739 */       showAppletException(instantiationException);
/*      */       return;
/*  741 */     } catch (IllegalAccessException illegalAccessException) {
/*  742 */       this.status = 7;
/*  743 */       showAppletStatus("noconstruct", str);
/*  744 */       showAppletLog("noconstruct", str);
/*  745 */       showAppletException(illegalAccessException);
/*      */       
/*      */       return;
/*  748 */     } catch (Exception exception) {
/*  749 */       this.status = 7;
/*  750 */       showAppletStatus("exception", exception.getMessage());
/*  751 */       showAppletException(exception);
/*      */       return;
/*  753 */     } catch (ThreadDeath threadDeath) {
/*  754 */       this.status = 7;
/*  755 */       showAppletStatus("death");
/*      */       return;
/*  757 */     } catch (Error error) {
/*  758 */       this.status = 7;
/*  759 */       showAppletStatus("error", error.getMessage());
/*  760 */       showAppletException(error);
/*      */       
/*      */       return;
/*      */     } finally {
/*  764 */       dispatchAppletEvent(51236, (Object)null);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  770 */     if (this.applet != null) {
/*      */ 
/*      */       
/*  773 */       this.applet.setStub(this);
/*  774 */       this.applet.hide();
/*  775 */       add("Center", this.applet);
/*  776 */       showAppletStatus("loaded");
/*  777 */       validate();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected Applet createApplet(AppletClassLoader paramAppletClassLoader) throws ClassNotFoundException, IllegalAccessException, IOException, InstantiationException, InterruptedException {
/*  783 */     String str1 = getSerializedObject();
/*  784 */     String str2 = getCode();
/*      */     
/*  786 */     if (str2 != null && str1 != null) {
/*  787 */       System.err.println(amh.getMessage("runloader.err"));
/*      */       
/*  789 */       throw new InstantiationException("Either \"code\" or \"object\" should be specified, but not both.");
/*      */     } 
/*  791 */     if (str2 == null && str1 == null) {
/*  792 */       String str = "nocode";
/*  793 */       this.status = 7;
/*  794 */       showAppletStatus(str);
/*  795 */       showAppletLog(str);
/*  796 */       repaint();
/*      */     } 
/*  798 */     if (str2 != null) {
/*  799 */       this.applet = paramAppletClassLoader.loadCode(str2).newInstance();
/*  800 */       this.doInit = true;
/*      */     } else {
/*      */       
/*  803 */       try(InputStream null = (InputStream)AccessController.<InputStream>doPrivileged(() -> paramAppletClassLoader.getResourceAsStream(paramString)); 
/*      */           
/*  805 */           AppletObjectInputStream null = new AppletObjectInputStream(inputStream, paramAppletClassLoader)) {
/*      */         
/*  807 */         this.applet = (Applet)appletObjectInputStream.readObject();
/*  808 */         this.doInit = false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  816 */     findAppletJDKLevel(this.applet);
/*      */     
/*  818 */     if (Thread.interrupted()) {
/*      */       try {
/*  820 */         this.status = 0;
/*  821 */         this.applet = null;
/*      */ 
/*      */ 
/*      */         
/*  825 */         showAppletStatus("death");
/*      */       } finally {
/*  827 */         Thread.currentThread().interrupt();
/*      */       } 
/*  829 */       return null;
/*      */     } 
/*  831 */     return this.applet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void loadJarFiles(AppletClassLoader paramAppletClassLoader) throws IOException, InterruptedException {
/*  839 */     String str = getJarFiles();
/*      */     
/*  841 */     if (str != null) {
/*  842 */       StringTokenizer stringTokenizer = new StringTokenizer(str, ",", false);
/*  843 */       while (stringTokenizer.hasMoreTokens()) {
/*  844 */         String str1 = stringTokenizer.nextToken().trim();
/*      */         try {
/*  846 */           paramAppletClassLoader.addJar(str1);
/*  847 */         } catch (IllegalArgumentException illegalArgumentException) {}
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void stopLoading() {
/*  860 */     if (this.loaderThread != null) {
/*      */       
/*  862 */       this.loaderThread.interrupt();
/*      */     } else {
/*  864 */       setLoadAbortRequest();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected synchronized boolean okToLoad() {
/*  870 */     return !this.loadAbortRequest;
/*      */   }
/*      */   
/*      */   protected synchronized void clearLoadAbortRequest() {
/*  874 */     this.loadAbortRequest = false;
/*      */   }
/*      */   
/*      */   protected synchronized void setLoadAbortRequest() {
/*  878 */     this.loadAbortRequest = true;
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void setLoaderThread(Thread paramThread) {
/*  883 */     this.loaderThread = paramThread;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isActive() {
/*  891 */     return (this.status == 3);
/*      */   }
/*      */ 
/*      */   
/*  895 */   private EventQueue appEvtQ = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appletResize(int paramInt1, int paramInt2) {
/*  901 */     this.currentAppletSize.width = paramInt1;
/*  902 */     this.currentAppletSize.height = paramInt2;
/*  903 */     final Dimension currentSize = new Dimension(this.currentAppletSize.width, this.currentAppletSize.height);
/*      */ 
/*      */     
/*  906 */     if (this.loader != null) {
/*  907 */       AppContext appContext = this.loader.getAppContext();
/*  908 */       if (appContext != null) {
/*  909 */         this.appEvtQ = (EventQueue)appContext.get(AppContext.EVENT_QUEUE_KEY);
/*      */       }
/*      */     } 
/*  912 */     final AppletPanel ap = this;
/*  913 */     if (this.appEvtQ != null) {
/*  914 */       this.appEvtQ.postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), new Runnable()
/*      */             {
/*      */               public void run()
/*      */               {
/*  918 */                 if (ap != null) {
/*  919 */                   ap.dispatchAppletEvent(51234, currentSize);
/*      */                 }
/*      */               }
/*      */             }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  930 */     super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*  931 */     this.currentAppletSize.width = paramInt3;
/*  932 */     this.currentAppletSize.height = paramInt4;
/*      */   }
/*      */   
/*      */   public Applet getApplet() {
/*  936 */     return this.applet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void showAppletStatus(String paramString) {
/*  944 */     getAppletContext().showStatus(amh.getMessage(paramString));
/*      */   }
/*      */   
/*      */   protected void showAppletStatus(String paramString, Object paramObject) {
/*  948 */     getAppletContext().showStatus(amh.getMessage(paramString, paramObject));
/*      */   }
/*      */   protected void showAppletStatus(String paramString, Object paramObject1, Object paramObject2) {
/*  951 */     getAppletContext().showStatus(amh.getMessage(paramString, paramObject1, paramObject2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void showAppletLog(String paramString) {
/*  958 */     System.out.println(amh.getMessage(paramString));
/*      */   }
/*      */   
/*      */   protected void showAppletLog(String paramString, Object paramObject) {
/*  962 */     System.out.println(amh.getMessage(paramString, paramObject));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void showAppletException(Throwable paramThrowable) {
/*  970 */     paramThrowable.printStackTrace();
/*  971 */     repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassLoaderCacheKey() {
/*  984 */     return getCodeBase().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  990 */   private static HashMap classloaders = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized void flushClassLoader(String paramString) {
/*  996 */     classloaders.remove(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized void flushClassLoaders() {
/* 1003 */     classloaders = new HashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AppletClassLoader createClassLoader(URL paramURL) {
/* 1013 */     return new AppletClassLoader(paramURL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized AppletClassLoader getClassLoader(final URL codebase, final String key) {
/* 1020 */     AppletClassLoader appletClassLoader = (AppletClassLoader)classloaders.get(key);
/* 1021 */     if (appletClassLoader == null) {
/*      */       
/* 1023 */       AccessControlContext accessControlContext = getAccessControlContext(codebase);
/*      */       
/* 1025 */       appletClassLoader = AccessController.<AppletClassLoader>doPrivileged(new PrivilegedAction<AppletClassLoader>()
/*      */           {
/*      */             public Object run() {
/* 1028 */               AppletClassLoader appletClassLoader = AppletPanel.this.createClassLoader(codebase);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1044 */               synchronized (getClass()) {
/*      */                 
/* 1046 */                 AppletClassLoader appletClassLoader1 = (AppletClassLoader)AppletPanel.classloaders.get(key);
/* 1047 */                 if (appletClassLoader1 == null) {
/* 1048 */                   AppletPanel.classloaders.put(key, appletClassLoader);
/* 1049 */                   return appletClassLoader;
/*      */                 } 
/* 1051 */                 return appletClassLoader1;
/*      */               } 
/*      */             }
/*      */           }accessControlContext);
/*      */     } 
/*      */     
/* 1057 */     return appletClassLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AccessControlContext getAccessControlContext(URL paramURL) {
/*      */     Permission permission;
/* 1069 */     PermissionCollection permissionCollection = AccessController.<PermissionCollection>doPrivileged(new PrivilegedAction<PermissionCollection>()
/*      */         {
/*      */           public Object run() {
/* 1072 */             Policy policy = Policy.getPolicy();
/* 1073 */             if (policy != null) {
/* 1074 */               return policy.getPermissions(new CodeSource(null, (Certificate[])null));
/*      */             }
/*      */             
/* 1077 */             return null;
/*      */           }
/*      */         });
/*      */ 
/*      */     
/* 1082 */     if (permissionCollection == null) {
/* 1083 */       permissionCollection = new Permissions();
/*      */     }
/*      */ 
/*      */     
/* 1087 */     permissionCollection.add(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
/*      */ 
/*      */     
/* 1090 */     URLConnection uRLConnection = null;
/*      */     try {
/* 1092 */       uRLConnection = paramURL.openConnection();
/* 1093 */       permission = uRLConnection.getPermission();
/* 1094 */     } catch (IOException iOException) {
/* 1095 */       permission = null;
/*      */     } 
/*      */     
/* 1098 */     if (permission != null) {
/* 1099 */       permissionCollection.add(permission);
/*      */     }
/* 1101 */     if (permission instanceof FilePermission) {
/*      */       
/* 1103 */       String str = permission.getName();
/*      */       
/* 1105 */       int i = str.lastIndexOf(File.separatorChar);
/*      */       
/* 1107 */       if (i != -1) {
/* 1108 */         str = str.substring(0, i + 1);
/*      */         
/* 1110 */         if (str.endsWith(File.separator)) {
/* 1111 */           str = str + "-";
/*      */         }
/* 1113 */         permissionCollection.add(new FilePermission(str, "read"));
/*      */       } 
/*      */     } else {
/*      */       
/* 1117 */       URL uRL = paramURL;
/* 1118 */       if (uRLConnection instanceof JarURLConnection) {
/* 1119 */         uRL = ((JarURLConnection)uRLConnection).getJarFileURL();
/*      */       }
/* 1121 */       String str = uRL.getHost();
/* 1122 */       if (str != null && str.length() > 0) {
/* 1123 */         permissionCollection.add(new SocketPermission(str, "connect,accept"));
/*      */       }
/*      */     } 
/*      */     
/* 1127 */     ProtectionDomain protectionDomain = new ProtectionDomain(new CodeSource(paramURL, (Certificate[])null), permissionCollection);
/*      */ 
/*      */     
/* 1130 */     return new AccessControlContext(new ProtectionDomain[] { protectionDomain });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Thread getAppletHandlerThread() {
/* 1137 */     return this.handler;
/*      */   }
/*      */   
/*      */   public int getAppletWidth() {
/* 1141 */     return this.currentAppletSize.width;
/*      */   }
/*      */   
/*      */   public int getAppletHeight() {
/* 1145 */     return this.currentAppletSize.height;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void changeFrameAppContext(Frame paramFrame, AppContext paramAppContext) {
/* 1165 */     AppContext appContext = SunToolkit.targetToAppContext(paramFrame);
/*      */     
/* 1167 */     if (appContext == paramAppContext) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1172 */     synchronized (Window.class) {
/*      */       
/* 1174 */       WeakReference<Frame> weakReference = null;
/*      */ 
/*      */ 
/*      */       
/* 1178 */       Vector<WeakReference<Frame>> vector = (Vector)appContext.get(Window.class);
/* 1179 */       if (vector != null) {
/* 1180 */         for (WeakReference<Frame> weakReference1 : (Iterable<WeakReference<Frame>>)vector) {
/* 1181 */           if (weakReference1.get() == paramFrame) {
/* 1182 */             weakReference = weakReference1;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 1187 */         if (weakReference != null) {
/* 1188 */           vector.remove(weakReference);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1193 */       SunToolkit.insertTargetMapping(paramFrame, paramAppContext);
/*      */ 
/*      */ 
/*      */       
/* 1197 */       vector = (Vector)paramAppContext.get(Window.class);
/* 1198 */       if (vector == null) {
/* 1199 */         vector = new Vector();
/* 1200 */         paramAppContext.put(Window.class, vector);
/*      */       } 
/*      */       
/* 1203 */       vector.add(weakReference);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean jdk11Applet = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean jdk12Applet = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void findAppletJDKLevel(Applet paramApplet) {
/* 1226 */     Class<?> clazz = paramApplet.getClass();
/*      */     
/* 1228 */     synchronized (clazz) {
/*      */ 
/*      */       
/* 1231 */       Boolean bool1 = this.loader.isJDK11Target(clazz);
/* 1232 */       Boolean bool2 = this.loader.isJDK12Target(clazz);
/*      */ 
/*      */ 
/*      */       
/* 1236 */       if (bool1 != null || bool2 != null) {
/* 1237 */         this.jdk11Applet = (bool1 == null) ? false : bool1.booleanValue();
/* 1238 */         this.jdk12Applet = (bool2 == null) ? false : bool2.booleanValue();
/*      */         
/*      */         return;
/*      */       } 
/* 1242 */       String str1 = clazz.getName();
/*      */ 
/*      */       
/* 1245 */       str1 = str1.replace('.', '/');
/*      */ 
/*      */       
/* 1248 */       String str2 = str1 + ".class";
/*      */       
/* 1250 */       byte[] arrayOfByte = new byte[8];
/*      */       
/* 1252 */       try (InputStream null = (InputStream)AccessController.<InputStream>doPrivileged(() -> this.loader.getResourceAsStream(paramString))) {
/*      */ 
/*      */ 
/*      */         
/* 1256 */         int j = inputStream.read(arrayOfByte, 0, 8);
/*      */ 
/*      */ 
/*      */         
/* 1260 */         if (j != 8) {
/*      */           return;
/*      */         }
/* 1263 */       } catch (IOException iOException) {
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/* 1268 */       int i = readShort(arrayOfByte, 6);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1276 */       if (i < 46) {
/* 1277 */         this.jdk11Applet = true;
/* 1278 */       } else if (i == 46) {
/* 1279 */         this.jdk12Applet = true;
/*      */       } 
/*      */ 
/*      */       
/* 1283 */       this.loader.setJDK11Target(clazz, this.jdk11Applet);
/* 1284 */       this.loader.setJDK12Target(clazz, this.jdk12Applet);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isJDK11Applet() {
/* 1292 */     return this.jdk11Applet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isJDK12Applet() {
/* 1299 */     return this.jdk12Applet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readShort(byte[] paramArrayOfbyte, int paramInt) {
/* 1306 */     int i = readByte(paramArrayOfbyte[paramInt]);
/* 1307 */     int j = readByte(paramArrayOfbyte[paramInt + 1]);
/* 1308 */     return i << 8 | j;
/*      */   }
/*      */   
/*      */   private int readByte(byte paramByte) {
/* 1312 */     return paramByte & 0xFF;
/*      */   }
/*      */ 
/*      */   
/* 1316 */   private static AppletMessageHandler amh = new AppletMessageHandler("appletpanel");
/*      */   
/*      */   protected abstract String getCode();
/*      */   
/*      */   protected abstract String getJarFiles();
/*      */   
/*      */   protected abstract String getSerializedObject();
/*      */   
/*      */   public abstract int getWidth();
/*      */   
/*      */   public abstract int getHeight();
/*      */   
/*      */   public abstract boolean hasInitialFocus();
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\applet\AppletPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */