package com.example.demo.web;

import com.example.demo.web.contract.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author zzs
 * @date 2019/9/26 14:30
 */
@Slf4j
@RestController
@RequestMapping("/main")
public class MainController {

    @GetMapping("/get")
    public BaseResponse getReq(@RequestParam Map<String, Object> map) {
        System.err.println(map);
        return new BaseResponse();
    }

    @PostMapping("/post")
    public BaseResponse postReq(@RequestBody Map<String, Object> map) {
        System.err.println(map);
        return new BaseResponse();
    }

    @PostMapping(value = "/file")
    public BaseResponse file(@RequestPart("file") MultipartFile file) {
        System.err.println(file.getOriginalFilename());
        return new BaseResponse();
    }
}
