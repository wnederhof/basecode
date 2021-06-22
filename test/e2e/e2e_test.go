package e2e

import (
	"crudcodegen/internal/cli"
	"fmt"
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
	println("Created test dir: " + testDir)

	err = os.Chdir(testDir)
	if err != nil {
		assert.Fail(t, "Could not go into temp dir.")
		_ = os.RemoveAll(testDir)
		_ = os.Chdir(oldDir)
		return
	}

	_ = cli.Run([]string { "crudcodegen", "new", "com.wouter", "testapp" })

	_ = os.Chdir(testDir + "/testapp")
	_ = cli.Run([]string { "crudcodegen", "generate", "scaffold", "Employee", "name:string" })
	_ = os.Chdir(testDir + "/testapp/testapp-server")

	cmd := exec.Command("mvn", "clean", "verify")
	stdout, err := cmd.Output()

	fmt.Println(string(stdout))
	if err != nil {
		assert.Fail(t, "Could not execute maven clean verify.", err.Error());
		_ = os.RemoveAll(testDir)
		_ = os.Chdir(oldDir)
		return
	}

	_ = os.Chdir(oldDir)
	_ = os.RemoveAll(testDir)
}