package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.ModeloUsuario;

/**
 * Servlet implementation class ModificarUsuario
 */
@WebServlet("/ModificarUsuario")
public class ModificarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificarUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		ModeloUsuario usuario = new ModeloUsuario();
		
		try {
			usuario = usuario.getUsuario(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nombre = usuario.getNombre();
		Date fecha = usuario.getFechaNacimiento();
		
		request.setAttribute("id", id);
		request.setAttribute("nombre", nombre);
		request.setAttribute("fecha", fecha);
		request.getRequestDispatcher("modificarUsuario.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String nombre = request.getParameter("nombre");
		java.sql.Date fecha = null;
		try {
			/*fecha = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_nacimiento"));*/
			
			String fechaFormato = request.getParameter(request.getParameter("fecha_nacimiento"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			//surround below line with try catch block as below code throws checked exception
			fecha = new java.sql.Date(sdf.parse(fechaFormato).getTime());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ModeloUsuario usuario = new ModeloUsuario();
		try {
			usuario.modUsuarios(id, nombre, fecha);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/VerUsuarios");
	}

}
