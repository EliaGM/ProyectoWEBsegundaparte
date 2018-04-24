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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.faces.component.html.HtmlSelectOneMenu;

@ManagedBean(name = "usuarioBean")
@RequestScoped
public class UsuarioBean {

    @EJB
    private OperacionesLocal oper;
    private String password;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String email;
    private String telefono;
    private Integer ciudad;
    private List<Ciudad> todasciudades = null;
    
    private HtmlInputText txtnombre = new HtmlInputText();
    private HtmlInputText txttelefono = new HtmlInputText();
    private HtmlInputText txtdireccion = new HtmlInputText();
    private HtmlInputText txtemail = new HtmlInputText();
    private HtmlInputText txtpassword = new HtmlInputText();
    private HtmlInputText txtapellidos = new HtmlInputText();
   
    private HtmlSelectOneMenu sciudad = new HtmlSelectOneMenu();
    
    public UsuarioBean() {
        
    }
    @PostConstruct
    public void init() {
        System.out.println("USUARIO BEAN");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));
        
        try {
            Usuario us = oper.getUsuario(datos.getUsuario());
            
            System.out.println(us.getNombre());
            password = us.getPassword();
            nombre = us.getNombre();
            apellidos = us.getApellidos();
            direccion = us.getDireccion();
            email = us.getEmail();
            telefono = us.getTelefono();
            ciudad = us.getCiudad();

            txtpassword.setSubmittedValue(us.getPassword());
            txtnombre.setSubmittedValue(us.getNombre());
            txtapellidos.setSubmittedValue(us.getApellidos());
            txtdireccion.setSubmittedValue(us.getDireccion());
            txtemail.setSubmittedValue(us.getEmail());
            txttelefono.setSubmittedValue(us.getTelefono());
            sciudad.setSubmittedValue(String.valueOf(us.getCiudad()));


            
        } catch (Exception ex) {
            try {
                excontext.redirect(excontext.getRequestContextPath() + "/faces/facelets/inicio.xhtml?expired=true");
            } catch (IOException ex1) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getCiudad() {
        return ciudad;
    }

    public void setCiudad(Integer ciudad) {
        this.ciudad = ciudad;
    }

    

    public HtmlInputText getTxtnombre() {
        return txtnombre;
    }

    public void setTxtnombre(HtmlInputText txtnombre) {
        this.txtnombre = txtnombre;
    }

    public HtmlInputText getTxttelefono() {
        return txttelefono;
    }

    public void setTxttelefono(HtmlInputText txttelefono) {
        this.txttelefono = txttelefono;
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

    public HtmlInputText getTxtpassword() {
        return txtpassword;
    }

    public void setTxtpassword(HtmlInputText txtpassword) {
        this.txtpassword = txtpassword;
    }

    public HtmlInputText getTxtapellidos() {
        return txtapellidos;
    }

    public void setTxtapellidos(HtmlInputText txtapellidos) {
        this.txtapellidos = txtapellidos;
    }


    public HtmlSelectOneMenu getSciudad() {
        return sciudad;
    }

    public void setSciudad(HtmlSelectOneMenu sciudad) {
        this.sciudad = sciudad;
    }

   

    public List<Ciudad> getCiudades() {
        todasciudades = oper.getCiudades();
        return todasciudades;
    }

    
}
