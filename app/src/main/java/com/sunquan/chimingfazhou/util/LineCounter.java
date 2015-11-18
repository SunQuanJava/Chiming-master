package com.sunquan.chimingfazhou.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
 
/**
 *
 *  计算源代码（src）行数
 *  将src下所有文件组装成list,再筛选出java文件，对文件进行遍历读取
 */
public final class LineCounter {
    List<File> list = new ArrayList<>();
    int linenumber = 0;
     
    FileReader fr = null;
    BufferedReader br = null;
 
    public void counter(String path) {
        System.out.println(path);
        File file = new File(path);
        File[] files = file.listFiles();
        addFile(files);
        isDirectory(files);
        readLinePerFile();
        System.out.println("Totle:" + linenumber + "行");
    }
 
    // 判断是否是目录
    public void isDirectory(File[] files) {
        for (File s : files) {
            if (s.isDirectory()) {
                File file[] = s.listFiles();
                addFile(file);
                isDirectory(file);
            }
        }
    }
 
    //将src下所有文件组织成list
    public void addFile(File file[]) {
        for (File aFile : file) {
            if (!aFile.getName().endsWith(".java")) {
                continue;
            }
            list.add(aFile);
        }
    }
     
    //读取行
    public void readLinePerFile() {
        try {
            for (File s : list) {
                int yuan = linenumber;
                if (s.isDirectory()) {
                    continue;
                }
                fr = new FileReader(s);
                br = new BufferedReader(fr);
                while ((br.readLine()) != null) {
                    linenumber++;
                }
                System.out.print(s.getName());
                System.out.println("\t\t有" + (linenumber - yuan) + "行");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ignored) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

     
    public static void main(String args[]) {
        LineCounter lc = new LineCounter();
        String path = "E:\\download\\ChiMingFaZhou";     //这里传入你的项目名称
        lc.counter(path);
    }
}