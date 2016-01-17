package com.ppm.javafx;

import com.ppm.Color;
import com.ppm.PPM;
import com.ppm.utils.Utils;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Display extends Application
{
	private static Canvas instance;
	public static synchronized void display(final PPM ppm)
	{
		if(instance != null)
			throw new IllegalStateException("display already called previously. Can only be called once");
		Utils.throwIAEIfNull(ppm, PPM.class, "ppm");
		final int width = ppm.getWidth();
		final int height = ppm.getHeight();
		final Canvas canvas = new Canvas(width, height);
		final PixelWriter writer = canvas.getGraphicsContext2D().getPixelWriter();
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				final Color c = ppm.getColor(x, y);
				writer.setColor(x, y, javafx.scene.paint.Color.color(c.getRed(), c.getGreen(), c.getBlue()));
			}
		}
		instance = canvas;
		launch(new String[0]);
	}
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		if(instance == null)
			throw new IllegalStateException("start called without static instance availabile. This should not be possible without reflection");
		Utils.throwIAEIfNull(primaryStage, Stage.class, "primaryStage");
		final Group root = new Group();
		root.getChildren().add(instance);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
