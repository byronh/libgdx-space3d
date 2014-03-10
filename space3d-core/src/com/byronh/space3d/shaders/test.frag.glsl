#ifdef GL_ES 
precision mediump float;
#endif

uniform vec3 u_lightDir;
//uniform vec3 u_cameraDir;

varying float v_opacity;
varying vec3 v_normal;
varying vec3 v_viewDir;
 
void main() {
	
	float intensity1 = 1.0 - pow(dot(normalize(v_viewDir), normalize(v_normal)), 0.45);
	float intensity2 = 1.0 - dot(normalize(u_lightDir), normalize(v_normal));
	float intensity = smoothstep(0.0, 1.0, intensity1 * intensity2);
	
	gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	gl_FragColor.a *= intensity;
	// * texture2D(u_diffuseTexture, v_texCoord0);
}