/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofinal;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Juan Javier
 */
public class ProyectoFinalController implements Initializable {
    
    @FXML
    private TextField txtArchivo;
    @FXML
    private TextField txtPalabra;
    @FXML
    private TextField txtPalabras;
    @FXML
    private Button buttonGenerar;
    @FXML
    private Button buttonBuscar1;
    @FXML
    private Button buttonBuscar2;
    @FXML
    private TextArea txtResultado;
    @FXML
    private void handleButtonGenAction(ActionEvent event) {
        try{
            ProyectoFinal.indiceInvertido(txtArchivo.getText());
            txtResultado.setText("Indice creado correctamente !!");
            buttonBuscar1.setDisable(false);
            buttonBuscar2.setDisable(false);
        }
        catch(Exception e){
            txtResultado.setText("Saltó una excepción al crear el índice.");
        }
    }
    @FXML
    private void handleButton1Action(ActionEvent event) {
        try{
            txtResultado.setText(txtPalabra.getText() + ": " + ProyectoFinal.getPalabra(txtPalabra.getText()));
        }
        catch(Exception e){
            txtResultado.setText("Saltó una excepción al llamar la función.");
        }
    }
    @FXML
    private void handleButton2Action(ActionEvent event) {
        try{
            String txtfld = txtPalabras.getText();
            String[] plbs = txtfld.split("[ ,.:;(){}\\[\\]\\n]+");
            
            txtResultado.setText(ProyectoFinal.getPalabras(plbs));
        }
        catch(Exception e){
            txtResultado.setText("Saltó una excepción al llamar la función.");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
