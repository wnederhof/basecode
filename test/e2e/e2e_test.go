package e2e

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"github.com/wnederhof/basecode/internal/cli"
	"io/ioutil"
	"os"
	"os/exec"
	"testing"
)

func TestGeneratedCodeBuilds(t *testing.T) {
	testDir, err := ioutil.TempDir("", "basecode")
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

	_ = cli.Run([]string{"basecode", "new", "com.wouter", "testapp"})

	_ = os.Chdir(testDir + "/testapp")
	_ = cli.Run([]string{"basecode", "generate", "frontend"})
	_ = cli.Run([]string{"basecode", "generate", "scaffold", "Post", "title", "description:text"})
	_ = cli.Run([]string{"basecode", "generate", "scaffold", "Comment", "postId:Post", "author", "content:text"})
	_ = cli.Run([]string{"basecode", "generate", "auth"})

	_ = os.Chdir(testDir + "/testapp/testapp-server")
	cmd := exec.Command("mvn", "clean", "verify")

	stdout, err := cmd.CombinedOutput()

	fmt.Println(string(stdout))
	if err != nil {
		assert.Fail(t, "Could not execute maven clean verify.", err.Error())
		_ = os.RemoveAll(testDir)
		_ = os.Chdir(oldDir)
		return
	}

	_ = os.Chdir(testDir + "/testapp/testapp-web")
	_ = exec.Command("npm", "install").Run()
	cmd = exec.Command("npm", "run", "test")

	stdout, err = cmd.CombinedOutput()

	fmt.Println(string(stdout))
	if err != nil {
		assert.Fail(t, "Could not execute npm test."+err.Error())
		_ = os.RemoveAll(testDir)
		_ = os.Chdir(oldDir)
		return
	}

	_ = os.Chdir(oldDir)
	_ = os.RemoveAll(testDir)
}
