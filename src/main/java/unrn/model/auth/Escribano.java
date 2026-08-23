package unrn.model.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Escribano {

    static final String ID_CLAIM_KEY = "id";
    private final SecretKey signatureKey;
    //Esta key no debería esta aca, sino en configuracion del servicio
    private String key = "2DazT1TCBGi4lcctlN7el8buF3n3uO9H8xfKiZWz3no=";

    private Escribano() {
        this.signatureKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public static Escribano escribano() {
        return new Escribano();
    }

    public String generarTokenPara(int userId) {
        return Jwts.builder()
                .claim(ID_CLAIM_KEY, userId)
                .signWith(signatureKey)
                .compact();
    }

    public int verificarToken(String token) {
        var jwt = Jwts.parser()
                .verifyWith(signatureKey)
                .build()
                .parseSignedClaims(token);
        return (int) jwt.getPayload().get(ID_CLAIM_KEY);
    }
}
