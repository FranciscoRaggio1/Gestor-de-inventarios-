package Modelos;


public class Producto {

    
    
    private int codigo;
    private String nombre;
    private int cantidad;
    private double precio;
    private String descripcion;
    private boolean estado;
    private int tipo;
    
    
    

    public Producto(int codigo, String nombre, int cantidad, double precio, String descripcion, boolean estado, int tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descripcion = descripcion;
        this.estado = estado;
        this.tipo = tipo;
    }
    public Producto(){
        
    }
    
    
    

    public int getCodigo() {
        return codigo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigo + "---" +  nombre;
    }
    
    
    
    
    
    
    
    
    
    
    
}
