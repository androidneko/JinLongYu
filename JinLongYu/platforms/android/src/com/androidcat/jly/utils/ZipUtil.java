package com.androidcat.jly.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2018/4/25.
 */

public class ZipUtil {
  private static final String TAG = "ZipUtil";
  public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
    ZipInputStream inZip = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileString)));
    ZipEntry zipEntry;
    String szName = "";
    while ((zipEntry = inZip.getNextEntry()) != null) {
      szName = zipEntry.getName();
      android.util.Log.d(TAG, "name = " + szName);
      if (zipEntry.isDirectory()) {
        // get the folder name of the widget
        szName = szName.substring(0, szName.length() - 1);
        File folder = new File(outPathString + File.separator + szName);
        folder.mkdirs();
      } else {
        File file = new File(outPathString + File.separator + szName);
        file.createNewFile();
        // get the output stream of the file
        FileOutputStream out = new FileOutputStream(file);
        int len;
        byte[] buffer = new byte[1024];
        // read (len) bytes into buffer
        while ((len = inZip.read(buffer)) != -1) {
          // write (len) byte from buffer at the position 0
          out.write(buffer, 0, len);
          out.flush();
        }
        out.close();
      }
    }
    inZip.close();
  }
}
