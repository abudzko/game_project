#version 330
precision mediump float;
in vec2 fragmentTextureAttribute;

uniform sampler2D textureSampler;

void main() {
    gl_FragColor = texture(textureSampler, fragmentTextureAttribute);
}
