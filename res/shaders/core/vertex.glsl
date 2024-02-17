#version 410 core

// VAO layouts:
layout(location = 0) in vec3 mesh_position;
layout(location = 1) in vec3 mesh_color;
layout(location = 2) in vec2 mesh_texture_coords;
layout(location = 3) in vec3 mesh_normal;
layout(location = 4) in vec3 mesh_tangent;
layout(location = 5) in vec3 mesh_bitangent;

// Uniforms:
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

// Outputs:
out vec3 out_position;
out vec3 out_color;
out vec2 out_texture_coords;
out vec3 out_normal;
out vec3 out_tangent;
out vec3 out_bitangent;

void main(void) {
    // Pre-math stuff:
    mat4 model_view_matrix = view_matrix * model_matrix;
    vec4 mv_position = model_view_matrix * vec4(mes_position, 1.0);

    // Vertex processing:
    gl_Position = projection_matrix * mv_position;
    
    // Basic texture outs:
    out_position = mv_position.xyz;
    out_color = mesh_color;
    out_texture_coords = mesh_texture_coords;

    // Complex texture outs:
    out_normal = normalize(model_view_matrix * vec4(mesh_normal, 0.0)).xyz;
    out_tangent = normalize(model_view_matrix * vec4(mesh_tangent, 0)).xyz;
    out_bitangent = normalize(model_view_matrix * vec4(mesh_bitangent, 0)).xyz;
}