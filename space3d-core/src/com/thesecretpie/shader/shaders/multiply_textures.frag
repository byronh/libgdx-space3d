uniform sampler2D u_texture1, u_texture2;
varying vec2 v_pos;
varying vec2 v_texCoords;
varying vec4 v_color;

void main() {
    gl_FragColor = texture2D(u_texture1, v_texCoords) 
        * texture2D(u_texture2, v_texCoords)
        + texture2D(u_texture1, v_texCoords)*0.1 
        + texture2D(u_texture2, v_texCoords);
}