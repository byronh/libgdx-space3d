/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code)
 * Copyright © 2010 Thomas J. Hodge (java port of libnoise)
 * 
 * This file is part of libnoiseforjava.
 * 
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be found at 
 * http://libnoise.sourceforge.net/.  libnoise was developed by Jason Bevins, who may be 
 * contacted at jlbezigvins@gmzigail.com (for great email, take off every 'zig').
 * Porting to Java was done by Thomas Hodge, who may be contacted at
 * libnoisezagforjava@gzagmail.com (remove every 'zag').
 * 
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.byronh.space3d.util;

import com.badlogic.gdx.graphics.Color;


public class MiscUtilities {

	// Performs linear interpolation between two 8-bit channel values.
	public static short blendChannel(float r, float r2, float alpha) {
		double c0 = (float) r / 255.0;
		double c1 = (float) r2 / 255.0;
		return (short) (((c1 * alpha) + (c0 * (1.0f - alpha))) * 255.0f);
	}

	// Performs linear interpolation between two colors
	public static Color linearInterpColor(Color color0, Color color1, float alpha) {
		Color color = new Color(blendChannel(color0.r, color1.r, alpha), blendChannel(color0.g, color1.g, alpha), blendChannel(
				color0.b, color1.b, alpha), blendChannel(color0.a, color1.a, alpha));
		return color;
	}

}
