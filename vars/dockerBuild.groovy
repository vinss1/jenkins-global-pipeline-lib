// package devops.docker

def call(directory, imageName) {
  docker.withServer('tcp://localhost:2375') {
    dir(directory) {
      docker.build imageName
    }
  }
}
