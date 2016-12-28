package devops.docker

def buildImage(directory, imageName) {
  def dockerPlugin = new org.jenkinsci.plugins.docker.workflow.Docker()
  dockerPlugin.withServer('tcp://localhost:2375') {
    dir(directory) {
      dockerPlugin.build imageName
    }
  }
}
