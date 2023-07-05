package tfg;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MiVentana extends JFrame implements ActionListener {

  private JButton boton;
  private JLabel labelTexto;
  private JTextField cajaTexto;
  private JLabel labelFecha;
  private JTextField cajaFecha;
  private static JTextArea areaTexto;
  private static JTextField areaPuntuacion;


  public MiVentana() {
    super("Mi ventana");

    labelTexto = new JLabel("Tweet:");
    labelTexto.setBounds(10, 10, 45, 20);

    cajaTexto = new JTextField(20);
    cajaTexto.setBounds(55, 10, 300, 20);

    labelFecha = new JLabel("Fecha:");
    labelFecha.setBounds(360, 10, 45, 20);

    cajaFecha = new JTextField(20);
    cajaFecha.setBounds(405, 10, 90, 20);

    boton = new JButton("Consultar");
    boton.addActionListener(this);
    boton.setBounds(505, 10, 150, 20);
    
    areaTexto = new JTextArea("");
    areaTexto.setBounds(10, 35, 670, 300);

    areaTexto.setLineWrap(true);
    areaTexto.setEditable(false);
    areaTexto.setWrapStyleWord(true);

    areaPuntuacion = new JTextField("0.0");
    areaPuntuacion.setBounds(10, 350, 100, 100);

    areaPuntuacion.setFont(new Font("Arial", Font.PLAIN, 30));
    areaPuntuacion.setHorizontalAlignment(JTextField.CENTER);
    areaPuntuacion.setEditable(false);


    
    
    JFrame f = new JFrame();

    f.setSize(700, 500);
    f.add(labelTexto);
    f.add(cajaTexto);
    f.add(labelFecha);
    f.add(cajaFecha);
    f.add(boton);
    f.add(areaTexto);
    f.add(areaPuntuacion);
    
    f.setLayout(null);
    f.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    if (e.getSource() == boton) {
      System.out.println("Se ha hecho clic en el botón");
      String text = "El " + cajaFecha.getText() + ", se dice que " + cajaTexto.getText();

      byte[] utf8Bytes = text.getBytes(Charset.forName("UTF-8"));

      String texto = new String(utf8Bytes, Charset.forName("UTF-8"));


      try {
        String respuesta = ChatGPT.chatGPT(texto);
        areaTexto.setText(respuesta);
        ChatGPT.analyze(respuesta);
    } catch (IOException e1) {
        e1.printStackTrace();
    }
    
    }
  }

  public static void colorTexto(String sentiment, String averageSentiment){
    if(sentiment == "Desmintiendo"){
      areaPuntuacion.setBackground(Color.RED);
    }else if(sentiment == "Neutral"){
      areaPuntuacion.setBackground(Color.LIGHT_GRAY);
    }else if(sentiment == "Corroborando"){
      areaPuntuacion.setBackground(Color.GREEN);
    }
    areaPuntuacion.setText(averageSentiment);
  }

  public static void main(String[] args) {
    new MiVentana();
  }

}