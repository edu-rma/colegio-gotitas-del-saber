/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.config;

/**
 *
 * @author informatica
 */
public class Credentials {
    
public static final String DATA_BASE = System.getenv("colegio_gotitas_del_saber_in4bm;");
public static final String URL_DB= System.getenv("jdbc:mysql://localhost:3306/")+DATA_BASE; 
public static final String USER_DB = System.getenv("IN4BM");
public static final String PASS_DB = System.getenv("$DmynM4A");
}
