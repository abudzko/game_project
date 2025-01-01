#version 330
precision mediump float;
in vec2 fragmentTextureAttribute;
in vec3 fragmentPositionAttribute;
in vec3 fragmentNormalAttribure;

uniform sampler2D textureSampler;
uniform vec3 lightPosition;
uniform vec3 cameraPosition;

// Phong Shading
void main() {
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(fragmentNormalAttribure);
    if (length(norm) == 0.0) {
        norm = vec3(0.0, 0.0, 1.0);
    }
    vec3 lightDir = normalize(lightPosition - fragmentPositionAttribute);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    float specularStrength = 0.8;
    vec3 viewDir = normalize(cameraPosition - fragmentPositionAttribute);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 64);
    vec3 specular = specularStrength * spec * lightColor;

    vec4 textureColor = texture(textureSampler, fragmentTextureAttribute);
    vec3 result = (ambient + diffuse + specular) * textureColor.rgb;
    gl_FragColor = vec4(result, textureColor.a);
}
