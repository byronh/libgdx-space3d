package com.byronh.space3d;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thesecretpie.shader.test.ProcessorTest;

public class ProcessorTestDesktop {

	public static void main (String[] argv) {
	    LwjglApplicationConfiguration conf = new LwjglApplicationConfiguration();
	    conf.fullscreen = false;
	    conf.width = 800;
	    conf.height = 600;
	    conf.title = "ProcessorTest";
	    System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "false");
	    new LwjglApplication(new ProcessorTest(), conf);
	}
}
