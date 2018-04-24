
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeans;

import ejbs.DatosSesionLocal;
import ejbs.OperacionesLocal;
import java.util.List;
import java.util.Locale;

import entidades.Item;
import entidades.Prod_Extendido;
import entidades.Producto;
import entidades.SinStock;
import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "tiendaBean")
@RequestScoped
public class TiendaBean {

    @EJB
    private OperacionesLocal oper;
    private List<Producto> todosproductos = null;
    private String filas_aviso;
    
    public TiendaBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("POST TIENDA BEAN");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(datos.getLanguage()));
        todosproductos = oper.getProductos(datos.getSeccion());
       
        
        if (datos.getDescripcion() != null) {
            int indice = todosproductos.indexOf(new Producto(datos.getDescripcion()));
            if (indice != -1) {
                Prod_Extendido ext = oper.getProd_Extendido(datos.getDescripcion());
                todosproductos.get(indice).setDescripcion(ext.getDescripcion());
                todosproductos.get(indice).setAño(ext.getAño());
                todosproductos.get(indice).setPaginas(ext.getPaginas());
                todosproductos.get(indice).setLengua(ext.getLengua());
            }
        }

    }


    public List<Producto> getProductos() {
        return todosproductos;
    }

    public void cambioSeccion() {
        System.out.println("cambioSeccion");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        refrescarItems();
        datos.setDescripcion(null);
    }

    public void cambioTab() {
        System.out.println("CAMBIO TAB");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();

        HttpSession sesion = (HttpSession) excontext.getSession(true);

        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        if (datos == null) {
            try {
                sesion.removeAttribute("datosSesion");
                sesion.invalidate();
                excontext.redirect(excontext.getRequestContextPath() + "/faces/facelets/inicio.xhtml");
            } catch (IOException ex) {
                System.out.println("Error en la redireccion del listener cerrar sesion");
            }
        } else {
            System.out.println("CAMBIO TAB= " + datos.getTabActivo());
            switch (datos.getTabActivo()) {
                case "cerrar":
                    System.out.println("cerrar");
                    cerrarSesion();
                    break;
                case "baja":
                    System.out.println("baja");
                    datos.setBaja(true);

                case "compra":
                    System.out.println("compra");
                    datos.setRecibo(0);
                    break;
                case "historico":
                    System.out.println("historico");
                    datos.setRecibo(0);
                    break;
            }
        }
    }

    public void AceptarAccion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setBaja(false);
        darseBaja();
        cerrarSesion();

    }

    public void CancelarAccion() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setBaja(false);
        datos.setTabActivo("compra");
    }

    private void cerrarSesion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        try {
            datos.cierreSesion();
            sesion.removeAttribute("datosSesion");
            sesion.invalidate();
            excontext.redirect(excontext.getRequestContextPath() + "/faces/facelets/inicio.xhtml");
        } catch (Exception ex) {
            System.out.println("Error en la redireccion del listener cerrar sesion");
        }
    }

    private void darseBaja() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        try {
            oper.eliminarUsuario(datos.getUsuario());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void clear() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setItems(new ArrayList<Item>());
    }

    public void cancelarCompra() {
        System.out.println("CANCELAR COMPRA");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        refrescarItems();
        datos.setEdtcomp(false);

    }

    public void err_aviso_stock() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setProd_erroneos(new ArrayList<SinStock>());
        datos.setEdtcomp(false);
    }

    public void err_aviso_eliminado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setProd_erroneos(new ArrayList<SinStock>());
    }

    public void realizarCompra() {
        System.out.println("REAIZAR COMPRA");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setEdtcomp(true);
        refrescarItems();
        if (datos.getErr_stock() || !datos.getProd_erroneos().isEmpty()) {
            datos.setEdtcomp(false);
        } else {
            datos.setEdtcomp(true);
        }
    }

    public void realizarPago() {
        System.out.println("REAIZAR PAGO");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        try {
            refrescarItems();
            if (datos.getProd_erroneos().isEmpty() && !datos.getErr_stock()) {
                oper.modificarStock(datos.getItems());
                datos.setRecibo(oper.nuevoReciboCliente(datos.getUsuario(), datos.getItems(), datos.getTotal()));
                datos.setProd_erroneos(new ArrayList<SinStock>());
                datos.setItems(new ArrayList<Item>());
                todosproductos = oper.getProductos(datos.getSeccion());
                if (datos.getDescripcion() != null) {
                    int indice = todosproductos.indexOf(new Producto(datos.getDescripcion()));
                    Prod_Extendido ext = oper.getProd_Extendido(datos.getDescripcion());
                    todosproductos.get(indice).setDescripcion(ext.getDescripcion());
                    todosproductos.get(indice).setAño(ext.getAño());
                    todosproductos.get(indice).setPaginas(ext.getPaginas());
                    todosproductos.get(indice).setLengua(ext.getLengua());
                    System.out.println("MOSTRAMOS IDPROD: " + datos.getDescripcion() + " INDICE: " + indice);

                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            datos.setEdtcomp(false);
        }

    }

    public void addCarrito(Producto p) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        Item it = new Item(p.getIdproducto(), p.getTitulo(), p.getAutor(), p.getIsbn(), p.getPrecio(), 1, false);
        datos.setTabActivo("compra");
        int id_item = datos.getItems().indexOf(it);
        if (id_item != -1) {
            Item itmod = datos.getItems().get(id_item);
            itmod.setUnidades(itmod.getUnidades() + 1);
        } else {
            datos.getItems().add(it);
        }
        refrescarItems();
    }

    public void refrescarItems() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.setErr_stock(false);
        List<Item> aux = new ArrayList<>();
        for (Item it : datos.getItems()) {
            try {
                Integer stock = oper.getStock(it.getIdproducto());
                if (stock != null) {
                    if (stock > 0) {
                        if (stock < it.getUnidades()) {
                            it.setErr_stock(true);
                            aux.add(it);
                            datos.setErr_stock(true);
                            if (datos.isEdtcomp()) {
                                datos.getProd_erroneos().add(new SinStock(it.getIdproducto(), it.getIsbn(), it.getTitulo(), stock));
                            }
                        } else {
                            it.setErr_stock(false);
                            aux.add(it);
                        }
                    } else {
                        datos.getProd_erroneos().add(new SinStock(it.getIdproducto(), it.getIsbn(), it.getTitulo(), stock));
                    }

                } else {
                    datos.getProd_erroneos().add(new SinStock(it.getIdproducto(), it.getIsbn(), it.getTitulo(), -1));
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        datos.setItems((ArrayList<Item>) aux);

    }

    public void Minimo(Item it) {
        if (it.getUnidades() > 1) {
            it.setUnidades(it.getUnidades() - 1);
        }
        refrescarItems();
    }

    public void Maximo(Item it) {
        it.setUnidades(it.getUnidades() + 1);
        refrescarItems();
    }

    public void Eliminar(Item it) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        datos.getItems().remove(it);
    }

    public void mostrarDesc(int indice, int idProducto) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        try {

            if (datos.getDescripcion() != null && datos.getDescripcion() == idProducto) {
                System.out.println("OCULTAMOS IDPROD: " + idProducto + " INDICE: " + indice);
                todosproductos.get(indice).setDescripcion(null);
                todosproductos.get(indice).setAño(null);
                todosproductos.get(indice).setPaginas(0);
                todosproductos.get(indice).setLengua(null);
                datos.setDescripcion(null);
            } else if (datos.getDescripcion() != null && datos.getDescripcion() != idProducto) {

                indice = todosproductos.indexOf(new Producto(datos.getDescripcion()));
                System.out.println("OCULTAMOS IDPROD: " + datos.getDescripcion() + " INDICE: " + indice);
                System.out.println(todosproductos.get(indice).getTitulo());
                todosproductos.get(indice).setDescripcion(null);
                todosproductos.get(indice).setAño(null);
                todosproductos.get(indice).setPaginas(0);
                todosproductos.get(indice).setLengua(null);
                datos.setDescripcion(idProducto);
                indice = todosproductos.indexOf(new Producto(idProducto));
                Prod_Extendido ext = oper.getProd_Extendido(idProducto);
                todosproductos.get(indice).setDescripcion(ext.getDescripcion());
                todosproductos.get(indice).setAño(ext.getAño());
                todosproductos.get(indice).setPaginas(ext.getPaginas());
                todosproductos.get(indice).setLengua(ext.getLengua());
                System.out.println("MOSTRAMOS IDPROD: " + idProducto + " INDICE: " + indice);
            } else {
                datos.setDescripcion(idProducto);
                Prod_Extendido ext = oper.getProd_Extendido(idProducto);
                todosproductos.get(indice).setDescripcion(ext.getDescripcion());
                todosproductos.get(indice).setAño(ext.getAño());
                todosproductos.get(indice).setPaginas(ext.getPaginas());
                todosproductos.get(indice).setLengua(ext.getLengua());
                System.out.println("MOSTRAMOS IDPROD: " + idProducto + " INDICE: " + indice);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getFilas_aviso() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext excontext = fc.getExternalContext();
        HttpSession sesion = (HttpSession) excontext.getSession(true);
        DatosSesionLocal datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
        filas_aviso = "";
        if (!datos.getProd_erroneos().isEmpty()) {
            for (SinStock s : datos.getProd_erroneos()) {
                if (s.getStock_actual() > 0) {
                    filas_aviso += "menosstockbackground,";
                } else if (s.getStock_actual() == -1) {
                    filas_aviso += "descbackground,";
                } else {
                    filas_aviso += "sinstockbackground,";
                }
            }
            filas_aviso = filas_aviso.substring(0, filas_aviso.lastIndexOf(","));

        } else {
            filas_aviso = "DescRow1,DescRow2";
        }
        System.out.println("FILASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS : " + filas_aviso);
        return filas_aviso;
    }

    public void setFilas_aviso(String filas_aviso) {
        this.filas_aviso = filas_aviso;
    }
}