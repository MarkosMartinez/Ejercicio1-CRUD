package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import modelo.ModeloRol;
import modelo.ModeloUsuario;
import modelo.Rol;
import modelo.Usuario;

/**
 * Servlet implementation class CrearUsuario
 */
@WebServlet("/CrearUsuario")
public class CrearUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearUsuario() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuarioLogeado = (Usuario) session.getAttribute("usuarioLogueado");
		if(usuarioLogeado == null) {
			response.sendRedirect("Login");
		}else {
		
		ModeloRol mrol = new ModeloRol();
		ArrayList<Rol> roles = new ArrayList<>();
		try {
			roles = mrol.getRoles();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("roles", roles);
		request.getRequestDispatcher("crearUsuario.jsp").forward(request, response);
	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String dni = request.getParameter("dni");
		String codigo = request.getParameter("codigo");
		String passSinEncriptar = request.getParameter("password");
		String password = DigestUtils.sha1Hex(passSinEncriptar);
		String fechaSinFormato = request.getParameter("fecha_nacimiento");
		int id_rol = Integer.parseInt(request.getParameter("roles"));
		Date fecha = null;
		try {
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaSinFormato);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (Usuario.verificarContraseña(password)) {
			ModeloUsuario usuario = new ModeloUsuario();
			try {
				usuario.insertUsuarios(nombre, dni, codigo, fecha, password, id_rol);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			response.sendRedirect(request.getContextPath() + "/VerUsuarios?aviso=usucreado");
		}else {
			response.sendRedirect(request.getContextPath() + "/VerUsuarios?aviso=error");
		}
		
		
	}

}
