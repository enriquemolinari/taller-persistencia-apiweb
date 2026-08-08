package unrn.model;

public class NumeroTelefono {
    static final String ERROR_CODIGO_AREA_INVALIDO = "El codigo de area debe tener exactamente 4 digitos";
    static final String ERROR_NUMERO_INVALIDO = "El numero debe tener entre 6 y 7 caracteres";

    private final String codigoArea;
    private final String numero;

    public NumeroTelefono(String codigoArea, String numero) {
        assertCodigoAreaValido(codigoArea);
        assertNumeroValido(numero);
        this.codigoArea = codigoArea;
        this.numero = numero;
    }

    boolean esMismoNumero(NumeroTelefono otroNumero) {
        return otroNumero != null
                && codigoArea.equals(otroNumero.codigoArea)
                && numero.equals(otroNumero.numero);
    }

    private void assertCodigoAreaValido(String codigoArea) {
        if (codigoArea == null || !codigoArea.matches("\\d{4}")) {
            throw new RuntimeException(ERROR_CODIGO_AREA_INVALIDO);
        }
    }

    private void assertNumeroValido(String numero) {
        if (numero == null || numero.length() < 6 || numero.length() > 7) {
            throw new RuntimeException(ERROR_NUMERO_INVALIDO);
        }
    }
}
