package Vistas;

import Sql.accesoDatos;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class CuentaCorriente extends javax.swing.JFrame {
    accesoDatos ad = new accesoDatos();
    DefaultTableModel modelo = new DefaultTableModel();

    public CuentaCorriente(int id) throws SQLException{
        initComponents();
        
        cargarDatosCunta(id);
        reloj();
        modelo.addColumn("Concepto");
        modelo.addColumn("Debe");
        modelo.addColumn("Haber");
        modelo.addColumn("Fecha");
        JtableCuentaCorriente.setModel(modelo);
        consultarOperaciones(id);
        cargarCombo();
        lblIdentificadorCuenta.setText(String.valueOf(id));
        
    }
    //agregar elemnto a la base datos cargando el combo y jtext(insert)
    //luego realizar un consulta select y cargar los datos obtenidos de la table cuenta x operacion donde el id de cuenta sea igual al id recibido por parametro y gloria(select)
    
    public void cargarCombo() throws SQLException{
        Statement consulta;      
        String sql = "SELECT * FROM Operaciones";
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
         while(respuesta.next()){
            
            int id = respuesta.getInt(1);
            String nombre = respuesta.getString(2);
            
            cboOperaciones.addItem(nombre);
        
        }
        consulta.close();
    }
    
    public void validarPlaceholer(){
        if(txtDebe.getText().isEmpty()){
            txtDebe.setText("Ingrese debe");
            txtDebe.setForeground(new Color(204,204,204));
        }
        if(txtHaber.getText().isEmpty()){
            txtHaber.setText("Ingrese haber");
            txtHaber.setForeground(new Color(204,204,204));
        }
      
      
    }
    
    public void insertarOperacion(int id) throws SQLException{
        
        BigDecimal debe = null;
        BigDecimal haber = null;
        LocalDate fechaHoy = LocalDate.now();
        
        int indice = cboOperaciones.getSelectedIndex() + 1;
        
        if(!txtDebe.getText().equals("Ingrese debe")){
            double debeInterno = Double.parseDouble(txtDebe.getText());
            debe = BigDecimal.valueOf(debeInterno);
        }
        
        if(!txtHaber.getText().equals("Ingrese haber")){
            double haberInterno = Double.parseDouble(txtHaber.getText());
            haber = BigDecimal.valueOf(haberInterno);
        }
           
        
        
            
       
        
        
        
        String sql = "INSERT INTO CuentaXOperaciones (Id_cuenta, Id_operacion, Debe, Haber, Fecha) VALUES(?,?,?,?,?)";
        
        
        
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        estamento.setInt(1, id);
        estamento.setInt(2, indice);
        estamento.setBigDecimal(3, debe);
        estamento.setBigDecimal(4, haber);
        estamento.setString(5, fechaHoy.toString());
        
        
        estamento.executeUpdate();
        estamento.close();
    }
    
    public void consultarOperaciones(int id) throws SQLException{
        Statement consulta;   
        
        
        String sql = "SELECT cxo.Debe, cxo.Haber, cxo.Fecha, o.Nombre \n" +
                     "FROM CuentaXOperaciones cxo \n" +
                     "JOIN Operaciones o ON cxo.Id_operacion = o.Id_operacion\n" +
                     "WHERE cxo.Id_cuenta =" + id;
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
         while(respuesta.next()){
             
             BigDecimal debe = respuesta.getBigDecimal(1);
             if(debe == null){
                debe = new BigDecimal("0");
             }
             BigDecimal haber = respuesta.getBigDecimal(2);
             if(haber == null){
                haber = new BigDecimal("0");
             }
             String fecha = respuesta.getString(3);
             String nombre = respuesta.getString(4);
              Object[] datos = new Object[]{nombre,debe,haber, fecha};
              
              modelo.addRow(datos);
             
             
         }
         consulta.close();
        
    }
    public void actualizarSaldo() throws SQLException{
        //bien ta funcionando ahora solo queda recuperar ese signo de pesos sugerencia: String original = "Hello, world!";     String sinComas = original.replace(",", ""); 
        BigDecimal resultadoDebe = new BigDecimal("0");
        BigDecimal resultadoHaber = new BigDecimal("0");
        BigDecimal balanceFinal = new BigDecimal("0");
        for (int i = 0; i < modelo.getRowCount(); i++) {
            BigDecimal debe = (BigDecimal) JtableCuentaCorriente.getValueAt(i, 1);//signo de pesos que devuelve da error carajo BigDecimal.valueOf(Double.parseDouble(valorCadena))
            resultadoDebe = resultadoDebe.add(debe);
        }
        for (int i = 0; i < modelo.getRowCount(); i++) {
            BigDecimal haber = (BigDecimal) JtableCuentaCorriente.getValueAt(i, 2);
            resultadoHaber = resultadoHaber.add(haber);
        }
        
        balanceFinal = resultadoDebe.subtract(resultadoHaber);
        lblSaldo.setText(balanceFinal.toString());
        
      //ahora solo falta el update
      
       String sql = "UPDATE Cuenta SET Saldo = ?  WHERE Id_cuenta = ?";
       
        PreparedStatement estamento = ad.getConnection().prepareStatement(sql);
        
        estamento.setBigDecimal(1, balanceFinal);
        estamento.setInt(2, Integer.parseInt(lblIdentificadorCuenta.getText()));
        
       
        
        estamento.executeUpdate();        
        estamento.close();
        
        int comparacion = balanceFinal.compareTo(BigDecimal.ZERO);
        if(comparacion > 0){
            
            
        }
        else{
            JOptionPane.showMessageDialog(null, "El saldo de esta cuenta es negativo: " + balanceFinal);
            
        }
        
        
    }
    
    public void vaciarCaja(JTextField caja){
        if(caja.getText().equals("Ingrese debe") || caja.getText().equals("Ingrese haber"))
        caja.setText("");
        caja.setForeground(Color.BLACK);
        
    }
    public void cargarDatosCunta(int id) throws SQLException{
        
        Statement consulta;      
        String sql = "SELECT c.Nombre, c.Direccion, c.Localidad, c.Cuit, cu.Saldo " +
                         "FROM Cliente c " +
                         "JOIN Cuenta cu ON c.Id_cliente = cu.Id_cliente " +
                         "WHERE c.Id_cliente = " + id;
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
        
        while(respuesta.next()){
            
            String nombre = respuesta.getString(1);
            String direccion = respuesta.getString(2);
            String localidad = respuesta.getString(3);
            long cuit = respuesta.getLong(4);
            BigInteger cuitReal = BigInteger.valueOf(cuit);
            BigDecimal saldo = respuesta.getBigDecimal(5);
            
            lblSaldo.setText(saldo.toString());
            lblTitular.setText(nombre);
            lblEstado.setText("Operativa");
            
            
               
        }
        
        
        
       
    }
   public void reloj(){
        ActionListener updateDateTime = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Obtener la fecha y hora actual
                    Date fechaHoraActual = new Date();

                    // Formatear la fecha y hora en el formato deseado (por ejemplo, "dd/MM/yyyy HH:mm:ss")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String fechaHoraTexto = sdf.format(fechaHoraActual);

                    // Actualizar el contenido del JLabel
                    lblFechaHoy.setText(fechaHoraTexto);
                }
            };

            // Crear un javax.swing.Timer para actualizar el JLabel cada segundo (1000 milisegundos)
            Timer timer = new Timer(1000, updateDateTime);
            timer.start();
            
       
   }
          
        
    


   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblFechaHoy = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblSaldo = new javax.swing.JLabel();
        lblTitular = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblIdentificadorCuenta = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JtableCuentaCorriente = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btnAñadirFila = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnQuitarFila = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cboOperaciones = new javax.swing.JComboBox<>();
        txtDebe = new javax.swing.JTextField();
        txtHaber = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFechaHoy.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        jPanel1.add(lblFechaHoy, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 250, 30));

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

        jPanel2.setBackground(new java.awt.Color(101, 147, 129));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(101, 147, 129));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Saldo:");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, 20));

        jLabel9.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Titular:");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        lblSaldo.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lblSaldo.setForeground(new java.awt.Color(255, 255, 255));
        lblSaldo.setText("saldo");
        jPanel4.add(lblSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 270, -1));

        lblTitular.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lblTitular.setForeground(new java.awt.Color(255, 255, 255));
        lblTitular.setText("titular");
        jPanel4.add(lblTitular, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 290, 30));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/membreteCuentaCorriente.jpg"))); // NOI18N
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 360, 110));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("$");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 30, 20));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 660, 160));

        jPanel5.setBackground(new java.awt.Color(101, 147, 129));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Datos de la cuenta");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 50));

        lblEstado.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setText("estado");
        jPanel5.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 290, 30));

        jLabel12.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Estado:");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel13.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Identificador:");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        lblIdentificadorCuenta.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lblIdentificadorCuenta.setForeground(new java.awt.Color(255, 255, 255));
        lblIdentificadorCuenta.setText("identificador");
        jPanel5.add(lblIdentificadorCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 290, 30));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 30, 510, 160));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 1310, 210));

        JtableCuentaCorriente.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        JtableCuentaCorriente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(JtableCuentaCorriente);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 1180, 170));

        jLabel7.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel7.setText("Fecha:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        btnAñadirFila.setBackground(new java.awt.Color(171, 209, 142));
        btnAñadirFila.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("+");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });
        btnAñadirFila.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        jPanel1.add(btnAñadirFila, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 450, 50, 50));

        btnQuitarFila.setBackground(new java.awt.Color(171, 209, 142));
        btnQuitarFila.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("-");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuitarFila.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        jPanel1.add(btnQuitarFila, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 450, 50, 50));

        jLabel8.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        jLabel8.setText("Añadir/Quitar filas");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 510, 130, 20));

        jPanel3.setBackground(new java.awt.Color(171, 209, 142));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Actualizar saldo");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 80));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 450, 250, 80));

        cboOperaciones.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        jPanel1.add(cboOperaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 170, 280, 50));

        txtDebe.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        txtDebe.setForeground(new java.awt.Color(204, 204, 204));
        txtDebe.setText("Ingrese debe");
        txtDebe.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebeFocusLost(evt);
            }
        });
        txtDebe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDebeMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtDebeMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtDebeMouseReleased(evt);
            }
        });
        txtDebe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDebeKeyPressed(evt);
            }
        });
        jPanel1.add(txtDebe, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 210, 50));

        txtHaber.setFont(new java.awt.Font("Roboto Black", 1, 14)); // NOI18N
        txtHaber.setForeground(new java.awt.Color(204, 204, 204));
        txtHaber.setText("Ingrese haber");
        txtHaber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHaberFocusLost(evt);
            }
        });
        txtHaber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHaberMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtHaberMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtHaberMouseReleased(evt);
            }
        });
        txtHaber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHaberKeyPressed(evt);
            }
        });
        jPanel1.add(txtHaber, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 170, 210, 50));

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

    private void btnCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMouseEntered
        btnCerrar.setBackground(Color.red);
    }//GEN-LAST:event_btnCerrarMouseEntered

    private void btnCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMouseExited
        btnCerrar.setBackground( new Color(101,147,129) );
    }//GEN-LAST:event_btnCerrarMouseExited

    private void btnCerrarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMousePressed
       Inicio vi = new Inicio();
       vi.setVisible(true);
       this.dispose();
    }//GEN-LAST:event_btnCerrarMousePressed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        try {
            modelo.setRowCount(0);
            insertarOperacion(Integer.parseInt(lblIdentificadorCuenta.getText()));
        } catch (SQLException ex) {
            Logger.getLogger(CuentaCorriente.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "error al insertar operacion en la bd");
        }
        
    }//GEN-LAST:event_jLabel1MousePressed

    private void txtDebeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebeFocusLost
       validarPlaceholer();
    }//GEN-LAST:event_txtDebeFocusLost

    private void txtHaberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHaberFocusLost
        validarPlaceholer();
    }//GEN-LAST:event_txtHaberFocusLost

    private void txtDebeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDebeMousePressed
        vaciarCaja(txtDebe);
    }//GEN-LAST:event_txtDebeMousePressed

    private void txtHaberMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHaberMousePressed
        vaciarCaja(txtHaber);
    }//GEN-LAST:event_txtHaberMousePressed

    private void txtDebeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDebeKeyPressed
        vaciarCaja(txtDebe);
    }//GEN-LAST:event_txtDebeKeyPressed

    private void txtHaberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHaberKeyPressed
        vaciarCaja(txtHaber);
    }//GEN-LAST:event_txtHaberKeyPressed

    private void txtHaberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHaberMouseClicked
        vaciarCaja(txtHaber);
    }//GEN-LAST:event_txtHaberMouseClicked

    private void txtDebeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDebeMouseClicked
        vaciarCaja(txtDebe);
    }//GEN-LAST:event_txtDebeMouseClicked

    private void txtDebeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDebeMouseReleased
        vaciarCaja(txtDebe);
    }//GEN-LAST:event_txtDebeMouseReleased

    private void txtHaberMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHaberMouseReleased
        vaciarCaja(txtHaber);
    }//GEN-LAST:event_txtHaberMouseReleased

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        try {
            consultarOperaciones(Integer.parseInt(lblIdentificadorCuenta.getText()));
        } catch (SQLException ex) {
            Logger.getLogger(CuentaCorriente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        try {
            actualizarSaldo();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaCorriente.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "error al actualizar");
        }
    }//GEN-LAST:event_jLabel11MouseClicked



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JtableCuentaCorriente;
    private javax.swing.JPanel btnAñadirFila;
    private javax.swing.JPanel btnCerrar;
    private javax.swing.JPanel btnQuitarFila;
    private javax.swing.JComboBox<String> cboOperaciones;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFechaHoy;
    private javax.swing.JLabel lblIdentificadorCuenta;
    private javax.swing.JLabel lblSaldo;
    private javax.swing.JLabel lblTitular;
    private javax.swing.JTextField txtDebe;
    private javax.swing.JTextField txtHaber;
    // End of variables declaration//GEN-END:variables
}
