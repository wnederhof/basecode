package generator

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestSubstitutePath(t *testing.T) {
	m := make(map[string]interface{})
	m["artifactId"] = "welcome"
	// Peb is for Pebble, which was used in the old Kotlin code. In a later stage, the files should probably be renamed.
	assert.Equal(t, "/templates/welcome", substitutePathParamsAndRemovePeb("/templates/welcome.peb", m))
	assert.Equal(t, "/templates/welcome.kt", substitutePathParamsAndRemovePeb("/templates/welcome.kt", m))
	assert.Equal(t, "/templates/welcome", substitutePathParamsAndRemovePeb("/templates/welcome", m))
	assert.Equal(t, "/templates/welcome", substitutePathParamsAndRemovePeb("/templates/[artifactId]", m))
}
