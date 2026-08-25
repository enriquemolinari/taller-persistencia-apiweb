package unrn.model;

import org.junit.jupiter.api.Test;
import unrn.model.auth.Escribano;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class EscribanoTest {

    @Test
    public void test01() {
        var token = Escribano.escribano().generarTokenPara(1);
        assertNotNull(token);
    }

    @Test
    public void test02() {
        var token = Escribano.escribano().generarTokenPara(1);
        var idUser = Escribano.escribano().verificarToken(token);
        assertEquals(1, idUser, "El idUser obtenido del token debería ser 1");
    }

    @Test
    public void test03() {
        var token = Escribano.escribano().generarTokenPara(2);
        Base64.Decoder decoder = Base64.getDecoder();
        String[] parts = token.split("\\.");
        String header = parts[0];
        String payload = new String(decoder.decode(parts[1]));
        String signature = parts[2];

        payload = payload.replaceAll("2", "3");
        String modifiedToken = header +
                "." + Base64.getEncoder().encodeToString(payload.getBytes())
                + "." + signature;

        assertThrows(RuntimeException.class, () ->
                        Escribano.escribano().verificarToken(modifiedToken)
                , "Se esperaba una excepción al verificar un token modificado");
    }
}
