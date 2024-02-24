package Vistas;

import Modelos.Producto;
import Sql.accesoDatos;
import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;





public class gestionProducto extends javax.swing.JFrame {
    accesoDatos ad = new accesoDatos();
    int mouseX, mouseY;
    
    


    
   
    ArrayList<Producto> arregloLista = new ArrayList<>();
    
    public gestionProducto() throws SQLException{
        initComponents();
        cargarLista();
        
        
      
        
    }
    
    public void cargarLista() throws SQLException{
        
        
        arregloLista.clear();
        Statement consulta;
        String sql = "SELECT * FROM \"Producto\"  ORDER BY \"Id_producto\" ASC";
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
        
        
        Producto p;
        boolean estado;
        while(respuesta.next()){
            int id = respuesta.getInt(1);
            String nombre = respuesta.getString(2);
            int cantidad = respuesta.getInt(3);
            double precio = respuesta.getDouble(4);
            String descripcion = respuesta.getString(5);
            if(respuesta.getInt(6) == 1){
                estado = true;
            }
            else{
                estado = false;
            }
            int tipo = respuesta.getInt(7);
            
            p = new Producto(id, nombre, cantidad, precio, descripcion, estado, tipo);
            
            arregloLista.add(p);
            
            
            
            
            
            
            
            
            
            
        }
        
       

        lstProductos.setListData(arregloLista.toArray());
        ad.desconectar();
       
         
    }
    
    
    public void cargarCampos(){
        
        Producto pro = (Producto) lstProductos.getSelectedValue();
        txtCodigo.setText(String.valueOf(pro.getCodigo()));
        txtNombre.setText(pro.getNombre());
        txtCantidad.setText(String.valueOf(pro.getCantidad()));
        txtPrecio.setText(String.valueOf(pro.getPrecio()));
        txtAreaDescripcion.setText(pro.getDescripcion());
        if(pro.getCantidad() > 0){
            rbtEnStrock.setSelected(true);
            
        }
        else{
            rbtAgotado.setSelected(true);
        }
        cboTipo.setSelectedIndex(pro.getTipo() - 1);
        
        
        
        
    }
    
    public void insertarDato() throws SQLException{
        
        
        
        Producto p;
        
        int codigo = Integer.parseInt(txtCodigo.getText());
        String nombre = txtNombre.getText();
        int cantidad =  Integer.parseInt(txtCantidad.getText());
        double precio = Double.parseDouble(txtPrecio.getText());
        String descripcion = txtAreaDescripcion.getText();
        boolean estado;
        int estadoBD;
        if(cantidad > 0){
            estado = true;
            estadoBD = 1;
        }
        else{
            estado = false;
            estadoBD = 0;
        }
        int tipo = cboTipo.getSelectedIndex() + 1;
        
        
        p = new Producto(codigo, nombre, cantidad, precio, descripcion, estado, tipo);
        
        
        String sql = """
                     INSERT INTO Producto (
                                              Id_producto,
                                              Nombre,
                                              Cantidad,
                                              Precio,
                                              Descripcion,
                                              Estado,
                                              Id_categoria
                                          ) VALUES (?,?,?,?,?,?,?)""";
        
        
        
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        estamento.setInt(1, codigo);
        estamento.setString(2, nombre);
        estamento.setInt(3, cantidad);
        estamento.setDouble(4, precio);
        estamento.setString(5, descripcion);
        estamento.setInt(6, estadoBD);
        estamento.setInt(7, tipo);
        
        estamento.executeUpdate();
        estamento.close();
        
        arregloLista.add(p);
        cargarLista();
        
        
        
        
        
        
    }
    
    
    public void eliminarDato() throws SQLException{
        
        JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el producto de forma permanente de la base de datos?");
        
   
        Producto p = (Producto) lstProductos.getSelectedValue();
        
        
        int valor;
        valor = p.getCodigo();
        String sql = "DELETE FROM Producto WHERE Id_producto = ?";
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        
        estamento.setInt(1, valor);
        estamento.executeUpdate();
        estamento.close();
        ad.desconectar();
        cargarLista();
        
        
    }
    
    
    public void updateDB() throws SQLException{
        
        Producto p = (Producto) lstProductos.getSelectedValue();
        
        
        int valorId;        
        valorId = p.getCodigo();
        String sql = "UPDATE Producto SET Id_producto = ?, Nombre = ?,Cantidad = ?,Precio = ?,Descripcion = ?,Estado = ?,Id_categoria = ?  WHERE Id_producto = ?";
        
        
        int codigo = Integer.parseInt(txtCodigo.getText());
        String nombre = txtNombre.getText();
        int cantidad =  Integer.parseInt(txtCantidad.getText());
        double precio = Double.parseDouble(txtPrecio.getText());
        String descripcion = txtAreaDescripcion.getText();
        boolean estado;
        int estadoBD;
        if(cantidad > 0){
            estado = true;
            estadoBD = 1;
        }
        else{
            estado = false;
            estadoBD = 0;
        }
        int tipo = cboTipo.getSelectedIndex() + 1;
        
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        
        estamento.setInt(1, codigo);
        estamento.setString(2, nombre);
        estamento.setInt(3, cantidad);
        estamento.setDouble(4, precio);
        estamento.setString(5, descripcion);
        estamento.setInt(6, estadoBD);
        estamento.setInt(7, tipo);
        estamento.setInt(8, valorId);
        
        estamento.executeUpdate();        
        estamento.close();
        cargarLista();
        
        
        
    }
    
    
    public void validarPlaceholer(){
        if(txtCodigo.getText().isEmpty()){
            txtCodigo.setText("Ingrese código del producto");
        }
        if(txtNombre.getText().isEmpty()){
            txtNombre.setText("Ingrese nombre del producto");
        }
        if(txtCantidad.getText().isEmpty()){
            txtCantidad.setText("Ingrese cantidad del producto");
        }
        if(txtPrecio.getText().isEmpty()){
            txtPrecio.setText("Ingrese precio del producto");
            
        }
        if(txtAreaDescripcion.getText().isEmpty()){
            txtAreaDescripcion.setText("Ingrese una descripción");
        }
    }
    
