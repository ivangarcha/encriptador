/*
 * Copyright (c) 2020. Asesoría Telemática Canarias, S.L. Todos los derechos reservados.
 */

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;

import static com.sun.org.apache.xml.internal.security.utils.Base64.encode;

public class EncriptadorGUI extends JFrame{
    private JPanel mainPanel;
    private JTextField claveField;
    private JTextField palabraField;
    private JTextField resultadoField;
    private JLabel claveLabel;
    private JLabel palabraLabel;
    private JLabel resultadoLabel;
    private JButton encriptarButton;

    private static boolean checkValues(String key, String word){
        if(key.isEmpty() || word.isEmpty()){
            return false;
        }
        return true;
    }


    public EncriptadorGUI(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        claveField.setText("&1974Ds%t&M$rc2006=");
        encriptarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = claveField.getText();
                String word = palabraField.getText();

                if(checkValues(key, word)){
                    String resultado = null;
                    try {
                        resultado = encriptar(key, word);
                    } catch (Exception ex) {
                        System.out.println(ex);
                        ex.printStackTrace();
                    }
                    resultadoField.setText(resultado);
                } else {
                    resultadoField.setText("Todos los campos son requeridos");
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new EncriptadorGUI("Encriptador");
        frame.setSize(600,300);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static String encriptar (String key, String word) throws Exception{
        return encode(encrypt(key, word));
    }


    private static byte[] encrypt(String key, String valor) throws InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        try {
            byte[] initVector = new byte[]{0x10, 0x10, 0x01, 0x04, 0x01, 0x01, 0x01, 0x02};
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            AlgorithmParameterSpec algParamSpec = new IvParameterSpec(initVector);
            Cipher m_encrypter = Cipher.getInstance("DES/CBC/PKCS5Padding");
            m_encrypter.init(Cipher.ENCRYPT_MODE, secretKey, algParamSpec);
            byte[] inputBytes = valor.getBytes();
            return m_encrypter.doFinal(inputBytes);
        } catch (Exception ex) {
            System.out.print("Error al encriptar el valor:" + ex.toString());
            return null;
        }
    }

    private static String encode(byte[] str) {
        try {
            return DatatypeConverter.printBase64Binary(str);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}
