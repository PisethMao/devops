pipeline {
    agent any

    stages {
        stage('Telegram Message') {
            steps {
                script {
                    def token='telegram-token'
                    def chatId='chat-id'
                    sh """
                        curl -s -X POST "https://api.telegram.org/bot${token}/sendMessage" \
                        -d "chat_id=${chatId}" \
                        --data-urlencode "text=Hello from Jenkins!"
                    """
                }
            }
        }

        stage('Clone Reactjs Code') {
            steps {
                git 'https://github.com/PisethMao/reactjs-template-product'
            }
        }

        stage('Check Code Quality in SonarQube') {
            environment {
                scannerHome = tool 'sonarqube-scanner'
            }

            steps {
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'sonarqube-scanner') {
                    script {
                        def projectKey = 'reactjs-template-product'
                        def projectName = 'ReactjsTemplateProduct'
                        def projectVersion = '1.0.0'
                        sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=${projectKey} \
                            -Dsonar.projectName=${projectName} \
                            -Dsonar.projectVersion=${projectVersion}
                        """
                    }
                }
            }
        }

        stage('Wait for Quality Gate') {
            steps {
                script {
                    def qg = waitForQualityGate()
                    if (qg.status != 'OK') {
                        currentBuild.result = 'FAILURE'
                        echo "Quality gate failed: ${qg.status}. Stopping pipeline"
                        return
                    }
                    echo "Quality gate passed!"
                    currentBuild.result == 'SUCCESS'
                }
            }
        }

        stage('Build') {
            when {
                expression {
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo 'Building the docker image'
            }
        }

        stage('Push') {
            when {
                expression {
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo 'Pushing the docker image to registry'
            }
        }
    }
}
