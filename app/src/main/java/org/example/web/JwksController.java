package org.example.web;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.example.config.RsaKeys.KeyPairHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JwksController {
    private final KeyPairHolder keys;
    public JwksController(KeyPairHolder keys) { this.keys = keys; }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        var jwk = new RSAKey.Builder(keys.pub())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(new com.nimbusds.jose.Algorithm("RS256"))
                .keyID(keys.kid())
                .build();
        return Map.of("keys", List.of(jwk.toJSONObject()));
    }
}
