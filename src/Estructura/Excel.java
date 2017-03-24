package Estructura;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel {

    public Excel(JTable td) throws IOException{
        /*La ruta donde se creará el archivo*/
        String rutaArchivo = System.getProperty("user.home")+"/asigaciones-del-dia.xls";
        /*Se crea el objeto de tipo File con la ruta del archivo*/
        File archivoXLS = new File(rutaArchivo);
        /*Si el archivo existe se elimina*/
        if(archivoXLS.exists()) archivoXLS.delete();
        /*Se crea el archivo*/
        archivoXLS.createNewFile();
        
        /*Se crea el libro de excel usando el objeto de tipo Workbook*/
        Workbook libro = new HSSFWorkbook();
        /*Se inicializa el flujo de datos con el archivo xls*/
        FileOutputStream archivo = new FileOutputStream(archivoXLS);
        
        /*Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente*/
        Sheet hoja = libro.createSheet("Asignaciones del dia");
        //rocorremos el modelo de la tabla para obtener los datos
        for (int f=0; f <= td.getRowCount();f++)
            {
               Row fila = hoja.createRow(f);
               for (int c=0; c < td.getColumnCount();c++){
                   Cell celda = fila.createCell(c);
                   if(f!=0){
                    celda.setCellValue(td.getValueAt(f-1, c).toString());   
                   }else{
                    celda.setCellValue(td.getColumnName(c));
                   }                   
               }
            }
        /*Escribimos en el libro*/
        libro.write(archivo);
        /*Cerramos el flujo de datos*/
        archivo.close();
        /*Y abrimos el archivo con la clase Desktop*/
        Desktop.getDesktop().open(archivoXLS);
    }
    
}
