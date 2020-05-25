package me.k28611.manage.utils;
import io.jsonwebtoken.*;
import me.k28611.manage.entity.Audience;
import me.k28611.manage.model.po.HrMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
/**
 * @author K28611
 * @date 2020/4/29 22:46
 */
public class JwtTokenUtils {
    public static String AUTH_HEADER_KEY = "Authorization";
    public static String TOKEN_PREFIX = "Bearer ";
    public static Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    public static String generateToken(HrMember user, Audience audience){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime()+audience.getJwtProperty() .getExpireSecond()*1000);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .claim("role",user.getAccountno())
                .setSubject(user+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,audience.getJwtProperty().getBase64Secret())
                .compact();
    }
    /*
    * 解析jwt
    */
    public static Claims parseJWT(String jsonWebToken, String base64Secret){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch (ExpiredJwtException e){
            logger.error("=============Token已过期==============",e);
        }
        catch (Exception e){
            logger.error("=============Token解析异常==============");
        }
        return  null;
    }
    public static String getUserName(String token,String base64Security){
        return parseJWT(token,base64Security).get("role",String.class);
    }

    public static boolean isTokenExpire(String token,String base64Security) {
        return parseJWT(token,base64Security).getExpiration().before(new Date());

    }



}
