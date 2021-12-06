package cn.com.xuxiaowei.authorization.server.controller;

import cn.com.xuxiaowei.authorization.server.properties.RsaKeyProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * JWK
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@RestController
public class JwkRestController {

    private RsaKeyProperties rsaKeyProperties;

    @Autowired
    public void setRsaKeyProperties(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    /**
     * JWK
     * <p>
     * {
     *     "keys": [
     *         {
     *             "kty": "RSA",
     *             "e": "AQAB",
     *             "n": "g-Vw_ExC-3ehMDelqIVISzD28ey7pqk2OIQ9MB0A1QfEHDrcioh0yRC_ip5mOutoJAyZ8YmnjFKl-XNgJ3l398CwmIuiPCZhakjYTncTXaP2L5JT3JgQJsaVtoK2XTuCdjFlcpB6wDA9O2CTilw2XMHuB0IKaCB-4Sr2pXHOEJ0"
     *         }
     *     ]
     * }
     * @return 返回 JWK
     */
    @RequestMapping("/jwk")
    public Map<String, Object> jwk() throws InvalidKeyException {
        RSAPublicKey rsaPublicKey = rsaKeyProperties.rsaPublicKey();
        RSAKey build = new RSAKey.Builder(rsaPublicKey).build();
        return new JWKSet(build).toJSONObject();
    }

}
