package com.example.demo.mock;

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

/**
 * @author zzs
 * @date 2019/9/26 14:10
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MvcTest {

    private MockMvc mockMvc;

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
