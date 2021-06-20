package engine

// TODO implement methods

func ListTemplateFilesRecursively(templateName string) []string {
	return []string{}
}

func ReadTemplate(templateName string, path string) string {
	return ""
}

func CreateTargetDirectory(root string, path string) string {
	return ""
}

func CopyFile(root string, templateName string, templatePath string, targetPath string, overwrite bool) {

}

func WriteTargetFileContent(root string, path string, content string, overwrite bool) {

}

func ReadTargetFile(root string, path string) string {
	return ""
}

func DeleteFile(root string, path string, removeDirIfEmpty bool) bool {
	return false
}

func Exists(root string, path string) bool {
	return false
}
