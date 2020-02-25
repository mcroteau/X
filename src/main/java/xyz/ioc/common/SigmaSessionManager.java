package xyz.ioc.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SigmaSessionManager{

    public ConcurrentMap<String, Long> sessions = new ConcurrentHashMap<String, Long>();

}