    public void vaciarCaja(JTextField caja){
        if(caja.getText().equals("Ingrese código del producto") || caja.getText().equals("Ingrese nombre del producto") || caja.getText().equals("Ingrese cantidad del producto") || caja.getText().equals("Ingrese precio del producto"))
        caja.setText("");
        
    }


   
    
    
  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDescripcion = new javax.swing.JTextPane();
        lblDescripcion = new javax.swing.JLabel();
        cboTipo = new javax.swing.JComboBox<>();
        lblEstado = new javax.swing.JLabel();
        rbtAgotado = new javax.swing.JRadioButton();
        rbtEnStrock = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstProductos = new javax.swing.JList<>();
        lblPrecio = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        lblCantidad = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtAreaDescripcion.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        txtAreaDescripcion.setForeground(new java.awt.Color(204, 204, 204));
        txtAreaDescripcion.setText("Ingrese una descripción");
        txtAreaDescripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAreaDescripcionFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(txtAreaDescripcion);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 480, 260, 80));

        lblDescripcion.setBackground(new java.awt.Color(255, 255, 255));
        lblDescripcion.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDescripcion.setText("Descripción:");
        jPanel1.add(lblDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 470, 110, 50));

        cboTipo.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        cboTipo.setForeground(new java.awt.Color(204, 204, 204));
        cboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Verduras", "Hortalizas", "Huevos", "Frutas" }));
        cboTipo.setOpaque(false);
        jPanel1.add(cboTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 410, 260, 40));

        lblEstado.setBackground(new java.awt.Color(255, 255, 255));
        lblEstado.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Estado:");
        jPanel1.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 560, 90, 50));

        rbtAgotado.setBackground(new java.awt.Color(0, 102, 102));
        buttonGroup1.add(rbtAgotado);
        rbtAgotado.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        rbtAgotado.setForeground(new java.awt.Color(255, 255, 255));
        rbtAgotado.setText("Agotado");
        rbtAgotado.setEnabled(false);
        rbtAgotado.setOpaque(false);
        rbtAgotado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtAgotadoActionPerformed(evt);
            }
        });
        jPanel1.add(rbtAgotado, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 570, -1, -1));

        rbtEnStrock.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbtEnStrock);
        rbtEnStrock.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        rbtEnStrock.setForeground(new java.awt.Color(255, 255, 255));
        rbtEnStrock.setSelected(true);
        rbtEnStrock.setText("En stock");
        rbtEnStrock.setEnabled(false);
        rbtEnStrock.setOpaque(false);
        rbtEnStrock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtEnStrockActionPerformed(evt);
            }
        });
        jPanel1.add(rbtEnStrock, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 570, -1, -1));

        lstProductos.setBackground(new java.awt.Color(101, 147, 129));
        lstProductos.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
        lstProductos.setForeground(new java.awt.Color(255, 255, 255));
        lstProductos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstProductosValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstProductos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 1030, 190));

        lblPrecio.setBackground(new java.awt.Color(255, 255, 255));
        lblPrecio.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblPrecio.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio.setText("Precio:");
        jPanel1.add(lblPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 350, 90, 50));

        txtPrecio.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtPrecio.setForeground(new java.awt.Color(204, 204, 204));
        txtPrecio.setText("Ingrese precio del producto");
        txtPrecio.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtPrecio.setOpaque(false);
        txtPrecio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecioFocusLost(evt);
            }
        });
        txtPrecio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPrecioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtPrecioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtPrecioMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtPrecioMousePressed(evt);
            }
        });
        txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioActionPerformed(evt);
            }
        });
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecioKeyPressed(evt);
            }
        });
        jPanel1.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 350, 260, 40));

        lblCantidad.setBackground(new java.awt.Color(255, 255, 255));
        lblCantidad.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidad.setText("Cantidad:");
        jPanel1.add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 480, 100, 50));

        btnCerrar.setBackground(new java.awt.Color(101, 147, 129));
        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCerrarMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnCerrarMousePressed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(101, 147, 129));
        jLabel4.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("X");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnCerrarLayout = new javax.swing.GroupLayout(btnCerrar);
        btnCerrar.setLayout(btnCerrarLayout);
        btnCerrarLayout.setHorizontalGroup(
            btnCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCerrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnCerrarLayout.setVerticalGroup(
            btnCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCerrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 60));

        txtCantidad.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(204, 204, 204));
        txtCantidad.setText("Ingrese cantidad del producto");
        txtCantidad.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtCantidad.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCantidad.setOpaque(false);
        txtCantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCantidadFocusLost(evt);
            }
        });
        txtCantidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCantidadMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtCantidadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtCantidadMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCantidadMousePressed(evt);
            }
        });
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
        });
        jPanel1.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 480, 260, 40));

        txtNombre.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(204, 204, 204));
        txtNombre.setText("Ingrese nombre del producto");
        txtNombre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtNombre.setOpaque(false);
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });
        txtNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtNombreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtNombreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNombreMousePressed(evt);
            }
        });
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreKeyPressed(evt);
            }
        });
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 410, 260, 40));

        lblNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblNombre.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombre.setText("Nombre:");
        jPanel1.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 100, 50));

        lblCodigo.setBackground(new java.awt.Color(255, 51, 0));
        lblCodigo.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblCodigo.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCodigo.setText("Código:");
        jPanel1.add(lblCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 90, 50));

        txtCodigo.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCodigo.setForeground(new java.awt.Color(204, 204, 204));
        txtCodigo.setText("Ingrese código del producto");
        txtCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtCodigo.setOpaque(false);
        txtCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoFocusLost(evt);
            }
        });
        txtCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCodigoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtCodigoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtCodigoMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCodigoMousePressed(evt);
            }
        });
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });
        jPanel1.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 350, 260, 40));

        jPanel2.setBackground(new java.awt.Color(101, 147, 129));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregar.setBackground(new java.awt.Color(171, 209, 142));
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Agregar");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnAgregarLayout = new javax.swing.GroupLayout(btnAgregar);
        btnAgregar.setLayout(btnAgregarLayout);
        btnAgregarLayout.setHorizontalGroup(
            btnAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        btnAgregarLayout.setVerticalGroup(
            btnAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel2.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 190, 70));

        btnModificar.setBackground(new java.awt.Color(171, 209, 142));
        btnModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Modificar");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnModificarLayout = new javax.swing.GroupLayout(btnModificar);
        btnModificar.setLayout(btnModificarLayout);
        btnModificarLayout.setHorizontalGroup(
            btnModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        btnModificarLayout.setVerticalGroup(
            btnModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel2.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, 190, 70));

        btnEliminar.setBackground(new java.awt.Color(171, 209, 142));
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel3.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Eliminar");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnEliminarLayout = new javax.swing.GroupLayout(btnEliminar);
        btnEliminar.setLayout(btnEliminarLayout);
        btnEliminarLayout.setHorizontalGroup(
            btnEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        btnEliminarLayout.setVerticalGroup(
            btnEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel2.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 70, 190, 70));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 1210, 200));

        jPanel3.setOpaque(false);
        jPanel3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel3MouseDragged(evt);
            }
        });
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel3MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 1150, -1));

        jPanel5.setBackground(new java.awt.Color(204, 255, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/trama gestionProductos alt.jpg"))); // NOI18N
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1210, 660));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1210, 660));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        btnEliminar.setBackground(new Color(190, 248, 142));
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        btnEliminar.setBackground(new Color(171,209,142));
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        btnModificar.setBackground(new Color(190, 248, 142));
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        btnModificar.setBackground(new Color(171,209,142));
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        btnAgregar.setBackground(new Color(190, 248, 142));
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        btnAgregar.setBackground(new Color(171,209,142));
    }//GEN-LAST:event_jLabel1MouseExited

    private void rbtAgotadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtAgotadoActionPerformed

    }//GEN-LAST:event_rbtAgotadoActionPerformed

    private void rbtEnStrockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtEnStrockActionPerformed

    }//GEN-LAST:event_rbtEnStrockActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void lstProductosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstProductosValueChanged
        cargarCampos();
    }//GEN-LAST:event_lstProductosValueChanged

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        try {
            insertarDato();
            JOptionPane.showConfirmDialog(null, "dato insertado con exito");
        } catch (SQLException ex) {
            Logger.getLogger(gestionProducto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al insertar dato revisar operacion");
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void txtCodigoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodigoMouseClicked
     
        
    }//GEN-LAST:event_txtCodigoMouseClicked

    private void txtNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMouseClicked
  
    }//GEN-LAST:event_txtNombreMouseClicked

    private void txtCantidadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCantidadMouseClicked
      
    }//GEN-LAST:event_txtCantidadMouseClicked

    private void txtPrecioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioMouseClicked
      
    }//GEN-LAST:event_txtPrecioMouseClicked

    private void txtCodigoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodigoMouseEntered
             
    }//GEN-LAST:event_txtCodigoMouseEntered

    private void txtCodigoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodigoMouseExited
   
    }//GEN-LAST:event_txtCodigoMouseExited

    private void txtNombreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMouseEntered
  
    }//GEN-LAST:event_txtNombreMouseEntered

    private void txtNombreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMouseExited
          
    }//GEN-LAST:event_txtNombreMouseExited

    private void txtCantidadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCantidadMouseEntered
     
    }//GEN-LAST:event_txtCantidadMouseEntered

    private void txtCantidadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCantidadMouseExited
     
        
    }//GEN-LAST:event_txtCantidadMouseExited

    private void txtPrecioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioMouseEntered
  
        
    }//GEN-LAST:event_txtPrecioMouseEntered

    private void txtPrecioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioMouseExited
   
    }//GEN-LAST:event_txtPrecioMouseExited

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked
      
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        try {
            eliminarDato();
            JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(gestionProducto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al eliminar registro revise");
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        try {
            updateDB();
            JOptionPane.showMessageDialog(null, "registro actualizado");
        } catch (SQLException ex) {
            Logger.getLogger(gestionProducto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "error al modificar registro");
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void txtCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoFocusLost
        validarPlaceholer();
    }//GEN-LAST:event_txtCodigoFocusLost

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost
        validarPlaceholer();
    }//GEN-LAST:event_txtNombreFocusLost

    private void txtCantidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadFocusLost
         validarPlaceholer();
    }//GEN-LAST:event_txtCantidadFocusLost

    private void txtPrecioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecioFocusLost
         validarPlaceholer();
    }//GEN-LAST:event_txtPrecioFocusLost

    private void txtAreaDescripcionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaDescripcionFocusLost
         validarPlaceholer();
    }//GEN-LAST:event_txtAreaDescripcionFocusLost

    private void txtCodigoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodigoMousePressed
        vaciarCaja(txtCodigo);
    }//GEN-LAST:event_txtCodigoMousePressed

    private void txtNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMousePressed
        vaciarCaja(txtNombre);
    }//GEN-LAST:event_txtNombreMousePressed

    private void txtCantidadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCantidadMousePressed
        vaciarCaja(txtCantidad);
    }//GEN-LAST:event_txtCantidadMousePressed

    private void txtPrecioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioMousePressed
        vaciarCaja(txtPrecio);
    }//GEN-LAST:event_txtPrecioMousePressed

    private void btnCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMouseEntered
        btnCerrar.setBackground(Color.red);
    }//GEN-LAST:event_btnCerrarMouseEntered

    private void btnCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMouseExited
        btnCerrar.setBackground( new Color(101,147,129) );
    }//GEN-LAST:event_btnCerrarMouseExited

    private void btnCerrarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMousePressed
        Inicio vI = new Inicio();
        vI.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCerrarMousePressed

    private void jPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MousePressed
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_jPanel3MousePressed

    private void jPanel3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseDragged
       int x = evt.getXOnScreen();
       int y = evt.getYOnScreen();
       this.setLocation(x - mouseX, y - mouseY);
    }//GEN-LAST:event_jPanel3MouseDragged

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        vaciarCaja(txtCodigo);
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyPressed
        vaciarCaja(txtNombre);
    }//GEN-LAST:event_txtNombreKeyPressed

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed
        vaciarCaja(txtCantidad);
    }//GEN-LAST:event_txtCantidadKeyPressed

    private void txtPrecioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyPressed
        vaciarCaja(txtPrecio);
    }//GEN-LAST:event_txtPrecioKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnAgregar;
    private javax.swing.JPanel btnCerrar;
    private javax.swing.JPanel btnEliminar;
    private javax.swing.JPanel btnModificar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JList<Object> lstProductos;
    private javax.swing.JRadioButton rbtAgotado;
    private javax.swing.JRadioButton rbtEnStrock;
    private javax.swing.JTextPane txtAreaDescripcion;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}

        

