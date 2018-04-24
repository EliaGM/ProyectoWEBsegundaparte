/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeans;

import ejbs.DatosSesionLocal;
import ejbs.OperacionesLocal;
import java.util.ArrayList;
import java.util.Locale;
import entidades.Detalle;
import entidades.Recibo;
import java.text.DecimalFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "historicoBean")
@RequestScoped
public class HistoricoBean {

    @EJB
    private OperacionesLocal oper;
    private List<Recibo> todosrecibos = new ArrayList<>();
    private List<Detalle> tododetalle;
    private DecimalFormat df = new DecimalFormat("#####.##");
    
    public HistoricoBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("CONSTRUCTOR HISTORICO");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(false);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));


        try {
            todosrecibos = oper.getRecibos(datos.getUsuario());
            if (datos.getRecibo() == 0) {
                mostrarDetalle(todosrecibos.get(0).getIdrecibo());
            } 
            datos.setEdtcomp(false);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    

    public List<Recibo> getRecibos() {
        return todosrecibos;
    }

    public List<Detalle> getDetalles() {
        return tododetalle;
    }

    public void Detalles(int idrecibo) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(false);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setRecibo(idrecibo);
        mostrarDetalle(idrecibo);

    }

    public void mostrarDetalle(int idrecibo) {

        tododetalle = new ArrayList<>();
        try {
            tododetalle = oper.getDetalles(idrecibo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }
    
    
    
}
