#!groovy
package devops.git

def raiseOnCommitKeywords(keywords) {
  commitMessage = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
  for (i=0; i < keywords.size(); i++) {
    keyword = keywords[i]
    if (commitMessage.contains(keyword)) {
      error "git.raiseOnCommitKeywords"
    }
  }
}
