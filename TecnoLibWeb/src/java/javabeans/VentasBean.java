
package javabeans;

import ejbs.DatosSesionLocal;
import ejbs.OperacionesLocal;
import entidades.Detalle;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "ventasBean")
@RequestScoped
public class VentasBean {

    @EJB
    private OperacionesLocal oper;
    private List<Detalle> ventas = new ArrayList<>();
    private String fecha;
    private HtmlInputText Htmlfecha;
    private DecimalFormat df = new DecimalFormat("#####.##");

    @PostConstruct
    public void init() {
        System.out.println("CONSTRUCTOR VENTAS");
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext excontext = fc.getExternalContext();
            HttpSession sesion = (HttpSession) excontext.getSession(true);
            DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));
            

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Detalle> getVentas() {
        return ventas;
    }

    public void setVentas(List<Detalle> ventas) {
        this.ventas = ventas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public HtmlInputText getHtmlfecha() {
        return Htmlfecha;
    }

    public void setHtmlfecha(HtmlInputText Htmlfecha) {
        this.Htmlfecha = Htmlfecha;
    }
    
    

    public void realizarFiltro() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse("2015-02-26");
            this.ventas = oper.getDetalles(date);
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

    public void mostrarTodos() {
        ventas = oper.getVentas();
    }
    
    public String salir(){
        return "inicio";
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }
    
    

}
