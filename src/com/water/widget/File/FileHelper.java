/**
 * @Title: FileHelper.java
 * @Package: com.water.widget.File
 * @Description: TODO

 * @author: zhouli
 * @date: 2012-6-18
 * @version V1.0
 */

package com.water.widget.File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName: FileHelper
 * @Description: TODO
 * @author: zhouli
 * @date: 2012-6-18
 * 
 */

public final class FileHelper {

  public static void copyFile(String oldPath, String newPath) {
    try {
      int bytesum = 0;
      int byteread = 0;
      File oldfile = new File(oldPath);
      if (oldfile.exists()) { // 文件存在时
        InputStream inStream = new FileInputStream(oldPath); // 读入原文件
        FileOutputStream fs = new FileOutputStream(newPath);
        byte[] buffer = new byte[4096];
        int length;
        while ((byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread; // 字节数 文件大小
          System.out.println(bytesum);
          fs.write(buffer, 0, byteread);
        }
        inStream.close();
      }
    } catch (Exception e) {
      System.out.println("复制单个文件操作出错");
      e.printStackTrace();

    }

  }

  public static List<String> getFileLines(String filePath) {
    List<String> values = new ArrayList<String>();

    FileInputStream fInputStream = null;
    BufferedReader fReader = null;
    try {
      fInputStream = new FileInputStream(filePath);
      fReader = new BufferedReader(new InputStreamReader(fInputStream));
      String str = null;
      while ((str = fReader.readLine()) != null) {
        values.add(str);
      }
    } catch (FileNotFoundException e) {
      LOG.error("Find file error", e);
    } catch (IOException e) {
      LOG.error("Read file error", e);
    } finally {
      if (fReader != null) {
        try {
          fReader.close();
        } catch (IOException e) {
          LOG.error("Close read error", e);
        } // try-catch
      } // if
    }

    return values;
  }

  public static void dirCopy(String src, String dst, List<String> excludes) {
    // copy and delete
    File dstFile = new File(dst);
    File srcFile = new File(src);
    if (!srcFile.exists()) {
      LOG.error("Source Dir not exists:" + src);
      return;
    }
    if (!dstFile.exists()) {
      dstFile.mkdirs();
    } else if (dstFile.isFile()) {
      LOG.error("Path of dst file is a file:" + dst);
      return;
    }

    // first copy
    try {
      FileUtils.copyDirectory(srcFile, dstFile);
    } catch (IOException e) {
      LOG.error("Copy Error:", e);
    }
    // delete excludes
    deleteSubFiles(dstFile, excludes);
  }

  public static void deleteSubFiles(File dir, List<String> excludes) {
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        if (isExcludeFile(file.getName(), excludes)) {
          file.delete();
          System.out.println("Delete file:" + file.getAbsolutePath());
        }
      } else {
        if (isExcludeFile(file.getName(), excludes)) {
          deleteDirectory(file);
          System.out.println("Delete dir:" + file.getAbsolutePath());
        } else {
          deleteSubFiles(file, excludes);
        }
      }
    }
  }

  public static void deleteDirectory(File dirFile) {
    if (!dirFile.exists() || !dirFile.isDirectory()) {
      return;
    }

    File[] files = dirFile.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isFile()) {
        files[i].delete();
      } else {
        deleteDirectory(files[i]);
      }
    }

    dirFile.delete();
  }

  private static boolean isExcludeFile(String name, List<String> excludes) {
    boolean include = false;
    if (excludes != null) {
      for (String filter : excludes) {
        if (name.contains(filter)) {
          include = true;
          break;
        }
      }
    }

    return include;
  }

  public static void listDirFiles(File dirFile, List<String> subFiles) {
    if (!dirFile.exists() || !dirFile.isDirectory()) {
      return;
    }

    File[] files = dirFile.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isFile()) {
        subFiles.add(files[i].getAbsolutePath());
      } else {
        listDirFiles(files[i], subFiles);
      }
    }

  }

  private static Logger LOG = Logger.getLogger(FileHelper.class);
}
