
package vistacontrolador;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.AlumnoNoExistenteExcepcion;
import modelo.CorrectorProyectos;
import modelo.Proyecto;

// @author Adrian Romero Gorria
public class GuiCorrectorProyectos extends Application {

	private MenuItem itemLeer;
	private MenuItem itemGuardar;
	private MenuItem itemSalir;

	private TextField txtAlumno;
	private Button btnVerProyecto;

	private RadioButton rbtAprobados;
	private RadioButton rbtOrdenados;
	private Button btnMostrar;

	private TextArea areaTexto;

	private Button btnClear;
	private Button btnSalir;

	private CorrectorProyectos corrector; // el modelo

	@Override
	public void start(Stage stage) {

		corrector = new CorrectorProyectos();

		BorderPane root = crearGui();

		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.setTitle("- Corrector de proyectos -");
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		stage.show();
	}

	private BorderPane crearGui() {

		BorderPane panel = new BorderPane();
		MenuBar barraMenu = crearBarraMenu();
		panel.setTop(barraMenu);

		VBox panelPrincipal = crearPanelPrincipal();
		panel.setCenter(panelPrincipal);

		HBox panelBotones = crearPanelBotones();
		panel.setBottom(panelBotones);

		return panel;
	}

	private MenuBar crearBarraMenu() {

		MenuBar barraMenu = new MenuBar();

		Menu menu = new Menu("Archivo");

		itemLeer = new MenuItem("_Leer de fichero");
		itemLeer.setAccelerator(KeyCombination.keyCombination("CTRL+L"));
		itemLeer.setOnAction(event -> leerDeFichero());

		itemGuardar = new MenuItem("_Guardar");
		itemGuardar.setAccelerator(KeyCombination.keyCombination("CTRL+G"));
		itemGuardar.setDisable(true);

		itemSalir = new MenuItem("_Salir");
		itemSalir.setAccelerator(KeyCombination.keyCombination("CTRL+S"));
		itemSalir.setOnAction(event -> salir());

		menu.getItems().addAll(new SeparatorMenuItem(), itemGuardar, itemLeer, itemSalir);

		barraMenu.getMenus().add(menu);
		// a completar

		return barraMenu;
	}

	private VBox crearPanelPrincipal() {

		VBox panel = new VBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);

		Label lblEntrada = new Label("Panel de entrada");
		lblEntrada.setPrefWidth(Integer.MAX_VALUE);
		lblEntrada.getStyleClass().add("titulo-panel");

		crearPanelEntrada();
		crearPanelBotones();

		Label lblOpciones = new Label("Panel de opciones");
		lblOpciones.setPrefWidth(Integer.MAX_VALUE);
		lblOpciones.getStyleClass().add("titulo-panel");
		areaTexto = new TextArea();
		areaTexto.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);

		panel.getChildren().addAll(lblEntrada, crearPanelEntrada(), lblOpciones, crearPanelOpciones(), areaTexto);
		// a completar

		return panel;
	}

	private HBox crearPanelEntrada() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));

		Label lblAlumno = new Label("Alumno");

		txtAlumno = new TextField();
		txtAlumno.setPrefColumnCount(30);
		txtAlumno.setOnAction(event -> verProyecto());

		btnVerProyecto = new Button("Ver Proyecto");
		btnVerProyecto.setPrefWidth(120);
		btnVerProyecto.setOnAction(event -> verProyecto());

		panel.setSpacing(10);
		panel.getChildren().addAll(lblAlumno, txtAlumno, btnVerProyecto);

		// a completar

		return panel;
	}

	private HBox crearPanelOpciones() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));

		rbtAprobados = new RadioButton("Mostrar Aprobados");
		rbtAprobados.setSelected(true);

		rbtOrdenados = new RadioButton("Mostrar Ordenados");

		if (rbtOrdenados.isSelected()) {
			rbtAprobados.setSelected(false);
			rbtOrdenados.setSelected(true);
		} else {
			rbtOrdenados.setSelected(false);
			rbtAprobados.setSelected(true);
		}

		btnMostrar = new Button("Mostrar");
		btnMostrar.setOnAction(event -> mostrar());

		panel.setSpacing(50);
		panel.setAlignment(Pos.CENTER);
		panel.getChildren().addAll(rbtAprobados, rbtOrdenados, btnMostrar);
		// a completar

		return panel;
	}

	private HBox crearPanelBotones() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));

		btnClear = new Button("Clear");
		btnClear.setPrefWidth(90);
		btnClear.setOnAction(event -> clear());

		btnSalir = new Button("Salir");
		btnSalir.setPrefWidth(90);
		btnSalir.setOnAction(event -> salir());

		panel.setAlignment(Pos.CENTER_RIGHT);
		panel.setSpacing(10);
		panel.getChildren().addAll(btnClear, btnSalir);
		

		// a completar

		return panel;
	}

	private void salvarEnFichero() {
		try {

			corrector.guardarOrdenadosPorNota();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		itemGuardar.setDisable(false);
		itemLeer.setDisable(true);
		// a completar

	}

	private void leerDeFichero() {

		corrector.leerDatosProyectos();

		itemLeer.setOnAction(event -> mostrar());
		itemLeer.setDisable(true);
		itemGuardar.setDisable(false);
		// a completar

	}

	private void verProyecto() {
		
		if (itemGuardar.isDisable()) {
			areaTexto.setText("No ha leido el fichero.");
		} 
		else {
			String texto = txtAlumno.getText();
			if (texto.isEmpty()) {
				areaTexto.setText("No ha introducido ningun nombre");
			}
			else{
				try {
				areaTexto.setText(corrector.proyectoDe(texto).toString());
				}
				catch(AlumnoNoExistenteExcepcion ex) {
					Alert alerta = new Alert(Alert.AlertType.WARNING);
					alerta.setHeaderText(null);
					alerta.setContentText( ex.getMessage());
					alerta.showAndWait();
				}
			}
		}
		cogerFoco();
	}

	private void mostrar() {
		
		if (!itemLeer.isDisable()) {
			areaTexto.setText("No se ha leido de fichero");
		} 
		else {
			if (rbtAprobados.isSelected()) {
				String texto = String.valueOf(corrector.aprobados());

				areaTexto.setText(texto);
			}
			if (rbtOrdenados.isSelected()) {
				areaTexto.setText(corrector.ordenadosPorNota().toString());
			}
		}

		// a completar

	}

	private void cogerFoco() {

		txtAlumno.requestFocus();
		txtAlumno.selectAll();

	}

	private void salir() {

		System.exit(0);
	}

	private void clear() {

		areaTexto.clear();
		cogerFoco();
	}

	public static void main(String[] args) {

		launch(args);
	}
}
