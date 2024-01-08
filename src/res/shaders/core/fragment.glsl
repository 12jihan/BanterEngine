#version 330 core

in vec3 out_color;
vec2 out_texture_coords;

// Uniforms:
uniform sampler2D texture0;
// uniform sampler2D texture2;

// Output:
out vec4 fragColor;

void main(void) {
    // fragColor = vec4(vec3(1.0, 0.0, 0.0), 1.0);
    fragColor = texture(texture0, out_texture_coords);
}