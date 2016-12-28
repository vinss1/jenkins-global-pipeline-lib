
def sendSlackMessage(message, color, channel, team) {
  def appendedmessage = """
${env.JOB_NAME} - #${env.BUILD_NUMBER}
${env.CHANGE_ID}
  ${env.CHANGE_AUTHOR}: ${env.CHANGE_TITLE}
${message}
(<${env.BUILD_URL}|Open>)
"""
  withCredentials([string(credentialsId: 'SlackToken', variable: 'SLACK_TOKEN')]) {
    slackSend channel: channel,
      color: color,
      message: appendedmessage,
      teamDomain: team,
      token: env.SLACK_TOKEN
  }
}