

class EnvironmentVariableSubstitutorTest {


    @Test
    void shouldNotBeVulnerableToCVE_2022_42889() {
        EnvironmentVariableSubstitutor substitutor = new EnvironmentVariableSubstitutor(false, false);
        assertThat(substitutor.replace("${script:javascript:3 + 4}")).isEqualTo("${script:javascript:3 + 4}");
    }
}
