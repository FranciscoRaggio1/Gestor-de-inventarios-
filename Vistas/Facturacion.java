package Vistas;

import Modelos.Cliente;
import Modelos.Producto;
import Sql.accesoDatos;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class Facturacion extends javax.swing.JFrame {
    accesoDatos ad = new accesoDatos();
    ArrayList<Cliente> listaClientes = new ArrayList<>();
    DefaultTableModel modelo = new DefaultTableModel();
    private boolean facturaA = true;
    double acumulador = 0;
    BigDecimal montoTotal;
    
    int mouseX , mouseY;
    

   
    public Facturacion() throws SQLException {
        initComponents();
        cargarComboClientes();
        cargarComboProducto();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio unitario");
        modelo.addColumn("Importe total");
        rbtConsumidorFinal.setVisible(false);
        rbtMonotributo.setVisible(false);
        rbtNoResponsable.setVisible(false);
        rbtExento.setVisible(false);
        
        
        
    }

    public void cargarComboClientes() throws SQLException{
        Statement consulta;
        String sql = "SELECT * FROM \"Cliente\"  ORDER BY \"Id_cliente\" ASC";
        consulta = ad.getConnection().createStatement();
        ResultSet respuesta = consulta.executeQuery(sql);
        
         while(respuesta.next()){
            
            String nombre = respuesta.getString(2);
            String direccion = respuesta.getString(3);
            String localidad = respuesta.getString(4);
            int cuit = respuesta.getInt(5);
            
            Cliente c = new Cliente(nombre, direccion, localidad, cuit);
            cboClientes.addItem(c);
         }
            
        
        
         
         
    }
    
    
    public void agregarProductoAlDetalle(){
        
            
        //guardar monto en bigdecimal como acumulador con precioUnitario para reflejar el valor total en la factura
        
        
        Producto p = (Producto) cboProducto.getSelectedItem();
        
               
        
        int cantidad = Integer.parseInt(txtCantidad.getText());
        double precioUnitario = cantidad * p.getPrecio();       
        
        
        Object[] datosProducto = new Object[]{p.getCodigo(), p.getNombre(), cantidad,"$ " + p.getPrecio(),"$ " + precioUnitario};
        acumulador = acumulador + precioUnitario;
        modelo.addRow(datosProducto);
        montoTotal = BigDecimal.valueOf(acumulador);
        lstDetalle.setModel(modelo);
        
       
        
        
    }
    
    public boolean validarTipoDato(){
        String expresionReg = "[0-9]+";
        String textoDias = txtDias.getText();
        String textoMeses = txtMeses.getText();
        String textoAnos = txtAnos.getText();
        boolean validado = true;
        
        if(textoDias.matches(expresionReg))
        {
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Ingrese los dias de la fecha correctamente");
            validado = false;
        }
        
        if(textoMeses.matches(expresionReg)){
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Ingrese los meses de la fecha correctamente");
            validado = false;
        }
        if(textoAnos.matches(expresionReg)){
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Ingrese los años de la fecha correctamente");
            validado = false;
        }
        return validado;
    }
    
      public void vaciarCaja(JTextField caja){
        if(caja.getText().equals("Ingrese código de la factura") || caja.getText().equals("DD") || caja.getText().equals("MM") || caja.getText().equals("AA"))
        caja.setText("");
        caja.setForeground(new Color(0,0,0));
        
    }
      
      public void cambiarPestaña(boolean estado){
          if(estado){
              btnFacturaB.setBackground(new Color(171,209,142));
              btnFacturaA.setBackground(new Color(101,147,129));
              rbtConsumidorFinal.setVisible(true);
              rbtMonotributo.setVisible(true);
              rbtNoResponsable.setVisible(true);
              rbtExento.setVisible(true);
              facturaA = false;
          }
          else{
              btnFacturaB.setBackground(new Color(101,147,129));
              btnFacturaA.setBackground(new Color(171,209,142));
              rbtConsumidorFinal.setVisible(false);
              rbtMonotributo.setVisible(false);
              rbtNoResponsable.setVisible(false);
              rbtExento.setVisible(false);
              facturaA = true;
          }
      }
      public void validarCantidad(){
        int contador = 0;
        try {

           
            int cantidad = Integer.parseInt(txtCantidad.getText());            
            if(cantidad <= 0){
                JOptionPane.showMessageDialog(null, "Ingrese la cantidad del producto deseado");
            }
            else if(facturaA == true && contador <= 6){
                agregarProductoAlDetalle();           
                cboProducto.setSelectedIndex(0);
                txtCantidad.setText("0");
                contador++;

            }
            else if(facturaA == false && contador <= 4){
                agregarProductoAlDetalle();           
                cboProducto.setSelectedIndex(0);
                txtCantidad.setText("0");
                contador++;

            }
            else{
                JOptionPane.showMessageDialog(null, "Los items en el detalle de la factura han alcanzado la capacidad máxima permitida por la factura seleccionada (varia según sea de tipo A o B), por favor divida la operación en dos o mas facturas.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error al cargar fila en detalle");
        }
      }

 
    
    
    
    
    public void cargarComboProducto() throws SQLException{
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
            cboProducto.addItem(p);
        
        }
    }
    public void buscarPorId(){
        
            int identificador = Integer.parseInt(txtBuscarId.getText());
            for (int i = 0; i < cboProducto.getItemCount(); i++) {
            Producto p = cboProducto.getItemAt(i);
            int id = p.getCodigo();
            if(id == identificador){
                cboProducto.setSelectedItem(p);
                break;
            }
            
            
            
        }
    }
    
    
    public void imprimirFactura(int operacion) throws IOException{
         
         Cliente c = (Cliente) cboClientes.getSelectedItem();
         
         if(facturaA == true){
           String imagePath = "C:\\Users\\Tango\\Documents\\NetBeansProjects\\GestiosVerduleria\\src\\Imagenes\\f-a.jpg";
           


          try {
              PDDocument document = new PDDocument();
              PDPage page = new PDPage(PDRectangle.A4);
              document.addPage(page);

              PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
              float pageWidth = PDRectangle.A4.getWidth();
              float pageHeight = PDRectangle.A4.getHeight();

              float imageWidth = image.getWidth();
              float imageHeight = image.getHeight();

              if (imageWidth > pageWidth || imageHeight > pageHeight) {
                  // Escalar la imagen para ajustarla dentro de la página
                  float scaleFactor = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);
                  imageWidth *= scaleFactor;
                  imageHeight *= scaleFactor;
              }

              float x = (pageWidth - imageWidth) / 2; // Centrar horizontalmente la imagen
              float y = (pageHeight - imageHeight) / 2; // Centrar verticalmente la imagen

              PDPageContentStream contentStream = new PDPageContentStream(document, page);
              contentStream.drawImage(image, x, y, imageWidth, imageHeight);


              //texto direccion

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(120, 575); // Posición X: 100, Posición Y: 700
              contentStream.showText(c.getDireccion());
              contentStream.endText();

              //texto destinatario

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(120, 610); // Posición X: 100, Posición Y: 700
              contentStream.showText(c.getNombre());
              contentStream.endText();

              //texto destinatario

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(370, 575); // Posición X: 100, Posición Y: 700
              contentStream.showText(c.getLocalidad());
              contentStream.endText();

              //texto cuit

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(435, 490); // Posición X: 100, Posición Y: 700
              contentStream.showText(String.valueOf(c.getCuit()));
              contentStream.endText();

              //texto fecha dias
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(100, 715); // Posición X: 100, Posición Y: 700
              contentStream.showText(txtDias.getText());
              contentStream.endText();

              //texto fecha meses
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(160, 715); // Posición X: 100, Posición Y: 700
              contentStream.showText(txtMeses.getText());
              contentStream.endText();

              //texto fecha años
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(210, 715); // Posición X: 100, Posición Y: 700
              contentStream.showText(txtAnos.getText());
              contentStream.endText();
              
              //texto monto total
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(450, 25); // Posición X: 100, Posición Y: 700
              contentStream.showText("$ " + montoTotal.toString());
              contentStream.endText();

              

              //check condicion de venta

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              if(rbtContado.isSelected()){
                  contentStream.newLineAtOffset(280, 485); // Posición X: 100, Posición Y: 700
              }
              else{
                  contentStream.newLineAtOffset(360, 485); // Posición X: 100, Posición Y: 700
              }

              contentStream.showText("X");
              contentStream.endText();

              //detalle      

               addTableToPDF(contentStream, 20, 370, 550, lstDetalle);







              contentStream.close();
              
                       // Crear un cuadro de diálogo para que el usuario elija la ubicación del archivo
               JFileChooser fileChooser = new JFileChooser();
               int userSelection = fileChooser.showSaveDialog(null);

               if (userSelection == JFileChooser.APPROVE_OPTION) {
                   File fileToSave = fileChooser.getSelectedFile();
                   String filePath = fileToSave.getAbsolutePath();
                   if(operacion == 1){//si la operacion es de abrir archivo el programa establece extension pdf y lo abre a la par que lo guarda depende del parametro que se pase al llamar a la funcion
                       // String filePath = fileToSave.getAbsolutePath();
                   // Guardar el documento en la ubicación seleccionada por el usuario  
                   // Agregar la extensión ".pdf" al nombre del archivo si no tiene ya una extensión
                     if (!filePath.toLowerCase().endsWith(".pdf")) {
                    fileToSave = new File(filePath + ".pdf");
                    }
                    document.save(fileToSave);
                    Desktop.getDesktop().open(fileToSave);
                       
                   }                   
                  if(operacion == 2){//si el paramatro es dos la operacion a realizar sera solo el guardado, sin apertura de archivo
                     if (!filePath.toLowerCase().endsWith(".pdf")) {
                    fileToSave = new File(filePath + ".pdf");
                    }
                      document.save(fileToSave);
                   
                  }
                   
               }
               

              //document.save(outputPDF);
              document.close();

              System.out.println("El archivo PDF se ha generado correctamente.");
          } catch (IOException e) {
              e.printStackTrace();
          }

        }//cierre if
         else{
             
           String imagePath = "C:\\Users\\Tango\\Documents\\NetBeansProjects\\GestiosVerduleria\\src\\Imagenes\\factura b.jpg";
           String outputPDF = "C:\\Users\\Tango\\Documents\\NetBeansProjects\\GestiosVerduleria\\prueba-dificil-facturaB.pdf";


           try {
              PDDocument document = new PDDocument();
              PDPage page = new PDPage(PDRectangle.A4);
              document.addPage(page);

              PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
              float pageWidth = PDRectangle.A4.getWidth();
              float pageHeight = PDRectangle.A4.getHeight();

              float imageWidth = image.getWidth();
              float imageHeight = image.getHeight();

              if (imageWidth > pageWidth || imageHeight > pageHeight) {
                  // Escalar la imagen para ajustarla dentro de la página
                  float scaleFactor = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);
                  imageWidth *= scaleFactor;
                  imageHeight *= scaleFactor;
              }

              float x = (pageWidth - imageWidth) / 2; // Centrar horizontalmente la imagen
              float y = (pageHeight - imageHeight) / 2; // Centrar verticalmente la imagen

              PDPageContentStream contentStream = new PDPageContentStream(document, page);
              contentStream.drawImage(image, x, y, imageWidth, imageHeight);


              //texto direccion

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(120, 575); // Posición X: 100, Posición Y: 700
              contentStream.showText(c.getDireccion());
              contentStream.endText();

              //texto destinatario

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(120, 610); // Posición X: 100, Posición Y: 700
              contentStream.showText(c.getNombre());
              contentStream.endText();

              //texto destinatario

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(370, 575); // Posición X: 100, Posición Y: 700
              contentStream.showText(c.getLocalidad());
              contentStream.endText();

              //texto cuit

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
              contentStream.newLineAtOffset(435, 490); // Posición X: 100, Posición Y: 700
              contentStream.showText(String.valueOf(c.getCuit()));
              contentStream.endText();

              //texto fecha dias
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(100, 715); // Posición X: 100, Posición Y: 700
              contentStream.showText(txtDias.getText());
              contentStream.endText();

              //texto fecha meses
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(160, 715); // Posición X: 100, Posición Y: 700
              contentStream.showText(txtMeses.getText());
              contentStream.endText();

              //texto fecha años
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(210, 715); // Posición X: 100, Posición Y: 700
              contentStream.showText(txtAnos.getText());
              contentStream.endText();
              
              //texto monto total
              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              contentStream.newLineAtOffset(450, 25); // Posición X: 100, Posición Y: 700
              contentStream.showText("$ " + montoTotal.toString());
              contentStream.endText();

        

              //check condicion de venta

              contentStream.beginText();
              contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
              if(rbtContado.isSelected()){
                  contentStream.newLineAtOffset(280, 485); // Posición X: 100, Posición Y: 700
              }
              else{
                  contentStream.newLineAtOffset(360, 485); // Posición X: 100, Posición Y: 700
              }

              contentStream.showText("X");
              contentStream.endText();

              //detalle      

               addTableToPDF(contentStream, 20, 270, 550, lstDetalle);







              contentStream.close();
              
           

              document.save(outputPDF);
              document.close();

              System.out.println("El archivo PDF se ha generado correctamente.");
          } catch (IOException e) {
              e.printStackTrace();
          }

             
        }
     
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboProducto = new javax.swing.JComboBox<>();
        btnAgregarRegistro = new javax.swing.JPanel();
        lblAgregarRegistro = new javax.swing.JLabel();
        txtBuscarId = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btnBuscarPorId = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstDetalle = new javax.swing.JTable();
        btnCerrar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnFacturaB = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnFacturaA = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboClientes = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        rbtCuentaCorriente = new javax.swing.JRadioButton();
        rbtContado = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtAnos = new javax.swing.JTextField();
        txtDias = new javax.swing.JTextField();
        txtMeses = new javax.swing.JTextField();
        lblCondicionIva = new javax.swing.JLabel();
        rbtConsumidorFinal = new javax.swing.JRadioButton();
        rbtMonotributo = new javax.swing.JRadioButton();
        rbtExento = new javax.swing.JRadioButton();
        rbtNoResponsable = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(153, 255, 153));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Guardar factura");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel6MousePressed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(153, 255, 153));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Guardar factura");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 800, 160, 60));

        jPanel4.setBackground(new java.awt.Color(101, 147, 129));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Agregar nuevo producto");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 203, 27));

        cboProducto.setFont(new java.awt.Font("Roboto Thin", 0, 18)); // NOI18N
        jPanel4.add(cboProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 260, 50));

        btnAgregarRegistro.setBackground(new java.awt.Color(171, 209, 142));

        lblAgregarRegistro.setFont(new java.awt.Font("Roboto Black", 1, 18)); // NOI18N
        lblAgregarRegistro.setForeground(new java.awt.Color(255, 255, 255));
        lblAgregarRegistro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAgregarRegistro.setText("+");
        lblAgregarRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAgregarRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblAgregarRegistroMousePressed(evt);
            }
        });

        javax.swing.GroupLayout btnAgregarRegistroLayout = new javax.swing.GroupLayout(btnAgregarRegistro);
        btnAgregarRegistro.setLayout(btnAgregarRegistroLayout);
        btnAgregarRegistroLayout.setHorizontalGroup(
            btnAgregarRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAgregarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        btnAgregarRegistroLayout.setVerticalGroup(
            btnAgregarRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAgregarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jPanel4.add(btnAgregarRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, 50, 50));

        txtBuscarId.setBackground(new java.awt.Color(204, 204, 204));
        txtBuscarId.setFont(new java.awt.Font("Roboto Thin", 1, 18)); // NOI18N
        txtBuscarId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(txtBuscarId, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 60, 70, 50));

        jLabel15.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Listado de productos");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 150, -1));

        jLabel16.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Buscar por código");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 130, -1));

        jLabel17.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Cantidad");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 130, -1));

        txtCantidad.setBackground(new java.awt.Color(204, 204, 204));
        txtCantidad.setFont(new java.awt.Font("Roboto Thin", 1, 18)); // NOI18N
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.setText("0");
        jPanel4.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 130, 190, 50));

        btnBuscarPorId.setBackground(new java.awt.Color(171, 209, 142));

        jLabel11.setFont(new java.awt.Font("Roboto Thin", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Buscar");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel11MousePressed(evt);
            }
        });

        javax.swing.GroupLayout btnBuscarPorIdLayout = new javax.swing.GroupLayout(btnBuscarPorId);
        btnBuscarPorId.setLayout(btnBuscarPorIdLayout);
        btnBuscarPorIdLayout.setHorizontalGroup(
            btnBuscarPorIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        btnBuscarPorIdLayout.setVerticalGroup(
            btnBuscarPorIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jPanel4.add(btnBuscarPorId, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 60, -1, 50));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, 740, 220));

        lstDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo producto", "Nombre producto", "Cantidad", "Precio unitario", "Importe total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(lstDetalle);
        if (lstDetalle.getColumnModel().getColumnCount() > 0) {
            lstDetalle.getColumnModel().getColumn(0).setResizable(false);
            lstDetalle.getColumnModel().getColumn(1).setResizable(false);
            lstDetalle.getColumnModel().getColumn(2).setResizable(false);
            lstDetalle.getColumnModel().getColumn(3).setResizable(false);
            lstDetalle.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 650, 740, 130));

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

        jLabel3.setBackground(new java.awt.Color(101, 147, 129));
        jLabel3.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("X");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel3MousePressed(evt);
            }
        });

        javax.swing.GroupLayout btnCerrarLayout = new javax.swing.GroupLayout(btnCerrar);
        btnCerrar.setLayout(btnCerrarLayout);
        btnCerrarLayout.setHorizontalGroup(
            btnCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCerrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnCerrarLayout.setVerticalGroup(
            btnCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCerrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 60));

        jPanel3.setBackground(new java.awt.Color(101, 147, 129));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnFacturaB.setBackground(new java.awt.Color(101, 147, 129));

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Factura \"B\"");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel12MousePressed(evt);
            }
        });

        javax.swing.GroupLayout btnFacturaBLayout = new javax.swing.GroupLayout(btnFacturaB);
        btnFacturaB.setLayout(btnFacturaBLayout);
        btnFacturaBLayout.setHorizontalGroup(
            btnFacturaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFacturaBLayout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        btnFacturaBLayout.setVerticalGroup(
            btnFacturaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnFacturaBLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(btnFacturaB, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 350, 120));

        btnFacturaA.setBackground(new java.awt.Color(171, 209, 142));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Factura \"A\"");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout btnFacturaALayout = new javax.swing.GroupLayout(btnFacturaA);
        btnFacturaA.setLayout(btnFacturaALayout);
        btnFacturaALayout.setHorizontalGroup(
            btnFacturaALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFacturaALayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        btnFacturaALayout.setVerticalGroup(
            btnFacturaALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );

        jPanel3.add(btnFacturaA, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 350, 120));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 350, 820));

        jLabel4.setFont(new java.awt.Font("Roboto Thin", 0, 18)); // NOI18N
        jLabel4.setText("Seleccione un cliente:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 200, 180, -1));

        jLabel5.setFont(new java.awt.Font("Roboto Thin", 0, 18)); // NOI18N
        jLabel5.setText("Detalle de la factura");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 620, -1, -1));

        cboClientes.setFont(new java.awt.Font("Roboto Thin", 0, 18)); // NOI18N
        jPanel1.add(cboClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 190, 360, 40));

        jPanel7.setBackground(new java.awt.Color(153, 255, 153));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Imprimir factura");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel9MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 800, 160, -1));

        buttonGroup1.add(rbtCuentaCorriente);
        rbtCuentaCorriente.setText("Cuenta corriente");
        jPanel1.add(rbtCuentaCorriente, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 510, -1, -1));

        buttonGroup1.add(rbtContado);
        rbtContado.setSelected(true);
        rbtContado.setText("Contado");
        rbtContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtContadoActionPerformed(evt);
            }
        });
        jPanel1.add(rbtContado, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 510, -1, -1));

        jLabel13.setText("Condición de venta");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, -1, -1));

        jLabel14.setFont(new java.awt.Font("Roboto Thin", 0, 18)); // NOI18N
        jLabel14.setText("Fecha:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, -1, -1));

        txtAnos.setForeground(new java.awt.Color(204, 204, 204));
        txtAnos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnos.setText("AA");
        txtAnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtAnosMousePressed(evt);
            }
        });
        jPanel1.add(txtAnos, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 130, 50, 40));

        txtDias.setForeground(new java.awt.Color(204, 204, 204));
        txtDias.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDias.setText("DD");
        txtDias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtDiasMousePressed(evt);
            }
        });
        jPanel1.add(txtDias, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 130, 50, 40));

        txtMeses.setForeground(new java.awt.Color(204, 204, 204));
        txtMeses.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMeses.setText("MM");
        txtMeses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtMesesMousePressed(evt);
            }
        });
        jPanel1.add(txtMeses, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 130, 50, 40));

        lblCondicionIva.setText("Condición frente al iva:");
        jPanel1.add(lblCondicionIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 540, -1, -1));

        buttonGroup2.add(rbtConsumidorFinal);
        rbtConsumidorFinal.setSelected(true);
        rbtConsumidorFinal.setText("Consumidor final");
        rbtConsumidorFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtConsumidorFinalActionPerformed(evt);
            }
        });
        jPanel1.add(rbtConsumidorFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 580, -1, -1));

        buttonGroup2.add(rbtMonotributo);
        rbtMonotributo.setText("Monotributo");
        jPanel1.add(rbtMonotributo, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 580, -1, -1));

        buttonGroup2.add(rbtExento);
        rbtExento.setText("Exento");
        jPanel1.add(rbtExento, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 580, -1, -1));

        buttonGroup2.add(rbtNoResponsable);
        rbtNoResponsable.setText("No responsable");
        jPanel1.add(rbtNoResponsable, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 580, -1, -1));

        jPanel9.setOpaque(false);
        jPanel9.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel9MouseDragged(evt);
            }
        });
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel9MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1320, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 1320, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 878, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        this.dispose();
    }//GEN-LAST:event_btnCerrarMousePressed

    private void lblAgregarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAgregarRegistroMousePressed
        validarCantidad();
       
        
    }//GEN-LAST:event_lblAgregarRegistroMousePressed

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed
           try {
            if(validarTipoDato()){
                imprimirFactura(2);
            }
            else{
               JOptionPane.showMessageDialog(null, "Ocurrió un error. Por favor, inténtalo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
               
        } catch (IOException ex) {
            Logger.getLogger(Facturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel6MousePressed

    private void rbtContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtContadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtContadoActionPerformed

    private void txtDiasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiasMousePressed
       //txtDias.setForeground(new Color(0,0,0));
       vaciarCaja(txtDias);
    }//GEN-LAST:event_txtDiasMousePressed

    private void txtMesesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMesesMousePressed
       vaciarCaja(txtMeses);
    }//GEN-LAST:event_txtMesesMousePressed

    private void txtAnosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnosMousePressed
        vaciarCaja(txtAnos);
    }//GEN-LAST:event_txtAnosMousePressed

    private void jLabel12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MousePressed
        cambiarPestaña(true);
    }//GEN-LAST:event_jLabel12MousePressed

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        cambiarPestaña(false);
    }//GEN-LAST:event_jLabel2MousePressed

    private void rbtConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtConsumidorFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtConsumidorFinalActionPerformed

    private void jPanel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MousePressed
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_jPanel9MousePressed

    private void jPanel9MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - mouseX, y - mouseY);
    }//GEN-LAST:event_jPanel9MouseDragged

    private void jLabel11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MousePressed
        buscarPorId();
    }//GEN-LAST:event_jLabel11MousePressed

    private void jLabel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MousePressed
              try {
            if(validarTipoDato()){
                imprimirFactura(1);
            }
            else{
               JOptionPane.showMessageDialog(null, "Ocurrió un error. Por favor, inténtalo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
               
        } catch (IOException ex) {
            Logger.getLogger(Facturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel9MousePressed

    private void jLabel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MousePressed
        Inicio vI = new Inicio();
        vI.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel3MousePressed

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        btnCerrar.setBackground(Color.red);
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
         btnCerrar.setBackground( new Color(101,147,129) );
    }//GEN-LAST:event_jLabel3MouseExited


    
public static void addTableToPDF(PDPageContentStream contentStream, float x, float y, float tableWidth, JTable jTable) throws IOException {
        float margin = 0;//10//
        float rowHeight = 30;//20//tamaño de la fila
        float cellMargin = 0;//5
        float fontSize = 9;//tamaño letra en tabla detalle

        float yStart = y;
        float tableHeight = jTable.getRowCount() * rowHeight;

        String[] headers = getTableHeaders(jTable);
        String[][] data = getTableData(jTable);

        // Dibujar encabezados
        drawRow(contentStream, x, y, tableWidth, rowHeight, headers, new Color(101,147,129), Color.WHITE, fontSize, true);

         //Dibujar celdas
        y -= rowHeight;
        for (String[] rowData : data) {
           drawRow(contentStream, x, y, tableWidth, rowHeight, rowData, new Color(101,147,129), Color.WHITE, fontSize, false);
            y -= rowHeight;
        }

        //Dibujar borde de la tabla
        //contentStream.setStrokingColor(Color.BLACK);
        //contentStream.setLineWidth(1f);
        //contentStream.addRect(x, y, tableWidth, tableHeight + rowHeight);
        //contentStream.stroke();
    }

    public static void drawRow(PDPageContentStream contentStream, float x, float y, float width, float height, String[] rowData,
                               Color borderColor, Color backgroundColor, float fontSize, boolean isHeader) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
        contentStream.setNonStrokingColor(backgroundColor);
        contentStream.setStrokingColor(borderColor);

        contentStream.addRect(x, y, width, height);
        contentStream.fill();

        float cellMargin = 5;//5
        float cellWidth = width / (float) rowData.length;
        float cellHeight = height - (2 * cellMargin);

        contentStream.setNonStrokingColor(borderColor);
        contentStream.setLineWidth(1f);

        float textX = x + cellMargin;
        float textY = y + cellMargin + ((height - fontSize) / 2);

        for (String text : rowData) {
            contentStream.beginText();
            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(text);
            contentStream.endText();

            contentStream.addRect(textX, y + cellMargin, cellWidth, cellHeight);
            contentStream.stroke();

            textX += cellWidth;
        }
    }

    public static String[] getTableHeaders(JTable jTable) {
        String[] headers = new String[jTable.getColumnCount()];
        for (int i = 0; i < jTable.getColumnCount(); i++) {
            headers[i] = "   " + jTable.getColumnName(i);
        }
        return headers;
    }

    public static String[][] getTableData(JTable jTable) {
        String[][] data = new String[jTable.getRowCount()][jTable.getColumnCount()];
        for (int i = 0; i < jTable.getRowCount(); i++) {
            for (int j = 0; j < jTable.getColumnCount(); j++) {
                data[i][j] = "   " + jTable.getValueAt(i, j).toString();
            }
        }
        return data;
    }

    public static JTable createSampleTable() {
        String[] columnNames = {"Columna 1", "Columna 2", "Columna 3"};
        String[][] data = {
                {"Dato 1", "Dato 2", "Dato 3"},
                {"Dato 4", "Dato 5", "Dato 6"},
                {"Dato 7", "Dato 8", "Dato 9"}
        };
        return new JTable(data, columnNames);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnAgregarRegistro;
    private javax.swing.JPanel btnBuscarPorId;
    private javax.swing.JPanel btnCerrar;
    private javax.swing.JPanel btnFacturaA;
    private javax.swing.JPanel btnFacturaB;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<Cliente> cboClientes;
    private javax.swing.JComboBox<Producto> cboProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAgregarRegistro;
    private javax.swing.JLabel lblCondicionIva;
    private javax.swing.JTable lstDetalle;
    private javax.swing.JRadioButton rbtConsumidorFinal;
    private javax.swing.JRadioButton rbtContado;
    private javax.swing.JRadioButton rbtCuentaCorriente;
    private javax.swing.JRadioButton rbtExento;
    private javax.swing.JRadioButton rbtMonotributo;
    private javax.swing.JRadioButton rbtNoResponsable;
    private javax.swing.JTextField txtAnos;
    private javax.swing.JTextField txtBuscarId;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDias;
    private javax.swing.JTextField txtMeses;
    // End of variables declaration//GEN-END:variables
}

