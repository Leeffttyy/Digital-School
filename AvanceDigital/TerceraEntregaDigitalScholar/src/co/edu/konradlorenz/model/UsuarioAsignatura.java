package co.edu.konradlorenz.model;

public class UsuarioAsignatura implements Registrable {
    private Usuario usuario, auxU;
    private Asignatura asignatura, auxA;
    
    public UsuarioAsignatura() {}
    public UsuarioAsignatura(Usuario usuario, Asignatura asignatura) {
        this.usuario = usuario;
        this.asignatura = asignatura;
        this.auxA = asignatura;
        this.auxU = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        if (this.usuario==null) {
            auxU = usuario;
        } else {
            auxU = this.usuario;
        }
        this.usuario = usuario;
    }
    public Asignatura getAsignatura() {
        return asignatura;
    }
    public void setAsignatura(Asignatura asignatura) {
        if (this.asignatura==null) {
            auxA = asignatura;
        } else {
            auxA = this.asignatura;
        }
        this.asignatura = asignatura;
    }
    public Usuario getAuxU() {
        return auxU;
    }
    public void setAuxU(Usuario auxU) {
        this.auxU = auxU;
    }
    public Asignatura getAuxA() {
        return auxA;
    }
    public void setAuxA(Asignatura auxA) {
        this.auxA = auxA;
    }
    @Override
    public String toString() {
        return usuario.toString();
    }
    
    @Override
    public Object[] obtenerCampos() {
        Object[] campos = {usuario.getCodigo(), this, asignatura.getId(), asignatura};
        return campos;
    }

}
