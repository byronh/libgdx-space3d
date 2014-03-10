attribute vec3 a_position;
attribute vec3 a_normal;
 
uniform mat4 u_worldTrans;
uniform mat4 u_viewTrans;
uniform mat4 u_projTrans;
uniform mat3 u_normalMatrix;
 
varying vec3 v_normal;
 
void main() {
	
	vec4 pos = u_worldTrans * vec4(a_position, 1.0);

    v_normal = u_normalMatrix * a_normal;
    
    gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}