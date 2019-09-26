## 一、Mock测试介绍

- **定义**
  在单元测试过程中，对于某些不容易构造或者不容易获取的对象，用一个虚拟对象来创建以便测试的方法。

- **为什么使用mock测试**

  ​		对模块进行集成测试时，希望能够通过输入URL对Controller进行测试，如果通过启动服务器，建立http client进行测试（或者是通过Postman等工具），这样会使得测试变得很麻烦，比如，启动速度慢，测试验证不方便，依赖网络环境等，所以为了可以对Controller进行测试，我们引入了MockMVC。
  ​        MockMvc实现了对Http请求的模拟，能够直接使用网络的形式，转换到Controller的调用，这样可以使得测试速度快、不依赖网络环境，而且提供了一套验证的工具，这样可以使得请求的验证统一而且很方便。



## 二、使用方式

使用起来很简单，直接上代码



1.controller控制器：

```java
import com.example.demo.web.contract.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
    
    //put与delete方法与post同理，这里就不写了
    //…………

    @PostMapping(value = "/file")
    public BaseResponse file(@RequestPart("file") MultipartFile file) {
        System.err.println(file.getOriginalFilename());
        return new BaseResponse();
    }
}
```



2.测试代码：

```java
import com.alibaba.fastjson.JSON;
import com.example.demo.web.MainController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MvcTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private MainController mainController;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();

        //通过参数指定一组控制器，这样就不需要从上下文获取了
        //mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    /** get **/
    @Test
    public void getTest() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/main/get")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("pageNum", "1");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print()) //打印输出发出请求的详细信息
                .andExpect(MockMvcResultMatchers.status().isOk()) //对返回值进行断言
                .andReturn();

        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    /** post **/
    @Test
    public void postTest() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/main/post")
                //.put("/main/put")
                //.delete("/main/delete")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(map));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.err.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void fileTest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/main/file")
                .file(new MockMultipartFile("file", "test.txt",
                        "multipart/form-data",
                        "hello upload".getBytes("UTF-8")))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8);


        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.err.println(mvcResult.getResponse().getContentAsString());
    }
}
```



