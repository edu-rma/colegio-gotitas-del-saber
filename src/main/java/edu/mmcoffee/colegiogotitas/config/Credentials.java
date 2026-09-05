
package main.java.edu.mmcoffee.colegiogotitas.config;

/**
 *
 * @author informatica
 */
public class Credentials {
    
public static final String DATA_BASE= System.getenv("DATA_BASE");
public static final String URL_DB= System.getenv("jdbc:mysql://localhost:3306")+DATA_BASE; 
public static final String USER_DB = System.getenv(".....");
public static final String PASS_DB = System.getenv(".......");
}

