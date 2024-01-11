#version 410 core

// VAO layouts:
layout(location = 0) in vec3 mesh_position;
layout(location = 1) in vec3 mesh_color;
layout(location = 2) in vec2 mesh_texture_coords;

// Uniforms:
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

// Outputs:
out vec3 out_position;
out vec3 out_color;
out vec2 out_texture_coords;

void main(void) {
    
    // gl_Position = projection_matrix * model_matrix * vec4(mesh_position, 1.0);
    gl_Position = projection_matrix * view_matrix * model_matrix * vec4(mesh_position, 1.0);
    // gl_Position = vec4(mesh_position, 1);
    out_color = mesh_color;
    out_texture_coords = mesh_texture_coords;
}