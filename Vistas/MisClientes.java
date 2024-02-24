package Vistas;

import Modelos.Cliente;
import Modelos.Producto;
import Sql.accesoDatos;
import java.awt.Color;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class MisClientes extends javax.swing.JFrame {
    
    accesoDatos ad = new accesoDatos();
    ArrayList<Object> datosCliente = new ArrayList<>();
    DefaultTableModel modelTabla = new DefaultTableModel();
    
    int mouseY, mouseX;
    
    
    

    public MisClientes() throws SQLException {
        initComponents();       
        cambiarVista(true);
        botoneraAnimada(true);
        btnRegistrarCliente.requestFocus();
        cargarListado();
        
        
        
       
    }
    public void cambiarVista(boolean x){
        txtNombre.setVisible(x);
        txtDireccion.setVisible(x);
        txtLocalidad.setVisible(x);
        txtCuit.setVisible(x);
        lblCuit.setVisible(x);
        lblDireccion.setVisible(x);
        lblLocalidad.setVisible(x);
        lblNombre.setVisible(x);
        btnRegistrarCliente.setVisible(x);
        tabla.setVisible(!x);
        btnConsultarCuenta.setVisible(!x);
        btnCrearCuenta.setVisible(!x);
        
        
    }

    public void botoneraAnimada(boolean valor){
        if(valor){
            btnIngresoDatos.setBackground(new Color(171,209,142));
            btnListado.setBackground(new Color(101,147,129));
        }
        else{
            btnIngresoDatos.setBackground(new Color(101,147,129));
            btnListado.setBackground(new Color(171,209,142));
            
        }
        
    }
    
    public void validarPlaceholer(){
        if(txtNombre.getText().isEmpty()){
            txtNombre.setText("Ingrese nombre");
            txtNombre.setForeground(new Color(204,204,204));
        }
        if(txtLocalidad.getText().isEmpty()){
            txtLocalidad.setText("Ingrese localidad");
            txtLocalidad.setForeground(new Color(204,204,204));
        }
        if(txtDireccion.getText().isEmpty()){
            txtDireccion.setText("Ingrese dirección");
            txtDireccion.setForeground(new Color(204,204,204));
            
        }
        if(txtCuit.getText().isEmpty()){
            txtCuit.setText("Ingrese cuit");
            txtCuit.setForeground(new Color(204,204,204));
        }
    }
    public void vaciarCaja(JTextField caja){
        if(caja.getText().equals("Ingrese nombre") || caja.getText().equals("Ingrese localidad") || caja.getText().equals("Ingrese dirección") || caja.getText().equals("Ingrese cuit"))
        caja.setText("");
        caja.setForeground(new Color(0,0,0));
        
    }
    public void vaciarCampos(){
        txtCuit.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtLocalidad.setText("");
    }
     
     public void insertarCliente() throws SQLException{
        
       
        
        Cliente c;
        
        
        String nombre = txtNombre.getText();
        String direccion =  txtDireccion.getText();
        String localidad = txtLocalidad.getText();
        long cuit = Long.parseLong(txtCuit.getText());
               
        
        c = new Cliente(nombre, direccion, localidad, cuit);
        
        
        String sql = """
                     INSERT INTO Cliente (                                              
                                              Nombre,
                                              Direccion,
                                              Localidad,
                                              Cuit                                             
                                          ) VALUES (?,?,?,?)""";
        
        
        
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        estamento.setString(1, nombre);
        estamento.setString(2, direccion);
        estamento.setString(3, localidad);
        estamento.setLong(4, cuit);
        
        
        estamento.executeUpdate();
        estamento.close();
               
        
        
        
        
        
        
        
    }
     
     public void cargarListado() throws SQLException{
        
         
        Statement consulta;
        String sql = "SELECT * FROM \"Cliente\"  ORDER BY \"Id_cliente\" ASC";
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
        
        modelTabla.addColumn("Id");
        modelTabla.addColumn("Nombre");
        modelTabla.addColumn("Dirección");
        modelTabla.addColumn("Localidad");
        modelTabla.addColumn("Cuit");        
        
        while(respuesta.next()){
            int id = respuesta.getInt(1);
            String nombre = respuesta.getString(2);
            String direccion = respuesta.getString(3);
            String localidad = respuesta.getString(4);
            long cuit = respuesta.getLong(5);
            BigInteger cuitReal = BigInteger.valueOf(cuit);
            
            
            
            
            
           Object datos[] = {id, nombre, direccion, localidad, cuitReal};
           modelTabla.addRow(datos);
        }
        
        jTable1.setModel(modelTabla);
        consulta.close();
           
            
            
            
            
     
     }
        
    
    
   
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanelRegistro = new javax.swing.JPanel();
        lblCuit = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        lblLocalidad = new javax.swing.JLabel();
        btnRegistrarCliente = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnConsultarCuenta = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtLocalidad = new javax.swing.JTextField();
        txtCuit = new javax.swing.JTextField();
        tabla = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Cereza = new javax.swing.JLabel();
        btnCrearCuenta = new javax.swing.JPanel();
        lblCrearCuenta = new javax.swing.JLabel();
        jPanelNavegador = new javax.swing.JPanel();
        btnIngresoDatos = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnListado = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelRegistro.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCuit.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblCuit.setText("Cuit:");
        jPanelRegistro.add(lblCuit, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 310, -1, -1));

        lblNombre.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblNombre.setText("Nombre: ");
        jPanelRegistro.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, -1, -1));

        lblDireccion.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblDireccion.setText("Direccion:");
        jPanelRegistro.add(lblDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, -1, -1));

        lblLocalidad.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblLocalidad.setText("Localidad:");
        jPanelRegistro.add(lblLocalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, -1, -1));

        btnRegistrarCliente.setBackground(new java.awt.Color(171, 209, 142));

        jLabel8.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Registrar cliente");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnRegistrarClienteLayout = new javax.swing.GroupLayout(btnRegistrarCliente);
        btnRegistrarCliente.setLayout(btnRegistrarClienteLayout);
        btnRegistrarClienteLayout.setHorizontalGroup(
            btnRegistrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        btnRegistrarClienteLayout.setVerticalGroup(
            btnRegistrarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanelRegistro.add(btnRegistrarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 430, 220, 70));

        btnConsultarCuenta.setBackground(new java.awt.Color(171, 209, 142));

        jLabel9.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Consultar cuenta");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel9MousePressed(evt);
            }
        });

        javax.swing.GroupLayout btnConsultarCuentaLayout = new javax.swing.GroupLayout(btnConsultarCuenta);
        btnConsultarCuenta.setLayout(btnConsultarCuentaLayout);
        btnConsultarCuentaLayout.setHorizontalGroup(
            btnConsultarCuentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
            .addGroup(btnConsultarCuentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnConsultarCuentaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnConsultarCuentaLayout.setVerticalGroup(
            btnConsultarCuentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(btnConsultarCuentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnConsultarCuentaLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanelRegistro.add(btnConsultarCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, -1, -1));

        txtNombre.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(204, 204, 204));
        txtNombre.setText("Ingrese nombre");
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });
        txtNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtNombreMouseReleased(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreKeyPressed(evt);
            }
        });
        jPanelRegistro.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 230, -1));

        txtDireccion.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtDireccion.setForeground(new java.awt.Color(204, 204, 204));
        txtDireccion.setText("Ingrese dirección");
        txtDireccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDireccionFocusLost(evt);
            }
        });
        txtDireccion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDireccionMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtDireccionMouseReleased(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionKeyPressed(evt);
            }
        });
        jPanelRegistro.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, 230, -1));

        txtLocalidad.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtLocalidad.setForeground(new java.awt.Color(204, 204, 204));
        txtLocalidad.setText("Ingrese localidad");
        txtLocalidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLocalidadFocusLost(evt);
            }
        });
        txtLocalidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtLocalidadMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtLocalidadMouseReleased(evt);
            }
        });
        txtLocalidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLocalidadKeyPressed(evt);
            }
        });
        jPanelRegistro.add(txtLocalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 230, -1));

        txtCuit.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        txtCuit.setForeground(new java.awt.Color(204, 204, 204));
        txtCuit.setText("Ingrese cuit");
        txtCuit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCuitFocusLost(evt);
            }
        });
        txtCuit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCuitMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtCuitMouseReleased(evt);
            }
        });
        txtCuit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCuitKeyPressed(evt);
            }
        });
        jPanelRegistro.add(txtCuit, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, 240, -1));

        tabla.setForeground(new java.awt.Color(101, 147, 129));

        jTable1.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(101, 147, 129));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "Dirección", "Localidad", "Cuit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.setDragEnabled(true);
        jTable1.setFocusable(false);
        jTable1.setGridColor(new java.awt.Color(101, 147, 129));
        jTable1.setRowHeight(35);
        jTable1.setSelectionBackground(new java.awt.Color(101, 147, 129));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jTable1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTable1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tabla.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanelRegistro.add(tabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 660, 350));

        Cereza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerezaClientes2.png"))); // NOI18N
        jPanelRegistro.add(Cereza, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 230, 270, 320));

        btnCrearCuenta.setBackground(new java.awt.Color(171, 209, 142));
        btnCrearCuenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCrearCuenta.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblCrearCuenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCrearCuenta.setText("Crear cuenta");
        lblCrearCuenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCrearCuenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCrearCuentaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCrearCuentaMousePressed(evt);
            }
        });
        btnCrearCuenta.add(lblCrearCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 70));

        jPanelRegistro.add(btnCrearCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 460, -1, -1));

        jPanel2.add(jPanelRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 770, 550));

        jPanelNavegador.setBackground(new java.awt.Color(101, 147, 129));
        jPanelNavegador.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnIngresoDatos.setBackground(new java.awt.Color(171, 209, 142));

        jLabel5.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Registro de datos");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnIngresoDatosLayout = new javax.swing.GroupLayout(btnIngresoDatos);
        btnIngresoDatos.setLayout(btnIngresoDatosLayout);
        btnIngresoDatosLayout.setHorizontalGroup(
            btnIngresoDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );
        btnIngresoDatosLayout.setVerticalGroup(
            btnIngresoDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanelNavegador.add(btnIngresoDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 340, -1));

        btnListado.setBackground(new java.awt.Color(171, 209, 142));

        jLabel6.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Listado de clientes");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnListadoLayout = new javax.swing.GroupLayout(btnListado);
        btnListado.setLayout(btnListadoLayout);
        btnListadoLayout.setHorizontalGroup(
            btnListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );
        btnListadoLayout.setVerticalGroup(
            btnListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanelNavegador.add(btnListado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 340, -1));

        jPanel2.add(jPanelNavegador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 340, 550));

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

        jLabel7.setBackground(new java.awt.Color(101, 147, 129));
        jLabel7.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("X");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnCerrarLayout = new javax.swing.GroupLayout(btnCerrar);
        btnCerrar.setLayout(btnCerrarLayout);
        btnCerrarLayout.setHorizontalGroup(
            btnCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCerrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnCerrarLayout.setVerticalGroup(
            btnCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCerrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 60));

        jPanel1.setOpaque(false);
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1050, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 1050, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
      cambiarVista(false);
      botoneraAnimada(false);
       
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
       cambiarVista(true);
       botoneraAnimada(true);
       btnRegistrarCliente.requestFocus();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void txtNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMouseClicked
        vaciarCaja(txtNombre);
    }//GEN-LAST:event_txtNombreMouseClicked

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost
        validarPlaceholer();
    }//GEN-LAST:event_txtNombreFocusLost

    private void txtDireccionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusLost
       validarPlaceholer();
    }//GEN-LAST:event_txtDireccionFocusLost

    private void txtLocalidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLocalidadFocusLost
        validarPlaceholer();
    }//GEN-LAST:event_txtLocalidadFocusLost

    private void txtCuitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCuitFocusLost
        validarPlaceholer();
    }//GEN-LAST:event_txtCuitFocusLost

    private void txtDireccionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDireccionMouseClicked
       vaciarCaja(txtDireccion);
    }//GEN-LAST:event_txtDireccionMouseClicked

    private void txtLocalidadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLocalidadMouseClicked
        vaciarCaja(txtLocalidad);
    }//GEN-LAST:event_txtLocalidadMouseClicked

    private void txtCuitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCuitMouseClicked
        vaciarCaja(txtCuit);
    }//GEN-LAST:event_txtCuitMouseClicked

    private void txtNombreMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMouseReleased
        vaciarCaja(txtNombre);
    }//GEN-LAST:event_txtNombreMouseReleased

    private void txtDireccionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDireccionMouseReleased
        vaciarCaja(txtDireccion);
    }//GEN-LAST:event_txtDireccionMouseReleased

    private void txtLocalidadMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLocalidadMouseReleased
        vaciarCaja(txtLocalidad);
    }//GEN-LAST:event_txtLocalidadMouseReleased

    private void txtCuitMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCuitMouseReleased
        vaciarCaja(txtCuit);
    }//GEN-LAST:event_txtCuitMouseReleased

    private void txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyPressed
        vaciarCaja(txtNombre);
    }//GEN-LAST:event_txtNombreKeyPressed

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        vaciarCaja(txtDireccion);
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtLocalidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocalidadKeyPressed
        vaciarCaja(txtLocalidad);
    }//GEN-LAST:event_txtLocalidadKeyPressed

    private void txtCuitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCuitKeyPressed
        vaciarCaja(txtCuit);
    }//GEN-LAST:event_txtCuitKeyPressed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        try {
            insertarCliente();
            vaciarCampos();
            validarPlaceholer();
        } catch (SQLException ex) {
            Logger.getLogger(MisClientes.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "error al intentar cargar");
        }
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
       int x = evt.getXOnScreen();
       int y = evt.getYOnScreen();
       this.setLocation(x - mouseX, y - mouseY);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
     
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MousePressed
        try {
            consultarCuenta();
        } catch (SQLException ex) {
            Logger.getLogger(MisClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel9MousePressed

    private void lblCrearCuentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCrearCuentaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCrearCuentaMouseClicked

    private void lblCrearCuentaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCrearCuentaMousePressed
        try {
            crearCuenta();
        } catch (SQLException ex) {
            Logger.getLogger(MisClientes.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "error al crear cuenta");
        }
        
        
        
    }//GEN-LAST:event_lblCrearCuentaMousePressed

    private void jTable1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTable1CaretPositionChanged
     
    }//GEN-LAST:event_jTable1CaretPositionChanged

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        try {
            validerExistenciaCuenta();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "error al validar");
            Logger.getLogger(MisClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
   
    }//GEN-LAST:event_jTable1MouseEntered

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    public void crearCuenta() throws SQLException{
        double saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese un saldo para poder aperturar la cuenta corriente:"));
        int filaSeleccionada = jTable1.getSelectedRow();
        
        int id = (int) jTable1.getValueAt(filaSeleccionada, 0);
                 
                
        //insert into cuenta (saldo-id_cliente)
       
        String sql = """
                     INSERT INTO Cuenta (                                            
                                            Saldo,
                                            Id_cliente
                                                                                          
                                          ) VALUES (?,?)""";
        
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        estamento.setDouble(1, saldo);
        estamento.setInt(2, id);
      
        
        
        estamento.executeUpdate();
        estamento.close();
        JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente ya puede consultarla");
       
        
        
    }
    
    public void validerExistenciaCuenta() throws SQLException{
        int filaSeleccionada = jTable1.getSelectedRow();        
        int id = (int) jTable1.getValueAt(filaSeleccionada, 0);
        
        Statement consulta;      
        String sql = "SELECT cu.Id_cliente FROM Cuenta cu";                       
                        
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
        
        while(respuesta.next()){
           int identificador = respuesta.getInt(1);
           if(id == identificador){
               btnCrearCuenta.setVisible(false);
               lblCrearCuenta.setVisible(false);
               btnConsultarCuenta.setVisible(true);
               break;
           }
           else{
               btnCrearCuenta.setVisible(true);
               lblCrearCuenta.setVisible(true);
               btnConsultarCuenta.setVisible(false);
           }
           
        }
        consulta.close();
       
        
        
                 
    }
    
    public void consultarCuenta() throws SQLException{

        int filaSeleccionada = jTable1.getSelectedRow();

        int id = (int) jTable1.getValueAt(filaSeleccionada, 0);
        
      CuentaCorriente cc = new CuentaCorriente(id);
      cc.setVisible(true);
      this.dispose();
                        
      
       
        
        
    }
    

      
    
    



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Cereza;
    private javax.swing.JPanel btnCerrar;
    private javax.swing.JPanel btnConsultarCuenta;
    private javax.swing.JPanel btnCrearCuenta;
    private javax.swing.JPanel btnIngresoDatos;
    private javax.swing.JPanel btnListado;
    private javax.swing.JPanel btnRegistrarCliente;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelNavegador;
    private javax.swing.JPanel jPanelRegistro;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCrearCuenta;
    private javax.swing.JLabel lblCuit;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblLocalidad;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JScrollPane tabla;
    private javax.swing.JTextField txtCuit;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtLocalidad;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
