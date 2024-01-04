#version 330 core

in vec3 out_color;
vec2 out_texture_coords;

// Uniforms:
uniform sampler2D texture1;
uniform sampler2D texture2;

// Output:
out vec4 fragColor;

void main(void) {
    fragColor = vec4(out_color, 1.0);
    // fragColor = mix(texture1, out_texture_coords) * vec4(out_color, 1.0);
}