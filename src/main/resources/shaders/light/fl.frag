#version 330
precision mediump float;
in vec2 fragmentTextureAttribute;
in vec3 fragmentPositionAttribute;
in vec3 fragmentNormalAttribure;

uniform sampler2D textureSampler;
uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform int isLight;
uniform vec3 cameraPosition;

vec3 phongShadingLighting(vec4 textureColor) {
    float ambientStrength = 0.2;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(fragmentNormalAttribure);
    vec3 lightDir = normalize(lightPosition - fragmentPositionAttribute);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    float specularStrength = 0.1;
    vec3 viewDir = normalize(cameraPosition - fragmentPositionAttribute);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 16);
    vec3 specular = specularStrength * spec * lightColor;
    vec3 result = (ambient + diffuse + specular) * textureColor.rgb;
    return result;
}

void main() {
    vec4 textureColor = texture(textureSampler, fragmentTextureAttribute);
    if (isLight == 1) {
        gl_FragColor = textureColor;
    } else {
        vec4 textureColor = texture(textureSampler, fragmentTextureAttribute);
        vec3 result = phongShadingLighting(textureColor);
        gl_FragColor = vec4(result, textureColor.a);
    }
}
