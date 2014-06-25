package com.conner.avoid.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Cache {

	private HashMap<String, Texture> textures;
	
	public Cache() {
		textures = new HashMap<String, Texture>();
	}
	
	public void loadTexture(String path, String key) {
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	public void disposeTexture(String key) {
		Texture tex = textures.get(key);
		if(tex != null) tex.dispose();
	}
	public void disposeAll() {
		for(Map.Entry<String, Texture> entry : textures.entrySet()) {
			if(entry.getKey() != null) {
				entry.getValue().dispose();
			}
		}
	}
}
