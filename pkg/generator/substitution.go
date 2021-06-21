package generator

import (
	"fmt"
	"github.com/flosch/pongo2/v4"
	"regexp"
)

var (
	regexPathParams       = regexp.MustCompile(`\[[a-zA-Z0-9]+\]`)
	regexPebFileExtension = regexp.MustCompile(`\.peb$`)
)

func substitutePathParamsAndRemovePeb(path string, context map[string]interface{}) string {
	pathWithPathParamsFilledIn := regexPathParams.ReplaceAllStringFunc(path, func(original string) string {
		found := original[1:(len(original) - 1)]
		return fmt.Sprintf("%v", context[found])
	})

	return regexPebFileExtension.ReplaceAllString(pathWithPathParamsFilledIn, "")
}

func substituteFile(template string, context map[string]interface{}) (string, error) {
	return pongo2.RenderTemplateString(template, context)
}