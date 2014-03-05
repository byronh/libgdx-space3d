[VS]
#include "g_attributes.glsl:VS"
#include "u_uniforms.glsl"
#include "common.glsl:VS"

//attribute vec3 a_position;
//attribute vec3 a_normal;
//attribute vec2 a_texCoord0;
 
//uniform mat4 u_worldTrans;
//uniform mat4 u_projViewTrans;
 
//varying vec2 v_texCoord0;
 
void main() {
    v_texCoord0 = a_texCoord0;
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}

[FS]
#ifdef GL_ES 
precision mediump float;
#endif

#include "g_attributes.glsl:FS"
#include "u_uniforms.glsl"
#include "common.glsl:FS"
 
//varying vec2 v_texCoord0;
 
void main() {
	//float lerpValue = gl_
    gl_FragColor = vec4(v_texCoord0, 0.0, 0.5);
}
