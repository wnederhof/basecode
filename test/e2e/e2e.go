package e2e

import (
	"crudcodegen/internal/cli"
	"github.com/stretchr/testify/assert"
	"io/ioutil"
	"os"
	"os/exec"
	"testing"
)

func TestGeneratedCodeBuilds(t *testing.T) {
	testDir, err := ioutil.TempDir("", "ccg")
	if err != nil {
		assert.Fail(t, "Could not create temp dir.")
		return
	}
	oldDir, err := os.Getwd()
	if err != nil {
		assert.Fail(t, "Could not go into temp dir.")
		_ = os.RemoveAll(testDir)
		return
	}

	err = os.Chdir(testDir)
	if err != nil {
		assert.Fail(t, "Could not go into temp dir.")
		_ = os.RemoveAll(testDir)
		_ = os.Chdir(oldDir)
		return
	}

	_ = cli.Run([]string { "new", "com.wouter", "testapp" })

	_ = os.Chdir(testDir + "/testApp")

	_ = exec.Command("mvn clean install").Run()

	_ = os.Chdir(oldDir)
}