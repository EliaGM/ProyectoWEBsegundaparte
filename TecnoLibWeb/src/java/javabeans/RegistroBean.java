/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeans;

import ejbs.DatosSesionLocal;
import ejbs.OperacionesLocal;
import java.util.List;
import java.util.Locale;
import entidades.Ciudad;
import entidades.Usuario;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "registroBean")
@RequestScoped
public class RegistroBean {

    @EJB
    private OperacionesLocal oper;
    private String reg_usuario;
    private String reg_password;
    private String reg_nombre;
    private String reg_apellidos;
    private String reg_direccion;
    private String reg_email;
    private String reg_telefono;
    private Integer reg_ciudad;
    private List<Ciudad> todasciudades = null;
    private boolean err_existe;
    private List<String> todosIdioma = null;
    
    private HtmlInputText txtusuario = new HtmlInputText();
    private HtmlInputText txtpwd = new HtmlInputText();
    private HtmlInputText txtnombre = new HtmlInputText();
    private HtmlInputText txtapellidos = new HtmlInputText();
    private HtmlInputText txtdireccion = new HtmlInputText();
    private HtmlInputText txtemail = new HtmlInputText();
    private HtmlInputText txttelefono = new HtmlInputText();

    public RegistroBean() {
        System.out.println("CONSTRUCTOR REGISTRO");
        todosIdioma = new ArrayList<>();
        todosIdioma.add("es");
        todosIdioma.add("en");
        
    }
    public void cambiarIdioma() {
        System.out.println("CAMBIO IDIOMA");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));
        datos.setDescripcion(null);
        txtusuario.setValid(true);
        txtpwd.setValid(true);
        txtnombre.setValid(true);
        txtapellidos.setValid(true);
        txtdireccion.setValid(true);
        txtemail.setValid(true);
        txttelefono.setValid(true);
    }
    
    public List<String> getIdiomas() {
        return todosIdioma;
    }

    public String volverInicio() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        
        datos.setModoregistro(false);
        return "inicio";
    }

    public void limpiar() {
        reg_password = reg_usuario = reg_nombre = reg_apellidos = reg_direccion = reg_email = reg_telefono = "";
        reg_ciudad = 1;
        err_existe = false;
    }

    public String registrarse() {
        err_existe = false;
        String go_to = "";
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));
        try {
            Usuario us = new Usuario(reg_usuario.trim(), reg_password.trim(), reg_nombre.trim(), reg_apellidos.trim(), reg_direccion.trim(), reg_email.trim(), reg_telefono.trim(), reg_ciudad);
            if (oper.insertarUsuario(us) == 0) {
                err_existe = true;
            } else {
                datos.setModoregistro(false);
                datos.setUsuario(reg_usuario);
                datos.setSeccion(-1);
                go_to = "inicio";
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            return go_to;
        }
    }

    public String getReg_usuario() {
        return reg_usuario;
    }

    public void setReg_usuario(String reg_usuario) {
        this.reg_usuario = reg_usuario;
    }

    public String getReg_password() {
        return reg_password;
    }

    public void setReg_password(String reg_password) {
        this.reg_password = reg_password;
    }

    public String getReg_nombre() {
        return reg_nombre;
    }

    public void setReg_nombre(String reg_nombre) {
        this.reg_nombre = reg_nombre;
    }

    public String getReg_apellidos() {
        return reg_apellidos;
    }

    public void setReg_apellidos(String reg_apellidos) {
        this.reg_apellidos = reg_apellidos;
    }

    public String getReg_direccion() {
        return reg_direccion;
    }

    public void setReg_direccion(String reg_direccion) {
        this.reg_direccion = reg_direccion;
    }

    public String getReg_email() {
        return reg_email;
    }

    public void setReg_email(String reg_email) {
        this.reg_email = reg_email;
    }

    public String getReg_telefono() {
        return reg_telefono;
    }

    public void setReg_telefono(String reg_telefono) {
        this.reg_telefono = reg_telefono;
    }

    public Integer getReg_ciudad() {
        return reg_ciudad;
    }

    public void setReg_ciudad(Integer reg_ciudad) {
        this.reg_ciudad = reg_ciudad;
    }

    public boolean getErr_existe() {
        return err_existe;
    }

    public void setErr_existe(boolean err_existe) {
        this.err_existe = err_existe;
    }

    public List<Ciudad> getCiudades() {
        todasciudades = oper.getCiudades();
        return todasciudades;
    }

    public HtmlInputText getTxtusuario() {
        return txtusuario;
    }

    public void setTxtusuario(HtmlInputText txtusuario) {
        this.txtusuario = txtusuario;
    }

    public HtmlInputText getTxtpwd() {
        return txtpwd;
    }

    public void setTxtpwd(HtmlInputText txtpwd) {
        this.txtpwd = txtpwd;
    }

    public HtmlInputText getTxtnombre() {
        return txtnombre;
    }

    public void setTxtnombre(HtmlInputText txtnombre) {
        this.txtnombre = txtnombre;
    }

    public HtmlInputText getTxtapellidos() {
        return txtapellidos;
    }

    public void setTxtapellidos(HtmlInputText txtapellidos) {
        this.txtapellidos = txtapellidos;
    }

    public HtmlInputText getTxtdireccion() {
        return txtdireccion;
    }

    public void setTxtdireccion(HtmlInputText txtdireccion) {
        this.txtdireccion = txtdireccion;
    }

    public HtmlInputText getTxtemail() {
        return txtemail;
    }

    public void setTxtemail(HtmlInputText txtemail) {
        this.txtemail = txtemail;
    }

    public HtmlInputText getTxttelefono() {
        return txttelefono;
    }

    public void setTxttelefono(HtmlInputText txttelefono) {
        this.txttelefono = txttelefono;
    }
    
    
    
}
