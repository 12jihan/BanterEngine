#version 410 core

in vec3 out_color;
in vec2 out_texture_coords;

// Uniforms:
uniform sampler2D texture_sampler_0;

// Output:
out vec4 fragColor;

void main(void) {
    // void test = texture_sampler_0;
    // fragColor = vec4(vec3(1.0, 0.0, 0.0), 1.0);
    fragColor = texture(texture_sampler_0, out_texture_coords)
    // * vec4(out_color, 1)
    ;
}