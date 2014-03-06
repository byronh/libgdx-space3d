#ifdef GL_ES 
precision mediump float;
#endif
 
uniform sampler2D u_diffuseTexture;

varying vec2 v_texCoord0;
varying vec3 v_normal;
varying vec3 v_lightDir;
varying float v_intensity;
 
void main() {

    vec4 color;
    
	if (v_intensity > 0.95)
		color = vec4(1.0,0.5,0.5,1.0);
	else if (v_intensity > 0.5)
		color = vec4(0.6,0.3,0.3,1.0);
	else if (v_intensity > 0.25)
		color = vec4(0.4,0.2,0.2,1.0);
	else
		color = vec4(0.2,0.1,0.1,1.0);
		
	gl_FragColor = color;// * texture2D(u_diffuseTexture, v_texCoord0);
}