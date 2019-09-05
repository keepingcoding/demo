package com.example.demo.web.upload;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author zzs
 * @date 2019/9/3 0:01
 */
@Controller
public class CommonFileController {

    /**
     * 1.@RequestPart这个注解用在multipart/form-data表单提交请求的方法上。
     * 2.支持的请求方法的方式MultipartFile，属于Spring的MultipartResolver类。这个请求是通过http协议传输的。
     * 3.@RequestParam也同样支持multipart/form-data请求。
     * 4.他们最大的不同是，当请求方法的请求参数类型不再是String类型的时候。
     * 5.@RequestParam适用于name-valueString类型的请求域，@RequestPart适用于复杂的请求域（像JSON，XML）。
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        return copyFile(multipartFile);
    }

    @PostMapping("/uploadFiles")
    @ResponseBody
    public List<String> uploadFiles(@RequestParam("file") MultipartFile[] multipartFiles) {
        List<String> resultList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String s = copyFile(multipartFile);
            resultList.add(s);
        }
        return resultList;
    }

    @PostMapping("/uploadFileAndData")
    @ResponseBody
    public String uploadFileWithFormData(@RequestPart("formData") FormData formData, @RequestPart("file") MultipartFile multipartFile) {
        System.err.println(formData);
        return copyFile(multipartFile);
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/postDownload")
    @ResponseBody
    public void downloadFileByPost(@RequestBody String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(fileName)) {
            return;
        }

        String parentPath = getParentPath();
        File file = new File(parentPath, fileName);
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));// 设置文件名

            byte[] buffer = new byte[1024];
            bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream();
            int i = -1;
            while ((i = bis.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
            }
        }
    }

    @GetMapping("/getDownload")
    public void downloadFileByGet(@RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(fileName)) {
            return;
        }

        String parentPath = getParentPath();
        File file = new File(parentPath, fileName);
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            response.setContentType("application/octet-stream");
            //response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));// 设置文件名

            byte[] buffer = new byte[1024];
            bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream();
            int i = -1;
            while ((i = bis.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
            }
        }
    }

    @GetMapping("/download2")
    public ResponseEntity<Object> downloadFile2(@RequestParam String fileName) {
        String parentPath = getParentPath();
        File file = new File(parentPath, fileName);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment;filename=\"%s", fileName));
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

        return responseEntity;
    }


    /**
     * 保存文件
     *
     * @param file
     * @return 保存的文件名
     */
    private String copyFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String s = UUID.randomUUID().toString();
        //s = s.replaceAll("-", "");
        String newName = s + suffix;
        String parentPath = getParentPath();

        File dest = new File(parentPath, newName);
        try {
            Files.createParentDirs(dest);
            file.transferTo(dest);
            return newName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getParentPath() {
        String parentPath = "";
        //默认存储classpath目录，可修改为可配置
        if (StringUtils.isBlank(baseStoreDir)) {
            String rootUri = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            parentPath = rootUri + File.separator + "upload" + File.separator + "base";
        } else {
            parentPath = baseStoreDir;
        }
        return parentPath;
    }

    @Value("${com.example.demo.baseStoreDir:''}")
    private String baseStoreDir;
}
