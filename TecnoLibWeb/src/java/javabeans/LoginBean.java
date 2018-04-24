/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeans;

import ejbs.DatosSesionLocal;
import ejbs.OperacionesLocal;
import java.util.Locale;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "loginBean")
@RequestScoped
@EJB(name = "datosSesion", beanInterface = DatosSesionLocal.class)
public class LoginBean {

    @EJB
    private OperacionesLocal oper;
    private String usuario;
    private String pwd;
    private boolean err_noexiste = false;

    public LoginBean() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext excontext = fc.getExternalContext();
            HttpSession sesion = (HttpSession) excontext.getSession(true);
            DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));
            datos.setPasoporLogin(true);


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean getErr_noexiste() {
        return err_noexiste;
    }

    public void setErr_noexiste(boolean err_noexiste) {
        this.err_noexiste = err_noexiste;
    }

    public String doLogin() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        String go_to = "";
        try {
            HttpSession sesion = (HttpSession) excontext.getSession(false);

            DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");


            String respuesta = oper.Validar(usuario.trim(), pwd.trim());

            err_noexiste = false;
            if (respuesta != null) {
                if (respuesta.equals("administrador")) {
                    datos.setUsuario(respuesta);
                    go_to = "ventas";
                } else {
                    datos.setUsuario(respuesta);
                    datos.setSeccion(1);
                    datos.setDescripcion(null);
                    datos.setTabActivo("compra");
                    datos.setRecibo(0);
                    go_to = "tienda";
                }


            } else {
                System.out.println("ES NULL");
                if (!datos.getModoregistro()) {
                    datos.setModoregistro(true);

                }
                err_noexiste = true;

            }


        } catch (Exception ex) {
            System.out.println("EXCEPTION : " + ex.getMessage());
        } finally {
            return go_to;
        }
    }

    public String irARegistro() {
        System.out.println("IR A REGISTRO");
        return "registro";
    }
}
