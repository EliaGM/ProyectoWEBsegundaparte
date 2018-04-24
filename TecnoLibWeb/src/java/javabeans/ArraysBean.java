/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeans;

import ejbs.DatosSesionLocal;
import ejbs.OperacionesLocal;
import entidades.Seccion;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "arraysBean")
@RequestScoped
public class ArraysBean {

    @EJB
    private OperacionesLocal oper;
    private ArrayList<String> todostabs = null;
    private List<Seccion> todassecciones = null;
    

    public ArraysBean() {
        System.out.println("CONSTRUCT arraysBean");
        todostabs = new ArrayList<>();
        todostabs.add("compra");
        todostabs.add("historico");
        todostabs.add("cerrar");
        todostabs.add("baja");
    }

    @PostConstruct
    public void init() {
        System.out.println("POST arraysBean");

        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext excontext = fc.getExternalContext();
            HttpSession sesion = (HttpSession) excontext.getSession(true);
            DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
            System.out.println(datos.getSeccion());
            todassecciones = oper.getSecciones();

        } catch (Exception ex) {
            System.out.println("EXCEPTION ARRRRRRRAYAAAA: " + ex.getMessage());
        }

    }

    public List<Seccion> getSecciones() {
        return todassecciones;
    }

    public ArrayList<String> getTabs() {
        return todostabs;
    }

    
}
