def call(region, remoteRepositoryPathAndImageName, localImageName, commitSha) {
  docker.withServer('tcp://localhost:2375') {
    withCredentials([[$class: 'StringBinding', credentialsId: AWS_ACCOUNT_ID, variable: 'AWS_ACCOUNT_ID'], [$class: 'UsernamePasswordMultiBinding', credentialsId: AWS_Key_and_Secret, passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID']]) {
      sh """
set +e
aws ecr describe-repositories --region $region --repository-names $remoteRepositoryPathAndImageName
create_result=\$?
set -e
if [ \$create_result != 0 ]; then
echo "Creating repository $remoteRepositoryPathAndImageName"
aws ecr create-repository --region $region --repository-name $remoteRepositoryPathAndImageName
fi
docker_login=\$(aws ecr get-login --region $region)
login_result=\$(\$docker_login)
latest_tag="${env.AWS_ACCOUNT_ID}.dkr.ecr.${region}.amazonaws.com/$remoteRepositoryPathAndImageName:latest"
sha_tag="${env.AWS_ACCOUNT_ID}.dkr.ecr.${region}.amazonaws.com/$remoteRepositoryPathAndImageName:$commitSha"
docker tag $localImageName:latest \$latest_tag
docker tag $localImageName:latest \$sha_tag
docker push \$latest_tag
docker push \$sha_tag

nildigests=\$(aws ecr list-images --region $region --repository-name "$remoteRepositoryPathAndImageName" | jq -r '.imageIds[] | select(has("imageTag") | not) | .imageDigest')
for i in \$nildigests; do
aws ecr batch-delete-image --region $region --repository-name "$remoteRepositoryPathAndImageName" --image-ids "imageDigest=\$i"
done
"""
    }
  }
}
