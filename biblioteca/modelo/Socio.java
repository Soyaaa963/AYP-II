package biblioteca.modelo;

import java.util.Objects;

public class Socio {
	
// atributos privados 
    private String nroSocio;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo; 

    //constructor 
    public Socio(String nroSocio, String nombre, String apellido, String email, boolean activo) {
    	  // TODO: inicializar atributos
		this.nroSocio = nroSocio;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.activo = activo;
	}

    // TODO: getters y setters
    public String getNroSocio() {
		return nroSocio;
	}

	public void setNroSocio(String nroSocio) {
		this.nroSocio = nroSocio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}


	// TODO: implementar - dos socios son iguales si tienen el mismo nroSocio
	@Override
	public int hashCode() {
		return Objects.hash(nroSocio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Socio other = (Socio) obj;
		return Objects.equals(nroSocio, other.nroSocio);
	}

	
	@Override
	public String toString() {
		 // TODO: implementar
		return "Socio [nroSocio=" + nroSocio + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email
				+ ", activo=" + activo + "]";
	}

}