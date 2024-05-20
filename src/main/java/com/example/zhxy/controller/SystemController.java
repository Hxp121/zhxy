package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.AdminService;
import com.example.zhxy.service.StudentService;
import com.example.zhxy.service.TeacherService;
import com.example.zhxy.uitl.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;


    @ApiOperation("生成验证码和其图片，并响应给浏览器")
    @RequestMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码写入session域
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @ApiOperation("用户名密码登录验证")
    @PostMapping("/login")
    public Result Login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        HttpSession session = request.getSession();
        String verifiCode = (String)session.getAttribute("verifiCode");
        //这个是传过来的验证码
        String loginFormVerifiCode = loginForm.getVerifiCode();

        if("".equals(verifiCode)){
            return Result.fail().message("验证码失效！");
        }

        if(!verifiCode.equalsIgnoreCase(loginFormVerifiCode)){
            return Result.fail().message("传过来的验证码和生成的验证码不一致");
        }


        Map<String, Object> map = new HashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    //这个Login方法是查用来询数据库，获得相关对象，然后再做校验
                    Admin admin = adminService.Login(loginForm);
                    if (admin!=null){
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(),1));

                    }else {
                        throw new RuntimeException("用户名或者密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    Student student = studentService.Login(loginForm);
                    if (student!=null){
                        map.put("token",JwtHelper.createToken(student.getId().longValue(),2));
                    }else {
                        throw new RuntimeException("用户名或者密码有误！");
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher = teacherService.Login(loginForm);
                    if (teacher!=null){
                        map.put("token",JwtHelper.createToken(teacher.getId().longValue(),2));
                    }else {
                        throw new RuntimeException("用户名或者密码有误！");
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }

        return Result.fail().message("用户名或者密码有误！");
    }


    @ApiOperation("将用户id和类型响应给浏览器")
    @GetMapping("/getInfo")
    public Result getUserInfoByToken(@RequestHeader String token,HttpServletRequest request){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);


        Map<Object, Object> map = new HashMap<>();
        switch (userType){
            case 1:
               Admin admin = adminService.getAdminById(userId.intValue());
               map.put("user",admin);
               map.put("userType",1);
               break;

            case 2:
               Student student = studentService.getStudentById(userId.intValue());
               map.put("user",student);
               map.put("userType",2);
               break;

            case 3:
               Teacher teacher = teacherService.getTeacherById(userId.intValue());
               map.put("user",teacher);
               map.put("userType",3);
               break;

        }

        return Result.ok(map);
    }



    @ApiOperation("上传头像")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
           @ApiParam("保存了头像的数据，包括名字") @RequestPart MultipartFile multipartFile
    ){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String  newFileName = uuid + originalFilename.substring(i);

        String upLoadPath = "D:\\JavaCode\\zhxy\\target\\classes\\public\\upload\\".concat(newFileName);

        try {
            multipartFile.transferTo(new File(upLoadPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String headerImg = "upload/" + newFileName;

        return Result.ok(headerImg);
    }



    @ApiOperation("对用户的密码进行修改")
    @RequestMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token里面包含用户数据数据") @RequestHeader("token") String token,
            @ApiParam("通过路径参数传递旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("通过路径参数传递新密码") @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.fail().message("token过期");
        }

        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);

         oldPwd = MD5.encrypt(oldPwd);
         newPwd = MD5.encrypt(newPwd);

        switch(userType){
            case 1:
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
                Admin adminServiceOne = adminService.getOne(adminQueryWrapper);

                if(adminServiceOne!=null){
                    adminServiceOne.setPassword(newPwd);
                    adminService.saveOrUpdate(adminServiceOne);
                }else {
                    return Result.fail().message("原密码错误");
                }
                break;

            case 2:
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
                Student studentServiceOne = studentService.getOne(studentQueryWrapper);

                if(studentServiceOne!=null){
                    studentServiceOne.setPassword(newPwd);
                    studentService.saveOrUpdate(studentServiceOne);
                }else {
                    return Result.fail().message("原密码错误");
                }
                break;

            case 3:
                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
                Teacher teacherServiceOne = teacherService.getOne(teacherQueryWrapper);

                if (teacherServiceOne != null) {
                    teacherServiceOne.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacherServiceOne);
                }
                break;
        }
        return Result.ok();
    }
}
