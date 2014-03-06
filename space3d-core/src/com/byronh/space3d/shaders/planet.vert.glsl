attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
 
uniform mat4 u_worldTrans;
uniform mat4 u_viewTrans;
uniform mat4 u_projTrans;
uniform mat3 u_normalMatrix;
uniform vec3 u_viewPos;
 
varying vec3 v_normal;
varying vec3 v_viewDir;
varying vec2 v_texCoord0;
 
void main() {
	
	vec4 pos = u_worldTrans * vec4(a_position, 1.0);

    v_normal = u_normalMatrix * a_normal;
    v_viewDir = normalize(u_viewPos - pos.xyz);
	v_texCoord0 = a_texCoord0;
    
    gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}