#ifdef GL_ES 
precision mediump float;
#endif
 
uniform sampler2D u_diffuseTexture;

varying vec2 v_texCoord0;
 
void main() {
    vec4 tint = vec4(1.0, 0.0, 0.0, 1.0);
    gl_FragColor = tint * texture2D(u_diffuseTexture, v_texCoord0);
}