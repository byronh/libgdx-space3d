#ifdef GL_ES 
precision mediump float;
#endif

//uniform vec3 u_lightDir;
uniform vec3 u_cameraDir;

varying float v_opacity;
varying vec3 v_normal;
//varying vec3 v_viewDir;
 
void main() {
	
	float intensity = pow(dot(normalize(u_cameraDir), normalize(v_normal)), 2.0);
	
	gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	gl_FragColor.a *= 1.0 - intensity;
	// * texture2D(u_diffuseTexture, v_texCoord0);
}