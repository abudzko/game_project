precision mediump float;
//uniform vec4 u_Color;
//in vec4 vColor;
in vec2 pass_attributeTexture;

uniform sampler2D textureSampler;

void main() {
    //gl_FragColor = vColor;
    //gl_FragColor = vec4(1,0,0,1);
    gl_FragColor = texture(textureSampler, pass_attributeTexture);

}