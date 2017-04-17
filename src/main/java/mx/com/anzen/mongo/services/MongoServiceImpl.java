package mx.com.anzen.mongo.services;
 
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import mx.com.anzen.mongo.config.ConfigBean; 
import mx.com.anzen.mongo.models.ConexionDataBean;
import mx.com.anzen.mongo.models.DataBean;
/**
 * Clase que contiene los metodos que realizan las insercion y la consulta a la base de datos.
 * @author anzen
 *
 */
@Repository
@Service
public class MongoServiceImpl implements MongoService{
 
	/**
	 * Inyection de ConexionBean para hacer uso de los valores de los atributos creados en el
	 * archivo properties.
	 */
	@Autowired
	private ConexionDataBean conexion;
	
	
	ApplicationContext appContext; 
	DataBean data=null; 
	
	/**
	 * Metodo que realiza el alta de datos hacia la base de datos configurada.
	 * @param nombreDB
	 * @param nombreTabla
	 * @param map
	 * @return
	 */
	public String alta(String nombreTabla, Map<String,Object> map){
		  
		ApplicationContext appContext;
		appContext=new AnnotationConfigApplicationContext(ConfigBean.class);
		DataBean data=(DataBean) appContext.getBean("iconexionbd");
		
        DB db=conexion().getDB(conexion.getDatabase());
        DBCollection table= db.getCollection(nombreTabla); 
  
        Iterator it = map.keySet().iterator();
		while(it.hasNext()){
		  Object key = it.next();
		  data.getObjectAlta().put(key.toString(), map.get(key));
		} 
		
		try{ 
			table.insert(data.getObjectAlta());
			((ConfigurableApplicationContext) appContext).close();
		}catch (Exception e) {
			return "Error: "+e.getMessage();
		}
		
		 return "OK";
	}
	
	/**
	 * Metodo que realiza la Consulta de datos de la base de datos configurada. 
	 * @param nombreTabla
	 * @param map
	 * @return
	 */
 
public Map<String,Object> consulta(String nombreTabla,Map<String,Object> map){
	 
		ApplicationContext appContext;
		appContext=new AnnotationConfigApplicationContext(ConfigBean.class); 
		DataBean data=(DataBean) appContext.getBean("iconexionbd");
		  
		DB db= conexion().getDB(conexion.getDatabase());  
        DBCollection table= db.getCollection(nombreTabla); 
        
        Map<String,Object> mapa= new HashMap<String, Object>();
        Map<String,Object> result= new HashMap<String, Object>();
        Iterator it = map.keySet().iterator();
		while(it.hasNext()){
		  Object key = it.next(); 
		  data.getObjectAlta().put(key.toString(), map.get(key));
		}
        DBCursor cursor; 
        try{  
        	cursor=table.find(data.getObjectAlta());  
            while(cursor.hasNext()) {
            	DBObject key = cursor.next();
            	String id= key.get("_id").toString();
            	Set<String> keyset=key.keySet();
            	for (String s: keyset){
            		if (!s.equals("_id")){  
                			mapa.put(s,key.get(s));
            		} 
            		result.put(id, mapa);
            	}
            }
            ((ConfigurableApplicationContext) appContext).close(); 
        	
        }catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
        
		 return result;
	}

	
	/**
	 * Clase que realiza la conexion a mongo.
	 * @return MongoClient
	 */
	
	public MongoClient conexion() { 
		
		 appContext=new AnnotationConfigApplicationContext(ConfigBean.class); 
		 data=(DataBean) appContext.getBean("iconexionbd");  
		 
		
			try {   
				data.setMongo(new MongoClient(conexion.getHost(),
						Integer.parseInt(conexion.getPuerto())));
	 
			} catch (UnknownHostException e) { 
				System.out.println("Error: "+e.getMessage());
			} catch (MongoException e) { 
				System.out.println("Error: "+e.getMessage());
			} catch (IOException e) { 
				System.out.println("Error: "+e.getMessage());
			}
			
			return data.getMongo();
	}
	
}
