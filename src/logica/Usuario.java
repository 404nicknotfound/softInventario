package logica;

import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String nombre;
	private String username;
	private String contrasena;
	private String rol;
	private boolean activo;
	
	public Usuario(String nombre, String username, String contrasena, String rol) {
		
		this.id = UUID.randomUUID().toString();
		this.nombre = nombre;
		this.username = username;
		this.contrasena = contrasena;
		this.rol = rol;
		this.activo = true;
	}
	
	public boolean autenticar(String username,String contrasena) {
		return this.username.equals(username) && this.contrasena.equals(contrasena);
	}
	
	public void cambiarContrasena(String nueva) {
		this.contrasena = nueva;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getId() {
		return id;
	}

	public String getContrasena() {
		return contrasena;
	}
	
	
	
}
