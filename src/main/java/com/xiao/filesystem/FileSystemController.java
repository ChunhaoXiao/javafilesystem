package com.xiao.filesystem;

import ch.qos.logback.core.util.FileUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RestController
public class FileSystemController {

    //创建目录
    @GetMapping("/createdir")
    public void createDirectory() {
        //在当前项目目录下创建一个 uploads 目录
        String dirName = "uploads";
        try {
            //即使目录已经存在，也不会报错
            Files.createDirectories(Paths.get(dirName));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //创建文件
    @GetMapping("/createfile")
    public String createFile() {
        //在项目目录下创建一个文件
        try {
            //也可以先创建一个目录，然后在该目录下创建文件.例如在项目目录下创建一个log目录：Files.createDirectories(Paths.get("log"));

            String fileName = "test1.txt";
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("1234444444777888");
            fileWriter.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "success";
    }

    //创建临时文件
    @GetMapping("/temp")
    public String createTempFile() {
        //获取临时文件的路径，Windows： %USER%\AppData\Local\Temp； Linux: /tmp
        String tmpdir = System.getProperty("java.io.tmpdir");
        try {
            //创建一个临时文件
            Path temp = Files.createTempFile("hello",".txt");
            //在临时文件中写入一些内容
            FileWriter fileWriter = new FileWriter(temp.toFile());//temp.toString()
            fileWriter.write("hhhhhhhhhh");
            fileWriter.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "success";
    }

    //下载文件
    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        String tmpdir = System.getProperty("java.io.tmpdir");
        Path temp = null;
        try {
            //创建一个临时文件
            temp = Files.createTempFile("hello",".txt");
            //在临时文件中写入一些内容
            FileWriter fileWriter = new FileWriter(temp.toFile());//temp.toString()
            fileWriter.write("嘻嘻嘻嘻嘻嘻嘻嘻嘻嘻嘻嘻");
            fileWriter.close();
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(temp.toFile()));
            HttpHeaders headers = new HttpHeaders();
            Random random = new Random();
            int num = random.nextInt(5000);
            String fname = String.valueOf(num);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fname+".txt\"");
            return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //上传文件
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String folderPath = "uploads";
            File dir = new File(folderPath);
            if(!dir.exists()) {
                dir.mkdir();
            }
            byte[] bytes = new byte[0];
            bytes = file.getBytes();
            Files.write(Paths.get(folderPath+file.getOriginalFilename()), bytes);
            return  ResponseEntity.ok().body("upload successful");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("upload failed");
        }
    }
}
