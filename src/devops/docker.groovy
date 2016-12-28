package devops.docker

def dockerPlugin = new org.jenkinsci.plugins.docker.workflow.Docker()

def buildImage(directory, imageName) {
  dockerPlugin.withServer('tcp://localhost:2375') {
    dir(directory) {
      dockerPlugin.build imageName
    }
  }
}
