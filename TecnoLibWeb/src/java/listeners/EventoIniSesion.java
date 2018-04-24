/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import ejbs.DatosSesionLocal;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class EventoIniSesion implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent se) {
       System.out.println("SESION CREADA:: ID="+se.getSession().getId());
     
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        
        HttpSession sesion = se.getSession();
        DatosSesionLocal datos = (DatosSesionLocal)sesion.getAttribute("datosSesion");
        datos.cierreSesion();
        
        System.out.println("SESION DESTRUIDA:: ID="+se.getSession().getId());
    }
    
}
