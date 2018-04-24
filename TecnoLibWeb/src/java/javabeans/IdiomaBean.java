package javabeans;

import ejbs.DatosSesionLocal;
import java.util.ArrayList;
import java.util.Locale;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "idiomaBean")
@RequestScoped
public class IdiomaBean {

    private ArrayList<String> todosIdioma = null;

    public IdiomaBean() {
        System.out.println("CONSTRUCTOR IDIOMA");
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
        
    }
    
    public ArrayList<String> getIdiomas() {
        return todosIdioma;
    }
}
