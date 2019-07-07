#version 330
uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

attribute vec3 attributeLocation;
//attribute vec4 attributeColor;
attribute vec2 attributeTexture;

//layout(location = 0) in vec2 attributeLocation;
//layout(location = 1) in vec4 attributeColor;


//out vec4 vColor;

out vec2 pass_attributeTexture;

void main() {

mat4 m = mat4(
   1, 0, 0, 0, // first column (not row!)
   0, 2, 0, 0, // second column
   0, 0, 1, 0, // third column
   0, 0, 0, 1 // forth column
);

    gl_Position =  projectionMatrix * viewMatrix * worldMatrix * vec4(attributeLocation, 1);

    pass_attributeTexture = attributeTexture;
    //vColor = attributeColor;
    //v_vertex_color = vec4(1, 0, 0, 1);

}