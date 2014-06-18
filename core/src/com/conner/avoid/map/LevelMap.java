package com.conner.avoid.map;

import static com.conner.avoid.utils.B2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.conner.avoid.utils.B2DVars;


public class LevelMap {

	// World References
	public World world;
	
	// Map vars
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	
	public LevelMap(World world, String mapName) {
		this.world = world;
		
		tileMap = new TmxMapLoader().load("maps/" + mapName + ".tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		initTiles();
	}
	
	public void initTiles() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("map");
		
		int tilesize = tileMap.getProperties().get("tilewidth", Integer.class);

		for(int i = 0; i < layer.getHeight(); i++) {
			for(int j = 0; j < layer.getWidth(); j++) {
				Cell cell = layer.getCell(j, i);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				MapProperties props = cell.getTile().getProperties();
				
				if(props.get("blocking").equals("1")) {
					// Create the tile in the physical world
					bdef.type = BodyType.StaticBody;
					bdef.position.set(
							(j + 0.5f) * tilesize / PPM,
							(i + 0.5f) * tilesize / PPM
					);
					
					
					if(props.get("isblock").equals("0")) {
						ChainShape cs = new ChainShape();
						Vector2[] v = new Vector2[5];
						
						// Chain Shape Vectors
						v[0] = new Vector2(-tilesize / 2 / PPM, -tilesize / 2 / PPM);
						v[1] = new Vector2(-tilesize / 2 / PPM, tilesize / 2 / PPM);
						v[2] = new Vector2(tilesize / 2 / PPM, tilesize / 2 / PPM);
						v[3] = new Vector2(tilesize / 2 / PPM, -tilesize / 2 / PPM);
						v[4] = new Vector2(-tilesize / 2 / PPM, -tilesize / 2 / PPM);
						
						cs = new ChainShape();
						cs.createChain(v);
						fdef.friction = 0;
						fdef.shape = cs;
						fdef.filter.categoryBits = B2DVars.BIT_GROUND;
						fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_DEFLECT;
						world.createBody(bdef).createFixture(fdef);
						cs.dispose();
					}
					else if(props.get("isblock").equals("1")) {
						PolygonShape shape = new PolygonShape();
						shape.setAsBox(tilesize / 2 / PPM, tilesize / 2 / PPM);
						fdef.friction = 0;
						fdef.shape = shape;
						fdef.filter.categoryBits = B2DVars.BIT_GROUND;
						fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_DEFLECT;
						world.createBody(bdef).createFixture(fdef);
						shape.dispose();
					}
				}
			}
		}
	}

	public void render(OrthographicCamera camera) {
		tmr.setView(camera);
		tmr.render();
	}
}
