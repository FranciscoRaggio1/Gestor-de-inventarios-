package Modelos;


public class Cliente {
    String nombre;
    String direccion;
    String localidad;
    long cuit;

    public Cliente(String nombre, String direccion, String localidad, long cuit) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.localidad = localidad;
        this.cuit = cuit;
    }
    
    public Cliente(String nombre, int cuit){
        this.nombre = nombre;
        this.cuit = cuit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public long getCuit() {
        return cuit;
    }

    public void setCuit(long cuit) {
        this.cuit = cuit;
    }

    @Override
    public String toString() {
        return nombre +  " " + cuit ;
    }
    
    
    
           
    
}
