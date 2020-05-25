package me.k28611.manage.controller;

import me.k28611.manage.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
@RestController("/file")
public class FileController {
    @RequestMapping("/upload")
    public JsonResult handleFileUpload(@RequestParam("file")MultipartFile file)  {
        if (!file.isEmpty()){
            try{
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream
                        (new File(file.getOriginalFilename())));
                outputStream.write(file.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        else {
            return new JsonResult("success");
        }
    }
}
