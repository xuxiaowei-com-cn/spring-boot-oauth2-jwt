package cn.com.xuxiaowei.authorization.server.controller;

import cn.com.xuxiaowei.authorization.server.properties.RsaKeyProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
public class JwkRestController {

    private RsaKeyProperties rsaKeyProperties;

    @Autowired
    public void setRsaKeyProperties(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    @RequestMapping("/jwk")
    public Map<String, Object> jwk() throws InvalidKeyException {
        RSAPublicKey rsaPublicKey = rsaKeyProperties.rsaPublicKey();
        RSAKey build = new RSAKey.Builder(rsaPublicKey).build();
        return new JWKSet(build).toJSONObject();
    }

}
