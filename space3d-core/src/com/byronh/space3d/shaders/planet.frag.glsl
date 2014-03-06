#ifdef GL_ES 
precision mediump float;
#endif

uniform sampler2D u_diffuseTexture;

varying vec2 v_texCoord0;
varying vec3 v_normal;
varying vec3 v_viewDir;
 
void main() {

	float c = 0.25;
	float p = 2.5;

    vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
    
    float intensity = pow(dot(normalize(v_viewDir), normalize(v_normal)), 5.5);
		
	gl_FragColor = (vec4(1.0-color.r, 1.0-color.g, 1.0-color.b, 1.0) * intensity);// * texture2D(u_diffuseTexture, v_texCoord0);
}