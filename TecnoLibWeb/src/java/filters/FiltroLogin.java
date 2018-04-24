/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import ejbs.DatosSesionLocal;
import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FiltroLogin implements Filter {

    public static final String INICIO_PATH = "/faces/facelets/inicio.xhtml";
    public static final String REGISTRO_PATH = "/faces/facelets/registro.xhtml";
    public static final String CSS = "/faces/javax.faces.resource/css/";
    public static final String IMAGES = "/faces/resources/images/";
    public static final String JS = "/faces/javax.faces.resource/js/";
    public static final String BUNDLE = "/faces/javax.faces.resource/jsf.js";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession sesion = httpRequest.getSession();
        String usuario = null;
        String inicio = httpRequest.getContextPath() + INICIO_PATH;
        String registro = httpRequest.getContextPath() + REGISTRO_PATH;
        String bundle = httpRequest.getContextPath() + BUNDLE;
        String css = httpRequest.getContextPath() + CSS + ".*";
        String images = httpRequest.getContextPath() + IMAGES + ".*";
        String js = httpRequest.getContextPath() + JS + ".*";
        if (httpRequest.getRequestURI().equals("/TiendaLibros3/")) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + INICIO_PATH);
        } else {
            DatosSesionLocal datos = null;
            if (sesion.getAttribute("datosSesion") != null) {
                datos = (DatosSesionLocal) sesion.getAttribute("datosSesion");
            } else {
                try {
                    Context ctx = new InitialContext();
                    datos = (DatosSesionLocal) ctx.lookup("java:comp/env/datosSesion");
                    sesion.setAttribute("datosSesion", datos);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            usuario = datos.getUsuario();
            if (httpRequest.getRequestURI().equals(inicio) 
                    || (httpRequest.getRequestURI().equals(registro) && usuario != null)
                    || httpRequest.getRequestURI().matches(bundle)
                    || httpRequest.getRequestURI().matches(css)
                    || httpRequest.getRequestURI().matches(images)
                    || httpRequest.getRequestURI().matches(js)) {
                if (httpRequest.getRequestedSessionId() != null && httpRequest.isRequestedSessionIdValid()) {
                    chain.doFilter(request, response);
                } else if (httpRequest.getRequestedSessionId() == null) {
                    chain.doFilter(request, response);
                }
            }else if(usuario != null && !httpRequest.getRequestURI().equals(registro)){
                chain.doFilter(request, response);
            } else {
                if(datos.isPasoporLogin()){
                    chain.doFilter(request, response);
                }else{
                   httpResponse.sendRedirect(inicio); 
                }
                
            }
        }
    }

    @Override
    public void destroy() {
    }
}
