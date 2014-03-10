#ifdef GL_ES 
precision mediump float;
#endif

uniform vec3 u_lightDir;

varying float v_opacity;
varying vec3 v_normal;
varying vec3 v_viewDir;
 
void main() {
	
	float intensity = pow(0.25 + dot(normalize(v_viewDir), normalize(v_normal)), -2.0);
	
	gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	gl_FragColor.a *= 0.5;
	gl_FragColor.rgb *= intensity;
	// * texture2D(u_diffuseTexture, v_texCoord0);
}