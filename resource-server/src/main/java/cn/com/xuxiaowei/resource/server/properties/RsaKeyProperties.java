package cn.com.xuxiaowei.resource.server.properties;

import cn.hutool.crypto.asymmetric.RSA;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA
 *
 * @author xuxiaowei
 * @see <a href="https://github.com/xuxiaowei-com-cn/RSA">RSA 非对称性加密、签名工具</a>
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties("rsa")
public class RsaKeyProperties {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 获取 RSA 公钥 对象
     *
     * @return 返回 RSA 公钥
     */
    public PublicKey publicKey() {
        return new RSA(null, publicKey).getPublicKey();
    }

    /**
     * 获取 RSA 公钥 对象
     *
     * @return 返回 RSA 公钥
     */
    public RSAPublicKey rsaPublicKey() {
        return (RSAPublicKey) publicKey();
    }

}
