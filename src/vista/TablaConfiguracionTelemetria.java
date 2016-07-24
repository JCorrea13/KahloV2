package vista;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import kahlo_configuraciones.ConfiguracionSensor;

/**
 * Esta es la tabla de configuracion de telemetria.
 * @author Jose Correa
 * @version 1.0.0
 */
public class TablaConfiguracionTelemetria extends TableView {

    public TablaConfiguracionTelemetria(){
        TableColumn<ConfiguracionSensor, String> col_id = new TableColumn("Señal");
        col_id.setCellValueFactory(param -> param.getValue().id_sensor_propertyProperty());
        col_id.setCellFactory(TextFieldTableCell.forTableColumn());
        col_id.setPrefWidth(100);

        TableColumn<ConfiguracionSensor, String> col_etiqueta = new TableColumn("Etiqueta");
        col_etiqueta.setCellValueFactory(param -> param.getValue().etiqueta_sensor_propertyProperty());
        col_etiqueta.setCellFactory(TextFieldTableCell.forTableColumn());
        col_etiqueta.setPrefWidth(100);


        TableColumn<ConfiguracionSensor, Boolean> col_habilitado = new TableColumn<>("Habilitado");
        col_habilitado.setCellValueFactory( param -> param.getValue().habilitadoProperty() );
        col_habilitado.setCellFactory( CheckBoxTableCell.forTableColumn( col_habilitado ) );
        col_habilitado.setPrefWidth(65);

        TableColumn<ConfiguracionSensor, String> col_bytes = new TableColumn("Indice Bytes");
        col_bytes.setCellValueFactory(param -> param.getValue().bytes_propertyProperty());
        col_bytes.setCellFactory(TextFieldTableCell.forTableColumn());
        col_bytes.setPrefWidth(100);

        TableColumn<ConfiguracionSensor, String> col_decimales = new TableColumn("Digitos Decimales");
        col_decimales.setCellValueFactory(param -> param.getValue().decimales_propertyProperty());
        col_decimales.setCellFactory(TextFieldTableCell.forTableColumn());
        col_decimales.setPrefWidth(115);

        TableColumn<ConfiguracionSensor, String> col_max = new TableColumn("Valor Maximo");
        col_max.setCellValueFactory(param -> param.getValue().valor_max_propertyProperty());
        col_max.setCellFactory(TextFieldTableCell.forTableColumn());
        col_max.setPrefWidth(95);

        TableColumn<ConfiguracionSensor, String> col_min = new TableColumn("Valor Minimo");
        col_min.setCellValueFactory(param -> param.getValue().valor_min_propertyProperty());
        col_min.setCellFactory(TextFieldTableCell.forTableColumn());
        col_min.setPrefWidth(95);

        TableColumn<ConfiguracionSensor,Boolean>  col_grafica = new TableColumn("Grafica");
        col_grafica.setCellValueFactory( param -> param.getValue().graficaProperty() );
        col_grafica.setCellFactory( CheckBoxTableCell.forTableColumn( col_grafica ) );
        col_grafica.setPrefWidth(45);

        getColumns().addAll(col_id, col_etiqueta, col_habilitado, col_bytes, col_decimales, col_max, col_min, col_grafica);
    }


}
