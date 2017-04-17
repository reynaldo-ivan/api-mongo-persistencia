package mx.com.anzen.mongo.services;
 
import java.util.Map;

import org.springframework.stereotype.Component;
 
/**
 * Interface que contiene la declaración de los metodos creados
 * @author anzen
 *
 */ 

public interface MongoService { 
	
	/**
	 * Metodo creado para realizar la inserción
	 * @param nombreTabla
	 * @param map
	 * @return
	 */
	public String alta(String nombreTabla, Map<String,Object> map);
	  
	/**
	 * Metodo creado para realizar la consulta a la collection mongo.
	 * @param nombreTabla
	 * @param map
	 * @return
	 */
	public Map<String,Object> consulta(String nombreTabla,Map<String,Object> map);
}
