package game.components;

import engine.artemis.Component;

public class SpatialForm implements Component {
	private String spatialFormFile;

	public SpatialForm(String spatialFormFile) {
		this.spatialFormFile = spatialFormFile;
	}

	public String getSpatialFormFile() {
		return spatialFormFile;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
