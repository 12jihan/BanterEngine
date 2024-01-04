#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 mesh_color;

out vec3 color;

void main(void) {
    gl_Position = vec4(position, 1.0);
    color = mesh_color;
}