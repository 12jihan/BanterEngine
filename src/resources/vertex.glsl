#version 330 core

// VAO layouts:
layout(location = 0) in vec3 mesh_position;
layout(location = 1) in vec3 mesh_color;
layout(location = 2) in vec2 mesh_texture_coords;

// Outputs:
out vec3 out_color;
out vec2 out_texture_coords;

void main(void) {
    gl_Position = vec4(mesh_position, 1.0);
    out_color = mesh_color;
    out_texture_coords = mesh_texture_coords;
}