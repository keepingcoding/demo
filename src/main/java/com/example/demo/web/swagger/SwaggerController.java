package com.example.demo.web.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"测试"})
@RequestMapping("/swagger")
@RestController
public class SwaggerController {

    @ApiOperation(value = "获取用户列表", notes = "")
    @GetMapping("/getAll")
    public List<User> getUserList() {
        return new ArrayList<>();
    }

    /**
     * paramType：参数放在哪个地方
     * · header --> 请求参数的获取：@RequestHeader
     * · query --> 请求参数的获取：@RequestParam
     * · path（用于restful接口）--> 请求参数的获取：@PathVariable
     * · body
     * · form
     */
    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User", paramType = "body")
    @PostMapping("/add")
    public String add(@RequestBody User user) {
        System.err.println(user);
        return "success";
    }

    @ApiOperation(value = "获取用户信息", notes = "根据id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path", example = "1")
    @GetMapping("/detail/{id}")
    public User detail(@PathVariable Long id) {
        System.err.println(id);
        return new User();
    }

    @ApiOperation(value = "更新用户信息", notes = "根据id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path", example = "1"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User", paramType = "body")
    })
    @PutMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @RequestBody User user) {
        System.err.println(id);
        System.err.println(user);
        return "success";
    }

    @ApiOperation(value = "删除用户", notes = "根据id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path", example = "1")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        System.err.println(id);
        return "success";
    }

    @ApiIgnore
    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
