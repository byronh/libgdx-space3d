package com.byronh.space3d;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thesecretpie.shader.test.FramebufferTest;

public class FramebufferTestDesktop {

	public static void main (String[] argv) {
	    LwjglApplicationConfiguration conf = new LwjglApplicationConfiguration();
	    conf.fullscreen = false;
	    conf.width = 640;
	    conf.height = 480;
	    conf.title = "FramebufferTest";
	    System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "false");
	    new LwjglApplication(new FramebufferTest(), conf);
	}
}
