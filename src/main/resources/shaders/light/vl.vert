#version 330
uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 cameraViewMatrix;

attribute vec2 textureAttribute;
attribute vec3 positionAttribute;
attribute vec3 normalAttribute;

out vec2 fragmentTextureAttribute;
out vec3 fragmentPositionAttribute;
out vec3 fragmentNormalAttribure;

void main() {
    fragmentTextureAttribute = textureAttribute;

    fragmentPositionAttribute = vec3(worldMatrix * vec4(positionAttribute, 1.0));
    fragmentNormalAttribure = mat3(transpose(inverse(worldMatrix))) * normalAttribute;

    gl_Position = projectionMatrix * cameraViewMatrix * worldMatrix * vec4(positionAttribute, 1.0);
}
