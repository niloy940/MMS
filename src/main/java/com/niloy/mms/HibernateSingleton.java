package com.niloy.mms;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSingleton {
    private static HibernateSingleton INSTANCE = new HibernateSingleton();
    private static SessionFactory sessionFactory;

    private HibernateSingleton(){
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
