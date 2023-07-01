

def installModule(extWorkspace) {
    stage("Build JAR file") {
        node {
            exws(extWorkspace) {
                withEnv(["JAVA_HOME=/usr/local/jdk-11.0.1", "SONAR=$SONAR"]) {
//                    sh '/usr/local/apache-maven-3.6.0/bin/mvn clean package sonar:sonar -Dsonar.host.url=$SONAR'
                    sh '/usr/local/apache-maven-3.6.0/bin/mvn clean package -DskipTests'
//                    sh '/usr/local/apache-maven-3.6.0/bin/mvn clean package'
                }
            }
        }
    }
}

def packageModule(extWorkspace) {
    node {
        exws(extWorkspace) {
            if ("${gitlabBranch}".toString() != "master") {
                sh "docker build -t ${REGISTRY}/${gitlabSourceRepoName}:${params.VERSION}-${gitlabBranch} ."
                sh "docker push ${REGISTRY}/${gitlabSourceRepoName}:${params.VERSION}-${gitlabBranch}"
            } else {
                sh "docker build -t ${REGISTRY}/${gitlabSourceRepoName}:${params.VERSION}.${env.BUILD_NUMBER} ."
                sh "docker push ${REGISTRY}/${gitlabSourceRepoName}:${params.VERSION}.${env.BUILD_NUMBER}"
            }
        }
    }
}

def buildPushCommit(extWorkspace) {
    updateGitlabCommitStatus name: 'build', state: 'running'
    node {
        exws(extWorkspace) {
            stage('Install') {
                installModule(extWorkspace)
            }
            stage('Package') {
                packageModule(extWorkspace)
            }
            stage('Clean up') {
                sh '/usr/local/apache-maven-3.6.0/bin/mvn clean'
            }
        }
    }
}

return this
