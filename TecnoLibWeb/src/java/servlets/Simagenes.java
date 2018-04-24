/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import ejbs.OperacionesLocal;
import entidades.Imagen;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Simagenes extends HttpServlet {

    @EJB
    private OperacionesLocal op;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String imageId = request.getParameter("idimagen");
        Imagen image = op.getImagen(Integer.valueOf(imageId));
        if (image != null) {
            response.reset();
            response.setContentType(image.getTipocontenido());
            response.setContentLength(image.getContenido().length);
            response.getOutputStream().write(image.getContenido());
        }

    }
}
