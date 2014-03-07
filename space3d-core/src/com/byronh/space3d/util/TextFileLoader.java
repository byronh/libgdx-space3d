package com.byronh.space3d.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;


/**
 * Loads text files using AssetManager.
 * 
 * @author Przemek Muller
 */
public class TextFileLoader extends SynchronousAssetLoader<String, TextFileLoader.TextFileParameter> {

	public TextFileLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public String load(AssetManager assetManager, String fileName, FileHandle file, TextFileParameter parameter) {
		String result = null;
		FileHandle fh = resolve(fileName);
		if (fh.exists()) {
			result = fh.readString("utf-8");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextFileParameter parameter) {
		return null;
	}

	static public class TextFileParameter extends AssetLoaderParameters<String> {
	}
}