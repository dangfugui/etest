package com.dang.etest.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

   public static File mkdir(String path){
       File file = new File(path);
       if(file.exists()){
           return file;
       }else {
           if(!file.getParentFile().exists()) {
               mkdir(file.getParent());
           }
           file.mkdir();
       }
       return file;
   }

   public static File createFile(File file) throws IOException {
       if(file.exists()){
           return file;
       }else {
           if(!file.getParentFile().exists()){
               mkdir(file.getParent());
           }
           file.createNewFile();
       }
       return file;
   }

   public static File createFile(String path) throws IOException {
       File file = new File(path);
       if(file.exists()){
           return file;
       }else {
         file = createFile(file);
       }
       return file;
   }


   public static String read(String path) throws IOException {
       BufferedReader reader = new BufferedReader(new FileReader(path));
       String line = "";
       StringBuffer stringBuffe = new StringBuffer();
       while ((line = reader.readLine()) != null) {
           stringBuffe.append(line+"\n");
       }
       return stringBuffe.toString();
   }

    public static List<String> readAsList(String path) throws IOException {
        return readAsList(new File(path));
    }
    public static List<String> readAsList(File file) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        StringBuffer stringBuffe = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            list.add(line.trim());
        }
        return list;
    }



    public static File write(String path, InputStream inputStream) throws IOException {
       File file = new File(path);
       if(!file.exists()){
           createFile(file);
       }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] tmp = new byte[1024];
            int len;
            while ((len = inputStream.read(tmp)) != -1) {
                outputStream.write(tmp,0,len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                outputStream.close();
            }
        }
        inputStream.close();
        return file;
    }
    public static File write(String path, String ...lines) throws IOException {
        File file = new File(path);
        if (!file.exists()){
            createFile(file);
        }
        return write(file,false ,lines);
    }

    public static File write(File file, boolean append,  String ...lines) throws IOException {
       FileWriter fileWriter = new FileWriter(file, append);
       BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
       for(String str: lines){
           bufferedWriter.write(str);
           bufferedWriter.newLine();
       }
       bufferedWriter.flush();
       bufferedWriter.close();
       fileWriter.close();
       return file;
    }

    public static boolean delete(String path) {
       File file = new File(path);
       if(file.exists()){
           return file.delete();
       }
        return false;
    }

    public static File merge(List<String> fileList , String outFilePath){
       File outFile = new File(outFilePath);
        try {
            OutputStream output = new FileOutputStream(outFile);
            for(String filePath : fileList){
                InputStream inputStream = new FileInputStream(filePath);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bytes))> 0){
                    output.write(bytes,0,len);
                }
                inputStream.close();
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }



    public static int copy(File file, String path) throws IOException {
        int sum = 0;
        if(!file.exists()){
            return -1;
        }
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                sum += copy(f,path+"/"+f.getName());
            }
        }else { // File
            File out = null;
            if(path.endsWith("/")||path.endsWith("\\")){
                out = new File(path + file.getName());
            }else {
                out = new File(path);
            }
            if(!out.exists()){
                createFile(out);
            }
            copyByChannel(file, out);
            sum ++;
        }
        return sum;
    }

    public static void copyByChannel(File source,File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            if(inputChannel!=null) {
                inputChannel.close();
            }
            if(outputChannel != null) {
                outputChannel.close();
            }
        }
    }

    public static void copyByJava(File source,File dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.flush();
            output.close();
        }
    }

    public static void copyByImage(File source,File dest) throws IOException {
        int length=2097152;
        FileInputStream in=new FileInputStream(source);
        RandomAccessFile out=new RandomAccessFile(dest,"rw");
        FileChannel inC=in.getChannel();
        MappedByteBuffer outC=null;
        MappedByteBuffer inbuffer=null;
        byte[] b=new byte[length];
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.force();
                out.getFD().sync();
                out.close();
                return;
            }
            if((inC.size()-inC.position())<length){
                length=(int)(inC.size()-inC.position());
            }else{
                length=20971520;
            }
            b=new byte[length];
            inbuffer=inC.map(FileChannel.MapMode.READ_ONLY,inC.position(),length);
            inbuffer.load();
            inbuffer.get(b);
            outC=out.getChannel().map(FileChannel.MapMode.READ_WRITE,inC.position(),length);
            inC.position(b.length+inC.position());
            outC.put(b);
            outC.force();
        }
    }


    public static void forChannel(File f1, File f2) throws IOException{
        int length=32*1024;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        ByteBuffer b=null;
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return ;
            }
            if((inC.size()-inC.position())<length){
                length=(int)(inC.size()-inC.position());
            }else
                length=2097152;
            b=ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }
}
