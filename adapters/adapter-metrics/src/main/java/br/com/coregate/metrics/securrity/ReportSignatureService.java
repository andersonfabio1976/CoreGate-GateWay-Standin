package br.com.coregate.metrics.securrity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * 🔐 Serviço de assinatura digital RSA-SHA256 dos relatórios de verificação.
 */
@Slf4j
@Service
public class ReportSignatureService {

    private static final String PRIVATE_KEY_PATH = "/opt/coregate/security/keys/coregate_private.pem";
    private static final String PUBLIC_KEY_PATH  = "/opt/coregate/security/keys/coregate_public.pem";

    public String sign(String payload) {
        try {
            PrivateKey privateKey = loadPrivateKey();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(payload.getBytes());
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            log.error("Erro ao assinar payload: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean verify(String payload, String signatureBase64) {
        try {
            PublicKey publicKey = loadPublicKey();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(payload.getBytes());
            byte[] signedBytes = Base64.getDecoder().decode(signatureBase64);
            return signature.verify(signedBytes);
        } catch (Exception e) {
            log.error("Erro ao verificar assinatura: {}", e.getMessage());
            return false;
        }
    }

    // ============================================================
    // 🔧 Leitura das chaves PEM
    // ============================================================

    private PrivateKey loadPrivateKey() throws Exception {
        String key = Files.readString(new File(PRIVATE_KEY_PATH).toPath())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey() throws Exception {
        String key = Files.readString(new File(PUBLIC_KEY_PATH).toPath())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
