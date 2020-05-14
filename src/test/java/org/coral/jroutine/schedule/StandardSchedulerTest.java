package org.coral.jroutine.schedule;

import java.net.MalformedURLException;
import java.net.URL;

import org.coral.jroutine.Task;
import org.coral.jroutine.weave.AsmClassTransformer;
import org.coral.jroutine.weave.WeaverClassLoader;

import junit.framework.TestCase;

public class StandardSchedulerTest extends TestCase {

    private StandardScheduler scheduler;

    @Override
    protected void setUp() throws Exception {
        scheduler = new StandardScheduler();
        scheduler.start();
    }

    public void testSubmit() throws MalformedURLException, InterruptedException {
        WeaverClassLoader classLoader = new WeaverClassLoader(new URL[] {}, new AsmClassTransformer());
        try {
            Class<?> clazz = classLoader.loadClass("org.coral.jroutine.weave.rewrite.Loop");
            Task task = new Task((Runnable) clazz.newInstance());
            scheduler.submit(task);
            
            Thread.sleep(2000);
            System.out.println("suspend");
            task.suspend();
            Thread.sleep(2000);
            System.out.println("resume");
            task.resume();
            
            Thread.sleep(10000);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }

}
