package com.xiao.filesystem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class FileSystemController {

    @GetMapping("/createdir")
    public void createDirectory() {
        //在当前项目目录下创建一个 uploads 目录
        String dirName = "uploads";
        try {
            //即使目录已经存在，也不会报错
            Files.createDirectories(Paths.get(dirName));
        }catch (Exception e) {

        }

    }

    @GetMapping("/createfile")
    public String createFile() {
        //在项目目录下创建一个文件
        try {
            //也可以先创建一个目录，然后在该目录下创建文件.例如在项目目录下创建一个log目录：Files.createDirectories(Paths.get("log"));

            String fileName = "test1.txt";
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("this is file contents");
            fileWriter.close();
        }catch (Exception e) {

        }

        return "success";
    }
}
