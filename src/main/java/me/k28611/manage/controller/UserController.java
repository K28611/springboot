package me.k28611.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.google.common.collect.Maps;
import me.k28611.manage.annotation.JwtIgnore;
import me.k28611.manage.dao.HrMemberMapper;
import me.k28611.manage.entity.Audience;
import me.k28611.manage.enums.ResultCode;
import me.k28611.manage.model.po.HrMember;
import me.k28611.manage.model.po.TUser;
import me.k28611.manage.service.EmailService;
import me.k28611.manage.service.FileService;
import me.k28611.manage.service.UserService;
import me.k28611.manage.utils.JsonResult;
import me.k28611.manage.utils.JwtTokenUtils;
import me.k28611.manage.utils.RedisUtil;
import me.k28611.manage.utils.VerCodeGenerateUtils;
import org.apache.catalina.Session;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userApi")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private Producer captchaProducer;
    @Autowired
    Audience audience;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    FileService fileService;
    @Autowired
    EmailService emailService;
    @Autowired
    RedisUtil redisUtil;



    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @JwtIgnore
    @GetMapping("/getVerImg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = UUID.randomUUID().toString();
        System.out.println(s);
        //返回类型
        response.setContentType("image/jpeg");
        //创建验证码文本
        String capText = captchaProducer.createText();
        //讲验证码文本设置到session
        request.getSession().setAttribute("captcha", capText);
        //创建验证码图片
        BufferedImage bi = captchaProducer.createImage(capText);
        //获取相应输出流
        ServletOutputStream out = response.getOutputStream();
        //写入流
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
    @PostMapping("/login")
    @ResponseBody
    @JwtIgnore
    public JsonResult login(@RequestBody Map<String, String> param, HttpServletRequest request) {


        Integer account = Integer.parseInt(param.get("account"));
        String passWord = param.get("password");
        HrMember user1 = userService.findUsersByAccount(account);
        System.out.println(user1);
        try {
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(passWord)) {
                //参数为空
                return new JsonResult(ResultCode.PARAM_IS_BLANK, null);
            } else {
                HrMember user = userService.findUsersByAccount(account);
                if (user == null) {
                    return new JsonResult(ResultCode.USER_LOGIN_ERROR, null);
                } else {
                    if (bCryptPasswordEncoder.matches(passWord, user.getPassword())) {
                        final String token = JwtTokenUtils.generateToken(user, audience);
                        return new JsonResult(ResultCode.SUCCESS, token);
                    } else {
                        return new JsonResult(ResultCode.USER_LOGIN_ERROR, null);
                    }
                }
            }
        } catch (Exception e) {
            return new JsonResult(e.getMessage());
        }
    }

    @JwtIgnore
    @RequestMapping("/test")
    public File test() {
        try {
            File file = fileService.downloadFile("/tmp/a.txt");
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @PostMapping("/email")
    public String sendEmail(@RequestParam("emailAddress") String emailAddress) {
        try {
            emailService.sendEmailVerCode(emailAddress, VerCodeGenerateUtils.generateVerCode(4));
            return "success";
        } catch (MailSendException e) {
            return "fail";
        }
    }

    @JwtIgnore
    @PostMapping("/getBackPssword")
    public JsonResult getBackPassword() {
        Integer account = 10001;
        String  recCode = ""; //前端发来的验证码
        //if session...
        String verCode ="";   //取自Session
        String password = "123456";
        boolean flag =  recCode == verCode ? true: false; //验证和session中的验证码是否一致
        if (flag){
            HrMember user = userService.findUsersByAccount(account);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            int i = userService.updateByPrimaryKey(user);
            ResultCode result;
            result = i == 0 ? ResultCode.SUCCESS : ResultCode.DATA_IS_WRONG;
            return new JsonResult(result, null);
        }
        else{
            return new JsonResult(ResultCode.VERCODE_IS_WRONG,null);
        }

    }
    @JwtIgnore
    @GetMapping("/getBackPsswordVercode")
    public JsonResult getPasswordVercode(@RequestParam Integer Account){
        System.out.println(Account);
        Integer account = Account; //找回的账户
        HrMember user = userService.findUsersByAccount(account);
        if (user != null) {
            String userEmailAddress = user.getEmail();
            String verCode = VerCodeGenerateUtils.generateVerCode(4); //生成验证码
            try {
                emailService.sendEmailVerCode(userEmailAddress,verCode);
            } catch (MailSendException e) {
                logger.error(e.getMessage());
                return new JsonResult(ResultCode.EMAIL_SEND_ERROR,null);
            }
            return new JsonResult(ResultCode.SUCCESS,null);
        }
        else{
            return new JsonResult(ResultCode.RESULT_DATA_NONE,null);
        }
    }

    @JwtIgnore
    @PostMapping("/getEmailVercode")
    public JsonResult getEmailVercode(@RequestBody Map<String, String> param, HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String ip = param.get("ip");
        String userEmailAddress = param.get("userEmailAddress");
        String verID =ip + new Date().toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(verID.getBytes("utf8"));
        verID = String.format("%064x", new BigInteger(1, digest.digest()));
        System.out.println(verID);
        String verCode = VerCodeGenerateUtils.generateVerCode(4);
        redisUtil.set("Ver"+verID,verCode,300);
        System.out.println(redisUtil.get("Ver"+verID));
        try {
            emailService.sendEmailVerCode(userEmailAddress,verCode);
        } catch (MailSendException e) {
            logger.error(e.getMessage());
            return new JsonResult(ResultCode.EMAIL_SEND_ERROR,null);
        }
        return new JsonResult(ResultCode.SUCCESS,verID);
    }
    @JwtIgnore
    @PostMapping("/register")
    public JsonResult register(@RequestParam("headIMG") MultipartFile file,@RequestParam("user") String user) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(user);
        String verID = "Ver"+ jsonObject.get("verID").toString();
        String verCode = jsonObject.get("verCode").toString();
        if(!redisUtil.hasKey(verID)){
            return  new JsonResult(ResultCode.VERCODE_IS_EXPIRED,null);
        }
        if(!redisUtil.get(verID).equals(verCode)){
            return new JsonResult(ResultCode.VERCODE_IS_WRONG,null);
        }
        String userName = jsonObject.get("userName").toString();
        String passWord = encoder().encode(jsonObject.get("passWord").toString()) ;
        String email = jsonObject.get("email").toString();
        int index = file.getOriginalFilename().lastIndexOf(".");
        String type =file.getOriginalFilename().substring(index+1);
        String fileName = UUID.randomUUID().toString()+"."+type;
        File head = File.createTempFile(fileName,type);
        file.transferTo(head);
        boolean flag = fileService.uploadFile("img/head/" +fileName+"", head);
        if(flag){
            HrMember newUser = new HrMember();
            int AccountNo = GenerateAccount();
            newUser.setEmail(email);
            newUser.setHeadimgurl(fileName);
            newUser.setAccountno(AccountNo);
            newUser.setUsername(userName);
            newUser.setPassword(passWord);
            userService.addUser(newUser);
            return new JsonResult(ResultCode.SUCCESS,null);
        }
        return new JsonResult(ResultCode.FAIL,null);

    }
    public byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    public File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    private int GenerateAccount(){
        Random random = new Random();
        int AccountNo =  random.nextInt(89999)+10001;
        if(userService.findUsersByAccount(AccountNo)==null){
            return AccountNo;
        }
        else{
           return GenerateAccount();
        }
    }
}
