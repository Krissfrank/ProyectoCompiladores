package proyectocompi;

import java.io.IOException;

    /**
     * <ul>
     * <li> prog → conjProds </li>
     * <li> conjProds → conjProds | Prod </li>
     * <li> Prod → variable DEF expr; </li>
     * <li> expr → expr ALT term | term </li>
     * <li> term → term&factor | factor </li>
     * <li> factor → {expr} | [expr] | primario </li>
     * <li> primario → (expr) | variable | terminal </li>
     * </ul>
     * 
     * @author Abelardo Gandara Gonzalez 271358
     *         Cristian Manuel Franco Diaz
     * 
     * @version 1.0
     * @since 12/03/2015
     *
     */

public class ProyectoCompi {

    /**
     * Creacion de tokens.
     */

    private static final int NUMERO = 521;
    private static final int ID = 607;
    private static final int CONCATENACION = '&';
    private static final int ALTERNACION = '|';
    private static final int EOF = '.';
    private static final int FIN_SENT = ';';
    private static final int SUMA = '+';
    private static final int RESTA = '-';
    private static final int Cuno_l = '[';
    private static final int Cmasi = '{';
    private static final int apos = 39;
    private static final int PAR_DER = '(';
    private static final int PAR_IZQ = ')';
    private static final int Cuno_d = ']';
    private static final int Cmas_d = '}';
    private static final int def = '=';
    private int token;
    private String notacion;

    /**
     * Constructor principal de la clase
     */
    public ProyectoCompi() {
        this.token = -1;
        this.notacion = "";
    }
    
    /**
     *
     * @throws IOException
     */
    public void iniciarAnalisis() throws IOException {
        this.prog();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            ProyectoCompi Pcompi = new ProyectoCompi();
            Pcompi.iniciarAnalisis();
        } catch (IOException ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }

    /**
     * Hace referencia a una produccion gramatical, verifica que se componga los
     * elementos de las demas proyecciones hacia el final de stencia
     *
     * @throws IOExption cuando no existe una entrada
     */
    private void prog() throws IOException {

        this.next(this.token);
        this.Prod();
        if (this.token == FIN_SENT) {

            System.out.println(this.notacion);

        } else {
            throw new Error(String.format("Error de syntaxis, se esperaba %s", (char) FIN_SENT));
        }
    }

    private void conjProd() throws IOException {
    }

    /**
     * Asigna un conjunto de productos a una variable.
     * @throws IOException cuando no es una entrada valida.
     */
    private void Prod() throws IOException {
        this.Variable();
        if (this.token == def) {
            this.next(def);
            this.notacion = String.format("%s%s%s", this.notacion, (char) def, this.notacion);
        } else {
            return;
        }
    }
    
    /**
     * Comprueba si es alternacion |.
     * @throws IOException cuando no es una entrada valida.
     */
    private void Expr() throws IOException {
        this.Term();
        if (this.token == ALTERNACION) {
            this.next(ALTERNACION);
            this.notacion = String.format("%s%s%s", this.notacion, (char) ALTERNACION, this.notacion);
        } else {
            return;
        }
    }
    /**
     * Comprueba si es concatenacion con el token &
     * @throws IOException cuando no es una entrada valida.
     */
    private void Term() throws IOException {
        this.Factor();
        if (this.token == CONCATENACION) {
            this.next(CONCATENACION);
            Factor();
            this.notacion = String.format("%s%s%s", this.notacion, (char) CONCATENACION, this.notacion);
        } else {

            return;
        }
    }

    /**
     * Comprueba si es [expr], {expr} o primario tomando en cuenta sus reglas.
     * @throws IOException cuando no es una entrada valida.
     */
    private void Factor() throws IOException {
        this.Primario();
        if (this.token == Cmasi) {
            this.next(Cmasi);
            this.Expr();
            this.notacion = String.format("%s%s", this.notacion, (char) Cmas_d);
        } else if (this.token == Cuno_l) {
            this.next(Cuno_l);
            this.Expr();
            this.notacion = String.format("%s%s", this.notacion, (char) Cuno_d);
        } else {
            return;
        }
    }

    /**
     *
     * Comprueba si es terminal, variable o (expr).
     *
     * @throws IOException cuando no es una entrada valida.
     */
    private void Primario() throws IOException {

        if (this.token == apos) {
            this.Termina();
            this.notacion = String.format("%s%s", this.notacion, (char) apos);
            this.next(this.token);
        } else {
            if (!(Character.isDigit((char) this.token))) {
                this.Variable();
                this.next(this.token);
                this.notacion = String.format("%s%s", this.notacion, (char) this.token);
                this.next(this.token);
            } else {
                if (this.token == PAR_IZQ) {
                    this.Expr();
                    this.notacion = String.format("%s%s", this.notacion, (char) PAR_DER);
                    this.next(this.token);
                } else {
                    throw new Error(String.format("Error de sintaxis token invalido %s", (char) this.token));
                }
            }
        }
    }

    /**
     * Verifica que la entrada sea una letra
     * @throws IOException cuando no es una entrada valida
     */
    private void Variable() throws IOException {
        if (!(Character.isDigit((char) this.token))) {
            this.next(this.token);
            this.notacion = String.format("%s%s", this.notacion, (char) this.token);
            this.next(this.token);
        }
    }

    /**
     * Verifica que la entrada sea un apostrofe.
     * @throws IOException cuando no es una entrada valida
     */
    private void Termina() throws IOException {
        if (this.token == apos) {
            this.notacion = String.format("%s%s", this.notacion, (char) FIN_SENT);
            this.next(this.token);
        }
    }

    /**
     * Verifica que la secuencia de tokens sea valida.
     * @param token caracter que no sea numero.
     * @throws IOException cuando no existe una entrada valida.
     */
    private void next(int token) throws IOException {
        if (this.token == token) {
            this.token = System.in.read();
        } else {
            throw new Error(String.format("Error de sintaxis token invalido %s", (char) this.token));
        }
    }
}
