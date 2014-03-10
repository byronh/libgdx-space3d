#ifdef GL_ES 
precision mediump float;
#endif

uniform vec3 u_lightDir;

varying vec3 v_normal;
 
void main() {

    vec4 color;
    
    float intensity = dot(u_lightDir, normalize(v_normal));
    
	if (intensity > 0.95)
		color = vec4(1.0,0.5,0.5,1.0);
	else if (intensity > 0.5)
		color = vec4(0.6,0.3,0.3,1.0);
	else if (intensity > 0.25)
		color = vec4(0.4,0.2,0.2,1.0);
	else
		color = vec4(0.2,0.1,0.1,1.0);
		
	gl_FragColor = color;
	// * texture2D(u_diffuseTexture, v_texCoord0);
}