/*    */ package sun.applet.resources;
/*    */ 
/*    */ import java.util.ListResourceBundle;
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
/*    */ public class MsgAppletViewer_es
/*    */   extends ListResourceBundle
/*    */ {
/*    */   public Object[][] getContents() {
/* 32 */     return new Object[][] { { "textframe.button.dismiss", "Descartar" }, { "appletviewer.tool.title", "Visor de Applet: {0}" }, { "appletviewer.menu.applet", "Applet" }, { "appletviewer.menuitem.restart", "Reiniciar" }, { "appletviewer.menuitem.reload", "Volver a Cargar" }, { "appletviewer.menuitem.stop", "Parar" }, { "appletviewer.menuitem.save", "Guardar..." }, { "appletviewer.menuitem.start", "Iniciar" }, { "appletviewer.menuitem.clone", "Clonar..." }, { "appletviewer.menuitem.tag", "Etiqueta..." }, { "appletviewer.menuitem.info", "Información..." }, { "appletviewer.menuitem.edit", "Editar" }, { "appletviewer.menuitem.encoding", "Codificación de Caracteres" }, { "appletviewer.menuitem.print", "Imprimir..." }, { "appletviewer.menuitem.props", "Propiedades..." }, { "appletviewer.menuitem.close", "Cerrar" }, { "appletviewer.menuitem.quit", "Salir" }, { "appletviewer.label.hello", "Hola..." }, { "appletviewer.status.start", "iniciando applet..." }, { "appletviewer.appletsave.filedialogtitle", "Serializar Applet en Archivo" }, { "appletviewer.appletsave.err1", "serializando {0} en {1}" }, { "appletviewer.appletsave.err2", "en appletSave: {0}" }, { "appletviewer.applettag", "Etiqueta Mostrada" }, { "appletviewer.applettag.textframe", "Etiqueta HTML de Applet" }, { "appletviewer.appletinfo.applet", "-- ninguna información de applet --" }, { "appletviewer.appletinfo.param", "-- ninguna información de parámetros --" }, { "appletviewer.appletinfo.textframe", "Información del Applet" }, { "appletviewer.appletprint.fail", "Fallo de impresión." }, { "appletviewer.appletprint.finish", "Impresión terminada." }, { "appletviewer.appletprint.cancel", "Impresión cancelada." }, { "appletviewer.appletencoding", "Codificación de Caracteres: {0}" }, { "appletviewer.parse.warning.requiresname", "Advertencia: la etiqueta <param name=... value=...> requiere un atributo name." }, { "appletviewer.parse.warning.paramoutside", "Advertencia: la etiqueta <param> está fuera de <applet> ... </applet>." }, { "appletviewer.parse.warning.applet.requirescode", "Advertencia: la etiqueta <applet> requiere el atributo code." }, { "appletviewer.parse.warning.applet.requiresheight", "Advertencia: la etiqueta <applet> requiere el atributo height." }, { "appletviewer.parse.warning.applet.requireswidth", "Advertencia: la etiqueta <applet> requiere el atributo width." }, { "appletviewer.parse.warning.object.requirescode", "Advertencia: la etiqueta <object> requiere el atributo code." }, { "appletviewer.parse.warning.object.requiresheight", "Advertencia: la etiqueta <object> requiere el atributo height." }, { "appletviewer.parse.warning.object.requireswidth", "Advertencia: la etiqueta <object> requiere el atributo width." }, { "appletviewer.parse.warning.embed.requirescode", "Advertencia: la etiqueta <embed> requiere el atributo code." }, { "appletviewer.parse.warning.embed.requiresheight", "Advertencia: la etiqueta <embed> requiere el atributo height." }, { "appletviewer.parse.warning.embed.requireswidth", "Advertencia: la etiqueta <embed> requiere el atributo width." }, { "appletviewer.parse.warning.appnotLongersupported", "Advertencia: la etiqueta <app> ya no está soportada, utilice <applet> en su lugar:" }, { "appletviewer.usage", "Sintaxis: appletviewer <opciones> url(s)\n\ndonde <opciones> incluye:\n  -debug                  Iniciar el visor de applet en el depurador Java\n  -encoding <codificación>    Especificar la codificación de caracteres utilizada por los archivos HTML\n  -J<indicador de tiempo de ejecución>        Transferir argumento al intérprete de Java\n\nLa opción -J es no estándar y está sujeta a cambios sin previo aviso." }, { "appletviewer.main.err.unsupportedopt", "Opción no soportada: {0}" }, { "appletviewer.main.err.unrecognizedarg", "Argumento no reconocido: {0}" }, { "appletviewer.main.err.dupoption", "Uso duplicado de la opción: {0}" }, { "appletviewer.main.err.inputfile", "No se ha especificado ningún archivo de entrada." }, { "appletviewer.main.err.badurl", "URL Errónea: {0} ( {1} )" }, { "appletviewer.main.err.io", "Excepción de E/S durante la lectura: {0}" }, { "appletviewer.main.err.readablefile", "Asegúrese de que {0} es un archivo y que se puede leer." }, { "appletviewer.main.err.correcturl", "¿Es {0} la URL correcta?" }, { "appletviewer.main.prop.store", "Propiedades Específicas del Usuario para AppletViewer" }, { "appletviewer.main.err.prop.cantread", "No se puede leer el archivo de propiedades del usuario: {0}" }, { "appletviewer.main.err.prop.cantsave", "No se puede guardar el archivo de propiedades del usuario: {0}" }, { "appletviewer.main.warn.nosecmgr", "Advertencia: desactivando seguridad." }, { "appletviewer.main.debug.cantfinddebug", "No se ha encontrado el depurador." }, { "appletviewer.main.debug.cantfindmain", "No se ha encontrado el método principal en el depurador." }, { "appletviewer.main.debug.exceptionindebug", "Excepción en el depurador." }, { "appletviewer.main.debug.cantaccess", "No se puede acceder al depurador." }, { "appletviewer.main.nosecmgr", "Advertencia: no se ha instalado SecurityManager." }, { "appletviewer.main.warning", "Advertencia: no se ha iniciado ningún applet. Asegúrese de que la entrada contiene una etiqueta <applet>." }, { "appletviewer.main.warn.prop.overwrite", "Advertencia: se sobrescribirá temporalmente la propiedad del sistema cuando lo solicite el usuario: clave: {0} valor anterior: {1} nuevo valor: {2}" }, { "appletviewer.main.warn.cantreadprops", "Advertencia: no se puede leer el archivo de propiedades de AppletViewer: {0}. Utilizando valores por defecto." }, { "appletioexception.loadclass.throw.interrupted", "carga de clase interrumpida: {0}" }, { "appletioexception.loadclass.throw.notloaded", "clase no cargada: {0}" }, { "appletclassloader.loadcode.verbose", "Abriendo flujo a: {0} para obtener {1}" }, { "appletclassloader.filenotfound", "No se ha encontrado el archivo al buscar: {0}" }, { "appletclassloader.fileformat", "Excepción de formato de archivo al cargar: {0}" }, { "appletclassloader.fileioexception", "Excepción de E/S al cargar: {0}" }, { "appletclassloader.fileexception", "Excepción de {0} al cargar: {1}" }, { "appletclassloader.filedeath", "{0} interrumpido al cargar: {1}" }, { "appletclassloader.fileerror", "error de {0} al cargar: {1}" }, { "appletclassloader.findclass.verbose.openstream", "Abriendo flujo a: {0} para obtener {1}" }, { "appletclassloader.getresource.verbose.forname", "AppletClassLoader.getResource para nombre: {0}" }, { "appletclassloader.getresource.verbose.found", "Recurso encontrado: {0} como un recurso de sistema" }, { "appletclassloader.getresourceasstream.verbose", "Recurso encontrado: {0} como un recurso de sistema" }, { "appletpanel.runloader.err", "Parámetro de código u objeto." }, { "appletpanel.runloader.exception", "excepción al deserializar {0}" }, { "appletpanel.destroyed", "Applet destruido." }, { "appletpanel.loaded", "Applet cargado." }, { "appletpanel.started", "Applet iniciado." }, { "appletpanel.inited", "Applet inicializado." }, { "appletpanel.stopped", "Applet parado." }, { "appletpanel.disposed", "Applet desechado." }, { "appletpanel.nocode", "Falta el parámetro CODE en la etiqueta APPLET." }, { "appletpanel.notfound", "cargar: clase {0} no encontrada." }, { "appletpanel.nocreate", "cargar: {0} no se puede instanciar." }, { "appletpanel.noconstruct", "cargar: {0} no es público o no tiene un constructor público." }, { "appletpanel.death", "interrumpido" }, { "appletpanel.exception", "excepción: {0}." }, { "appletpanel.exception2", "excepción: {0}: {1}." }, { "appletpanel.error", "error: {0}." }, { "appletpanel.error2", "error: {0}: {1}." }, { "appletpanel.notloaded", "Iniciación: applet no cargado." }, { "appletpanel.notinited", "Iniciar: applet no inicializado." }, { "appletpanel.notstarted", "Parar: applet no iniciado." }, { "appletpanel.notstopped", "Destruir: applet no parado." }, { "appletpanel.notdestroyed", "Desechar: applet no destruido." }, { "appletpanel.notdisposed", "Cargar: applet no desechado." }, { "appletpanel.bail", "Interrumpido: rescatando." }, { "appletpanel.filenotfound", "No se ha encontrado el archivo al buscar: {0}" }, { "appletpanel.fileformat", "Excepción de formato de archivo al cargar: {0}" }, { "appletpanel.fileioexception", "Excepción de E/S al cargar: {0}" }, { "appletpanel.fileexception", "Excepción de {0} al cargar: {1}" }, { "appletpanel.filedeath", "{0} interrumpido al cargar: {1}" }, { "appletpanel.fileerror", "error de {0} al cargar: {1}" }, { "appletpanel.badattribute.exception", "Análisis HTML: valor incorrecto para el atributo width/height." }, { "appletillegalargumentexception.objectinputstream", "AppletObjectInputStream requiere un cargador no nulo" }, { "appletprops.title", "Propiedades de AppletViewer" }, { "appletprops.label.http.server", "Servidor Proxy HTTP:" }, { "appletprops.label.http.proxy", "Puerto Proxy HTTP:" }, { "appletprops.label.network", "Acceso de Red:" }, { "appletprops.choice.network.item.none", "Ninguno" }, { "appletprops.choice.network.item.applethost", "Host del Applet" }, { "appletprops.choice.network.item.unrestricted", "No Restringido" }, { "appletprops.label.class", "Acceso de Clase:" }, { "appletprops.choice.class.item.restricted", "Restringido" }, { "appletprops.choice.class.item.unrestricted", "No Restringido" }, { "appletprops.label.unsignedapplet", "Permitir Applets no Firmados:" }, { "appletprops.choice.unsignedapplet.no", "No" }, { "appletprops.choice.unsignedapplet.yes", "Sí" }, { "appletprops.button.apply", "Aplicar" }, { "appletprops.button.cancel", "Cancelar" }, { "appletprops.button.reset", "Restablecer" }, { "appletprops.apply.exception", "Fallo al guardar las propiedades: {0}" }, { "appletprops.title.invalidproxy", "Entrada no Válida" }, { "appletprops.label.invalidproxy", "El puerto proxy debe ser un valor entero positivo." }, { "appletprops.button.ok", "Aceptar" }, { "appletprops.prop.store", "Propiedades específicas del usuario para AppletViewer" }, { "appletsecurityexception.checkcreateclassloader", "Excepción de Seguridad: classloader" }, { "appletsecurityexception.checkaccess.thread", "Excepción de Seguridad: thread" }, { "appletsecurityexception.checkaccess.threadgroup", "Excepción de Seguridad: threadgroup: {0}" }, { "appletsecurityexception.checkexit", "Excepción de Seguridad: salir: {0}" }, { "appletsecurityexception.checkexec", "Excepción de Seguridad: ejecutar: {0}" }, { "appletsecurityexception.checklink", "Excepción de Seguridad: enlace: {0}" }, { "appletsecurityexception.checkpropsaccess", "Excepción de Seguridad: propiedades" }, { "appletsecurityexception.checkpropsaccess.key", "Excepción de Seguridad: acceso a propiedades {0}" }, { "appletsecurityexception.checkread.exception1", "Excepción de Seguridad: {0}, {1}" }, { "appletsecurityexception.checkread.exception2", "Excepción de Seguridad: file.read: {0}" }, { "appletsecurityexception.checkread", "Excepción de Seguridad: file.read: {0} == {1}" }, { "appletsecurityexception.checkwrite.exception", "Excepción de Seguridad: {0}, {1}" }, { "appletsecurityexception.checkwrite", "Excepción de Seguridad: file.write: {0} == {1}" }, { "appletsecurityexception.checkread.fd", "Excepción de Seguridad: fd.read" }, { "appletsecurityexception.checkwrite.fd", "Excepción de Seguridad: fd.write" }, { "appletsecurityexception.checklisten", "Excepción de Seguridad: socket.listen: {0}" }, { "appletsecurityexception.checkaccept", "Excepción de Seguridad: socket.accept: {0}:{1}" }, { "appletsecurityexception.checkconnect.networknone", "Excepción de Seguridad: socket.connect: {0}->{1}" }, { "appletsecurityexception.checkconnect.networkhost1", "Excepción de Seguridad: no se puede conectar a {0} con origen de {1}." }, { "appletsecurityexception.checkconnect.networkhost2", "Excepción de Seguridad: no se puede resolver la IP para el host {0} o para {1}. " }, { "appletsecurityexception.checkconnect.networkhost3", "Excepción de Seguridad: no se puede resolver la IP para el host {0}. Consulte la propiedad trustProxy." }, { "appletsecurityexception.checkconnect", "Excepción de Seguridad: conexión: {0}->{1}" }, { "appletsecurityexception.checkpackageaccess", "Excepción de Seguridad: no se puede acceder al paquete: {0}" }, { "appletsecurityexception.checkpackagedefinition", "Excepción de Seguridad: no se puede definir el paquete: {0}" }, { "appletsecurityexception.cannotsetfactory", "Excepción de Seguridad: no se puede definir el valor de fábrica" }, { "appletsecurityexception.checkmemberaccess", "Excepción de Seguridad: comprobar el acceso de miembro" }, { "appletsecurityexception.checkgetprintjob", "Excepción de Seguridad: getPrintJob" }, { "appletsecurityexception.checksystemclipboardaccess", "Excepción de Seguridad: getSystemClipboard" }, { "appletsecurityexception.checkawteventqueueaccess", "Excepción de Seguridad: getEventQueue" }, { "appletsecurityexception.checksecurityaccess", "Excepción de Seguridad: operación de seguridad: {0}" }, { "appletsecurityexception.getsecuritycontext.unknown", "tipo de cargador de clase desconocido. no se puede comprobar para getContext" }, { "appletsecurityexception.checkread.unknown", "tipo de cargador de clase desconocido. no se puede comprobar para lectura de comprobación {0}" }, { "appletsecurityexception.checkconnect.unknown", "tipo de cargador de clase desconocido. no se puede comprobar para conexión de comprobación" } };
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\applet\resources\MsgAppletViewer_es.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */