package templating

import (
	"fmt"
	"regexp"
)

var (
	regexPathParams = regexp.MustCompile(`\[[a-zA-Z0-9]+\]`)
)

func SubstitutePathParams(path string, context map[string]interface{}) string {
	return regexPathParams.ReplaceAllStringFunc(path, func(original string) string {
		found := original[1:(len(original) - 1)]
		return fmt.Sprintf("%v", context[found])
	})
}

func SubstituteFile(template string, context map[string]interface{}) string {
	// TODO
	return template
}