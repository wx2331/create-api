package com.sun.tools.javac.nio;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

public interface PathFileManager extends JavaFileManager {
  FileSystem getDefaultFileSystem();

  void setDefaultFileSystem(FileSystem paramFileSystem);

  Iterable<? extends JavaFileObject> getJavaFileObjectsFromPaths(Iterable<? extends Path> paramIterable);

  Iterable<? extends JavaFileObject> getJavaFileObjects(Path... paramVarArgs);

  Path getPath(FileObject paramFileObject);

  Iterable<? extends Path> getLocation(Location paramLocation);

  void setLocation(Location paramLocation, Iterable<? extends Path> paramIterable) throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\nio\PathFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
