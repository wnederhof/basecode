package templating

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestSubstitutePath(t *testing.T) {
	m := make(map[string]interface{})
	m["artifactId"] = "welcome"
	assert.Equal(t, "/templates/welcome", SubstitutePathParams("/templates/welcome", m))
	assert.Equal(t, "/templates/welcome", SubstitutePathParams("/templates/[artifactId]", m))
}
